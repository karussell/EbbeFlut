/***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************
 *
 * SettingsDialog.java
 *
 * Created on 7. Oktober 2004, 19:20
 */

import java.awt.*;
import java.awt.event.*;
import java.io.*;

/** to modify the settings of the players, with respect to the old settings
 *
 * @author  peter
 */
public class SettingsDialog extends MyDialog
{
    Player player,oldPlayer;
    TextField nameWindow;
    Button nameWindowHead;      
    Choice kindWindow;
    Button kindWindowHead;      
    Choice colorWindow;
    Button colorWindowHead;            
    boolean writeSettings;
    
    /** Creates a new instance of SettingsDialog */
    public SettingsDialog(Frame owner,Player oP,boolean writeSettings)
    { super(owner,true);
      this.writeSettings=writeSettings;
      oldPlayer=oP;
       Button saveB=new Button("save"),cancelB=new Button("cancel");
       
      saveB.addActionListener(new ButtonAL());
      cancelB.addActionListener(new ButtonAL());
      
      setLayout(new GridLayout(4,2));
      
      nameWindow=new TextField(oldPlayer.getName());
      nameWindowHead=new Button("Name?");
      
      kindWindow=new Choice();
      kindWindowHead=new Button("Type Of Player?");
      
      colorWindow=new Choice();
      colorWindowHead=new Button("Which color?");            
      
      kindWindow.add(Const.AI_PLAYER);
      //kindWindow.add(Const.PC_PLAYER);
      kindWindow.add(Const.HUMAN_PLAYER);
      kindWindow.select(oldPlayer.getType());
      
      colorWindow.add("white");
      colorWindow.add("black");
      colorWindow.add("blue");
      colorWindow.add("yellow");      
      colorWindow.select(colorToString(oldPlayer.getColor()));
      
      add(nameWindowHead);      add(nameWindow);
      add(kindWindowHead);      add(kindWindow);
      add(colorWindowHead);     add(colorWindow);
      add(saveB);                add(cancelB);      
      
      addWindowListener(new WindowAdapter()
      {   public void windowClosing(WindowEvent evt)
          { doClose(cancel);
          }
      });
      
      pack();
    }
    
    public Player getPlayer()
    { if(player==null) return oldPlayer;
      else return player;
    }

    class ButtonAL implements ActionListener
    {   /** Invoked when an action occurs.
         */
        public void actionPerformed(ActionEvent e) 
        { if(e.getActionCommand().equals("cancel")) doClose(cancel);
          
           Color color;
           String name=nameWindow.getText(),colString;
           int no=oldPlayer.no;
           
          if(name.length()==0) name=oldPlayer.getName();
          
          
          String kind=kindWindow.getSelectedItem();
          color=stringToColor(colorWindow.getSelectedItem());
          //if(color==null) color=oldPlayer.getColor();
          
          player=Player.cloneWithNewSettings(oldPlayer.no, kind, name, color);
          if(writeSettings) writeSettings(player);
          doClose(cancel);
        }
        
    }    
  
  public void writeSettings(Player player)
  { File file;
    
    if(player.no==Const.NO_1) file=new File(Const.player1File);
    else file=new File(Const.player2File);
    
    try
    { BufferedWriter bw=new BufferedWriter(new FileWriter(file));
      
      bw.write(player.getName()+"\n");
      bw.write(player.getType()+"\n");
      bw.write(colorToString(player.getColor()));
      bw.flush();
      bw.close();         
    }
    catch(Exception exc)
    { exc.printStackTrace();
    }
    
  }
  
  /** give back new values where the user give us new values;
   *  @oldPlayer where the user give us no information we use oldPlayer instead
   */
  static public Player readSettings(int no)
  { String fileName,name,type;
    Color color;
    Player player=null;
    if(no==Const.NO_1) fileName=Const.player1File;
    else fileName=Const.player2File;
    
    File file=new File(fileName);
    if(!file.canRead()) return null;    
    
    try
    {  BufferedReader br=new BufferedReader(new FileReader(file));
       name=br.readLine();if(name==null) return null;
       type=br.readLine();if(type==null) return null;
       if(Const.OS_IS_ZAURUS) 
       { br.readLine();
         if(no==Const.NO_1) color=Const.ZAURUS_COLOR1;
         else color=Const.ZAURUS_COLOR2;
       }
       else 
       { color=stringToColor(br.readLine());
         if(color==null) return null;
       } 
       player=Player.cloneWithNewSettings(no, type, name, color);
       br.close();
    }
    catch(Exception exc)
    { exc.printStackTrace();
    }
    
    return player;
  }
  
  static private Color stringToColor(String colString)
  { Color color;
    
    if(colString.equals("white")) color=Color.white;
    else if(colString.equals("black")) color=Color.black;
    else if(colString.equals("blue")) color=Color.blue;
    else if(colString.equals("yellow")) color=Color.yellow;
    else color=null;
    
    return color;
  }
  
  private String colorToString(Color color)
  { String str;
    
    if(Color.white.equals(color)) return "white";
    else if(Color.black.equals(color)) return "black";
    else if(Color.blue.equals(color)) return "blue";
    else if(Color.yellow.equals(color)) return "yellow";
    else return null;    
  }  
}
