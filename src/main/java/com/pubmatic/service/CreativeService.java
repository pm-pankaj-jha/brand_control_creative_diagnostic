package com.pubmatic.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pubmatic.adda.domain.CreativeInfo;
import com.pubmatic.dao.CreativeDAO;
import com.pubmatic.util.CDUtil;
import com.pubmatic.util.SpringApplicationContextProvider;
import com.pubmatic.workflow.CreativeWorkflow;

@Service("creativeService")
public class CreativeService {

	public CreativeInfo getCreativeInfo (CreativeInfo creative) {
		CreativeWorkflow workflow =(CreativeWorkflow) SpringApplicationContextProvider.getBean("creativeWorkflow");
		
		if (CDUtil.isNullOrEmpty(creative.getUrl())){
			creative.setMessage("ERROR:Creative URL is mandatory");
			return creative;
		}
    	return workflow.processWorkflow(creative);
		
	}
	
	
	public void processCreatives () {
		CreativeWorkflow workflow =(CreativeWorkflow) SpringApplicationContextProvider.getBean("creativeWorkflow");
		CreativeDAO creativeDAO = (CreativeDAO)SpringApplicationContextProvider.getBean("creativeDAO");
		//TODO : RUN in parallel.Check thread pools
		List <CreativeInfo> creativeList=creativeDAO.getAllCreatives();
    	for (int i=0;i<creativeList.size();i++){
    		CreativeInfo creative = creativeList.get(i);
    		workflow.processWorkflow(creative);
    	}
	
		
	}

}
