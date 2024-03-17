package org.edupoll.app.entity;


import java.time.LocalDateTime;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Memo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String title;
	private String body;
	private LocalDateTime writeAt;
	private Boolean star;
	private Boolean bin;
	private LocalDateTime deleteAt;
	
	
	@ManyToOne
	private Folder folder;
	
	
	
	
	@ManyToOne
	private Account account;
	
	

}
