import java.io.*;

class Util
{ 

 static public void standardStreamChange(String fileName,String device)
 {   FileOutputStream fos=null;
     PrintStream o;
  
   try
   { fos=new FileOutputStream(fileName);
   }
   catch(Exception exc) 
   { exc.printStackTrace();
     System.err.println("file not found, let standard output");
     return;
   }

   if(fos!=null)
   { o=new PrintStream(fos);
     if("out".equalsIgnoreCase(device)) System.setOut(o);
     else if("err".equalsIgnoreCase(device)) System.setErr(o);
     
   }
 }
}