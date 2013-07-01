package com.tikal.share.youtube;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.tikal.share.InfraException;

public class LookupChannel {
	public LookupChannel(boolean debugMode) {
		super();
		this.debugMode = debugMode;
	}

	private static final String playlistUrl = "https://gdata.youtube.com/feeds/api/users/%s/playlists?v=2&fields=entry/title,entry/id";
	private static final String videosUrl = "http://gdata.youtube.com/feeds/api/playlists/%s?fields=entry(title,published,link[@rel='alternate'],media:group(media:thumbnail,yt:duration,media:description))";
//	
	private static String debugPlayList = "<?xml version='1.0' encoding='UTF-8'?><feed xmlns='http://www.w3.org/2005/Atom'><entry><id>tag:youtube.com,2008:user:androiddev101:playlist:PLKRP8CTQBP8Ez1hka2dmWeuSGvPhwS8u1</id><title>Basics and Other</title></entry><entry><id>tag:youtube.com,2008:user:androiddev101:playlist:PLKRP8CTQBP8Ge6G5SK27s4LjFniHz6-PB</id><title>Interfaces</title></entry><entry><id>tag:youtube.com,2008:user:androiddev101:playlist:PLKRP8CTQBP8FMOF9sYZSslUy7zGmG8Xpp</id><title>Graphical</title></entry><entry><id>tag:youtube.com,2008:user:androiddev101:playlist:PLKRP8CTQBP8GE7TRf72Y_M0-2YDRjEQlm</id><title>AndroidDev101 Tutorials</title></entry></feed>";
	private static String debugVideo = "<?xml version='1.0' encoding='UTF-8'?><feed xmlns='http://www.w3.org/2005/Atom'><entry><id>tag:youtube.com,2008:user:androiddev101:playlist:PLKRP8CTQBP8Ez1hka2dmWeuSGvPhwS8u1</id><title>Basics and Other</title></entry><entry><id>tag:youtube.com,2008:user:androiddev101:playlist:PLKRP8CTQBP8Ge6G5SK27s4LjFniHz6-PB</id><title>Interfaces</title></entry><entry><id>tag:youtube.com,2008:user:androiddev101:playlist:PLKRP8CTQBP8FMOF9sYZSslUy7zGmG8Xpp</id><title>Graphical</title></entry><entry><id>tag:youtube.com,2008:user:androiddev101:playlist:PLKRP8CTQBP8GE7TRf72Y_M0-2YDRjEQlm</id><title>AndroidDev101 Tutorials</title></entry></feed>";
	private boolean debugMode = true;

	public static Document stringToDocument(String xmlSource) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			return builder.parse(new InputSource(new StringReader(xmlSource)));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public YoutubeData getYoutubeData(String userName,boolean downloadThumbNail){
		YoutubeData result = new YoutubeData(userName);
		result.setPlayList(getFullListByUser(userName,downloadThumbNail));
		result.setUpdated(new Date());
		return result;
	}

	public List<YoutubePlaylist> getFullListByUser(String userName, boolean downloadThumbNail){
		List<YoutubePlaylist> result = getPlayList(userName);
		for (YoutubePlaylist youtubePlaylist : result){
			youtubePlaylist.setYoutubeVideoInfo(getVideoList(youtubePlaylist.getId(),downloadThumbNail));
		}
		return result;
	}
	
	public List<YoutubePlaylist> getPlayList(String userName) {
		List<YoutubePlaylist> list = new LinkedList<YoutubePlaylist>();
		String sendRequest;
		if (debugMode) {
			sendRequest = debugPlayList;
		} else {
			sendRequest = sendRequest(String.format(playlistUrl, userName));
		}
		Document stringToDocument = stringToDocument(sendRequest);
		try {
			NodeList nodes = stringToDocument.getElementsByTagName("entry");
			for (int i = 0; i < nodes.getLength(); i++) {
				Node item = nodes.item(i);
				String id = item.getChildNodes().item(0).getTextContent();
				int playListIndex = id.indexOf("playlist:");
				id = id.substring(playListIndex + 9);
				String title = item.getChildNodes().item(1).getTextContent();
				list.add(new YoutubePlaylist(title, id));
			}
		} catch (Exception e) {
			throw new InfraException(e);
		}
		return list;
	}

	private Node getByName(NodeList list, String name){
		for (int i=0; i<list.getLength();i++){
			Node item = list.item(i);
			if (item.getNodeName().equals(name)){
				return item;
			}
		}
		return null;
	}
	
	public List<YoutubeVideoInfo> getVideoList(String playListId, boolean downloadThumbNail) {
		List<YoutubeVideoInfo> list = new LinkedList<YoutubeVideoInfo>();
		String sendRequest;
		if (debugMode) {
			sendRequest = debugVideo;
		} else {
			sendRequest = sendRequest(String.format(videosUrl, playListId));
		}

		Document stringToDocument = stringToDocument(sendRequest);
		NodeList nodes = stringToDocument.getElementsByTagName("entry");
		for (int i = 0; i < nodes.getLength(); i++) {
			Node item = nodes.item(i);
			String published = item.getChildNodes().item(0).getTextContent();
			String title = item.getChildNodes().item(1).getTextContent();
			Node linkNode = item.getChildNodes().item(2);
			Node href = linkNode.getAttributes().getNamedItem("href");
			int pos = href.getTextContent().indexOf("?v=");
			String videoId = href.getTextContent().substring(pos+3);
			pos = videoId.indexOf("&");
			videoId = videoId.substring(0, pos);
			Node mediaGroup = item.getChildNodes().item(3);
			Node thumbnail = getByName(mediaGroup.getChildNodes(),"media:thumbnail");
			Node durationNode = getByName(mediaGroup.getChildNodes(),"yt:duration");
			Node descriptionNode = getByName(mediaGroup.getChildNodes(),"media:description");
			
			Integer duration = Integer.parseInt(durationNode.getAttributes().getNamedItem("seconds").getTextContent());
			YoutubeVideoInfo youtubeVideoInfo = new YoutubeVideoInfo(videoId, title,descriptionNode.getTextContent(),thumbnail.getAttributes().getNamedItem("url").getTextContent(),published,duration);
			if (downloadThumbNail && youtubeVideoInfo.getThumbnail()!=null && youtubeVideoInfo.getThumbnail()!=""){
				try{
				URL newurl = new URL(youtubeVideoInfo.getThumbnail());
				Bitmap decodeStream = BitmapFactory.decodeStream(newurl.openConnection() .getInputStream());
				youtubeVideoInfo.setThumbnailBmp(decodeStream);
				}
				catch(Exception e){
					throw new InfraException(e);
				}
			}
			list.add(youtubeVideoInfo);

		}

		return list;
	}

	private String sendRequest(String url) {
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response = httpclient.execute(new HttpGet(url));
			StatusLine statusLine = response.getStatusLine();
			if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				out.close();
				return out.toString();
			} else {
				// Closes the connection.
				response.getEntity().getContent().close();
				throw new IOException(statusLine.getReasonPhrase());
			}
		} catch (Exception e) {
			throw new InfraException(e);
		}
	}
}
