/***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************
 *
 * TextFileView.java
 *
 * Created on 7. Oktober 2004, 21:18
 */

import java.awt.*;
import java.awt.event.*;
import java.io.*;

/** to view some text files fom the programs
 *
 * @author  peter
 */
public class TextFileView extends MyDialog
{
    
    /** Creates a new instance of TextFileView */
    public TextFileView(Frame frame,File file) 
    { super(frame,false);
      
      setLocation(frame.getLocation());
      
      StringBuffer sb=new StringBuffer();
      BufferedReader br;
      
      try
      {  br=new BufferedReader(new FileReader(file));
         String str;
        
        while((str=br.readLine())!=null)
        { sb.append(str);
          sb.append("\n");            
        }        
        
        br.close();
      }
      catch(FileNotFoundException fnf)
      {   fnf.printStackTrace();
      }
      catch(IOException ie)      
      {   ie.printStackTrace();      
      }      
      
      TextArea text=new TextArea(sb.toString(),80,50);
      text.setEditable(false);
      add(text);
      
      addWindowListener(new WindowAdapter()
      {   public void windowClosing(WindowEvent evt)
          { doClose(cancel);
          }
      });
      pack();
    }        
    
    public TextFileView(Frame frame,String fileName) 
    { this(frame,new File(fileName));
    }
}
