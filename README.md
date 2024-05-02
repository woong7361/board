
# 개인 게시판 프로젝트

**[프로젝트 시연]**  
이 프로젝트는 Spring 과 Vue.js를 통해 만들어졌습니다.

범용적으로 가장 많이 쓰이는 개발 요소인 게시판을 구현하였고, 특색을 살리기 위해 조금의 정책사항을 부여하였습니다.

[영상 예정]

## Getting Started
<a href="http://13.125.211.168/" target="_blank">프로젝트 새창으로 열기</a>

회원 아이디: abc123   
회원 비밀번호: qwe123

관리자 아이디: a123  
관리자 비밀번호: b123

### 주요 개발 사항

<details>
    <summary> threadLocal과 Spring Interceptor를 통한 spring Security 따라잡기</summary>
    
1. Thread Local이 Thread Safe를 확인하기 위해 test 진행
   https://github.com/woong7361/board/blob/5ac16d321fcd836cf585a918006657608bbc8c0e/src/test/java/com/example/notice/auth/AuthenticationHolderTest.java#L60C1-L99C10
    - test 코드 링크 or 사진
2. Thread Local을 사용하기 위해 wrapping 저장소인 AuthenticationHolder 생성과 인증 wrapper 객체 생성
    - 링크
3. intercepter와 JWT를 사용해 인증과 인가 구현
    - 링크
4. 기존 interceptor와의 통일성을 고려해 config 에서 patter 추가
    - 링크
5. 사용하기 위해 resolveHandler를 통해 parameter 주입 사용
    - 링크
</details>

<details>
    <summary> Pyhsical File Repository와 DB File Repository의 분리 </summary>

물리적 파일과 DB 파일을 같이 다루고 있었는데 DB와는 달리 물리적 파일은 저장 위치나 저장 방법이 달라질
가능성이 크기 때문에 분리를 결정하였다.

- 구조 사진
- interface는 확장이 가능할 수 있도록 multipartfile이 아닌 byte[]로 받기로 결정하였다.

</details>

<details>
    <summary> 파일 저장시 정책사항 </summary>
</details>


### test code
test는 Spring의 단위테스트와 Controller테스트만 진행하였습니다.


![test_count.png](assets%2Ftest_count.png)

![test coverage.png](assets%2Ftest%20coverage.png)

비어있는 테스트 커버리지는 접근을 제한하고자 작성한 코드나 @SpringBootApplication 같은
단위테스트에 쓰지지 않는 코드라 무리하게 커버리지를 높이고자 작성하지 않았습니다.

<details>
    <summary> test code 진행 예시</summary>

![test_example.png](assets%2Ftest_example.png)
</details>



## 문서화
API문서화는 spring restdocs를 이용하여 진행하였습니다.

[Spring Restdocs 링크](assets%2Findex.html)

코드 문서화는 java docs를 이용하여 진행하였습니다.

class와 interface의 모든 public 메서드에 작성하였고, 추가적인 사항이 없다면 구현체에는 작성하지 않았습니다.

<details>
    <summary> java docs 진행 예시</summary>

![java_docs_example.png](assets%2Fjava_docs_example.png)
</details>

### 개발 스택
- Spring boot
- mysql
- JWT
- Spring Restdocs 


### ERD 다이어그램
[erd 사진]



### 프론트 엔드 GITHUB 링크
[Vue.js 프론트 링크],,,


