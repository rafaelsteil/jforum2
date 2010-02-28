/*
 * Copyright (c) JForum Team
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, 
 * with or without modification, are permitted provided 
 * that the following conditions are met:
 * 
 * 1) Redistributions of source code must retain the above 
 * copyright notice, this list of conditions and the 
 * following  disclaimer.
 * 2)  Redistributions in binary form must reproduce the 
 * above copyright notice, this list of conditions and 
 * the following disclaimer in the documentation and/or 
 * other materials provided with the distribution.
 * 3) Neither the name of "Rafael Steil" nor 
 * the names of its contributors may be used to endorse 
 * or promote products derived from this software without 
 * specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT 
 * HOLDERS AND CONTRIBUTORS "AS IS" AND ANY 
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, 
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF 
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL 
 * THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE 
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, 
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER 
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER 
 * IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN 
 * ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF 
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE
 * 
 * Created on Jan 18, 2005 3:08:48 PM
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.view.forum.common;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.jforum.SessionFacade;
import net.jforum.context.RequestContext;
import net.jforum.dao.AttachmentDAO;
import net.jforum.dao.DataAccessDriver;
import net.jforum.entities.Attachment;
import net.jforum.entities.AttachmentExtension;
import net.jforum.entities.AttachmentInfo;
import net.jforum.entities.Group;
import net.jforum.entities.Post;
import net.jforum.entities.QuotaLimit;
import net.jforum.entities.User;
import net.jforum.exceptions.AttachmentException;
import net.jforum.exceptions.AttachmentSizeTooBigException;
import net.jforum.exceptions.BadExtensionException;
import net.jforum.repository.SecurityRepository;
import net.jforum.security.SecurityConstants;
import net.jforum.util.I18n;
import net.jforum.util.MD5;
import net.jforum.util.image.ImageUtils;
import net.jforum.util.legacy.commons.fileupload.FileItem;
import net.jforum.util.preferences.ConfigKeys;
import net.jforum.util.preferences.SystemGlobals;

import org.apache.log4j.Logger;

/**
 * @author Rafael Steil
 * @version $Id: AttachmentCommon.java,v 1.36 2007/09/20 16:07:08 rafaelsteil Exp $
 */
public class AttachmentCommon
{
	private static Logger logger = Logger.getLogger(AttachmentCommon.class);
	private static final String DENY_ALL = "*";
	
	private RequestContext request;
	private AttachmentDAO am;
	private boolean canProceed;
	private Map filesToSave = new HashMap();
	
	public AttachmentCommon(RequestContext request, int forumId)
	{
		this.request = request;
		this.am = DataAccessDriver.getInstance().newAttachmentDAO();
		
		this.canProceed = SecurityRepository.canAccess(SecurityConstants.PERM_ATTACHMENTS_ENABLED, 
			Integer.toString(forumId));
	}
	
	public void preProcess()
	{
		if (!this.canProceed) {
			return;
		}
		
		String t = this.request.getParameter("total_files");
		
		if (t == null || "".equals(t)) {
			return;
		}
		
		int total = Integer.parseInt(t);
		
		if (total < 1) {
			return;
		}
		
		if (total > SystemGlobals.getIntValue(ConfigKeys.ATTACHMENTS_MAX_POST)) {
			total = SystemGlobals.getIntValue(ConfigKeys.ATTACHMENTS_MAX_POST);
		}

		long totalSize = 0;
		int userId = SessionFacade.getUserSession().getUserId();
		Map extensions = this.am.extensionsForSecurity();
		
		for (int i = 0; i < total; i++) {
			FileItem item = (FileItem)this.request.getObjectParameter("file_" + i);
			
			if (item == null) {
				continue;
			}

			if (item.getName().indexOf('\000') > -1) {
				logger.warn("Possible bad attachment (null char): " + item.getName()
					+ " - user_id: " + SessionFacade.getUserSession().getUserId());
				continue;
			}
			
			UploadUtils uploadUtils = new UploadUtils(item);

			// Check if the extension is allowed
			boolean containsExtension = extensions.containsKey(uploadUtils.getExtension());
			boolean denyAll = extensions.containsKey(DENY_ALL);

			boolean isAllowed = (!denyAll && !containsExtension)
				|| (containsExtension && extensions.get(uploadUtils.getExtension()).equals(Boolean.TRUE));

			if (!isAllowed) { 
				throw new BadExtensionException(I18n.getMessage("Attachments.badExtension", 
					new String[] { uploadUtils.getExtension() }));
			}

			// Check comment length:
			String comment = this.request.getParameter("comment_" + i);
			if (comment.length() > 254) {
				throw new AttachmentException("Comment too long.");
			}
			
			Attachment a = new Attachment();
			a.setUserId(userId);
			
			AttachmentInfo info = new AttachmentInfo();
			info.setFilesize(item.getSize());
			info.setComment(comment);
			info.setMimetype(item.getContentType());
			
			// Get only the filename, without the path (IE does that)
			String realName = this.stripPath(item.getName());
			
			info.setRealFilename(realName);
			info.setUploadTimeInMillis(System.currentTimeMillis());
			
			AttachmentExtension ext = this.am.selectExtension(uploadUtils.getExtension().toLowerCase());
			if (ext.isUnknown()) {
				ext.setExtension(uploadUtils.getExtension());
			}
			
			info.setExtension(ext);
			String savePath = this.makeStoreFilename(info);
			info.setPhysicalFilename(savePath);
			
			a.setInfo(info);
			filesToSave.put(uploadUtils, a);
			
			totalSize += item.getSize();
		}
		
		// Check upload limits
		QuotaLimit ql = this.getQuotaLimit(userId);
		if (ql != null) {
			if (ql.exceedsQuota(totalSize)) {
				throw new AttachmentSizeTooBigException(I18n.getMessage("Attachments.tooBig", 
					new Integer[] { new Integer(ql.getSizeInBytes() / 1024), 
						new Integer((int)totalSize / 1024) }));
			}
		}
	}

	/**
	 * @param realName String
	 * @return String
	 */
	public String stripPath(String realName)
	{
		String separator = "/";
		int index = realName.lastIndexOf(separator);
		
		if (index == -1) {
			separator = "\\";
			index = realName.lastIndexOf(separator);
		}
		
		if (index > -1) {
			realName = realName.substring(index + 1);
		}
		
		return realName;
	}
	
	public void insertAttachments(Post post)
	{
		if (!this.canProceed) {
			return;
		}
		
		post.hasAttachments(this.filesToSave.size() > 0);
		
		for (Iterator iter = this.filesToSave.entrySet().iterator(); iter.hasNext(); ) {
			Map.Entry entry = (Map.Entry)iter.next();
			Attachment a = (Attachment)entry.getValue();
			a.setPostId(post.getId());
			
			String path = SystemGlobals.getValue(ConfigKeys.ATTACHMENTS_STORE_DIR) 
				+ "/" 
				+ a.getInfo().getPhysicalFilename();
			
			this.am.addAttachment(a);
			((UploadUtils)entry.getKey()).saveUploadedFile(path);
			
			if (this.shouldCreateThumb(a)) {
				this.createSaveThumb(path);
			}
		}
	}
	
	private boolean shouldCreateThumb(Attachment a) {
		String extension = a.getInfo().getExtension().getExtension().toLowerCase();
		
		return SystemGlobals.getBoolValue(ConfigKeys.ATTACHMENTS_IMAGES_CREATE_THUMB)
			&& ("jpg".equals(extension) || "jpeg".equals(extension) 
				|| "gif".equals(extension) || "png".equals(extension));
	}
	
	private void createSaveThumb(String path) {
		try {
			BufferedImage image = ImageUtils.resizeImage(path, ImageUtils.IMAGE_JPEG, 
				SystemGlobals.getIntValue(ConfigKeys.ATTACHMENTS_IMAGES_MAX_THUMB_W),
				SystemGlobals.getIntValue(ConfigKeys.ATTACHMENTS_IMAGES_MAX_THUMB_H));
			ImageUtils.saveImage(image, path + "_thumb", ImageUtils.IMAGE_JPEG);
		}
		catch (Exception e) {
			logger.error(e.toString(), e);
		}
	}
	
	public QuotaLimit getQuotaLimit(int userId)
	{
		QuotaLimit ql = new QuotaLimit();
		User u = DataAccessDriver.getInstance().newUserDAO().selectById(userId);
		
		for (Iterator iter = u.getGroupsList().iterator(); iter.hasNext();) {
			QuotaLimit l = this.am.selectQuotaLimitByGroup(((Group)iter.next()).getId());
			if (l == null) {
				continue;
			}
			
			if (l.getSizeInBytes() > ql.getSizeInBytes()) {
				ql = l;
			}
		}
		
		if (ql.getSize() == 0) {
			return null;
		}
		
		return ql;
	}
	
	public void editAttachments(int postId, int forumId)
	{
		// Allow removing the attachments at least
		AttachmentDAO am = DataAccessDriver.getInstance().newAttachmentDAO();
		
		// Check for attachments to remove
		List deleteList = new ArrayList();
		String[] delete = null;
		String s = this.request.getParameter("delete_attach");
		
		if (s != null) {
			delete = s.split(",");
		}
		
		if (delete != null) {
			for (int i = 0; i < delete.length; i++) {
				if (delete[i] != null && !delete[i].equals("")) {
					int id = Integer.parseInt(delete[i]);
					Attachment a = am.selectAttachmentById(id);
					
					am.removeAttachment(id, postId);
					
					String filename = SystemGlobals.getValue(ConfigKeys.ATTACHMENTS_STORE_DIR)
						+ "/" + a.getInfo().getPhysicalFilename();
					
					File f = new File(filename);
					
					if (f.exists()) {
						f.delete();
					}
					
					// Check if we have a thumb to delete
					f = new File(filename + "_thumb");
					
					if (f.exists()) {
						f.delete();
					}
				}
			}
			
			deleteList = Arrays.asList(delete);
		}
		
		if (!SecurityRepository.canAccess(SecurityConstants.PERM_ATTACHMENTS_ENABLED, 
				Integer.toString(forumId))
				&& !SecurityRepository.canAccess(SecurityConstants.PERM_ATTACHMENTS_DOWNLOAD)) {
			return;
		}
		
		// Update
		String[] attachIds = null;
		s = this.request.getParameter("edit_attach_ids");
		if (s != null) {
			attachIds = s.split(",");
		}
		
		if (attachIds != null) {
			for (int i = 0; i < attachIds.length; i++) {
				if (deleteList.contains(attachIds[i]) 
						|| attachIds[i] == null || attachIds[i].equals("")) {
					continue;
				}
				
				int id = Integer.parseInt(attachIds[i]);
				Attachment a = am.selectAttachmentById(id);
				a.getInfo().setComment(this.request.getParameter("edit_comment_" + id));

				am.updateAttachment(a);
			}
		}
	}
	
	private String makeStoreFilename(AttachmentInfo a)
	{
		Calendar c = new GregorianCalendar();
		c.setTimeInMillis(System.currentTimeMillis());
		c.get(Calendar.YEAR);
		
		int year = Calendar.getInstance().get(Calendar.YEAR);
		int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
		int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		
		StringBuffer dir = new StringBuffer(256);
		dir.append(year).append('/').append(month).append('/').append(day).append('/');
		
		new File(SystemGlobals.getValue(ConfigKeys.ATTACHMENTS_STORE_DIR) + "/" + dir).mkdirs();
		
		return dir
			.append(MD5.crypt(a.getRealFilename() + System.currentTimeMillis()))
			.append('_')
			.append(SessionFacade.getUserSession().getUserId())
			.append('.')
			.append(a.getExtension().getExtension())
			.append('_')
			.toString();
	}
	
	public List getAttachments(int postId, int forumId)
	{
		if (!SecurityRepository.canAccess(SecurityConstants.PERM_ATTACHMENTS_DOWNLOAD)
				&& !SecurityRepository.canAccess(SecurityConstants.PERM_ATTACHMENTS_ENABLED, 
						Integer.toString(forumId))) {
			return new ArrayList();
		}
		
		return this.am.selectAttachments(postId);
	}
	
	public boolean isPhysicalDownloadMode(int extensionGroupId) 
	{
		return this.am.isPhysicalDownloadMode(extensionGroupId);
	}

	public void deleteAttachments(int postId, int forumId) 
	{
		// Attachments
		List attachments = DataAccessDriver.getInstance().newAttachmentDAO().selectAttachments(postId);
		StringBuffer attachIds = new StringBuffer();
		
		for (Iterator iter = attachments.iterator(); iter.hasNext(); ) {
			Attachment a = (Attachment)iter.next();
			attachIds.append(a.getId()).append(',');
		}
		
		this.request.addOrReplaceParameter("delete_attach", attachIds.toString());
		this.editAttachments(postId, forumId);
	}
}
