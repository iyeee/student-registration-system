package com.iyeee.dao;

import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;


public class SysDao extends BaseDao{
    public int getStudentState(){
        int state=1;
        String sql="select forbid_student_select from sys limit 1";
        ResultSet resultSet=query(sql);
        try {
        while (resultSet.next()){
            state=resultSet.getInt("forbid_student_select");
        }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return state;
    }
    public int getTeacherState(){
        int state=1;
        String sql="select forbid_teacher_select from sys limit 1";
        ResultSet resultSet=query(sql);
        try {
            while (resultSet.next()){
                state=resultSet.getInt("forbid_teacher_select");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return state;
    }
    public boolean changeTeacherState(){
        //原状态取反
       int state=0;
       if (getTeacherState()==0)
           state=1;
        boolean flag=false;
        String sql="update sys set forbid_teacher_select='"+state+"'";
        return update(sql);
    }
    public boolean changeStudentState(){
        //原状态取反
        int state=0;
        if(getStudentState()==0)
            state=1;
        boolean flag=false;
        String sql="update sys set forbid_student_select='"+state+"'";
        return update(sql);
    }
    @Test
    public void test(){
        System.out.println(getStudentState());
        System.out.println(getTeacherState());
        changeTeacherState();
        changeStudentState();
    }
}
