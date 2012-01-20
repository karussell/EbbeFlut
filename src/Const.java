/***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************
 *
 * Const.java
 *
 * Created on 1. Januar 2004, 12:57
 */

import java.io.*;
import java.awt.*;
import java.util.Random;

/** some constants useful for the game
 * @author  peter karich
 */
public class Const
{ 
   static public void setOS()
   {  Dimension  d=Toolkit.getDefaultToolkit().getScreenSize();
     
     windowLocation=new Point();     
     if(OS_IS_ZAURUS)
     { COLOR1=ZAURUS_COLOR1;
       COLOR2=ZAURUS_COLOR2;
     }
     else
     { //shitty getScreenSize, because it is often too big, cause of the panels
       Dimension old=d;
       d=new Dimension(Math.round(old.width*SHORTER_FACTOR),
                       Math.round(old.height*SHORTER_FACTOR));
       windowLocation.setLocation((old.width-d.width)/2, (old.height-d.height)/2);
     }
     windowSize=d;    
   }
  
  static private Random randC=new Random();
  static public File createTempFile(String prae, String suff, File dir)
  {  String str=prae+"_"+randC.nextInt()%10000+suff;
     File file=new File(dir,str);
    if(file.exists()) return createTempFile(prae,suff,dir);
    return file;
  }

  static public void initSettings()
  {  String line;

    File file=new File(gameSettingsFile);
    if(!file.canRead()) return;    
    
    try
    {  BufferedReader br=new BufferedReader(new FileReader(file));
       
       while((line=br.readLine())!=null) readAndSetVariable(line);
       br.close();
    }
    catch(Exception exc)
    { exc.printStackTrace();
    }

    if(Const.CHANGE_STANDARD_STREAMS)
    { int i=randC.nextInt()%1000;
      logFile=createTempFile("Temp_"+i,outSuffix,new File(tmpDir));
      errFile=createTempFile("Temp_"+i,errSuffix,new File(tmpDir));
      System.out.println("err:"+errFile.getPath());
      System.out.println("out:"+logFile.getPath());
      Util.standardStreamChange(logFile.getPath(),"out");
      Util.standardStreamChange(errFile.getPath(),"err");
      
      //catch(IOException exc)
      //{ exc.printStackTrace();
      //  System.out.println("cant change standard streams to temporary files");
      //}
    }
    
    if(VISUALISATION) setOS();
  }

  static public void printSettings()
  { System.out.println("CHANGE_STANDARD_STREAMS="+CHANGE_STANDARD_STREAMS);
    System.out.println("OS_IS_ZAURUS="+OS_IS_ZAURUS);

    System.out.println("DEBUG="+DEBUG);
    System.out.println("NC="+NC);
    System.out.println("PLAYBACK1="+PLAYBACK1);
    System.out.println("PLAYBACK2="+PLAYBACK2);
    System.out.println("rand="+randomValue);
    System.out.println("TIMER_TIME1="+TIMER_TIME1);
    System.out.println("TIMER_TIME2="+TIMER_TIME2);
    System.out.println("EXIT_NUMBER="+EXIT_NUMBER);
    System.out.println("VISUALISATION="+VISUALISATION);

    System.err.println("CHANGE_STANDARD_STREAMS="+CHANGE_STANDARD_STREAMS);
    System.err.println("OS_IS_ZAURUS="+OS_IS_ZAURUS);

    System.err.println("DEBUG="+DEBUG);
    System.err.println("NC="+NC);
    System.err.println("PLAYBACK1="+PLAYBACK1);
    System.err.println("PLAYBACK2="+PLAYBACK2);
    System.err.println("rand="+randomValue);
    System.err.println("TIMER_TIME1="+TIMER_TIME1);
    System.err.println("TIMER_TIME2="+TIMER_TIME2);
    System.err.println("EXIT_NUMBER="+EXIT_NUMBER);
    System.err.println("VISUALISATION="+VISUALISATION);
  }

/** BE SURE that the (initial) variables you use here will initialized their variables after this function!!
*/
 static void readAndSetVariable(String line)
{   String variable,value;
     int delimer1=line.indexOf(COMMAND_IS);

    if(delimer1==-1) return;
    variable=line.substring(0,delimer1);
     int delimer2=line.indexOf(COMMAND_END,delimer1+1);
    
    value=line.substring(delimer1+1,delimer2);
    if(delimer2==-1) return;
 
     long longValue=0;
     boolean parsingOkay=true;
    try
    { longValue=Long.parseLong(value);
    }
    catch(NumberFormatException e)    
    { parsingOkay=false;    
    }

    if(variable.equalsIgnoreCase("CHANGE_STANDARD_STREAMS")) 
    { if(value.equalsIgnoreCase("NO")) CHANGE_STANDARD_STREAMS=false;
      else CHANGE_STANDARD_STREAMS=true;
    }
    else if(variable.equalsIgnoreCase("OS_IS_ZAURUS")) 
    { if(value.equalsIgnoreCase("YES")) OS_IS_ZAURUS=true;
      else OS_IS_ZAURUS=false;
    }
    else if(variable.equalsIgnoreCase("DEBUG"))
    { if(value.equalsIgnoreCase("YES")) Const.DEBUG=true;
      return;
    }

   //FROM HERE: DEBUG SECTION
    if(!DEBUG) return;

    if(variable.equalsIgnoreCase("VISUALISATION")) 
    { if(value.equalsIgnoreCase("NO")) VISUALISATION=false;
      else VISUALISATION=true;
    }   

    if(parsingOkay && longValue >=0)
    {  if(variable.equalsIgnoreCase("EXIT_NUMBER")) Const.EXIT_NUMBER=(int)longValue;
       else if(variable.equalsIgnoreCase("NC")  && longValue<=25) NC=(int)longValue;
       //else NC=25;
       else if(variable.equalsIgnoreCase("PLAYBACK1")) PLAYBACK1=(int)longValue;
       //else PLAYBACK1=2;
       else if(variable.equalsIgnoreCase("PLAYBACK2")) PLAYBACK2=(int)longValue;
       //else PLAYBACK2=2;
       else if(variable.equalsIgnoreCase("RANDOM")) 
       { randomValue=longValue;
         EbbeFlut.rand=new java.util.Random(randomValue);
       }
       //else let rand=Random();
       else if(variable.equalsIgnoreCase("TIMER_TIME1")) TIMER_TIME1=longValue;
       else if(variable.equalsIgnoreCase("TIMER_TIME2")) TIMER_TIME2=longValue;
    }
}

//now the standard values, if system can't the file or if you set DEBUG=NO;
static public int NC=25; //Number of all Cards, for one! player //|CHEAT| NC=15;
static public long TIMER_TIME1=60000; //to change the time for Timer(?) in AI.class
static public long TIMER_TIME2=60000; //to change the time for Timer(?) in AI.class
static public int PLAYBACK1=2;
static public int PLAYBACK2=2;
static public int EXIT_NUMBER=99;
static public boolean DEBUG=false;
static public boolean VISUALISATION=true;
static public boolean CHANGE_STANDARD_STREAMS=false;
static public boolean OS_IS_ZAURUS=false;
static public long randomValue=-1;

  static public String COMMAND_END=";", COMMAND_IS="="; 

  static public Dimension windowSize=Toolkit.getDefaultToolkit().getScreenSize();  
  static public Point windowLocation=new Point(0,0);  
  static public int XCOUNT=5, YCOUNT=5; //Number of x, y Lines
  static public int NO_1=1,NO_2=2;
  static public Color COLOR1=Color.blue, COLOR2=Color.yellow;
  static public Color MARKE_COLOR=Color.red;  
  static public Color NORMAL_BACKGROUND=Color.lightGray;
  static public Color ZAURUS_COLOR1=Color.black, ZAURUS_COLOR2=Color.black; 
  static public String player1File="dat/settingsPlayer1.txt";
  static public String player2File="dat/settingsPlayer2.txt";
  static public String gameSettingsFile="dat/gameSettings.txt";
  static public String outSuffix="_outlog.txt";
  static public String errSuffix="_errLog.txt";
  static public File   logFile;
  static public File   errFile;
  static public String ebbeFlutPictureFile="pics/ebbeflut.png";
  static public String germanInfoFile="doc/liesmich.tex";
  static public String englishInfoFile="doc/readme.tex";
  static public String tmpDir="tmp/";
  static public String gplFile="doc/gpl.txt";
  static public String toDoFile="doc/toDo.txt";  
  static public String AI_PLAYER="computer",HUMAN_PLAYER="human";
  static public Color starterFieldsColor1=Color.gray, starterFieldsColor2=Color.gray;
  static public int CardFieldID=1,OwnerFieldID=2;
  static public float SHORTER_FACTOR=0.9f;  //make the window smaller to prevent oversize
  //now only for identification (actionCmd) of the start and finishfiel(button);
  //you need other values then -1 until 5
  static public int start1=-5, start2=-6, finish1=-7, finish2=-8, remove1=-9,remove2=-10, yAll=-11;
  static Move doYourWorkMove=new Move(new Card('a',1,null),27,27);//throw the card away
}
 
