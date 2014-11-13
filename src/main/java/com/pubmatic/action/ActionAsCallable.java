package com.pubmatic.action;

import java.util.concurrent.Callable;

import com.pubmatic.adda.domain.CreativeInfo;
import com.pubmatic.util.SpringApplicationContextProvider;
import com.pubmatic.workflow.CreativeWorkflow;

public class ActionAsCallable implements Callable<CreativeInfo>{
	
	private String actionName;
	private CreativeInfo creativeInfo;
    
    public ActionAsCallable(String actionName,CreativeInfo creativeInfo) {
        this.actionName = actionName;
        this.creativeInfo = creativeInfo;
    }
	@Override
	public CreativeInfo call() throws Exception {
		System.out.println("Exec----------parallel");
		CreativeWorkflow workflow =(CreativeWorkflow) SpringApplicationContextProvider.getBean("creativeWorkflow");
		CreativeInfo creative= workflow.processWorkflowAction(actionName,creativeInfo);
		creative.setAction(actionName);
		return creative;
		
	}

}
