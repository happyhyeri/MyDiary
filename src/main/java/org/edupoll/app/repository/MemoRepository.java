package org.edupoll.app.repository;

import java.util.List;

import org.edupoll.app.entity.Account;
import org.edupoll.app.entity.Folder;
import org.edupoll.app.entity.Memo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface MemoRepository extends JpaRepository<Memo, Integer> {

	public List<Memo> findByStarAndAccountAndBin(Boolean star, Account account, Boolean bin);
	public List<Memo> findByAccountAndBin( Account account, Boolean bin);
	public List<Memo> findAllByBinAndAccount(Boolean bin,Account account);
	public List<Memo> findAllByBinAndAccountAndFolder(Boolean bin,Account account,Folder folder);
	public List<Memo> findAllByIdAndAccount(Integer id, Account account);
	public Memo findByIdAndAccount(Integer id, Account account);
	public Page<Memo> findByStarAndAccountAndBinOrderByWriteAtDesc(Boolean star, Account account, Boolean bin, Pageable pageable);
	public Page<Memo> findByAccountAndBinOrderByWriteAtDesc( Account account, Boolean bin, Pageable pageable);

}


