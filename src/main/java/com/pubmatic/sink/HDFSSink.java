package com.pubmatic.sink;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URISyntaxException;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.Progressable;

import com.pubmatic.adda.domain.CreativeInfo;

public class HDFSSink implements DataSink{

	@Override
	public CreativeInfo storeEvent(CreativeInfo creativeInfo) {
		Configuration configuration = new Configuration();
		configuration.addResource(new Path("/usr/local/hadoop/etc/hadoop/core-site.xml"));
		configuration.addResource(new Path("/usr/local/hadoop/etc/hadoop/hdfs-site.xml"));

		FileSystem hdfs=null;
		try {
			hdfs = FileSystem.get( new URI( "hdfs://127.0.0.1:9000" ), configuration );
			OutputStream os = null;
		Path file = new Path("hdfs://localhost:54310/2014-10-10/creative/creative.txt");
		if ( !hdfs.exists( file )) {
		 os = hdfs.create( file,
		    new Progressable() {
		        public void progress() {
		            System.out.println("...bytes written: [ bytesWritten ]");
		        } });
		}
		BufferedWriter br = new BufferedWriter( new OutputStreamWriter( os, "UTF-8" ) );
		br.write("Hello World");
		br.close();
		hdfs.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return creativeInfo;
	}
public static void main(String[] args) {
	HDFSSink sink = new HDFSSink();
	CreativeInfo info = new CreativeInfo();
	info.setImage("Hello.image");
	sink.storeEvent(info);
	
}
}
