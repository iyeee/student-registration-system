package com.ischoolbar.programmer.dao;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.ischoolbar.programmer.model.Admin;
import com.ischoolbar.programmer.model.Clazz;
import com.ischoolbar.programmer.model.Page;
import com.ischoolbar.programmer.model.Student;
import com.ischoolbar.programmer.util.StringUtil;
import org.junit.Test;

public class StudentDao extends BaseDao {
	public boolean addStudent(Student student){
		String sql = "insert into s_student values(null,'"+student.getSn();
		sql +="','"+student.getNum()+"";
		sql +="','"+student.getName()+"'";
		sql += ",'" + student.getPassword() + "','" +student.getGrade()+"','"+ student.getClazzId();
		sql += "','" + student.getStatus()+"'";
		sql += ",'" + student.getSex()+"'";
		sql += ",'" +student.getIdentityId()+"'";
		sql += ",'" +student.getGraduateDate()+"'";
		sql += ",'" +student.getBirthday()+"'";
		sql += ",'" + student.getMobile() + "'";
		sql += ",'" + student.getQq() + "',null)";
		return update(sql);
	}

	public boolean editStudent(Student student) {
		// TODO Auto-generated method stub
		String sql = "update s_student set name = '"+student.getName()+"'";
		sql += ",sex = '" + student.getSex() + "'";
		sql += ",mobile = '" + student.getMobile() + "'";
		sql += ",qq = '" + student.getQq() + "'";
		sql += ",clazz_id = " + student.getClazzId();
		sql += ",status = '"+student.getStatus();
		sql += "' where id = " + student.getId();
		return update(sql);
	}

	public boolean setStudentPhoto(Student student) {
		// TODO Auto-generated method stub
		String sql = "update s_student set photo = ? where id = ?";
		Connection connection = getConnection();
		try {
			PreparedStatement prepareStatement = connection.prepareStatement(sql);
			prepareStatement.setBinaryStream(1, student.getPhoto());
			prepareStatement.setInt(2, student.getId());
			return prepareStatement.executeUpdate() > 0;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return update(sql);
	}
	public boolean deleteStudent(String ids) {
		// TODO Auto-generated method stub
		String sql = "delete from s_student where id in("+ids+")";
		return update(sql);
	}
	public Student getStudent(int id){
		String sql = "select * from s_student where id = " + id;
		Student student = null;
		ResultSet resultSet = query(sql);
		try {
			if(resultSet.next()){
				student = new Student();
				student.setId(resultSet.getInt("id"));
				student.setClazzId(resultSet.getInt("clazz_id"));
				student.setMobile(resultSet.getString("mobile"));
				student.setName(resultSet.getString("name"));
				student.setPassword(resultSet.getString("password"));
				student.setPhoto(resultSet.getBinaryStream("photo"));
				student.setQq(resultSet.getString("qq"));
				student.setSex(resultSet.getString("sex"));
				student.setSn(resultSet.getString("sn"));
				student.setNum(resultSet.getInt("num"));
				student.setGrade(resultSet.getInt("grade"));
				student.setStatus(resultSet.getString("status"));
				student.setIdentityId(resultSet.getString("identity_id"));
				student.setGraduateDate(resultSet.getDate("graduate_date"));
				student.setBirthday(resultSet.getDate("birthday"));
				return student;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return student;
	}
	public List<Student> getStudentList(Student student,Page page){
		List<Student> ret = new ArrayList<Student>();
		String sql = "select * from s_student ";
		if(!StringUtil.isEmpty(student.getName())){
			sql += "and name like '%" + student.getName() + "%'";
		}
		if(student.getClazzId() != 0){
			sql += " and clazz_id = " + student.getClazzId();
		}
		if(student.getId() != 0){
			sql += " and id = " + student.getId();
		}
		sql += " limit " + page.getStart() + "," + page.getPageSize();
		ResultSet resultSet = query(sql.replaceFirst("and", "where"));
		try {
			while(resultSet.next()){
				Student s = new Student();
				s.setId(resultSet.getInt("id"));
				s.setClazzId(resultSet.getInt("clazz_id"));
				s.setMobile(resultSet.getString("mobile"));
				s.setName(resultSet.getString("name"));
				s.setPassword(resultSet.getString("password"));
				s.setPhoto(resultSet.getBinaryStream("photo"));
				s.setQq(resultSet.getString("qq"));
				s.setSex(resultSet.getString("sex"));
				s.setSn(resultSet.getString("sn"));
				ret.add(s);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	public int getStudentListTotal(Student student){
		int total = 0;
		String sql = "select count(*)as total from s_student ";
		if(!StringUtil.isEmpty(student.getName())){
			sql += "and name like '%" + student.getName() + "%'";
		}
		if(student.getClazzId() != 0){
			sql += " and clazz_id = " + student.getClazzId();
		}
		if(student.getId() != 0){
			sql += " and id = " + student.getId();
		}
		ResultSet resultSet = query(sql.replaceFirst("and", "where"));
		try {
			while(resultSet.next()){
				total = resultSet.getInt("total");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return total;
	}
	
	public Student login(String name ,String password){
		String sql = "select * from s_student where name = '" + name + "' and password = '" + password + "'";
		ResultSet resultSet = query(sql);
		try {
			if(resultSet.next()){
				Student student = new Student();
				student.setId(resultSet.getInt("id"));
				student.setName(resultSet.getString("name"));
				student.setPassword(resultSet.getString("password"));
				student.setClazzId(resultSet.getInt("clazz_id"));
				student.setMobile(resultSet.getString("mobile"));
				student.setPhoto(resultSet.getBinaryStream("photo"));
				student.setQq(resultSet.getString("qq"));
				student.setSex(resultSet.getString("sex"));
				student.setSn(resultSet.getString("sn"));
				return student;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public boolean editPassword(Student student,String newPassword) {
		// TODO Auto-generated method stub
		String sql = "update s_student set password = '"+newPassword+"' where id = " + student.getId();
		return update(sql);
	}

	@Test
	public void Test2() throws ParseException {
		Student student=new Student();
		student.setId(16);
		student.setSn("S21528633634989");
		student.setNum(21172536);
		student.setName("莫琪");
		student.setPassword("980523");
		student.setGrade(2017);
		student.setClazzId(4);
		student.setStatus("大组长");
		student.setSex("女");
		student.setIdentityId("330481998072638462");
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
		java.util.Date gradaDate=sdf.parse("2021-06-30");
		java.util.Date birth=sdf.parse("1998-06-30");
		student.setGraduateDate(new Date(gradaDate.getTime()));
		student.setBirthday(new Date(birth.getTime()));
		student.setMobile("13750748731");
		student.setQq("10097989123");
		editStudent(student);

	}
	@Test
	public void Test() throws ParseException {
		Student student=new Student();
		student.setSn("S21528633634989");
		student.setNum(21172536);
		student.setName("莫琪");
		student.setPassword("980523");
		student.setGrade(2017);
		student.setClazzId(4);
		student.setStatus("组长");
		student.setSex("女");
		student.setIdentityId("330481998072638462");
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
		java.util.Date gradaDate=sdf.parse("2021-06-30");
		java.util.Date birth=sdf.parse("1998-06-30");
		student.setGraduateDate(new Date(gradaDate.getTime()));
		student.setBirthday(new Date(birth.getTime()));
		student.setMobile("13750748731");
		student.setQq("10097989123");

		addStudent(student);
	}
	@Test
	public void Test3(){
		Student student = getStudent(1);
		System.out.println(student.getStatus()+" "+student.getBirthday());
	}
}
