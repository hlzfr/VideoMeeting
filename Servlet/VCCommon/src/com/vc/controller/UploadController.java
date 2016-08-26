package com.vc.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.vc.entity.Result;

@Controller
@RequestMapping("/file")
public class UploadController {
	
	@RequestMapping(value="/upload", method = RequestMethod.POST)
	@ResponseBody
	public Result<String> upload(HttpServletRequest request,
			HttpServletResponse response, @RequestParam(value="file") MultipartFile file) throws IllegalStateException,
			IOException {
		if(file==null) {
			System.out.println("file is null");
		}
		if (file !=null && !file.isEmpty()) {
	        try {
	            byte[] bytes = file.getBytes();
	            String filePath = System.currentTimeMillis()+"_"+file.getOriginalFilename();
	            // 定义上传路径，不放在临时目录里
	            File savedFile = new File("E:/apache-tomcat-8.0.27/webapps/file/"+filePath);
	            BufferedOutputStream stream =
	                    new BufferedOutputStream(new FileOutputStream(savedFile));
	            stream.write(bytes);
	            stream.close();
//	            photo.setUri(filePath);
//	            monumentsRepo.updatePhoto(photo);
	            return new Result<String>(true, "success", filePath);
	        } catch (Exception e) {
	        	System.out.println("Exception e "+e.getMessage());
	            return new Result<String>(false, e.getMessage(), "");
	        }
	    }
		System.out.println("file is empty");
		return new Result<String>(false, "file is empty", "");
	}

	
}
