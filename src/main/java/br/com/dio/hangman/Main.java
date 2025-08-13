package br.com.dio.hangman;

import br.com.dio.hangman.exception.GameIsFinishedException;
import br.com.dio.hangman.exception.LetterAlreadyInputException;
import br.com.dio.hangman.model.HangmanChar;
import br.com.dio.hangman.model.HangmanGame;

import java.util.Scanner;
import java.util.stream.Stream;

public class Main {
        private final static Scanner scanner = new Scanner(System.in);

    public static void main(String... args) {
        var characters = Stream.of(args).map(arg -> arg.toLowerCase().charAt(0))
                .map(HangmanChar::new).toList();

        System.out.println(characters);
        var hangmanGame = new HangmanGame(characters);

        System.out.println("Bem vindo ao jogo da forca!");
        System.out.println("Tente adivinhar a palavra. Boa Sorte.");
        System.out.println(hangmanGame);

        while (true){

            System.out.println("Selecione uma das opções:");
            System.out.println("1 - Informar uma letra");
            System.out.println("2 - Ver status do jogo");
            System.out.println("3 - Sair do jogo");
           var option = scanner.nextInt();

           switch (option) {
               case 1 ->  inputCharacter(hangmanGame);
               case 2 -> showStatusGame(hangmanGame);
               case 3 ->  System.exit(0);
               default ->  System.out.println("Opção invalida!");
            }
        }



    }

    private static void showStatusGame(HangmanGame hangmanGame) {
        System.out.println(hangmanGame.getHangmanStatus());
        System.out.println(hangmanGame);
    }

    private static void inputCharacter(HangmanGame hangmanGame) {
        System.out.println("Informe uma letra:");
        var character = scanner.next().charAt(0);
        try{
            hangmanGame.inputCharacter(character);
        }catch (LetterAlreadyInputException exception){
            System.out.println(exception);
            System.out.println(hangmanGame);
        }catch (GameIsFinishedException exception) {
            System.out.println(exception.getMessage());
            System.exit(0);
        }finally {
            System.out.println(hangmanGame);
        }
    }
}