package flashcards;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Game {

    private static final Map<String, String> cardConcepts = new LinkedHashMap<>();
    private static final Map<String, Integer> gameStatistics = new LinkedHashMap<>();
    private final GameLogger gameLogger = new GameLogger();

    protected void addCard() {
        gameLogger.logOutput("The card:");
        String term = gameLogger.logInput();
        if (cardConcepts.containsKey(term)) {
            gameLogger.logOutput("The card \"" + term + "\" already exists.");
            return;
        }

        gameLogger.logOutput("The definition of the card:");
        String definition = gameLogger.logInput();
        if (cardConcepts.containsValue(definition)) {
            gameLogger.logOutput("The definition \"" + definition + "\" already exists.");
            return;
        }

        gameLogger.logOutput("The pair (\"" + term + "\": \"" + definition + "\") has been added");
        cardConcepts.put(term, definition);
        gameStatistics.put(term, 0);
    }

    protected void removeCard() {
        gameLogger.logOutput("The card:");
        String term = gameLogger.logInput();
        if (cardConcepts.containsKey(term)) {
            cardConcepts.remove(term);
            gameStatistics.remove(term);
            gameLogger.logOutput("The card has been removed");
        } else {
            gameLogger.logOutput("Can't remove \"" + term + "\": there is no such card");
        }
    }

    public void importFromConsole() {
        gameLogger.logOutput("File name:");
        String fileName = gameLogger.logInput();
        importFromCommandLine(fileName);
    }

    protected void importFromCommandLine(String fileName) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            List<String[]> importedCards = (ArrayList) ois.readObject();
            for (String[] card : importedCards) {
                cardConcepts.put(card[0], card[1]);
                gameStatistics.put(card[0], Integer.parseInt(card[2]));
            }
            gameLogger.logOutput(importedCards.size() + " cards have been loaded");
        } catch (IOException e) {
            gameLogger.logOutput("File not found.");
        } catch (ClassNotFoundException e) {
            gameLogger.logOutput("Class not found.");
        }
    }

    protected void exportFromConsole(){
        gameLogger.logOutput("File name:");
        String fileName = gameLogger.logInput();
        exportFromCommandLine(fileName);
    }

    protected void exportFromCommandLine(String fileName) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            List<String[]> exportedCards = new ArrayList<>();
            for (Map.Entry<String, String> entry : cardConcepts.entrySet()) {
                String term = entry.getKey();
                String definition = entry.getValue();
                exportedCards.add(new String[]{term, definition, Integer.toString(gameStatistics.get(term))});
            }
            oos.writeObject(exportedCards);
            gameLogger.logOutput(cardConcepts.size() + " cards have been saved");
        } catch (IOException e) {
            gameLogger.logOutput("File not found.");
        }
    }

    protected void askCards() {
        Random randomNumber = new Random();
        List<String> listOfCards = new ArrayList<>(cardConcepts.keySet());
        gameLogger.logOutput("How many times to ask?");
        int timesToAsk = Integer.parseInt(gameLogger.logInput());

        for (int i = 0; i < timesToAsk; i++) {
            String term = listOfCards.get(randomNumber.nextInt(listOfCards.size()));
            gameLogger.logOutput("Print the definition of \"" + term + "\":");
            String definition = gameLogger.logInput();
            if (definition.equals(cardConcepts.get(term))) {
                gameLogger.logOutput("Correct!");
            } else if (cardConcepts.containsValue(definition)) {
                gameLogger.logOutput("Wrong. The right answer is \"" + cardConcepts.get(term) + "\" but your definition is correct for \"" + getKeyFromValue(definition)+ "\":");
                gameStatistics.put(term, gameStatistics.get(term) + 1);
            } else {
                gameLogger.logOutput("Wrong. The right answer is \"" + cardConcepts.get(term) + "\"");
                gameStatistics.put(term, gameStatistics.get(term) + 1);
            }
        }
    }

    private static String getKeyFromValue(String value) {
        for (Map.Entry<String, String> entry : cardConcepts.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }

    protected void checkHardestCards() {
        List<String> hardestCards = new ArrayList<>();
        int max = 1;
        for (String term : gameStatistics.keySet()) {
            Integer errors = gameStatistics.get(term);
            if (errors > max) {
                max = errors;
                hardestCards.clear();
                hardestCards.add(term);
            } else if (errors == max) {
                hardestCards.add(term);
            }
        }

        switch (hardestCards.size()) {
            case 0:
                gameLogger.logOutput("There are no cards with errors.");
                break;
            case 1:
                gameLogger.logOutput("The hardest card is \"" + hardestCards.get(0) + "\". You have " + max + " errors answering it.");
                break;
            default:
                StringBuilder terms = new StringBuilder();
                for (int i = 0; i < hardestCards.size(); i++) {
                    if (i < hardestCards.size() - 1) {
                        terms.append("\"").append(hardestCards.get(i)).append("\", ");
                    } else {
                        terms.append("\"").append(hardestCards.get(i)).append("\". ");
                    }
                }
                gameLogger.logOutput("The hardest cards are " + terms + "You have " + max + " errors answering them.");
        }
    }

    protected void setErrorsToZero() {
        for (Map.Entry<String, Integer> entry : gameStatistics.entrySet()) {
            String term = entry.getKey();
            Integer error = entry.getValue();
            gameStatistics.replace(term, 0);
        }
        gameLogger.logOutput("Card statistics has been reset.");
    }
}