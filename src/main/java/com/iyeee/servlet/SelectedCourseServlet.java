package com.iyeee.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.iyeee.dao.SysDao;
import com.iyeee.model.Sys;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.iyeee.dao.CourseDao;
import com.iyeee.dao.SelectedCourseDao;
import com.iyeee.model.Page;
import com.iyeee.model.SelectedCourse;
import com.iyeee.model.Student;
import org.junit.Test;

public class SelectedCourseServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7120913402001186955L;

	public void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException{
		doPost(request, response);
	}
	public void doPost(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String method = request.getParameter("method");
		if("toSelectedCourseListView".equals(method)){
			SysDao sysDao=new SysDao();
			int forbidStudent=sysDao.getStudentState();
			int forbidTeacher=sysDao.getTeacherState();
			int userType = Integer.parseInt(request.getSession().getAttribute("userType").toString());
			System.out.println("forbidStudent:"+forbidStudent+" forbidteacher:"+forbidTeacher+"type:"+userType);
			try {
				if(forbidStudent==1&&userType==2||forbidTeacher==1&&userType==3||userType==1) {
					System.out.println("notforbid");
					request.getRequestDispatcher("view/selectedCourseList.jsp").forward(request, response);
				}else{
					request.getRequestDispatcher("view/selectedCourseListForbid.jsp").forward(request,response);
				}
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if("AddSelectedCourse".equals(method)){
			addSelectedCourse(request,response);
		}else if("SelectedCourseList".equals(method)){
			getSelectedCourseList(request,response);
		}else if("DeleteSelectedCourse".equals(method)){
			deleteSelectedCourse(request,response);
		}else if("getMainCost".equals(method)){
			getTotalCost(request,response);
		}
	}
	private void getTotalCost(HttpServletRequest request,HttpServletResponse response){
		int studentId = request.getParameter("studentid") == null ? 0 : Integer.parseInt(request.getParameter("studentid").toString());
		SelectedCourseDao selectedCourseDao=new SelectedCourseDao();
		int mainCost = selectedCourseDao.getMainCost(studentId);
		try {
			response.getWriter().write(mainCost);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void deleteSelectedCourse(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		// TODO Auto-generated method stub
		int id = Integer.parseInt(request.getParameter("id"));
		SelectedCourseDao selectedCourseDao = new SelectedCourseDao();
		SelectedCourse selectedCourse = selectedCourseDao.getSelectedCourse(id);
		String msg = "success";
		if(selectedCourse == null){
			msg = "not found";
			response.getWriter().write(msg);
			selectedCourseDao.closeCon();
			return;
		}
		if(selectedCourseDao.deleteSelectedCourse(selectedCourse.getId())){
			CourseDao courseDao = new CourseDao();
			if (selectedCourseDao.getKind(id)==1) {
				courseDao.updateCourseSelectedNum(selectedCourse.getCourseId(), -1);
			}
			courseDao.closeCon();
		}else{
			msg = "error";
		}
		selectedCourseDao.closeCon();
		response.getWriter().write(msg);
	}
	private void addSelectedCourse(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		// TODO Auto-generated method stub
		int studentId = request.getParameter("studentid") == null ? 0 : Integer.parseInt(request.getParameter("studentid").toString());
		int courseId = request.getParameter("courseid") == null ? 0 : Integer.parseInt(request.getParameter("courseid").toString());
		String kind=request.getParameter("kind");

		CourseDao courseDao = new CourseDao();
		String msg = "success";
		if(courseDao.isFull(courseId)){
			msg = "courseFull";
			response.getWriter().write(msg);
			courseDao.closeCon();
			return;
		}
		SelectedCourseDao selectedCourseDao = new SelectedCourseDao();
		if(selectedCourseDao.isSelected(studentId, courseId)){
			msg = "courseSelected";
			response.getWriter().write(msg);
			selectedCourseDao.closeCon();
			return;
		}
		int preCount=selectedCourseDao.countSelect(studentId,courseId);
//		System.out.println(preCount);
		if(preCount==0){
//			System.out.println("preCount");
			msg="nopre";
			response.getWriter().write(msg);
			selectedCourseDao.closeCon();
//			System.out.println("nopre");
			return;
		}
		if(selectedCourseDao.isConflict(courseId,studentId)){
			msg="conflict";
			response.getWriter().write(msg);
			selectedCourseDao.closeCon();
			System.out.println("conflict");
			return;
		}
		SelectedCourse selectedCourse = new SelectedCourse();
		selectedCourse.setStudentId(studentId);
		selectedCourse.setCourseId(courseId);
		if(kind.trim().equals("main")){
			selectedCourse.setKind(1);
		}else if (kind.trim().equals("secondary")){
			selectedCourse.setKind(2);
		}else {
			System.out.println("333333");
		}
		if(selectedCourseDao.addSelectedCourse(selectedCourse)){
			msg = "success";
		}
		if (kind.trim().equals("main")) {
			courseDao.updateCourseSelectedNum(courseId, 1);
		}
		courseDao.closeCon();
		selectedCourseDao.closeCon();
		response.getWriter().write(msg);
	}

	private void getSelectedCourseList(HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		int studentId = request.getParameter("studentid") == null ? 0 : Integer.parseInt(request.getParameter("studentid").toString());
		int courseId = request.getParameter("courseid") == null ? 0 : Integer.parseInt(request.getParameter("courseid").toString());
//		String type=request.getParameter("type");
		Integer currentPage = request.getParameter("page") == null ? 1 : Integer.parseInt(request.getParameter("page"));
		Integer pageSize = request.getParameter("rows") == null ? 999 : Integer.parseInt(request.getParameter("rows"));
		SelectedCourse selectedCourse = new SelectedCourse();
		//获取当前登录用户类型
		int userType = Integer.parseInt(request.getSession().getAttribute("userType").toString());
		if(userType == 2){
			//如果是学生，只能查看自己的信息
			Student currentUser = (Student)request.getSession().getAttribute("user");
			studentId = currentUser.getId();
		}
		selectedCourse.setCourseId(courseId);
		selectedCourse.setStudentId(studentId);
//		selectedCourse.setType(type);
		SelectedCourseDao selectedCourseDao = new SelectedCourseDao();
		List<SelectedCourse> courseList = selectedCourseDao.getSelectedCourseList(selectedCourse, new Page(currentPage, pageSize));
		/*
		* 测试
//		* */
//		for(SelectedCourse s:courseList){
//			System.out.println(s.getCourseId()+" "+s.getType());
//		}

		int total = selectedCourseDao.getSelectedCourseListTotal(selectedCourse);
		selectedCourseDao.closeCon();
		response.setCharacterEncoding("UTF-8");
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("total", total);
		ret.put("rows", courseList);
		try {
			String from = request.getParameter("from");
			if("combox".equals(from)){
				response.getWriter().write(JSONArray.fromObject(courseList).toString());
			}else{
				response.getWriter().write(JSONObject.fromObject(ret).toString());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void test(){
		String[] split = "1-17".split("-");
		for(String s:split){
			System.out.println(s);
		}
		System.out.println(Integer.max(1,17));
	}
}
