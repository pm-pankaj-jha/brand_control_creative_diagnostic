package com.pubmatic.sink;

import java.io.IOException;
import java.io.PrintStream;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.telnet.TelnetClient;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.pubmatic.adda.domain.CreativeInfo;



public class NetCatSink implements DataSink {
	
	private String host = "localhost";
	private int port = 8788;
	private PrintStream tsdbout = null;
	private List eventList = new ArrayList();
	private TelnetClient telnetClient = new TelnetClient();
	
	public CreativeInfo storeEvent(CreativeInfo creativeInfo) {
		ObjectMapper mapper = new ObjectMapper();
		String event = "";
		try {
			event = mapper.writeValueAsString(creativeInfo);
		} catch (JsonGenerationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (JsonMappingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		/*eventList.add(eventStr);
		if(eventList.size()>=50) {*/
			try {
				
				if (tsdbout == null) { // Only open the connection if necessary.
					telnetClient.connect(host, port);
					tsdbout = new PrintStream(telnetClient.getOutputStream());
				}
				tsdbout.print(event);
				tsdbout.flush();

			} catch (UnknownHostException e) {
				System.err.println("Don't know about host: " + host);
				creativeInfo.setMessage("Error : NetCatSink Exception");
			} catch (Exception e) {
				tsdbout.close(); // Only close the connection on errors.
				try {
					telnetClient.disconnect();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				tsdbout = null;
				

		/*	}*/

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
