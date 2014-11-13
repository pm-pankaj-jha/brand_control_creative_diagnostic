package com.pubmatic.action;

import org.codehaus.jackson.map.ObjectMapper;

import com.pubmatic.adda.domain.CreativeInfo;
import com.pubmatic.sink.NetCatSink;

public class FlumeDispatchAction extends BaseCreativeAction {

	NetCatSink dataSink;
	
	@Override
	public void preProcess() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public CreativeInfo execute(CreativeInfo creativeInfo) throws Exception {
		
		creativeInfo = dataSink.storeEvent(creativeInfo);
		
		return creativeInfo;
		
	}

	@Override
	public void postProcess() throws Exception {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @return the dataSink
	 */
	public NetCatSink getDataSink() {
		return dataSink;
	}

	/**
	 * @param dataSink the dataSink to set
	 */
	public void setDataSink(NetCatSink dataSink) {
		this.dataSink = dataSink;
	}
	

}
