package org.edupoll.app.repository;

import java.util.List;

import org.edupoll.app.entity.Account;
import org.edupoll.app.entity.Image;
import org.edupoll.app.entity.Memo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {

	
	List<Image> findByImageIdAndAccountAndMemo(Integer imageId,Account account, Memo memo);
	List<Image> findByAccountAndMemo(Account account, Memo memo);
}
