import java.util.Scanner;
import java.util.Random;
import java.util.InputMismatchException;

/**
 * AmericanRouletteSimulator
 * Description: A console-based American Roulette game featuring a bankroll system,
 * color bets (red/black with green house edge), straight number bets (0-36, 00),
 * and exception handling to prevent crashes from invalid user inputs.
 */
public class AmericanRouletteSimulator {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        double bankroll = 100.00;
        boolean keepPlaying = true;

        System.out.println("========================================");
        System.out.println("       AMERICAN ROULETTE SIMULATOR      ");
        System.out.println("========================================");

        // Main game loop runs while player has money and wants to continue
        while (keepPlaying && bankroll > 0) {
            System.out.printf("\nCurrent Bankroll: $%.2f\n", bankroll);
            System.out.println("1. Color Bet (Red or Black) - Pays 1:1 (Green 0/00 = House Wins)");
            System.out.println("2. Straight Number Bet (0 to 36, or 37 for 00) - Pays 35:1");

            int betType = 0;

            // Exception handling loop for menu choice selection
            while (true) {
                try {
                    System.out.print("Enter choice (1 or 2): ");
                    betType = scanner.nextInt();
                    if (betType == 1 || betType == 2) {
                        break;
                    } else {
                        System.out.println("Error: Please enter either 1 or 2.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Error: Invalid input. Please enter a whole number.");
                    scanner.next(); // Clear invalid token from scanner buffer
                }
            }

            // Exception handling loop for wager amount
            double wager = 0.0;
            while (true) {
                try {
                    System.out.print("Enter wager amount ($): ");
                    wager = scanner.nextDouble();

                    if (wager <= 0) {
                        System.out.println("Error: Wager must be greater than $0.00.");
                    } else if (wager > bankroll) {
                        System.out.println("Error: You cannot wager more than your current bankroll.");
                    } else {
                        break;
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Error: Invalid amount. Please enter a valid numerical value.");
                    scanner.next(); // Clear invalid token
                }
            }

            // Spin the American roulette wheel (0 to 37, where 37 represents 00)
            int spin = random.nextInt(38);
            String spinColor = "";

            // Determine color using conditional logic
            if (spin == 0 || spin == 37) {
                spinColor = "green"; // House edge pockets
            } else if (spin % 2 == 0) {
                spinColor = "black";
            } else {
                spinColor = "red";
            }

            // Format display output for 00 instead of index 37
            String displaySpin = (spin == 37) ? "00" : String.valueOf(spin);

            if (betType == 1) {
                // --- COLOR BET LOGIC ---
                scanner.nextLine(); // Consume leftover newline character
                String userColor = "";

                while (true) {
                    System.out.print("Choose color to bet on (red or black): ");
                    userColor = scanner.nextLine().trim().toLowerCase();
                    if (userColor.equals("red") || userColor.equals("black")) {
                        break;
                    }
                    System.out.println("Error: Invalid choice. Type 'red' or 'black'.");
                }

                System.out.println("\n[SPIN RESULT] Wheel landed on: " + displaySpin + " (" + spinColor.toUpperCase() + ")");

                // Evaluate color match win, loss, or house edge (green) win
                if (spinColor.equals("green")) {
                    bankroll -= wager;
                    System.out.printf("HOUSE WINS! The wheel landed on Green (%s). You lost your $%.2f wager.\n", displaySpin, wager);
                } else if (userColor.equals(spinColor)) {
                    bankroll += wager;
                    System.out.printf("CONGRATULATIONS! You won $%.2f!\n", wager);
                } else {
                    bankroll -= wager;
                    System.out.printf("OUCH! You lost your $%.2f wager.\n", wager);
                }

            } else {
                // --- STRAIGHT NUMBER BET LOGIC ---
                int userNum = -1;

                while (true) {
                    try {
                        System.out.print("Choose a number (0 to 36, or enter 37 for 00): ");
                        userNum = scanner.nextInt();
                        if (userNum >= 0 && userNum <= 37) {
                            break;
                        }
                        System.out.println("Error: Number must be between 0 and 37.");
                    } catch (InputMismatchException e) {
                        System.out.println("Error: Invalid entry. Please enter an integer.");
                        scanner.next();
                    }
                }

                System.out.println("\n[SPIN RESULT] Wheel landed on pocket: " + displaySpin);

                // Evaluate straight number win or loss (35:1 payout, including hitting 0 or 00!)
                if (userNum == spin) {
                    double win = wager * 35.0;
                    bankroll += win;
                    System.out.printf("JACKPOT! Direct hit! You won $%.2f!\n", win);
                } else {
                    bankroll -= wager;
                    System.out.printf("NO LUCK! You lost your $%.2f wager.\n", wager);
                }

                scanner.nextLine(); // Consume leftover newline after scanner.nextInt()
            }

            // Show updated bankroll immediately after the round results
            System.out.printf(">>> Updated Bankroll: $%.2f <<<\n", bankroll);

            // Check if player has run out of funds
            if (bankroll <= 0) {
                System.out.println("\n>>> Your bankroll has hit $0.00. Game Over! <<<\n");
                keepPlaying = false;
            } else {
                System.out.print("\nWould you like to play another round? (yes/no): ");
                String ans = scanner.nextLine().trim().toLowerCase();
                // Only continue if the player explicitly types yes or y
                if (!(ans.equals("yes") || ans.equals("y"))) {
                    keepPlaying = false;
                }
            }
        }

        System.out.printf("\n========================================\n");
        System.out.printf("   GAME OVER - Final Bankroll: $%.2f    \n", bankroll);
        System.out.printf("========================================\n");

        scanner.close();
    }
}