//Import
import java.util.ArrayList;

public class Player{
  private String name;
  private ArrayList<CardNode> hand;
  private ArrayList<String> books;
  private int numOfBooks;

  //Constructor
  public Player(String name){
    this.name = name;
    hand = new ArrayList<CardNode>();
    books = new ArrayList<String>();
    numOfBooks = 0;
  }

  //Get methods
  public String getName(){
    return name;
  }
  public ArrayList<CardNode> getHand(){
    return hand;
  }
  public ArrayList<String> getBooks(){
    return books;
  }
  public int getNumOfBooks(){
    return numOfBooks;
  }

  //Set methods
  public void setNumOfBooks(int newNum){
    numOfBooks = newNum;
  }

  //Method to add a card to Player's hand
  public void addToHand(CardNode cardToAdd){
    hand.add(cardToAdd);
  }

  //Method to add a book to a Player's book library
  public void addToBooks(String rank){
    books.add(rank);
  }

  //Method to check to see if player has a card of x number/rank
  public boolean haveNumber(String rank){
    for(int i = 0; i < hand.size(); i++){
      if(hand.get(i) != null && hand.get(i).getNumber().equals(rank)){
        return true;
      }
    }
    return false;
  }

  //Method for printing out a Player's hand and/or a Player's book library
  public String printOut(String command){
    String toReturn = "";

    //For hand
    if(command.equals("display hand")){
      for(int i = 0; i < hand.size(); i++){
        if(hand.get(i) != null){
          toReturn = toReturn + hand.get(i).toString();
        }
      }
    }
    //For books
    else if(command.equals("display books")){
      for(int i = 0;(i < books.size() && books.get(i) != null); i++){
        toReturn = toReturn + books.get(i) + "\n";
      }
    }
    return toReturn;
  }

  public String toString(){
    return "Player: " + name;
  }


}
