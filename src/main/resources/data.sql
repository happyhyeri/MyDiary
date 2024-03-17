insert into Account (id, password, nickname, img)
	values ('hyeri','{bcrypt}$2a$12$Ohmgak1Go1cJrC.7b/OzVeiBOH5EDjyyKKZ9W3pOUrfmmK2K46AEa', '꿍꿍', 'default.jpg');
	
insert into folder (name, account_id)
	values('springBoot','hyeri'),
			('java','hyeri');
			

insert into Memo (title,body,star,account_id,folder_id,write_at,bin) values('나는 누구인가','이혜리~',0,'hyeri',1,localtime(),0);
insert into Memo (title,body,star,account_id,folder_id,write_at,bin) values('아뇽','여기 처음써봐',1,'hyeri',1,localtime(),0);
insert into Memo (title,body,star,account_id,folder_id,write_at,bin) values('나 자바공부해','이혜리~',1,'hyeri',2,localtime(),1);
insert into Memo (title,body,star,account_id,folder_id,write_at,bin) values('첫번째 게시글','이혜리~',1,'hyeri',2,localtime(),0);
insert into Memo (title,body,star,account_id,folder_id,write_at,bin) values('두번째 게시글','이혜리~',0,'hyeri',2,localtime(),0);
insert into Memo (title,body,star,account_id,folder_id,write_at,bin) values('세번째 게시글','이혜리~',1,'hyeri',2,localtime(),0);
insert into Memo (title,body,star,account_id,folder_id,write_at,bin) values('네번째 게시글','이혜리~',0,'hyeri',2,localtime(),1);
insert into Memo (title,body,star,account_id,folder_id,write_at,bin) values('중요메모1','이혜리~',1,'hyeri',2,localtime(),0);
insert into Memo (title,body,star,account_id,folder_id,write_at,bin) values('중요메모2','이혜리~',1,'hyeri',2,localtime(),0);
insert into Memo (title,body,star,account_id,folder_id,write_at,bin) values('중요메모3','이혜리~',1,'hyeri',2,localtime(),0);
insert into Memo (title,body,star,account_id,folder_id,write_at,bin) values('중요메모4','이혜리~',1,'hyeri',2,localtime(),0);
insert into Memo (title,body,star,account_id,folder_id,write_at,bin) values('중요메모5','이혜리~',1,'hyeri',2,localtime(),0);