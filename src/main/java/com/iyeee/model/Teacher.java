package com.iyeee.model;

import java.io.InputStream;

/**
 * 
 * @author llq
 *教师实体表设计
 */
public class Teacher {
	private int id;
	private String sn;//教师工号
	private int num;
	private String name;
	private String password;
	private int clazzId;
	private String sex;
	private String mobile;
	private String qq;
	private String identity;
	private String status;
	private String department;
	private InputStream photo;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getClazzId() {
		return clazzId;
	}

	public void setClazzId(int clazzId) {
		this.clazzId = clazzId;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getidentity() {
		return identity;
	}

	public void setidentity(String identity) {
		this.identity = identity;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public InputStream getPhoto() {
		return photo;
	}

	public void setPhoto(InputStream photo) {
		this.photo = photo;
	}

	@Override
	public String toString() {
		return "Teacher{" +
				"id=" + id +
				", sn='" + sn + '\'' +
				", num=" + num +
				", name='" + name + '\'' +
				", password='" + password + '\'' +
				", clazzId=" + clazzId +
				", sex='" + sex + '\'' +
				", mobile='" + mobile + '\'' +
				", qq='" + qq + '\'' +
				", identity='" + identity + '\'' +
				", status='" + status + '\'' +
				", department='" + department + '\'' +
				", photo=" + photo +
				'}';
	}
}
