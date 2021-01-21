/*
* This program is a game of Black Jack for the Java console on AWS Cloud9.
*
* @author  Jacob Bonner
* @version 1.0
* @since   2021-01-18
*/

import java.util.Scanner;  // Import the Scanner class

/**
 * This class makes use of objects to create a game of black jack.
 */
public class Main {
  /**
   * This function clears the screen.
   */
  static void clearScreen() {
    System.out.print("\033[H\033[2J");
    System.out.flush();
  }

  /**
   * This function figures out if a player has gone bust.
   */
  static Boolean isBust(CardHand bustHand) {
    // Checking if the player's hand amount is over 21
    if (bustHand.getHandValue() > 21) {
      // Returning the player's hand is valid
      return true;
    } else {
      // Returning the player's hand is valid
      return false;
    }
  }

  /**
   * This function figures out a winner between the player and the dealer.
   */
  static int findWinner(CardHand playerSet, CardHand dealerSet) {
    // Checking if the player's hand is higher than the dealer's hand
    if (playerSet.getHandValue() > dealerSet.getHandValue()) {
      return 0;

      // Checking if the dealer's hand is greater than the player's hand
    } else if (playerSet.getHandValue() < dealerSet.getHandValue()) {
      return 1;

      // Returning that there was a tie
    } else {
      return 2;
    }
  }

  /**
   * This function pits the player's hand against the dealer's hand.
   */
  static int stand(CardHand userSet, CardHand cpuSet, DeckOfCards standDeck,
                   int money) {
    // Drawing cards for the dealer
    while (cpuSet.getHandValue() < 17) {
      cpuSet = hit(cpuSet, standDeck, cpuSet.amountOfCards());
    }

    try {
      // Printing info about the dealer's hand and the card deck
      System.out.print("Dealer's Hand");
      System.out.print("       Total: " + cpuSet.getHandValue());
      System.out.println("       Cards in Deck: " + standDeck.numberOfCards());

      // Printing out the dealer's final hand
      for (int cpuCounter = 0; cpuCounter < cpuSet.amountOfCards();
           cpuCounter++) {
        System.out.print(cpuSet.showCardFace(cpuCounter));
        if (cpuCounter == cpuSet.amountOfCards() - 1) {
          continue;
        } else {
          System.out.print(" | ");
        }
      }
      System.out.println("");
      System.out.println("");

      // Printing info about the player's hand and how much money they have
      System.out.print("Player's Hand");
      System.out.print("       Total: " + userSet.getHandValue());
      System.out.println("       Money: $" + money);

      // Printing out the player's final hand
      for (int userCounter = 0; userCounter < userSet.amountOfCards();
           userCounter++) {
        System.out.print(userSet.showCardFace(userCounter));
        if (userCounter == userSet.amountOfCards() - 1) {
          continue;
        } else {
          System.out.print(" | ");
        }
      }

      // Waiting for two seconds before doing anything else
      Thread.sleep(2000);

      // Checking to see if the dealer went bust
      if (isBust(cpuSet)) {
        // Returning that the dealer went bust and awarding money accordingly
        System.out.println("");
        System.out.println("");
        System.out.println("Dealer Went Bust. You Won! You gained $10.");
        Thread.sleep(2000);
        money = money + 10;
        return money;
      } else {
        // Figuring out which player won the game
        int winValue = findWinner(userSet, cpuSet);

        // Printing out the outcome of the round and awarding money accordingly
        switch (winValue) {
          case 0:
            // Case when the player wins
            System.out.println("");
            System.out.println("");
            System.out.println("You Won! You gained $10.");
            money = money + 10;
            break;

          case 1:
            // Case when the dealer wins
            System.out.println("");
            System.out.println("");
            System.out.println("You Lost! You lost $10.");
            money = money - 10;
            break;

          case 2:
            // Case when a tie occurs
            System.out.println("");
            System.out.println("");
            System.out.println("You Tied! You keep your current money.");
            break;

          default:
            // No winner was determined
            System.out.println("");
            System.out.println("");
            System.out.println("Unable to determine winner.");
            break;
        }
      }

      // Waiting for three seconds before doing anything else
      Thread.sleep(3000);

      // Catching any errors that occurred
    } catch (InterruptedException e) {
      System.out.println("ERROR: Unable to complete stand");
    }

    // Returning the player's new amount of money
    return money;
  }

  /**
   * This function allows a player to draw a card.
   */
  static CardHand hit(CardHand drawHand, DeckOfCards drawDeck, int handSize) {
    // Checking if the hand size is too large to draw a card
    if (handSize > 6) {
      // Returning the current hand
      return drawHand;

    } else {
      // Drawing a new card
      PlayingCard newCard = new PlayingCard(drawDeck.drawCard(),
                                            drawHand.getHandValue());

      // Adding the new card to the hand
      drawHand.addCard(newCard);

      // Returning the card hand
      return drawHand;
    }
  }

  /**
   * This function prints a help screen for the user.
   */
  static void helpScreen() {
    // Creating a scanner to receive the input to end the help screen
    final Scanner helpInput = new Scanner(System.in);

    // Clearing the screen
    clearScreen();

    // Printing text that helps the player understand how the game works
    System.out.println("Rules:");
    System.out.println("-Your goal: get more points than the dealer without "
                       + "going over 21!");
    System.out.println("-Maximum 6 cards each");
    System.out.println("-Type 'Hit' for another card");
    System.out.println("-Type 'Stand' to end your turn and put your hand "
                       + "against the dealer's");
    System.out.println("-Type 'CashOut' to quit early with the money you have");
    System.out.println("-Aces can be worth 1 or 11 depending on when they are "
                       + "dealt");
    System.out.println("-Face cards are all worth 10");
    System.out.println("-You start with $50 and play until you have $0 or cash "
                       + "out");
    System.out.println("-You gain $10 for a win and lose $10 for a loss");
    System.out.println("-Good luck, have fun!");
    System.out.println("");
    System.out.flush();

    // Receiving user input about ending help screen
    System.out.print("Press ‘ENTER’ when you wish to continue playing: ");
    String endHelpInput = helpInput.nextLine();
  }

  /**
   * This function deals cards to the two players.
   */
  static CardHand[] dealCards(DeckOfCards cardDeck) {
    // Instantiating card hands for each player
    CardHand userCardHand = new CardHand();
    CardHand cpuCardHand = new CardHand();

    // Drawing the first two cards for the player's and dealer's hand
    PlayingCard userCard1 = new PlayingCard(cardDeck.drawCard(), 0);
    userCardHand.addCard(userCard1);
    PlayingCard cpuCard1 = new PlayingCard(cardDeck.drawCard(), 0);
    cpuCardHand.addCard(cpuCard1);

    // Drawing the second two cards for the player's and dealer's hand
    PlayingCard userCard2 = new PlayingCard(cardDeck.drawCard(),
                                            userCardHand.getHandValue());
    userCardHand.addCard(userCard2);
    PlayingCard cpuCard2 = new PlayingCard(cardDeck.drawCard(),
                                           userCardHand.getHandValue());
    cpuCardHand.addCard(cpuCard2);

    // Creating a list to return each hand from the function
    CardHand[] handList = new CardHand[2];

    // Adding the hands to an array to pass out of this function
    handList[0] = userCardHand;
    handList[1] = cpuCardHand;

    // Returning the list with the hands
    return handList;
  }

  /**
   * This function runs a game of black jack.
   */
  public static void main(String[] args) {
    try {
      // Creating scanners to accept the starting user input
      final Scanner beginInput = new Scanner(System.in);
      final Scanner gameInput = new Scanner(System.in);

      // Printing title text
      System.out.println("♠♥♦♣ Welcome to Black Jack! ♣♦♥♠");
      System.out.println("");

      // Input that starts the game
      System.out.print("Press 'ENTER' to Play: ");
      String startInput = beginInput.nextLine();
      System.out.println("");
      System.out.println("");

      // Instantiating a deck of cards
      DeckOfCards deck = new DeckOfCards();

      // Dealing each player a hand of cards
      CardHand[] handInfo = dealCards(deck);
      CardHand playerHand = handInfo[0];
      CardHand dealerHand = handInfo[1];

      // Clearing the screen
      clearScreen();

      // Setting up the player's initial monetary amount
      int playerMoney = 50;

      // Game loop
      while (true) {
        // Printing info about the dealer's hand and the card deck
        System.out.print("Dealer's Hand");
        System.out.print("       Total: " + dealerHand.showCardValue(1));
        System.out.println("       Cards in Deck: " + deck.numberOfCards());

        // Printing the hand of the dealer
        System.out.println("?? | " + dealerHand.showCardFace(1));
        System.out.println("");

        // Printing info about the player's hand and how much money they have
        System.out.print("Player's Hand");
        System.out.print("       Total: " + playerHand.getHandValue());
        System.out.println("       Money: $" + playerMoney);

        // Printing the hand of the player
        for (int playerCounter = 0; playerCounter < playerHand.amountOfCards();
             playerCounter++) {
          System.out.print(playerHand.showCardFace(playerCounter));
          if (playerCounter == playerHand.amountOfCards() - 1) {
            continue;
          } else {
            System.out.print(" | ");
          }
        }
        System.out.println("");
        System.out.println("");

        // Checking to see if the player went bust
        if (isBust(playerHand)) {
          // Printing that the player went bust and removing appropriate money
          System.out.println("You Went Bust! You lose $10");
          playerMoney = playerMoney - 10;
          Thread.sleep(2000);
          clearScreen();
        } else {

          // Receiving input for the move the player would like to make
          System.out.print("What would you like to do "
                           + "(Hit/Stand/Help/CashOut): ");
          String userInputLowerCase = gameInput.nextLine();
          String userInput = userInputLowerCase.toUpperCase();

          // Checking to see which input was entered
          if (userInput.equals("HELP")) {
            // Calling the help screen function
            helpScreen();

          } else if (userInput.equals("HIT")) {
            // Checking to see if the player can draw another card
            if (playerHand.amountOfCards() > 5) {
              // Printing that the user cannot draw anymore cards
              System.out.println("");
              System.out.println("You cannot draw anymore cards");
              Thread.sleep(2000);
          } else {
            // Drawing a card
            playerHand = hit(playerHand, deck, playerHand.amountOfCards());
          }

          } else if (userInput.equals("STAND")) {
            // Calling the stand function to find a winner
            clearScreen();
            playerMoney = stand(playerHand, dealerHand, deck, playerMoney);
          }

          // Clearing the screen again
          clearScreen();
        }
      }

      // Catches and tells the user what error occured
    } catch (NullPointerException e) {
      System.out.println("");
      System.out.println("ERROR: Card could not be created");
    } catch (Exception e) {
      System.out.println("");
      System.out.println("ERROR: Invalid Input");
    }
  }
}
