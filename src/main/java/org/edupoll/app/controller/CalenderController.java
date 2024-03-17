package org.edupoll.app.controller;

import java.time.LocalDate;
import java.util.List;

import org.edupoll.app.entity.Account;
import org.edupoll.app.entity.Memo;
import org.edupoll.app.repository.MemoRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
@RequestMapping("/calender")
@RequiredArgsConstructor
@Controller
public class CalenderController {
	
	private final MemoRepository memoRepository;
	@ModelAttribute
	public void setDefaultModel(@AuthenticationPrincipal(expression = "account") Account account, Model model) {
		if (account != null) {
			model.addAttribute("user", account);
		}
		LocalDate today = LocalDate.now();
		model.addAttribute("today", today);
		List<Memo> starList = memoRepository.findByStarAndAccountAndBin(true, account, false);
		model.addAttribute("starCnt", starList.size());
		List<Memo> all = memoRepository.findAllByBinAndAccount(false, account);
		model.addAttribute("allCnt", all.size());
		List<Memo> binSize = memoRepository.findAllByBinAndAccount(true, account);
		model.addAttribute("binCnt", binSize.size());
	}
	
	@GetMapping("/show")
	public String showCalender(){
		
		return "calender/show";
	}
	
	
}
