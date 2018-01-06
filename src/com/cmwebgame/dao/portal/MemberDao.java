package com.cmwebgame.dao.portal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.cmwebgame.ControllerUtils;
import com.cmwebgame.GPortalExecutionContext;
import com.cmwebgame.dao.AutoKeys;
import com.cmwebgame.dao.InitDao;
//import com.cmwebgame.dao.driver.DataAccessDriver;
//import com.cmwebgame.entities.AccountExperience;
//import com.cmwebgame.entities.AccountMoney;
//import com.cmwebgame.entities.AccountScore;
import com.cmwebgame.entities.Member;
//import com.cmwebgame.entities.MemberGame;
//import com.cmwebgame.entities.Spreader;
import com.cmwebgame.exceptions.DatabaseException;
import com.cmwebgame.exceptions.PortalException;
import com.cmwebgame.service.MemberManager;
//import com.cmwebgame.service.SpreaderManager;
import com.cmwebgame.sso.LoginAuthenticator;
import com.cmwebgame.util.DbUtils;
import com.cmwebgame.util.preferences.ConfigKeys;
import com.cmwebgame.util.preferences.SystemGlobals;
import com.google.common.collect.Lists;


/**
 * member實現類
 * @author Wenson
 */
public class MemberDao extends InitDao<Member> implements MemberManager {
	
	private static final Logger logger = Logger.getLogger(MemberDao.class);
	
	private static LoginAuthenticator loginAuthenticator;

	public MemberDao()
	{
		loginAuthenticator = (LoginAuthenticator)SystemGlobals.getObjectValue(
			ConfigKeys.LOGIN_AUTHENTICATOR_INSTANCE);
		
		if (loginAuthenticator == null) {
			throw new PortalException("There is no login.authenticator configured. Check your login.authenticator configuration key.");
		}
		
		loginAuthenticator.setMemberModel(this);
	}
	
	/**
	 * 特殊方法，只用來做會員登錄驗證
	 */
	public Member validateLogin(String username, String password)
	{
		return (Member)loginAuthenticator.validateLogin(username, password, null);
	}
	/**
	 * 取得外站會員中有在育駿官網設置帳密的帳號資料
	 * @param username
	 * @param password
	 * @return
	 */
//	public Member getBySiteUidAndSitePassword(String siteUid, String sitePassword)
//	{
//		Member member = null;
//		ResultSet rs=null;
//		PreparedStatement p=null;
//		
//		try 
//		{
//			p = GPortalExecutionContext.getConnection().prepareStatement(
//					SystemGlobals.getSql("MemberModel.checkBySiteUidAndSitePassword"));
//			p.setString(1, siteUid);
//			p.setString(2, sitePassword);
//			p.setInt(3, Member.YES);
//			p.setInt(4, Member.NORMAL);
//
//			rs = p.executeQuery();
//			if (rs.next() && rs.getLong("id") > 0) {
////				member = new Member();
////				member = this.makeMember(rs);
//				member = this.getByIdAndStatus(rs.getLong("id"),Member.NORMAL);
//			}
//		}
//		catch (SQLException e)
//		{
//			throw new PortalException(e);
//		}
//		finally
//		{
//			DbUtils.close(rs, p);
//		}
//
//		if (member != null) {
//			return member;
//		}
//
//		return null;
//	}
	
	/**
	 * 先從siteUid獲取會員資料，如果為空，則從username獲取資料
	 * @param username
	 * @param status
	 * @return
	 */
	public Member getMemberBySiteUidOrUsername(String username, int status){
		Member member = null;
		member = this.getBySiteUid(username, status);
		if(member == null){
			member = this.getByUserName(username, status);
		}
		return member;
	}
	
	public Long getMemberIdByUsername(String userName){
		PreparedStatement p = null;
		ResultSet rs = null;
		try {
			p = GPortalExecutionContext.getConnection()
					.prepareStatement(SystemGlobals.getSql("MemberModel.getMemberIdByUsername"));
			p.setString(1, userName);
			p.setString(2, userName);

			rs = p.executeQuery();

			Long memberId = null;

			if (rs.next()) {
				memberId = rs.getLong("id");
			}

			return memberId;
		} catch (SQLException e) {
			throw new DatabaseException(e);
		} finally {
			DbUtils.close(rs, p);
		}
	}
	
	public Long getMemberIdBySiteUid(String siteUid){
		PreparedStatement p = null;
		ResultSet rs = null;
		try {
			p = GPortalExecutionContext.getConnection()
			.prepareStatement(SystemGlobals.getSql("MemberModel.getMemberIdBySiteUid"));
			p.setString(1, siteUid);
			
			rs = p.executeQuery();
			
			Long memberId = null;
			
			if (rs.next()) {
				memberId = rs.getLong("id");
			}
			
			return memberId;
		} catch (SQLException e) {
			throw new DatabaseException(e);
		} finally {
			DbUtils.close(rs, p);
		}
	}
	
	
	public Member getByUserName(String username,int status) {
		PreparedStatement p = null;
		ResultSet rs = null;
		try {
			p = GPortalExecutionContext.getConnection()
					.prepareStatement(SystemGlobals.getSql("MemberModel.selectByUserName"));
			p.setInt(1, status);
			p.setString(2, username);

			rs = p.executeQuery();

			Member member = null;

			if (rs.next()) {
				member = new Member();
				member = this.makeMember(rs);
			}

			return member;
		} catch (SQLException e) {
			throw new DatabaseException(e);
		} finally {
			DbUtils.close(rs, p);
		}
	}
	
	public Member getByIdAndStatus(Long memberId,int status) {
		PreparedStatement p = null;
		ResultSet rs = null;
		try {
			p = GPortalExecutionContext.getConnection()
			.prepareStatement(SystemGlobals.getSql("MemberModel.selectByIdAndStatus"));
			p.setInt(1, status);
			p.setLong(2, memberId);
			
			rs = p.executeQuery();
			
			Member member = null;
			
			if (rs.next()) {
				member = new Member();
				member = this.makeMember(rs);
			}
			
			return member;
		} catch (SQLException e) {
			throw new DatabaseException(e);
		} finally {
			DbUtils.close(rs, p);
		}
	}
	
	public String getPasswordByIdAndStatus(Long memberId,int status) {
		PreparedStatement p = null;
		ResultSet rs = null;
		try {
			p = GPortalExecutionContext.getConnection()
			.prepareStatement(SystemGlobals.getSql("MemberModel.getPasswordByIdAndStatus"));
			p.setInt(1, status);
			p.setLong(2, memberId);
			
			rs = p.executeQuery();
			
			String password = null;
			
			if (rs.next()) {
				password = rs.getString("password");
			}
			
			return password;
		} catch (SQLException e) {
			throw new DatabaseException(e);
		} finally {
			DbUtils.close(rs, p);
		}
	}
	
	public void delete(Long memberId){
		PreparedStatement p = null;
		ResultSet rs = null;

		try {
			//獲取資料庫連接并執行SQL
			p = GPortalExecutionContext.getConnection()
					.prepareStatement(SystemGlobals.getSql("MemberModel.deleteMember"));
			p.setInt(1, Member.DELETE);
			p.setLong(2, memberId);
			p.executeUpdate();

		} catch (SQLException e) {
			throw new DatabaseException(e);
		} finally {
			DbUtils.close(rs, p);
		}
	}
	
	public void updateMemberAuthHash(int memberId, String hash)
	{
		PreparedStatement p = null;
		try {
			p = GPortalExecutionContext.getConnection().prepareStatement(
					SystemGlobals.getSql("MemberModel.saveMemberAuthHash"));
			p.setString(1, hash);
			p.setInt(2, memberId);
			p.executeUpdate();
		}
		catch (SQLException e) {
			throw new DatabaseException(e);
		}
		finally {
			DbUtils.close(p);
		}
	}
	
	public void updateLoginCountAndLoginTimeOrAndSiteUid(Long memberId, Integer loginCount, Timestamp loginTime, String siteUid)
	{
		PreparedStatement p = null;
		try {
			if(!StringUtils.isBlank(siteUid)){
				p = GPortalExecutionContext.getConnection().prepareStatement(
						SystemGlobals.getSql("MemberModel.updateLoginContAndLoginTimeAndSiteUid"));
				p.setInt(1, loginCount);
				p.setTimestamp(2, loginTime);
				p.setString(3, siteUid);
				p.setLong(4, memberId);
				p.executeUpdate();
			}else {
				p = GPortalExecutionContext.getConnection().prepareStatement(
						SystemGlobals.getSql("MemberModel.updateLoginContAndLoginTime"));
				p.setInt(1, loginCount);
				p.setTimestamp(2, loginTime);
				p.setLong(3, memberId);
				p.executeUpdate();
			}
			
			
		}
		catch (SQLException e) {
			throw new DatabaseException(e);
		}
		finally {
			DbUtils.close(p);
		}
	}
	
	public void updateLoginCount(Long memberId, Integer loginCount)
	{
		PreparedStatement p = null;
		try {
			p = GPortalExecutionContext.getConnection().prepareStatement(
					SystemGlobals.getSql("MemberModel.updateLoginCount"));
			p.setInt(1, loginCount);
			p.setLong(2, memberId);
			p.executeUpdate();
			
			
		}
		catch (SQLException e) {
			throw new DatabaseException(e);
		}
		finally {
			DbUtils.close(p);
		}
	}
	
	public String getMemberAuthHash(int memberId)
	{
		PreparedStatement p = null;
		ResultSet rs = null;
		try {
			p = GPortalExecutionContext.getConnection().prepareStatement(
					SystemGlobals.getSql("MemberModel.getMemberAuthHash"));
			p.setInt(1, memberId);

			rs = p.executeQuery();

			String hash = null;
			if (rs.next()) {
				hash = rs.getString(1);
			}

			return hash;
		}
		catch (SQLException e) {
			throw new DatabaseException(e);
		}
		finally {
			DbUtils.close(rs, p);
		}
	}
	/**
	 * 取得指定會員所注冊的所有游戲伺服器
	 * @param memberId
	 * @return
	 */
	public List<Long> findServerIdsByMemberId(Long memberId){
		PreparedStatement p = null;
		ResultSet rs = null;
		try {
			p = GPortalExecutionContext.getConnection().prepareStatement(
					SystemGlobals.getSql("MemberServerModel.findServerIdsByMemberId"));
			p.setLong(1, memberId);

			rs = p.executeQuery();

			List<Long> list = new ArrayList<Long>();
			while (rs.next()) {
				list.add(rs.getLong("server_id"));
			}

			return list;
		}
		catch (SQLException e) {
			throw new DatabaseException(e);
		}
		finally {
			DbUtils.close(rs, p);
		}
	}

	/**
	 * 增加會員註冊遊戲服務器記錄
	 */
	public void addMemberServer(Long memberId, Long serverId, String promoteCode) {
		String sql = SystemGlobals.getSql("MemberServerModel.addMemberServer");
		List<Object> list = Lists.newArrayList();
		list.add(memberId);
		list.add(serverId);
		list.add(promoteCode);
		this.insert(sql, list);
	}
	
	/**
	 * 檢查會員是否已經注冊過游戲服務器
	 */
	public boolean checkMemberServer(Long memberId, Long serverId){
		boolean exist = false;
		PreparedStatement p = null;
		ResultSet rs = null;
		try {
			p = GPortalExecutionContext.getConnection().prepareStatement(
					SystemGlobals.getSql("MemberServerModel.checkMemberServer"));
			p.setLong(1, memberId);
			p.setLong(2, serverId);

			rs = p.executeQuery();

			List<Long> list = new ArrayList<Long>();
			if (rs.next()) {
				exist = true;
			}

			return exist;
		}
		catch (SQLException e) {
			throw new DatabaseException(e);
		}
		finally {
			DbUtils.close(rs, p);
		}
	}


	/**
	 * 把從資料庫撈出的資料填到member object里面
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	protected Member makeMember(ResultSet rs) throws SQLException {
		Member member = new Member();
		member.setId(rs.getLong("id"));
		member.setUserName(rs.getString("user_name"));
		member.setPassword(rs.getString("password"));
		member.setName(rs.getString("name"));
		member.setEmail(rs.getString("email"));
		member.setSafeEmail(rs.getString("safe_email"));
		member.setNickName(rs.getString("nick_name"));
		member.setGender(rs.getInt("gender"));
		member.setBirthday(rs.getDate("birthday"));
		//member.setCreateday(rs.getDate("createday"));
		member.setCreateday(rs.getTimestamp("createday"));
		member.setPhone(rs.getString("phone"));
		member.setMobile(rs.getString("mobile"));
		member.setIdCard(rs.getString("id_card"));
		member.setCountyId(rs.getLong("county"));
		member.setAddress(rs.getString("address"));
		member.setLoginTime(rs.getTimestamp("login_time"));
		member.setBuy_sn(rs.getString("buy_sn"));
		member.setBuy_kind(rs.getString("buy_kind"));
		member.setPartner_sn(rs.getString("partner_sn"));
		member.setParentMemberId(rs.getLong("parent_member"));
		member.setLoginCount(rs.getInt("login_count"));
		member.setPayCount(rs.getInt("pay_count"));
		member.setPopulace(rs.getString("populace"));
		member.setServerunion(rs.getString("serverunion"));
		member.setSiteUid(rs.getString("site_uid"));
		member.setSitePassword(rs.getString("site_Password"));
		member.setStatus(rs.getInt("status"));
		member.setExperience(rs.getInt("experience"));
		member.setAmount(rs.getInt("amount"));
		member.setScore(rs.getInt("score"));
		member.setIsFacebookAccount(rs.getInt("is_facebook_account"));
		member.setSpreaderMemberId(rs.getLong("spreader_member"));
		member.setAvatar(rs.getString("avatar"));
		member.setAvatarStatus(rs.getInt("avatar_status"));
		member.setSpreaderId(rs.getLong("spreader_id"));
		member.setCookieValue(rs.getString("cookie_value"));

		return member;
	}
	
	protected void putMember(PreparedStatement p,Member member) throws SQLException {
//		p.setLong(1, member.getId());
		p.setString(1, member.getUserName());
		p.setString(2, member.getPassword());
		p.setString(3, member.getName());
		p.setString(4, member.getEmail());
		p.setString(5, member.getSafeEmail());
		p.setString(6, member.getNickName());
		p.setInt(7, member.getGender());
		p.setDate(8, member.getBirthday());
		p.setTimestamp(9, member.getCreateday());
		p.setString(10, member.getPhone());
		p.setString(11, member.getMobile());
		p.setString(12, member.getIdCard());
		p.setLong(13, member.getCountyId());
		p.setString(14, member.getAddress());
		p.setTimestamp(15, member.getLoginTime());
		p.setString(16, member.getBuy_sn());
		p.setString(17, member.getBuy_kind());
		p.setString(18, member.getPartner_sn());
		p.setLong(19, member.getParentMemberId());
		p.setInt(20, member.getLoginCount());
		p.setInt(21, member.getPayCount());
		p.setString(22, member.getPopulace());
		p.setString(23, member.getServerunion());
		p.setString(24, member.getSiteUid());
		p.setString(25, member.getSitePassword());
		p.setInt(26, member.getStatus());
		p.setInt(27, member.getExperience());
		p.setInt(28, member.getAmount());
		p.setInt(29, member.getScore());
		p.setInt(30, member.getIsFacebookAccount());
		p.setLong(31, member.getSpreaderMemberId());
		p.setString(32, member.getAvatar());
		p.setInt(33, member.getAvatarStatus());
		p.setLong(34, member.getSpreaderId());
		p.setString(35, member.getCookieValue());
	}
	
	public Member save(Member member){
		try {
			if (member != null && member.getId() != null) {
				this.update(member);
			} else {
				this.add(member);
			}
			return member;
		} catch (Exception e) {
			throw new DatabaseException(e);
		}
	}
	/*
	protected void add(Member member) {
		PreparedStatement p = null;
		try {
			p = this.getStatementForAutoKeys("MemberModel.addNewMember");

			this.putMember(p, member);


			int memberId = this.executeAutoKeysQuery(p);
			member.setId(Long.valueOf(memberId));
		} catch (SQLException e) {
			throw new DatabaseException(e);
		} finally {
			DbUtils.close(p);
		}
	}
	*/
	/**
	 * 使用事務增加會員並同時增加會員的儲值餘額、積分餘額、經驗餘額帳號
	 */
	protected void add(Member member) {
		String spreadCode = null;
		Cookie spreadcodeCookie = ControllerUtils.getCookie("spreadcode");
		if(spreadcodeCookie != null && !spreadcodeCookie.equals("")){
			spreadCode = spreadcodeCookie.getValue();
		}
//		if(spreadCode != null && !spreadCode.equals("")){
//			SpreaderManager sm = DataAccessDriver.getInstance().newSpreaderManager();
//			Spreader spreader = sm.getBySpreadCode(spreadCode);
//			if(spreader != null){
//				member.setSpreaderId(spreader.getId());
//			}
//		}
		try {
			PreparedStatement p1 = null;
			PreparedStatement p2 = null;
			PreparedStatement p3 = null;
			PreparedStatement p4 = null;

			Connection conn = GPortalExecutionContext.getConnection();
			boolean autoCommit = conn.getAutoCommit();
			// 开始一个新的事务 
			conn.setAutoCommit(false);
			
//			String sql1 = SystemGlobals.getSql("MemberModel.addNewMember");
			int memberId = this.insert(conn, member);
			member.setId(Long.valueOf(memberId));
			
			String sql2 = SystemGlobals.getSql("AccountMoneyModel.addAccountMoney");
			List<Object> list2 = Lists.newArrayList();
			list2.add(Long.valueOf(memberId));
			list2.add(Long.valueOf(0));
			list2.add(Long.valueOf(0));
			int accountMoneyId = this.insert(conn, sql2, list2);
			
			String sql3 = SystemGlobals.getSql("AccountScoreModel.addAccountScore");
			List<Object> list3 = Lists.newArrayList();
			list3.add(Long.valueOf(memberId));
			list3.add(Long.valueOf(0));
			int accountScoreId = this.insert(conn, sql3, list3);
			
			String sql4 = SystemGlobals.getSql("AccountExperienceModel.addAccountExperience");
			List<Object> list4 = Lists.newArrayList();
			list4.add(Long.valueOf(memberId));
			list4.add(Long.valueOf(0));
			int accountExperienceId = this.insert(conn, sql4, list4);
			
			conn.commit();

		} catch (Exception e) {
			throw new PortalException(e);
		}
	}
	
	protected void update(Member member) {
		PreparedStatement p = null;

		try {
			p = GPortalExecutionContext.getConnection()
					.prepareStatement(SystemGlobals.getSql("MemberModel.updateMember"));
			this.putMember(p, member);
			p.setLong(36, member.getId());

			p.executeUpdate();
		} catch (SQLException e) {
			throw new DatabaseException(e);
		} finally {
			DbUtils.close(p);
		}
	}
	
	/**
	 * 通過指定的行銷商的會員Id和指定日期，得到該行銷商導入的會員數量
	 */
	public Long getMemberCountByParentMemberIdAndDate(Long parentMemberId, String dateString){
		PreparedStatement p = null;
		ResultSet rs = null;
		Long memberCount = Long.valueOf(0);
		try {
			p = GPortalExecutionContext.getConnection().prepareStatement(
					SystemGlobals.getSql("MemberModel.getMemberCountByParentMemberIdAndDate"));
			p.setLong(1, parentMemberId);
			p.setString(2, dateString);

			rs = p.executeQuery();
			if (rs.next() && rs.getLong("member_count") > 0) {
				memberCount = rs.getLong("member_count");
			}

			return memberCount;
		}
		catch (SQLException e) {
			throw new DatabaseException(e);
		}
		finally {
			DbUtils.close(rs, p);
		}
	}
	
	public boolean checkUsernameRegistered(String userName){
		boolean status = false;

		PreparedStatement p = null;
		ResultSet rs = null;
		try {
			p = GPortalExecutionContext.getConnection().prepareStatement(
					SystemGlobals.getSql("MemberModel.checkUsernameRegistered"));
			p.setString(1, userName);

			rs = p.executeQuery();
			if (rs.next() && rs.getInt("registered") > 0) {
				status = true;
			}

			return status;
		}
		catch (SQLException e) {
			throw new DatabaseException(e);
		}
		finally {
			DbUtils.close(rs, p);
		}
	}
	
	public boolean checkSiteUidRegistered(String siteUid){
		boolean status = false;
		
		PreparedStatement p = null;
		ResultSet rs = null;
		try {
			p = GPortalExecutionContext.getConnection().prepareStatement(
					SystemGlobals.getSql("MemberModel.checkSiteUidRegistered"));
			p.setString(1, siteUid);
			
			rs = p.executeQuery();
			if (rs.next() && rs.getInt("registered") > 0) {
				status = true;
			}
			
			return status;
		}
		catch (SQLException e) {
			throw new DatabaseException(e);
		}
		finally {
			DbUtils.close(rs, p);
		}
	}
	
	public boolean checkNicknameRegistered(String nickName){
		boolean status = false;
		
		PreparedStatement p = null;
		ResultSet rs = null;
		try {
			p = GPortalExecutionContext.getConnection().prepareStatement(
					SystemGlobals.getSql("MemberModel.checkNicknameRegistered"));
			p.setString(1, nickName);
			
			rs = p.executeQuery();
			if (rs.next() && rs.getInt("registered") > 0) {
				status = true;
			}
			
			return status;
		}
		catch (SQLException e) {
			throw new DatabaseException(e);
		}
		finally {
			DbUtils.close(rs, p);
		}
	}

	/**
	 * 存在則返回true
	 * @param userName
	 * @param password
	 * @return
	 */
	public boolean checkByUserNameAndPassword(String userName, String password){
		PreparedStatement p = null;
		ResultSet rs = null;
		boolean status = false;
		try {
			p = GPortalExecutionContext.getConnection().prepareStatement(
					SystemGlobals.getSql("MemberModel.checkByUserNameAndPassword"));
			p.setString(1, userName);
			p.setString(2, password);
			p.setInt(3, Member.NORMAL);
			
			rs = p.executeQuery();
			if (rs.next() && rs.getInt("registered") > 0) {
				status = true;
			}
			
			return status;
		}
		catch (SQLException e) {
			throw new DatabaseException(e);
		}
		finally {
			DbUtils.close(rs, p);
		}
	}
	
	public Member getByNickName(String nickName) {
		PreparedStatement p = null;
		ResultSet rs = null;
		try {
			p = GPortalExecutionContext.getConnection().prepareStatement(
					SystemGlobals.getSql("MemberModel.getByNickName"));
			p.setString(1, nickName);
			p.setInt(2, Member.NORMAL);
			
			rs = p.executeQuery();
			Member member = null;
			if (rs.next()) {
				member = this.makeMember(rs);
			}
			
			return member;
		}
		catch (SQLException e) {
			throw new DatabaseException(e);
		}
		finally {
			DbUtils.close(rs, p);
		}
	}
	
	public Member getByUserNameAndPassword(String userName, String password){
		PreparedStatement p = null;
		ResultSet rs = null;
		try {
			p = GPortalExecutionContext.getConnection().prepareStatement(
					SystemGlobals.getSql("MemberModel.getByUserNameAndPassword"));
			p.setString(1, userName);
			p.setString(2, password);
			p.setInt(3, Member.NORMAL);
			
			rs = p.executeQuery();
			Member member = null;
			if (rs.next()) {
				member = this.makeMember(rs);
			}
			
			return member;
		}
		catch (SQLException e) {
			throw new DatabaseException(e);
		}
		finally {
			DbUtils.close(rs, p);
		}
	}
	
	/**
	 * 根據合作網站的uid查出會員資料
	 * @param siteUid
	 * @return
	 */
	public Member getBySiteUid(String siteUid,int status) {
		PreparedStatement p = null;
		ResultSet rs = null;
		try {
			p = GPortalExecutionContext.getConnection().prepareStatement(
					SystemGlobals.getSql("MemberModel.getBySiteUid"));
			p.setString(1, siteUid);
			p.setInt(2, Member.NORMAL);
			
			rs = p.executeQuery();
			Member member = null;
			if (rs.next()) {
				member = this.makeMember(rs);
			}
			
			return member;
		}
		catch (SQLException e) {
			throw new DatabaseException(e);
		}
		finally {
			DbUtils.close(rs, p);
		}		
	}
	
	public Member getByEmail(String email) {
		PreparedStatement p = null;
		ResultSet rs = null;
		try {
			p = GPortalExecutionContext.getConnection().prepareStatement(
					SystemGlobals.getSql("MemberModel.getByEmail"));
			p.setString(1, email);
			p.setInt(2, Member.NORMAL);
			
			rs = p.executeQuery();
			Member member = null;
			if (rs.next()) {
				member = this.makeMember(rs);
			}
			
			return member;
		}
		catch (SQLException e) {
			throw new DatabaseException(e);
		}
		finally {
			DbUtils.close(rs, p);
		}
	}

	/**
	 * 查找拥有指定推廣員的會員.
	 */
	public List<Member> findMembersByspreaderMemberForPage(Long spreaderMemberId,int pageNo, int pageSize){
		PreparedStatement p = null;
		ResultSet rs = null;
		try {
			p = GPortalExecutionContext.getConnection()
			.prepareStatement(SystemGlobals.getSql("MemberModel.selectByIdAndStatus"));
			p.setInt(1, Member.NORMAL);
			p.setLong(2, spreaderMemberId);
			
			rs = p.executeQuery();
			
			List<Member> l = new ArrayList();
			
			if (rs.next()) {
				l.add(this.makeMember(rs));
			}
			
			return l;
		} catch (SQLException e) {
			throw new DatabaseException(e);
		} finally {
			DbUtils.close(rs, p);
		}
	}
	
	public List<Member> getMembersBySpreaderMember(Long spreaderMemberId, int start, int count){
		PreparedStatement p = null;
		ResultSet rs = null;
		try {
			p = GPortalExecutionContext.getConnection().prepareStatement(
					SystemGlobals.getSql("MemberModel.getMembersBySpreaderMember"));
			p.setLong(1, spreaderMemberId);
			p.setInt(2, start);
			p.setInt(3, count);
			
			rs = p.executeQuery();
			
			List<Member> list = new ArrayList<Member>();
			
			while (rs.next()) {
				list.add(this.makeMember(rs));
			}
			return list;
		}
		catch (SQLException e) {
			throw new DatabaseException(e);
		}
		finally {
			DbUtils.close(rs, p);
		}
	}
	
	public Long getTotalMembersBySpreaderMember(Long spreaderMemberId){
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = GPortalExecutionContext.getConnection().prepareStatement(
					SystemGlobals.getSql("MemberModel.totalMembersByspreaderMember"));
			preparedStatement.setLong(1, spreaderMemberId);
			return this.getTotalMembersCommon(preparedStatement);
		}
		catch (SQLException e) {
			throw new DatabaseException(e);
		}
		finally {
			DbUtils.close(preparedStatement);
		}
	}
	
	protected Long getTotalMembersCommon(PreparedStatement p) throws SQLException
	{
		ResultSet rs = p.executeQuery();
		rs.next();

		Long total = rs.getLong(1);

		rs.close();
		p.close();

		return total;
	}
	
	public List<Member> findListCreate24HR() {
		return null;
//		return this.find(QUERY_BY_CREATE_24HR);
	}

	@Override
	public List<Member> findBySpreaderIdByLimit(Long spreaderId, int start, int count) {
		// TODO Auto-generated method s)tub
		PreparedStatement p = null;
		ResultSet rs = null;
		try {
			p = GPortalExecutionContext.getConnection().prepareStatement(
					SystemGlobals.getSql("MemberModel.findBySpreaderIdByLimit"));
			p.setLong(1, spreaderId);
			p.setInt(2, start);
			p.setInt(3, count);
			
			rs = p.executeQuery();
			
			List<Member> list = new ArrayList<Member>();
			
			while (rs.next()) {
				list.add(this.makeMember(rs));
			}
			return list;
		}
		catch (SQLException e) {
			throw new DatabaseException(e);
		}
		finally {
			DbUtils.close(rs, p);
		}
	}

	@Override
	public List<Member> findBySpreaderPartnerIdByLimit(Long spreaderParnerId, int start, int count) {
		// TODO Auto-generated method stub
		PreparedStatement p = null;
		ResultSet rs = null;
		try {
			p = GPortalExecutionContext.getConnection().prepareStatement(
					SystemGlobals.getSql("MemberModel.findBySpreaderPartnerIdByLimit"));
			p.setLong(1, spreaderParnerId);
			p.setInt(2, start);
			p.setInt(3, count);
			
			rs = p.executeQuery();
			
			List<Member> list = new ArrayList<Member>();
			
			while (rs.next()) {
				list.add(this.makeMember(rs));
			}
			return list;
		}
		catch (SQLException e) {
			throw new DatabaseException(e);
		}
		finally {
			DbUtils.close(rs, p);
		}
	}

	@Override
	public List<Member> findByUsernameAndSpreaderIdByLimit(String userName, Long spreaderId, int start, int count) {
		// TODO Auto-generated method stub
		PreparedStatement p = null;
		ResultSet rs = null;
		try {
			p = GPortalExecutionContext.getConnection().prepareStatement(
					SystemGlobals.getSql("MemberModel.findByUsernameAndSpreaderIdByLimit"));
			p.setString(1, userName);
			p.setLong(2, spreaderId);
			p.setInt(3, start);
			p.setInt(4, count);
			
			rs = p.executeQuery();
			
			List<Member> list = new ArrayList<Member>();
			
			while (rs.next()) {
				list.add(this.makeMember(rs));
			}
			return list;
		}
		catch (SQLException e) {
			throw new DatabaseException(e);
		}
		finally {
			DbUtils.close(rs, p);
		}
	}

	@Override
	public List<Member> findByUsernameAndSpreaderPartnerIdByLimit(String userName, Long spreaderParnerId, int start,
			int count) {
		// TODO Auto-generated method stub
		PreparedStatement p = null;
		ResultSet rs = null;
		try {
			p = GPortalExecutionContext.getConnection().prepareStatement(
					SystemGlobals.getSql("MemberModel.findByUsernameAndSpreaderPartnerIdByLimit"));
			p.setString(1, userName);
			p.setLong(2, spreaderParnerId);
			p.setInt(3, start);
			p.setInt(4, count);
			
			rs = p.executeQuery();
			
			List<Member> list = new ArrayList<Member>();
			
			while (rs.next()) {
				list.add(this.makeMember(rs));
			}
			return list;
		}
		catch (SQLException e) {
			throw new DatabaseException(e);
		}
		finally {
			DbUtils.close(rs, p);
		}
	}

	@Override
	public List<Member> searchByUsernameAndCreateDayAndSpreaderIdByLimit(String userName, String startDay,
			String endDay, Long spreaderId, int start, int count) {
		// TODO Auto-generated method stub
		PreparedStatement p = null;
		ResultSet rs = null;
		String sql = "SELECT id,user_name,password,name,email,safe_email,nick_name,gender,birthday,createday,phone,mobile,id_card,county,"+
                                 " address,login_time,buy_sn,buy_kind,partner_sn,parent_member,login_count,pay_count,populace,serverunion,"+
                                 " site_Uid,site_Password,status,experience,amount,score,is_facebook_account,spreader_member,avatar," +
                                 "avatar_status,spreader_id FROM members WHERE 1=1";
		if(spreaderId != null && spreaderId.intValue() != 0){
			sql += " and spreader_id = " + spreaderId;
		}
		if(userName != null && !userName.equals("")){
			sql += " and user_name = '" + userName + "'";
		}
		if(startDay != null && !startDay.equals("")){
			startDay += " 00:00:00";
			sql += " and createday >='" + startDay + "'";
		}
		if(endDay != null && !endDay.equals("")){
			endDay += " 23:59:59";
			sql += " and createday <='" + endDay + "'";
		}
		sql += " order by id desc limit ?,?";
		try {
			p = GPortalExecutionContext.getConnection().prepareStatement(sql);
			p.setInt(1, start);
			p.setInt(2, count);
			
			rs = p.executeQuery();
			
			List<Member> list = new ArrayList<Member>();
			
			while (rs.next()) {
				list.add(this.makeMember(rs));
			}
			return list;
		}
		catch (SQLException e) {
			throw new DatabaseException(e);
		}
		finally {
			DbUtils.close(rs, p);
		}
	}
	
	@Override
	public int countByUsernameAndCreateDayAndSpreaderId(String userName, String startDay,
			String endDay, Long spreaderId) {
		// TODO Auto-generated method stub
		PreparedStatement p = null;
		ResultSet rs = null;
		String sql = "SELECT count(id) as total FROM members WHERE 1=1";
		if(spreaderId != null && spreaderId.intValue() != 0){
			sql += " and spreader_id = " + spreaderId;
		}
		if(userName != null && !userName.equals("")){
			sql += " and user_name = '" + userName + "'";
		}
		if(startDay != null && !startDay.equals("")){
			startDay += " 00:00:00";
			sql += " and createday >='" + startDay + "'";
		}
		if(endDay != null && !endDay.equals("")){
			endDay += " 23:59:59";
			sql += " and createday <='" + endDay + "'";
		}
		try {
			p = GPortalExecutionContext.getConnection().prepareStatement(sql);
			
			rs = p.executeQuery();
			
			Long count = new Long(0);
			
			if (rs.next()) {
				count = rs.getLong("total");
			}
			return count.intValue();
		}
		catch (SQLException e) {
			throw new DatabaseException(e);
		}
		finally {
			DbUtils.close(rs, p);
		}
	}

	@Override
	public List<Member> searchByUsernameAndCreateDayAndSpreaderPartnerIdByLimit(String userName, String startDay,
			String endDay, Long spreaderPartnerId, int start, int count) {
		// TODO Auto-generated method stub
		PreparedStatement p = null;
		ResultSet rs = null;
		String sql = "SELECT id,user_name,password,name,email,safe_email,nick_name,gender,birthday,createday,phone,mobile,id_card,county,"+
                                 " address,login_time,buy_sn,buy_kind,partner_sn,parent_member,login_count,pay_count,populace,serverunion,"+
                                 " site_Uid,site_Password,status,experience,amount,score,is_facebook_account,spreader_member,avatar," +
                                 "avatar_status,spreader_id,cookie_value FROM members WHERE 1=1";
		if(userName != null && !userName.equals("")){
			sql += " and user_name = '" + userName + "'";
		}
		if(startDay != null && !startDay.equals("")){
			startDay += " 00:00:00";
			sql += " and createday <='" + startDay + "'";
		}
		if(endDay != null && !endDay.equals("")){
			endDay += " 23:59:59";
			sql += " and createday >='" + endDay + "'";
		}
		if(spreaderPartnerId!=null){
			sql += " and spreader_id in(select id from spreader where spreader_partner_id = ?) order by id desc limit ?,?";
		}
		try {
			p = GPortalExecutionContext.getConnection().prepareStatement(sql);
			p.setLong(1, spreaderPartnerId);
			p.setInt(2, start);
			p.setInt(3, count);
			
			rs = p.executeQuery();
			
			List<Member> list = new ArrayList<Member>();
			
			while (rs.next()) {
				list.add(this.makeMember(rs));
			}
			return list;
		}
		catch (SQLException e) {
			throw new DatabaseException(e);
		}
		finally {
			DbUtils.close(rs, p);
		}
	}
	
	@Override
	public int countByUsernameAndCreateDayAndSpreaderPartnerId(String userName, String startDay,
			String endDay, Long spreaderPartnerId) {
		// TODO Auto-generated method stub
		PreparedStatement p = null;
		ResultSet rs = null;
		String sql = "SELECT count(id) as total FROM members WHERE 1=1";
		if(userName != null && !userName.equals("")){
			sql += " and user_name = '" + userName + "'";
		}
		if(startDay != null && !startDay.equals("")){
			startDay += " 00:00:00";
			sql += " and createday <='" + startDay + "'";
		}
		if(endDay != null && !endDay.equals("")){
			endDay += " 23:59:59";
			sql += " and createday >='" + endDay + "'";
		}
		if(spreaderPartnerId!=null){
			sql += " and spreader_id in(select id from spreader where spreader_partner_id = ?)";
		}
		try {
			p = GPortalExecutionContext.getConnection().prepareStatement(sql);
			p.setLong(1, spreaderPartnerId);
			
			rs = p.executeQuery();
			
			Long count = new Long(0);
			
			if (rs.next()) {
				count = rs.getLong("total");
			}
			return count.intValue();
		}
		catch (SQLException e) {
			throw new DatabaseException(e);
		}
		finally {
			DbUtils.close(rs, p);
		}
	}

}
