package org.edupoll.app.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.edupoll.app.entity.Account;
import org.edupoll.app.entity.Image;
import org.edupoll.app.entity.Memo;
import org.edupoll.app.repository.ImageRepository;
import org.edupoll.app.repository.MemoRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class BinController {

	private final MemoRepository memoRepository;
	private final ImageRepository imageRepository;

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

	@GetMapping("/bin")
	public String showBin(@AuthenticationPrincipal(expression = "account") Account account, Model model) {
		List<Memo> binAll = memoRepository.findAllByBinAndAccount(true, account);
		model.addAttribute("binAll", binAll);
		return "memo/bin";
	}

	@DeleteMapping("/bin")
	public String proceedDelete(@RequestParam(required = false) List<Integer> memoId,
			@AuthenticationPrincipal(expression = "account") Account account, Model model) {
		if (memoId == null) {
			return "error";
		}

		for (Integer i : memoId) {
			Memo memo = memoRepository.findByIdAndAccount(i, account);

			List<Image> img = imageRepository.findByAccountAndMemo(account, memo);
			imageRepository.deleteAll(img);
		}

		List<Memo> m = new ArrayList<>();

		List<Memo> findAll = memoRepository.findAllByBinAndAccount(true, account);

		for (Memo one : findAll) {
			for (Integer i : memoId) {
				if (one.getId().equals(i)) {
					m.add(one);
				}
			}
		}

		for (Memo one : m) {
			memoRepository.delete(one);
		}
		return "redirect:/bin";
	}

	@PatchMapping("/bin")
	public String proceedRollback(@RequestParam(required = false) List<Integer> memoId,
			@AuthenticationPrincipal(expression = "account") Account account, Model model) {
		if (memoId == null) {
			return "error";
		}

		List<Memo> m = new ArrayList<>();

		List<Memo> findAll = memoRepository.findAllByBinAndAccount(true, account);

		for (Memo one : findAll) {
			for (Integer i : memoId) {
				if (one.getId().equals(i)) {
					m.add(one);
				}
			}
		}

		for (Memo one : m) {
			one.setBin(false);
			memoRepository.save(one);
		}

		return "redirect:/bin";
	}

}
