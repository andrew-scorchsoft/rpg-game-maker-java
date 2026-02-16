package test.issues;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;

import org.jvnet.flamingo.ribbon.JRibbonFrame;
import org.jvnet.flamingo.ribbon.RibbonApplicationMenu;

public class Issue44 extends JRibbonFrame
{
  public Issue44 ()
  {
    this.setSize(600, 300);
    this.setLocationRelativeTo(null);
    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
  }

  public static void main (String[] args)
  {
    SwingUtilities.invokeLater(new Runnable()
    {
      @Override
      public void run ()
      {
        Issue44 ribbon = new Issue44();
        ribbon.setUndecorated(true);
        ribbon.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);

        // Uncomment the next line to make the bug go away....
//          ribbon.getRibbon().setMinimumSize(new Dimension(100, 100));
        ribbon.setVisible(true);
      }
    });
  }
}
