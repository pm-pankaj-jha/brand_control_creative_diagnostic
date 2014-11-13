package com.pubmatic.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.BufferedReader;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class HTMLUtil {
  private HTMLUtil() {}
  
  private static PrintWriter writer = null;

  public static String extractText(Reader reader) throws IOException {
    StringBuilder sb = new StringBuilder();
    BufferedReader br = new BufferedReader(reader);
    String line;
    while ( (line=br.readLine()) != null) {
      sb.append(line);
    }
    String textOnly = Jsoup.parse(sb.toString()).text();
    return textOnly;
  }
  

  public    static String getIndustry (String nativeUrl) throws IOException {
	 System.out.println(nativeUrl);
	  String url = "http://www.google.com/search?hl=en&btnI=1&q="+nativeUrl+" wikipedia";
	  //Search for local cache
	  String categoryText1 = "";
	  String categoryText2 = "";
	  String categoryText3 = "";
	  String keywords = "";
	  String description="";
	  String brand="";
	  Document document = null;
	try {
		document = Jsoup.connect(url).timeout(5000).userAgent("Mozilla").get();
		 Elements category= document.select("td.category");
		 
		  int i=0;
		  for (Element element : category) {
			//  System.out.println(element);
			  if(i==1 ){			  
				  categoryText1 = element.text();
				  break;
			  }		    
			    i++;
			}
		  
		
		  Elements paragraphs = document.select(".mw-content-ltr p, .mw-content-ltr li");

		   Element firstParagraph = paragraphs.first();
		   if  (firstParagraph!=null) {
		   String [] textArray = firstParagraph.text().split(" ");
		   for (int j=0;j<textArray.length;j++){
			   if (textArray[j].equals("is")){
				   categoryText2=textArray[j+2] + " " +textArray[j+3] + " " + textArray[j+4] +" "+ textArray[j+5];
				   break;
			   }
		   }
		  }
		
		  
			 Elements brandEle= document.select("h1.firstHeading");
			 for (Element element : brandEle) {
					//  System.out.println(element);
					  if(i==1 ){			  
						  brand = element.text();
						  break;
					  }		    
					    i++;
					}
			 
			
			   
			   
			 
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	 
	 // String categoryText = category.().text();
	 
		 // url = "http://www.google.com/search?hl=en&btnI=1&q="+nativeUrl;
	
	url = nativeUrl;
	if (!url.contains("www.")){
		url=url.replaceFirst("//", "//www.");
		System.out.println(url);
	}
		  try {
			document = Jsoup.connect(url).followRedirects(true).timeout(10000).userAgent("Mozilla").get();
			
			if ( CDUtil.isNullOrEmpty(brand)){
				String title = document.title();
			 if(title.contains(" ")){
				   brand= title.substring(0, title.indexOf(" ")); 
				}
			 else {
				 brand=title;
			 }
			}
			  description = document.select("meta[name=description]").get(0).attr("content");
			 keywords = document.select("meta[name=keywords]").first().attr("content");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	 
	 
	  //String textOnly = Jsoup.parse(document.html()).text();
	  //System.out.println(textOnly);
		  String urlkeywords =DictionaryValidWords.getKeywordsFromURL(nativeUrl);
	 String data= nativeUrl + "," + categoryText1+ "," + categoryText2+ "," + categoryText3+ "," + brand+ "," + urlkeywords +"," + keywords + "," + description;
	
	 
	 return data;
   
    
  }

  public final static void main(String[] args) throws Exception{
    /*FileReader reader = new FileReader
          ("/devtools/phantom/deliverables/1.html");
    System.out.println(HTMLUtil.extractText(reader));*/
    
    ///System.out.println(getIndustry("www.sony.com"));
	 test();
	  //getIndustry1("blackberry");
  }
  
  public static void test() throws FileNotFoundException, IOException {
	  String csvFile = "/home/pubmatic/Desktop/domains.csv";
	  //String csvFile = "/home/pubmatic/workspace/CD/WebContent/alexa-top-10000-global.txt";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
	 
		try {
	 
			br = new BufferedReader(new FileReader(csvFile));
			String data="";
			while ((line = br.readLine()) != null) {
	          
			     try {   // use comma as separator
				String url = line.trim();
				//data=getIndustry(url);
				data=DictionaryValidWords.getKeywordsFromURL(url);
			     }catch (Exception e){
			    	 e.printStackTrace();
			     }
	 
			     writeToCSV(data);
	 
			}
	 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
					 writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
  
  public   static void getIndustry1 (String nativeUrl) {
	  String url = "http://google.com/search?q="+nativeUrl;
	  //Search for local cache
	  String categoryText = "";
	  String brand="";
	  Document document = null;
	  int i =0;
	try {
		document = Jsoup.connect(url).timeout(5000).userAgent("Mozilla").get();
		 
		System.out.println(document.html());
		Elements category= document.select("div.krable");
		 for (Element element : category) {
				//  System.out.println(element);
				  if(i==1 ){			  
					  brand = element.text();
					  System.out.println(brand);
					  break;
				  }		    
				    i++;
				}
	} catch (Exception e){
		e.printStackTrace();
	}
  }
  
  public static void writeToCSV(String data) throws IOException {
	  
	  String outputFile = "/home/pubmatic/Desktop/landingPageInsights.csv";
	  
	  if(writer==null){
		   writer = new PrintWriter(outputFile, "UTF-8");
	  }
	  
	 writer.println(data);		
	 writer.flush();
		
	}
}


