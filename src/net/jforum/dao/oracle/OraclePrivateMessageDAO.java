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
 * Created on 24/05/2004 01:07:39
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.dao.oracle;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.jforum.JForumExecutionContext;
import net.jforum.dao.generic.GenericPrivateMessageDAO;
import net.jforum.entities.PrivateMessage;
import net.jforum.util.preferences.SystemGlobals;

/**
 * @author Dmitriy Kiriy
 * @version $Id: OraclePrivateMessageDAO.java,v 1.10 2007/08/31 22:56:40 rafaelsteil Exp $
 */
public class OraclePrivateMessageDAO extends GenericPrivateMessageDAO
{
	/**
	 * @see net.jforum.dao.generic.GenericPrivateMessageDAO#addPmText(net.jforum.entities.PrivateMessage)
	 */
	protected void addPmText(PrivateMessage pm) throws Exception
    {
		PreparedStatement p = JForumExecutionContext.getConnection().prepareStatement(
				SystemGlobals.getSql("PrivateMessagesModel.addText"));
		p.setInt(1, pm.getId());
		p.executeUpdate();
		p.close();
		
		OracleUtils.writeBlobUTF16BinaryStream(SystemGlobals.getSql("PrivateMessagesModel.addTextField"), 
			pm.getId(), pm.getPost().getText());
	}
	
	/**
	 * @see net.jforum.dao.generic.GenericPrivateMessageDAO#getPmText(java.sql.ResultSet)
	 */
	protected String getPmText(ResultSet rs) throws SQLException
	{
		return OracleUtils.readBlobUTF16BinaryStream(rs, "privmsgs_text");
	}
}
