package com.example.cacheyoutubedata;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class YouTubeDataCacher {
	private AbstractDataCacheStore myStore;
	
	public YouTubeDataCacher(AbstractDataCacheStore myStore){
		this.myStore = myStore;
	}
	
	public void cacheThis(String cache_id, Object obj){
		String jsonStr = new Gson().toJson(obj);
		String objClass = obj.getClass().getName();
		
		this.myStore.store(cache_id, jsonStr, objClass);
	}
	
	public Object unchacheThis(String cache_id){
		String jsonStr ="";
		String objClass = "";
		
		String [] rr = this.myStore.retrieve(cache_id);
		jsonStr =  rr[0];
		objClass = rr[1];
		
		//The object is not cached (in particular since class name is empty)
		if(jsonStr.length() < 1 || objClass.length() < 1)
			return null;
		
		
		try {
			return new Gson().fromJson(jsonStr, Class.forName(objClass));
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
}
