/***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************
 */

import java.util.Vector;

/**
 *   the board looks like 00 01 02 .. 05;
 *                        10 11 12 ..;
 *                        ...........;
 *  so you get "yx" with 0<y<5;0<x<5;
 *  use THIS class for new computer oponents, instead of GraphicBoard
 *  @author  peter karich
 */
public class Board
{ 
  protected OwnerField finishStack1, removedCards1, finishStack2, removedCards2;
  protected StartField startStack1, startStack2;
  protected CardField brett[][];  
  protected Player player1,player2;
  
  static private String startCards[]={"a1","a2","a3","a4","a5",
			"b1","b2","b3","b4","b5",
			"c1","c2","c3","c4","c5",
			"d1","d2","d3","d4","d5",
			"e1","e2","e3","e4","e5"};
  //DO NOT USE STATIC VARIABLES, IF YOU WANT TO USE THEM IN GETCLONE
  
  /**  After "new Board()" you have to make sure, that you set Move.setBoard correctly
    */
  protected Board()
  { brett=new CardField[5][];
    for(int count=0; count<5; count++)
    { brett[count]=new CardField[5];
    }

    for(int a=0; a<5; a++)
    { for(int b=0; b<5; b++)
      { brett[a][b]=new CardField(a,b);
      }
    }
    
    startStack1=new StartField(Const.start1,Const.yAll,player1);
    finishStack1=new OwnerField(Const.finish1,Const.yAll,player1);
    removedCards1=new OwnerField(Const.remove1,Const.yAll,player1);      
    startStack2=new StartField(Const.start2,Const.yAll,player2);
    finishStack2=new OwnerField(Const.finish2,Const.yAll,player2);
    removedCards2=new OwnerField(Const.remove2,Const.yAll,player2);        
    
    Move.setBoard(null);
  }
  
  public void setPlayer(Player o1,Player o2)
  { player1=o1;
    player2=o2;
    
    //reInit owner
    for(int a=0; a<5; a++)
    { for(int b=0; b<5; b++)
      { brett[a][b].setOwner(o1,o2);        
      }
    }
    startStack1.setOwner(o1);
    finishStack1.setOwner(o1);
    removedCards1.setOwner(o1);
    finishStack2.setOwner(o2);
    startStack2.setOwner(o2);
    removedCards2.setOwner(o2);
  } 
   
  public void push(int x,int y,Card card)
  { card.setPosition(x,y);   
    brett[x][y].push(card);
  }

  public Card pop(int x,int y)
  { return brett[x][y].pop();
  }

  public Card peek(int x,int y)
  { return brett[x][y].peek();
  }
  
  /** to initialize at the beginning the random cards of each player
   */
  protected void init(StartField startStack,Player player)
  {  String tmp;
     int i;
     int no=player.no;         
     Vector initStack=new Vector();     
     
    //make a real field with card objects from startCards
    for(int count=0; count <Const.NC; count++)
    { tmp=startCards[count];
      i=Integer.parseInt(tmp.charAt(1)+"");
      initStack.addElement(new Card(tmp.charAt(0),i,player));
    }
    //now mix the stack randomly und make startstack
    for(int count=0; count <Const.NC; count++)   
    { i=Math.abs(EbbeFlut.nextRandomInt()%(Const.NC-count));
      startStack.pushInitial((Card)initStack.elementAt(i));
      initStack.removeElementAt(i);
    }    
  }
  
  public String toString()
  { StringBuffer sb=new StringBuffer("\n\n");
    Card tmp;
    
    for(int m=0; m<5;m++)
    { for(int n=0; n<5; n++)
      { sb.append(m+","+n+":"+brett[m][n].getContentsAsString()+"\n");
      }
    }
    sb.append("\n\nstartStack1  :"+startStack1.getContentsAsString());
    sb.append("\nstartStack2  :"+startStack2.getContentsAsString());
    sb.append("\nfinishStack1 :"+finishStack1.getContentsAsString());
    sb.append("\nfinishStack2 :"+finishStack2.getContentsAsString());
    sb.append("\nremovedCards1:"+removedCards1.getContentsAsString());
    sb.append("\nremovedCards2:"+removedCards2.getContentsAsString());
    return sb.toString();
  }   
     
  public StartField getStartStack1()
  { return startStack1;
  }
  
  public StartField getStartStack2()
  { return startStack2;  
  }
  
  public OwnerField getFinishStack1()
  { return finishStack1;
  }
  
  public OwnerField getFinishStack2()
  { return finishStack2;
  }
  
  public OwnerField getRemovedStack1()
  { return removedCards1;
  }
  
  public OwnerField getRemovedStack2()
  { return removedCards2;
  }
  
  public OwnerField getFinishStack(int no)
  { if(no==Const.NO_1) return finishStack1;
    else return finishStack2;
  }
  
  public StartField getStartStack(int no)
  { if(no==Const.NO_1) return startStack1;
    else return startStack2;
  }
  
  public OwnerField getRemovedStack(int no)
  { if(no==Const.NO_1) return removedCards1;
    else return removedCards2;
  }
}//Board Class

