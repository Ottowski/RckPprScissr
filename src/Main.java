import java.util.Scanner;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

interface OpponentStrategy {
    String makeMove();
}

class SlumpisStrategy implements OpponentStrategy {
    @Override
    public String makeMove() {
        String[] moves = {"Rock", "Paper", "Scissor"};
        return moves[new Random().nextInt(moves.length)];
    }
}

class KlockisStrategy implements OpponentStrategy {
    @Override
    public String makeMove() {
        String[] moves = {"Rock", "Paper", "Scissor"};
        int randomIndex = new Random().nextInt(moves.length);
        return moves[randomIndex];
    }
}

class NamnisStrategy implements OpponentStrategy {
    private final String playerName;

    public NamnisStrategy(String playerName) {
        this.playerName = playerName;
    }

    @Override
    public String makeMove() {
        String[] moves = {"Rock", "Paper", "Scissor"};
        int playerNameHashCode = playerName.hashCode();
        int index = Math.abs(playerNameHashCode) % moves.length;
        return moves[index];
    }
}

class Game {
    private final OpponentStrategy opponentStrategy;
    private final String playerName;
    private int playerTotalWins = 0;
    private int opponentTotalWins = 0;
    private List<String> roundResults = new ArrayList<>();

    public Game(OpponentStrategy opponentStrategy, String playerName) {
        this.opponentStrategy = opponentStrategy;
        this.playerName = playerName;
    }

    public void play(int gameRounds, List<String> roundResults) {
        for (int game = 1; game <= gameRounds; game++) {
            String playerMove = getPlayerMove();
            String computerMove = opponentStrategy.makeMove();
            String roundResult = "Game " + game + ": " + playerName + " vs. Opponent - ";
            if (playerMove.equals(computerMove)) {
                roundResult += "Tie";
                System.out.println("Game " + game + " was a tie!");
            } else if ((playerMove.equals("Rock") && computerMove.equals("Scissor")) ||
                    (playerMove.equals("Paper") && computerMove.equals("Rock")) ||
                    (playerMove.equals("Scissor") && computerMove.equals("Paper"))) {
                roundResult += playerName + " wins";
                System.out.println("You won game round " + game + "! ;-)");
                playerTotalWins++;
            } else {
                roundResult += "Opponent wins";
                System.out.println("You lost game round " + game + "! :-(");
                opponentTotalWins++;
            }
            this.roundResults.add(roundResult);
        }
    }

    public List<String> getRoundResults() {
        return roundResults;
    }

    private String getPlayerMove() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose your move: Rock, Paper, or Scissor");
        String playerMove;
        while (true) {
            playerMove = scanner.nextLine();
            if (playerMove.equals("Rock") || playerMove.equals("Paper") || playerMove.equals("Scissor")) {
                break;
            } else {
                System.out.println("Invalid choice. Please choose Rock, Paper, or Scissor.");
            }
        }
        return playerMove;
    }

    public boolean didPlayerWin() {
        return playerTotalWins > opponentTotalWins;
    }

    public boolean didOpponentWin() {
        return opponentTotalWins > playerTotalWins;
    }

    public int getPlayerTotalWins() {
        return playerTotalWins;
    }

    public int getOpponentTotalWins() {
        return opponentTotalWins;
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to Rock, Paper, Scissors!");

        String playerName;

        while (true) {
            playerName = getPlayerName(scanner);

            if (!playerName.isEmpty()) {
                break;
            } else {
                System.out.println("Please enter your name.");
            }
        }

        String[] opponentTypes = {"Slumpis", "Klockis", "Namnis"};

        int playerGameScore = 0;
        int opponentGameScore = 0;

        List<String> matchHistory = new ArrayList<>();

        while (true) {
            int choice = showMainMenu(scanner);

            if (choice == 1) {
                int gameRounds = getGameRounds(scanner);
                String opponentType = chooseOpponent(scanner, opponentTypes);
                List<String> roundResults = new ArrayList<>();
                Game game = getGame(opponentType, playerName, gameRounds);
                game.play(gameRounds, roundResults);

                for (String result : roundResults) {
                    System.out.println(result);
                }

                if (game.didPlayerWin()) {
                    playerGameScore++;
                } else if (game.didOpponentWin()) {
                    opponentGameScore++;
                }

                String result = playerName + ": " + game.getPlayerTotalWins() + " vs. Opponent: " + game.getOpponentTotalWins();
                matchHistory.add(result);

                showGameResults(playerName, game);
            } else if (choice == 2) {
                showMatchHistory(matchHistory, playerName, playerGameScore, opponentGameScore);
            } else if (choice == 3) {
                break;
            }
        }
        scanner.close();
    }

    private static String getPlayerName(Scanner scanner) {
        System.out.print("Enter your name: ");
        String playerName = scanner.nextLine();
        if (playerName.isEmpty()) {
            System.out.println("Name not entered. Returning to the main menu.");
        }
        return playerName;
    }

    private static int showMainMenu(Scanner scanner) {
        System.out.println("Main Menu:");
        System.out.println("1. Start a new game");
        System.out.println("2. Show match history");
        System.out.println("3. Exit");
        int choice;
        while (true) {
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            if (choice >= 1 && choice <= 3) {
                break;
            } else {
                System.out.println("Invalid choice. Please choose 1, 2, or 3.");
            }
        }
        return choice;
    }

    private static int getGameRounds(Scanner scanner) {
        int gameRounds;
        while (true) {
            System.out.println("How many possible 'Best out of' game rounds would you like to play (1, 3, 5)?");
            gameRounds = scanner.nextInt();
            if (gameRounds == 1 || gameRounds == 3 || gameRounds == 5) {
                break;
            } else {
                System.out.println("Invalid choice. Please choose 'Best out of' 1, 3, or 5 games.");
            }
        }
        return gameRounds;
    }

    private static String chooseOpponent(Scanner scanner, String[] opponentTypes) {
        System.out.println("Choose your opponent");
        for (int i = 0; i < opponentTypes.length; i++) {
            System.out.println((i + 1) + ". " + opponentTypes[i]);
        }
        int choice;
        while (true) {
            System.out.print("Enter the number between 1-3 of your choice: ");
            choice = scanner.nextInt();
            if (choice >= 1 && choice <= opponentTypes.length) {
                break;
            } else {
                System.out.println("Invalid choice. Please choose a valid opponent.");
            }
        }
        return opponentTypes[choice - 1];
    }

    private static Game getGame(String opponentType, String playerName, int gameRounds) {
        OpponentStrategy opponentStrategy = switch (opponentType) {
            case "Slumpis" -> new SlumpisStrategy();
            case "Klockis" -> new KlockisStrategy();
            case "Namnis" -> new NamnisStrategy(playerName);
            default -> new SlumpisStrategy();
        };
        return new Game(opponentStrategy, playerName);
    }

    private static void showGameResults(String playerName, Game game) {
        System.out.println("----------------------------------------------------------");
        System.out.println("Match round result");
        System.out.println(playerName + ": " + game.getPlayerTotalWins() + " round(s).");
        System.out.println("Opponent: " + game.getOpponentTotalWins() + " round(s).");
        System.out.println("----------------------------------------------------------");
    }

    private static void showMatchHistory(List<String> matchHistory, String playerName, int playerGameScore, int opponentGameScore) {
        System.out.println("Match History:");
        System.out.println("----------------------------------------------------------");
        if (matchHistory.isEmpty()) {
            System.out.println("No match history available.");
        } else {
            for (String result : matchHistory) {
                System.out.println(result);

                if (result.startsWith(playerName)) {
                    Game game = new Game(new SlumpisStrategy(), playerName);
                    List<String> roundResults = game.getRoundResults();
                    for (String roundResult : roundResults) {
                        System.out.println(roundResult);
                    }
                }
            }
        }
        System.out.println("----------------------------------------------------------");

        System.out.println("Total match score:");
        System.out.println(playerName + " total match score: " + playerGameScore + " vs. Opponent total match score: " + opponentGameScore);
        System.out.println("----------------------------------------------------------");
    }
}
