package ru.javawebinar.topjava.web;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionInfoHandlerTest {

    @Test
    void findFieldOfException() {
        String result = ExceptionInfoHandler.findFieldOfException("rvfrv on field 'password' rrvr");
        System.out.println(result);
        assertEquals(result, "'password'");
    }
}