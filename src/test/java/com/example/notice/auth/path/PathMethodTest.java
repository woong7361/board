package com.example.notice.auth.path;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;


class PathMethodTest {

    @Nested
    @DisplayName("PathMethod 비교 테스트")
    public class NestedClass {

        @DisplayName("모든 메서드를 허용할때 - ANY")
        @Test
        public void allowALlMethod() throws Exception {
            //given
            //when
            for (PathMethod targetMethod : PathMethod.values()) {
                Boolean result = PathMethod.match(PathMethod.ANY, targetMethod);

                //then
                Assertions.assertThat(result).isTrue();

            }
        }

        @DisplayName("같은 메서드일때 테스트")
        @Test
        public void matchSameMethod() throws Exception{
            //given
            //when
            for (PathMethod targetMethod : PathMethod.values()) {
                Boolean result = PathMethod.match(targetMethod, targetMethod);

                //then
                Assertions.assertThat(result).isTrue();
            }
        }

        @DisplayName("다른 메서드일때 테스트")
        @Test
        public void notSameMethod() throws Exception{
            //given
            //when
            for (PathMethod method : PathMethod.values()) {
                if (PathMethod.ANY == method) continue;
                for (PathMethod targetMethod : PathMethod.values()) {
                    if (PathMethod.ANY == targetMethod) continue;
                    if (method == targetMethod) continue;

                    Boolean result = PathMethod.match(method, targetMethod);

                    Assertions.assertThat(result).isFalse();
                }
                //then
            }
        }
    }

}