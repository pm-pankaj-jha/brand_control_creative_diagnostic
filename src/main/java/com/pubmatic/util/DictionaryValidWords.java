package com.pubmatic.util;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rita.RiWordNet;
import rita.support.PorterStemmer;

public final class DictionaryValidWords {
	private static RiWordNet wordnet = new RiWordNet("/devtools/WordNet-3.1");
    private static final Set<String> brandDictionary = new TreeSet<String>();
    static {
    	brandDictionary.add("this");
    	brandDictionary.add("his");
    	brandDictionary.add("is");
    	brandDictionary.add("awe");
    	brandDictionary.add("we");
    	brandDictionary.add("some");
    	brandDictionary.add("awesome");
    	brandDictionary.add("foo");
    	brandDictionary.add("bar");
    }

    private DictionaryValidWords() {}

    /**
     * Returns set of valid words given an input string.
     * It eliminates duplicates.
     * 
     * @param str   The input string whose valid words need to be found out.
     * @return      List of valid words nested in the string.
     */
    public static Set<String> findValidStrings(String str) {
        if (str.length() ==  0) {
            throw new IllegalArgumentException("Strings of length 0 are illegal");
        }

        final Set<String> validWords = new HashSet<String>(); 
        for (int i = 0; i < str.length(); i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = i; j < str.length(); j++) {
                sb.append(str.charAt(j)); // O(1) complexity.
                if ( (wordnet.exists(sb.toString() ) || brandDictionary.contains(sb.toString() )) && sb.length() > 3 )  {
                    validWords.add(sb.toString());
                }
            }
        }
        return validWords;
    }
    
    public static Set  getKeywordsFromURL(String url) {    	
    	StringBuffer keywords=new StringBuffer();
    	//Remove all digits
    	url = url.replaceAll("\\d","");    
    	url=url.replace("http://","").replace("https://","");
    	/*Pattern pattern = Pattern.compile("(https?://)([^:^/]*)(:\\d*)?(.*)?");
   
    	Matcher matcher = pattern.matcher(url);
    	matcher.find();
    	String domain   = matcher.group(2);
    	String uri      = matcher.group(4);*/
    	//System.out.println(domain);
    	//System.out.println(uri);
    	//lowercase
    	url = url.toLowerCase();
    	Set<String> validWords=findValidStrings(url);
    	Set<String> keyWords=new HashSet();
    	//Stem the words
    	/* PorterStemmer ps = new PorterStemmer();
    	 
    	 System.out.println(validWords);
    	 for (String s : validWords) {
    		 if(keywords.length()> 0){
    			 keywords=keywords.append(",");
    		 }
    		 keywords=keywords.append(ps.stem(s));
    		 keyWords.add(ps.stem(s));
    		}*/
    	
    	return validWords;
	}
    
    public static void main(String[] args) {
		
		 String csvFile = "/home/pubmatic/Desktop/domains.csv";
		   Map<String, Integer> map = new HashMap<String, Integer>();
		  //String csvFile = "/home/pubmatic/workspace/CD/WebContent/alexa-top-10000-global.txt";
			BufferedReader br = null;
			String line = "";
			String cvsSplitBy = ",";
		 
			try {
		 
				br = new BufferedReader(new FileReader(csvFile));
				
				while ((line = br.readLine()) != null) {
		          
				     try {   // use comma as separator
					String url = line.trim();
					//data=getIndustry(url);
					Set <String> keywords=DictionaryValidWords.getKeywordsFromURL(url);
					for (String s : keywords) {
						Integer value = map.get(s);
			            if(value == null) {
			                map.put(s, 1);
			            }
			            else {
			                map.put(s, value + 1);
			            }
					}
				     }catch (Exception e){
				    	 e.printStackTrace();
				     }
		 
				     
		 
				}
				System.out.println(map.size());
				List <String>list=sortByComparator(map);
				List candidateList = new ArrayList();
				for (Map.Entry<String, Integer> entry : map.entrySet())
				{	
				    String candidate =entry.getKey();
				   // String member="";
				    for (int i=0;i<list.size();i++){
				    	String member=list.get(i);
				    	System.out.println();
				    	if (candidate.toLowerCase().contains(member.toLowerCase())){
				    		candidateList.add(candidate);
				    	}
					}
				}
				
			//	System.out.println(map.values());
		//		System.out.println(map.keySet());
				
				//System.out.println(x[x.length -1]);
				System.out.println("The member list is :"+list);
				System.out.println("The member list is :"+candidateList);
		 
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (br != null) {
					try {
						br.close();
						
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		
		
	}
    
    private static List<String> sortByComparator(Map<String, Integer> unsortMap) {
    	List list1=new ArrayList();
		// Convert Map to List
		List<Map.Entry<String, Integer>> list = 
			new LinkedList<Map.Entry<String, Integer>>(unsortMap.entrySet());
			
		// Sort list with comparator, to compare the Map values
		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Map.Entry<String, Integer> o1,
                                           Map.Entry<String, Integer> o2) {
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});
		System.out.println(list.size());
		int x= 	(int) (list.size() * 0.10);
		x=list.size() -x;
		System.out.println(x);
		
		for (int i=x;i <list.size();i++ ){
			list1.add(list.get(i));
		}
		System.out.println("list1" + list1);
 
		/*// Convert sorted map back to a Map
		Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
		for (Iterator<Map.Entry<String, Integer>> it = list.iterator(); it.hasNext();) {
			Map.Entry<String, Integer> entry = it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}*/
		return list1;
	}

}


