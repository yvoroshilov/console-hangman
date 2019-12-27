import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        final String[] words = new String[] {"program", "word", "thing"};
        
        boolean finish = false;
        do {
            Random random = new Random();
            String word = words[random.nextInt(words.length)];
            
            Hangman game = new Hangman(word);
            game.Play();
            
            boolean correctAns = false;
            while (!correctAns) {
                System.out.println("Do you want to start again? y/n");
                String input = new Scanner(System.in).nextLine();
                input = input.toLowerCase();
                
                if (input.length() == 1) {
                    if (input.charAt(0) == 'n') {
                        correctAns = true;
                        finish = true;
                    }
                    if (input.charAt(0) == 'y') {
                        correctAns = true;
                    }
                }
            }
        } while (!finish);
    }
}

