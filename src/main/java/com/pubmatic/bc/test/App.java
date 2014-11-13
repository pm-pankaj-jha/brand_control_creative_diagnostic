package com.pubmatic.bc.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


import com.pubmatic.adda.domain.CreativeInfo;
import com.pubmatic.dao.CreativeDAO;
import com.pubmatic.phantom.PhantomWorker;
import com.pubmatic.phantom.PhantomServerManager;
import com.pubmatic.workflow.CreativeWorkflow;

public class App {
	public static void main( String[] args )
    {
    	//ApplicationContext context =  new ClassPathXmlApplicationContext(new String[] {"Spring-Common.xml", "Spring-Connection.xml","Spring-ModuleA.xml"});
    	ApplicationContext context = new ClassPathXmlApplicationContext("**/spring-job.xml");
    	PhantomServerManager serverManager = (PhantomServerManager)context.getBean("phantomServerManager");
    	serverManager.init();
    	
    	//Sysnchronous workflow (via rest)
    	CreativeWorkflow workflow = (CreativeWorkflow)context.getBean("creativeWorkflow");
    	CreativeInfo creativeInfo =new CreativeInfo();
    	creativeInfo.setUrl("localhost/test2.html") ;
    	workflow.processWorkflow(creativeInfo);
    	
    	// Asynchronous workflow via schedule
		CreativeDAO creativeDAO = (CreativeDAO) context.getBean("creativeDAO");
		List <CreativeInfo> creativeList=creativeDAO.getAllCreatives();
    	for (int i=0;i<creativeList.size();i++){
    		CreativeInfo creative = creativeList.get(i);
    		System.out.println(workflow.processWorkflow(creative));
    	}
		
 
    }
}
