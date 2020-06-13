//package com.iyeee.listener;
//
//
//
//import com.iyeee.dao.daoimpl.BaseDaoImpl;
//import com.iyeee.model.SystemInfo;
//import org.junit.Test;
//
//import javax.servlet.ServletContext;
//import javax.servlet.ServletContextEvent;
//import javax.servlet.ServletContextListener;
//
///**
// * 系统初始化
// * @author bojiangzhou
// *
// */
//public class SystemInitListener implements ServletContextListener {
//
//    public SystemInitListener() {
//
//    }
//
//    public void contextInitialized(ServletContextEvent sce)  {
//    	ServletContext application = sce.getServletContext();
//    	//获取系统初始化对象
//    	SystemInfo sys = (SystemInfo) new BaseDaoImpl().getObject(SystemInfo.class, "SELECT * FROM system", null);
//    	//放到域中
//    	application.setAttribute("systemInfo", sys);
//    }
//
//    public void contextDestroyed(ServletContextEvent sce)  {
//
//    }
//    @Test
//    public void test(){
//        SystemInfo sys = (SystemInfo) new BaseDaoImpl().getObject(SystemInfo.class, "SELECT * FROM system", null);
//        System.out.println(sys.getSchoolName());
//        System.out.println(sys.getNoticeStudent());
//    }
//
//}
