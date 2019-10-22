public class CardNode{

  //Attributes
  private String suit;
  private String number;
  private CardNode nextCard;

  //Constructor
  public CardNode(String suit, String number){
    this.suit = suit;
    this.number = number;
    nextCard = null;
  }

  //Get next CardNode
  public CardNode getNext(){
    return nextCard;
  }
  public String getSuit(){
    return suit;
  }
  public String getNumber(){
    return number;
  }

  //Set next CardNode
  public void setNext(CardNode nextCard){
    this.nextCard = nextCard;
  }

  //toString method
  public String toString(){
    String toReturn = number + " of " + suit + "\n";
    return toReturn;
  }

}
