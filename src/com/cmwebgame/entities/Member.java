package com.cmwebgame.entities;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.cmwebgame.dao.IdEntity;

/**
 * 會員.
 * 
 * @author wenson
 */
public class Member extends IdEntity { 
	
	public static final int GENDER_COUNT = 2;
	public static final String[] GENDERS = {"保密", "男", "女"};
	public static final int MAN = 1;
	public static final int WOMEN = 2;
	
    public static final int FACEBOOK_ACCOUNT_COUNT = 2;
	public static final String[] FACEBOOK_ACCOUNT = {"保密", "是", "否"};
	public static final int FACEBOOK_ACCOUNT_YES = 1;
	public static final int FACEBOOK_ACCOUNT_NO = 0;
	
	public static final int YES = 1;
	public static final int NO = 0;
	
	public static final int STATUS_COUNT = 3;
	public static final String[] STATUS = {"未設置", "正常", "停權", "刪除"};
	public static final int NORMAL = 1;
	public static final int STOP = 2;
	public static final int DELETE = 3;
	
	public static final int AVATAR_STATUS_COUNT = 2;
	public static final String[] AVATAR_STATUS_ = {"未設置", "未驗證", "已驗證"};
	public static final int NO_PASS = 0;
	public static final int PASS = 1;
	

	private String userName;
	private String password; 
	private String nickName; 
	private String email;
	private Timestamp loginTime;
	private String name;
	private int gender;
	private Date birthday;
	//private Date createday = new java.sql.Date(new java.util.Date().getTime());
	private Timestamp createday = new Timestamp(System.currentTimeMillis());
	private String phone;
	private String mobile;
	private String idCard;
	private Long countyId = Long.valueOf(0);
	private String address;
	private String safeEmail;
	private Long parentMemberId =new Long(1);
	private Long spreaderMemberId =new Long(1);//推廣人ID
	private String siteUid; //從合作網站過來的會員,這邊會紀錄合作網站會員申請的育駿id,格式為id@site
	private String sitePassword;//從合作網站過來的會員所設定的密碼
	
	private String buy_sn; //通路王專用-推廣授權碼
	private String buy_kind; //通路王專用-回饋用標籤
	private String partner_sn; //通路王專用-廠商合作項目代號
	private String cookieValue; //通路王專用-OEYA生成的cookie值
	
	private String populace; //所有服務器人口 格式：伺服器名稱：人數<br>伺服器名稱：人數
	private String serverunion; //所有服務器人口 格式：伺服器名稱：聯盟名稱<br>伺服器名稱：人數
	
	private Integer loginCount=Integer.valueOf(0); //登入次數
	private Integer payCount = Integer.valueOf(0); //成功儲值次數
	/*
	private Integer validateEmail = 0; //是否通過EMAIL驗證：1，通過驗證；0，未通過驗證 默認值設為0
	private String validateCode;	//Email驗證碼
	*/
//	private Set<Member> members = new LinkedHashSet<Member>();
//	private Set<Server> servers = new LinkedHashSet<Server>(); //有序的关联对象集合.
	private List serverlList;
	
	@Deprecated
	private int experience;  //个人经验，每日首次登入得5經驗,只是臨時變量，用于cache
	@Deprecated
	private int amount;  //個人成功儲值總數量，只是臨時變量，用于cache
	@Deprecated
	private int score;   //個人積分總數，只是臨時變量，用于cache
	@Deprecated
	private int restAmount;  //當前剩余育駿幣數量，只是臨時變量，用于cache
	
	private int status = this.NORMAL;
	
	private int isFacebookAccount = this.NO;
	
	private String avatar;//頭像圖片名稱
	
	private int avatarStatus = this.NO_PASS;//頭像圖片是否通過驗證
	
	private Long spreaderId = new Long(0); //所屬推廣人Id，即spreader,默認為0，即無推廣人
	
	public Long getSpreaderId() {
		return spreaderId;
	}

	public void setSpreaderId(Long spreaderId) {
		this.spreaderId = spreaderId;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public int getAvatarStatus() {
		return avatarStatus;
	}

	public void setAvatarStatus(int avatarStatus) {
		this.avatarStatus = avatarStatus;
	}

	public int getIsFacebookAccount() {
		return isFacebookAccount;
	}

	public void setIsFacebookAccount(int isFacebookAccount) {
		this.isFacebookAccount = isFacebookAccount;
	}

	public int getRestAmount() {
		return restAmount;
	}

	public void setRestAmount(int restAmount) {
		this.restAmount = restAmount;
	}

	public int getExperience() {
		return experience;
	}

	public void setExperience(int experience) {
		this.experience = experience;
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public List getServerlList() {
		return serverlList;
	}

	public Member() {
		this.serverlList = new ArrayList();
	}
	
	public Long getParentMemberId() {
		return parentMemberId;
	}

	public void setParentMemberId(Long parentMemberId) {
		this.parentMemberId = parentMemberId;
	}
	
	public Long getSpreaderMemberId() {
		return spreaderMemberId;
	}

	public void setSpreaderMemberId(Long spreaderMemberId) {
		this.spreaderMemberId = spreaderMemberId;
	}
	
	public String getSafeEmail() {
		return safeEmail;
	}

	public void setSafeEmail(String safeEmail) {
		this.safeEmail = safeEmail;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public Long getCountyId() {
		return countyId;
	}

	public void setCountyId(Long countyId) {
		this.countyId = countyId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

//	@Index(name="userNameIndex", columnNames={"userName"})
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Timestamp getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Timestamp loginTime) {
		this.loginTime = loginTime;
	}

	public Timestamp getCreateday() {
		return createday;
	}

	public void setCreateday(Timestamp createday) {
		if(createday == null){
			createday = new Timestamp(System.currentTimeMillis());
		}
		this.createday = createday;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getBuy_sn() {
		return buy_sn;
	}

	public void setBuy_sn(String buy_sn) {
		this.buy_sn = buy_sn;
	}
	
	public String getBuy_kind() {
		return buy_kind;
	}

	public void setBuy_kind(String buy_kind) {
		this.buy_kind = buy_kind;
	}
	
	public String getPartner_sn() {
		return partner_sn;
	}

	public void setPartner_sn(String partner_sn) {
		this.partner_sn = partner_sn;
	}
	
	public String getCookieValue() {
		return cookieValue;
	}

	public void setCookieValue(String cookieValue) {
		this.cookieValue = cookieValue;
	}
	
	public String getPopulace() {
		return populace;
	}

	public void setPopulace(String populace) {
		this.populace = populace;
	}
	
	public String getServerunion() {
		return serverunion;
	}

	public void setServerunion(String serverunion) {
		this.serverunion = serverunion;
	}
	
	public Integer getLoginCount() {
		return loginCount;
	}

	public void setLoginCount(Integer loginCount) {
		this.loginCount = loginCount;
	}
	
	public Integer getPayCount() {
		return payCount;
	}

	public void setPayCount(Integer payCount) {
		this.payCount = payCount;
	}
	
	public String getSiteUid() {
		return siteUid;
	}

	public void setSiteUid(String siteUid) {
		this.siteUid = siteUid;
	}

	public String getSitePassword() {
		return sitePassword;
	}

	public void setSitePassword(String sitePassword) {
		this.sitePassword = sitePassword;
	}

	/*
	public Integer getValidateEmail() {
		return validateEmail;
	}

	public void setValidateEmail(Integer validateEmail) {
		this.validateEmail = validateEmail;
	}
	
	public String getValidateCode() {
		return validateCode;
	}

	public void setValidateCode(String validateCode) {
		this.validateCode = validateCode;
	}
	*/
	
}