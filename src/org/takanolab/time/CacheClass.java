package org.takanolab.time;

import java.util.HashMap;

import org.apache.http.cookie.SetCookie;

public class CacheClass {

	private HashMap<String, String> cache = new HashMap<String, String>();
	
	public void setCache(){
		cache.put("one", "one");
		cache.put("two", "one");
		cache.put("three", "one");
		cache.put("four", "one");
		cache.put("five", "one");
		cache.put("six", "one");
		cache.put("seven", "one");
		cache.put("eight", "one");
		cache.put("nine", "one");
	}
	public boolean isCache(){
		if(cache == null){
			return false;
		}else{
			return true;
		}
	}
}
