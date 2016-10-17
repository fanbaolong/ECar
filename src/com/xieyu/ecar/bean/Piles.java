package com.xieyu.ecar.bean;

import java.io.Serializable;

/**
 * @author fbl
 * 
 * 充电桩实体
 *
 */
public class Piles implements Serializable{

	private static final long serialVersionUID = 6331854125970833142L;

	private int id;
	
	private String createTime;
	
	private String updateTime;
	
	private boolean onDeleted;
	
	private String remarks;
	/** 订单编号 */
	private String sn;
	
	private PilesStatus pilesStatus;
	
    private Sites site;
    
    private PilesCategory pilesCategory;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public boolean isOnDeleted() {
		return onDeleted;
	}

	public void setOnDeleted(boolean onDeleted) {
		this.onDeleted = onDeleted;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public PilesStatus getPilesStatus() {
		return pilesStatus;
	}

	public void setPilesStatus(PilesStatus pilesStatus) {
		this.pilesStatus = pilesStatus;
	}

	public Sites getSite() {
		return site;
	}

	public void setSite(Sites site) {
		this.site = site;
	}

	public PilesCategory getPilesCategory() {
		return pilesCategory;
	}

	public void setPilesCategory(PilesCategory pilesCategory) {
		this.pilesCategory = pilesCategory;
	}
	
	public String getPilesStatusStr(PilesStatus pilesStatus){
		String ss = "";
		
		switch (pilesStatus) {
		case Using:
			ss = "使用中";
			break;
		case Usable:
			ss = "可使用";
			break;
		case Exception:
			ss = "故障中";
			break;

		default:
			break;
		}
		
		return ss;
	}
    

}
