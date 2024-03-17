package org.edupoll.app.controller;


import java.util.List;

import org.edupoll.app.command.AddFolder;
import org.edupoll.app.entity.Account;
import org.edupoll.app.entity.Folder;
import org.edupoll.app.entity.Memo;
import org.edupoll.app.repository.FolderRepository;
import org.edupoll.app.repository.MemoRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/folder")
@Controller
public class FolderController {
	private final FolderRepository folderRepository;
	private final MemoRepository memoRepository;
	
	@PostMapping("/add")
		public String proceedAddFolder(@ModelAttribute AddFolder addFolder,@AuthenticationPrincipal(expression = "account") Account account){
			
		Folder folder = Folder.builder().name(addFolder.getFolderName()).account(account).build();
		folderRepository.save(folder);
		
			
			return "redirect:/memo/write";
		}
	
	@PatchMapping("/modify")
	public String proceedModifyFolder(
			@RequestParam(required = false)Integer folderId,@RequestParam(required = false)String modifyFolderName, Model model,@AuthenticationPrincipal(expression = "account") Account account) {
			Folder folder = folderRepository.findByIdAndAccount(folderId, account);
			folder.setName(modifyFolderName);
			folderRepository.save(folder);
			
		
		return "redirect:/memo/write";                                 
	}
	
	
	@DeleteMapping("/delete")
	public String proceedDeleteFolder(@RequestParam(required = false) List<Integer> folderName,Model model,@AuthenticationPrincipal(expression = "account") Account account) {
		

		
		
		for(Integer i : folderName ) {
			Folder folder = folderRepository.findByIdAndAccount(i, account);
				
				
			List<Memo> findFolderMemos =memoRepository.findAllByBinAndAccountAndFolder(false, account, folder);
			
			for(Memo m : findFolderMemos) {
				m.setFolder(null);
				
			}
			memoRepository.saveAll(findFolderMemos);
			folderRepository.delete(folder);
		}
		

		return "redirect:/memo/write";
	}
	
	@GetMapping("/list")
	public String showList(@RequestParam(required = false) Integer folderId,@AuthenticationPrincipal(expression = "account") Account account, Model model) {
		
		
		
		List<Memo> MemoList = memoRepository.findAllByBinAndAccount(false,account);
		
		List<Memo> mList = MemoList.stream().filter(t->t.getFolder() !=null).filter(t->t.getFolder().getId().equals(folderId)).toList();
		model.addAttribute("memoList",mList);

		List<Memo> m2List = MemoList.stream().filter(t->t.getFolder() ==null).toList();
		model.addAttribute("noFolderMemoList",m2List);
			

		
		return "folder/memolist";
	}
	
	


}
