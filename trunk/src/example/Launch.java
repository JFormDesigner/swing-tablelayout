package example;



import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.*;
import exampleSupport.Misc;



/**
 * Used code like
          <p>
            <applet height='30' width='200' codeBase='..\' code='example.Launch'>
              Enable Java to launch example.
              <param name='className' value='example.Simple'>
            </applet>
          <p>
 */

public class Launch extends Applet implements ActionListener
{

    boolean start;
    private String className;
    private Button button;
    
    public void init ()
    {
        setBackground (Color.white);
        className = getParameter("className");
        start = true;
        
        button = new Button("Launch " + className);
        button.addActionListener (this);
        add (button);
    }
    
    public void actionPerformed (ActionEvent e)
    {
        try
        {
            if (start)
            {
                launch();
                button.setLabel ("Close " + className);
            }
            else
            {
                dispose();
                button.setLabel ("Launch " + className);
            }
            
            start = !start;
        }
        catch (Exception error)
        {
            error.printStackTrace();
        }
    }
    
    public void launch () throws Exception
    {
            String paramList[] = {"[Ljava.lang.String;"};
            Method method = Misc.getMethod(className, "main", paramList);
            
            Object parameter[] = {new String[0]};
            method.invoke (null, parameter);
    }
    
    public void dispose () throws Exception
    {
        Frame frame[] = Frame.getFrames();
        
        for (int i = 0; i < frame.length; i++)
            frame[i].dispose();
    }

}
