package com.pubmatic.action;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.util.ErrorHandler;

import com.pubmatic.adda.domain.CreativeInfo;
import com.pubmatic.phantom.PhantomServerManager;

public class PhantomAction extends BaseCreativeAction {
    
	private PhantomServerManager phantomManager;
    
	
	/**
	 * @return the phantomManager
	 */
	public PhantomServerManager getPhantomManager() {
		return phantomManager;
	}


	/**
	 * @param phantomManager the phantomManager to set
	 */
	public void setPhantomManager(PhantomServerManager phantomManager) {
		this.phantomManager = phantomManager;
	}


	public void preProcess() throws Exception {
		// TODO Auto-generated method stub
		
	}

	
	public CreativeInfo execute(CreativeInfo creativeInfo) throws Exception {
		System.out.println("This is phantomAction");
		String creativeInfoStr = phantomManager.request(creativeInfo);
		//System.out.println(creativeInfoStr);
		ObjectMapper mapper = new ObjectMapper();
		creativeInfo = mapper.readValue(creativeInfoStr, CreativeInfo.class);
		return creativeInfo;
	}

	
	public void postProcess() throws Exception {
		// TODO Auto-generated method stub
		
	}


}
