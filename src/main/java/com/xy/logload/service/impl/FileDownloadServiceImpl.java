package com.xy.logload.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import com.xy.logload.service.FileDownloadService;
import com.xy.logload.util.HttpClientPool;

@Service("fileDownloadService")
public class FileDownloadServiceImpl implements FileDownloadService {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileDownloadServiceImpl.class);
	@Autowired private HttpClientPool httpClientPool;
	
	private SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public List<String> getFileList(String project,String fileName,String date){
//		HttpGet get = new HttpGet("http://10.33.119.31/cgi/downlog?warname=lujingGis&logname=out.log&date=20180411");
		List<String> result = new ArrayList<>();
		HttpGet get = new HttpGet("http://10.33.119.31/logs_sc/20180411/tradePush");
		CloseableHttpResponse  response = null;
	    try {
	        response = (CloseableHttpResponse )httpClientPool.getHttpClient().execute(get);
	        if(response.getStatusLine().getStatusCode()==200){
	            HttpEntity entity = response.getEntity();
	            String entityString = EntityUtils.toString(entity, "utf-8");
	            EntityUtils.consume(entity);
	            Pattern pattern = Pattern.compile("<a href=\"(out.log[-_gz0-9.]*)\">");
	            Matcher matcher = pattern.matcher(entityString);
	            while (matcher.find()) 
	            {
	            	result.add(matcher.group(1));  // 打印所有 
	                   
	            }     
	        }
	    } catch (RuntimeException | IOException e) {
	    	LOGGER.warn("query error: ", e);
	    } finally {
	        try {
	            if (response != null)
	                response.close();
	        } catch (IOException e) {
	        	LOGGER.warn("query Location, close conn error, moblie: ", e);
	        }
	    }
	    return result;
	}
	public boolean downloadFile(String fileName){
        HttpGet httpget = new HttpGet("http://10.33.119.31/logs_sc/20180411/tradePush/"+fileName);
		CloseableHttpResponse  response = null;
        try {
        	response = (CloseableHttpResponse )httpClientPool.getHttpClient().execute(httpget);
        	if(response.getStatusLine().getStatusCode()==200){
        		HttpEntity entity = response.getEntity();
        		File desc = new File("c:\\downloadfile\\"+fileName);
        		try (InputStream is = entity.getContent(); //
                        OutputStream os = new FileOutputStream(desc)) {
                    StreamUtils.copy(is, os);
                }catch(Throwable e){
                	LOGGER.error("文件下载失败......", e);
                    return false;
                }
        	}
        }catch (RuntimeException | IOException e) {
	    	LOGGER.warn("query error: ", e);
	    } finally {
	        try {
	            if (response != null)
	                response.close();
	        } catch (IOException e) {
	        	LOGGER.warn("query Location, close conn error, moblie: ", e);
	        }
	    }
        return true;
	}
	
	public void readRemoteFile(String fileName){
		
		HttpGet httpget = new HttpGet("http://10.33.119.31/logs_sc/20180411/tradePush/"+fileName);
		CloseableHttpResponse  response = null;
		
		String fromDateStr = "2018-04-11 02:00:00";
		String toDateStr = "2018-04-11 03:00:00";
		String normalLog = fromDateStr.substring(0, 10);
		Date fromDate = null;
		Date toDate = null;
		try {
			fromDate = formater.parse(fromDateStr);
			toDate = formater.parse(toDateStr);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return;
		}
		boolean start = false;
        try {
        	response = (CloseableHttpResponse )httpClientPool.getHttpClient().execute(httpget);
        	if(response.getStatusLine().getStatusCode()==200){
        		HttpEntity entity = response.getEntity();
        		File desc = new File("c:\\downloadfile\\"+fileName);
        		try (InputStream is = new GZIPInputStream(entity.getContent())) {
        			 Scanner sc=new Scanner(is);  
        		        List<String> lines=new ArrayList<>();  
        		        while(sc.hasNextLine()){  
        		        	String line = sc.nextLine();
        		        	int index = line.indexOf(normalLog);
        		            if(index==-1 || index>4){
        		            	if(start){
        		        			System.out.println(line);
        			        	}
        		            	continue;
        		            }
        		        	String dateStr = line.substring(index,20);
        		        	try{
        		        		Date date = formater.parse(dateStr);
        		        		if(!start && date.before(fromDate)){
        		        			continue;
        		        		}else{
        		        			start=true;
        		        		}
        			        	if(date.after(toDate))
        			        		break;
        			        	if(start){
        		        			System.out.println(line);
        			        	}
        		        	}catch(ParseException e){
        		        		if(start){
        		        			System.out.println(line);
        		        		}
        			        	continue;
        		        	}
        		        	
        		        }  
                }catch(Throwable e){
                	LOGGER.error("文件下载失败......", e);
                }
        	}
        }catch (RuntimeException | IOException e) {
	    	LOGGER.warn("query error: ", e);
	    } finally {
	        try {
	            if (response != null)
	                response.close();
	        } catch (IOException e) {
	        	LOGGER.warn("query Location, close conn error, moblie: ", e);
	        }
	    }
		
	}
	
	public void readFile(String fileName,OutputStream out){
		String fromDateStr = "2018-04-11 02:00:00";
		String toDateStr = "2018-04-11 03:00:00";
		String normalLog = fromDateStr.substring(0, 10);
		Date fromDate = null;
		Date toDate = null;
		try {
			fromDate = formater.parse(fromDateStr);
			toDate = formater.parse(toDateStr);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return;
		}
		InputStream in;
		boolean start = false;
		try {
			in = new GZIPInputStream(new FileInputStream("c:\\downloadfile\\"+fileName));
	        Scanner sc=new Scanner(in);  
	        List<String> lines=new ArrayList<>();  
	        while(sc.hasNextLine()){  
	        	String line = sc.nextLine();
	        	int index = line.indexOf(normalLog);
	            if(index==-1 || index>4){
	            	if(start){
	        			out.write(line.getBytes());
	        			out.write("<br>".getBytes());
		        	}
	            	continue;
	            }
	        	String dateStr = line.substring(index,20);
	        	try{
	        		Date date = formater.parse(dateStr);
	        		if(!start && date.before(fromDate)){
	        			continue;
	        		}else{
	        			start=true;
	        		}
		        	if(date.after(toDate))
		        		break;
		        	if(start){
	        			out.write(line.getBytes());
	        			out.write("<br>".getBytes());
		        	}
	        	}catch(ParseException e){
	        		if(start){
	        			out.write(line.getBytes());
	        			out.write("<br>".getBytes());
	        		}
		        	continue;
	        	}
	        	
	        }  
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	}
}
