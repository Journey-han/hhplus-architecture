package io.hhplus.architecture.exception;

public class LectureException extends RuntimeException {

    public LectureException(String message) {
        super(message);
    }

    public LectureException(String message, Throwable cause) {
        super(message, cause);
    }
}
