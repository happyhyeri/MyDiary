package org.edupoll.app.controller;

import java.time.LocalDate;
import java.util.List;

import org.edupoll.app.entity.Account;
import org.edupoll.app.entity.Memo;
import org.edupoll.app.repository.MemoRepository;
import org.edupoll.app.service.PagingHandleService;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class IndexController {
	
	private final MemoRepository memoRepository;
	private final PagingHandleService pagingHandleService;
	
	@GetMapping({"/index","/"})
	public String showIndex(@AuthenticationPrincipal(expression = "account") Account account,@RequestParam(value="page", defaultValue="1") int page, @RequestParam(value="p", defaultValue="1") int p, Model model) {
	
		if (account != null) {
			model.addAttribute("user",account);
		}
		
		LocalDate today = LocalDate.now();
		model.addAttribute("today",today);
		
		List<Memo> starList = memoRepository.findByStarAndAccountAndBin(true,account,false);	
		model.addAttribute("starList",starList);
		
		model.addAttribute("starCnt",starList.size());
		
		
		
		List<Memo> all = memoRepository.findAllByBinAndAccount(false,account);
		model.addAttribute("allCnt",all.size());
		
		List<Memo> binSize = memoRepository.findAllByBinAndAccount(true,account);
		model.addAttribute("binCnt",binSize.size());
		
		//페이징 처리 부분 (중요메모)
		Page<Memo> pageStarList = pagingHandleService.getStarMemoList(page-1, account);
		model.addAttribute("pageStarList",pageStarList);
	
		//현재 페이지 번호
		int starCurrentPage =  pageStarList.getNumber();
		model.addAttribute("starCurrentPage",starCurrentPage+1);
		
		int starTotalPage = pageStarList.getTotalPages();
		model.addAttribute("starTotalPage",starTotalPage);
		
		//페이징 처리부분 (최신등록순)
		Page<Memo> pageRecentRegistList = pagingHandleService.getRecentRegistMemoList(p-1, account);
		model.addAttribute("pageRecentRegistList",pageRecentRegistList);	
		
		int recentCurrentPage =  pageRecentRegistList.getNumber();
		model.addAttribute("recentCurrentPage",recentCurrentPage+1);	
		
		int recentTotalPage = pageRecentRegistList.getTotalPages();
		model.addAttribute("recentTotalPage",recentTotalPage);	
		
		return "index";
		
		
		
	}
	

}
