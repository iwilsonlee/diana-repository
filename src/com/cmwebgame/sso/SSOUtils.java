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
 * Created on Jun 2, 2005 6:56:25 PM
 * The CMWEBGAME Project
 * http://www.cmwebgame.com
 */
package com.cmwebgame.sso;

import com.cmwebgame.dao.driver.DataAccessDriver;
import com.cmwebgame.entities.Member;
import com.cmwebgame.service.MemberManager;


/**
 * General utilities to use with SSO.
 * 
 * @author wilson
 * @version $Id: SSOUtils.java,v 1.6 2006/08/20 22:47:43 rafaelsteil Exp $
 */
public class SSOUtils
{
	private String username;
	private boolean exists = true;
	private Member member;
	private MemberManager memberManager;
	
	/**
	 * Checks if an user exists in the database
	 * 
	 * @param username The username to check
	 * @return <code>true</code> if the user exists. If <code>false</code> is
	 * returned, then you can insert the user by calling {@link #register(String, String)}
	 * @see #register(String, String)
	 * @see #getUser()
	 */
	public boolean memberExists(String username)
	{
		this.username = username;
		this.memberManager = DataAccessDriver.getInstance().newMemberManager();

		this.member = this.memberManager.getByUserName(username, Member.NORMAL);
		this.exists = this.member != null;

		return this.exists;
	}
	
	/**
	 * Registers a new user. 
	 * This method should be used together with {@link #userExists(String)}. 
	 * 
	 * @param password the user's password. It <em>should</em> be the real / final 
	 * password. In other words, the data passed as password is the data that'll be
	 * written to the database
	 * @param email the user's email
	 * @see #getUser()
	 */
	public void register(String password, String email)
	{
		if (this.exists) {
			return;
		}
		
		// Is a new user for us. Register him
		this.member = new Member();
		member.setUserName(this.username);
		member.setPassword(password);
		member.setEmail(email);
		member.setStatus(Member.NORMAL);
		
		this.memberManager.save(member);
	}
	
	public void updater(String username, String password, String email){
		this.member = this.memberManager.getByUserName(username,Member.NORMAL);
		member.setPassword(password);
		member.setEmail(email);
		
		this.memberManager.save(member);
	}
	
	/**
	 * Gets the member associated to this class instance.
	 * 
	 * @return the member
	 */
	public Member getMember()
	{
		return this.member;
	}
}
