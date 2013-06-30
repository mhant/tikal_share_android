package com.example.cacheyoutubedata;

public interface AbstractDataCacheStore {
	public void store(String cache_id, String json, String class_name);
	public String[] retrieve(String cache_id);
}
