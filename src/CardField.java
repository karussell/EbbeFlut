/***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************
 *
 * CardField.java
 *
 * Created on 8. Oktober 2004, 14:31
 */

import java.awt.event.*;
import java.awt.*;

/** shows the first card in its color, to layout the boardButtons
 *
 * @author  peter karich
 */
public class CardField extends Field
{ private Card first=null;
  
  /** Creates a new instance of CardField */
  public CardField(int a, int b) 
  { super(a,b);
  }
    
  public void push(Card karte)
  { super.push(karte);
    if(action==null) return; //this line is for faster calculation!!!  
    set1stCard(karte);
  }
  
  public Card pop()
  { Card tmp=super.pop();
    if(action==null) return tmp;
    
    if(tmp==null) set1stCard(null);
    else    set1stCard(super.peek());
    return tmp;
  }
    
  public String getLabel() 
  { if(first==null) return "";
    else return first.toString();
  }
    
  public void setLabel(String str)
  { if(action!=null)  
    { if(first==null) action.actionPerformed(new ActionEvent(this,Const.CardFieldID,str));    
      else            action.actionPerformed(new ActionEvent(this,Const.CardFieldID,str));    
    }
  }
    
  public void set1stCard(Card card)
  { first=card;
    
    if(first!=null) setLabel(first.toString());
    else setLabel("");
  }    
  
  public void repaint()   
  { set1stCard(first);  
  }
  
  public void setOwner(Player o1,Player o2)
  { Card tmp;
    
    //refresh owner (color) from all cards
    for(int i=0; i<cards.size(); i++)
    { tmp=(Card)cards.elementAt(i);
      if(tmp.getOwner().no==Const.NO_1) tmp.setOwner(o1);
      else                              tmp.setOwner(o2);
    }
    //refresh layout
    repaint();
  }
  
  public CardField getClone()
  { CardField tmp=new CardField(x,y);
    
    for(int i=0; i<cards.size(); i++)
      tmp.cards.push(((Card)cards.elementAt(i)).clone());
    return tmp;
  }  
  
  /** @return the color of the first card, if first==null return null;
   */
  public Color getColor()
  { if(first==null) return null;
    return first.getColor();    
  }
   
  public Panel getStackPanel()
  {  Panel cardPanel = new Panel();
     int MAX=5;
    cardPanel.setLayout(new GridLayout(MAX,1));    
    boolean hide=false;
    Card tmpCard;
    Button button;
    
    for(int i=cards.size()-1; i>= (cards.size()-MAX) && i>=0; i--)
    { tmpCard=(Card)cards.elementAt(i);
      button=new Button();
      GraphicBoard.setGraphics(button,tmpCard.getColor(),"?");
      if(!hide) 
      { button.setLabel(tmpCard.toString());        
        hide=true;
      }
      cardPanel.add(button);      
    }
    return cardPanel;
  }
}