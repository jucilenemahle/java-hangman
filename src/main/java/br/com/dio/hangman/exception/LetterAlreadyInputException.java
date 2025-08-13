package br.com.dio.hangman.exception;

public class LetterAlreadyInputException extends RuntimeException {
    public LetterAlreadyInputException(final String message) {
        super(message);
    }
}
