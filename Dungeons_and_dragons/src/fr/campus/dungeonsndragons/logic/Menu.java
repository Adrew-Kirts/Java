package fr.campus.dungeonsndragons.logic;

import java.util.Arrays;
import java.util.Scanner;

import fr.campus.dungeonsndragons.board.EnemySquare;
import fr.campus.dungeonsndragons.board.Square;
import fr.campus.dungeonsndragons.db.DatabaseConnection;
import fr.campus.dungeonsndragons.npc.Enemy;
import fr.campus.dungeonsndragons.players.Hero;

/**
 * The Menu class handles various interactions with the user during the Dungeons and Dragons game.
 * It includes methods for starting the game, continuing, restarting, and handling hero creation.
 */
public class Menu {

    Game mainGame;
    Hero hero;
    Artwork artwork = new Artwork();
    private DatabaseConnection dbconnect = new DatabaseConnection();


    /**
     * Prompts the user to start the game or quit.
     *
     * @return true if the user chooses to start the game, false if they choose to quit.
     */
    public boolean startGame() {
        boolean choiceMade = false;
        Scanner myObj = new Scanner(System.in);
        String choice;

        while (!choiceMade) {
            System.out.println("\n          1. Start game");
            System.out.println("            2. Quit");
            choice = myObj.nextLine().toLowerCase();
            if (choice.equals("1")) {
                return true;
            } else if (choice.equals("2")) {
                System.out.println("Maybe next time!");
                System.exit(0);
                return false;
            } else {
                System.out.println("Please give a valid choice\n");
            }
        }
        return false;
    }

    /**
     * Prompts the user to continue the game by pressing enter.
     *
     * @return true if the user presses enter to continue, false otherwise.
     */
    public boolean continueGame() {
        System.out.println("\nPress enter to continue...");
        Scanner myObj = new Scanner(System.in);
        String choice;
        choice = myObj.nextLine().toLowerCase();
        if (choice.equals("")) {
            return true;
        }
        return false;
    }

    public boolean restartGame() {
        boolean choiceMade = false;
        Scanner myObj = new Scanner(System.in);
        String choice;

        while (!choiceMade) {
            artwork.openingMenu();
            System.out.println("\nWhat would you like to do now?");
            System.out.println("     1. Start a new game");
            System.out.println("          2. Quit");
            choice = myObj.nextLine().toLowerCase();
            if (choice.equals("1")) {
                return true;
            } else if (choice.equals("2")) {
                System.out.println("See you next time!");
                System.exit(0);
                return false;
            } else {
                System.out.println("Please give a valid choice\n");
            }
        }
        return false;
    }

    public boolean heroCreation() {
        boolean choiceMade = false;
        Scanner myObj = new Scanner(System.in);
        String choice;

        while (!choiceMade) {
            artwork.openingMenu();
            System.out.println("       1. Create character");
            System.out.println("            2. Quit");

            choice = myObj.nextLine().toLowerCase();
            if (choice.equals("1")) {
                return true;
            } else if (choice.equals("2")) {
                System.out.println("Maybe next time!");
                System.exit(0);
                return false;
            } else {
                System.out.println("Please give a valid choice\n");
            }
        }
        return false;
    }

    public String heroNameInput() {
        Scanner myObj = new Scanner(System.in);
        System.out.println("Enter the name of your hero: ");
        String heroName = myObj.nextLine();

        return heroName;
    }

    public String heroTypeInput() {

        Scanner myObj = new Scanner(System.in);

        System.out.println("What type is your hero? [\"warrior\" or \"wizard\"]\n(or press \"q\" to quit)");
        boolean choiceMade = false;
        String heroType = null;

        heroType = myObj.nextLine().toLowerCase();
        if (heroType.equals("warrior")) {
            choiceMade = true;
            return heroType;
        } else if (heroType.equals("wizard")) {
            choiceMade = true;
            return heroType;
        } else if (heroType.equals("q")) {
            System.out.println("See you next time!");
            System.exit(0);
        } else {
            System.out.println("Please give a valid choice\n");
            heroTypeInput();
        }
        return heroType;
    }

    public boolean movePlayer() {
        Scanner myObj = new Scanner(System.in);
        String choice;
        String coward;

        System.out.println("\nxxxxxxxxxxxxxxxxxxxxxxxxxx");
        System.out.println("x     Throw dice [d]     x");
        System.out.println("x Show player status [s] x");
        System.out.println("x     Quit game [q]      x");
        System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxx");
        choice = myObj.nextLine().toLowerCase();
        if (choice.equals("d")) {
            return true;
        } else if (choice.equals("s")) {
            System.out.println(hero);
            dbconnect.getLastHero();
            return false;
        } else if (choice.equals("q")) {
            System.out.println("Frightened little one...");
            System.out.println("Do you want to quit the dungeon");
            System.out.println("[y or n]");
            coward = myObj.nextLine().toLowerCase();
            if (coward.equals("y")) {
                System.out.println("You get killed while leaving the dungeon");
                System.exit(0);
            } else if (coward.equals("n")) {
                return false;
            }
        } else {
            System.out.println("Please type in \"d\", \"s\", or \"q\"\n");
        }

        return false;
    }

    public void givePosition() {
        int playerPosition = this.mainGame.getPosition();

        if (playerPosition >= 0 && playerPosition < this.mainGame.getGameBoard().getGameboard().size()) {
            Square square = this.mainGame.getGameBoard().getGameboard().get(playerPosition);
            if (square instanceof EnemySquare) {
                EnemySquare enemySquare = (EnemySquare) square;
                Enemy enemy = enemySquare.getEnemy();

                String enemyType = enemy.getType();
                int enemyLifePoints = enemy.getLifePoints();
                int enemyAttackPower = enemy.getAttackPower();

                if (enemyLifePoints <= 0){
                    System.out.println("\nYou killed the " + enemyType + " !");
                }
                else {
                    System.out.println("Player is on square " + playerPosition + " out of " + this.mainGame.array.length);
                    System.out.println("-------------------------------");
                    System.out.println("\nA " + enemyType + " appeared!");
                    System.out.println("HP: " + enemyLifePoints);
                    System.out.println("ATK: " + enemyAttackPower);
                }

                if (enemy.getLifePoints() > 0 && fightOrFlight()){
                    System.out.println("You attack the "+enemyType+" with a total ATK of "+hero.getTotalAttackLevel()+ "...");

                    enemy.setLifePoints((enemy.getLifePoints()-hero.getTotalAttackLevel()));

                    if (enemy.getLifePoints() > 0){
                        //attack hero function
                        int enemyATK = enemy.getAttackPower();
                        System.out.println("The " + enemyType + " attacks you with " + enemyATK + "ATK");
                        hero.setLifePoints((hero.getLifePoints()-enemyATK));
                        dbconnect.setCurrentHeroHP(hero);
//                        TODO: delete this:
                        System.out.println("Hero HP: " + hero.getLifePoints());
                    }

                    //for testing purposes:
                    givePosition();
                }

            } else {
                System.out.println("Player is on square " + playerPosition + " out of " + this.mainGame.array.length);
                System.out.println("-------------------------------");
                System.out.println("No enemies here...");
            }
        }
    }

    public boolean fightOrFlight() {
        Scanner myObj = new Scanner(System.in);
        String choice;
        String coward;

        System.out.println("\nxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        System.out.println("x        Fight [f]        x");
        System.out.println("x         Run [r]         x");
        System.out.println("x Show player status [s]  x");
        System.out.println("x      Quit game [q]      x");
        System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxx");
        choice = myObj.nextLine().toLowerCase();
        if (choice.equals("f")) {
            return true;
        } else if (choice.equals("r")) {
            System.out.println("You ran away! Let's roll how much you moved back");
            int run = mainGame.throwDice();
            mainGame.setPosition(mainGame.getPosition()-run);
            givePosition();
        } else if (choice.equals("s")) {
            System.out.println(hero);
            dbconnect.getLastHero();
            fightOrFlight();
        } else if (choice.equals("q")) {
            System.out.println("Frightened little one...");
            System.out.println("Do you want to quit the dungeon");
            System.out.println("[y or n]");
            coward = myObj.nextLine().toLowerCase();
            if (coward.equals("y")) {
                System.out.println("You get killed while leaving the dungeon");
                System.exit(0);
            } else if (coward.equals("n")) {
                fightOrFlight();
            }
        } else {
            System.out.println("Please type in \"f\", \"r\", \"s\", or \"q\"\n");
            fightOrFlight();
        }
        return false;
    }


    public void showDice(int rolledDice) {
        System.out.println(Arrays.toString(artwork.diceArray[rolledDice]));
        System.out.println(" --------------------");
        System.out.println("|   You rolled a: " + rolledDice + "  |");
        System.out.println(" --------------------");

    }

    public boolean randomChestChoice() {
        boolean choiceMade = false;
        Scanner myObj = new Scanner(System.in);
        String choice;

        while (!choiceMade) {
            System.out.println("\nWould you like to roll for a random chest?");
            artwork.showChest();
            System.out.println("\nYes [y] or [n]");

            choice = myObj.nextLine().toLowerCase();
            if (choice.equals("y")) {
                return true;
            } else if (choice.equals("n")) {
                System.out.println("\nI applaud your braveness " + hero.getType() + " !");
                return false;
            } else {
                System.out.println("Please give a valid choice\n");
            }
        }
        return false;
    }

    public int enemyCountChoice() {

        boolean choiceMade = false;
        Scanner myObj = new Scanner(System.in);
        int choice = 1;

        while (!choiceMade) {
            System.out.println("Choose how many enemies you want to combat: ");

            choice = Integer.parseInt(myObj.nextLine());
            if (choice < 64) {
                return choice;
            } else {
                System.out.println("Please give a valid choice\n");
            }
        }
        return choice;
    }


    public Menu(Game game) {
        this.mainGame = game;
    }


}
