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
 * Created on 30/05/2004 15:10:57
 * The CMWEBGAME Project
 * http://www.cmwebgame.com
 */
package com.cmwebgame.dao.portal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.cmwebgame.entities.MemberSession;
import com.cmwebgame.exceptions.DatabaseException;
import com.cmwebgame.util.DbUtils;
import com.cmwebgame.util.preferences.SystemGlobals;


/**
 * @author wilson
 */
public class MemberSessionDao implements com.cmwebgame.service.MemberSessionManager
{
	/**
	 * @see com.cmwebgame.dao.MemberSessionDao#add(com.cmwebgame.entities.MemberSession, java.sql.Connection)
	 */
	public void add(MemberSession ms, Connection conn)
	{
		this.add(ms, conn, false);
	}

	private void add(MemberSession ms, Connection conn, boolean checked)
	{
		if (!checked && this.getById(ms, conn) != null) {
			return;
		}

		PreparedStatement p = null;
		try {
			p = conn.prepareStatement(SystemGlobals.getSql("MemberSessionModel.add"));
			p.setString(1, ms.getSessionId());
			p.setInt(2, ms.getMemberId().intValue());
			p.setTimestamp(3, new Timestamp(ms.getStartTime().getTime()));

			p.executeUpdate();
		}
		catch (SQLException e) {
			throw new DatabaseException(e);
		}
		finally {
			DbUtils.close(p);
		}
	}

	/**
	 * @see com.cmwebgame.dao.MemberSessionDao#update(com.cmwebgame.entities.MemberSession,
	 *      java.sql.Connection)
	 */
	public void update(MemberSession ms, Connection conn)
	{
		if (this.getById(ms, conn) == null) {
			this.add(ms, conn, true);
			return;
		}

		PreparedStatement p = null;
		try {
			p = conn.prepareStatement(SystemGlobals.getSql("MemberSessionModel.update"));
			p.setTimestamp(1, new Timestamp(ms.getStartTime().getTime()));
			p.setLong(2, ms.getSessionTime());
			p.setString(3, ms.getSessionId());
			p.setInt(4, ms.getMemberId().intValue());

			p.executeUpdate();
		}
		catch (SQLException e) {
			throw new DatabaseException(e);
		}
		finally {
			DbUtils.close(p);
		}
	}

	/**
	 * @see com.cmwebgame.dao.MemberSessionDao#selectById(com.cmwebgame.entities.MemberSession,
	 *      java.sql.Connection)
	 */
	public MemberSession getById(MemberSession ms, Connection conn)
	{
		PreparedStatement p = null;
		ResultSet rs = null;
		try {
			p = conn.prepareStatement(SystemGlobals.getSql("MemberSessionModel.selectById"));
			p.setInt(1, ms.getMemberId().intValue());

			rs = p.executeQuery();
			boolean found = false;

			MemberSession returnUs = new MemberSession(ms);

			if (rs.next()) {
				returnUs.setSessionTime(rs.getLong("session_time"));
				returnUs.setStartTime(rs.getTimestamp("session_start"));
				found = true;
			}

			return (found ? returnUs : null);
		}
		catch (SQLException e) {
			throw new DatabaseException(e);
		}
		finally {
			DbUtils.close(rs, p);
		}
	}
}
