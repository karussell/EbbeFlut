/***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************
 *
 * BoardButtonAL.java
 *
 * Created on 8. Oktober 2004, 13:10
 */

import java.awt.*;
import java.awt.event.*;

/** the actionListener for all buttons on the visible board
 * @author  peter karich
 */  
class BoardButtonAL implements ActionListener
{   Card firstCard;
    Move move;
    int a;
    int b;
    boolean peekCard=false;
    private EbbeFlut main;

  public BoardButtonAL(EbbeFlut mainFrameProg)
  { main=mainFrameProg;
  }

   /** Is this a bad implementation with usage of main.chronical ? <p>
    *  (1) normal move <p>
    *      if(chronical==fresh) then possible use of: 
    *      a) startbutton to show startCard 
    *      b) finishButton to make a contest to the opponent <p>
    *      
    *      if(chronical==showed) then click and place a special card from/to 
    *      5x5 board and startstacks  <p>
    *
    *      if(chronical==placed) then
    *      a) click and place a special card from/to 5x5 board and startstacks
    *      b) click startField to end move <p>
    *
    *  (3) last moves <p>    
    *      always allowed to
    *      a) click and place a special card from/to 5x5 board and startstacks
    *      b) click startField to end move <p>
    *      if(!peekCard) then possible use of finishButton to make a 
    *      contest to the opponent <p>
    *       
    *
    */
   public void actionPerformed(ActionEvent ae)
   { if(fireTo==REMOVE) { fireToRemove(ae.getActionCommand()); return;}
      else if(fireTo==SHOWSTACK) { fireToShowStack(ae.getActionCommand()); return;}
     

     if(main.chronical.isGameFinished() && !main.chronical.lastMoves()) return;
     if(!main.currentPlayer.getType().equals(Const.HUMAN_PLAYER)) return;
           
      String cl=((Button)ae.getSource()).getActionCommand();
      Punkt d=GraphicBoard.parseActionCommand(cl);
      a=d.getX();
      b=d.getY();      
      
     
     if(main.chronical.lastMoves())
     { if(!main.chronical.sthWasClicked() && clickOppentsFinishButton()) return;
       //now we know sth was clicked!!
       main.chronical.setSthWasClicked();
       if(clickStartButtonToEnd()) return;
       else if(!peekCard) peekCard();
       else pushTo5x5();
       
       return;
     }
     //if this player didnt do his work or if he places the startCard while actual move
     if(main.chronical.placedStartCard())
     { if(clickStartButtonToEnd()) return;
       else if(!peekCard) peekCard();
       else pushTo5x5();
       
       return;
     }
     else
     { if(main.chronical.fresh())
       { if(clickOppentsFinishButton()) return;
         else if(clickStartButtonToShow()) return;
       }
       else if(main.chronical.showed())
       { if(!peekCard) peekCard();
         else pushTo5x5();
         return;
       }
       else if(main.chronical.placedStartCard())
       { if(clickStartButtonToEnd()) return;
         else if(!peekCard) peekCard();
         else pushTo5x5();
         return;
       }
     }                        
   }//public actionPerformed
   
    /** @return true if the methode identify the click as click on oppents finishbutton
    */
   private boolean clickOppentsFinishButton()
   {  if(!((main.currentPlayer.no==Const.NO_1 && a==Const.finish2) || 
           (main.currentPlayer.no==Const.NO_2 && a==Const.finish1)
          )) return false;
          
     if(Move.getAllPossible(main.getOtherPlayer(main.currentPlayer)).size()>0)
     { main.chronical.setTheOtherPlayerIsNotReady();
        main.currentPlayer.path.push(Const.doYourWorkMove);//i hope path is clear now!!!
     }
     
     return true;
   }
   
   /** @return true if the methode identify the click as a click on startbutton
    */
   private boolean clickStartButtonToShow()
   { //player wants to see the card=> now opponent isn't contestable
     StartField field;
     
     if(main.currentPlayer.no==Const.NO_1 && a==Const.start1)
       field=main.board.getStartStack1();     
     else if(main.currentPlayer.no==Const.NO_2 && a==Const.start2)
       field=main.board.getStartStack2();     
     else return false;
     
     field.setLabel("click me and "+field.peek()+" to the board!");
     main.chronical.setStatus(Chronos.SHOWED);
     return true;
   }
   
   
   /** @return true if the methode identify the click as a click on startbutton
    */
   private boolean clickStartButtonToEnd()
   { //player is ready??:move-end action
     if(main.board.getStartStack(main.currentPlayer.no).peek()!=null) return false;
     if((main.currentPlayer.no==Const.NO_1 && a==Const.start1) ||
        (main.currentPlayer.no==Const.NO_2 && a==Const.start2))
     { main.chronical.setTurnIsFinished();
        
       return true;
     }
     
     return false;     
   }
   
   /** @return true if the methode identify the click as 5x5 click
    */
   private boolean pushTo5x5()
   { if(!(a>=0 && a<5 && b>=0 && b<5)) return false;
     
     if(firstCard==null) return true;
     move=new Move(firstCard, a,b);
     if(Move.isStart1(move.fromX,move.fromY))
       main.board.setBackground(move.fromX, move.fromY, Const.starterFieldsColor1);     
     else if(Move.isStart2(move.fromX,move.fromY))
       main.board.setBackground(move.fromX, move.fromY, Const.starterFieldsColor2);     
     else main.board.setBackground(move.fromX, move.fromY, Const.NORMAL_BACKGROUND);     
     
     peekCard=false;      
     if(move.isPossible())
     {  int i=move.doIt();
       main.currentPlayer.path.push(move); 
       //if(i==Move.FINISH) System.out.println("a card is ins saeckl gehuepferlt");
       //a "wrong" edge move? is not longer possible to move directly
       //else if(i==Move.PROMPT) System.out.println("\"wrong\" move!Your PLACED card was removed!");
         
       if(move.fromX==Const.start1 || move.fromX==Const.start2)
       {  main.chronical.setStatus(Chronos.STARTCARD_PLACED);       
           main.board.getStartStack(main.currentPlayer.no).setLabel("Click me to end the move!");
       }
     }
       
     return true;     
   }
   
   /** @return true if the methode identify the click as 5x5 click,if chronical is placed
    */
   private boolean peekCard()
   { 
     if(a>=0 && a<5 && b>=0 && b<5)
     { firstCard=main.board.peek(a,b);       
       if(firstCard !=null && main.currentPlayer.isOwnerOf(firstCard))
       { peekCard=true;
         main.board.setBackground(a,b, Const.MARKE_COLOR);
         return true;
       }  
       firstCard=null;
     }
     else if((a==Const.start1 && b==Const.yAll && main.currentPlayer.no==Const.NO_1)
          || (a==Const.start2 && b==Const.yAll && main.currentPlayer.no==Const.NO_2))
     { firstCard=main.board.getStartStack(main.currentPlayer.no).peek();
       if(firstCard !=null && main.currentPlayer.isOwnerOf(firstCard))
       { main.board.setBackground(a,b, Const.MARKE_COLOR);    
         peekCard=true;         
         return true;
       }         
     }
     return false;     
   }   
   
  static private int NOWHERE=0, REMOVE=1, SHOWSTACK=2, fireTo=NOWHERE;

  /** for communication between this class and main.MenuActionListener;
   *  if (al != null ) this class will fire to menuAL else it will make moves etc.
   */
  static public void fireToRemove()
  { fireTo=REMOVE;
  }

  static public void fireToShowStack()
  { fireTo=SHOWSTACK;
  }

  static public void fireToBoardButtonAL()
  { fireTo=NOWHERE;
  }
  
PDialog removeCardAnswer,showStackAnswer;

//set back to "fireToBoardButtonAL" before return!!
private void fireToShowStack(String cmd)
{    Punkt p=main.board.parseActionCommand(cmd);
      PDialog showStackAnswer=new PDialog(main, main.board.getStackPanel(p.x, p.y),true,"ok_cancel");
    showStackAnswer.setVisible(true);
    showStackAnswer.getReturnStatus();
    fireToBoardButtonAL();
}

//set back to "fireToBoardButtonAL" before return!!
private void fireToRemove(String cmd)
{  if(main.chronical.fresh() && !main.chronical.lastMoves())
    {  if(removeCardAnswer==null) removeCardAnswer=new PDialog(main,"Sorry removing is not possible!",true,"ok_cancel");
        removeCardAnswer.setVisible(true);
        removeCardAnswer.getReturnStatus();           
        fireToBoardButtonAL();
        return;
    }
    
     Punkt p=main.board.parseActionCommand(cmd);
     Stack all=Move.getAllPossible(main.currentPlayer);
     Move tmpMove;
     boolean removingPossible=false;

    for(int i=0; i<all.size(); i++)
    { tmpMove=(Move)all.elementAt(i);
       if(tmpMove.fromX==p.x && tmpMove.fromY==p.y)
       { if(main.currentPlayer.no==Const.NO_1 && (tmpMove.toX==-1 || tmpMove.toY==-1)) 
          { removingPossible=true;
             tmpMove.doIt();
             break;
          }
          if(main.currentPlayer.no==Const.NO_2 && (tmpMove.toX==5 || tmpMove.toY==5)) 
          { removingPossible=true;
             tmpMove.doIt();
             break;
          }
       }
    }//for
    if(!removingPossible)
    { if(removeCardAnswer==null) removeCardAnswer=new PDialog(main,"Sorry removing is not possible!",true,"ok_cancel");
       removeCardAnswer.setVisible(true);
       removeCardAnswer.getReturnStatus();
     }
    
     fireToBoardButtonAL();
}


}//class BoardButton A L        
  
