package org.edupoll.app.controller;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.edupoll.app.command.AddMemo;

import org.edupoll.app.command.ModifyMemo;
import org.edupoll.app.data.FolderDataSet;
import org.edupoll.app.entity.Account;
import org.edupoll.app.entity.Folder;
import org.edupoll.app.entity.Image;
import org.edupoll.app.entity.Memo;
import org.edupoll.app.repository.FolderRepository;
import org.edupoll.app.repository.ImageRepository;
import org.edupoll.app.repository.MemoRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/memo")
@Controller
public class MemoController {
	private final FolderRepository folderRepository;
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

	@GetMapping("/write")
	public String showMemoWriteForm(@AuthenticationPrincipal(expression = "account") Account account, Model model) {
		if (account != null) {
			model.addAttribute("user", account);
		}

		List<Folder> folderList = folderRepository.findAllByAccount(account);
		model.addAttribute("folderList", folderList);

		List<Memo> memoList = memoRepository.findAllByBinAndAccount(false, account);
		model.addAttribute("memoCount", memoList.size());

		List<FolderDataSet> namedeFolderdatas = new ArrayList<>();

		for (Folder one : folderList) {

			FolderDataSet namedFolderdatas = new FolderDataSet();

			long count = memoList.stream().filter(t -> t.getFolder() != null).filter(t -> t.getFolder().equals(one))
					.count();
			namedFolderdatas.setFolder(one);
			namedFolderdatas.setCount(count);

			namedeFolderdatas.add(namedFolderdatas);
		}

		FolderDataSet noNamedDataset = new FolderDataSet();

		long count2 = memoList.stream().filter(t -> t.getFolder() == null).count();
		noNamedDataset.setFolder(null);
		noNamedDataset.setCount(count2);

		model.addAttribute("namedeFolderdatas", namedeFolderdatas);
		model.addAttribute("noNamedDataset", noNamedDataset);

		return "memo/write";
	}

	@PostMapping("/save")
	public String proceedWriteMemo(@ModelAttribute AddMemo addMemo,
			@AuthenticationPrincipal(expression = "account") Account account)
			throws IllegalStateException, IOException {
		if (addMemo.getFolderId() == null) {
			Memo newMemo = Memo.builder().title(addMemo.getTitle()).body(addMemo.getBody()).writeAt(LocalDateTime.now())
					.account(account).bin(false).build();

			if (addMemo.getStar() == null) {
				newMemo.setStar(false);
			} else {
				newMemo.setStar(true);
			}

			memoRepository.save(newMemo);

			if (addMemo.getImage() != null) {
				for (MultipartFile file : addMemo.getImage()) {
					if (file.isEmpty())
						continue;

					File dir = new File("d:/upload/memoImg/" + newMemo.getId());
					dir.mkdirs();
					String fileName = UUID.randomUUID().toString();
					File target = new File(dir, fileName);
					file.transferTo(target);

					Image img = Image.builder().url("static/memoImg/" + newMemo.getId() + "/" + fileName)//
							.path(target.getAbsolutePath())//
							.memo(newMemo)//
							.account(account).build();

					imageRepository.save(img);
				}
			}

			return "redirect:/memo/detail?id=" + newMemo.getId();
		}

		Folder findFolder = folderRepository.findById(addMemo.getFolderId()).get();

		Memo newMemo = Memo.builder().title(addMemo.getTitle()).body(addMemo.getBody()).writeAt(LocalDateTime.now())
				.folder(findFolder).account(account).bin(false).build();
		if (addMemo.getStar() == null) {
			newMemo.setStar(false);
		} else {
			newMemo.setStar(true);
		}

		memoRepository.save(newMemo);

		if (addMemo.getImage() != null) {
			for (MultipartFile file : addMemo.getImage()) {
				if (file.isEmpty())
					continue;

				File dir = new File("d:/upload/memoImg/" + newMemo.getId());
				dir.mkdirs();
				String fileName = UUID.randomUUID().toString();
				File target = new File(dir, fileName);
				file.transferTo(target);

				Image img = Image.builder().url("static/memoImg/" + newMemo.getId() + "/" + fileName)//
						.path(target.getAbsolutePath())//
						.memo(newMemo)//
						.account(account).build();

				imageRepository.save(img);
			}
		}
		return "redirect:/memo/detail?id=" + newMemo.getId();
	}

	@GetMapping("/all")
	public String showAllMemoList(@AuthenticationPrincipal(expression = "account") Account account, Model model) {

		List<Memo> all = memoRepository.findAllByBinAndAccount(false, account);
		model.addAttribute("all", all);
		model.addAttribute("allCnt", all.size());

		List<Folder> folder = folderRepository.findAllByAccount(account);
		model.addAttribute("folder", folder);

		model.addAttribute("null", null);
		return "memo/showAll";
	}

	@GetMapping("/folder")
	public String ShowAllByFolderMemo(@AuthenticationPrincipal(expression = "account") Account account, Model model,
			@RequestParam(required = false) Integer folderId) {
		List<Folder> f = folderRepository.findAllByAccount(account);
		model.addAttribute("folder", f);

		if (folderId == null) {
			List<Memo> defaultList = memoRepository.findAllByBinAndAccountAndFolder(false, account, null);
			model.addAttribute("all", defaultList);
		}

		Folder folder = folderRepository.findByIdAndAccount(folderId, account);
		List<Memo> folderList = memoRepository.findAllByBinAndAccountAndFolder(false, account, folder);
		model.addAttribute("all", folderList);
		return "memo/showAll";
	}

	@PatchMapping("/move/bin")
	public String proceedMoveBin(@RequestParam(required = false) List<Integer> memoId,
			@AuthenticationPrincipal(expression = "account") Account account, Model model) {
		if (memoId == null) {
			return "error";
		}

		List<Memo> m = new ArrayList<>();

		List<Memo> findAll = memoRepository.findAllByBinAndAccount(false, account);

		for (Memo one : findAll) {
			for (Integer i : memoId) {
				if (one.getId().equals(i)) {
					m.add(one);
				}
			}
		}

		for (Memo one : m) {
			one.setBin(true);
			LocalDateTime now = LocalDateTime.now();

			one.setDeleteAt(now);
			memoRepository.save(one);
		}

		return "redirect:/memo/all";
	}

	@GetMapping("/modifyMemo")
	public String showModifyMemo(@RequestParam(required = false) Integer memoId,
			@AuthenticationPrincipal(expression = "account") Account account, Model model) {

		List<Folder> folderList = folderRepository.findAllByAccount(account);
		model.addAttribute("folderList", folderList);

		Memo findMemo = memoRepository.findByIdAndAccount(memoId, account);
		model.addAttribute("findMemo", findMemo);

		List<Image> findImage = imageRepository.findByAccountAndMemo(account, findMemo);
		model.addAttribute("findImage", findImage);
		return "memo/modifyMemo";
	}

	@PatchMapping("/modify")
	public String proceedModifyMemo(@RequestParam(required = false) Integer memoId,
			@ModelAttribute ModifyMemo modifyMemo, @AuthenticationPrincipal(expression = "account") Account account,
			Model model) throws IllegalStateException, IOException {
		Memo findMemo = memoRepository.findByIdAndAccount(memoId, account);
		System.out.println(findMemo);
		findMemo.setTitle(modifyMemo.getTitle());
		findMemo.setBody(modifyMemo.getBody());
		findMemo.setWriteAt(LocalDateTime.now());
		findMemo.setBin(false);

		if (findMemo.getFolder() == null && modifyMemo.getFolderId() != null) {
			Folder findFolder = folderRepository.findById(modifyMemo.getFolderId()).get();
			findMemo.setFolder(findFolder);
		} else if (findMemo.getFolder() != null && modifyMemo.getFolderId() == null) {
			findMemo.setFolder(null);
		}

		if (modifyMemo.getStar() == null) {
			findMemo.setStar(false);
		} else {
			findMemo.setStar(true);
		}

		memoRepository.save(findMemo);

		// 이미지 삭제가 있다면
		if (modifyMemo.getDeleteImgId() != null) {

			List<Image> findImage = imageRepository.findByAccountAndMemo(account, findMemo);

			List<Integer> existIds = new ArrayList<>();
			for (Image one : findImage) {
				existIds.add(one.getImageId());
			}

			for (Integer i : existIds) {
				for (Integer ii : modifyMemo.getDeleteImgId()) {
					if (i.equals(ii)) {
						imageRepository.deleteAll(imageRepository.findByImageIdAndAccountAndMemo(i, account, findMemo));
					}
				}

			}
		}

			for (MultipartFile file : modifyMemo.getImage()) {
				if (file.isEmpty())
					continue;

				File dir = new File("d:/upload/memoImg/" + findMemo.getId());
				dir.mkdirs();
				String fileName = UUID.randomUUID().toString();
				File target = new File(dir, fileName);
				file.transferTo(target);

				Image img = Image.builder().url("static/memoImg/" + findMemo.getId() + "/" + fileName)//
						.path(target.getAbsolutePath())//
						.memo(findMemo)//
						.account(account).build();

				imageRepository.save(img);
			}
		

		return "redirect:/memo/detail?id=" + findMemo.getId();
	}

	@GetMapping("/star")
	public String showStarMemoList(@AuthenticationPrincipal(expression = "account") Account account, Model model) {
		List<Memo> all = memoRepository.findAllByBinAndAccount(false, account);
		model.addAttribute("all", all);

		List<Memo> starList = memoRepository.findByStarAndAccountAndBin(true, account, false);
		model.addAttribute("starList", starList);

		List<Folder> folder = folderRepository.findAllByAccount(account);
		model.addAttribute("folder", folder);

		return "memo/starList";
	}

	@GetMapping("/detail")
	public String showDetailMemo(@AuthenticationPrincipal(expression = "account") Account account,
			@RequestParam(required = false) Integer id, Model model) {

		Memo memo = memoRepository.findByIdAndAccount(id, account);
		List<Image> img = imageRepository.findByAccountAndMemo(account, memo);
		if (img != null) {

			model.addAttribute("img", img);
		}

		model.addAttribute("memo", memo);

		return "memo/detail";
	}

}
