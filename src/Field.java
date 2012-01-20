/***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************
 *
 * Field.java
 *
 * Created on 8. Oktober 2004, 14:30
 */

import java.awt.event.ActionListener;
import java.awt.Color;

/** a little stack implementation with the position of the field
 *  and for graphical representation let fire information to the ActionListener
 * @author  peter
 */
abstract public class Field
{ protected int x,y;
  protected Stack cards=new Stack(25,5);
  ActionListener action;
  
  public Field(int a, int b)
  { x=a;
    y=b;    
  }
  
  public void push(Card karte)
  { karte.setPosition(x,y);
    cards.push(karte);
  }

  public Card pop()
  {return (Card)cards.pop();
  }

  public Card peek()
  { return (Card)cards.peek();
  }
  
  public int getSize()
  { return cards.size();
  }    
  
  public boolean empty()
  { return cards.empty();
  }
  
  public void clear()
  { cards.removeAllElements();
  }
  
  //now graphical routines section
  //---------------------------------------------------
    
  /** actionListener is better than using the buttons as variables in this class directly
   */
  public void setActionListener(ActionListener a)
  { action=a;
  }
  
  public ActionListener getActionListener()
  { return action;
  }
  
  abstract public String getLabel();
  abstract public void setLabel(String str);
  abstract public Color getColor();
   
  public String toString()
  { return getLabel();
  }

  public String getContentsAsString()
  {   StringBuffer sb=new StringBuffer();

     for(int i=cards.size()-1; i>=0;i--)
     {  sb.append(((Card)cards.elementAt(i)).toString()+" , ");      
     }

     return sb.toString();
  }
}