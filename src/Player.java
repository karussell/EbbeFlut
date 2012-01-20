/***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************
 *
 * Player.java
 *
 */

import java.awt.Color;

/** if you want to make a new player, may be an computerthinking one than use this class
 */
abstract class Player
{ private String name;  
  private Color color;
  private String type;
  private long waitMillis=500;
  private int playBackLoop;
  private boolean directAccess;//if the player is local or has direct acces to the "real" board

  public Path path=new Path();
  public int no;   
  
  public Player(String type, Color colour, String namePlayer,int no, boolean directAccessToTheBoard)
  { this.type=type; 
     directAccess=directAccessToTheBoard;
     this.color=colour;
     name=namePlayer;
     this.no=no;    
     if(no==Const.NO_1) playBackLoop=Const.PLAYBACK1;
     else playBackLoop=Const.PLAYBACK2;
  }  
  
  public void nextTurn()  
  {System.out.println("\n\n"+getName());    
    path.clear();//this is only important for the directAccess Player like HumanPlayer
    path=moves();
    showMoves(path);    
    EbbeFlut.board.setStartStackLabel("Ready!",no);
  }
  
  protected void showMoves(Path path)
  { for(int i=0; i<path.getSize(); i++)
     {  System.out.println(path.getElement(i).toString());
         if(path.getElement(i)==Const.doYourWorkMove) return;
     }
  
     if(directAccess) 
     { if(playBackLoop==0) return;//skip play back, this is not possible for pc, because pc hasn't move directly
        for(int i=path.getSize()-1; i>=0; i--)
        { path.getElement(i).takeBack();
        }
     } 

    for(int i=0; i<path.getSize(); i++)
     { loop(playBackLoop,i);
        path.getElement(i).doIt();
        if(playBackLoop>0) myWait(waitMillis);        
      }        
  }

   private void myWait(long millis)
   { try
     { Thread.sleep(millis);
     }
     catch(InterruptedException ie)
     { System.out.println("sth goes wrong with the sleep operation");
     }
   }
    
    private void loop(int playBack,int i)
    { for(int counter=0; counter<playBack; counter++)
      { path.getElement(i).doIt();
        myWait(waitMillis);
        path.getElement(i).takeBack();        
        myWait(waitMillis);                
      }
    }
   
  abstract public Path moves();
  
  public Color getColor()
  { return color;
  }
  
  public String getName()
  { return name;
  }
  
  public String getType()
  { return type;
  }
  
  public void setColor(Color c)
  { color=c;
  }
  
  public void setName(String n)
  { name=n;
  } 
  
  public boolean equals(Object obj)
  { return this.no==((Player)obj).no;
  }
  
  public boolean isOwnerOf(Card card)
  { //if(card==null) return false;
    return no==card.getOwner().no;
  }
  
  public Player getClone()
  { return cloneWithNewSettings(no,type,name,color);
  }
  
  static public Player cloneWithNewSettings(int newNo, String newKind, String newName,Color newColor)
  { Player player;
    
    if(newKind.equals(Const.HUMAN_PLAYER)) 
      player=new HumanPlayer(newColor,newName,newNo);
    else if(newKind.equals(Const.AI_PLAYER)) 
      player=new AI(newColor,newName,newNo);
    else return null;       
    
    return player;
  }
}