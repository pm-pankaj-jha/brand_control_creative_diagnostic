package com.pubmatic.action;

import org.springframework.util.ErrorHandler;

import com.pubmatic.adda.domain.CreativeInfo;

public class OCRAction extends BaseCreativeAction {
	
	
	public void preProcess() throws Exception {
		
		
	}

	
	public CreativeInfo execute(CreativeInfo creativeInfo) throws Exception {
		preProcess();
		
		System.out.println("This is OCRAction");
		postProcess();
		return creativeInfo;
	}

	
	public void postProcess() throws Exception {
		
		
	}


	
}
