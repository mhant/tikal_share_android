package com.tikal.share.cacheyoutubedata;

public interface AbstractDataCacheStore {
	void store(String cache_id, String json, String class_name);
	String[] retrieve(String cache_id);
	
	void store(String cache_id, Object obj);
}
