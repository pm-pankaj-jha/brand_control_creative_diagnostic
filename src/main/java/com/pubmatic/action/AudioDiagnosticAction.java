package com.pubmatic.action;

import org.codehaus.jackson.map.ObjectMapper;

import com.pubmatic.adda.domain.CreativeInfo;

public class AudioDiagnosticAction extends BaseCreativeAction {
	
	
	public void preProcess() throws Exception {
		
		
	}

	
	public CreativeInfo execute(CreativeInfo creativeInfo) throws Exception {
		preProcess();
		System.out.println("This is AudioDiagnosticAction");
		postProcess();
		creativeInfo.setIsAutoAudio("false");
		return creativeInfo;
		
	}

	
	public void postProcess() throws Exception {
		
		
	}

	


}

