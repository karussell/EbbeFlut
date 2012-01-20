/*
 * StartField.java
 *
 * Created on 12. September 2004, 11:30
 */

/**
 *
 * @author  peter
 */
public class StartField extends OwnerField 
{ private Card firstCard=null;
    
  /** create a new instance from a special startstack class, in this class it is 
   *  secure that only one card each move will placed
   */
  public StartField(int a, int b, Player player)
  { super(a,b,player);
  }
  
  /** after a push (push TO startstack is only possible from computer players) 
   *  it is also possible to pop
   */
  public void push(Card c)
  { firstCard=c;
     c.setPosition(x,y);//da diese Zeile fehlte war mal eine Exception
    //super.push(c);
  }
  
  public void pushInitial(Card c)
  { super.push(c);
  } 
  
  public Card peek()
  { return firstCard;
  }
  
  public Card pop()
  { Card tmp=firstCard;
     firstCard=null;
    return tmp;
  }  
  
  /** call this methode before player moves, so that he could pop the card in his
   *  next move
   */
  public void nextCard()
  { firstCard=super.pop();
  }
  
  protected Object getInstance(int x,int y,Player p)
  { StartField sf=new StartField(x,y,p);
    if(firstCard!=null) sf.firstCard=(Card)firstCard.clone();
    return sf;
  }
}
