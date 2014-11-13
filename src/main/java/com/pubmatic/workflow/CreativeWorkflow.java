package com.pubmatic.workflow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

import com.pubmatic.action.ActionAsCallable;
import com.pubmatic.action.BaseCreativeAction;
import com.pubmatic.action.CreativeAction;
import com.pubmatic.action.OCRAction;
import com.pubmatic.adda.domain.CreativeInfo;

public class CreativeWorkflow {

	private final Log LOG = LogFactory.getLog(CreativeWorkflow.class);

	private String name = "creativeWorkFlow";

	private Map<String, CreativeAction> creativeActions;

	private Properties config;
	
	private static final ExecutorService workers = Executors.newCachedThreadPool();
	
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the config
	 */
	public Properties getConfig() {
		return config;
	}

	/**
	 * @param config the config to set
	 */
	public void setConfig(Properties config) {
		this.config = config;
	}

	/**
	 * @return the workflowname
	 */
	public  String getName() {
		return name;
	}

	private ApplicationContext applicationContext;	
	
	public CreativeInfo processWorkflowAction(String action ,CreativeInfo creativeInfo) throws Exception {
		try {
		ObjectMapper mapper = new ObjectMapper();
		creativeInfo= getCreativeAction(action).execute(creativeInfo);	
		creativeInfo.setMessage(creativeInfo.getMessage()+"|"+action+"sucess");
		} catch (Exception e) {
			e.printStackTrace();
			creativeInfo.setMessage(creativeInfo.getMessage()+"|"+action+":fail");
		}
		return creativeInfo;
	}
	
	public CreativeInfo processWorkflow(CreativeInfo creativeInfo) {
		
		//Get list of action to execute
		//String actionToExecute="phantomAction,netLogAction:audioDiagnosticAction,riakDispatchAction";
		String actionToExecute="phantomAction";
		//colon seperated should be run in parallel
		List<String> actionlist= Arrays.asList(actionToExecute.split("\\s*,\\s*"));
		for (String action : actionlist) {
			try {
				
				
				if (action.contains(":")){
					ExecutorService workers = Executors.newCachedThreadPool();
					List<String> parallellist= Arrays.asList(action.split("\\s*:\\s*"));
					List<Callable<CreativeInfo>> tasks = new ArrayList<Callable<CreativeInfo>>();
					for (final String parallel : parallellist) {
						tasks.add(new ActionAsCallable(parallel,creativeInfo));
					}
					// returns a list of Futures holding their status and results when all complete
			        List<Future<CreativeInfo>> completedTasks = workers.invokeAll(tasks);
			        System.out.println(tasks.size() +" Responses recieved.\n");
			         
			        for(Future<CreativeInfo> task : completedTasks)
			        {
			            //System.out.println(task.get().toString());
			        	CreativeInfo creative = task.get();
			        	if(creative.getAction().equals("netLogAction")){
			        		creativeInfo.setNetlog(creative.getNetlog());
			        	}else if(creative.getAction().equals("audioDiagnosticAction")){
			        		creativeInfo.setIsAutoAudio(creative.getIsAutoAudio());
			        	}
			        }
			         
			        /* shutdown your thread pool, else your application will keep running */
			        workers.shutdown();
				} else {
					creativeInfo = getCreativeAction(action).execute(creativeInfo);	
					creativeInfo.setMessage(creativeInfo.getMessage()+"|"+action+":success");
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				creativeInfo.setMessage(creativeInfo.getMessage()+"|"+action+":fail");
				/*if (action.equals("phantomAction")){
					creativeInfo.setDom("fail");
					creativeInfo.setHar("fail");
					creativeInfo.setNetlog("fail");
					creativeInfo.setSnapshot("fail");
				} else if (action.equals("netLogAction")) {
					creativeInfo.setDaisyChain("fail");
				}*/
			}
		}
		return creativeInfo;
	}

	private CreativeAction getCreativeAction(String actionName) {
		CreativeAction action = creativeActions.get(actionName);
		if (action == null ) {
			LOG.error("There is no defined action for " + actionName);
			throw new IllegalArgumentException(
					"There is no defined action for " + actionName);
		}
		return action;
	}


	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

	
	public Map<String, CreativeAction> getCreativeActions() {
		return creativeActions;
	}

	public void setCreativeActions(
			Map<String, CreativeAction> CreativeActions) {
		this.creativeActions = CreativeActions;
	}
}