package com.xy.service;

import java.io.OutputStream;
import java.util.List;

public interface FileDownloadService {

	public List<String> getFileList(String project,String fileName,String date);
	public void readFile(String fileName,OutputStream out);
	
	public void readRemoteFile(String fileName);
}
