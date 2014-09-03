package com.agloco.form;

import org.apache.struts.action.ActionForm;

public class LogMessageForm extends ActionForm
{
	private String logDate = "";

	private String operate = "";
	
	private String userId = "";

	private String emailAddress = "";

	private String emailAddressType = "";

	private String memberCode = "";

	private String memberCodeType = "";

	private String referralCode = "";

	private String referralCodeType = "";

	private String ip = "";

	private String ipType = "";

	private String priority = "";

	private Long pageSize = new Long(0);

	private Long pageNum = new Long(0);

	private int totalResult = 0;

	public String getEmailAddress()
	{
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress)
	{
		this.emailAddress = emailAddress.trim();
	}

	public String getEmailAddressType()
	{
		return emailAddressType;
	}

	public void setEmailAddressType(String emailAddressType)
	{
		this.emailAddressType = emailAddressType.trim();
	}

	public String getIp()
	{
		return ip;
	}

	public void setIp(String ip)
	{
		this.ip = ip.trim();
	}

	public String getIpType()
	{
		return ipType;
	}

	public void setIpType(String ipType)
	{
		this.ipType = ipType;
	}

	public String getMemberCode()
	{
		return memberCode;
	}

	public void setMemberCode(String memberCode)
	{
		this.memberCode = memberCode.trim();
	}

	public String getMemberCodeType()
	{
		return memberCodeType;
	}

	public void setMemberCodeType(String memberCodeType)
	{
		this.memberCodeType = memberCodeType;
	}

	public String getOperate()
	{
		return operate;
	}

	public void setOperate(String operate)
	{
		this.operate = operate;
	}

	public String getReferralCode()
	{
		return referralCode;
	}

	public void setReferralCode(String referralCode)
	{
		this.referralCode = referralCode.trim();
	}

	public String getReferralCodeType()
	{
		return referralCodeType;
	}

	public void setReferralCodeType(String referralCodeType)
	{
		this.referralCodeType = referralCodeType;
	}

	public String getLogDate()
	{
		return logDate;
	}

	public void setLogDate(String logDate)
	{
		this.logDate = logDate;
	}

	public Long getPageNum()
	{
		return pageNum;
	}

	public void setPageNum(Long pageNum)
	{
		this.pageNum = pageNum;
	}

	public Long getPageSize()
	{
		return pageSize;
	}

	public void setPageSize(Long pageSize)
	{
		this.pageSize = pageSize;
	}

	public String getPriority()
	{
		return priority;
	}

	public void setPriority(String priority)
	{
		this.priority = priority;
	}

	public int getTotalResult()
	{
		return totalResult;
	}

	public void setTotalResult(int totalResult)
	{
		this.totalResult = totalResult;
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId.trim();
	}

}
