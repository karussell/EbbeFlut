/***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************
 */

/** represent a card; 
 *  @author  peter karich
 */
public class Card implements Cloneable
{ char c;
  int i;
  /**position is neccessary for the class "Move"
   */
  private int x,y;
  /** useful for the getColor routine
   */
  private Player owner;
  
  public Card(char z, int n, Player player)
  { owner=player;
    if(n<1 || n>5)
       throw new IllegalArgumentException("zeichen oder zahl nicht im bereich a-e bzw. 1-5 throw it in new Card()");     
    c=z;
    i=n;
  }

  public void setPosition(int a,int b)
  { x=a;
    y=b;      
  }
  
  public java.awt.Color getColor()
  { return owner.getColor();
  }
  
  public int getX()  
  { return x;  
  }
  
  public int getY()  
  { return y;  
  }
  
  public Player getOwner()
  { return owner;
  }
  
  public void setOwner(Player owner)
  { this.owner=owner;
  }
  
  public String toString()
  { if(c==' ') return "";    
     String tmp=""+c+i;
    if(owner.no==Const.NO_1) tmp=tmp.toUpperCase();
    return tmp;
  }
  
  /** it is not an identity equal, it is "so equal" that this card could be moved
   */
  public boolean equals(Object obj)
  { Card tmp=(Card)obj;
    
    return tmp.getOwner().no==owner.no && (tmp.c==c || tmp.i == i);
  }  
  
  public Object clone()
  { Card tmp=new Card(c,i,owner);
    tmp.setPosition(x,y);
    return tmp;
  }
}