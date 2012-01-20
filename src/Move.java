/***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************
 *
 * Move.java
 *
 * Created on 26. September 2004, 19:01
 */

/**
 *
 * @author  peter karich
 */
public class Move implements Cloneable
{   private Card card;
    public int fromY,fromX;
    private Player owner;
    public int toX,toY;
    static private Board board;
    static public int NOTHING=0, FINISH=1, PROMPT=2, FIRST=3, TAKE_BACK_WASNT_CALLED=4;
    
    /** Creates a new instance of Move;
     * you have to ensure that this coord are possible and there 
     * is a card in the right color
     */
    public Move(Card ca,int xTo,int yTo)
    { card=ca;
      fromX=card.getX();
      fromY=card.getY();
      this.owner=card.getOwner();
      toX=xTo;
      toY=yTo;
    }
    
    /** to set another board for ai -> to make calculating nonvisible
     */
    static public void setBoard(Board b)
    { board=b;      
    }
    
    public Card getCard()
    { return card;
    }
    
    private boolean isFromStartStack1()
    { return fromX==Const.start1 && fromY==Const.yAll;
    }
    
    private boolean isFromStartStack2()
    { return fromX==Const.start2 && fromY==Const.yAll;
    }
    
    static public boolean isStart1(int x,int y)
    { return x==4 && y==4 || x==4 && y==3 || x==3 && y==4;
    }
    
    static public boolean isStart2(int x,int y)
    { return x==0 && y==0 || x==0 && y==1 || x==1 && y==0;
    }
    
    /**
     * test wether card is on its supposed place?   => ca 4% slower
     */
    public boolean isPossible()
    { if(!takeBackCalled) 
       { if(Const.DEBUG) System.out.println("WARNING: Move.isPossible() was not called, cause you takeBack this move");
          return false;
       }
       
       boolean possible=false;
       Card tmp;
      
      if(owner.no==Const.NO_1)
      { if(fromX==Const.start1 && fromY==Const.yAll) 
        { //here: test wether card is on its supposed place?   => ca 4% slower
               return isStart1(toX,toY);
        }
          else if(toX==fromX && toY==fromY-1)
          { for(int m=0; m<5; m++)
            { if(m==fromX) continue;
              
              tmp=board.peek(m,fromY);
              if(tmp==null) continue;
              if(tmp.equals(getCard())) 
              { possible=true;
                break;
              }
            }  
          }
          else if(toX==fromX-1 && toY==fromY)
          { for(int n=0; n<5; n++)
            { if(n==fromY) continue;
              
              tmp=board.peek(fromX,n);
              if(tmp==null) continue;
              if(tmp.equals(getCard())) 
              { possible=true;
                break;
              }
            }
          }//else if        
      }
      else//no== NO_2
      { if(fromX==Const.start2 && fromY==Const.yAll) 
        { return isStart2(toX,toY);          
        }
          else if(toX==fromX && toY==fromY+1)
          { for(int m=0; m<5; m++)
            { if(m==fromX) continue;
              
              tmp=board.peek(m,fromY);
              if(tmp==null) continue;
              if(tmp.equals(getCard())) 
              { possible=true;
                break;
              }
            }
          }
          else if(toX==fromX+1 && toY==fromY)
          { for(int n=0; n<5; n++)
            { if(n==fromY) continue;
              
              tmp=board.peek(fromX,n);
              if(tmp==null) continue;
              if(tmp.equals(getCard())) 
              { possible=true;
                break;
              }
            }
          }//else if
      }
       
      return possible;
    }
    
    int returnStatus;
    boolean takeBackCalled=true;
    
    /** after calling this methode you can call takeBack,
      * but its your work to prevent clashs
     **/
    public int doIt()
    { if(!takeBackCalled) 
      { if(Const.DEBUG) System.out.println("WARNING: Move.doIt() was not called, cause you do this move without taken it back");
         return TAKE_BACK_WASNT_CALLED;
      }

      takeBackCalled=false;
      
      Card tmp;      
      returnStatus=NOTHING;
      
      if(owner.no==Const.NO_1)
      { //start move, push only for pc moves!!! humans start move is handle in BoardButtonAl push5x5
        if(fromX==Const.start1)
        { board.push(toX,toY,board.getStartStack1().pop());         
          returnStatus=FIRST;
          return FIRST;          
        }
        tmp=board.pop(fromX,fromY);
        
        //move into finish
        if(this.isStart2(toX,toY))
        { board.getFinishStack1().push(tmp);
          returnStatus=FINISH;          
          return FINISH;
        }        
        //"wrong" move to the horizontal edge
        else if(toX==-1)
        { board.getRemovedStack1().push(tmp);
          returnStatus=PROMPT;          
          return PROMPT;
        }
        else if(toY==-1)
        { board.getRemovedStack1().push(tmp);
          returnStatus=PROMPT;          
          return PROMPT;          
        }
        //regular move        
        else board.push(toX, toY, tmp);        
      }
      else//if no==2
      { if(fromX==Const.start2)
        { board.push(toX,toY,board.getStartStack2().pop());
          returnStatus=FIRST;          
          return FIRST;          
        }             
        tmp=board.pop(fromX,fromY);
        
        if(isStart1(toX,toY))
        { board.getFinishStack2().push(tmp);
          returnStatus=FINISH;          
          return FINISH;
        }
        else if(toX==5)
        { board.getRemovedStack2().push(tmp);
          returnStatus=PROMPT;          
          return PROMPT;          
        }
        else if(toY==5)
        { board.getRemovedStack2().push(tmp);
          returnStatus=PROMPT;          
          return PROMPT;          
        }
        else board.push(toX, toY, tmp);        
      }
      return NOTHING;
    }
    
    /**be sure that you call doIt before this methode
     * is it better to give takeBack Move as argument??
     */
    public void takeBack()
    { takeBackCalled=true;
       Card tmp;
      
      if(owner.no==Const.NO_1)
      { if(isStart2(toX,toY))
         tmp=board.getFinishStack1().pop();
        else if(returnStatus==PROMPT)
         tmp=board.getRemovedStack1().pop();
        else
        { tmp=board.pop(toX,toY);
          if(fromX==Const.start1)
          { board.getStartStack1().push(tmp);
            return;// Chronos.SHOWED;
          }
        }         
      }
      else //no ==2
      { if(isStart1(toX,toY))
         tmp=board.getFinishStack2().pop();
        else if(returnStatus==PROMPT)
         tmp=board.getRemovedStack2().pop();        
        else
        { tmp=board.pop(toX,toY);        
          if(fromX==Const.start2)
          { board.getStartStack2().push(tmp);
            return;// Chronos.SHOWED;
          }
        }
      }

      board.push(fromX, fromY, tmp);
      
      return; //Chronos.SHOWED;
    }
    
    static private Move moveTmp;
    
    /** call move.doIt before calling this routine!
     */
    static public Stack getNewPossible(Stack old, Player player,Move doneMove)
    {  Stack newMoves=new Stack(10,5);
       boolean uncoveredH=false, uncoveredV=false, movedH=false,movedV=false;
       int moveDir=+1; // move direction for player two       
      if(player.no==Const.NO_1) moveDir=-1;
      
       Card uncoveredCard=null, tmpCard;       
       //is this move vertical?
       boolean verticalMove=doneMove.fromY == doneMove.toY; 
       
      //System.out.println("sth goes wrong in Move.getNewPossible");
      //remove all impossible moves now, do Move.doIt before!!!
      for(int i=0; i < old.size(); i++)
      { moveTmp=(Move)old.elementAt(i);
        if(moveTmp.isPossible()) 
            newMoves.push(moveTmp);        
        //System.out.println(i+" "+moveTmp.toString());
      }
      
      //if doneMove is a start move
      if(   (doneMove.isFromStartStack1() && player.no==Const.NO_1)
         || (doneMove.isFromStartStack2() && player.no==Const.NO_2))
      { //let both uncovered == false
        movedH=true;
        movedV=true;
      }
      else
      { tmpCard=board.peek(doneMove.fromX,doneMove.fromY);
        
        if(tmpCard!=null && player.isOwnerOf(tmpCard)) uncoveredCard=tmpCard;
      
        //if doneMove.to are "off board" values, or finish moves
        if( (isStart1(doneMove.toX, doneMove.toY) && player.no==Const.NO_2)
         || (isStart2(doneMove.toX, doneMove.toY) && player.no==Const.NO_1)
         || doneMove.toX>=5 || doneMove.toY>=5 || doneMove.toX <0 || doneMove.toY<0)
        { if(uncoveredCard!=null) { uncoveredH=true;  uncoveredV=true;  }
        }
        //"normal moves"
        else
        { if(uncoveredCard!=null) 
          { uncoveredH=true;  uncoveredV=true;  
            if(verticalMove) movedV=true;
            else movedH=true;
          }
          else 
          { movedH=true; movedV=true;          
          }
        }
      }
      //push covered Card themself only for one time in the newMoves stack
       boolean firstHorizontal=true, firstVertical=true;       
           
      //calculate the moves for the uncovered card with location doneMove.fromX,fromY
      if(uncoveredH || uncoveredV)
      { for(int m=0; m<5; m++)
        { if(uncoveredH)
          { //the real board "horizontal"
            if(doneMove.fromX != m) // don't pick up the uncovered themself
            { tmpCard=board.peek(m, doneMove.fromY);
              if(tmpCard!=null &&tmpCard.equals(uncoveredCard))
              { newMoves.push(new Move(tmpCard, m,doneMove.fromY+moveDir));
                if(firstHorizontal) 
                    newMoves.push(new Move(uncoveredCard, doneMove.fromX, doneMove.fromY+moveDir)); //push it only one time
                firstHorizontal=false;
              }
            }
          }
          if(uncoveredV)
          { //vertical
            if(doneMove.fromY != m)
            { tmpCard=board.peek(doneMove.fromX, m);              
              if(tmpCard!=null &&tmpCard.equals(uncoveredCard))
              { newMoves.push(new Move(tmpCard, doneMove.fromX+moveDir, m));              
                if(firstVertical) 
                    newMoves.push(new Move(uncoveredCard, doneMove.fromX+moveDir, doneMove.fromY));
                firstVertical=false;
              }
            }
          }
        }//for
      }
       
      if(movedH || movedV)
      { firstHorizontal=true;
        firstVertical=true;
        Card movedCard=doneMove.getCard();
        
        //calculate the moves for the moved card with actual location doneMove.toX,toY
        for(int m=0; m<5; m++)
        { //the real board "horizontal"
          if(movedH)
          { if(doneMove.toX != m) // don't pick up the uncovered themself
            { tmpCard=board.peek(m, doneMove.toY);          
              if(tmpCard!=null &&tmpCard.equals(movedCard))
              { newMoves.push(new Move(tmpCard, m,doneMove.toY+moveDir));
                if(firstHorizontal) 
                    newMoves.push(new Move(movedCard, doneMove.toX, doneMove.toY+moveDir)); //push it only one time
                firstHorizontal=false;
              }
            }
          }
          if(movedV)
          { //vertical
            if(doneMove.toY != m)
            { tmpCard=board.peek(doneMove.toX, m);
              if(tmpCard!=null &&tmpCard.equals(movedCard))
              { newMoves.push(new Move(tmpCard, doneMove.toX+moveDir, m));              
                if(firstVertical) 
                    newMoves.push(new Move(movedCard, doneMove.toX+moveDir, doneMove.toY));
                firstVertical=false;
              }
            }
          }
        }//for
      }
       
      return newMoves;
    }
    
  /** get all possible moves
   *  @return all possible moves as a stack of class Move
   */
    static public Stack getAllPossible(Player player)
    { Move move;
      Stack all=new Stack(10,5);
      int m,n;
      Card tmp;
      
      if(player.no==Const.NO_1)
      { tmp=board.getStartStack1().peek();
        if(tmp!=null)
        { all.push(new Move(tmp,4,4));
          all.push(new Move(tmp,4,3));
          all.push(new Move(tmp,3,4));        
        }
        for(m=0; m<5; m++)
        { for(n=0; n<5; n++)
          { tmp=board.peek(m,n);
            if(tmp==null) continue;
            if(!player.isOwnerOf(tmp)) continue;
            move=new Move(tmp, m-1,n  );
            if(move.isPossible()) all.push(move);
            //move.toX=m; move.toY=n-1; //is the same as: 
            move=new Move(tmp, m,  n-1);          //but you have to "new Move" it
            if(move.isPossible()) all.push(move);
          }
        }
      }
      else //if no==2
      { tmp=board.getStartStack2().peek();
        if(tmp!=null)
        { all.push(new Move(tmp,0,0));
          all.push(new Move(tmp,0,1));
          all.push(new Move(tmp,1,0));        
        }
        for(m=0; m<5; m++)
        { for(n=0; n<5; n++)
          { tmp=board.peek(m,n);
            if(tmp==null) continue;
            if(!player.isOwnerOf(tmp)) continue;
            move=new Move(tmp, m+1,n);
            if(move.isPossible()) all.push(move);
            move=new Move(tmp, m,  n+1);
            if(move.isPossible()) all.push(move);
          }
        }        
      }       
      return all;
    }
    
    /** how much cards are covered by the opponent?
     *  count them and the covered cards of the oppenent
     *  @return opponent - own covered cards
     */
    static public int getCoveredAssessment(Player player)
    {  int own,foreign;
       Card tmp;
     
      if(player.no==Const.NO_1)
      { own=Const.NC-board.getFinishStack1().getSize()-
            board.getStartStack1().getSize()-board.getRemovedStack1().getSize();
        foreign=Const.NC-board.getFinishStack2().getSize()-
                board.getStartStack2().getSize()-board.getRemovedStack2().getSize();
      }
      else
      { foreign=Const.NC-board.getFinishStack1().getSize()-
                board.getStartStack1().getSize()-board.getRemovedStack1().getSize();
        own=Const.NC-board.getFinishStack2().getSize()-
            board.getStartStack2().getSize()-board.getRemovedStack2().getSize();
      }
       
      //subtract all visible cards => covered
      for(int m=0; m<5; m++)
      { for(int n=0; n<5; n++)
        { tmp=board.peek(m,n);
          if(tmp==null) continue;
          if(player.isOwnerOf(tmp)) own--;
          else foreign--;
        }
      }      
      
      //own covers are negative, foreign are good
      return foreign-own;
    }
    
    /** how much fields of the 3 starter fields are empty
     */ 
    static public int getFreeStartAssessment(Player player)
    {  int own=0;
       
      if(player.no==Const.NO_1)
      { if(board.peek(4,4)==null) own++;
        if(board.peek(4,3)==null) own++;
        if(board.peek(3,4)==null) own++;        
      }
      else
      { if(board.peek(0,0)==null) own++;
        if(board.peek(0,1)==null) own++;
        if(board.peek(1,0)==null) own++;        
      }
       
      return own;
    }
    
    public boolean equals(Object obj)
    { Move tmp=(Move)obj;
       return tmp.card.equals(this.card) && tmp.toX == this.toX && tmp.toY==this.toY;
    }

    public String toString()
    { if(this==Const.doYourWorkMove) return "do Your Work!";
        return card.toString()+" from "+fromX+","+fromY+" to "+toX+","+toY;
    }
    
    public Object clone()
    { return new Move((Card)card.clone(), toX,  toY);
    }
}
