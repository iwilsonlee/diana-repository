/*
 * Copyright (c) CMWEBGAME Team
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
 * This file creation date: 30/12/2003 / 21:40:54
 * The CMWEBGAME Project
 * http://www.cmwebgame.com
 */
package com.cmwebgame.entities;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.Date;

import com.cmwebgame.GPortalExecutionContext;
import com.cmwebgame.repository.MemberSessionFacade;
import com.cmwebgame.util.Captcha;
import com.cmwebgame.util.preferences.ConfigKeys;
import com.cmwebgame.util.preferences.SystemGlobals;
import com.octo.captcha.image.ImageCaptcha;

/**
 * Stores information about member's session.
 * 
 * @author wilson 
 */
public class MemberSession implements Serializable
{
	static final long serialVersionUID = 0;
	
	private long sessionTime;
	
	private Long memberId;
	
	private Date startTime;
	private Date lastVisit;
	
	private String sessionId;
	private String username;
	private String siteUid;
	private int isFacebookAccount = Member.FACEBOOK_ACCOUNT_NO;
	private String password;
	private String email;
	private String nickname;
	private String ip;
	
    private int experience;  //个人经验，每日首次登入得5經驗
	
	private int amount;  //個人成功儲值總數量
	
	private int restAmount;  //當前剩余育駿幣數量
	
	private int score;   //個人積分總數
	
	private String avatar;
	
	private String avatarTmp; //臨時頭像
	
	private int avatarStatus;
	
	private boolean autoLogin;
	
	private ImageCaptcha imageCaptcha = null;

	private boolean vip;

	private int buType; //平台類型 - 0:育駿, 1:可可平台

	public MemberSession() {}

	public MemberSession(MemberSession ms)
	{
		if (ms.getStartTime() != null) {
			this.startTime = new Date(ms.getStartTime().getTime());
		}

		if (ms.getLastVisit() != null) {
			this.lastVisit = new Date(ms.getLastVisit().getTime());
		}
		
		this.sessionTime = ms.getSessionTime();
		this.memberId = ms.getMemberId();
		this.sessionId = ms.getSessionId();
		this.username = ms.getUsername();
		this.autoLogin = ms.getAutoLogin();
		this.email = ms.getEmail();
		this.nickname = ms.getNickname();
		this.password = ms.getPassword();
		this.imageCaptcha = ms.imageCaptcha;
		this.ip = ms.getIp();
		this.experience = ms.getExperience();
		this.amount = ms.getAmount();
		this.restAmount = ms.getRestAmount();
		this.score = ms.getScore();
		this.avatar = ms.getAvatar();
		this.avatarTmp = ms.getAvatarTmp();
		this.avatarStatus = ms.getAvatarStatus();
		this.siteUid = ms.getSiteUid();
		this.isFacebookAccount = ms.getIsFacebookAccount();
		this.vip = ms.isVip();
		this.buType = ms.getBuType();
	}
	
	public Date sessionLastUpdate()
	{
		return new Date(this.startTime.getTime() + this.sessionTime);
	}
	
	public ImageCaptcha getImageCaptcha() {
		return imageCaptcha;
	}

	public void setImageCaptcha(ImageCaptcha imageCaptcha) {
		this.imageCaptcha = imageCaptcha;
	}

	public String getSiteUid() {
		return siteUid;
	}

	public void setSiteUid(String siteUid) {
		this.siteUid = siteUid;
	}

	public int getIsFacebookAccount() {
		return isFacebookAccount;
	}

	public void setIsFacebookAccount(int isFacebookAccount) {
		this.isFacebookAccount = isFacebookAccount;
	}

	public void setIp(String ip)
	{
		this.ip = ip;
	}
	
	public String getIp()
	{
		return this.ip;
	}

	/**
	 * Set session's start time.
	 * 
	 * @param startTime  Start time in miliseconds
	 */
	public void setStartTime(Date startTime)
	{
		this.startTime = startTime;
	}


	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getExperience() {
		return experience;
	}

	public void setExperience(int experience) {
		this.experience = experience;
	}

	public int getRestAmount() {
		return restAmount;
	}

	public void setRestAmount(int restAmount) {
		this.restAmount = restAmount;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getAvatarTmp() {
		return avatarTmp;
	}

	public void setAvatarTmp(String avatarTmp) {
		this.avatarTmp = avatarTmp;
	}

	public int getAvatarStatus() {
		return avatarStatus;
	}

	public void setAvatarStatus(int avatarStatus) {
		this.avatarStatus = avatarStatus;
	}

	/**
	 * Set session last visit time.
	 * 
	 * @param lastVisit Time in miliseconds
	 */
	public void setLastVisit(Date lastVisit)
	{
		this.lastVisit = lastVisit;
	}


	/**
	 * Set user's name
	 * 
	 * @param username The username
	 */
	public void setUsername(String username)
	{
		this.username = username;
	}

	public void setSessionId(String sessionId)
	{
		this.sessionId = sessionId;
	}

	public void setSessionTime(long sessionTime)
	{
		this.sessionTime = sessionTime;
	}


	/**
	 * Update the session time.
	 */
	public void updateSessionTime()
	{
		this.sessionTime = System.currentTimeMillis() - this.startTime.getTime();
	}

	/**
	 * Enable or disable auto-login.
	 * 
	 * @param autoLogin  <code>true</code> or <code>false</code> to represent auto-login status
	 */
	public void setAutoLogin(boolean autoLogin)
	{
		this.autoLogin = autoLogin;
	}

	public void setVip(boolean vip) {
		this.vip = vip;
	}

	public void setBuType(int buType) {
		this.buType = buType;
	}

	/**
	 * Gets user's session start time
	 * 
	 * @return Start time in miliseconds
	 */
	public Date getStartTime()
	{
		return this.startTime;
	}


	/**
	 * Gets user's last visit time
	 * 
	 * @return Time in miliseconds
	 */
	public Date getLastVisit()
	{
		//return new GregorianCalendar(2007, 6, 28, 15, 15, 19).getTime();
		return this.lastVisit;
	}

	/**
	 * Gets the session time.
	 * 
	 * @return The session time
	 */
	public long getSessionTime()
	{
		return this.sessionTime;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId (Long memberId) {
		this.memberId = memberId;
	}

	/**
	 * Gets the username
	 * 
	 * @return The username
	 */
	public String getUsername()
	{
//		if (this.username == null && this.memberId == SystemGlobals.getIntValue(ConfigKeys.ANONYMOUS_USER_ID)) {
//			this.username = I18n.getMessage("Guest");
//		}
		
		return this.username;
	}

	/**
	 * Gets auto-login status
	 * 
	 * @return <code>true</code> if auto-login is enabled, or <code>false</code> if disabled.
	 */
	public boolean getAutoLogin()
	{
		return this.autoLogin;
	}

	public boolean isVip() {
		return vip;
	}

	public int getBuType() {
		return buType;
	}

	/**
	 * Gets the session id related to this user session
	 * 
	 * @return A string with the session id
	 */
	public String getSessionId()
	{
		return this.sessionId;
	}

	/**
	 * Checks if the user is an administrator
	 * 
	 * @return <code>true</code> if the user is an administrator
	 */
	public boolean isAdmin()
	{
		return false;
//		return SecurityRepository.canAccess(this.memberId.intValue(), SecurityConstants.PERM_ADMINISTRATION);
	}

	/**
	 * Checks if the user is a moderator
	 * 
	 * @return <code>true</code> if the user has moderations rights
	 */
	public boolean isModerator()
	{
		return false;
//		return SecurityRepository.canAccess(this.memberId.intValue(), SecurityConstants.PERM_MODERATION);
	}
	
	/**
	 * Checks if the user can moderate a forum
	 * 
	 * @param forumId the forum's id to check for moderation rights
	 * @return <code>true</code> if the user has moderations rights
	 */
	public boolean isModerator(int forumId)
	{
//		PermissionControl pc = SecurityRepository.get(this.memberId.intValue());
		
//		return (pc.canAccess(SecurityConstants.PERM_MODERATION))
//			&& (pc.canAccess(SecurityConstants.PERM_MODERATION_FORUMS, 
//				Integer.toString(forumId)));
		return true;
	}

	/**
	 * Makes the user's session "anoymous" - eg, the user. This method sets the session's start and
	 * last visit time to the current datetime, the user id to the return of a call to
	 * <code>SystemGlobals.getIntValue(ConfigKeys.ANONYMOUS_USER_ID)</code> and finally sets
	 * session attribute named "logged" to "0" will be considered a non-authenticated / anonymous
	 * user
	 */
	public void makeAnonymous()
	{
		this.registerBasicInfo();
		
//		ControllerUtils.addCookie(SystemGlobals.getValue(ConfigKeys.COOKIE_AUTO_LOGIN), null);
//		ControllerUtils.addCookie(SystemGlobals.getValue(ConfigKeys.COOKIE_NAME_DATA),null);
		
		
//		ControllerUtils.addCookie(SystemGlobals.getValue(ConfigKeys.COOKIE_NAME_DATA),
//			SystemGlobals.getValue(ConfigKeys.ANONYMOUS_USER_ID));
		

		MemberSessionFacade.makeUnlogged();
	}

	/**
	 * Sets the startup and last visit time to now, as well set the
	 * user id to Anonymous. This method is usually called when the
	 * user hits the forum for the first time. 
	 */
	public void registerBasicInfo()
	{
		this.setStartTime(new Date(System.currentTimeMillis()));
		this.setLastVisit(new Date(System.currentTimeMillis()));
		this.setUsername(SystemGlobals.getValue(ConfigKeys.ANONYMOUS_USERNAME));
		this.setSiteUid(null);
		this.setIsFacebookAccount(Member.FACEBOOK_ACCOUNT_NO);
		this.setMemberId(Long.valueOf(SystemGlobals.getIntValue(ConfigKeys.ANONYMOUS_USER_ID)));
		this.setVip(false);
		this.setBuType(0);
	}

	/**
	 * Sets a new user session information using information from an <code>User</code> instance.
	 * This method sets the user id, username, the number of private messages, the session's start
	 * time ( set to the current date and time ) and the language.
	 * 
	 * @param user The <code>User</code> instance to get data from
	 */
	public void dataToMember(Member member)
	{
		this.setMemberId(member.getId());
		this.setUsername(member.getUserName());
		this.setStartTime(new Date(System.currentTimeMillis()));
		this.setEmail(member.getEmail());
		this.setNickname(member.getNickName());
		this.setPassword(member.getPassword());
		this.setExperience(member.getExperience());
		this.setAmount(member.getAmount());
		this.setScore(member.getScore());
		this.setRestAmount(member.getRestAmount());
		this.setAvatar(member.getAvatar());
		this.setAvatarStatus(member.getAvatarStatus());
		this.setSiteUid(member.getSiteUid());
		this.setIsFacebookAccount(member.getIsFacebookAccount());
	}

	/**
	 * Get the captcha image to challenge the user
	 * 
	 * @return BufferedImage the captcha image to challenge the user
	 */
	public BufferedImage getCaptchaImage()
	{
		if (this.imageCaptcha == null) {
			return null;
		}
		
		return (BufferedImage)this.imageCaptcha.getChallenge();
	}

	/**
	 * Validate the captcha response of user
	 * 
	 * @param userResponse String the captcha response from user
	 * @return boolean true if the answer is valid, otherwise return false
	 */
	public boolean validateCaptchaResponse(String userResponse)
	{
		System.out.println("validateCaptchaResponse() sessionid="+this.sessionId);
		if( this.imageCaptcha != null ) {
			System.out.println(" begin validate" + userResponse+"this.imageCaptcha ");
		} else  {
			System.out.println(" begin validate" + userResponse+"this.imageCaptcha = null ");
		}
		
		if ((SystemGlobals.getBoolValue(ConfigKeys.CAPTCHA_REGISTRATION) 
				|| SystemGlobals.getBoolValue(ConfigKeys.CAPTCHA_POSTS))
				&& this.imageCaptcha != null) {
			
			if (SystemGlobals.getBoolValue(ConfigKeys.CAPTCHA_IGNORE_CASE)) {
				userResponse = userResponse.toLowerCase();
			}
			
			System.out.println(" ===captcha=================================" + userResponse);
			
			boolean result =  this.imageCaptcha.validateResponse(userResponse).booleanValue();
			
			System.out.println(" ===captcha  result=================================" + result);
			if (result) {
				this.destroyCaptcha();
			}
			return result;
		}
		
		return true;
	}

	/**
	 * create a new image captcha
	 * 
	 */
	public void createNewCaptcha()
	{
		this.destroyCaptcha();
		System.out.println("createNewCaptcha() sessionid="+this.sessionId);
		this.imageCaptcha = Captcha.getInstance().getNextImageCaptcha();
		MemberSessionFacade.update(this);
	}

	/**
	 * Destroy the current captcha validation is done
	 * 
	 */
	public void destroyCaptcha()
	{
		this.imageCaptcha = null;
	}
	
	/**
     * @deprecated use CMWEBGAMEExecutionContext.getForumContext().isBot() instead
     *
     *
	 * Checks if it's a bot
	 * @return <code>true</code> if this user session is from any robot
	 */
	public boolean isBot()
	{
//        return Boolean.TRUE.equals(GPortalExecutionContext.getRequest().getAttribute(ConfigKeys.IS_BOT));
        return GPortalExecutionContext.getForumContext().isBot();
    }
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (!(o instanceof MemberSession)) {
			return false;
		}
		
		return this.sessionId.equals(((MemberSession)o).getSessionId());
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		return this.sessionId.hashCode();
	}

}
