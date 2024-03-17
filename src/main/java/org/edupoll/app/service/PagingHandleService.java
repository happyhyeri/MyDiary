package org.edupoll.app.service;

import org.edupoll.app.entity.Account;
import org.edupoll.app.entity.Memo;
import org.edupoll.app.repository.MemoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PagingHandleService {
	private final MemoRepository memoRepository;

	public Page<Memo> getStarMemoList(int page,@AuthenticationPrincipal(expression = "account") Account account) {
		Pageable pageable = PageRequest.of(page, 5);
		// page: 조회하려는 페이지 번호 0부터 시작함.
		//5: 페이지당 항목수

		return memoRepository.findByStarAndAccountAndBinOrderByWriteAtDesc(true, account, false, pageable);
	}

	public Page<Memo> getRecentRegistMemoList(int p,@AuthenticationPrincipal(expression = "account") Account account) {
		Pageable pageable = PageRequest.of(p, 5);
		// page: 조회하려는 페이지 번호 0부터 시작함.
		//5: 페이지당 항목수

		return memoRepository.findByAccountAndBinOrderByWriteAtDesc( account, false, pageable);
	}
}
