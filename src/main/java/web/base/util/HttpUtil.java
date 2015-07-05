package web.base.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class HttpUtil {

	public static final int TIMEOUT = 180000;
	
	public static String doGet(String urlPath, Map<String, String> paramMap) throws IOException{
		return doGet(urlPath, paramMap, "UTF-8");
	}
	public static String doGet(String urlPath, Map<String, String> paramMap, String charset) throws IOException{
		String responseText = null;
		HttpURLConnection conn = null;
		
		try{
			if(AssertUtil.notEmpty(paramMap)){
				if(urlPath.indexOf('?') == -1){
					urlPath += "?" + buildParamter(paramMap);
				}else{
					urlPath += "&" + buildParamter(paramMap);
				}
			}
			URL url = new URL(urlPath);
			conn = (HttpURLConnection)url.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setConnectTimeout(TIMEOUT);
			conn.setReadTimeout(TIMEOUT);
			conn.setRequestMethod("GET");
			conn.connect();
			
			responseText = readResponse(conn, charset);
		}finally{
			if(conn != null)
				conn.disconnect();
		}
		
		return responseText;
	}
	
	public static String doPost(String urlPath, Map<String, String> paramMap) throws IOException{
		return doPost(urlPath, paramMap, "UTF-8");
	}
	public static String doPost(String urlPath, Map<String, String> paramMap, String charset) throws IOException{
		
		return doPost(urlPath, "application/x-www-form-urlencoded", buildParamter(paramMap), charset);
	}
	
	public static String doPost(String urlPath, String contentType, String body, String charset) throws IOException{
		
		return doPost(urlPath, contentType, body.getBytes(charset), charset);
	}
	
	public static String doPost(String urlPath, String contentType, byte[] body, String charset) throws IOException{
		String responseText = null;
		HttpURLConnection conn = null;
		
		try{
			URL url = new URL(urlPath);
			conn = (HttpURLConnection)url.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setConnectTimeout(TIMEOUT);
			conn.setReadTimeout(TIMEOUT);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", contentType);
			conn.connect();
			
			conn.getOutputStream().write(body);
			conn.getOutputStream().flush();
			responseText = readResponse(conn, charset);
		}finally{
			if(conn != null)
				conn.disconnect();
		}
		
		return responseText;
	}
	
	private static String readResponse(HttpURLConnection conn, String charset) throws IOException{
		InputStream is = conn.getErrorStream();
		if(is == null){
			is = conn.getInputStream();
		}
		
		return readInputStream(is, charset);
	}
	private static String readInputStream(InputStream is, String charset)throws IOException{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int data = 0;
		while((data = is.read()) != -1){
			baos.write(data);
		}
		
		return new String(baos.toByteArray(), charset);
	}
	
	private static String buildParamter(Map<String, String> paramMap){
		StringBuilder sb = new StringBuilder();
		if(AssertUtil.notEmpty(paramMap)){
			for(Map.Entry<String, String> entry : paramMap.entrySet()){
				sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
			}
		}
		sb = StringUtil.compareAndDeleteLastChar(sb, '&');
		return sb.toString();
	}
	
}
