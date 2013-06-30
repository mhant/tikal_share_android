package com.example.cacheyoutubedata;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

import android.util.Base64;

import com.google.gson.JsonSyntaxException;

public class YouTubeDataCacher {
	private AbstractDataCacheStore myStore;

	public YouTubeDataCacher(AbstractDataCacheStore myStore) {
		this.myStore = myStore;
	}

	public void cacheThis(String cache_id, Object obj) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(baos);
			oos.writeObject(obj);
			oos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String jsonStr = Base64.encodeToString(baos.toByteArray(), 0);
		String objClass = obj.getClass().getName();

		this.myStore.store(cache_id, jsonStr, objClass);
	}

	public Object unchacheThis(String cache_id) {
		String jsonStr = "";
		String objClass = "";

		String[] rr = this.myStore.retrieve(cache_id);
		jsonStr = rr[0];
		objClass = rr[1];

		// The object is not cached (in particular since class name is empty)
		if (jsonStr.length() < 1 || objClass.length() < 1)
			return null;

		try {
			byte[] data = Base64.decode(jsonStr, 0);
			ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
			Object o = ois.readObject();
			//The  Close raises an exception!! Don't do it!!
			//ois.close();
			return o;
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
