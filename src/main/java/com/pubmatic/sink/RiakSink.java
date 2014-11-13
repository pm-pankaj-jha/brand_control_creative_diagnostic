package com.pubmatic.sink;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.net.telnet.TelnetClient;


import com.basho.riak.client.DefaultRiakObject;
import com.basho.riak.client.IRiakClient;
import com.basho.riak.client.IRiakObject;
import com.basho.riak.client.RiakException;
import com.basho.riak.client.RiakFactory;
import com.basho.riak.client.bucket.Bucket;
import com.basho.riak.client.query.indexes.RiakIndexes;
import com.pubmatic.adda.domain.CreativeInfo;

public class RiakSink implements DataSink {
	
	private String host = "localhost";
	private String bucket = "creativeInfo";
	private int port = 8087;
	private IRiakClient riakClient;
    private Bucket creativeInfoBucket;
    private static RiakSink instance;
    private String defaultContentType ="application/json";
    
    private RiakSink() {
        init();
    }
	
	private void init() {
		 try {
			 riakClient = RiakFactory.pbcClient(host, port);
			 creativeInfoBucket = riakClient.createBucket(bucket).nVal(1).allowSiblings(true).execute();
			 
		 } catch (RiakException ex) {
	            
	            if (null != riakClient) {
	                riakClient.shutdown();
	            }
	        }
	}
		
	

	public CreativeInfo storeEvent(CreativeInfo creativeInfo) {
		try {
		System.out.println("Inside Riak Sink");	
		String key = java.util.UUID.randomUUID().toString();
		IRiakObject myData = new DefaultRiakObject(bucket, key, null, null, null,defaultContentType, creativeInfo.getImage().getBytes(), null, new HashMap(), new RiakIndexes());
		
		creativeInfoBucket.store(myData).execute();
		String creativeURL= "http://localhost:8098/buckets/"+bucket+"/keys/"+key;
		System.out.println("creativeURL :" + creativeURL);
		creativeInfo.setImage(creativeURL);
		
		}catch (Exception e){
			e.printStackTrace();
			creativeInfo.setMessage("Error : RiakSink Exception");
		}
		return creativeInfo;
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return the bucket
	 */
	public String getBucket() {
		return bucket;
	}

	/**
	 * @param bucket the bucket to set
	 */
	public void setBucket(String bucket) {
		this.bucket = bucket;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}

}
