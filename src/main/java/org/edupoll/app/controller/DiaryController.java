package org.edupoll.app.controller;

import java.time.LocalDate;
import java.util.List;

import org.edupoll.app.command.CheckPW;
import org.edupoll.app.entity.Account;
import org.edupoll.app.entity.Memo;
import org.edupoll.app.repository.MemoRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
@RequestMapping("/diary")
@RequiredArgsConstructor
@Controller
public class DiaryController {
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


	@GetMapping("/pwCheck")
	public String ShowCheckPWForm() {

		return "auth/pwCheck";
	}
	
	@PostMapping("/pwCheck")
	public String proceedCheckPW(@AuthenticationPrincipal(expression = "account") Account account, @ModelAttribute CheckPW checkpw) {
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		if(encoder.matches(checkpw.getPassword(), account.getPassword().replace("{bcrypt}", ""))) {
			return "redirect:/diary/index";
		}
		return "redirect:/diary/pwCheck?error";
		
		
	
	}
	
	@GetMapping("/index")
	public String ShowDiaryIndex() {
		
		return "diary/diaryIndex";
	}
}
