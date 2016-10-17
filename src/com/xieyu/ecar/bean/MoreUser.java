package com.xieyu.ecar.bean;

import java.io.Serializable;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * @author fbl
 *
 *做记住密码用
 */
@Table(name="moreuser")
public class MoreUser implements Serializable
{
	private static final long serialVersionUID = 927361822949073778L;

	@Column(name = "id", isId = true)
	private int id;
	@Column(name = "UserName")
	public String UserName;
	@Column(name = "PassWord")
	public String PassWord;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		UserName = userName;
	}
	public String getPassWord() {
		return PassWord;
	}
	public void setPassWord(String passWord) {
		PassWord = passWord;
	}
	@Override
	public String toString() {
		return "MoreUser [id=" + id + ", UserName=" + UserName + ", PassWord="
				+ PassWord + "]";
	}

}
