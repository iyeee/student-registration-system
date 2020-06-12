package com.iyeee.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.iyeee.model.Clazz;
import com.iyeee.model.Page;
import com.iyeee.util.StringUtil;
import org.junit.Test;

/**
 * 
 * @author llq
 *班级信息数据库操作
 */
public class ClazzDao extends BaseDao {
	public List<Clazz> getClazzList(Clazz clazz, Page page){
		List<Clazz> ret = new ArrayList<Clazz>();
		String sql = "select * from s_clazz ";
		if(!StringUtil.isEmpty(clazz.getName())){
			sql += "where name like '%" + clazz.getName() + "%'";
		}
		sql += " limit " + page.getStart() + "," + page.getPageSize();
		ResultSet resultSet = query(sql);
		try {
			while(resultSet.next()){
				Clazz cl = new Clazz();
				cl.setId(resultSet.getInt("id"));
				cl.setName(resultSet.getString("name"));
				cl.setInfo(resultSet.getString("info"));
				cl.setNum(resultSet.getInt("num"));
				cl.setInstitute(resultSet.getString("institute"));
				cl.setGrade(resultSet.getInt("grade"));
				ret.add(cl);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	public int getClazzListTotal(Clazz clazz){
		int total = 0;
		String sql = "select count(*)as total from s_clazz ";
		if(!StringUtil.isEmpty(clazz.getName())){
			sql += "where name like '%" + clazz.getName() + "%'";
		}
		ResultSet resultSet = query(sql);
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
	public boolean addClazz(Clazz clazz){
		String sql = "insert into s_clazz values(null,'"+clazz.getName()+"','"+clazz.getInstitute()+"','"+clazz.getNum()+"','"+clazz.getGrade()+"','"+clazz.getInfo()+"') ";
		return update(sql);
	}
	public boolean deleteClazz(int id){
		String sql = "delete from s_clazz where id = "+id;
		return update(sql);
	}
	public boolean editClazz(Clazz clazz) {
		// TODO Auto-generated method stub
		String sql = "update s_clazz set name = '"+clazz.getName()+"',info = '"+clazz.getInfo()+"',institute='"+clazz.getInstitute()+"',num='"+clazz.getNum()+"',grade='"+clazz.getGrade()+"' where id = " + clazz.getId();
		return update(sql);
	}
	@Test
	public void testAddClazz(){
		Clazz clazz=new Clazz();
		clazz.setName("软件学院十班");
		clazz.setInstitute("软件学院");
		clazz.setNum(10);
		clazz.setGrade(2020);
		clazz.setInfo("吉林大学大学软件学院十班 求实创新，励志图强");
		addClazz(clazz);
	}
	@Test
	public void testEditClazz(){
		Clazz clazz=new Clazz();
		clazz.setId(8);
		clazz.setName("软件学院十班");
		clazz.setInstitute("软件学院");
		clazz.setNum(10);
		clazz.setGrade(2017);
		clazz.setInfo("吉林大学大学软件学院十班 求实创新，励志图强!");
		editClazz(clazz);
	}
}
