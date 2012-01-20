/***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************
 *
 *
 * Chronos.java
 *
 * Created on 26. September 2004, 19:07
 */

/** chronical order to identify wether a card is popped, placed, moved
 *  and wether the turn is finished or the complete game
 * 
 * @author  peter karich
 */
public class Chronos 
{
    int moveNo=0;
    boolean finishedGame, finishedTurn, retFromBeginning, lastMoves;
	
    /** a whole turn is splitted into several steps
     */
    static int FRESH=0, SHOWED=1, STARTCARD_PLACED=2, status;
    private boolean sthWasClicked;
    
    public Chronos() 
    { status=FRESH;
      finishedGame=false;
      finishedTurn=false;
      retFromBeginning=true;
      lastMoves=false;
    }
    
    public void newTurn(boolean fromBeg)
    { finishedTurn=false;          
      retFromBeginning=true;//!!important -> this var is false if this player will see forgotten moves of the other one. But if he had forgotten moves than he shouldn't able to see forgotten moves of the other player
      sthWasClicked=false;
      
      if(fromBeg) status=FRESH;      
      else        status=STARTCARD_PLACED;
    }    
    
    public void setStatus(int s)
    { status=s;
      if(getStatus().equals("undefined"))      
      { throw new Error("sth gowes wrong in class chronos");      
      }    
    }
    
    public String getStatus()
    { String tmp="undefined";
      
      switch(status)
      { case 0: tmp="none"; break;
        case 1: tmp="showed"; break;
        case 2: tmp="placed"; break;        
      }
      return tmp;
    }
    
    public void setGameIsFinished()
    { finishedGame=true;
    }

    public void setTurnIsFinished()
    { finishedTurn=true;      
    }    
    
    public boolean isGameFinished()
    { return finishedGame;
    }
     
    public void setSthWasClicked()
    { sthWasClicked=false;
    }    

    public boolean sthWasClicked()
    { return sthWasClicked;
    } 
    
    public boolean fresh()
    { return status==FRESH;
    }    
    
    public boolean showed()
    { return status==SHOWED;
    }    
    
    public boolean placedStartCard()
    { return status==STARTCARD_PLACED;
    }            
    
    public boolean isTurnFinished()
    { return finishedTurn;
    }
    
    public void setLastMoves(boolean b)
    { lastMoves=b;      
    } 
    
    public boolean lastMoves()
    { return lastMoves;
    }
    
    public void setTheOtherPlayerIsNotReady()
    { retFromBeginning=false;
      finishedTurn=true;      
    }
    
    public boolean fromBeginning() 
    { return retFromBeginning;    
    }
    
    public void nextMove()    
    { moveNo++;     
    }
    
    public void prevMove()    
    { moveNo--;           
    }          
    
    public int getMoveNo()
    { return moveNo/2+1;
    }
}
