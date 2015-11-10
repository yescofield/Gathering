package com.yezimm.gathering.utils;


public class NullUtils {
	
	public static boolean isNull(Object o) {
		if (o == null) {
			return true;
		}
		
		if (o instanceof String) {
			return ((String) o).trim().length() == 0;
		}
		
		
		return false;
	}
}
