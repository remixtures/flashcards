package flashcards;

public class Main {

    private static final GameLogger gameLogger = new GameLogger();

    public static void main(String[] args) {
        Game flashCardGame = new Game();
        boolean endGame = false;
        String exportFile = "";
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-import":
                    flashCardGame.importFromCommandLine(args[i + 1]);
                    break;
                case "-export":
                    exportFile = args[i + 1];
                    break;
                default:
            }
        }
        while (!endGame) {
            gameLogger.logOutput("Input the action (add, remove, import, " +
                    "export, ask, exit, log, hardest card, reset stats):");
            switch (gameLogger.logInput()) {
                case "add":
                    flashCardGame.addCard();
                    break;
                case "remove":
                    flashCardGame.removeCard();
                    break;
                case "import":
                    flashCardGame.importFromConsole();
                    break;
                case "export":
                    flashCardGame.exportFromConsole();
                    break;
                case "ask":
                    flashCardGame.askCards();
                    break;
                case "exit":
                    gameLogger.logOutput("Bye Bye!");
                    if (!"".equals(exportFile)) {
                        flashCardGame.exportFromCommandLine(exportFile);
                    }
                    endGame = true;
                    break;
                case "log":
                    gameLogger.saveGame();
                    break;
                case "hardest card":
                    flashCardGame.checkHardestCards();
                    break;
                case "reset stats":
                    flashCardGame.setErrorsToZero();
                    break;
                default:
                    gameLogger.logOutput("Invalid option. Try again...");
                    break;
            }
        }
    }
}