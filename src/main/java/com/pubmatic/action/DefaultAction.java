package com.pubmatic.action;

import org.springframework.util.ErrorHandler;

import com.pubmatic.adda.domain.CreativeInfo;

public class DefaultAction extends BaseCreativeAction {

	
	public void preProcess() throws Exception {
		
		
	}

	
	public CreativeInfo execute(CreativeInfo context) throws Exception {
		preProcess();
		postProcess();
		return null;
	}

	
	public void postProcess() throws Exception {
		
		
	}

	

}
