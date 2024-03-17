package org.edupoll.app.command;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class ModifyMemo {
	
	private String Title;
	private String body;
	private Boolean star;
	
	private  Integer folderId;
	private MultipartFile[] image;
	private List<Integer> deleteImgId;
}
