package org.edupoll.app.command;

import lombok.Data;

@Data
public class AddAccount {

	private String newId;
	private String newPassword;
	private String newNickname;
	private String newImg;
	
}
