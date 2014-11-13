package com.pubmatic.action;

import org.codehaus.jackson.map.ObjectMapper;

import com.pubmatic.adda.domain.CreativeInfo;
import com.pubmatic.sink.NetCatSink;
import com.pubmatic.sink.RiakSink;

public class RIAKDispatchAction extends BaseCreativeAction {

	private RiakSink dataSink;

	@Override
	public void preProcess() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public CreativeInfo execute(CreativeInfo creativeInfo) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		String event = mapper.writeValueAsString(creativeInfo);
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
	public RiakSink getDataSink() {
		return dataSink;
	}

	/**
	 * @param dataSink
	 *            the dataSink to set
	 */
	public void setDataSink(RiakSink dataSink) {
		this.dataSink = dataSink;
	}

}
