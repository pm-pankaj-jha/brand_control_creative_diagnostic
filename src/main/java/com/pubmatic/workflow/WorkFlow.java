package com.pubmatic.workflow;

import java.util.List;

import org.springframework.util.ErrorHandler;

import com.pubmatic.action.CreativeAction;

public interface WorkFlow {
	
	 public boolean supports(CreativeAction action);
     public void doActivities();
     public void doActivities(Object seedData);
     public void setActivities(List actions);
     public void setDefaultErrorHandler(ErrorHandler defaultErrorHandler);

}
