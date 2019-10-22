//Import
import java.util.Scanner;
import java.util.ArrayList;
public class GamePlay{

  //Attributes
  private static CardNode[]  freshPile = makeFreshPile();
  private CardQueue deck;
  private Player[] players;
  private boolean privacyProtector;
  private int numOfBooks;


  //Constructor
  public GamePlay(){
    numOfBooks = 0;
    int playerCount = getNumOfPlayers();
    players = createPlayers(playerCount);

    //Make deck (shuffle, then add cards into the queue)
    CardNode[] toQueue = freshPile;
    CardNode[] shuffled = shuffle(toQueue);
    deck = putToQueue(shuffled);

    //Privacy protector initiation
    System.out.println("Would you like to turn on privacy protector? (lines will be printed so other players will not see your hand)");
    Scanner s = new Scanner(System.in);
    String answer = s.nextLine();
    privacyProtector = false;
    if(answer.equals("Yes") || answer.equals("YES") || answer.equals("yes") || answer.equals("Y") || answer.equals("y") || answer.equals("Ye") || answer.equals("ye") || answer.equals("YE")){
      privacyProtector = true;
    }
    start(playerCount);
  }


  //non-static methods

  //Method to start game
  private void start(int playerCount){
    System.out.println("Time to start! \nCards are now being distributed.");
    int handSize = 7;
    if(playerCount > 3){
      handSize = 5;
    }

    //Outer loop for hand size
    for(int i = 0; i < handSize; i++){

      //Inner loop to give each Player a cards
      for(int j = 0; j < players.length; j++){

        //try/catch thrown exception
        try{
          players[j].addToHand(deck.remove());
        }catch(QueueEmptyException e){
          System.out.println("No more cards in deck.");
          break;
        }
      }
    }

    //Show hand to each player, use Scanner as a way to ensure that the Player is ready
    System.out.println("Hands will now be shown to each player");
    for(int i = 0; i < players.length; i++){
      System.out.println("Player " + (i + 1) + ", are you ready?");
      Scanner s = new Scanner(System.in);
      String startConfirmation = s.nextLine();
      System.out.println("Your current hand is:\n \n" + players[i].printOut("display hand"));
      System.out.println("Ready to proceed?");
      String endConfirmation = s.nextLine();

      //Privacy protector if desired by players
      if(privacyProtector){
        privacyProtectorPrint();
      }
  }

    //Begin turns
    turn(0);
  }
  //Method for turns
  private void turn(int playerAt){
    int turnCounter = playerAt;

    //End game if all books have been declared
    while(true){
      if(numOfBooks == 13){
        endOfGame();
        return;
      }

      //Game options
      else{
        boolean goFish = false;
        while(goFish == false){
          System.out.println("It is " + players[turnCounter].toString() + "'s turn! Are you ready, " + players[turnCounter].toString() + "? (Press Enter)");

          //If Player's hand is empty
          if(players[turnCounter].getHand().size() == 0){
            System.out.println("You have no cards in hand! You can only draw a card this turn!");
            drawCard(players[turnCounter]);
            System.out.println("Current hand: \n" + players[turnCounter].printOut("display hand"));
            System.out.println("Current books: \n" + players[turnCounter].printOut("display books"));
            System.out.println("Please pass to next player, then click Enter");
            Scanner s = new Scanner(System.in);
            String endTurnConfirm = s.nextLine();

            //Privacy protector if desired by players
            if(privacyProtector){
              privacyProtectorPrint();
            }
            break;
          }

          //If Player's hand is not empty
          System.out.println("Current hand: \n" + players[turnCounter].printOut("display hand"));
          System.out.println("Current books: \n" + players[turnCounter].printOut("display books"));

          //Begin turn options
          System.out.println("What would you like to do?\n.Fish (f) \n.Declare a book (d) \n");
          Scanner s = new Scanner(System.in);
          String answer = s.nextLine();

          try{
            if(answer.equals("f") || answer.equals("F")){
              //Get desired Player to be asked
              System.out.println("Who would you like to ask? (Enter their name)");
              //Scanner s = new Scanner(System.in);
              String name = s.nextLine();
              Player toCheck = findPlayer(name);

              //If the current Player did not enter a valid Player, restart turn
              if(toCheck == null || toCheck.equals(players[turnCounter])){
                System.out.println("Invalid name entered.");
                turn(playerAt);
              }

              //If Player to be asked is found, get desired rank
              else{
                System.out.println("Which rank would you like to ask for?");
                String rank = s.nextLine();

                //Check if current Player can ask for desired rank
                boolean valid = players[turnCounter].haveNumber(rank);

                //if current Player cannot ask for desired rank, restart process
                if(!(valid)){
                  System.out.println("You cannot ask for " + rank);
                  turn(playerAt);
                }

                //Go fish
                goFish = fish(players[turnCounter], toCheck, rank);
              }
            }

            //To declare a rank
            else if(answer.equals("d") || answer.equals("D")){
              System.out.println("Which rank would you like to declare?");
              String declaredRank = s.nextLine();
              bookCheck(players[turnCounter], declaredRank);
            }else{
              throw new InvalidDeclarationException();
            }
          }catch(InvalidDeclarationException e){
              continue;
          }
        }

        //When Player's turn is over
        System.out.println("Please pass to next player, then click Enter");
        Scanner s = new Scanner(System.in);
        String endTurnConfirm = s.nextLine();

        //Privacy protector if desired by players
        if(privacyProtector){
          privacyProtectorPrint();
        }

        //Go to Player 1 if last player has played
        if(turnCounter == (players.length - 1)){
          turnCounter = 0;
        }

        //Else go to next Player
        else{
          turnCounter++;
        }
      }
    }
  }

  //Method to search for rank asked for from certain player
  private boolean fish(Player currentPlayer, Player name, String rank){

    //End game if all books have been declared
    if(numOfBooks == 13){
      return true;
    }

        boolean cardsGiven = getCards(currentPlayer, name, rank);

        //For if no cards are retrieved
        if(cardsGiven == false){
          System.out.println("Go Fish!");

          //Draw a card
          drawCard(currentPlayer);
          System.out.println("Current hand: \n" + currentPlayer.printOut("display hand"));
          System.out.println("Current books: \n" + currentPlayer.printOut("display books"));
          return true;
        }else{
          System.out.println("You get to ask again!");
          return false;
        }
    }

  //Method for end of game
  private void endOfGame(){
    System.out.println("The game has now ended");
    System.out.println("The winner(s) is/are " + getWinners() + "!");
  }

  //Method to make players
  private Player[] createPlayers(int playerCount){
    Player[] toReturn = new Player[playerCount];
    for(int i = 0; i < playerCount; i++){
      int playerNum = i + 1;
      System.out.println("What is the name of player " + playerNum + "?");
      Scanner s = new Scanner(System.in);
      String playerName = s.nextLine();
      toReturn[i] = new Player(playerName);
    }
    boolean repeat = false;
    for(int i = 0; i < playerCount; i++){
      repeat = findPlayer(toReturn, i);
      if(repeat == true){
        break;

      }
    }
    if(repeat == true){
      System.out.println("There is a name repeat. Naming process will restart. \n");
      createPlayers(playerCount);
    }
    return toReturn;
  }

  //Helper method for giving player a card from the top of the deck (go fish)
  private void drawCard(Player currentPlayer){
    try{
    //Get card from top of deck
    CardNode goFish = deck.remove();
    currentPlayer.getHand().add(goFish);
    }catch(QueueEmptyException e){
      System.out.println("No fish to go due to empty deck (<){");
    }
  }


  //Helper method for finding books in Player's hand
  private void bookCheck(Player currentPlayer, String rank){
    int howManyCards = 0;
    for(int i = 0; i < currentPlayer.getHand().size(); i++){
      if(currentPlayer.getHand().get(i) != null && ((CardNode)(currentPlayer.getHand().get(i))).getNumber().equals(rank)){
        howManyCards = howManyCards + 1;
      }
    }

    //If rank was incorrectly declared
    if(howManyCards < 4){
      System.out.println("Invalid declaration.");
      return;
    }

    //If correct, add rank to Player's books
    currentPlayer.addToBooks(rank);
    this.numOfBooks = this.numOfBooks + 1;
    currentPlayer.setNumOfBooks(currentPlayer.getNumOfBooks() + 1);

    //Remove all cards of declared rank from Player's hand
    for(int i = 0; i < currentPlayer.getHand().size();){
      if(currentPlayer.getHand().get(i) != null && ((CardNode)(currentPlayer.getHand().get(i))).getNumber().equals(rank)){
        currentPlayer.getHand().remove(i);
      }else{
        i++;
      }
    }
  }


  //Method to help with the retrieval of cards from the asked Player
  private boolean getCards(Player currentPlayer, Player toCheck, String rank){
    if(!(toCheck.haveNumber(rank))){
      return false;
    }
    for(int i = 0; i < toCheck.getHand().size();){
      if(toCheck.getHand().get(i) != null && ((CardNode)(toCheck.getHand().get(i))).getNumber().equals(rank)){
        currentPlayer.getHand().add(((CardNode)toCheck.getHand().get(i)));
        toCheck.getHand().remove(i);
      }else{
        i++;
      }
    }
    return true;
  }

  //Method to find if Plyer is valid
  private Player findPlayer(String name){
    for(int i = 0; i < players.length; i++){
      if(players[i].getName().equals(name)){
        return players[i];
      }
    }
    return null;
  }

  //Method to find if Player with same name already exists
  private boolean findPlayer(Player[] proposedPlayers, int playerPlacement){
    String playerName = proposedPlayers[playerPlacement].getName();
    for(int i = 0; i < proposedPlayers.length; i++){
      if(i == playerPlacement){
        continue;
      }else if(proposedPlayers[i].getName().equals(playerName)){
        return true;
      }
    }
    return false;
  }


  //Method to aid with hand privacy
  private void privacyProtectorPrint(){
    for(int i = 0; i < 40; i++){
      int selectString = (int)(Math.random()*7);
      if(selectString == 0){
        System.out.println("           }(oO)");
      }else if(selectString == 1){
        System.out.println("  }(oO)");
      }else if(selectString == 2){
        System.out.println("               (oO){  ");
      }else if(selectString == 3){
        System.out.println("                             }(Oo)");
      }else if(selectString == 4){
        System.out.println("                   }(oO)");
      }else if(selectString == 5){
        System.out.println("        (OO){  ");
      }else if(selectString == 6){
        System.out.println("                        }(oo)");
      }
    }
  }

  //Method to get number of players
  private int getNumOfPlayers(){
    System.out.println("How many players in your party? (Must be 2 to 6)");
    Scanner s = new Scanner(System.in);
    int numberOfPlayers = s.nextInt();
    try{

      //Try/catch to assure there are enough players/not too many
      if(numberOfPlayers > 6){
        throw new TooManyPlayersException();
      }else if(numberOfPlayers < 2){
        throw new TooLittlePlayersException();
      }
    }catch(TooManyPlayersException e){
      getNumOfPlayers();
    }catch(TooLittlePlayersException e){
      getNumOfPlayers();
    }
    return numberOfPlayers;
  }


  //Method to determine winner(s) of game
  private String getWinners(){
    String toReturn = "";
    Player winner = players[0];
    for(int i = 1; i < players.length; i++){
      if(winner.getNumOfBooks() < players[i].getNumOfBooks()){
        winner = players[i];
      }
    }

    //Second loop for if multiple winners
    toReturn = toReturn + winner.getName();
    for(int i = 0; i < players.length; i++){
      if(winner.getNumOfBooks() == players[i].getNumOfBooks() && !(players[i].equals(winner))){
        toReturn = toReturn + ", and " + players[i].getName();
      }
    }
    return toReturn;
  }



  //Method to put cards into a queue (deck) after initial shuffle
  private CardQueue putToQueue(CardNode[] beforeQueued){
    CardQueue toReturn = new CardQueue();
    for(int i = 0; i < beforeQueued.length; i++){
    toReturn.add(beforeQueued[i]);
    }
    return toReturn;
 }


  //Static methods


  //Method to make fresh pile of cards (not yet shuffled)
  public static CardNode[] makeFreshPile(){
    CardNode[] toReturn = new CardNode[52];
    String[] suit = {"Hearts","Diamonds", "Spades", "Clubs"};
    String[] rank = {"Ace", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Jack", "Queen", "King"};
    int addToPile = 0;
    for(int i = 0; i < suit.length; i++){
      for(int j = 0; j < rank.length; j++){
        toReturn[addToPile] = new CardNode(suit[i], rank[j]);
        addToPile++;
      }
    }
    return toReturn;
  }
  //Method to shuffle deck
  public static CardNode[] shuffle(CardNode[] toBeShuffled){

    //Generate number for how many times
    int shuffleTime = (int)(Math.random()*500 + 100);
    for(int i = 0; i <= shuffleTime; i++){

      //Places to switch
      int firstPlace = (int)(Math.random()*52);
      int secondPlace = (int)(Math.random()*52);

      //Switch cards
      CardNode placement= toBeShuffled[firstPlace];
      toBeShuffled[firstPlace] = toBeShuffled[secondPlace];
      toBeShuffled[secondPlace] = placement;
    }
    return toBeShuffled;
  }
}
