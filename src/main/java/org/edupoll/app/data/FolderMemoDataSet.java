package org.edupoll.app.data;

import java.util.List;


import org.edupoll.app.entity.Memo;

import lombok.Data;

@Data
public class FolderMemoDataSet {

	private Integer folderId;
	
	private List<Memo> Memos;
}
