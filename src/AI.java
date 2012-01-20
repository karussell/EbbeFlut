/***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************
 *
 * AI.java
 *
 * Created on 29. September 2004, 22:45
 */

/**
 * Artificial "Intelligence" :-)
 * @author  peter karich
 */
public class AI extends Player
{   private Board board;
    private MyTimer timer;
    
    /** -1 is for the first player and means that if a move goes to a coordinate
      * with that integer than it is a "wrong move" and in calc you skip those moves
      * to reserve calculation time
     */
    private int edge=-1;
    
    
    /** Creates a new instance of PcPlayer */
    public AI(java.awt.Color color,String name, int no) 
    { super(Const.AI_PLAYER, color, name, no,false);
    }
    
    /** @param fromBeginning hope the pc wont need the case fromBeginning==false :-)
     */
    public Path moves()
    { //test wether all possible moves are done from the other player
      if(Move.getAllPossible(EbbeFlut.getOtherPlayer(this)).size()>0) 
      { EbbeFlut.chronical.setTheOtherPlayerIsNotReady();        
         path.push(Const.doYourWorkMove);
        return path;
      }

        long longValue;
      if(no==Const.NO_1) longValue=Const.TIMER_TIME1;
      else longValue=Const.TIMER_TIME2;

      //interrupt the AI if the methode calc is recursing too long
      timer=new MyTimer(longValue);      
      timer.start();
      
       board=EbbeFlut.board.getBoard();
      //before we can start => CHANGE the board from visible to an unvisible one:
      Move.setBoard(board);             
        
      if(no==Const.NO_1) edge=-1; 
      else edge=5;

      path=calcWithNoPopping();
      timer.deactivate();
         
      //make the CHANGE BACK -> set/get the "real" board
      Move.setBoard(EbbeFlut.board);
      return path;
    }
    
    /** the assessment of a path
     */
    public int getAssess(int doItMoveRet,int oldPathSize)
    {  int assess=0;
       
      if(doItMoveRet==Move.PROMPT) assess=-60;
      int a=board.getFinishStack(this.no).getSize();
      int b=board.getRemovedStack(this.no).getSize();
      int c=board.getFinishStack(this.no).getSize();
      int d=Move.getCoveredAssessment(this);
      int e=Move.getFreeStartAssessment(this);
      int f=board.getStartStack1().getSize();
      int g=board.getStartStack2().getSize();
      
      assess+=3*(oldPathSize-1)+60*board.getFinishStack(this.no).getSize()              
              -60*board.getRemovedStack(this.no).getSize()              
              +2*Move.getCoveredAssessment(this)+Move.getFreeStartAssessment(this);
      
      return assess;
    }
    
    /*
    private void calcWithPopping()
    {  Stack all=Move.getAllPossible(this);
       Path retPath,newPath;
       
       retPath=calc((Move)all.elementAt(0),new Path());
      for(int i=1; i < all.size() && timer.isActive(); i++)
      { newPath=calc((Move)all.elementAt(i),new Path());
        if(newPath.getAssessment()>retPath.getAssessment())
        { retPath=newPath;
        }    
      }
      
    }*/
    
    /** calculates the moves without popping at the beginning
     *  this methode will call calc()
     */
    public Path calcWithNoPopping()
    {  Path retPath=new Path(),newPath,oldPath=new Path();
       int ass=getAssess(Move.NOTHING, -1000);
       Stack all=Move.getAllPossible(this);             
      
      oldPath.setAssessment(ass);
      retPath.setAssessment(ass);              
      
      for(int i=0; i < all.size() && timer.isActive(); i++)
      { newPath=calc((Move)all.elementAt(i),oldPath); 
        if(newPath.getAssessment()>retPath.getAssessment())      retPath=newPath;            
      }
      
      return retPath;    
    }
    
    /** recursive calculation of: the moves = one path
     */
    public Path calc(Move move,Path oP)
    {  Path oldPath=oP.getClone();
       int ret=move.doIt();
      
      Stack all=Move.getAllPossible(this);   
      oldPath.push(move);
      
      if(all.empty() || move.toX==edge || move.toY==edge) 
      { oldPath.setAssessment( getAssess(ret,oldPath.getSize()) ); 
        move.takeBack();
        return oldPath;
      }
      
       Path newPath,retPath;
      
      //iter has next cause all is not empty!
      retPath=calc((Move)all.elementAt(0),oldPath);
      for(int i=1; i < all.size() && timer.isActive(); i++)
      { newPath=calc((Move)all.elementAt(i),oldPath);
        if(newPath.getAssessment()>retPath.getAssessment())
        { retPath=newPath;
        }    
      }      
      
      move.takeBack();       
      return retPath;
  }//calc
  
  public String getPlayerName()
  { return super.getName();
  }
 
   /** if calculation time of AI will increase too much
    */
   class MyTimer extends Thread
   {   long millis;
       boolean active;
       long realTime;
       
      public MyTimer(long milli)
      { super();
        millis=milli;
        active=true;
        realTime=System.currentTimeMillis();
        setPriority(Thread.MIN_PRIORITY);
      }
      
      public void run() 
      { int loopCounter=60;
        try
        { for(int a=0; a<loopCounter && active; a++)
           Thread.sleep(millis/loopCounter);         
        }
        catch(InterruptedException ie)
        { System.out.println("sth goes wrong with the sleep operation");
        }
        if(active) 
        { System.out.println(getName()+" was interrupted by myTimer! too long thinking :-) !");
          deactivate();
        }        
        //System.out.println(getName()+" ends with "+(double)(System.currentTimeMillis()-realTime)/1000+" sec");              
      }      
      
      public boolean isActive()
      { return active;
      }
      
      /** if there is no need to interrupt, because AI is already ready
       */
      public void deactivate()
      { active=false;        
        System.out.println(getName()+" needed "+(double)(System.currentTimeMillis()-realTime)/1000+" sec");
      }
   }
}