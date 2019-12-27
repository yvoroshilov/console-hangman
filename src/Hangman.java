import jdk.jshell.execution.*;
import org.jetbrains.annotations.*;

import java.io.*;
import java.util.*;

public class Hangman {
    private static final char STUB = '*';
    private String word;
    private char[] state;
    private int misses;
    
    public Hangman (String word) {
        this.word = word;
        state = new char[word.length()];
        Arrays.fill(state, STUB);
        misses = 0;
    }

    public void Play () throws IOException, InterruptedException {
        boolean finish = false;
        do {
            Printer.clearConsole();
            Printer.displayGame(state);
            Printer.displayPrompt();
            char guess = '-';
    
            boolean successfulGuess = false;
            while (!successfulGuess) {
                boolean correctGuess = false;
                while (!correctGuess) {
                    try {
                        guess = makeGuess();
                        correctGuess = true;
                    } catch (InputMismatchException e) {
                        Printer.displayError("Not a letter! " + "Your input was: " + e.getMessage());
                        Printer.displayPrompt();
                    }
                }
                try {
                    updateWord(guess);
                    successfulGuess = true;
                } catch (Exception e) {
                    Printer.displayError(e.getMessage());
                    Printer.displayPrompt();
                }
            }
            
            boolean wordCompleted = Util.indexOf(state, STUB) == -1;
            if (wordCompleted) {
                Printer.displayError("Done! The word: " + word + " | Misses: " + misses);
                finish = true;
            }
        } while (!finish);
    }
    
    private void updateWord (char guess) throws Exception {
        boolean isWordContains = word.indexOf(guess) != -1;
        if (isWordContains) {
            boolean isAlreadyOpened = true;
            int index = -1;
            for (int i = 0; i < word.length(); i++) {
                if (word.charAt(i) == guess && state[i] == STUB) {
                    isAlreadyOpened = false;
                    index = i;
                    break;
                }
            }
            if (!isAlreadyOpened) {
                state[index] = guess;
            } else {
                throw new Exception(String.format("Letter '%c' is already opened!", guess));
            }
        } else {
            misses++;
            throw new Exception(String.format("'%c' is not in the word!", guess));
        }
    }
    
    private char makeGuess () {
        Scanner in = new Scanner(System.in);
        String input = in.nextLine();
        if (input.length() != 1) throw new InputMismatchException(input);
        return input.charAt(0);
    }
    
    private static class Printer {
        private static final String PROMPT = "Your guess: ";
    
        private static void clearConsole () throws IOException, InterruptedException {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                Runtime.getRuntime().exec("clear");
            }
        }
        
        private static void displayGame (char[] state) {
            System.out.print("Word: ");
            for (char ch : state) {
                System.out.print(ch);
            }
            System.out.println();
        }
    
        private static void displayPrompt () {
            System.out.print(PROMPT);
        }
        
        private static void displayError (String errorMsg) {
            System.out.println(errorMsg);
        }
    }
    
    private static class Util {
        private static int indexOf (@NotNull char[] arr, char ch) {
            return indexOf(arr, ch, 0);
        }
        private static int indexOf (@NotNull char[] arr, char ch, int indexStart) {
            if (indexStart < 0 || indexStart >= arr.length) throw new IndexOutOfBoundsException(indexStart);
            for (int i = indexStart; i < arr.length; i++) {
                if (arr[i] == ch) return i;
            }
            return -1;
        }
    }
}
