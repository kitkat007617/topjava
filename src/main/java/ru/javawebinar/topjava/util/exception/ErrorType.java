package ru.javawebinar.topjava.util.exception;

public enum ErrorType {
    APP_ERROR("Ошибка приложения"),
    DATA_NOT_FOUND("Данные не найдены"),
    DATA_ERROR("Ошибка данных"),
    VALIDATION_ERROR("Ошибка проверки данных");

    private final String s;

    @Override
    public String toString() {
        return s;
    }

    ErrorType(String s) {
        this.s = s;
    }


}
