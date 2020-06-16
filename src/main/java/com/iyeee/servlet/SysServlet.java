package com.iyeee.servlet;

import com.iyeee.dao.SysDao;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SysServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String method = request.getParameter("method");
        if ("changeTeacherState".equals(method)) {
            changeTeacherState(request, response);
        } else if ("changeStudentState".equals(method)) {
            changeStudentState(request, response);
        }else if("getTeacherState".equals(method)){
//            getTeacherState(request,response);
        }else if("getStudentState".equals(method)){
//            getStudentState(request,response);
        }

    }


    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request, response);
    }


    private void changeTeacherState(HttpServletRequest request, HttpServletResponse response) {

        SysDao sysDao=new SysDao();
        System.out.println("TeacheroriginState:"+sysDao.getTeacherState());
        sysDao.changeTeacherState();
        if(sysDao.getTeacherState()==1){
            try {
                request.getRequestDispatcher("view/teacherOpen.jsp").forward(request,response);
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            try {
                request.getRequestDispatcher("view/teacherClose.jsp").forward(request,response);
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void changeStudentState(HttpServletRequest request, HttpServletResponse response) {
        SysDao sysDao=new SysDao();
        System.out.println("StudentoriginState:"+sysDao.getStudentState());
        sysDao.changeStudentState();
        if(sysDao.getStudentState()==1){
            try {
                request.getRequestDispatcher("view/studentOpen.jsp").forward(request,response);
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            try {
                request.getRequestDispatcher("view/studentClose.jsp").forward(request,response);
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}