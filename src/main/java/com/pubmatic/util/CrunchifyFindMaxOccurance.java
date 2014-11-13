package com.pubmatic.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;

public class CrunchifyFindMaxOccurance {
	 
    /**
     * @author Crunchify.com
     */
 
    public static void main(String[] args) throws FileNotFoundException, IOException {
 
    	FileReader reader = new FileReader
    	          ("/devtools/phantom/deliverables/1.html");
    	    String inputLine = HTMLUtil.extractText(reader);
        Map<String, Integer> map = new TreeMap<String, Integer>();
 
        
           
            String[] words = inputLine.split("[ \n\t\r.,;:!?(){}]");
 
                for (int counter = 0; counter < words.length; counter++) {
                    String key = words[counter].toLowerCase(); // remove .toLowerCase for Case Sensitive result.
                    if (StringUtils.isNumeric(key)|| CDUtil.isNullOrEmpty(key)|| key.length()<=3){
                    	continue;
                    }
                    if (key.length() > 0) {
                        if (map.get(key) == null ) {
                            map.put(key, 1);
                        }
                        else {
                            int value = map.get(key).intValue();
                            value++;
                            map.put(key, value);
                        }
                    }
                 }
            
          
            map=sortByValue(map);
            Set<Map.Entry<String, Integer>> entrySet = map.entrySet();
            System.out.println("Words" + "\t" + "# of Occurances");
            for (Map.Entry<String, Integer> entry : entrySet) {
                System.out.println(entry.getKey() + "\t\t" + entry.getValue());
            }
            //System.out.println("\nMaixmum Occurance of Word in file: " + getLastElement(entrySet));
 
       
 
    }
    static Object getLastElement(final Collection<Entry<String, Integer>> c) {
        final Iterator<Entry<String, Integer>> itr = c.iterator();
        Object lastElement = itr.next();
        while(itr.hasNext()) {
            lastElement=itr.next();
        }
        return lastElement;
    }
    
    public static <K, V extends Comparable<? super V>> Map<K, V> 
    sortByValue( Map<K, V> map )
{
    List<Map.Entry<K, V>> list =
        new LinkedList<Map.Entry<K, V>>( map.entrySet() );
    Collections.sort( list, new Comparator<Map.Entry<K, V>>()
    {
        public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 )
        {
            return (o1.getValue()).compareTo( o2.getValue() );
        }
    } );

    Map<K, V> result = new LinkedHashMap<K, V>();
    for (Map.Entry<K, V> entry : list)
    {
        result.put( entry.getKey(), entry.getValue() );
    }
    return result;
}
}
