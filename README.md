## MyDiary 

### 💡만들면서 했던 고민에 대한 해결책
-----

**1. BCryptPasswordEncoder 비밀번호 일치여부**
```
 //암호화할때
 BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
 String encoded = encoder.encode(cmd.getNewPassword());
 
 Account account = Account.builder().id(cmd.getNewId()).password("{bcrypt}" + encoded).nickname(cmd.getNewNickname()).img("default.jpg").build();

  //복호화할때
 BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
** if (encoder.matches(checkpw.getPassword(), account.getPassword().replace("{bcrypt}", ""))) { **
     return "redirect:/diary/index";
 }

  //단순하게 {bcrypt}를 제거하여 encoder.matches()를 해주면 됐다.
```

**2.한 페이지 내에서 2개의 페이징처리 (새로고침 없이)**
```
<li th:each="p: ${#numbers.sequence(1, recentTotalPage)}" 
									class="page-item"
									th:classappend="${p == recentCurrentPage} ? 'active'"><a 
									th:text="${p}" class="page-link" 
									**th:href="@{/index?(p=${p},page=${starCurrentPage})}"></a></li>**

//파라미터 값을 P를 기준으로 페이지가 변결될때마다 P로 잡아놓고 또 다른 하나의 페이지처리인 Page는 전에 잡아놨던 현재 페이지를 디폴트로 잡아놓으면 됐었다
```

**3.코드의 중복**
<br>
나는 그동안 각각의 페이지마다 @getMapping으로 데이터를 세팅해줘 반복되는 코드가 너무 많았는데
이번에 컨트롤러 위에 **@modelAtrribute**로 한번만 세팅을 해주면 된다는 점을 배웠다.

```
@ModelAttribute
public void setDefaultModel(@AuthenticationPrincipal(expression = "account") Account account, Model model) {
    if (account != null) {
        model.addAttribute("user", account);
    }
    LocalDate today = LocalDate.now();
    model.addAttribute("today", today);
    List < Memo > starList = memoRepository.findByStarAndAccountAndBin(true, account, false);
    model.addAttribute("starCnt", starList.size());
    List < Memo > all = memoRepository.findAllByBinAndAccount(false, account);
    model.addAttribute("allCnt", all.size());
    List < Memo > binSize = memoRepository.findAllByBinAndAccount(true, account);
    model.addAttribute("binCnt", binSize.size());
}
```

### 📄프로젝트를 진행하면서 메모해놓고 싶은 코드
-----
1.JSP로 페이징 처리하기 
<https://hyericodingnote.tistory.com/19>

