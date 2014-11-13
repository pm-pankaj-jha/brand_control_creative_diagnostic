package com.pubmatic.phantom;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Timer;
import java.util.concurrent.TimeoutException;

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.map.ObjectMapper;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.pubmatic.adda.exception.PhantomWorkerException;


public class PhantomWorker {
	private Process process;
	private final int port;
	private final String host;	
	private ServerState state = ServerState.IDLE;
	
	
	private final static Logger logger = Logger.getLogger(PhantomWorker.class.getName());
	
	public PhantomWorker(String host, int port ){
		this.port = port;
		this.host = host;
	}
	
	public PhantomWorker(String exec, String script, String host, int port, int connectTimeout, int readTimeout, int maxTimeout) {

		// assign port and host to this instance
		this.port = port;
		this.host = host;
		
		try {
			ArrayList<String> commands = new ArrayList<String>();
			commands.add(exec);
			commands.add( script);
		/*	commands.add("-host");
			commands.add(host);*/
			commands.add("-port");
			commands.add("" + port);
			commands.add("> "+port+".log");

			logger.log(Level.INFO,commands.toString());

			process = new ProcessBuilder(commands).start();
			//process=Runtime.getRuntime().exec("phantomjs --debug=true /home/pubmatic/Desktop/phantomServerTest.js --port 7070");
			final BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(process.getInputStream()));
			String readLine = bufferedReader.readLine();
			System.out.println(readLine);
			if (readLine == null || !readLine.contains("ready")) {
				throw new RuntimeException("Error, PhantomJS couldnot start");
			}

			initialize();

			Runtime.getRuntime().addShutdownHook(new Thread() {
				@Override
				public void run() {
					if (process != null) {
						logger.log(Level.INFO,"Shutting down PhantomJS instance, kill process directly, " + this.toString());
						try {
							process.getErrorStream().close();
							process.getInputStream().close();
							process.getOutputStream().close();
						} catch (IOException e) {
							logger.log(Level.SEVERE,"Error while shutting down process: " + e.getMessage());
						}
						process.destroy();
					}
				}
			});
		
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void initialize() {
		logger.log(Level.INFO,"Phantom server started on port " + port);
	}

	public boolean isRunning () {
		try {
		URL url = new URL("http://" + host + ":"	+ port + "/");
        System.out.println(url.getPath());
	  	URLConnection connection = url.openConnection();
		connection.setDoOutput(true);		
		connection.setRequestProperty("content-type","application/json; charset=utf-8");
		OutputStream out = connection.getOutputStream();
		ObjectMapper mapper = new ObjectMapper();			
		String params="{\"status\": true}";
		
		out.write(params.getBytes());
		out.close();
		InputStream in = connection.getInputStream();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		IOUtils.copy(in, baos);
		in.close();
		String response = new String(baos.toByteArray(), Charset.forName("utf-8"));
		if (response.equals("OK")){
			System.out.println("--------------------IS RUNNING");
			return true;
		}
		}catch (Exception e){
			System.out.println(e.getMessage());
		}
		return false;
	}

	public void cleanup() {
		try {
			/* It's not enough to only destroy the process, this helps*/
			process.getErrorStream().close();
			process.getInputStream().close();
			process.getOutputStream().close();
		} catch (IOException e) {
			logger.log(Level.SEVERE,"Error while shutting down process: " + e.getMessage());
		}

		process.destroy();
		process = null;
		logger.log(Level.SEVERE,"Destroyed phantomJS process running on port " + port);
	}

	public int getPort() {
		return port;
	}

	public String getHost() {
		return host;
	}

	public ServerState getState() {
		return state;
	}

	public void setState(ServerState state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return this.getClass().getName() + "listening to port: " + port;
	}
	public static void main(String[] args) throws SocketTimeoutException, TimeoutException, PhantomWorkerException, InterruptedException {
		System.out.println("Hello");
		PhantomWorker server = new PhantomWorker("", "", "localhost", 8001, 500, 6000, 6500);
		PhantomWorker server1 = new PhantomWorker("", "", "localhost", 8002, 500, 6000, 6500);
		PhantomWorker server2 = new PhantomWorker("", "", "localhost", 8003, 500, 6000, 6500);
		PhantomWorker server3 = new PhantomWorker("", "", "localhost", 8004, 500, 6000, 6500);
		//String params="{\"url\": \"yahoo.com\"}";
		
		//String params="{"url": "yahoo.com"}";
		//System.out.println(server.request(params));
		
		while (true){
			Thread.currentThread().sleep(5000);
			
		}
	}
}

