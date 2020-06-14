package com.iyeee.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.iyeee.model.Course;
import com.iyeee.model.Page;
import com.iyeee.util.StringUtil;
import org.junit.Test;

/**
 * 
 * @author llq
 *课程数据库操作类
 */

public class CourseDao extends BaseDao {
	public boolean addCourse(Course course){
		String sql = "insert into s_course values(null,'"+course.getName()+"','"+course.getTeacherId()+"',0,'"+course.getMaxNum()+"','"+course.getInfo()+"','"+course.getCyear()+"','"+course.getSemester()+"','"+course.getTime()+"','"+course.getWeek()+"','"+course.getCost()+"','"+course.getPre()+"') ";
		return update(sql);
	}

	public static void main(String[] args) {
			Course course=new Course();
			course.setName("大学语文");
			course.setTeacherId(9);
			course.setSelectedNum(2);
			course.setMaxNum(50);
			course.setInfo("");
			course.setCyear(2020);
			course.setSemester("上");
			course.setTime("08:00-09:40");
			course.setWeek("1-17");
			course.setCost(312);
			course.setPre("大学识字");
//			new CourseDao().addCourse(course);
			new CourseDao().editCourse(course);
	}
	
	public List<Course> getCourseList(Course course, Page page){
		List<Course> ret = new ArrayList<Course>();
		String sql = "select * from s_course ";
		if(!StringUtil.isEmpty(course.getName())){
			sql += "and name like '%" + course.getName() + "%'";
		}
		if(course.getTeacherId() != 0){
			sql += " and teacher_id = " + course.getTeacherId() + "";
		}
		sql += " limit " + page.getStart() + "," + page.getPageSize();
		ResultSet resultSet = query(sql.replaceFirst("and", "where"));
		try {
			while(resultSet.next()){
				Course cl = new Course();
				cl.setId(resultSet.getInt("id"));
				cl.setName(resultSet.getString("name"));
				cl.setTeacherId(resultSet.getInt("teacher_id"));
				cl.setSelectedNum(resultSet.getInt("selected_num"));
				cl.setMaxNum(resultSet.getInt("max_num"));
				cl.setInfo(resultSet.getString("info"));
				cl.setCyear(Integer.parseInt(resultSet.getString("cyear")));
				cl.setSemester(resultSet.getString("semester"));
				cl.setTime(resultSet.getString("time"));
				cl.setWeek(resultSet.getString("week"));
				cl.setCost(Integer.parseInt(resultSet.getString("cost")));
				cl.setPre(resultSet.getString("pre"));
				ret.add(cl);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}



	public int getCourseListTotal(Course course){
		int total = 0;
		String sql = "select count(*)as total from s_course ";
		if(!StringUtil.isEmpty(course.getName())){
			sql += "and name like '%" + course.getName() + "%'";
		}
		if(course.getTeacherId() != 0){
			sql += " and teacher_id = " + course.getTeacherId() + "";
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
	
	public boolean editCourse(Course course) {
		// TODO Auto-generated method stub
		String sql = "update s_course set name = '"+course.getName()+"',teacher_id = '"+course.getTeacherId()+"',max_num = '"+course.getMaxNum()+"',info = '"+course.getInfo()+"',cyear='"+course.getCyear()+"',semester='"+course.getSemester()+"',time='"+course.getTime()+"',week='"+course.getWeek()+"',cost='"+course.getCost()+"',pre='"+course.getPre()+"' where id =" + course.getId();
		return update(sql);
	}
	public boolean deleteCourse(String ids) {
		// TODO Auto-generated method stub
		String sql = "delete from s_course where id in("+ids+")";
		return update(sql);
	}
	/**
	 * 检查该课程是否已选满
	 * @param courseId
	 * @return
	 */
	public boolean isFull(int courseId){
		boolean ret = false;
		String sql = "select * from s_course where selected_num >= max_num and id = " + courseId;
		ResultSet query = query(sql);
		try {
			if(query.next()){
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	/**
	 * 更新课程已选人数
	 * @param courseId
	 */
	public void updateCourseSelectedNum(int courseId ,int num){
		String sql = "";
		if(num > 0){
			sql = "update s_course set selected_num = selected_num + "+ num + " where id = " + courseId;
		}else{
			sql = "update s_course set selected_num = selected_num - " + Math.abs(num) + " where id = " + courseId;
		}
		update(sql);
	}
	
	/**
	 * 获取制定id范围内的课程列表
	 * @param ids
	 * @return
	 */
	public List<Course> getCourse(String ids){
		List<Course> ret = new ArrayList<Course>();
		String sql = "select * from s_course where id in("+ids+")";
		ResultSet query = query(sql);
		try {
			while(query.next()){
				Course cl = new Course();
				cl.setId(query.getInt("id"));
				cl.setName(query.getString("name"));
				cl.setTeacherId(query.getInt("teacher_id"));
				cl.setSelectedNum(query.getInt("selected_num"));
				cl.setMaxNum(query.getInt("max_num"));
				cl.setInfo(query.getString("info"));
				cl.setCyear(query.getInt("cyear"));
				cl.setSemester(query.getString("semester"));
				cl.setTime(query.getString("time"));
				cl.setWeek(query.getString("week"));
				cl.setCost(query.getInt("cost"));
				cl.setPre(query.getString("pre"));

				ret.add(cl);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	
	/**
	 * 获取指定的课程
	 * @param id
	 * @return
	 */
	public Course getCourse(int id){
		Course course = null;
		String sql = "select * from s_course where id = " + id;
		ResultSet query = query(sql);
		try {
			while(query.next()){
				course = new Course();
				course.setId(query.getInt("id"));
				course.setName(query.getString("name"));
				course.setTeacherId(query.getInt("teacher_id"));

				course.setSelectedNum(query.getInt("selected_num"));
				course.setMaxNum(query.getInt("max_num"));
				course.setInfo(query.getString("info"));
				course.setCyear(query.getInt("cyear"));
				course.setSemester(query.getString("semester"));
				course.setTime(query.getString("time"));
				course.setWeek(query.getString("week"));
				course.setCost(query.getInt("cost"));
				course.setPre(query.getString("pre"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return course;
	}
}
