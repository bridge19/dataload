package com.xy.logload.controllers;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xy.logload.domain.BaseResult;
import com.xy.logload.domain.User;
import com.xy.logload.service.FileDownloadService;

@Controller
public class UserController {

	@Autowired private FileDownloadService fileDownloadService;
	
	@ResponseBody
	@RequestMapping(value="/getFileList", method= RequestMethod.GET)
	public BaseResult<List<String>> getFileList(@RequestParam("projectName") String projectName,
												@RequestParam("fileName") String fileName,
												@RequestParam("logDate") String logDate){
//		return new BaseResult<List<String>>(true,"",fileDownloadService.getFileList(projectName, fileName, logDate),"");
		return new BaseResult<List<String>>(true,"",Arrays.asList(new String[]{"out.log"}),"");
		}
	
	@RequestMapping(value="/loadData", method= RequestMethod.GET)
	public void loadData(HttpServletRequest request, HttpServletResponse response){
		try {
			OutputStream out = response.getOutputStream();
			fileDownloadService.readFile("out.log-20180411_10.33.33.186.gz", out);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
