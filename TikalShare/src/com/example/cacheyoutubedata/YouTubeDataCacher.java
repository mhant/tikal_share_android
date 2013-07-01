package com.example.cacheyoutubedata;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

import android.content.Context;
import android.util.Base64;

import com.google.gson.JsonSyntaxException;
import com.tikal.share.InfraException;
import com.tikal.share.youtube.YoutubeData;

public class YouTubeDataCacher {
	private AbstractDataCacheStore myStore;
	private static String FILE_NAME = "tikal.youtube";

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
			throw new InfraException(e);
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
			// The Close raises an exception!! Don't do it!!
			// ois.close();
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

	public void saveToFile(YoutubeData obj, Context context) {
		final File file = new File(context.getCacheDir(), FILE_NAME);
		FileOutputStream outputStream = null;
		ObjectOutputStream objectOutputStream = null;

		try {
			outputStream = new FileOutputStream(file);
			objectOutputStream = new ObjectOutputStream(outputStream);
			obj.write(objectOutputStream);
		}

		catch (Exception e) {
			throw new InfraException(e);
		} finally {
			try {
				if (objectOutputStream != null) {
					objectOutputStream.close();
				}
				if (outputStream != null) {
					outputStream.close();
				}
			} catch (Exception e) {
				throw new InfraException(e);
			}
		}
	}

	public YoutubeData loadFromFile(Context context) {
		final File file = new File(context.getCacheDir(), FILE_NAME);
		FileInputStream inputStream = null;
		ObjectInputStream objectInputStream = null;

		try {
			inputStream = new FileInputStream(file);
			objectInputStream = new ObjectInputStream(inputStream);

			YoutubeData youtubeData = new YoutubeData();
			youtubeData.read(objectInputStream);
			return youtubeData;
		} catch (FileNotFoundException f) {
			return null;
		} catch (Exception e) {
			throw new InfraException(e);
		} finally {
			try {
				if (objectInputStream != null) {
					objectInputStream.close();
				}
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (Exception e) {
				throw new InfraException(e);
			}
		}
	}

}
