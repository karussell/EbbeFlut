/***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************
 *
 * OwnerField.java
 *
 * Created on 8. Oktober 2004, 14:30
 */

import java.awt.Color;
import java.awt.event.ActionEvent;

/** a graphical field: shows always the size in the owners color
 *
 * @author  peter
 */
public class OwnerField extends Field
{ Player owner;
  
  public OwnerField(int a, int b, Player player)
  { super(a,b);
    if(player==null) return;
    owner=player;    
  }
  
  public void setOwner(Player pl)
  { owner=pl;
    Card tmp;
    
    //refresh owner from all
    for(int i=0; i<cards.size(); i++)
    { ((Card)cards.elementAt(i)).setOwner(owner);
    }
    //refresh layout
    setLabel(getLabel());
  }

  public void push(Card card)
  { card.setOwner(owner);
    super.push(card);
    setLabel(getLabel());
  }
  
  public void setLabel(String str)
  { if(action!=null) action.actionPerformed(new ActionEvent(this,Const.OwnerFieldID,str));
  }  
  
  public Card pop()
  { Card tmp=super.pop();
    setLabel(getLabel());
    return tmp;
  }
  
  public String getLabel() 
  { return getSize()+"";
  }  
  
  protected Object getInstance(int x,int y,Player p)
  { return new OwnerField(x,y,p);
  }
  
  public Object getClone(Player newOwner)
  { OwnerField tmp=(OwnerField)getInstance(x,y,newOwner);
    Card card;
    
    for(int i=0; i<cards.size(); i++)//Iterator iter=cards.iterator(); iter.hasNext();)
    { card=(Card)((Card)cards.elementAt(i)).clone();
      card.setOwner(newOwner);
      tmp.cards.push(card);
    }
    return tmp;
  } 
  
  public Color getColor() 
  { return owner.getColor();
  }  
}
