package com.pubmatic.util;

public class CDUtil {
	
	public static boolean isNullOrEmpty(Object obj) {
	    if (obj == null || obj.toString().length() < 1 || obj.toString().equals("")|| obj.toString().equalsIgnoreCase("NA") || obj.toString().equalsIgnoreCase("null"))
	        return true;
	    return false;
	}

}
