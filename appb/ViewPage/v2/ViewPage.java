import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.InputStream;
import java.io.IOException;

import java.net.URL;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ViewPage
{
   public static void main(String[] args)
   {
      Runnable r = () ->
      {
         final JFrame frame = new JFrame("View Page");
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         JPanel panel = new JPanel();
         panel.add(new JLabel("Enter URL"));
         final JTextField txtURL = new JTextField(40);
         panel.add(txtURL);
         frame.getContentPane().add(panel, BorderLayout.NORTH);
         final JTextArea txtHTML = new JTextArea(10, 40);
         frame.getContentPane().add(new JScrollPane (txtHTML),
                                    BorderLayout.CENTER);
         ActionListener al = (ae) ->
         {
            InputStream is = null;
            try
            {
               URL url = new URL(txtURL.getText());
               is = url.openStream();
               StringBuilder sb = new StringBuilder();
               int b;
               while ((b = is.read()) != -1)
                  sb.append((char) b);
               txtHTML.setText(sb.toString());
            }
            catch (IOException ioe)
            {
               txtHTML.setText(ioe.getMessage());
            }
            finally
            {
               txtHTML.setCaretPosition(0);
               if (is != null)
                  try
                  {
                     is.close();
                  }
                  catch (IOException ioe)
                  {
                  }
            }
         };
         txtURL.addActionListener(al);
         frame.pack();
         frame.setVisible(true);
      };
      EventQueue.invokeLater(r);
   }
}