package com.pubmatic.phantom;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.concurrent.TimeoutException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.pubmatic.adda.domain.CreativeInfo;
import com.pubmatic.adda.exception.PhantomWorkerException;

@EnableScheduling
public class PhantomServerManager extends Thread {

	private static Map<String, PhantomWorker> serverPool;
	protected static Logger logger = Logger.getLogger("PhantomServerManager");
	private int readTimeout;
	private int connectTimeout;
	private int maxTimeout;
	private String loadbalacerHost;
	private int loadbalacerPort;
	private String exec;
	private String script;
	private Set ports;

	public void init() {
		if (serverPool == null) {
			serverPool = new HashMap<String, PhantomWorker>();
		}

		Iterator iter = ports.iterator();

		System.out.println(serverPool);
		while (iter.hasNext()) {
			int port = Integer.parseInt("" + iter.next());
			try {
				if (isPhantomWorkerRunning(port)) {
					continue;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			serverPool
					.put("" + port, new PhantomWorker(exec, script,
							"localhost", port, connectTimeout, readTimeout,
							maxTimeout));
		}

	}

	public boolean isPhantomWorkerRunning(int port) throws IOException {

		PhantomWorker phantom = new PhantomWorker("localhost", port);
		return phantom.isRunning();

	}

	/*
	 * @Scheduled(fixedRate = 5000) public void reportCurrentTime() {
	 * SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	 * System.out.println("The time is now " + dateFormat.format(new Date())); }
	 */

	@Scheduled(fixedRate = 3600000)
	public void checkPhantoms() {
		init();
	}

	public String request(CreativeInfo creativeInfo)
			throws SocketTimeoutException, TimeoutException,
			PhantomWorkerException {
		String response = "";
		System.out.println("Inside request");
		Timer timer = new Timer();
		try {
			URL url = new URL("http://" + loadbalacerHost + ":"
					+ loadbalacerPort + "/");
			System.out.println(url.getPath());
			timer.schedule(new TimeOut(), maxTimeout);

			URLConnection connection = url.openConnection();
			connection.setDoOutput(true);
			connection.setConnectTimeout(connectTimeout);
			connection.setReadTimeout(readTimeout);
			connection.setRequestProperty("content-type",
					"application/json; charset=utf-8");
			OutputStream out = connection.getOutputStream();
			ObjectMapper mapper = new ObjectMapper();
			String params = mapper.writeValueAsString(creativeInfo);
			System.out.println(params);
			// params="{\"url\": \"yahoo.com\"}";
			out.write(params.getBytes());
			out.close();
			InputStream in = connection.getInputStream();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			IOUtils.copy(in, baos);
			in.close();
			response = new String(baos.toByteArray(), Charset.forName("utf-8"));

			timer.cancel();
		} catch (SocketTimeoutException ste) {
			timer.cancel();
			throw new SocketTimeoutException(ste.getMessage());
		} catch (Exception e) {
			timer.cancel();
			throw new PhantomWorkerException(e.getMessage());
		}
		System.out.println(response);
		return response;
	}

	public String createUniqueFileName(String extension) throws IOException {
		return System.getProperty("java.io.tmpdir") + "/"
				+ RandomStringUtils.randomAlphanumeric(8) + extension;
	}

	/**
	 * @return the readTimeout
	 */
	public int getReadTimeout() {
		return readTimeout;
	}

	/**
	 * @param readTimeout
	 *            the readTimeout to set
	 */
	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}

	/**
	 * @return the connectTimeout
	 */
	public int getConnectTimeout() {
		return connectTimeout;
	}

	/**
	 * @param connectTimeout
	 *            the connectTimeout to set
	 */
	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	/**
	 * @return the maxTimeout
	 */
	public int getMaxTimeout() {
		return maxTimeout;
	}

	/**
	 * @param maxTimeout
	 *            the maxTimeout to set
	 */
	public void setMaxTimeout(int maxTimeout) {
		this.maxTimeout = maxTimeout;
	}

	/**
	 * @return the loadbalacerHost
	 */
	public String getLoadbalacerHost() {
		return loadbalacerHost;
	}

	/**
	 * @param loadbalacerHost
	 *            the loadbalacerHost to set
	 */
	public void setLoadbalacerHost(String loadbalacerHost) {
		this.loadbalacerHost = loadbalacerHost;
	}

	/**
	 * @return the loadbalacerPort
	 */
	public int getLoadbalacerPort() {
		return loadbalacerPort;
	}

	/**
	 * @param loadbalacerPort
	 *            the loadbalacerPort to set
	 */
	public void setLoadbalacerPort(int loadbalacerPort) {
		this.loadbalacerPort = loadbalacerPort;
	}

	/**
	 * @return the exec
	 */
	public String getExec() {
		return exec;
	}

	/**
	 * @param exec
	 *            the exec to set
	 */
	public void setExec(String exec) {
		this.exec = exec;
	}

	/**
	 * @return the script
	 */
	public String getScript() {
		return script;
	}

	/**
	 * @param script
	 *            the script to set
	 */
	public void setScript(String script) {
		this.script = script;
	}

	/**
	 * @return the ports
	 */
	public Set getPorts() {
		return ports;
	}

	/**
	 * @param ports
	 *            the ports to set
	 */
	public void setPorts(Set ports) {
		this.ports = ports;
	}

}
