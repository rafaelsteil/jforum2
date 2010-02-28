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
 * This file creating date: Feb 17, 2003 / 10:25:04 PM
 * The JForum Project
 * http://www.jforum.net 
 * 
 * $Id: User.java,v 1.22 2008/03/14 02:56:08 andowson Exp $
 */
package net.jforum.entities;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.jforum.SessionFacade;
import net.jforum.util.preferences.ConfigKeys;
import net.jforum.util.preferences.SystemGlobals;

/**
 * Represents a single user in the system.
 * An user is every person which uses the forum. Well,
 * every registered user. Anonymous users does not have
 * a specific ID, for example. This class contains all information
 * about some user configuration options and preferences.
 * 
 * @author Rafael Steil
 */
public class User implements Serializable
{
	private int id;
	private int themeId;
	private int level;
	private int totalPosts;
	private boolean attachSignatureEnabled = true;
	private int rankId = 1;
	private boolean htmlEnabled = true;
	private boolean bbCodeEnabled = true;
	private boolean smiliesEnabled = true;
	private boolean avatarEnabled = true;
	private boolean privateMessagesEnabled = true;
	private boolean viewOnlineEnabled = true;
	private boolean notifyPrivateMessagesEnabled = true;
	private boolean notifyOnMessagesEnabled = true;
	private boolean notifyAlways;
	private boolean notifyText;
	private String username;
	private String password;
	private Date lastVisit;
	private Date registrationDate;
	private String avatar;
	private boolean isExternalAvatar;
	private String email;
	private String icq;
	private String webSite;
	private String from;
	private String signature;
	private String aim;
	private String yim;
	private String msnm; 
	private String occupation;
	private String interests;
	private String biography;
	private String gender;
	private String timeZone;
	private String lang;
	private String dateFormat;
	private boolean viewEmailEnabled = true;
	private List groupsList;
	private int privateMessagesCount;
	private KarmaStatus karma;
	private int active;
	private String activationKey;
	private int deleted;
	private String firstName;
	private String lastName;
	private Map extra = new HashMap();
	
	public User(int userId)
	{
		this.id = userId;
	}
	
	/**
	 * Default Constructor
	 */
	public User() 
	{
		this.groupsList = new ArrayList(); 
	}
	
	public void addExtra(String name, Object value)
	{
		this.extra.put(name, value);
	}
	
	public Object getExtra(String name)
	{
		return this.extra.get(name);
	}
	
	public void setFirstName(String name)
	{
		this.firstName = name;
	}
	
	public String getFirstName()
	{
		return this.firstName;
	}
	
	public void setLastName(String name)
	{
		this.lastName = name;
	}
	
	public String getLastName()
	{
		return this.lastName;
	}
	
	public String getName()
	{
		return this.firstName + " " + this.lastName;
	}
	
	public boolean isDeleted() {
		return this.deleted == 1;
	}	
	
	public void setDeleted(int deleted){
		this.deleted = deleted;
	}	
	
	/**
	 * Gets the AIM identification
	 * 
	 * @return String with the AIM ID
	 */
	public String getAim() {
		return this.aim;
	}

	/**
	 * Gets the avatar of the user
	 * 
	 * @return String with the avatar
	 */
	public String getAvatar() {
		return this.avatar;
	}

	/**
	 * Checks if avatar is enabled
	 * 
	 * @return boolean value
	 */
	public boolean isAvatarEnabled() {
		return this.avatarEnabled;
	}

	/**
	 * Checks if BB code is enabled
	 * 
	 * @return boolean value
	 */
	public boolean isBbCodeEnabled() {
		return this.bbCodeEnabled;
	}

	/**
	 * Gets the format to represent dates and time
	 * 
	 * @return String with the format
	 */
	public String getDateFormat() {
		return this.dateFormat;
	}

	/**
	 * Gets the user email
	 * 
	 * @return String with the email
	 */
	public String getEmail() {
		return this.email;
	}

	/**
	 * Gets the user location ( where he lives )
	 * 
	 * @return String with the location
	 */
	public String getFrom() {
		return this.from;
	}

	/**
	 * Gets the user gender
	 * 
	 * @return String value. Possible values are <code>M</code> or <code>F</code>
	 */
	public String getGender() {
		return this.gender;
	}

	/**
	 * Checks if HTML code is enabled by default in user messages
	 * 
	 * @return boolean value
	 */
	public boolean isHtmlEnabled() {
		return this.htmlEnabled;
	}

	/**
	 * Gets the ICQ UIM
	 * 
	 * @return String with the UIN
	 */
	public String getIcq() {
		return this.icq;
	}

	/**
	 * Gets the user id
	 * 
	 * @return int value with the id
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Gets the user interests
	 * 
	 * @return String literal
	 */
	public String getInterests() {
		return this.interests;
	}

	/**
	 * Gets the user language
	 * 
	 * @return String value with the language chosen
	 */
	public String getLang() {
		return this.lang;
	}

	/**
	 * Gets the last visit time the user was in the forum
	 * 
	 * @return long value representing the time
	 */
	public Date getLastVisit() {
		return this.lastVisit;
	}

	/**
	 * Gets the user leve
	 * 
	 * @return int value with the level
	 */
	public int getLevel() {
		return this.level;
	}

	/**
	 * Checks if notification of new private messages is enabled
	 * 
	 * @return boolean value
	 */
	public boolean isNotifyPrivateMessagesEnabled() {
		return this.notifyPrivateMessagesEnabled;
	}

	/**
	 * Gets the OCC 
	 * 
	 * @return String
	 */
	public String getOccupation() {
		return this.occupation;
	}

	/**
	 * Gets the user password
	 * 
	 * @return String with the password ( in plain/text )
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * Checks if user permits other people to sent private messages to him
	 * 
	 * @return boolean value
	 */
	public boolean isPrivateMessagesEnabled() {
		return this.privateMessagesEnabled;
	}

	/**
	 * Gets the ranking ID of the user
	 * 
	 * @return int
	 */
	public int getRankId() {
		return this.rankId;
	}

	/**
	 * Gets the registration date of the user
	 * 
	 * @return String value with the registration date
	 */
	public String getRegistrationDate() 
	{
		SimpleDateFormat df = new SimpleDateFormat(SystemGlobals.getValue(ConfigKeys.DATE_TIME_FORMAT));

		return df.format(this.registrationDate);
	}

	/**
	 * Gets the user signature
	 * 
	 * @return String literal with the signature
	 */
	public String getSignature() {
		return this.signature;
	}

	/**
	 * Checks if smilies are enabled
	 * 
	 * @return boolean value
	 */
	public boolean isSmiliesEnabled() {
		return this.smiliesEnabled;
	}
	
	/**
	 * Gets the id of the theme chosen by the user
	 * 
	 * @return int value with the theme ID
	 */
	public int getThemeId() {
		return this.themeId;
	}

	/**
	 * Gets the timezone
	 * 
	 * @return String value with the timezone
	 */
	public String getTimeZone() {
		return this.timeZone;
	}

	/**
	 * Gets the total number of messages posted by the user
	 * 
	 * @return int value with the total of messages
	 */
	public int getTotalPosts() {
		return this.totalPosts;
	}

	/**
	 * Gets the username
	 * 
	 * @return String with the username
	 */
	public String getUsername() {
		return this.username;
	}

	/**
	 * Checks if the user permits other people to see he online
	 * 
	 * @return boolean value
	 */
	public boolean isViewOnlineEnabled() {
		return this.viewOnlineEnabled;
	}

	/**
	 * Gets the user website address
	 * 
	 * @return String with the URL
	 */
	public String getWebSite() {
		return this.webSite;
	}

	/**
	 * Gets the Yahoo messenger ID
	 * 
	 * @return String with the ID
	 */
	public String getYim() {
		return this.yim;
	}

	/**
	 * Is the user's email authenticated?
	 * 
	 * @return integer 1 if true
	 */	
	public boolean isActive(){
		return this.active == 1;
	}
	
	/**
	 * Gets the Yahoo messenger ID
	 * 
	 * @return String with the activation key that is created during user registration
	 */	
	public String getActivationKey(){
		return this.activationKey;
	}
	
	/**
	 * Sets the aim.
	 * 
	 * @param aim The aim ID to set
	 */
	public void setAim(String aim) {
		this.aim = aim;
	}

	/**
	 * Sets the avatar.
	 * 
	 * @param avatar The avatar to set
	 */
	public void setAvatar(String avatar) {
		this.avatar = avatar;
		
		if (avatar != null && avatar.toLowerCase().startsWith("http://")) {
			this.isExternalAvatar = true;
		}
	}

	/**
	 * Indicates if the avatar points to an external URL
	 * @return <code>true</code> if the avatar is some external image
	 */
	public boolean isExternalAvatar() {
		return this.isExternalAvatar;
	}

	/**
	 * Sets avatar status
	 * 
	 * @param avatarEnabled <code>true</code> or <code>false</code>
	 */
	public void setAvatarEnabled(boolean avatarEnabled) {
		this.avatarEnabled = avatarEnabled;
	}

	/**
	 * Sets the status for BB codes
	 * 
	 * @param bbCodeEnabled <code>true</code> or <code>false</code>
	 */
	public void setBbCodeEnabled(boolean bbCodeEnabled) {
		this.bbCodeEnabled = bbCodeEnabled;
	}

	/**
	 * Sets the date format.
	 * 
	 * @param dateFormat The date format to set
	 */
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	/**
	 * Sets the email.
	 * 
	 * @param email The email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Sets the user location ( where he lives )
	 * 
	 * @param from The location
	 */
	public void setFrom(String from) {
		this.from = from;
	}

	/**
	 * Sets the gender.
	 * 
	 * @param gender The gender to set. Possible values must be <code>M</code> or <code>F</code>
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}

	/**
	 * Enable or not HTML code into the messages
	 * 
	 * @param htmlEnabled <code>true</code> or <code>false</code>
	 */
	public void setHtmlEnabled(boolean htmlEnabled) {
		this.htmlEnabled = htmlEnabled;
	}

	/**
	 * Sets the icq UIN
	 * 
	 * @param icq The icq to set
	 */
	public void setIcq(String icq) {
		this.icq = icq;
	}

	/**
	 * Sets the user id.
	 * 
	 * @param id The user id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Sets the interests.
	 * 
	 * @param interests The interests to set
	 */
	public void setInterests(String interests) {
		this.interests = interests;
	}

	/**
	 * Sets the language.
	 * 
	 * @param lang The lang to set
	 */
	public void setLang(String lang) {
		this.lang = lang;
	}

	/**
	 * Sets the last visit time
	 * 
	 * @param lastVisit Last visit time, represented as a long value
	 */
	public void setLastVisit(Date lastVisit) {
		this.lastVisit = lastVisit;
	}

	/**
	 * Sets the level.
	 * 
	 * @param level The level to set
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	/**
	 * Sets the status for notification of new private messages
	 * 
	 * @param notifyPrivateMessagesEnabled <code>true</code> or <code>false</code>
	 */
	public void setNotifyPrivateMessagesEnabled(boolean notifyPrivateMessagesEnabled) {
		this.notifyPrivateMessagesEnabled = notifyPrivateMessagesEnabled;
	}

	/**
	 * Sets the occ.
	 * 
	 * @param occ The occ to set
	 */
	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	/**
	 * Sets the password.
	 * 
	 * @param password The password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Enable or not private messages to the user
	 * 
	 * @param privateMessagesEnabled <code>true</code> or <code>false</code>
	 */
	public void setPrivateMessagesEnabled(boolean privateMessagesEnabled) {
		this.privateMessagesEnabled = privateMessagesEnabled;
	}

	/**
	 * Sets the ranking id
	 * 
	 * @param rankId The id of the ranking
	 */
	public void setRankId(int rankId) {
		this.rankId = rankId;
	}

	/**
	 * Sets the registration date.
	 * 
	 * @param registrationDate The registration date to set
	 */
	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	/**
	 * Sets the signature.
	 * 
	 * @param signature The signature to set
	 */
	public void setSignature(String signature) {
		this.signature = signature;
	}

	/**
	 * Enable or not smilies in messages
	 * 
	 * @param smilesEnabled <code>true</code> or <code>false</code>
	 */
	public void setSmiliesEnabled(boolean smilesEnabled) {
		this.smiliesEnabled = smilesEnabled;
	}

	/**
	 * Sets the theme id
	 * 
	 * @param themeId The theme Id to set
	 */
	public void setThemeId(int themeId) {
		this.themeId = themeId;
	}

	/**
	 * Sets the Timezone.
	 * 
	 * @param timeZone The Timezone to set
	 */
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	/**
	 * Sets the total number of posts by the user
	 * 
	 * @param totalPosts int value with the total of messages posted by the user
	 */
	public void setTotalPosts(int totalPosts) {
		this.totalPosts = totalPosts;
	}

	/**
	 * Sets the username.
	 * 
	 * @param username The username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Sets the viewOnlineEnabled.
	 * @param viewOnlineEnabled The viewOnlineEnabled to set
	 */
	public void setViewOnlineEnabled(boolean viewOnlineEnabled) {
		this.viewOnlineEnabled = viewOnlineEnabled;
	}

	/**
	 * Sets the webSite.
	 * 
	 * @param webSite The webSite to set
	 */
	public void setWebSite(String webSite) {
		this.webSite = webSite;
	}

	/**
	 * Sets the Yahoo messenger ID
	 * 
	 * @param yim The yim to set
	 */
	public void setYim(String yim) {
		this.yim = yim;
	}

	/**
	 * @return
	 */
	public String getMsnm() {
		return this.msnm;
	}

	/**
	 * @param string
	 */
	public void setMsnm(String string) {
		this.msnm = string;
	}

	/**
	 * @return
	 */
	public boolean isNotifyOnMessagesEnabled() {
		return this.notifyOnMessagesEnabled;
	}

	/**
	 * @param b
	 */
	public void setNotifyOnMessagesEnabled(boolean b) {
		this.notifyOnMessagesEnabled = b;
	}

	/**
	 * @return
	 */
	public boolean isViewEmailEnabled() {
		return this.viewEmailEnabled;
	}

	/**
	 * @param b
	 */
	public void setViewEmailEnabled(boolean b) {
		this.viewEmailEnabled = b;
	}

	/**
	 * @return
	 */
	public boolean getAttachSignatureEnabled() {
		return this.attachSignatureEnabled;
	}

	/**
	 * @param b
	 */
	public void setAttachSignatureEnabled(boolean b) {
		this.attachSignatureEnabled = b;
	}

	/**
	 * @return
	 */
	public List getGroupsList() {
		return this.groupsList;
	}

	/**
	 * @return Returns the privateMessagesCount.
	 */
	public int getPrivateMessagesCount()
	{
		return this.privateMessagesCount;
	}
	/**
	 * @param privateMessagesCount The privateMessagesCount to set.
	 */
	public void setPrivateMessagesCount(int privateMessagesCount)
	{
		this.privateMessagesCount = privateMessagesCount;
	}
	/**
	 * @return Returns the hasPrivateMessages.
	 */
	public boolean hasPrivateMessages()
	{
		return this.privateMessagesCount > 0;
	}
	
	/**
	 * Set when user authenticates his email after user registration
	*/
	public void setActive(int active){
		this.active = active;
	}
	
	public void setActivationKey(String activationKey){
		this.activationKey = activationKey;
	}
	
	public void setKarma(KarmaStatus karma)
	{
		this.karma = karma;
	}
	
	public KarmaStatus getKarma()
	{
		return this.karma;
	}
	
	/**
	 * Is the user online?
	 * 
	 * @return true if user is in Session
	 */	
	public boolean isOnline()
	{
		return (SessionFacade.isUserInSession(this.id) != null);
	}

	/**
	 * Gets the user's biography
	 * @return the user biography
	 */
	public String getBiography() {
		return biography;
	}

	/**
	 * Sets the user's biography
	 * @param biography the user's biography
	 */
	public void setBiography(String biography) {
		this.biography = biography;
	}

	/**
	 * @return the notifyAlways
	 */
	public boolean notifyAlways()
	{
		return this.notifyAlways;
	}

	/**
	 * @return the notifyText
	 */
	public boolean notifyText()
	{
		return this.notifyText;
	}

	/**
	 * @param notifyAlways the notifyAlways to set
	 */
	public void setNotifyAlways(boolean notifyAlways)
	{
		this.notifyAlways = notifyAlways;
	}

	/**
	 * @param notifyText the notifyText to set
	 */
	public void setNotifyText(boolean notifyText)
	{
		this.notifyText = notifyText;
	}
}
