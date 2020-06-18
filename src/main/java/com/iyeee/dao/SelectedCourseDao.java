package com.iyeee.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.iyeee.model.Course;
import com.iyeee.model.Page;
import com.iyeee.model.SelectedCourse;
import com.iyeee.model.Student;
import org.junit.Test;

/**
 * 
 * @author llq
 *选课表数据库操作封装
 */
public class SelectedCourseDao extends BaseDao {
	public List<SelectedCourse> getSelectedCourseList(SelectedCourse selectedCourse, Page page){
//		System.out.println("getSelectCourseList");
		List<SelectedCourse> ret = new ArrayList<SelectedCourse>();
		String sql = "select * from s_selected_course ";
		if(selectedCourse.getStudentId() != 0){
			sql += " and student_id = " + selectedCourse.getStudentId();
		}
		if(selectedCourse.getCourseId() != 0){
			sql += " and course_id = " + selectedCourse.getCourseId();
		}
		sql += " limit " + page.getStart() + "," + page.getPageSize();
		sql = sql.replaceFirst("and", "where");
		ResultSet resultSet = query(sql);
		try {
			while(resultSet.next()){
				SelectedCourse cl = new SelectedCourse();
				cl.setId(resultSet.getInt("id"));
				cl.setCourseId(resultSet.getInt("course_id"));
				cl.setStudentId(resultSet.getInt("student_id"));
				cl.setKind(resultSet.getInt("kind"));
				ret.add(cl);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	public int getSelectedCourseListTotal(SelectedCourse selectedCourse){
//		System.out.println("getSelectedCourseListTotal(SelectedCourse selectedCourse)");
		int total = 0;
		String sql = "select count(*)as total from s_selected_course ";
		if(selectedCourse.getStudentId() != 0){
			sql += " and student_id = " + selectedCourse.getStudentId();
		}
		if(selectedCourse.getCourseId() != 0){
			sql += " and course_id = " + selectedCourse.getCourseId();
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
	public int getKind(int id){
		int kind=1;
		String sql="select kind from s_selected_course where id='"+id+"'";
		ResultSet resultSet=query(sql);
		try {
			while (resultSet.next()) {
				kind = resultSet.getInt("kind");
			}
		}catch (SQLException e){
			e.printStackTrace();
		}
		return kind;
	}
	public int getStudentId(int id){
		int studentId=0;
		String sql="select student_id from s_selected_course where id='"+id+"'";
		ResultSet resultSet=query(sql);
		try {
			while (resultSet.next()) {
				studentId= resultSet.getInt("student_id");
			}
		}catch (SQLException e){
			e.printStackTrace();
		}
		return studentId;
	}
	public int getCourseId(int id){
		int courseId=0;
		String sql="select course_id from s_selected_course where id='"+id+"'";
		ResultSet resultSet=query(sql);
		try {
			while (resultSet.next()) {
				courseId= resultSet.getInt("course_id");
			}
		}catch (SQLException e){
			e.printStackTrace();
		}
		return courseId;
	}
	public int isSecondary(int studentId){
		int ret=0;
		String sql="select * from s_selected_course where student_id='"+studentId+"' and kind=2;";
		ResultSet resultSet=query(sql);
		try {
			if (resultSet.next()){
				ret=resultSet.getInt("id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ret;
	}
	@Test
	public void	test99(){
		System.out.println(isSecondary(16));
	}
	public boolean updateKind(int studentId){
		String sql="update s_selected_course\n" +
				" set  kind=1" +
				" where kind=2 and  student_id='" +studentId+
				"'order by id  " +
				" limit 1";
		return update(sql);
	}
	@Test
	public void test(){
		updateKind(9);
	}
	/**
	 * 检查学生是否已经选择该门课程
	 * @param studentId
	 * @param courseId
	 * @return
	 */
	public boolean isSelected(int studentId,int courseId){
//		System.out.println("isSelected");
		boolean ret = false;
		String sql = "select * from s_selected_course where student_id = " + studentId + " and course_id = " + courseId;
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
	 * 添加选课信息
	 * @param selectedCourse
	 * @return
	 */
	public boolean addSelectedCourse(SelectedCourse selectedCourse){
		String sql = "insert into s_selected_course values(null,"+selectedCourse.getStudentId()+","+selectedCourse.getCourseId()+",\""+selectedCourse.getKind()+"\")";
		return update(sql);
	}
	/**
	 * 删除所选课程
	 * @param id
	 * @return
	 */
	public boolean deleteSelectedCourse(int id){
		CourseDao courseDao=new CourseDao();
		System.out.println("getKind:"+getKind(id));
		if(getKind(id)==1){
			courseDao.updateCourseSelectedNum(getCourseId(id),-1);
		}
		//主选课被删除备选成为主选
		int studentId=getStudentId(id);
		int kind=getKind(id);
		if (kind==1){
			int selectCourseId=isSecondary(studentId);
			if(selectCourseId>0){
				updateKind(studentId);

				courseDao.updateCourseSelectedNum(getCourseId(selectCourseId),1);
			}
		}
		courseDao.closeCon();
		String sql = "delete from s_selected_course where id = " + id;
		return update(sql);
	}

	public int getMainCost(int id){
		String sql="select sum(cost) as total from s_course,s_selected_course where s_course.id=s_selected_course.course_id and s_selected_course.Kind= '" +"主选"+

				"' group by student_id " +
				"having student_id="+id;
		ResultSet query = query(sql);
		int total=0;
		try {
			while (query.next()) {
				total = query.getInt("total");
			}
			} catch(SQLException e){
				e.printStackTrace();
			}

		return total;

	}
	public int getTotalCost(int id){
		String sql="select sum(cost) as total from s_course,s_selected_course where s_course.id=s_selected_course.course_id" +

				" group by student_id " +
				"having student_id="+id;
		ResultSet query = query(sql);
		int total=0;
		try {
			while (query.next()) {
				total = query.getInt("total");
			}
		} catch(SQLException e){
			e.printStackTrace();
		}

		return total;

	}

	@Test
	public void testGetMain(){
//		System.out.println(getMainCost(2));
		System.out.println(getTotalCost(2));
	}
	/**
	 * 获取一条选课数据
	 * @param id
	 * @return
	 */
	public SelectedCourse getSelectedCourse(int id){
//		System.out.println("getSelectCourse");
		SelectedCourse ret = null;
		String sql = "select * from s_selected_course where id = " + id;
		ResultSet query = query(sql);
		try {
			if(query.next()){
				ret = new SelectedCourse();
				ret.setId(query.getInt("id"));
				ret.setCourseId(query.getInt("course_id"));
				ret.setStudentId(query.getInt("student_id"));
				ret.setKind(query.getInt("kind"));
				return ret;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	public String getPre(int courseId){
		String pre="";
		String sql="select pre from s_course where s_course.id="+courseId;
		ResultSet resultSet=query(sql);
		try {
			while (resultSet.next()){
				try {
					pre= resultSet.getString("pre");
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return pre;
	}
	public boolean isConflict(int CousreId,int studentId){
		String sql1="select cyear,semester,time,week from s_course where s_course.id="+CousreId;
		ResultSet resultSet1=query(sql1);
		int cyear1 = 0,cyear2;
		String semester1 = "",time1 = "",week1 = "",semester2="",time2="",week2="";
        try {
            while(resultSet1.next()){
                cyear1=resultSet1.getInt("cyear");
                semester1=resultSet1.getString("semester");
                time1=resultSet1.getString("time");
                week1= resultSet1.getString("week");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("111---"+cyear1+" "+semester1+" "+time1+" "+week1);
        String sql2="select cyear,semester,time,week from s_selected_course,s_course"+
		" where s_selected_course.course_id=s_course.id and student_id="+studentId;
		ResultSet resultSet2=query(sql2);
		try {
		    while(resultSet2.next()) {
		        cyear2=resultSet2.getInt("cyear");
		        semester2=resultSet2.getString("semester");
		        time2=resultSet2.getString("time");
		        week2=resultSet2.getString("week");
                System.out.println("2222------"+cyear2+semester2+time2+week2);
		        if (isTimeConflict(cyear1,cyear2,semester1,semester2,time1,time2,week1,week2)) {
		            return true;
                }
            }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
public boolean isTimeConflict(int cyear1,int cyear2,String semester1,String semester2,String time1,String time2,String week1,String week2){
    if(cyear1!=cyear2){
        return false;
    }
    if(!semester1.equals(semester2))
    {
        return false;
    }
    String[] timeArray1=time1.split("-");
    String[] timeArray2=time2.split("-");
    String[] weekArray1=week1.split("-");
    String[] weekArray2=week2.split("-");
    int sweek1= Integer.parseInt(weekArray1[1]);
    int eweek1= Integer.parseInt(weekArray1[2]);
    int sweek2=Integer.parseInt(weekArray2[1]);
    int eweek2=Integer.parseInt(weekArray2[2]);
    if (!weekArray1[0].equals(weekArray2[0])){
    	return false;
	}
    String stime1=timeArray1[0];
    String etime1=timeArray1[1];
    String stime2=timeArray2[0];
    String etime2=timeArray2[1];
	System.out.println(cyear1+" "+semester1+" "+sweek1+" "+eweek1+" "+stime1+" "+etime1);
	System.out.println(cyear2+" "+semester2+" "+sweek2+" "+eweek2+" "+stime2+" "+etime2);
	if (!isConflictHelper(sweek1,sweek2,eweek1,eweek2)){
		return false;
	}
	if(stime1.equals(stime2)||etime1.equals(etime2)){
		return true;
	}else return false;
}
	public boolean isConflictHelper(int sweek1,int sweek2,int eweek1,int eweek2){
		if (Integer.min(sweek1,eweek1)>Integer.max(sweek2,eweek2)){
			return false;
		}else if(Integer.max(sweek1,eweek1)<Integer.min(sweek2,eweek2)){
			return false;
		}
		return true;
	}

	public int countSelect(int studentid,int courseId){
		int count=0;
		//如果不存在先修课，直接返回1
		String preName=getPre(courseId);
		if(preName.length()<=0||preName.equals("null")){
			System.out.println(1);
			return 1;
		};
//        System.out.println(preName+"---------");
		String sql= "select count(*) as count from s_course,s_selected_course where student_id="+studentid+
		" and s_selected_course.course_id=s_course.id"+
		" and s_course.name='"+preName+"'";
		ResultSet query = query(sql);
		try {
			if(query.next()){
				count=query.getInt("count");
				return count;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return  count;
	}


	@Test
	public void Test(){
		deleteSelectedCourse(6);
	}
	@Test
	public void Test2(){
		SelectedCourse selectedCourse = getSelectedCourse(6);
		System.out.println(selectedCourse.getKind());
	}
	@Test
	public void Test3(){
		SelectedCourse selectedCourse=new SelectedCourse();
		selectedCourse.setStudentId(1);
		selectedCourse.setCourseId(14);
		selectedCourse.setKind(1);
		addSelectedCourse(selectedCourse);
	}
	@Test
	public void testConflict(){
		System.out.println(isConflict(23, 9));
//		System.out.println(isTimeConflict(2019, 2019, "下",
//				"下", "08:00-09:40", "08:00-09:42", "周四-6-17",
//				"周四-1-7"));
	}
}
