package org.apache.mybatis.jpa.test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.mybatis.jpa.test.dao.service.StudentsService;
import org.apache.mybatis.jpa.test.domain.Students;
import org.apache.mybatis.jpa.util.WebContext;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MyBatisTestRunner {
	
	private static final Logger _logger = LoggerFactory.getLogger(MyBatisTestRunner.class);
	
	public static ApplicationContext context;
	
	
	@Test
	public void insert() throws Exception{
		_logger.info("insert...");
		WebContext.applicationContext=context;
		StudentsService ss=(StudentsService)WebContext.getBean("studentsService");
		Students student=new Students();
		student.setStdNo("10024");
		student.setStdGender("M");
		student.setStdName("司马昭");
		student.setStdAge(20);
		student.setStdMajor("政治");
		student.setStdClass("4");
		ss.insert(student);
		
		Thread.sleep(1000);
		ss.remove(student.getId());
		
	}
	
	@Test
	public void get() throws Exception{
		_logger.info("get...");
		WebContext.applicationContext=context;
		StudentsService ss=(StudentsService)WebContext.getBean("studentsService");
		
		Students student=ss.get("921d3377-937a-4578-b1e2-92fb23b5e512");
		
		 _logger.info("Students "+student);

	}
	
	
	@Test
	public void remove() throws Exception{
		
		_logger.info("remove...");
		WebContext.applicationContext=context;
		StudentsService ss=(StudentsService)WebContext.getBean("studentsService");
		Students student=new Students();
		student.setId("921d3377-937a-4578-b1e2-92fb23b5e512");
		ss.remove(student.getId());
		
	}
	
	@Test
	public void batchDelete() throws Exception{
		_logger.info("batchDelete...");
		WebContext.applicationContext=context;
		StudentsService ss=(StudentsService)WebContext.getBean("studentsService");		
		List<String> idList=new ArrayList<String>();
		idList.add("8584804d-b5ac-45d2-9f91-4dd8e7a090a7");
		idList.add("ab7422e9-a91a-4840-9e59-9d911257c918");
		idList.add("12b6ceb8-573b-4f01-ad85-cfb24cfa007c");
		idList.add("dafd5ba4-d2e3-4656-bd42-178841e610fe");
		ss.batchDelete(idList);
	}

	@Test
	public void queryPageResults() throws Exception{
		
		_logger.info("queryPageResults...");
		WebContext.applicationContext=context;
		StudentsService ss=(StudentsService)WebContext.getBean("studentsService");
		 Students student=new Students();
		 //student.setId("af04d610-6092-481e-9558-30bd63ef783c");
		 student.setStdGender("M");
		 //student.setStdMajor(政治");
		 student.setPageResults(10);
		 student.setPage(2);
		 _logger.info("queryPageResults "+ss.queryPageResults(student));
	}
	
	@Test
	public void queryPageResultsByMapperId() throws Exception{

		_logger.info("queryPageResults by mapperId...");
		WebContext.applicationContext=context;
		StudentsService ss=(StudentsService)WebContext.getBean("studentsService");
		 Students student=new Students();
		 student.setStdGender("M");
		 //student.setStdMajor(政治");
		 student.setPageResults(10);
		 student.setPage(2);
		 
		 _logger.info("queryPageResults by mapperId "+ss.queryPageResults("queryPageResults1",student));
		 
	}
	
	
	
	@Test
	public void findAll() throws Exception{
		_logger.info("findAll...");
		WebContext.applicationContext=context;
		StudentsService ss=(StudentsService)WebContext.getBean("studentsService");
		_logger.info("findAll "+ss.findAll());
	}
	
	@Before
	public void initSpringContext(){
		if(context!=null) return;
		_logger.info("init Spring Context...");
		SimpleDateFormat sdf_ymdhms =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String startTime=sdf_ymdhms.format(new Date());

		try{
			MyBatisTestRunner runner=new MyBatisTestRunner();
			runner.init();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		_logger.info("-- --Init Start at " + startTime+" , End at  "+sdf_ymdhms.format(new Date()));
	}
	
	//Initialization ApplicationContext for Project
	public void init(){
		_logger.info("init ...");
		String path_str=MyBatisTestRunner.class.getResource("ApplicationRunner.properties").getFile().toString();
		String appRunnerClassPath=path_str.substring(0,path_str.indexOf("org"));
		_logger.info("Application class "+appRunnerClassPath);
		
		System.setProperty("APPRUNNER_CLASSPATH",appRunnerClassPath);
		String appRunnerPath=path_str.substring(
				path_str.startsWith("/")?1:0,
				path_str.indexOf(appRunnerClassPath.split("/")[appRunnerClassPath.split("/").length-1]));
		
		System.setProperty("APPRUNNER_PATH",appRunnerPath);
		
		_logger.info("appRunner dir "+appRunnerPath);
		_logger.info("Application dir "+System.getProperty("user.dir"));
		context = new ClassPathXmlApplicationContext(new String[] {"spring/applicationContext.xml"});
		
	}
	
}
