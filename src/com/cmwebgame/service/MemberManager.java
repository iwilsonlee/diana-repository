package com.cmwebgame.service;

import java.sql.Timestamp;
import java.util.List;

import com.cmwebgame.entities.Member;

/**
 * 用户管理类.
 * 
 * 实现领域对象用户的所有业务管理函数.
 * 
 * @author wilson
 */
public interface MemberManager {
	
	public Long getMemberIdByUsername(String userName);
	
	public Long getMemberIdBySiteUid(String siteUid);
	
	public Member validateLogin(String username, String password);
	
//	public Member getBySiteUidAndSitePassword(String siteUid, String sitePassword);
	/**
	 * 先從siteUid獲取會員資料，如果為空，則從username獲取資料
	 * @param username
	 * @param status
	 * @return
	 */
	public Member getMemberBySiteUidOrUsername(String username, int status);
	
	public Member getByUserName(String username,int status);
	
	public Member getByIdAndStatus(Long memberId,int status);
	
	public String getPasswordByIdAndStatus(Long memberId,int status);
	
	public void delete(Long id) ;
	
	public String getMemberAuthHash(int memberId);
	
	public void updateMemberAuthHash(int memberId, String hash);
	/**
	 * 更新登录次数栏位，或者连siteUid一起更新
	 * @param memberId
	 * @param loginTime
	 * @param loginCount
	 * @param siteUid
	 */
	public void updateLoginCountAndLoginTimeOrAndSiteUid(Long memberId, Integer loginCount, Timestamp loginTime, String siteUid);
	/**
	 * 更新登录次数栏位
	 * @param memberId
	 * @param loginCount
	 */
	public void updateLoginCount(Long memberId, Integer loginCount);
	/**
	 * 保存member，包括update操作
	 * @param member
	 * @return
	 */
	public Member save(Member member);
	/**
	 * 檢查制定的username是否存在，true表示存在，false表示不存在
	 * @param userName
	 * @return
	 */
	public boolean checkUsernameRegistered(String userName);
	/**
	 * 檢查制定的siteUid是否存在，true表示存在，false表示不存在
	 * @param siteUid
	 * @return
	 */
	public boolean checkSiteUidRegistered(String siteUid);
	
	public boolean checkNicknameRegistered(String nickName);

	public boolean checkByUserNameAndPassword(String userName, String password);


	public Member getByNickName(String nickName);
	
	public Member getBySiteUid(String siteUid, int status);
	
	public Member getByEmail(String email);
	
	public Long getMemberCountByParentMemberIdAndDate(Long parentMemberId, String dateString);
	/**
	 * 查找會員.
	 */
//	public Page<Member> searchByMemberQuery(Page<Member> page, MemberQuery memberQuery);
	
	/**
	 * 查找會員.
	 */
//	public List<Member> searchListMembersByMemberQuery(MemberQuery memberQuery);
	
	/**
	 * 根據條件查找會員一天加總.
	 */
//	public Long searchMemberCountForPerformanceTotal(MemberQuery memberQuery, String day);

	public Member getByUserNameAndPassword(String userName, String password);
	
	/**
	 * 查詢新註冊過24小時的會員
	 * @return
	 */
	public List<Member> findListCreate24HR();
	
	/**
	 * 查詢我推廣的會員分頁
	 * @return
	 */
	public List<Member> getMembersBySpreaderMember(Long spreaderMemberId, int start, int count);
	/**
	 * 獲取我推廣的會員總數
	 * @return
	 */
	public Long getTotalMembersBySpreaderMember(Long spreaderMemberId);

	/**
	 * 增加會員註冊遊戲服務器記錄
	 */
	public void addMemberServer(Long memberId, Long serverId, String promoteCode);

	/**
	 * 新增會員登入指定遊戲紀錄
	 */
//	public void addMemberGame(Long memberId, Long gameId, String appId);

	/**
	 * 取得指定會員所注冊的所有游戲伺服器
	 */
	public List<Long> findServerIdsByMemberId(Long memberId);
	/**
	 * 檢查會員是否已經注冊過游戲服務器
	 */
	public boolean checkMemberServer(Long memberId, Long serverId);
	/**
	 * 分頁查找指定推廣合作平台的會員
	 * @param spreaderParnerId
	 * @param start
	 * @param count
	 * @return
	 */
	public List<Member> findBySpreaderPartnerIdByLimit(Long spreaderParnerId, int start, int count);
	/**
	 * 分頁查找指定推廣人的會員
	 * @param spreaderId
	 * @param start
	 * @param count
	 * @return
	 */
	public List<Member> findBySpreaderIdByLimit(Long spreaderId, int start, int count);
	/**
	 * 分頁查找指定會員帳號和推廣合作平台的會員
	 * @param userName
	 * @param spreaderParnerId
	 * @param start
	 * @param count
	 * @return
	 */
	public List<Member> findByUsernameAndSpreaderPartnerIdByLimit(String userName,Long spreaderParnerId, int start, int count);
	/**
	 * 分頁查找指定會員帳號和推廣人的會員
	 * @param userName
	 * @param spreaderId
	 * @param start
	 * @param count
	 * @return
	 */
	public List<Member> findByUsernameAndSpreaderIdByLimit(String userName,Long spreaderId, int start, int count);
	/**
	 * 分頁查詢會員，條件：會員帳號、帳號創建日期範圍、所屬推廣人
	 * @param userName
	 * @param startDay
	 * @param endDay
	 * @param spreaderId
	 * @param start
	 * @param count
	 * @return
	 */
	public List<Member> searchByUsernameAndCreateDayAndSpreaderIdByLimit(String userName, String startDay, String endDay, Long spreaderId, int start, int count);
	/**
	 * 分頁查詢會員，查詢條件：會員帳號、帳號創建日期範圍、所屬推廣平台
	 * @param userName
	 * @param startDay
	 * @param endDay
	 * @param spreaderPartnerId
	 * @param start
	 * @param count
	 * @return
	 */
	public List<Member> searchByUsernameAndCreateDayAndSpreaderPartnerIdByLimit(String userName, String startDay, String endDay, Long spreaderPartnerId, int start, int count);
	
	public int countByUsernameAndCreateDayAndSpreaderId(String userName, String startDay,
			String endDay, Long spreaderId);
	
	public int countByUsernameAndCreateDayAndSpreaderPartnerId(String userName, String startDay, String endDay, Long spreaderPartnerId);
}
