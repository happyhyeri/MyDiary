package org.edupoll.app.service;

import org.edupoll.app.command.AddAccount;
import org.edupoll.app.entity.Account;
import org.edupoll.app.repository.AccountRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AccountRegisterService {
	
	private final AccountRepository accountRepository;
	
	public boolean isAvailable(AddAccount cmd) {
		if(accountRepository.findById(cmd.getNewId()).isEmpty()) {
			return true;
		}else {
			return false;
			
		}
		
	}
	
	public Account exeucte(AddAccount cmd) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String encoded = encoder.encode(cmd.getNewPassword());
			
	
		Account account = Account.builder().id(cmd.getNewId()).password("{bcrypt}"+encoded).nickname(cmd.getNewNickname()).img("default.jpg").build();
		
		return accountRepository.save(account);
		
		
	}
	

	

}
