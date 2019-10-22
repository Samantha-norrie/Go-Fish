public class CardQueue{
  //Attributes
  private CardNode head;
  private CardNode tail;

  //Constructor
  public CardQueue(){
    head = null;
    tail = null;
  }

  //Add method
  public void add(CardNode toAdd){
    if(head == null){
      head = toAdd;
      tail = toAdd;
    }else{
      tail.setNext(toAdd);
      tail = tail.getNext();
    }
  }

  //Peek method
  public CardNode peek() throws QueueEmptyException{
    if(head == null){
      throw new QueueEmptyException();
    }else{
      return head;
    }
  }

  //remove method
  public CardNode remove() throws QueueEmptyException{
    if(head == null){
      throw new QueueEmptyException();
    }else{
      CardNode toReturn = head;
      head = head.getNext();
      return toReturn;
    }
  }
}
