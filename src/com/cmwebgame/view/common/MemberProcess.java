package com.cmwebgame.view.common;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.cmwebgame.ControllerUtils;
import com.cmwebgame.GPortalExecutionContext;
import com.cmwebgame.context.RequestContext;
import com.cmwebgame.context.ResponseContext;
import com.cmwebgame.dao.driver.DataAccessDriver;
//import com.cmwebgame.entities.AdTrackStatistics;
//import com.cmwebgame.entities.AtsMemberRecord;
//import com.cmwebgame.entities.ConsigneeInfo;
//import com.cmwebgame.entities.County;
//import com.cmwebgame.entities.IpLimit;
import com.cmwebgame.entities.Member;
import com.cmwebgame.entities.MemberSession;
//import com.cmwebgame.repository.AccountRepository;
//import com.cmwebgame.repository.ConsigneeInfoRepository;
import com.cmwebgame.repository.MemberSessionFacade;
//import com.cmwebgame.repository.PortalRepository;
//import com.cmwebgame.service.AdTrackStatisticsManager;
//import com.cmwebgame.service.AtsMemberRecordManager;
//import com.cmwebgame.service.ExperienceRecordManager;
//import com.cmwebgame.service.IpLimitManager;
import com.cmwebgame.service.MemberManager;
import com.cmwebgame.service.MemberSessionManager;
//import com.cmwebgame.service.OrderManager;
//import com.cmwebgame.service.ScoreRecordManager;
//import com.cmwebgame.service.TradeMerchandiseManager;
import com.cmwebgame.util.EscapeUnescape;
import com.cmwebgame.util.MD5;
import com.cmwebgame.util.preferences.ConfigKeys;
import com.cmwebgame.util.preferences.SystemGlobals;
 
/**
 * 會員相關處理的方法
 * @author Wilson
 *
 */
public class MemberProcess {
	protected final static Logger logger = Logger.getLogger(MemberProcess.class);

	public static final String COOKIE_BU_TYPE = "bu_type";
	
	private static String SESSNON_KEY = "oju76hytcvf34@#";
	private static String COOKIE_SESSNON_KEY_NAME = "cm";

	public static Member validateLogin(String username, String password) {
		MemberManager mm = DataAccessDriver.getInstance().newMemberManager();
		return mm.validateLogin(username, password);
	}
	
//	@Deprecated
//	public static Member validateLoginByAccountType(String username, String password) {
//		MemberManager mm = DataAccessDriver.getInstance().newMemberManager();
//		return mm.getBySiteUidAndSitePassword(username, password);
//	}

	@Deprecated
	public static JSONObject insertMember(String username, String password, String nickname, String consigneeCode,
			RequestContext request, ResponseContext response) {
		//因應FB APP代碼功能，增加spreaderId參數，原有API預設值為null
		//增加可可平台識別，育駿會員預設值為0
		return insertMember(username, password, nickname, consigneeCode, request, response, null, 0);
	}

	public static JSONObject insertMember(String username, String password, String nickname, String consigneeCode,
			RequestContext request, ResponseContext response, Long spreaderId) {
		//增加可可平台識別，育駿會員預設值為0
		return insertMember(username, password, nickname, consigneeCode, request, response, spreaderId, 0);
	}

	/**
	 * 因應FB APP代碼功能，增加spreaderId參數；
	 * 增加可可平台識別
	 * 
	 * @param username
	 * @param password
	 * @param nickname
	 * @param consigneeCode
	 * @param request
	 * @param response
	 * @param spreaderId
	 * @param buType - 平台類型 - 0:育駿, 1:可可平台
	 * @return
	 */
	public static JSONObject insertMember(String username, String password, String nickname, String consigneeCode,
			RequestContext request, ResponseContext response, Long spreaderId, int buType) {
		/*Wilson 20140812
		String isIpLimit = SystemGlobals.getValue(ConfigKeys.REGISTER_IP_LIMIT);
		IpLimit ipLimit = null;
		IpLimitManager ipLimitManager = null;
		String result = "1"; //默認成功
		Map map = new HashMap();
		String fromIP = "01";
		if(isIpLimit != null && isIpLimit.equals("true")){
			fromIP = request.getRemoteAddr();
			
			//IP白名单判定，如ip在白名单内，则该ip的每天注册帐号数量限制额度提升到20个
			List<String> ipWhitelist = PortalRepository.findIpInIpWhiteList();
			int limitCount = 3;
			if(ipWhitelist.contains(fromIP)){
				limitCount = 20;
			}
			
			ipLimitManager = DataAccessDriver.getInstance().newIpLimitManager();
			
			java.sql.Date recordDate = new java.sql.Date(System.currentTimeMillis());
			ipLimit = ipLimitManager.getByIpAndToday(fromIP, recordDate);
			System.out.print("==============insertMember username:" + username + " ## fromIP : " + fromIP + "============");
			if(ipLimit == null || (ipLimit != null && ipLimit.getIpCount() < limitCount)){
				result = "01";
				if(ipLimit != null){
					System.out.println(username + "=============ipLimit content : " + ipLimit.toString());
				}else {
					System.out.println("===============ipLimit content :  empty, from ip :" + fromIP);
				}
			}else{
				result = "00";//ip限制註冊帳號
				if(ipLimit != null){
					System.out.println(username + "==============ipLimit content2 : " + ipLimit.toString());
				}else {
					System.out.println("==============ipLimit content2 :  empty, from ip : " + fromIP);
				}
			}
		}else{
			result = "01";
		}
		*/
		String result = "01";
		Map map = new HashMap();
		if(result != null && result.equals("01")){
			MemberSession memberSession = MemberSessionFacade.getMemberSession();
			if(memberSession != null && !memberSession.getUsername().equals(username)){
				MemberProcess.logoutSite(memberSession);
			}
			int memberId = memberSession.getMemberId().intValue();

			Member m = new Member();
			MemberManager dao = DataAccessDriver.getInstance().newMemberManager();

			boolean error = false;
			if (username == null || username.trim().equals("") || password == null || password.trim().equals("")) {
				request.setAttribute("error", "請輸入帳號、密碼！");
				result = "03";
			}

			if (username != null) {
				username = username.trim();
			}
			if (nickname != null) {
				nickname = nickname.trim();
			}

			if (!error && dao.checkUsernameRegistered(username)) {
				request.setAttribute("error", "此帳號已存在"); //判断只能是数字或字母
				result = "02";
			}

			if (result != null && !result.equals("01")) {
				map.put("result", result);
				map.put("member", null);
				JSONObject json = JSONObject.fromObject(map);
				return json;
			} else {
/*Wilson 20140812
				Long parentMemberId = Long.valueOf(0);
				if (consigneeCode != null && !consigneeCode.trim().equals("")) {
					ConsigneeInfo c = ConsigneeInfoRepository.getByConsigneeCode(consigneeCode);
					parentMemberId = c.getMember();
				}
				if (parentMemberId.intValue() != 0) {
					m.setParentMemberId(parentMemberId);
				}*/
				m.setUserName(username);
				m.setSiteUid(username);//會員註冊時，會同時保存username和site_uid，而且這兩個值要相同，包括通過api進來註冊的外站會員也是這樣；
				m.setPassword(MD5.crypt(password));
				m.setNickName(nickname);
				//m.setCreateday(new java.sql.Date(new Date().getTime()));
				m.setCreateday(new Timestamp(System.currentTimeMillis()));
				m.setStatus(Member.NORMAL);

				if (spreaderId != null && spreaderId != 0) {
					m.setSpreaderId(spreaderId);
				}

				Member newMember = dao.save(m);
				//添加登錄member session start
				if (newMember != null) {
					//獲取會員VIP等級\經驗\積分
					padMemberPropertyProcess(newMember, request);
					//更改session
					loginSuccessProcess(memberSession, newMember, request, response, buType);
					/*Wilson 20140812
					if(isIpLimit != null && isIpLimit.equals("true")){
						if(ipLimit == null){
							ipLimit = new IpLimit();
							ipLimit.setIpCount(0);
							ipLimit.setIp(fromIP);
							ipLimit.setToday(new java.sql.Date(System.currentTimeMillis()));
						}
						ipLimit.setIpCount(ipLimit.getIpCount()+1);
						ipLimitManager.save(ipLimit);
					}*/
					//保存廣告追踪內容
//					saveAdTrack(request, response, "registers", 1, newMember.getUserName()); // 注释 by wilson 20140812
					//			this.setViewName("member.jsp");
					map.put("result", result);
					map.put("memberId", newMember.getId().toString());
					
					JSONObject json = JSONObject.fromObject(map);
					return json;
				} else {
					map.put("result", "04");
					map.put("memberId", null);
					JSONObject json = JSONObject.fromObject(map);
					return json;
				}
				//添加登錄member session end
			}
		
		
		}else{
			map.put("result", result);
			map.put("memberId", null);
			JSONObject json = JSONObject.fromObject(map);
			return json;
		}

	}

	/**
	 * 獲取會員VIP等級\經驗\積分
	 * @param member
	 */
	public static void padMemberPropertyProcess(Member member, RequestContext request) {
//		PaymentManager pm = DataAccessDriver.getInstance().newPaymentManager();
//		ExperienceRecordManager em = DataAccessDriver.getInstance().newExperienceRecordManager();
//		RecordManager rm = DataAccessDriver.getInstance().newRecordManager();
		
		//判斷會員當天是否有登入過,如果沒有登入過，則為其增加一次登錄經驗積分
		/**先下架
		if (!em.checkMemberLoginToday(member.getId())) {
			
			ExperienceRecord er = new ExperienceRecord();
			er.setMember(member.getId());
			er.setPoint(SystemGlobals.getIntValue(ConfigKeys.EXPERIENCE_POINT_FOR_LOGIN_TODY));
			er.setLogip(request.getRemoteAddr());
			er.setLogtime(new java.sql.Date(new Date().getTime()));
			er.setDescription(SystemGlobals.getValue(ConfigKeys.EXPERIENCE_ADD_BY_LOGIN_TODAY));
			er.setStatus(ExperienceRecord.NORMAL);
			em.save(er);
			
			//註銷在緩存中的會員經驗積分餘額記錄
			AccountRepository.removeAccountExperienceByMemberId(member.getId());
		}
		**/
		//取得會員當前儲值育駿幣總數、經驗數、積分
		/*不用此做法，改讀AccountMoney、AccountScore、AccountExperience
		member.setAmount(pm.getTotalPointByMemberId(member.getId()).intValue());
		member.setExperience(em.getTotalPointsByMemberId(member.getId()).intValue());
		member.setRestAmount(rm.getMemberRemainingPoints(member.getId()).intValue());
		member.setScore(getMemberScore(member));
		*/
	}

	/**
	 * 取得會員積分
	 * @param member
	 * modify by Wilson 20140812
	 
	public static int getMemberScore(Member member){
		ScoreRecordManager scoreRecordManager = DataAccessDriver.getInstance().newScoreRecordManager();
		OrderManager om = DataAccessDriver.getInstance().newOrderManager();
		TradeMerchandiseManager tradeMerchandiseManager = DataAccessDriver.getInstance().newTradeMerchandiseManager();	
		int score = scoreRecordManager.getTotalPointsByMemberId(member.getId()).intValue();
		score += om.getTotalScoresByMemberId(member.getId()).intValue() * 0.2;//推廣所得積分
		score = score - tradeMerchandiseManager.getTotalScoreByMemberId(member.getId()).intValue();
		return score;
	}
	*/
	/**
	 * 登錄驗證會員成功后的過程處理
	 * @param memberSession
	 * @param member
	 * @param request 
	 * @param response
	 */
	public static boolean loginSuccessProcess(MemberSession memberSession, Member member, RequestContext request, ResponseContext response) {
		return loginSuccessProcess(memberSession, member, request, response, 0);
	}

	/**
	 * 登錄驗證會員成功後的過程處理 - 增加可可平台識別
	 * 
	 * @param memberSession
	 * @param member
	 * @param request
	 * @param response
	 * @param buType - 平台類型 - 0:育駿, 1:可可平台
	 * @return
	 */
	public static boolean loginSuccessProcess(MemberSession memberSession, Member member, RequestContext request, ResponseContext response, int buType) {
		MemberSessionFacade.makeLogged();
		
		String sessionId = MemberSessionFacade.isMemberInSession(member.getId().intValue());
		memberSession = new MemberSession(MemberSessionFacade.getMemberSession());
		
		// Remove the "guest" session
		if (memberSession.getMemberId().intValue() == SystemGlobals.getIntValue(ConfigKeys.ANONYMOUS_USER_ID)) {
			MemberSessionFacade.remove(memberSession.getSessionId());
		} 
//		else {//當前一帳號還沒登出的情況下，返回false
//			return false;
//		}

		memberSession.dataToMember(member);
		
		MemberSession currentUs = MemberSessionFacade.getMemberSession(sessionId);
		
		// Check if the user is returning to the system
		// before its last session has expired ( hypothesis )
		MemberSession tmpUs;
		if (sessionId != null && currentUs != null) {
			// Write its old session data
			MemberSessionFacade.storeSessionData(sessionId, GPortalExecutionContext.getConnection());
			tmpUs = new MemberSession(currentUs);
			MemberSessionFacade.remove(sessionId);
		} else {
			MemberSessionManager sm = DataAccessDriver.getInstance().newMemberSessionManager();
			tmpUs = sm.getById(memberSession, GPortalExecutionContext.getConnection());
		}
		
//			I18n.load(user.getLang());
		
		MemberManager memberManager = DataAccessDriver.getInstance().newMemberManager();
		// Autologin
		if (SystemGlobals.getBoolValue(ConfigKeys.AUTO_LOGIN_ENABLED)) {
			memberSession.setAutoLogin(true);
			
			// Generate the user-specific hash
			/*
			String systemHash = MD5.crypt(SystemGlobals.getValue(ConfigKeys.USER_HASH_SEQUENCE) + member.getId());
			String userHash = MD5.crypt(System.currentTimeMillis() + systemHash);

			// Persist the user hash
			
			memberManager.saveMemberAuthHash(member.getId().intValue(), userHash);
			
			systemHash = MD5.crypt(userHash);*/
			
			ControllerUtils.addCookie(SystemGlobals.getValue(ConfigKeys.COOKIE_AUTO_LOGIN), "1");
//			ControllerUtils.addCookie(SystemGlobals.getValue(ConfigKeys.COOKIE_USER_HASH), systemHash);
		} else {
			// Remove cookies for safety
			ControllerUtils.addCookie(SystemGlobals.getValue(ConfigKeys.COOKIE_USER_HASH), null);
//			ControllerUtils.addCookie(SystemGlobals.getValue(ConfigKeys.COOKIE_AUTO_LOGIN), null);
		}
		
		member.setLoginTime(new Timestamp(System.currentTimeMillis()));
		if (member.getLoginCount() == null || member.getLoginCount() == 0) {
			member.setLoginCount(1);
		} else {
			member.setLoginCount(member.getLoginCount() + 1);
		}
		//針對已經註冊過的育駿會員，在其登入時，會先檢查site_uid是否為空，若為空則把username複製到site_uid，並保存到資料庫及cache；
		String siteUid = member.getSiteUid();
		if (siteUid == null || siteUid.trim().equals("")) {//如果siteUid为空，则使用username初始化
			member.setSiteUid(member.getUserName());
			memberSession.setSiteUid(member.getUserName());
			siteUid = member.getUserName();
		}else {//如果siteUid不为空，则设为null，以便在updateLoginCountOrAndSiteUid方法中不用对siteUid进行更新
			siteUid = null;
		}
		memberManager.updateLoginCountAndLoginTimeOrAndSiteUid(member.getId(), member.getLoginCount(), member.getLoginTime(), siteUid);
		
		
		
		//saveAdTrack(request, response, "logins", 1, member.getUserName());		modify by wilson 20140812
		
		if (tmpUs == null) {
			memberSession.setLastVisit(new Date(System.currentTimeMillis()));
			memberSession.setIp(request.getRemoteAddr());
		} else {
			// Update last visit and session start time
			memberSession.setLastVisit(new Date(tmpUs.getStartTime().getTime() + tmpUs.getSessionTime()));
		}
		
		//增加可可平台識別
		memberSession.setBuType(buType);

		MemberSessionFacade.add(memberSession);
//		MemberSessionFacade.setAttribute(ConfigKeys.TOPICS_READ_TIME, new HashMap());
		ControllerUtils.addCookie(SystemGlobals.getValue(ConfigKeys.COOKIE_NAME_DATA), Integer.toString(member.getId()
				.intValue()));
		
//			SecurityRepository.load(member.getId(), true);
//			validInfo = true;
		ControllerUtils.addCookie(SystemGlobals.getValue(ConfigKeys.COOKIE_NAME_USER), member.getUserName());
		ControllerUtils.addCookie("cmwebgame_nickname", EscapeUnescape.escape(member.getNickName()));
		/* by wilson 20140812
		ConsigneeInfo consigneeInfo = ConsigneeInfoRepository.getByMemberId(member.getParentMemberId());
		if(consigneeInfo != null){
			ControllerUtils.addCookie(SystemGlobals.getValue(ConfigKeys.COOKIE_NAME_CONSIGNEE_CODE), consigneeInfo.getConsigneeCode());
		}
		*/
		//使用3DES加密用戶MemberSession關鍵信息并增加到cookie，以便memberSession time out時restore
//		saveMemberSessionToCooke(memberSession, request);
		
		ControllerUtils.addCookie(COOKIE_BU_TYPE, String.valueOf(buType));

		String site_uname = null;
		try {
			site_uname = new String(org.apache.commons.codec.binary.Base64.encodeBase64(member.getSiteUid().getBytes("UTF-8")), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		ControllerUtils.addCookie("site_uname", site_uname);

		return true;
	}
	
	/**
	 * 用戶登出網站的處理過程
	 * @param memberSession
	 */
	public static void logoutSite(MemberSession memberSession){
		
		logoutMemberSession(memberSession);
		clearCookieForSite();
	}
	
	public static void clearCookieForSite(){
		ControllerUtils.addCookie(SystemGlobals.getValue(ConfigKeys.COOKIE_NAME_USER), null);
		ControllerUtils.addCookie(SystemGlobals.getValue(ConfigKeys.COOKIE_SESSION_DATA), null);
		ControllerUtils.addCookie(SystemGlobals.getValue(ConfigKeys.COOKIE_MEMBERSESSION_DATA), null);
		ControllerUtils.addCookie("cmwebgame_nickname", null);
		ControllerUtils.addCookie(SystemGlobals.getValue(ConfigKeys.COOKIE_USER_HASH), null);
		ControllerUtils.addCookie(SystemGlobals.getValue(ConfigKeys.COOKIE_AUTO_LOGIN), null);
		ControllerUtils.addCookie(SystemGlobals.getValue(ConfigKeys.COOKIE_NAME_CONSIGNEE_CODE), null);
		ControllerUtils.addCookie(COOKIE_BU_TYPE, null);
	}
	
	public static MemberSession logoutMemberSession(MemberSession memberSession){
		//先清除cache裡的經驗、儲值、積分三項的餘額記錄及總額記錄
		/* by wilson 20140812
		AccountRepository.removeAccountExperienceByMemberId(memberSession.getMemberId());
		AccountRepository.removeAccountMoneyByMemberId(memberSession.getMemberId());
		AccountRepository.removeAccountScoreByMemberId(memberSession.getMemberId());
		*/
		MemberSessionFacade.storeSessionData(memberSession.getSessionId(), GPortalExecutionContext.getConnection());

		MemberSessionFacade.makeUnlogged();
		MemberSessionFacade.remove(memberSession.getSessionId());
		

		// Disable auto login
		memberSession.setAutoLogin(false);
		memberSession.makeAnonymous();

		MemberSessionFacade.add(memberSession);
		return memberSession;
	}
	/**
	 * 記錄廣告追踪內容
	 * @param request
	 * @param response
	 * @param field
	 * @param times
	 * @param username
	 * modify by wilson 20140812
	
	public static void saveAdTrack(RequestContext request, ResponseContext response, String field, int times, String username){
		//取得cookie中的广告id
		Cookie cookies[] = request.getCookies();
		String bannerid = null;
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				if ("as_id".equals(cookies[i].getName())) {
					bannerid = cookies[i].getValue();
					break;
				}
			}
		}
		//根据cookie中的广告id记录广告统计，此处记录旧会员登入统计
		if(bannerid != null && !bannerid.trim().equals("")){
			AdTrackStatisticsManager adTrackStatisticsManager = DataAccessDriver.getInstance().newAdTrackStatisticsManager();
			AdTrackStatistics adTrackStatistics = adTrackStatisticsManager.getById(new Long(bannerid));
//			adTrackStatistics.setLogins(adTrackStatistics.getLogins()+1);
//			adTrackStatisticsManager.save(adTrackStatistics);
			adTrackStatisticsManager.addTimesForAdTrackStatistics(new Long(bannerid),field,times);
			
			AtsMemberRecordManager atsMemberRecordManager = DataAccessDriver.getInstance().newAtsMemberRecordManager();
			AtsMemberRecord atsMemberRecord = atsMemberRecordManager.getByUsername(username);
			if(atsMemberRecord != null){
				atsMemberRecord.setAdTrackStatistics(new Long(bannerid));
				atsMemberRecordManager.save(atsMemberRecord);
			}else{
				atsMemberRecord = new AtsMemberRecord();
				atsMemberRecord.setFromIp(request.getRemoteAddr());
				atsMemberRecord.setUsername(username);
				atsMemberRecord.setIsRecord(AtsMemberRecord.STATUS_NOT_RECORD);//第一次從廣告最終碼進來的會員，屬於未記錄會員，方便接下來記錄創角數目
				atsMemberRecord.setAdTrackStatistics(adTrackStatistics.getId());
				atsMemberRecordManager.save(atsMemberRecord);
			}
			//使用完该cookie进行记录统计后，进行删除
			Cookie cookie = new Cookie("as_id","");
			cookie.setDomain("cmwebgame.com");
			cookie.setPath("/");
			cookie.setMaxAge(0);
			response.addCookie(cookie);
		}
	}
 */
	

	/**
	 * 打開會員編輯頁面的過程處理
	 */
	public static void profileProcess(RequestContext request, ServletContext servletContext) {
		MemberSession memberSession = MemberSessionFacade.getMemberSession();
		Member member = DataAccessDriver.getInstance().newMemberManager().getByIdAndStatus(memberSession.getMemberId(),
				Member.NORMAL);
		int birthYear = 0;
		int birthMonth = 0;
		int birthDay = 0;
		Calendar calendar = Calendar.getInstance();
		Date birthday = member.getBirthday();
		if (birthday != null) {
			calendar.setTime(birthday);
			birthYear = calendar.get(Calendar.YEAR);
			birthMonth = calendar.get(Calendar.MONTH) + 1;
			birthDay = calendar.get(Calendar.DAY_OF_MONTH);
		}
//		List<County> countyList = PortalRepository.findAllCounties(); 注释 by wilson 20140812
		if (member.getCountyId() != null) {
			request.setAttribute("countyId", member.getCountyId());
		}
		request.setAttribute("birthYear", birthYear);
		request.setAttribute("birthMonth", birthMonth);
		request.setAttribute("birthDay", birthDay);
		request.setAttribute("member", member);
//		request.setAttribute("countyList", countyList); 注释 by wilson 20140812
		request.setAttribute("GPortalContext", servletContext.getAttribute("GPortalContext"));
	}

	@Deprecated
	public static void saveMemberServer(MemberSession memberSession, Long serverId) {
		saveMemberServer(memberSession, serverId, null);
	}

	/**
	 * 添加會員註冊遊戲服務器記錄
	 * 
	 * @param memberSession
	 * @param serverId
	 * @param response
	 */
	public static void saveMemberServer(MemberSession memberSession, Long serverId, String promoteCode) {
		MemberManager memberManager = DataAccessDriver.getInstance().newMemberManager();

		if (!MemberSessionFacade.checkExistByMemberIdAndServerId(memberSession.getMemberId(), serverId)) {
			memberManager.addMemberServer(memberSession.getMemberId(), serverId, promoteCode);
			MemberSessionFacade.updateServerIdsByMemberId(memberSession.getMemberId());

			//每開通一個遊戲伺服器則贈送一定數額的經驗值
			/**先下架
			ExperienceRecordManager em = DataAccessDriver.getInstance().newExperienceRecordManager();
			ExperienceRecord er = new ExperienceRecord();
			er.setMember(memberSession.getMemberId());
			er.setPoint(SystemGlobals.getIntValue(ConfigKeys.EXPERIENCE_POINT_FOR_SERVER_OPEN));
			er.setLogip(memberSession.getIp());
			er.setLogtime(new java.sql.Date(new Date().getTime()));
			er.setDescription(SystemGlobals.getValue(ConfigKeys.EXPERIENCE_ADD_BY_SERVER_OPEN));
			er.setStatus(ExperienceRecord.NORMAL);
			em.save(er);
			**/
			//註銷在緩存中的會員經驗積分餘額記錄
//			AccountRepository.removeAccountExperienceByMemberId(memberSession.getMemberId()); 注释 by wilson 20140812
		}
	}
	
	
	
	
	/**
	 * 加密sessionId和username组合信息并增加到cookie，以便memberSession time out時restore
	 * @param memberSession
	 * @param request
	 */
	public static void saveSessionInfoToCooke(MemberSession memberSession, RequestContext request) {
		String result = encodeMemberSession(memberSession, request);
		String md5_result = MD5.crypt(result + SESSNON_KEY);
		ControllerUtils.addCookie(SystemGlobals.getValue(ConfigKeys.COOKIE_MEMBERSESSION_DATA), result);
		ControllerUtils.addCookie(COOKIE_SESSNON_KEY_NAME, md5_result);
	}
	
	/**
	 * 把sessionId和username组合后经过序列化，把序列化结果进行加密
	 * @param memberSession
	 * @param request
	 * @return
	 */
	public static String encodeMemberSession(MemberSession memberSession, RequestContext request){
		/*先使用以下方式生成Key ————
		MD5(<client-ip>:<shared-secret>  ：<user-agent>) 
                     其中，<client-ip>為客戶端的IP 位址，格式為’nnn.nnn.nnn.nnn’；
		<shared-secret>為預先選好的固定密碼与會員加密后的密碼組合字符串，預先選好的固定密碼必須由密碼產生器產生，長度至少不小於16 個字元； 
        <user-agent>為瀏覽器傳送過來的’User-agent’header 值。
                     上述三個元素，與’:’字符按 照上述順序串接(concatenate)後，求得其MD5 哈希值(hash value)  ，哈希值共有16 bytes
		*/
		String shared_secret = MD5.crypt(SystemGlobals.getValue(ConfigKeys.MEMBERSESSION_SHARED_SECRET));
		String userAgent = request.getHeader("user-agent");
		String keyMD5 = MD5.crypt(shared_secret + ":" + userAgent).toLowerCase().substring(0, 24);
//		final byte[] keyBytes = keyMD5.getBytes();//加密的key
		String content = memberSession.getSessionId() + "&" + memberSession.getUsername();//加密内容
		String encodeResult = null;
		/*
		 * 把memberSession序列化(serialized)
		 
		String encodeResult = null;
		String encodeString = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos;
			oos = new ObjectOutputStream(baos);
			oos.writeObject(content);
			encodeString = baos.toString("ISO-8859-1");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
        
		// 添加新安全算法,如果用JCE就要把它添加进去
//		Security.addProvider(new com.sun.crypto.provider.SunJCE());
//		Security.addProvider(new BouncyCastleProvider());
//		encodeResult = ThreeDes.DataEncrypt(encodeString,keyMD5);
		try {
			encodeResult = URLEncoder.encode(content, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		ControllerUtils.addCookie(SystemGlobals.getValue(ConfigKeys.COOKIE_MEMBERSESSION_DATA), encodeResult);
		
		return encodeResult;
	}
	
	/**
	 * 從cookie里獲取加密过的資料，進行解密，解密後再反序列化，得到sessionId&username
	 * @param request
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static String decodeMemberSession(RequestContext request) throws IOException, ClassNotFoundException{

		String COOKIE_NAME = SystemGlobals
				.getValue(ConfigKeys.COOKIE_MEMBERSESSION_DATA);
		Cookie cookie = ControllerUtils.getCookie(COOKIE_NAME);
		Cookie cookieHash = ControllerUtils.getCookie(COOKIE_SESSNON_KEY_NAME);
//		Cookie cookie2 = ControllerUtils.getCookie("cmwebgame_nickname");
		
		
		if (cookie != null && cookieHash != null) {
			
//			if(cookie2 != null){
//				if(cookie2.getValue() != null && cookie2.getValue().indexOf("uxura@gb") != -1){
//					Cookie[] cookie3 = request.getCookies();
//					for(Cookie cookie4 : cookie3){
//						System.out.print("===decodeMemberSession cookie name== " + cookie4.getName()  + " : value:" + cookie4.getValue());
//						System.out.print("===decodeMemberSession cookie name== " + cookie4.getName()  + " : length:" + cookie4.getValue().length());
//					}
//				}
//				
//			}
			
			String cookieValue = "";
			String cookieHashValue = "";
			cookieValue = cookie.getValue();
			cookieHashValue = cookieHash.getValue();

			String userAgent = request.getHeader("user-agent");
			String shared_secret = MD5.crypt(SystemGlobals
					.getValue(ConfigKeys.MEMBERSESSION_SHARED_SECRET));
			// 生成解密用的key
			String keyMD5 = MD5.crypt(
					shared_secret + ":" + userAgent).toLowerCase()
					.substring(0, 24);
//			final byte[] keyBytes = keyMD5.getBytes();
			// 進行解密
			if (cookieValue != null && !cookieValue.trim().equals("")) {
				if(MD5.crypt(cookieValue+SESSNON_KEY).equals(cookieHashValue)){
					try {
						return URLDecoder.decode(cookieValue, "UTF-8");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else {
					return null;
				}
				
				/*
				String decryptString = ThreeDes.DataDecrypt(cookieValue,
						keyMD5);
				if (decryptString != null && !decryptString.equals("")) {
					// 反序列化memberSession
					ByteArrayInputStream bais = new ByteArrayInputStream(decryptString.getBytes("ISO-8859-1"));
					ObjectInputStream ios = new ObjectInputStream(bais);
					return (String) ios.readObject();

				}*/
			}
		}
		
		return null;
	
	}
}
