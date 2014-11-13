package com.pubmatic.bc.test;



import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.TimeoutException;


import org.apache.commons.io.IOUtils;
import org.im4java.core.CompositeCmd;
import org.im4java.core.IMOperation;
import org.im4java.core.Info;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;


import com.fasterxml.jackson.core.JsonParser;
import com.google.gson.Gson;
import com.pubmatic.adda.exception.PhantomWorkerException;
import com.pubmatic.phantom.PhantomWorker;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;


public class Server {
	private Process process;
	private final int port;
	private final String host;
	private final int readTimeout;
	private final int connectTimeout;
	private final int maxTimeout;
	private ServerState state = ServerState.IDLE;
	// assumes the current class is called logger
	private final static Logger logger = Logger.getLogger(Server.class.getName());
	
	public Server(String exec, String script, String host, int port, int connectTimeout, int readTimeout, int maxTimeout) throws InterruptedException {

		// assign port and host to this instance
		this.port = port;
		this.host = host;
		this.connectTimeout = connectTimeout;
		this.readTimeout = readTimeout;
		this.maxTimeout = maxTimeout;
		exec = "/opt/phantomjs-1.9.7-linux-x86_64/bin/phantomjs";
		script="phantomServerTest.js";
		try {
			
			
			ArrayList<String> commands = new ArrayList<String>();
			commands.add(exec);
			commands.add("/home/pubmatic/Desktop/"  + script);
		/*	commands.add("-host");
			commands.add(host);*/
			commands.add("-port");
			commands.add("" + port);

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

	public String request(String params) throws TimeoutException, PhantomWorkerException, IOException {
		String response = "";
		System.out.println("Inside request");
		Timer _timer = new Timer();
		System.out.println(host +":"+port);
		try {
			URL url = new URL("http://" + host + ":"
					+ port + "/");
            System.out.println(url.getPath());
			// TEST sockettimeout
			//url = new URL("http://" + host + ":7777/");

			state = ServerState.BUSY;

			_timer.schedule(new TimeOut(this), maxTimeout);			
			
			URLConnection connection = url.openConnection();
			connection.setDoOutput(true);
			connection.setConnectTimeout(connectTimeout);
			connection.setReadTimeout(readTimeout);

			OutputStream out = connection.getOutputStream();
			//params="{\"url\": \"yahoo.com\"}";
			out.write(params.getBytes());
			out.close();
			InputStream in = connection.getInputStream();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			IOUtils.copy(in, baos);
			in.close();
			response = new String(baos.toByteArray(), Charset.forName("utf-8"));

			_timer.cancel();
			state = ServerState.IDLE;
		} catch (SocketTimeoutException ste) {
			_timer.cancel();
			throw new SocketTimeoutException(ste.getMessage());
		} catch (Exception e) {
			if(state == ServerState.TIMEDOUT) {
				throw new TimeoutException(e.getMessage());
			}
			_timer.cancel();
			throw new PhantomWorkerException(e.getMessage());
		}
		return response;
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
	public static void main(String[] args) throws TimeoutException, PhantomWorkerException, InterruptedException, IOException {
		System.out.println("Hello");
	    Server server = new Server("", "", "localhost", 8001, 500, 15000, 20000);
		//Server server1 = new Server("", "", "localhost", 8002, 500, 10000, 10000);
		
		String params="{\"url\": \"localhost/test2.html\"}";
		Thread.currentThread().sleep(5000);
		//String params="{"url": "yahoo.com"}";
		if (server.getState().equals(ServerState.IDLE)){
		String response =server.request(params);
		System.out.println(response);
		}
		//Map jsonJavaRootObject = new Gson().fromJson(response, Map.class);
		//String image=(String) jsonJavaRootObject.get("image");
		//System.out.println(image);
		//trimImage(image);
		Thread.currentThread().sleep(60000);
		
	
		
		
    }
 
		
	

	
	public static String compress(String src) throws IOException{
		
		 ByteArrayOutputStream rstBao = new ByteArrayOutputStream();
		 GZIPOutputStream zos = new GZIPOutputStream(rstBao);
		 zos.write(src.getBytes());
		 IOUtils.closeQuietly(zos);
		 byte[] bytes = rstBao.toByteArray();
		 return Base64.encode(bytes);
		 
		
		
	}
	
	 public static void uncompressString(String zippedBase64Str)
			 throws IOException {
		    String result = null;
		    byte[] bytes = Base64.decode(zippedBase64Str);
		    GZIPInputStream zi = null;
		    try {
		    	zi = new GZIPInputStream(new ByteArrayInputStream(bytes));
		    	result = IOUtils.toString(zi);
		    } finally {
		    	IOUtils.closeQuietly(zi);
		    }
		   // return result;
		    FileWriter fw = new FileWriter("/opt/compress.txt");
		    IOUtils.write(result, fw);
		    IOUtils.closeQuietly(fw);
	
	 }
	 
	 public static void trimImage(String base64) throws IOException, InterruptedException{
		 //String size = "600x";
		/*ProcessBuilder pb = new ProcessBuilder("convert", "-size", size,
		         "xc:white", "-font",
		         "/usr/share/fonts/truetype/ttf-dejavu/DejaVuSerif.ttf",
		         "-pointsize", "12", "-draw",
		         "text 300,300 \"*****@hotmail.com\"",
		         "/home/pubmatic/workspace/Test/test.png");*/
		
			
		 //convert -trim 'inline:data:png;base64,iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAAHElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg==' - | base64
		 //base64="iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAAHElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg==";
		 
		
		 ProcessBuilder pb = new ProcessBuilder("/home/pubmatic/Desktop/trim.sh", base64);
		 pb.redirectErrorStream(true);

		 Process p = pb.start();
		 BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
		 String line = null;
		 while((line=br.readLine())!=null){
		     System.out.println(line);
		 }
		 System.out.println(p.waitFor());
	 }
}

