/***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************
 *
 * PDialog.java
 *
 * Created on 1. Januar 2004, 16:27
 */

import java.awt.*;
import java.awt.event.*;

/** for new game, and other questions or interactions
 *
 * @author  peter karich 
 */
public class PDialog extends MyDialog
{
    
    private Panel  buttonPanel;
    private TextField text;
    private Button okButton;
    private Button cancelButton;
    
    private String okString;
    private String cancelString;    
    
    PActionListener actionListener=new PActionListener();
      
    private boolean choiceDialog=false;
    private String retChoiceString;
    
    /** Creates new form Dialog 
        @param parent mainly this
        @param title the QuestionTitle of the dialog
        @param modal if modal is true other frames are blocked
        @param option "yes_no" "ok_cancel" "ok"
     
     */
    public PDialog(Frame parent, String title, boolean modal,String option)
    {   super(parent, modal); 
        // this here doesnt work ?? :  setTitle(super.getTitle());
        buttonPanel = new Panel();
        text=new TextField(title);
        text.setEditable(false);      
        
        okButton = new Button();
        cancelButton = new Button();
        
      if(option.equalsIgnoreCase("ok_cancel") || option.equalsIgnoreCase(ok))
      { okString=ok;
        cancelString=cancel;
      }
      else if(option.equalsIgnoreCase("yes_no"))
      { okString=yes;
        cancelString=no;
      }      
      
      okButton.setLabel(okString);
      cancelButton.setLabel(cancelString);
      
      okButton.addActionListener(actionListener);
      cancelButton.addActionListener(actionListener);
        
      buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
      buttonPanel.add(okButton);
      if(!option.equalsIgnoreCase(ok)) buttonPanel.add(cancelButton);      
      
      setLayout(new BorderLayout(5,5));
      add(text, BorderLayout.NORTH);
      add(buttonPanel, BorderLayout.SOUTH);
      
      addWindowListener(new WindowAdapter()
      {   public void windowClosing(WindowEvent evt)
          { if(choiceDialog) doClose(retChoiceString);
            else doClose(cancel);
          }
      });           
     
      pack();            
    }    
        
    /** Creates new form Dialog 
        @param choice the choice strings 
    */
    public PDialog(Frame parent, String title, boolean modal,Choice choice)
    { this(parent,title,modal,"ok_cancel");
      choiceDialog=true;
      
      buttonPanel.removeAll();
      buttonPanel.add(okButton);
      
      retChoiceString=choice.getItem(0);
      add(choice,BorderLayout.CENTER);
      choice.addItemListener(new ItemL());
      pack();
    }
    
    public PDialog(Frame parent, Panel panel, boolean modal,String option)
    { this(parent,"",modal,option);
      removeAll();
      add(panel, BorderLayout.NORTH);
      add(buttonPanel, BorderLayout.SOUTH);      
      pack();
    }
    
    /** the action listener: default actions for cancel and ok button*/
    class PActionListener implements ActionListener
    {
        
      /** Invoked when an action occurs.
       */
      public void actionPerformed(ActionEvent e)
      {  Object source=e.getSource();
             
        if(source instanceof Button)
        {  Button button=(Button)source;
               
          if(choiceDialog)
          { doClose(retChoiceString);
          }
          else if(button.getLabel().equals(okString))
          { doClose(okString);
          }
          else if(button.getLabel().equals(cancelString))
          { doClose(cancelString);
          }
        }
       }
        
    }//PActionListener
    
    class ItemL implements ItemListener
    { public void itemStateChanged(ItemEvent event)
      {  retChoiceString = (String) event.getItem();
      }
    }
    
}//Dialog

