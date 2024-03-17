package org.edupoll.app.command;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class AddMemo {
	private String Title;
	private String body;
	private Boolean star;
	
	private  Integer folderId;
	private MultipartFile[] image;
}
