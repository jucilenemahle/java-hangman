package br.com.dio.hangman.exception;

public class GameIsFinishedException extends RuntimeException {
    public GameIsFinishedException(final String message) {
        super(message);
    }
}
