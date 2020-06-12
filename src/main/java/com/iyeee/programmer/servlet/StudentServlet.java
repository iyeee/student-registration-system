package com.iyeee.programmer.servlet;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.iyeee.programmer.dao.StudentDao;
import com.iyeee.programmer.model.Page;
import com.iyeee.programmer.model.Student;
import com.iyeee.programmer.util.SnGenerateUtil;
import org.junit.Test;

/**
 * 
 * @author llq
 *学生信息管理功能实现servlet
 */
public class StudentServlet extends HttpServlet {
	public void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException{
		doPost(request, response);
	}
	public void doPost(HttpServletRequest request,HttpServletResponse response) throws IOException{
		System.out.println("StudentServlet");
		String method = request.getParameter("method");
		if("toStudentListView".equals(method)){
			studentList(request,response);
		}else if("AddStudent".equals(method)){
			try {
				addStudent(request,response);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}else if("StudentList".equals(method)){
			getStudentList(request,response);
		}else if("EditStudent".equals(method)){
			editStudent(request,response);
		}else if("DeleteStudent".equals(method)){
			deleteStudent(request,response);
		}
	}
	private void deleteStudent(HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		String[] ids = request.getParameterValues("ids[]");
		String idStr = "";
		for(String id : ids){
			idStr += id + ",";
		}
		idStr = idStr.substring(0, idStr.length()-1);
		StudentDao studentDao = new StudentDao();
		if(studentDao.deleteStudent(idStr)){
			try {
				response.getWriter().write("success");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				studentDao.closeCon();
			}
		}
	}
	private void editStudent(HttpServletRequest request,
			HttpServletResponse response) {
		System.out.println("eidtStudent");
		// TODO Auto-generated method stub
		String name = request.getParameter("name");
		int id = Integer.parseInt(request.getParameter("id"));
		String sex = request.getParameter("sex");
		String mobile = request.getParameter("mobile");
		String qq = request.getParameter("qq");
		int clazzId = Integer.parseInt(request.getParameter("clazzid"));
		String status=request.getParameter("status");
		String identityId=request.getParameter("identityID");
		String graduateDate=request.getParameter("graduateDate");
		String birthday=request.getParameter("birthday");
		Student student = new Student();
		student.setClazzId(clazzId);
		student.setMobile(mobile);
		student.setName(name);
		student.setId(id);
		student.setQq(qq);
		student.setSex(sex);
		student.setStatus(status);
		student.setIdentityId(identityId);
		student.setGraduateDate(graduateDate);
		student.setBirthday(birthday);
		StudentDao studentDao = new StudentDao();
		if(studentDao.editStudent(student)){
			try {
				response.getWriter().write("success");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				studentDao.closeCon();
			}
		}
	}
	private void getStudentList(HttpServletRequest request,
			HttpServletResponse response) {
		System.out.println("getStudentList");
		// TODO Auto-generated method stub
		String name = request.getParameter("studentName");
		Integer currentPage = request.getParameter("page") == null ? 1 : Integer.parseInt(request.getParameter("page"));
		Integer pageSize = request.getParameter("rows") == null ? 999 : Integer.parseInt(request.getParameter("rows"));
		Integer clazz = request.getParameter("clazzid") == null ? 0 : Integer.parseInt(request.getParameter("clazzid"));
		//获取当前登录用户类型
		int userType = Integer.parseInt(request.getSession().getAttribute("userType").toString());
		Student student = new Student();
		student.setName(name);
		student.setClazzId(clazz);
		if(userType == 2){
			//如果是学生，只能查看自己的信息
			Student currentUser = (Student)request.getSession().getAttribute("user");
			student.setId(currentUser.getId());
		}
		StudentDao studentDao = new StudentDao();
		List<Student> studentList = studentDao.getStudentList(student, new Page(currentPage, pageSize));
		/*
		*测试*/
		for (Student student1 : studentList) {
			System.out.println(student1);
		}
		int total = studentDao.getStudentListTotal(student);
		studentDao.closeCon();
		response.setCharacterEncoding("UTF-8");
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("total", total);
		ret.put("rows", studentList);
		try {
			String from = request.getParameter("from");
			if("combox".equals(from)){
				System.out.println(JSONArray.fromObject(studentList).toString());
				response.getWriter().write(JSONArray.fromObject(studentList).toString());
			}else{
				System.out.println(JSONArray.fromObject(ret).toString());
				response.getWriter().write(JSONObject.fromObject(ret).toString());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void addStudent(HttpServletRequest request,
			HttpServletResponse response) throws ParseException {
		System.out.println("addStudent");
		// TODO Auto-generated method stub
		String name = request.getParameter("name");
		String password = request.getParameter("password");
		String sex = request.getParameter("sex");
		String mobile = request.getParameter("mobile");
		String qq = request.getParameter("qq");
		int clazzId = Integer.parseInt(request.getParameter("clazzid"));

//		int num=Integer.parseInt(request.getParameter("num").trim());
		int grade=Integer.parseInt(request.getParameter("grade").trim());
		String status=request.getParameter("status");
//		String graduateDate=request.getParameter("graduateDate");
//		String birthday=request.getParameter("birthday");
		String identityId=request.getParameter("identityId");


		Student student = new Student();
		student.setClazzId(clazzId);
		student.setMobile(mobile);
		student.setName(name);
		student.setPassword(password);
		student.setQq(qq);
		student.setSex(sex);
		student.setSn(SnGenerateUtil.generateSn(clazzId));

//		student.setNum(num);
		student.setGrade(grade);
		student.setStatus(status);
//		student.setGraduateDate(graduateDate);
//		student.setBirthday(birthday);
		student.setIdentityId(identityId);

		StudentDao studentDao = new StudentDao();
		if(studentDao.addStudent(student)){
			try {
				System.out.println("success");
				response.getWriter().write("success");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				studentDao.closeCon();
			}
		}
	}
	/*
	*测试addStudent*/
	@Test
	public void testAddStudent(){
		Student student = new Student();
		String name="mqd";
		String password="12332112";
		String sex="男";
		String mobile="13123983121";
		String qq="123123123";
		int grade=2019;
		int clazzId=1;
		String status="1231";
		String identityId="123";
		String graduateDate="2020-06-30";
		String birthday="2020-06-30";
		student.setClazzId(clazzId);
		student.setMobile(mobile);
		student.setName(name);
		student.setPassword(password);
		student.setQq(qq);
		student.setSex(sex);
		student.setSn(SnGenerateUtil.generateSn(clazzId));

//		student.setNum(num);
		student.setGrade(grade);
		student.setStatus(status);
		student.setGraduateDate(graduateDate);
		student.setBirthday(birthday);
		student.setIdentityId(identityId);

		StudentDao studentDao = new StudentDao();
		if(studentDao.addStudent(student)) {
			System.out.println("success");
		}
	}
	private void studentList(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		// TODO Auto-generated method stub
		try {
			System.out.println("studentList");
			request.getRequestDispatcher("view/studentList.jsp").forward(request, response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
