# 인증 서버
## 역할
|서비스|역할|
|---|---|
|인증 서버|- 인증 서비스를 제공하기 위해 Spring Security 활용 <br>- Spring MVC를 활용
## 기술 스택
- Java 11
- Springboot 3.0.1
- Spring MVC
- JPA
- H2Database
## 제공 기능
|기능|설명|
|---|---|
|이메일 인증 | 기본적인 인증 기능 |
|웹 어플리케이션 <br> 로그인/회원가입 <br> 회원/회원리스트 조회 | H2Database와 Thymeleaf를 활용한 회원 관리 기능|
|Spring Security | 회원 로그인에 Spring Security 를 활용
### 이메일 인증
- EmailConfig 클래스에서 mail server 설정을 세팅해줍니다.
- EmailServiceImp 클래스에서 메일 내용과 인증 기능을 수행합니다.
### 회원가입
- @Valid, errorCheck 메소드를 통해 입력에 문제가 없는지 확인합니다.
- MVC, Thymeleaf를 활용해 회원 가입 메소드를 구현합니다.
- 만들고자 하였던 검증 시스템 :
    1) 타입 검증 : 이름에 숫자가 들어가면 오류 처리
    2) 필드 검증
     -> 이름에 공백 x
     -> 필수 입력 값 존재
     -> 이름 최대 입력 수 : 12
     -> 휴대폰 번호에 “-” 금지
     -> 휴대폰 번호 자리수는 11자리
     -> 비밀번호 골뱅이, 영문자, 숫자 조합 확인
     -> 비밀번호 확인, 재확인 문자가 같은지 확인
    3) 검증 실패시 다른 페이지로 넘어가는 것이 아닌 데이터 유지한 상태로 알림
### Spring Security
- Security Config 에 Spring Security 에 대한 코드를 작성
- login 컨트롤러와 연동
<br/>
<br/>
<br/>

## 프로젝트 진행 중 이슈
<br/>
<br/>

## ⭐️ Spring MVC
<br/>
<br/>
<br/>

### 1. 데이터 중복 저장 문제
- 로직흐름: 상품 목록 -> 상품 등록 폼 -> 상품 저장 -> 상품 목록
- 새로 고침 : 마지막에 했던 행위를 다시 하는 것
- 상품 목록을 새로 고침하면 마지막에 서버에 전송한 데이터를 다시 전송하므로
  의미없는 데이터가 계속 저장됨
- 해결 방법 : 리다이렉트를 사용해서 url을 상품 상세페이지로 보내버리는 방법
    -> 새로고침하면 상품상세페이지를 다시 요청하게 되는 것 ( GET )
- ex. return “redirect:/member/memberList/” + member.getId();
<br/>
-정리: 데이터를 저장하는 메서드에는 이러한 리다이렉트 작업이 필요함<br/>
 1) get : 회원가입 페이지 호출       <br/>
 2) form action, button submit 을 통해 입력 폼 만들고 post 로 전송     <br/> 
 3) post : 회원가입 페이지에 post 로 데이터가 들어오면 로직 실행      <br/>
 4) 레포지토리, 모델에 데이터 저장      <br/>
 5) 리다이렉트를 통해 /member/member.getId() 전송      <br/>
 6) get : /member/{memeberId} 로 저장 회원 출력 페이지 호출    <br/>  
 7) 레포지토리에서 findById(memeberId) 를 통해 멤버 찾기   <br/>   
 8) 모델에 저장      <br/>
 9) /memeber/checkMember.html 호출     
<br/>
<br/>

### 2. 버튼을 submit type 으로 해야만 정상작동이 확인되는데, 이러면 페이지가 새로고침되기 때문에 입력한 데이터가 날라감
- button type = submit 대신 iframe 와 input, form 태그를 적절히 활용해 해결 완료
<br/>
<br/>

### 3. 이메일 인증 -> 이메일 검증, 회원가입 진행, 이메일이 비어있다는 에러 발생
- 서로 다른 폼태그와 버튼을 통해 입력값을 받다보니 서로 다른 메서드에서 모델에 데이터를 담음
- 이메일 전역 변수를 만들어서 거기에 담는 형태로 구성
<br/>
<br/>

### 4. 오류가 발생하는 경우 다시 입력 폼으로 돌아옴, 그런데 사용자 입력 데이터가 초기화됨, 입력 데이터를 그냥 놔두는 방법에 대한 문제
- `<input th:field=“*{password}“>` 와 같이 도메인 클래스 멤버 변수를 안에 넣어준다.
- 오류났을 때 다시 불러오는 페이지가 /join 이라면, 컨트롤러에서 해당 주소 불러오는 메서드에서
  model.addAttribute(“member”,new Member());
  return “/member/join”; 을 해준다
- join 페이지에서 다시 member 에 해당하는 데이터를 받아와 출력한다.
- 이를 통해, 오류가 나더라도 입력되어 있던 데이터르 다시 보여준다.
<br/>
<br/>
<br/>

## ⭐️ Spring Security
<br/>
<br/>

### 1. CSRF 문제
    http.loginPage("/member/login") 를 통해 커스텀 로그인 페이지를 만들었는데,
    성공했을 때 POST 로 login-do 로 들어가야하는데 계속 GET 으로 login 만 접속한다.
- 해결 방법 : .csrf().disable(); 옵션 추가

<br/>
<br/>
<br/>


## 참고
- spring MVC : 인프런 '김영한 Spring MVC'
- Spring Security : 인프런 'Spring Boot 기반으로 개발하는 Spring Security'
- JWT : 인프런 'Spring Boot JWT Tutorial'
- JPA : 인프런 '김영한 Spring JPA'
- WEB : 부트스트랩 '로그인/회원가입 페이지'
- HTTP : 인프런 '김영한 HTTP API'


## 코드리뷰
- 컨트롤러에 로그인/회원가입 등 많은 코드를 작성하였는데, 컨트롤러 또한 세부적으로 분리해야되는지
- 컨트롤러 안에 회원가입 검증 메소드를 작성하는게 아닌, 따로 서비스에 클래스를 만들어 분리시키는게 나은지
- JWT토큰을 로그인api를 만들어 발급하는 것으로 알고 있는데, 이 토큰은 회원가입할 때 DB에 따로 저장안해도 되는지
- 프론트 개발자들은 리액트와 같은 프레임워크를 사용하는데, 백엔드에서 타임리프를 사용하는게 맞는지가 궁금합니다.

- * JWT 토큰은 아직 학습 미숙으로 스스로 진행해보면서 다시 질문하도록 하겠습니다.



