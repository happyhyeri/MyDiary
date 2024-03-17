package org.edupoll.app.repository;

import java.util.List;


import org.edupoll.app.entity.Account;
import org.edupoll.app.entity.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface FolderRepository extends JpaRepository<Folder, Integer> {

	public List<Folder> findAllByAccount(Account account);
	public Folder findByIdAndAccount(Integer id, Account account );
	
	
}
