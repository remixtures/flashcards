package flashcards;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GameLogger {

    private static final Scanner input = new Scanner(System.in);
    private static final List<String> loggingList = new ArrayList<>();

    protected void logOutput(String text) {
        System.out.println(text);
        loggingList.add(text);
    }

    protected String logInput() {
        String userInput = input.nextLine();
        loggingList.add(userInput);
        return userInput;
    }

    protected void saveGame() {
        logOutput("File name:");
        String fileName = logInput();
        File file = new File(fileName);
        try (PrintWriter printWriter = new PrintWriter(file)) {
            printWriter.println("LOG STARTS HERE\n");
            printWriter.println("********************");
            for (String logEntry : loggingList) {
                printWriter.println(logEntry);
            }
            printWriter.println("\n********************");
            printWriter.println("LOG ENDS HERE");
            printWriter.close();
            logOutput("The log has been saved.");
        } catch (IOException e) {
            logOutput("An exception occurred: " + e.getMessage());
        }
    }
}