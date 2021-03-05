//package com.magiplatform.dorahack.utils;
//
//import com.alibaba.fastjson.JSONObject;
//import org.apache.http.HttpStatus;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.params.HttpClientParams;
//import org.checkerframework.common.reflection.qual.GetMethod;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.codec.multipart.FilePart;
//
//import java.io.*;
//import java.util.Iterator;
//import java.util.Map;
//import java.util.Set;
//
///**
// * @version V1.0
// * @Title: ChannelUserApiController.java
// * @Package com.magiplatform.dorahack.utils;
// * @Description: UploadUtil
// * @date 2020/8/26 10:53
// */
//public class UploadUtil {
//		protected final static Logger log = LoggerFactory.getLogger(UploadUtil.class);
//
//	    private static final int sotimeout = 5000;
//	    private static final int connectTimeout = 1000;
//	    private static final String ENCODING_UTF_8 = "UTF-8";
//		/**
//	     * 返回一个http request 的responseString
//	     *
//	     *
//	     * @param url
//	     * @param parameters 要post的parameters
//	     *
//	     * @return string
//	     * @throws Exception
//	     */
//	    public static String postRequest(String url, Map<String, String> parameters) throws Exception {
//	        return postRequest(url, parameters, 0,null,null,"GBK");
//	    }
//
//	    public static String postRequest(String url, Map<String, String> parameters, Map<String,String> requestHeadMap, String body, String encoding) throws Exception {
//	        return postRequest(url, parameters, 0,requestHeadMap,body,encoding);
//	    }
//
//	    public static String postRequest(String url, Map<String, String> parameters, int timeoutmiliseconds, Map<String,String> requestHeadMap, String body, String encoding) throws Exception {
//
//	        HttpClient client = new HttpClient();
//
//	        if(timeoutmiliseconds>0){
//	            HttpClientParams params = new HttpClientParams();
//	            params.setSoTimeout(timeoutmiliseconds);
//	            client.setParams(params);
//	        }
//
//	        client.getParams().setContentCharset(encoding);
//
//	        PostMethod post = null;
//	        post = new PostMethod(url);
//
//	        if(requestHeadMap!=null){
//	        	Set<Map.Entry<String, String>> set = requestHeadMap.entrySet();
//	        	for (Iterator<Map.Entry<String, String>> it = set.iterator(); it.hasNext();) {
//	                Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
//	                post.addRequestHeader(entry.getKey(),entry.getValue());
//	            }
//	        }
//
//	        if(ENCODING_UTF_8.equalsIgnoreCase(encoding)){
//	        	post.addRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
//	        }else{
//	        	post.addRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=GBK");
//	        }
//	        if(StringUtils.isNotBlank(body)){
//	        	post.setRequestBody(body);
//	        }
//	        try{
//	            // 处理提交parameters
//	            Set<String> parameterkeySet = parameters.keySet();
//	            Iterator<String> parameterkeySetNames = parameterkeySet.iterator();
//	            while (parameterkeySetNames.hasNext()) {
//	                String parameterkey = parameterkeySetNames.next();
//	                //System.out.println(parameterkey);
//	                if(parameterkey!=null){
//	                	String parametervalue=parameters.get(parameterkey);
//	                	if(parametervalue!=null){
//	                		post.setParameter(parameterkey, parametervalue);
//	                		//System.out.println(parameterkey);
//	                	}
//	                }
//	            }
//
//	            int result = client.executeMethod(post);
//
//	            if (result == HttpStatus.SC_OK) {
//
//	                InputStream inputStream = post.getResponseBodyAsStream();
//	                return getResponseAsString(inputStream, encoding);
//	            } else {
//	                return null;
//	            }
//	        }catch(Exception e){
//	            throw e;
//	        }finally{
//	            post.releaseConnection();
//	        }
//	    }
//
//	    /**
//	     * use get method to return http request response String
//	     *
//	     * @param url
//	     * @return String
//	     * @throws Exception
//	     */
//	    public static String getRequest(String url){
//	    	if(url==null || "".equals(url)) return "";
//
//	        HttpClient client = new HttpClient();
//
//	        client.getParams().setContentCharset("GBK");
//
//	        GetMethod method = new GetMethod(url);
//	        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,new DefaultHttpMethodRetryHandler());
//	        method.addRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=GBK");
//	        method.setRequestHeader("Connection", "close");
//	        //method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));
//
//	        String retstring=null;
//	        try {
//	            int result = client.executeMethod(method);
//	            if (result == HttpStatus.SC_OK) {
//	            	//byte[] responseBody = method.getResponseBody();
//	            	// Deal with the response.
//	            	// Use caution: ensure correct character encoding and is not binary data
//	            	//retstring= new String(responseBody,"UTF-8");
//	                retstring = method.getResponseBodyAsString();
//
//
//	            }
//	        } catch (IOException e) {
//	            log.info("IOException :" +e.getMessage());
//	        } finally {
//	        	method.releaseConnection();
//	        }
//	        return retstring;
//	    }
//
//	    public static String getRequestWithTimeout(String url, Map<String,String> requestHeadMap, String encoding, int connTime, int soTime){
//	    	if(url==null || "".equals(url)) return "";
//
//	        HttpClient client = new HttpClient();
//	        if(connTime==0){
//	        	connTime=connectTimeout;
//	        }
//	        if(soTime==0){
//	        	soTime=sotimeout;
//	        }
//	        client.getHttpConnectionManager().getParams().setConnectionTimeout(connTime);
//	        client.getHttpConnectionManager().getParams().setSoTimeout(soTime);
//
//	        client.getParams().setContentCharset(encoding);
//
//	        GetMethod method = new GetMethod(url);
//	        method.addRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=GBK");
//	        method.addRequestHeader("Connection", "close");
//	        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));
//
//	        if(requestHeadMap!=null){
//	        	Set<Map.Entry<String, String>> set = requestHeadMap.entrySet();
//	        	for (Iterator<Map.Entry<String, String>> it = set.iterator(); it.hasNext();) {
//	                Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
//	                method.addRequestHeader(entry.getKey(),entry.getValue());
//	            }
//	        }
//
//	        String retstring=null;
//	        try {
//	            int result = client.executeMethod(method);
//
//	            if (result == HttpStatus.SC_OK) {
//	                retstring = method.getResponseBodyAsString();
//
//	                InputStream inputStream = method.getResponseBodyAsStream();
//	                retstring = getResponseAsString(inputStream, encoding);
//	            }
//	        } catch (IOException e) {
//	        	log.error(e.getMessage());
//	        	retstring="-1";
//	        } finally {
//	        	method.releaseConnection();
//	        }
//	        return retstring;
//	    }
//
//	    public static InputStream getRequestInputStream(String url, Map<String,String> requestHeadMap, String encoding, int connTime, int soTime){
//
//	    	InputStream retStream = null;
//
//	    	if(url==null || "".equals(url)) return null;
//	        HttpClient client = new HttpClient();
//	        if(connTime==0){
//	        	connTime=connectTimeout;
//	        }
//	        if(soTime==0){
//	        	soTime=sotimeout;
//	        }
//	        client.getHttpConnectionManager().getParams().setConnectionTimeout(connTime);
//	        client.getHttpConnectionManager().getParams().setSoTimeout(soTime);
//
//	        client.getParams().setContentCharset(encoding);
//
//	        GetMethod method = new GetMethod(url);
//	        if(ENCODING_UTF_8.equalsIgnoreCase(encoding)){
//	        	method.addRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
//	        }else{
//	        	method.addRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=GBK");
//	        }
//	        method.addRequestHeader("Connection", "close");
//	        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));
//
//	        if(requestHeadMap!=null){
//	        	Set<Map.Entry<String, String>> set = requestHeadMap.entrySet();
//	        	for (Iterator<Map.Entry<String, String>> it = set.iterator(); it.hasNext();) {
//	                Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
//	                method.addRequestHeader(entry.getKey(),entry.getValue());
//	            }
//	        }
//
//	        String retstring=null;
//	        try {
//	            int result = client.executeMethod(method);
//
//	            if (result == HttpStatus.SC_OK) {
//	                retStream = method.getResponseBodyAsStream();
//	            }
//	        } catch (IOException e) {
//	        	log.error(e.getMessage());
//	        	retstring="-1";
//	        }
//	        return retStream;
//	    }
//
//	    public static InputStream getRequestInputStream(String url){
//	    	if(url==null || "".equals(url)) return null;
//
//	    	GetMethod getMethod = new GetMethod(url);
//	    	HttpClient client = new HttpClient();
//	        InputStream retStream = null;
//			try {
//				int result = client.executeMethod(getMethod);
//	            if (result == HttpStatus.SC_OK) {
//	            	//byte[] responseBody = getMethod.getResponseBody();
//	            	// Deal with the response.
//	            	// Use caution: ensure correct character encoding and is not binary data
//	            	retStream = getMethod.getResponseBodyAsStream();
//	            }
//			} catch (IOException e) {
//				log.error(e);
//				retStream = null;
//			}
//
//	         return retStream;
//	    }
//
//	    public static byte[] getRequestByte(String url){
//	    	if(url==null || "".equals(url)) return null;
//
//	    	GetMethod getMethod = new GetMethod(url);
//	    	getMethod.setRequestHeader("Connection", "close");
//
//	    	HttpClient client = new HttpClient();
//
//	    	byte[] responseBody = null;
//			try {
//				int result = client.executeMethod(getMethod);
//	            if (result == HttpStatus.SC_OK) {
//	            	responseBody = getMethod.getResponseBody();
//	            	// Deal with the response.
//	            	// Use caution: ensure correct character encoding and is not binary data
//	            	//retStream = getMethod.getResponseBodyAsStream();
//	            }
//			} catch (IOException e) {
//				log.error(e);
//				responseBody = null;
//			}
//
//	         return responseBody;
//	    }
//
//	    private static String getResponseAsString(InputStream is, String encoding) throws UnsupportedEncodingException, IOException {
//
//	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//	        int i = -1;
//	        while ((i = is.read()) != -1) {
//	            baos.write(i);
//	        }
//	        return baos.toString(encoding);
//
//	    }
//
//	    public static String postMultipart(String url, File file){
//	    	String result = null;
//	    	//String url = "http://upload.dhgate.com/fileuploadservlet?functionname=desc&supplierid=402880f100f59ccd0100f59cd37d0004&imagebannername=test";
//
//	    	HttpClient client = new HttpClient();
//	    	PostMethod post = new PostMethod(url);
//	    	try{
//	    		FilePart fp =  new FilePart(file.getName(), file);
//
//	        	MultipartRequestEntity mrp =   new  MultipartRequestEntity( new  Part[] { fp} , post
//	                    .getParams());
//	        	post.setRequestEntity(mrp);
//	        	//超时设置
//	        	client.getHttpConnectionManager().getParams().setConnectionTimeout(60000);
//	        	client.getHttpConnectionManager().getParams().setSoTimeout(60000);
//
//	            int rst = client.executeMethod(post);
//
//	            if (rst == HttpStatus.SC_OK) {
//
//	                InputStream inputStream = post.getResponseBodyAsStream();
//	                result = getResponseAsString(inputStream, "UTF-8");
//	                log.info(result);
//
//	            } else {
//	            	log.warn("url: "+url +" | httpstatus: " + rst);
//	            }
//	    	}catch(Exception e){
//	    		log.error(e);
//	    	}finally{
//	            post.releaseConnection();
//	        }
//	    	return result;
//	    }
//
//	    public static void main(String[] args) {
//	    	File uploadFile = new File("/Users/otupia/Pictures/图像/logo_03.png");
//
//
//			String result = UploadUtil.postMultipart("http://upload.dhgate.com/uploadfile?functionname=jhb&userid=00000000000000000000000000000000", uploadFile);
//
//			System.out.println(JSONObject.toJSONString(result));
//		}
//
//}
