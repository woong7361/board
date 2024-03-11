package com.example.notice.dto;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 성공 실패 공통 DTO
 * @param <T>
 */
@Getter
public class SuccessesAndFails<T> {

    private List<T> successes;
    private List<T> fails;

    private SuccessesAndFails(List<T> successes, List<T> fails) {
        this.successes = successes;
        this.fails = fails;
    }

    public static <T> SuccessesAndFails<T> emptyList() {
        return new SuccessesAndFails<T>(new ArrayList<>(), new ArrayList<>());
    }

    /**
     * 성공 추가
     * @param element 성공 값
     */
    public void addSuccess(T element) {
        this.successes.add(element);
    }

    /**
     * 실패값 추가
     * @param element 실패 값
     */
    public void addFail(T element) {
        this.fails.add(element);
    }

    public void addALl(SuccessesAndFails<T> successesAndFails) {
        this.successes.addAll(successesAndFails.getSuccesses());
        this.fails.addAll(successesAndFails.getFails());
    }
}
