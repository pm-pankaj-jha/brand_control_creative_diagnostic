package com.pubmatic.sink;

import java.io.IOException;

import com.pubmatic.adda.domain.CreativeInfo;

public interface DataSink {
	
	public CreativeInfo storeEvent(CreativeInfo creativeInfo) ;

}
