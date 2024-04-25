package com.example.notice.auth.path;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;


class PathContainerTest {

    PathContainer pathContainer;

    @BeforeEach
    public void init() {
        pathContainer = new PathContainer();
    }
    @Nested
    @DisplayName("pathPattern match 테스트")
    public class PathPatternIncludeExcludeTest {

        @DisplayName("include match true test")
        @ParameterizedTest
        @MethodSource("includePathPatternParams")
        public void includeTest(String targetPath, PathMethod targetMethod) throws Exception {
            //given
            //when
            pathContainer.includePathPattern("/api/a", PathMethod.GET, AuthorizationRole.MEMBER);
            pathContainer.includePathPattern("/api/a", PathMethod.POST, AuthorizationRole.MEMBER);
            pathContainer.includePathPattern("/api/a", PathMethod.PUT, AuthorizationRole.MEMBER);
            pathContainer.includePathPattern("/api/a", PathMethod.DELETE, AuthorizationRole.MEMBER);
            pathContainer.includePathPattern("/api/a", PathMethod.PATCH, AuthorizationRole.MEMBER);
            pathContainer.includePathPattern("/api/a", PathMethod.PUT, AuthorizationRole.MEMBER);
            pathContainer.includePathPattern("/api/b", PathMethod.DELETE, AuthorizationRole.MEMBER);
            pathContainer.includePathPattern("/api/c", PathMethod.DELETE, AuthorizationRole.MEMBER);
            pathContainer.includePathPattern("/api/d", PathMethod.ANY, AuthorizationRole.MEMBER);
            //then

            Boolean matchResult = pathContainer.match(targetPath, targetMethod, AuthorizationRole.MEMBER);

            Assertions.assertThat(matchResult).isTrue();
        }

        private static Stream<Arguments> includePathPatternParams() {
            return Stream.of(
                    Arguments.of("/api/a", PathMethod.GET),
                    Arguments.of("/api/a", PathMethod.POST),
                    Arguments.of("/api/a", PathMethod.PUT),
                    Arguments.of("/api/a", PathMethod.PATCH),
                    Arguments.of("/api/a", PathMethod.DELETE),
                    Arguments.of("/api/b", PathMethod.DELETE),
                    Arguments.of("/api/c", PathMethod.DELETE),
                    Arguments.of("/api/d", PathMethod.GET),
                    Arguments.of("/api/d", PathMethod.POST),
                    Arguments.of("/api/d", PathMethod.PUT),
                    Arguments.of("/api/d", PathMethod.PATCH),
                    Arguments.of("/api/d", PathMethod.DELETE)
            );
        }

        @DisplayName("exclude와 함께 있을경우 test")
        @ParameterizedTest
        @MethodSource("PathPatternParams")
        public void withExclude(String targetPath, PathMethod targetMethod) throws Exception{
            //given
            pathContainer.includePathPattern("/api/a", PathMethod.ANY, AuthorizationRole.MEMBER);
            pathContainer.excludePathPattern("/api/a", PathMethod.PATCH, AuthorizationRole.MEMBER);

            pathContainer.includePathPattern("/api/b", PathMethod.POST, AuthorizationRole.MEMBER);
            pathContainer.excludePathPattern("/api/b", PathMethod.ANY, AuthorizationRole.MEMBER);

            //when
            Boolean matchResult = pathContainer.match(targetPath, targetMethod, AuthorizationRole.MEMBER);

            //then
            Assertions.assertThat(matchResult).isFalse();
        }

        private static Stream<Arguments> PathPatternParams() {
            return Stream.of(
                    Arguments.of("/api/a", PathMethod.PATCH),
                    Arguments.of("/api/b", PathMethod.POST),
                    Arguments.of("/api/c", PathMethod.POST)
            );
        }

        @DisplayName("pattern을 쓸 경우")
        @Test
        public void usingAntPattern() throws Exception{
            //given
            pathContainer.includePathPattern("/api/**", PathMethod.POST, AuthorizationRole.MEMBER);
            pathContainer.includePathPattern("/api/**", PathMethod.PUT, AuthorizationRole.MEMBER);
            pathContainer.excludePathPattern("/api/abc", PathMethod.PUT, AuthorizationRole.MEMBER);

            //when
            Boolean matchResult = pathContainer.match("/api/av/sd", PathMethod.POST, AuthorizationRole.MEMBER);
            Boolean excludeResult = pathContainer.match("/api/abc", PathMethod.PUT, AuthorizationRole.MEMBER);
            Boolean notExcludeResult = pathContainer.match("/api/abc", PathMethod.POST, AuthorizationRole.MEMBER);

            //then
            Assertions.assertThat(matchResult).isTrue();
            Assertions.assertThat(excludeResult).isFalse();
            Assertions.assertThat(notExcludeResult).isTrue();
        }

        @DisplayName("role이 다를 경우 테스트")
        @Test
        public void notMatchRole() throws Exception{
            //given
            pathContainer.includePathPattern("/api/a/**", PathMethod.POST, AuthorizationRole.MEMBER);
            pathContainer.includePathPattern("/api/b/**", PathMethod.POST, AuthorizationRole.GUEST);

            //when
            Boolean nonMatchResult = pathContainer.match("/api/a/123", PathMethod.POST, AuthorizationRole.GUEST);
            Boolean matchResult = pathContainer.match("/api/a/123", PathMethod.POST, AuthorizationRole.MEMBER);

            //then
            Assertions.assertThat(nonMatchResult).isFalse();
            Assertions.assertThat(matchResult).isTrue();
        }
    }

}