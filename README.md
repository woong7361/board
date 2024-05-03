
# 개인 게시판 프로젝트

**[프로젝트 시연]**  
이 프로젝트는 Spring 과 Vue.js를 통해 만들어졌습니다.

범용적으로 가장 많이 쓰이는 개발 요소인 게시판을 구현하였습니다. 
세가지 게시판이 존재하고 그것을 관리할 수 있는 관리자 페이지또한 구현하였습니다.

https://github.com/woong7361/board/assets/87160021/9cc6b1a9-e168-4d08-a959-b56deb1e8ea5


## Getting Started
<a href="http://43.203.209.77/" target="_blank">프로젝트로 이동</a>

회원 아이디: abc123   
회원 비밀번호: qwe123

관리자 아이디: a123  
관리자 비밀번호: b123

### 주요 개발 사항

<details>
    <summary style="font-size: 20px"> threadLocal을 통한 인증&인가 구현하기</summary>

프레임워크 없이 인증 과정을 구현하다보니 Spring Security에서 영감을 얻어 ThreadLocal을 사용해  내가 필요한 부분까지 Security와 비슷하게 구현하게 되었다.

1. #### Thread Local을 사용하기 위해 wrapping 저장소인 AuthenticationHolder 생성과 인증 wrapper 객체 생성
   ```
    /**
     * 인증된 회원 보관소
     */
     public class AuthenticationHolder {
     private static final ThreadLocal<Principal> threadLocal = ThreadLocal.withInitial(() -> null);

       /**
        * 인증된 회원 주입
        *
        * @param principal 인증된 회원
        */
       public static void setPrincipal(Principal principal) {
           threadLocal.set(principal);
       }

       /**
        * 인증된 회원 가져오기
        *
        * @return 인증된 회원
        */
       public static Principal getPrincipal() {
           return threadLocal.get();
       }
   
      ...
   }
   ```
   
   - <a href="https://github.com/woong7361/board/blob/main/src/main/java/com/example/notice/auth/AuthenticationHolder.java" target="_blank">AuthenticationHolder - threadLocal Wrapping Class</a>
   - <a href="https://github.com/woong7361/board/blob/main/src/main/java/com/example/notice/auth/principal/Principal.java" target="_blank">Principal - Holder에 저장되는 인증 객체</a>
   
2. #### Thread Local이 Thread Safe를 확인하기 위해 Thread test 진행

   ```
   @DisplayName("로컬 스레드마다 다른 값 확인")
        @Test
        public void multiThread() throws Exception{
            //given
            ## 100개의 스레드풀
            ExecutorService executorService = Executors.newFixedThreadPool(100);

            ## 10000번의 작업 진행
            int threadCount = 10000; 
            CountDownLatch latch = new CountDownLatch(threadCount);

            List<Long> memberIds = new ArrayList<>();
            List<Long> results = new CopyOnWriteArrayList<>();

            //when
            for (long i = 0; i < threadCount; i++) {
                memberIds.add(i);

                Member member = Member.builder()
                        .memberId(i)
                        .build();
                Principal<Member> principal = new MemberPrincipal(member);

                executorService.submit(() -> {
                    try {
                        AuthenticationHolder.setPrincipal(principal);
                        Principal<Member> savedPrincipal = AuthenticationHolder.getPrincipal();
                        long savedMemberId = savedPrincipal.getAuthentication().getMemberId();

                        results.add(savedMemberId);
                    } finally {
                        latch.countDown();
                    }
                });
            }
            latch.await();

            //then
            memberIds.sort((t1, t2) -> (int) (t1 - t2));
            results.sort((t1, t2) -> (int) (t1 - t2));
            assertThat(memberIds).usingRecursiveComparison().isEqualTo(results);
        }
   ```
   - <a href="https://github.com/woong7361/board/blob/5ac16d321fcd836cf585a918006657608bbc8c0e/src/test/java/com/example/notice/auth/AuthenticationHolderTest.java#L60C1-L99C10" target="_blank">test code 링크</a>

3. #### intercepter와 JWT를 사용해 인증과 인가 구현
   ```
   인증 과정
    /**
     * JWT를 통해 인증 과정을 진행한다.
     * @apiNote token이 없다면 비회원으로, 있다면 회원으로 다음 interceptor로 진행한다.
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getMethod().equals(PathMethod.OPTIONS.name())) {
            return true;
        }

        String bearerToken = request.getHeader(AUTHORIZATION);

        AuthenticationHolder.clear();
        if (bearerToken == null) {
            setGuest();
        } else {
            setMember(bearerToken);
        }

        return true;
    }
   ```
   ```
   인가 과정
    /**
     * AuthenticationRole에 따라 인가 과정을 진행한다.
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getMethod().equals(PathMethod.OPTIONS.name())) {
            return true;
        }

        if (pathContainer.match(request.getRequestURI(), PathMethod.valueOf(request.getMethod()), AuthenticationHolder.getRole())) {
            return true;
        }
        throw new AuthorizationException(ErrorMessageConstant.AUTHORIZATION_EXCEPTION_MESSAGE);
    }
   ```
    - <a href="https://github.com/woong7361/board/blob/main/src/main/java/com/example/notice/auth/filter/JwtTokenInterceptor.java" target="_blank">인증 Interceptor Class</a>
    - <a href="https://github.com/woong7361/board/blob/main/src/main/java/com/example/notice/auth/filter/AuthorizationInterceptor.java" target="_blank">인가 Interceptor Class</a>

4. #### urlPattern, HttpMethod, Role을 갖춘 pathContainer 구현
   - <a href="https://github.com/woong7361/board/blob/main/src/main/java/com/example/notice/auth/path/PathContainer.java" target="_blank">pathContainer class</a>

5. #### 기존 interceptor와의 통일성을 고려해 config 에서 patter 추가
   - <a href="https://github.com/woong7361/board/blob/07ffefaeca7192eb97c6ea21774cda8d62fe870a/src/main/java/com/example/notice/config/WebConfig.java#L53C1-L80C6" target="_blank">WebConfig Class</a>

6. #### 사용하기 위해 resolveHandler를 통해 parameter 주입 사용
    - <a href="https://github.com/woong7361/board/blob/main/src/main/java/com/example/notice/auth/resolvehandler/AuthenticationHolderResolveHandler.java" target="_blank">Resolve Handler</a>
    - <a href="https://github.com/woong7361/board/blob/main/src/main/java/com/example/notice/auth/resolvehandler/AuthenticationPrincipal.java" target="_blank">Annotation</a>
    

</details>

<details>
    <summary style="font-size: 20px"> 파일 저장 실패시 정책사항 </summary>

> Disk 오류 또는 다른 사항들에 파일 저장이 실패했을때(일부라도) 의해 내가 작성한 모든 사항이 'Rollback' 된다는 사항이 사용자 입장에서 
> 받아들이기 힘들다고 생각하여 파일 저장이 실패하여도 transaction은 정상적으로 진행되도록 결정하였다.  

- 결과화면
![file_save.png](assets%2Ffile_save.png)

Checked Exception은 Transaction Rollback을 일으키지 않는 성질을 이용하여 file save에 실패할경우 throw와 catch를 통해 정책을 수행한다. 

   - throw 부분
   ```
   @Override
    public String save(byte[] bytes, String originalFileName) throws FileSaveCheckedException {
        String fullPath = configurationService.getFilePath() + "/" + getNewFilename(getExtension(originalFileName));

        try (OutputStream outputStream = new FileOutputStream(fullPath))
        {
            outputStream.write(bytes);
        } catch (IOException e) {
            log.info("file save failed  fileName: {},  stackTrace{}", originalFileName, e);
            
            **error가 발생하면 checked Exception으로 먹어준다**
            throw new FileSaveCheckedException(e.getMessage());
        }

        return fullPath;
    }
   ```
   - catch 부분
   ```
       @Transactional
    @Override
    public SuccessesAndFails<String> saveFiles(List<MultipartFile> multipartFiles, Long freeBoardId) {
        SuccessesAndFails<String> results = SuccessesAndFails.emptyList();

        for (MultipartFile multipartFile : multipartFiles) {
            try {
                fileUtil.checkAllowFileExtension(multipartFile);
                AttachmentFile attachmentFile = saveFile(multipartFile, freeBoardId);
                
                **성공사례 저장**
                results.addSuccess(attachmentFile.getOriginalName());
            } catch (FileSaveCheckedException e) {
                String originalFilename = multipartFile.getOriginalFilename();
               
                **실패사례 저장**
                results.addFail(multipartFile.getOriginalFilename());
            }
        }

        return results;
    }
   ```

<a href="https://github.com/woong7361/board/blob/main/src/main/java/com/example/notice/files/DiskFileRepository.java" target="_blank">Physical file repository</a>

<a href="https://github.com/woong7361/board/blob/022ab9e11ec150085f93acfe5aea11ba53b44668/src/main/java/com/example/notice/service/FileServiceImpl.java#L52C1-L71C6" target="_blank">파일 저장 로직</a>

</details>

<details>
    <summary style="font-size: 20px"> Pyhsical File Repository와 DB File Repository의 분리 </summary>

물리적 파일과 DB 파일을 같이 다루고 있었는데 서로 다른 유형의 데이터를 저장하기에 분리를 결정하였다.
또한 DB와 File은 저장소의 확장이나 변경에 다르게 반응해야하므로 분리를 결정하게되었다.

   ```
   /**
    * 물리적 파일 저장소
    */
   public interface PhysicalFileRepository {
   
       /**
        * 파일 저장
        *
        * @param bytes 파일 bytes
        * @param originalFileName 파일 원본 이름
        * @return 저장된 파일 경로
        */
       String save(byte[] bytes, String originalFileName) throws FileSaveCheckedException;
   
       /**
        * 파일 삭제
        *
        * @param fileId 파일 식별자
        */
       void delete(Long fileId);
   
       /**
        * 물리적 파일을 조회
        *
        * @param path 파일의 이름을 포함한 경로
        * @return 물리적 파일
        */
       File getFile(String path);
   }
   ```
</details>

<a href="https://github.com/woong7361/board/blob/main/src/main/java/com/example/notice/files/PhysicalFileRepository.java" target="_blank">Physical file repository</a>


### test code
test는 Spring의 단위테스트와 Controller테스트만 진행하였습니다.


![test_count.png](assets%2Ftest_count.png)

![test coverage.png](assets%2Ftest%20coverage.png)

> 비어있는 테스트 커버리지는 접근을 제한하고자 작성한 코드나 @SpringBootApplication 같은
단위테스트에 쓰지지 않는 코드라 무리하게 커버리지를 높이고자 작성하지 않았습니다.

<details>
    <summary> test code 진행 예시</summary>

![test_example.png](assets%2Ftest_example.png)

</details>

<a href="https://github.com/woong7361/board/tree/main/src/test/java/com/example/notice" target="_blank">test code src folder</a>


## 문서화
API문서화는 spring restdocs를 이용하여 진행하였습니다.

<a href="http://43.203.209.77:8888/docs/index.html" target="_blank">Spring Restdocs 링크</a>

<details>
    <summary> spring restdocs 진행 예시</summary>

![image](https://github.com/woong7361/board/assets/87160021/ec7f1ed9-9303-4814-a109-0d4aa0c5f6fe)

![restdocs.png](assets%2Frestdocs.png)
</details>

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


### 프론트 엔드 GITHUB 링크
<a href="https://github.com/woong7361/board_front" target="_blank">Vue.js 프론트 링크</a>

