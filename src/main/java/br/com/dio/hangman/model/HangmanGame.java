package br.com.dio.hangman.model;

import br.com.dio.hangman.exception.GameIsFinishedException;
import br.com.dio.hangman.exception.LetterAlreadyInputException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static br.com.dio.hangman.model.HangmanGameStatus.*;

public class HangmanGame {

    private final static int HANGMAN_INITIAL_LINE_LENGTH = 9;
    private final static int HANGMAN_INITIAL_LINE_LENGTH_WITH_LINE_SEPARATOR = 10;

    private final int lineSize;
    private final int hangmanInitialSize;

    private final List<HangmanChar> characters;
    private final List<HangmanChar> hangmanPath;
    private final List<Character> failAttempts = new ArrayList<>();

    private String hangman;
    private HangmanGameStatus hangmanGameStatus;

    public HangmanGame(final List<HangmanChar> characters){
        var whiteSpace = " ".repeat(characters.size());
        var characterSpace = "-".repeat(characters.size());
        this.lineSize = HANGMAN_INITIAL_LINE_LENGTH_WITH_LINE_SEPARATOR + whiteSpace.length();
        this.hangmanGameStatus = PENDING;
        this.hangmanPath = buildHangmanPathPosition();
        this.buildHangmanDesign(whiteSpace, characterSpace);
        this.characters = setCharacterSpacesPositionInGame(characters, whiteSpace.length());
        this.hangmanInitialSize = hangman.length();
    }

    public void inputCharacter(final char character) {
        if (!PENDING.equals(this.hangmanGameStatus)){
            var message = WIN.equals(hangmanGameStatus)
                    ? "Parabéns você venceu!"
                    : "Você perdeu! tente novamente!";
            throw new GameIsFinishedException(message);
        }

        var found = this.characters
                .stream()
                .filter(c -> c.getCharacter() == character)
                .toList();

        if (this.failAttempts.contains(character)){
            throw new LetterAlreadyInputException("A letra '"+ character + "' já foi informada anteriormente");
        }
        if (found.isEmpty()){
            failAttempts.add(character);
            if (failAttempts.size() >= 6){
                this.hangmanGameStatus = LOSE;
            }
            this.rebuildHangman(this.hangmanPath.removeFirst());
            return;
        }

        if (found.getFirst().isInvisible()){
           throw new LetterAlreadyInputException("A letra '"+ character + "' já foi informada anteriormente");
        }

        this.characters.forEach( c->{
            if (c.getCharacter() == found.getFirst().getCharacter()){
                c.enableVisibility();
            }
        });
        if(this.characters.stream().noneMatch(HangmanChar::isInvisible)){
            this.hangmanGameStatus = WIN;
        }
        rebuildHangman(found.toArray(HangmanChar[]::new));
    }

    @Override
    public String toString() {
        return this.hangman;
    }

    public HangmanGameStatus getHangmanStatus(){
        return hangmanGameStatus;
    }

    private List<HangmanChar> buildHangmanPathPosition(){
        final var HEAD_LINE  = 3;
        final var BODY_LINE  = 4;
        final var LEGS_LINE  = 5;

        return new ArrayList<>(
                List.of(
                        new HangmanChar('0', this.lineSize * HEAD_LINE + 6),
                        new HangmanChar('|', this.lineSize * BODY_LINE + 6),
                        new HangmanChar('/', this.lineSize * BODY_LINE + 5),
                        new HangmanChar('\\', this.lineSize * BODY_LINE + 7),
                        new HangmanChar('/', this.lineSize * LEGS_LINE + 5),
                        new HangmanChar('\\', this.lineSize * LEGS_LINE + 7)
                )
        );
    }

    private List<HangmanChar> setCharacterSpacesPositionInGame(final List<HangmanChar> characters, final int whiteSpacesAmount ){
        final var LINE_LETTER = 6;
        for (int i = 0; i < characters.size(); i++){
            characters.get(i).setPosition(this.lineSize * LINE_LETTER + HANGMAN_INITIAL_LINE_LENGTH + i);

        }
        return characters;
    }

    private void rebuildHangman(final HangmanChar... hangmanChars){
        var hangmanBuilder = new StringBuilder(this.hangman);
        Stream.of(hangmanChars)
                .forEach(
                        h -> hangmanBuilder.setCharAt(h.getPosition(), h.getCharacter()
                        ));
        var failMessage = this.failAttempts.isEmpty() ? "" : "Tentativas:" + failAttempts;
        this.hangman = hangmanBuilder.substring(0, hangmanInitialSize) + failMessage;
    }

    private void buildHangmanDesign(final String whiteSpace, final String characterSpaces){
        this.hangman = "  ----- " + whiteSpace + System.lineSeparator() +
                       "  |   | " + whiteSpace + System.lineSeparator() +
                       "  |   | " + whiteSpace + System.lineSeparator() +
                       "  |     " + whiteSpace + System.lineSeparator() +
                       "  |     " + whiteSpace + System.lineSeparator() +
                       "  |     " + whiteSpace + System.lineSeparator() +
                       "  |     " + whiteSpace + System.lineSeparator() +
                       "========" + characterSpaces + System.lineSeparator() ;
    }
}
