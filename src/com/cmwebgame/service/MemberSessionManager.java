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
 * Created on 30/05/2004 13:12:34
 * The CMWEBGAME Project
 * http://www.cmwebgame.com
 */
package com.cmwebgame.service;

import java.sql.Connection;

import com.cmwebgame.entities.MemberSession;


/**
 * @author Rafael Steil
 * @version $Id: UserSessionDAO.java,v 1.6 2006/08/23 02:13:34 rafaelsteil Exp $
 */
public interface MemberSessionManager
{
	/**
	 * Writes a new <code>MemberSession</code> to the database.
	 * 
	 * @param us The <code>MemberSession</code> to store
	 * @param conn The {@link java.sql.Connection} object to use. 
	 * As many times user session management will be done in places where 
	 * a valid request is not available, we cannot try to retrieve the 
	 * conneciton from the thread local implementation. <br>
	 * If any driver implementation of this method will not use a database
	 * ( eg, where a <code>Connection</code> is not required ), when just
	 * pass <code>null</code> as argument.
	 */
	public void add(MemberSession ms, Connection conn) ;
	
	/**
	 * Updates an <code>MemberSession</code> 
	 * 
	 * @param us The <code>MemberSession</code> to update
	 * @param conn The {@link java.sql.Connection} object to use. 
	 * As many times user session management will be done in places where 
	 * a valid request is not available, we cannot try to retrieve the 
	 * conneciton from the thread local implementation. <br>
	 * If any driver implementation of this method will not use a database
	 * ( eg, where a <code>Connection</code> is not required ), when just
	 * pass <code>null</code> as argument.

	 */
	public void update(MemberSession ms, Connection conn) ;
	
	/**
	 * Gets an <code>MemberSession</code> from the database.
	 * The object passed as argument should at least have the user id 
	 * in order to find the correct register. 
	 * 
	 * @param us The complete <code>MemberSession</code> object data
	 * @param conn The {@link java.sql.Connection} object to use. 
	 * As many times user session management will be done in places where 
	 * a valid request is not available, we cannot try to retrieve the 
	 * conneciton from the thread local implementation. <br>
	 * If any driver implementation of this method will not use a database
	 * ( eg, where a <code>Connection</code> is not required ), when just
	 * pass <code>null</code> as argument.
	 * 
	 * @return MemberSession
	 */
	public MemberSession getById(MemberSession ms, Connection conn) ;
}
