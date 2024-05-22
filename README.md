
# 개인 게시판 프로젝트

**[프로젝트 시연]**  
이 프로젝트는 Java Spring 과 Vue.js를 통해 만들어졌습니다.

범용적으로 가장 많이 쓰이는 개발 요소인 게시판을 구현하였습니다. 
세가지 게시판이 존재하고 그것을 관리할 수 있는 관리자 페이지또한 구현하였습니다.

https://github.com/woong7361/board/assets/87160021/9cc6b1a9-e168-4d08-a959-b56deb1e8ea5


## Getting Started
<a href="http://15.165.133.6/" target="_blank">프로젝트로 이동</a>

회원 아이디: abc123   
회원 비밀번호: qwe123

관리자 아이디: a123  
관리자 비밀번호: b123

## 주요 개발 사항 (클릭해서 펼치기)

<details>
    <summary style="font-size: 20px"> threadLocal를 사용해 ThreadSafe한 인증&인가 구현하기</summary>

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
   인증 과정
   ```
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
   인가 과정
   ```
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

4. #### urlPattern, HttpMethod, Role을 갖춘 pathContainer 구현 (인가 과정중에 사용)
   ```
    /**
     * 요청 경로를 관리하는 경로 저장소
     */
    public class PathContainer {
        private final PathMatcher pathMatcher = new AntPathMatcher();
        private final List<PathWithRole> includePathPattern = new ArrayList<>();
        private final List<PathWithRole> excludePathPattern = new ArrayList<>();
    
        /**
         * 경로 저장소에 경로를 추가한다
         * @param pathPattern 요청 경로
         * @param pathMethod 요청 메서드
         * @param role 권한
         */
        public void includePathPattern(String pathPattern, PathMethod pathMethod, AuthorizationRole role) {
            this.includePathPattern.add(new PathWithRole(pathPattern, pathMethod, role));
        }
    
        /**
         * 경로 저장소에 경로를 제외한다.
         * @param pathPattern 요청 경로
         * @param pathMethod 요청 메서드
         * @param role 권한
         */
        public void excludePathPattern(String pathPattern, PathMethod pathMethod, AuthorizationRole role) {
            this.excludePathPattern.add(new PathWithRole(pathPattern, pathMethod, role));
        }
    
        /**
         * 요청 경로가 저장된 경로에 해당하는지 판별한다.
         * @param targetPath 요청 경로
         * @param pathMethod 요청 메서드
         * @param role 권한
         * @return 판별 결과
         */
        public Boolean match(String targetPath, PathMethod pathMethod, AuthorizationRole role) {
            ...
        }
        ...
    }
   ```
   - <a href="https://github.com/woong7361/board/blob/main/src/main/java/com/example/notice/auth/path/PathContainer.java" target="_blank">pathContainer class</a>

5. #### 기존 interceptor와의 통일성을 고려해 WebConfig 에서 pattern 추가
   ```
   @Configuration
   @RequiredArgsConstructor
   public class WebConfig implements WebMvcConfigurer {
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

       ...

        registry.addInterceptor(authorizationInterceptor)
                .addPathPatterns("/api/**");

        setInterceptorPatterns(authorizationInterceptor);
    }

    private void setInterceptorPatterns(AuthorizationInterceptor authorizationInterceptor) {
        authorizationInterceptor.includePathPatterns("/api/**", PathMethod.ANY, AuthorizationRole.MEMBER);

        authorizationInterceptor.includePathPatterns("/api/**", PathMethod.ANY, AuthorizationRole.GUEST);

        authorizationInterceptor.excludePathPatterns("/api/boards/free", PathMethod.POST, AuthorizationRole.GUEST);
        authorizationInterceptor.excludePathPatterns("/api/boards/free", PathMethod.PUT, AuthorizationRole.GUEST);
   
       ...

    }
   
   ...
   }
   ```
   - <a href="https://github.com/woong7361/board/blob/07ffefaeca7192eb97c6ea21774cda8d62fe870a/src/main/java/com/example/notice/config/WebConfig.java#L53C1-L80C6" target="_blank">WebConfig Class</a>

6. #### 파라미터 주입을 사용하기 위해 HandlerMethodArgumentResolver 구현체 작성
    ```
    /**
     * AuthenticationHolder 에서 인증객체를 가져오는 역할을 한다.
     */
    @Component
    public class AuthenticationHolderResolveHandler implements HandlerMethodArgumentResolver {
    
        /**
         * parameter가 Principal.class 인지 AND @Annotation이 AuthenticationPrincipal.class 인지
         * @param parameter method Argument parameter
         * @return 파라미터를 지원하는지
         */
        @Override
        public boolean supportsParameter(MethodParameter parameter) {
            return isSupportAnnotationClass(parameter) & isSupportParameterType(parameter);
        }
    
        /**
         * Authentication Holder에서 인증된 회원 객체를 꺼내준다.
         * @return 인증된 회원 객체
         */
        @Override
        public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
            Principal principal = AuthenticationHolder.getPrincipal();
            if (principal == null) {
                throw new AuthenticationException();
            }
    
            return principal;
        }
    
        ...
    
    }

    ```
    - <a href="https://github.com/woong7361/board/blob/main/src/main/java/com/example/notice/auth/resolvehandler/AuthenticationHolderResolveHandler.java" target="_blank">Resolve Handler</a>
    - <a href="https://github.com/woong7361/board/blob/main/src/main/java/com/example/notice/auth/resolvehandler/AuthenticationPrincipal.java" target="_blank">Annotation</a>
    

</details>

<details>
    <summary style="font-size: 20px"> 파일 저장 실패시 정책사항 </summary>

> Disk 오류 또는 다른 사항들에 파일 저장이 실패했을때(일부라도) 의해 내가 작성한 모든 사항이 'Rollback' 된다는 사항이 사용자 입장에서 
> 받아들이기 힘들다고 생각하여 파일 저장이 일부 실패하여도 transaction은 정상적으로 진행되도록 결정하였다.  

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
    <summary style="font-size: 20px"> 파일 저장소의 변경에 대한 반응성 향상 </summary>

실제 파일의 저장 위치가 달라짐에 대응하기 위하여(local storage, cloud storage, NAS 등...) byte를 저장할 physicalFileStorage와 metadata를 저장할 repository를 분리하고
interface를 통해 변경에 열려있도록 작성하였다.

파일 byte 저장소
    
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
        * 파일을 바로 삭제하지는 않고 로그를 남긴다.
        *
        * @param fileId 파일 식별자
        */
       void delete(Long fileId);
   
       /**
        * 물리적 파일을 조회
        *
        * @param 파일 경로
        * @return 물리적 파일
        */
       File getFile(String path);
   }
   ```
</details>

<details>
    <summary style="font-size: 20px"> CRUD 예시 (자유게시판 검색)</summary>

Controller
    
    ```
    /**
     * 자유게시판 게시글 리스트 조회/검색
     *
     * @param freeBoardSearchDTO 게시글 검색 파라미터
     * @param pageRequest 페이지네이션 요청 파라미터
     * @return 게시글 페이지 정보
     */
    @GetMapping("/api/boards/free")
    public ResponseEntity<PageResponse<FreeBoard>> getFreeBoards(
            @ModelAttribute FreeBoardSearchDTO freeBoardSearchDTO,
            @Valid @ModelAttribute PageRequest pageRequest
    ) {
        PageResponse<FreeBoard> boards = freeBoardService.getBoardsBySearchParams(freeBoardSearchDTO, pageRequest);

        return ResponseEntity.ok(boards);
    }

    ```

Service
    
    ```
    @Override
    public PageResponse<FreeBoard> getBoardsBySearchParams(FreeBoardSearchDTO freeBoardSearchDTO, PageRequest pageRequest) {
        Integer totalCount = freeBoardRepository.getTotalCountBySearchParam(freeBoardSearchDTO);
        List<FreeBoard> boards = freeBoardRepository.findBoardsBySearchParam(freeBoardSearchDTO, pageRequest);

        return new PageResponse<>(boards, pageRequest, totalCount);
    }
    ```

Repository (mybatis 사용)
    
    ```
    /**
     * 자유게시판 게시글 검색
     *
     * @param freeBoardSearchDTO 게시글 검색 조건 파라미터
     * @param pageRequest 게시글 페이지네이션 파라미터
     * @return 검색 결과
     */
    List<FreeBoard> findBoardsBySearchParam(@Param("search") FreeBoardSearchDTO freeBoardSearchDTO, @Param("page") PageRequest pageRequest);

    /**
     * 검색 조건에 맞는 게시글 총 개수 조회
     *
     * @param freeBoardSearchDTO 게시글 검색 조건 파라미터
     * @return 검색된 게시글 총 개수
     */
    Integer getTotalCountBySearchParam(@Param("search") FreeBoardSearchDTO freeBoardSearchDTO);
    ```
    
</details>

<a href="https://github.com/woong7361/board/blob/main/src/main/java/com/example/notice/files/PhysicalFileRepository.java" target="_blank">Physical file repository</a>


## test code
test는 Spring의 단위테스트와 Controller테스트만 진행하였습니다.


![test_count.png](assets%2Ftest_count.png)

![test coverage.png](assets%2Ftest%20coverage.png)

> 비어있는 테스트 커버리지는 접근을 제한하고자 작성한 코드나 @SpringBootApplication 같은
단위테스트에 쓰지지 않는 코드이므로 작성하지 않았습니다.


test code 진행 예시

![test_example.png](assets%2Ftest_example.png)

<a href="https://github.com/woong7361/board/tree/main/src/test/java/com/example/notice" target="_blank">test code src folder</a>


## 문서화
API문서화는 spring restdocs를 이용하여 진행하였습니다.

<a href="http://15.165.133.6:8888/docs/index.html" target="_blank">Spring Restdocs 링크</a>

![image](https://github.com/woong7361/board/assets/87160021/ec7f1ed9-9303-4814-a109-0d4aa0c5f6fe)


spring restdocs 진행 예시

![restdocs.png](assets%2Frestdocs.png)

</details>

코드 문서화는 java docs를 이용하여 진행하였습니다.

class와 interface의 모든 public 메서드에 작성하였고, 추가적인 사항이 없다면 구현체에는 작성하지 않았습니다.


java docs 진행 예시

![java_docs_example.png](assets%2Fjava_docs_example.png)


## ERD diagram
![포트폴리오 (1)](https://github.com/woong7361/board/assets/87160021/a1ec69fd-eed9-4be9-b5b2-e9450d012458)



## 개발 스택
- Spring boot
- mysql
- JWT
- Spring Restdocs
- mybatis


## 프론트 엔드 GITHUB 링크
<a href="https://github.com/woong7361/board_front" target="_blank">Vue.js 프론트 링크</a>

