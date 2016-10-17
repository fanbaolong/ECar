package com.xieyu.ecar.bean;

import java.io.Serializable;

/**
 * @author fbl
 * 
 *         用户数据
 *
 */
public class User implements Serializable
{

	// companyName : null
	// level : 0

	private static final long serialVersionUID = -5676685135675303700L;
	// 用户ID
	private int id;

	private String createTime;

	private String updateTime;

	private boolean onDeleted;

	private String orderNo;

	private String remarks;
	// 用户名
	private String userName;
	// 电话
	private String telPhone;

	private String passWord;
	// 邮箱
	private String email;
	// 地址
	private String address;
	// 名称
	private String name;
	// 身份证
	private String license;
	// 身份证图片
	private String licensePath1;
	// 身份证图片
	private String licensePath2;
	// 身份证图片审核状态
	private String licenseExamineState;
	// 驾驶证
	private String drivingLicense;
	// 驾驶证图片
	private String driveImgPath1;
	// 驾驶证图片
	private String driveImgPath2;
	// 驾驶证图片审核状态
	private String drivingLicenseExamineState;
	// 身份证审核失败原因
	private String failureCauseForlicense;
	// 驾驶证审核失败原因
	private String failureCauseFordrivingLicense;
	// 性别
	private String gender;

	private String webChatID;
	// 余额
	private double balance;
	// 冻结金额
	private double freezeBalance;

	private String driveImgPath;

	private int voucher;

	private String lastLoginTime;

	private String companyName;

	private int level;

	private String memberType;

	public String getFailureCauseForlicense()
	{
		return failureCauseForlicense;
	}

	public void setFailureCauseForlicense(String failureCauseForlicense)
	{
		this.failureCauseForlicense = failureCauseForlicense;
	}

	public String getFailureCauseFordrivingLicense()
	{
		return failureCauseFordrivingLicense;
	}

	public void setFailureCauseFordrivingLicense(String failureCauseFordrivingLicense)
	{
		this.failureCauseFordrivingLicense = failureCauseFordrivingLicense;
	}

	public String getAddress()
	{
		return address;
	}

	public void setAddress(String address)
	{
		this.address = address;
	}

	public String getLicenseExamineState()
	{

		return licenseExamineState;
	}

	public void setLicenseExamineState(String licenseExamineState)
	{
		this.licenseExamineState = licenseExamineState;
	}

	public String getDriveImgPath1()
	{
		return driveImgPath1;
	}

	public void setDriveImgPath1(String driveImgPath1)
	{
		this.driveImgPath1 = driveImgPath1;
	}

	public String getDriveImgPath2()
	{
		return driveImgPath2;
	}

	public void setDriveImgPath2(String driveImgPath2)
	{
		this.driveImgPath2 = driveImgPath2;
	}

	public String getDrivingLicenseExamineState()
	{
//		if (null == drivingLicenseExamineState)
//		{
//			return "未上传";
//		}
		return drivingLicenseExamineState;
	}

	public void setDrivingLicenseExamineState(String drivingLicenseExamineState)
	{
		this.drivingLicenseExamineState = drivingLicenseExamineState;
	}

	public String getCompanyName()
	{
		return companyName;
	}

	public void setCompanyName(String companyName)
	{
		this.companyName = companyName;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public String getTelPhone()
	{
		return telPhone;
	}

	public void setTelPhone(String telPhone)
	{
		this.telPhone = telPhone;
	}

	public String getPassWord()
	{
		return passWord;
	}

	public void setPassWord(String passWord)
	{
		this.passWord = passWord;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(String createTime)
	{
		this.createTime = createTime;
	}

	public String getUpdateTime()
	{
		return updateTime;
	}

	public void setUpdateTime(String updateTime)
	{
		this.updateTime = updateTime;
	}

	public boolean isOnDeleted()
	{
		return onDeleted;
	}

	public void setOnDeleted(boolean onDeleted)
	{
		this.onDeleted = onDeleted;
	}

	public String getOrderNo()
	{
		return orderNo;
	}

	public void setOrderNo(String orderNo)
	{
		this.orderNo = orderNo;
	}

	public String getRemarks()
	{
		return remarks;
	}

	public void setRemarks(String remarks)
	{
		this.remarks = remarks;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getLicense()
	{
		return license;
	}

	public void setLicense(String license)
	{
		this.license = license;
	}

	public String getDrivingLicense()
	{
		return drivingLicense;
	}

	public void setDrivingLicense(String drivingLicense)
	{
		this.drivingLicense = drivingLicense;
	}

	public String getGender()
	{
		return gender;
	}

	public void setGender(String gender)
	{
		this.gender = gender;
	}

	public String getWebChatID()
	{
		return webChatID;
	}

	public void setWebChatID(String webChatID)
	{
		this.webChatID = webChatID;
	}

	public double getBalance()
	{
		return balance;
	}

	public void setBalance(double balance)
	{
		this.balance = balance;
	}

	public double getFreezeBalance()
	{
		return freezeBalance;
	}

	public void setFreezeBalance(double freezeBalance)
	{
		this.freezeBalance = freezeBalance;
	}

	public String getDriveImgPath()
	{
		return driveImgPath;
	}

	public void setDriveImgPath(String driveImgPath)
	{
		this.driveImgPath = driveImgPath;
	}

	public String getLicensePath1()
	{
		return licensePath1;
	}

	public void setLicensePath1(String licensePath1)
	{
		this.licensePath1 = licensePath1;
	}

	public String getLicensePath2()
	{
		return licensePath2;
	}

	public void setLicensePath2(String licensePath2)
	{
		this.licensePath2 = licensePath2;
	}

	public int getVoucher()
	{
		return voucher;
	}

	public void setVoucher(int voucher)
	{
		this.voucher = voucher;
	}

	public String getLastLoginTime()
	{
		return lastLoginTime;
	}

	public void setLastLoginTime(String lastLoginTime)
	{
		this.lastLoginTime = lastLoginTime;
	}

	public int getLevel()
	{
		return level;
	}

	public void setLevel(int level)
	{
		this.level = level;
	}

	public String getMemberType()
	{
		return memberType;
	}

	public void setMemberType(String memberType)
	{
		this.memberType = memberType;
	}

}
