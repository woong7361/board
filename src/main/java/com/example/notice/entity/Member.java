package com.example.notice.entity;

import com.example.notice.validate.group.MemberLoginValidationGroup;
import com.example.notice.validate.group.MemberRegisterValidationGroup;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 회원 엔티티
 */
@Getter
@NoArgsConstructor
public class Member {
    private Long memberId;

    @Pattern(
            regexp = "(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-z0-9-_]{4,11}",
            message = "아이디는 영문과 숫자를 포함하여야하고, 특수문자는 (-, _) 만 가능한 4-12자 이내의 중복되지 않는 문자여야 합니다.",
            groups = {MemberRegisterValidationGroup.class, MemberLoginValidationGroup.class}
    )
    @NotNull(groups = {MemberRegisterValidationGroup.class, MemberLoginValidationGroup.class})
    private String loginId;

    @Pattern(
            regexp = "(?=.*[a-zA-Z])(?=.*[0-9])(?!.*(.)\\1{2})[a-zA-z0-9]{4,11}",
            message = "비밀번호는 영문과 숫자를 포함하여야하고 연속된 문자가 3번이상 나오면 안됩니다.",
            groups = {MemberRegisterValidationGroup.class, MemberLoginValidationGroup.class}
    )
    @NotNull(groups = {MemberRegisterValidationGroup.class, MemberLoginValidationGroup.class})
    private String password;

    @Pattern(
            regexp = ".{2,4}",
            message = "이름은 2-4문자 이여야한다.",
            groups = {MemberRegisterValidationGroup.class}
    )
    @NotNull(groups = {MemberRegisterValidationGroup.class})
    private String name;

    private MemberRole role;

    @Builder
    protected Member(Long memberId, String loginId, String password, String name, MemberRole role) {
        this.memberId = memberId;
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.role = role;
    }

    public void setUserRole() {
        this.role = MemberRole.USER;
    }
}
