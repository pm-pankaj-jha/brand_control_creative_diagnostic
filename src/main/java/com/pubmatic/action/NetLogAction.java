package com.pubmatic.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.util.ErrorHandler;

import com.pubmatic.adda.domain.CreativeInfo;
import com.pubmatic.util.CDUtil;

public class NetLogAction extends BaseCreativeAction {
	
	
	public void preProcess() throws Exception {
		
		
	}

	
	public CreativeInfo execute(CreativeInfo creativeInfo) throws Exception {
		preProcess();
		System.out.println("This is NetLogAction");
		if(CDUtil.isNullOrEmpty(creativeInfo.getNetlog())){
			return creativeInfo;
		}
		List<String> netLogURls = Arrays.asList(creativeInfo.getNetlog().split("\\s*#\\s*"));
		
		/*** create stack variables to collect the track urls ***/
		List<String> showAdsUrlStack = new ArrayList() ;
		List<String> trackUrlStack = new ArrayList() ;
		
		for (int i=0;i<netLogURls.size();i++) {
			String url=netLogURls.get(i);
			if (CDUtil.isNullOrEmpty(url)){
				String regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
				if (url.matches("^(http|https)://[a-zA-z0-9]*showads.*.pubmatic.com//AdServer//AdServerServlet")) {
					showAdsUrlStack.add(url);
				}else if (url.matches("^(http|https)://[a-zA-z0-9]*track.*.pubmatic.com//AdServer//AdDisplayTrackerServlet")){
					trackUrlStack.add(url);
				}
			}
			
		}
		
		
		postProcess();
		return creativeInfo;
	}
	public void parseTrackURLs(){
		
	}
	
	public void postProcess() throws Exception {
		
		
	}

	


}
