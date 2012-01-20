/***************************************************************************
 * Created on 20. September 2004, 12:57                                    *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************
 */

import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.io.*;

/** if you use netbeans then change the working directory to see the images under  *  tools-options-debuggingAndexecution-excecutiontype-externalexc-expert-workingdir;
 *  the same for debuggingAndExec-debuggertypes-defaultdebugging-expert--workingdir;
 * @author  Peter Karich
 */
public class EbbeFlut extends Frame 
{
  static private PDialog newGameDialog;
  private int gameCounter=0;

  //Cheat the game:
  //CHEAT it with System.exit(0); after playing
  //CHEAT it with 15 moves
  //CHEAT it with canceling the slow PlayBack modus
  //CHEAT it with setting Cards to board or popping cards from stack
  //if you init the constructor with a value (e.g. 60), than you can "play god"
 //set time in AI.class to init Timer( time ) on a bigger value, to debug the prog
  static public Random rand=new Random();// |CHEAT| 
  
  static public GraphicBoard board;
  static public Chronos chronical;
  static public Player currentPlayer;
  static public Player player1, player2;  
        
  static public void main(String arg[])
  { try
     {new EbbeFlut();
     }
     catch(Exception exc)
     { exc.printStackTrace();
        System.out.println(EbbeFlut.board.toString());
     }//catch
  }

  public EbbeFlut()
  { Const.initSettings();
    Const.printSettings();
    
    if(Const.VISUALISATION)
    { setMenuBar(new MenuInit());
      setTitle("Ebbe und Flut. Fuer Jule.");
      setSize(Const.windowSize);
      setLocation(Const.windowLocation);
      if(!Const.OS_IS_ZAURUS)
      { Image ii=Toolkit.getDefaultToolkit().getImage(Const.ebbeFlutPictureFile);
        setIconImage(ii);
      }       
      newGameDialog=new PDialog(this,"Would you like to start a new game?", true,"yes_no");    
      addWindowListener(new Close());
    }
    start();       
  }     
  
  private void start()
  {  SettingsDialog settings;
     Player player;
    
    //------------a loop for new game option-------------
    while(true)
    { System.out.println("new game");
      player=SettingsDialog.readSettings(Const.NO_1);
      if(player==null)
      { //standard values to init SettingsDialog
        player=new HumanPlayer(Const.COLOR1, "peter1",1);
      
        settings=new SettingsDialog(this,player,true);
        settings.setVisible(true);
        player1=settings.getPlayer();
      }
      else player1=player;
      
      player=SettingsDialog.readSettings(Const.NO_2);
      if(player==null)
      { player=new AI(Const.COLOR2,"ai",2);

        settings=new SettingsDialog(this,player,true);
        settings.setVisible(true);
        player2=settings.getPlayer();
      }
      else player2=player;
      
      board=new GraphicBoard(new BoardButtonAL(this),player1,player2);
      Move.setBoard(board);

      if(Const.VISUALISATION) 
      {  removeAll();
         add(board.getPanel());
      } 

      boolean swap=false;
      chronical=new Chronos();

      if(Const.VISUALISATION) this.setVisible(true);      
      
      //|CHEAT| THE GAME TO TEST IT
      /*
      
      for(int i=0; i<14; i++)
      {    board.getStartStack1().nextCard();
           chronical.nextMove();
           board.getStartStack2().nextCard();
           chronical.nextMove();
      }
      swap=true;
      board.getStartStack2().pushInitial(new Card('a',5,player2));
      board.push(0,0,new Card('a',4,player2));
      board.push(1,0,new Card('b',5,player2));
      board.push(4,0,new Card('b',2,player1));
      board.push(3,1,new Card('a',2,player2));
      board.push(2,1,new Card('b',4,player2));
      board.push(2,1,new Card('b',4,player1));
      board.push(2,1,new Card('a',3,player2));
      board.push(1,1,new Card('a',1,player1));
      board.push(2,2,new Card('a',5,player1));
      board.push(4,3,new Card('a',3,player1));
      board.push(3,3,new Card('b',5,player1));
      board.push(1,4,new Card('b',3,player1));
     
      */
      //--------!game loop!----------------------------------------------------
      while(!chronical.isGameFinished())
      { if(swap)  currentPlayer=player2;        
        else      currentPlayer=player1;
          
         //------show that the other player found a forgotten move => fromBeginning==false
        if(!chronical.fromBeginning())
        { board.setStartStackLabel("Do your work "+currentPlayer.getName()+"!",currentPlayer.no);
          board.getFinishStack(currentPlayer.no).pop();
        }
        else
        { board.getStartStack(currentPlayer.no).nextCard();
          //overwrite the counter with a good message
          board.setStartStackLabel("Click me "+currentPlayer.getName()
                                +"! turn no:"+chronical.getMoveNo(),currentPlayer.no);          
        }
        
        currentPlayer.nextTurn();
        
        if(chronical.fromBeginning())
        { chronical.nextMove();
          
          //if this player or his opponnent has moves
          if( Move.getAllPossible(getOtherPlayer(currentPlayer)).size()==0  &&
              Move.getAllPossible(currentPlayer).size()==0 && 
 		  board.getStartStack1().getSize()==0 && board.getStartStack2().getSize()==0 &&
		  board.getStartStack1().peek()==null && board.getStartStack2().peek()==null)
          {  String tmpString;
          
            if(board.getFinishStack1().getSize()==board.getFinishStack2().getSize()) tmpString="There is no winner!";
            else if(board.getFinishStack1().getSize()>board.getFinishStack2().getSize()) tmpString="The winner is player1: "+player1.getName();
            else tmpString="The winner is player2: "+player2.getName();

            board.setStartStack1Label(tmpString);
            board.setStartStack2Label(tmpString);
            System.err.println(tmpString+" player one="+board.getFinishStack1().getSize()+
			"; player two="+board.getFinishStack2().getSize()); 
               
            break;
          }
        }
        //if this currentplayer found a move -> currentPlayers has not moved, so take back in Chronos
        else chronical.prevMove();
        
        swap=!swap;
      }              
      //--------game loop end----------------------------------------------------

      gameCounter++;
      if(gameCounter>=Const.EXIT_NUMBER) System.exit(0);     
      if(Const.VISUALISATION)
      { newGameDialog.setVisible(true);
         if(!newGameDialog.getReturnStatus().equals(PDialog.yes))
         { System.exit(0);
         }
         this.setVisible(false);
      }      
    }//while
  }       
  
  static public Player getOtherPlayer(Player player)
  { if(player.no==Const.NO_1) return player2;
    else return player1;
  }
  
  class Action implements ActionListener
  {  SettingsDialog playerSettings;
     TextFileView germanText, englishText, gpl, logText;
     PDialog showStack, removeCard;
     
     public void actionPerformed(ActionEvent ae)
     {  String cmd= ae.getActionCommand();
        
       if(cmd.equals("Exit")) System.exit(0);
       else if(cmd.equals("New"))  chronical.setGameIsFinished();
       /*else if(cmd.equals("back")) //it is useful but so you can see whats under the cards!
       { if(chronical.placed() && currentPlayer.getColor()==move.getCard().getColor())
         { chronical.setStatus(move.takeBack());
           System.out.println("Take back the last move from current player!");
         }
       }*/
       else if(cmd.equals("german")) 
       { if(germanText==null) germanText=new TextFileView(getThis(),Const.germanInfoFile);
         germanText.setVisible(true);
       }
       else if(cmd.equals("english")) 
       { if(englishText==null) englishText=new TextFileView(getThis(),Const.englishInfoFile);
         englishText.setVisible(true);
       }
       else if(cmd.equals("gpl")) 
       { if(gpl==null) gpl=new TextFileView(getThis(),Const.gplFile);
         gpl.setVisible(true);
       }
       else if(cmd.equals("log")) 
       { if(Const.logFile!=null)
         { logText=new TextFileView(getThis(),Const.logFile.getPath());
           logText.setVisible(true);
         }
       }
       else if(cmd.equals("player1")) 
       { playerSettings=new SettingsDialog(getThis(),player1,true);
         
         playerSettings.setVisible(true);
         player1=playerSettings.getPlayer();
         board.setPlayer(player1,player2);
       }
       else if(cmd.equals("player2")) 
       { playerSettings=new SettingsDialog(getThis(),player2,true);
         
         playerSettings.setVisible(true);
         player2=playerSettings.getPlayer();
         board.setPlayer(player1,player2);
       }
       else if(cmd.equals("show stack")) 
       { if(showStack==null) 
            showStack=new PDialog(getThis(),"Click the stack you want to see!",
                                  true,"ok_cancel");
         showStack.setVisible(true);
         if(showStack.getReturnStatus().equals(PDialog.ok))   BoardButtonAL.fireToShowStack();
       }
       else if(cmd.equals("remove")) 
       { if(removeCard==null) 
            removeCard=new PDialog(getThis(),"Click the card you want to move"
                                   +" out of board!",true,"ok_cancel");
         removeCard.setVisible(true);
         if(removeCard.getReturnStatus().equals(PDialog.ok)) BoardButtonAL.fireToRemove(); 
       }
     }     
  }
  
  /** how to call this in a sub class? like EbbeFlut.Action
   */
  private EbbeFlut getThis()
  { return this;
  }
  
  static public int nextRandomInt()
  { return Math.abs(rand.nextInt());
  }

  class MenuInit extends MenuBar
  { ActionListener al=new Action();
    
    public MenuInit()
    {  Menu m;
      
      m = new Menu("Game");
      addItem(m, "New", al);
      m.addSeparator();
      addItem(m, "Exit", al);
      add(m);
      
      m = new Menu("Settings");
      addItem(m, "player2", al);
      addItem(m, "player1", al);      
      add(m);
      
      m = new Menu("Card");
      addItem(m, "show stack", al);
      addItem(m, "remove", al);      
      add(m);
      
      m = new Menu("Info");
      addItem(m, "german", al);
      addItem(m, "english", al);
      addItem(m, "gpl", al);      
      addItem(m, "log", al);      
      add(m);
    }
      
    public void addItem(Menu m, String name, ActionListener l)
    {  MenuItem mi=new MenuItem(name);
         
      mi.setActionCommand(name);
      mi.addActionListener(l);
      m.add(mi);
    }
  }//MenuInit
    
  class Close extends WindowAdapter
  { public void windowClosing(WindowEvent e)
    { System.exit(0);
    }
  }
  
}
