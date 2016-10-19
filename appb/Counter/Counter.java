import java.awt.EventQueue;
import java.awt.FlowLayout;

import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Counter extends JFrame
{
   int count;

   public Counter(String title)
   {
      super(title);
      setDefaultCloseOperation(EXIT_ON_CLOSE);

      JPanel pnl = new JPanel();
      ((FlowLayout) pnl.getLayout()).setHgap(20);
      final JLabel lblCount = new JLabel("");
      pnl.add(lblCount);
      final JButton btnStartStop = new JButton("Start");
      ActionListener al = (ae) ->
      {
         ++count;
         lblCount.setText(count + " ");
      };
      final Timer timer = new Timer(30, al);
      al = (ae) ->
      {
         if (btnStartStop.getText().equals("Start"))
         {
            btnStartStop.setText("Stop");
            timer.start();
         }
         else
         {
            btnStartStop.setText("Start");
            timer.stop();
         }         
      };
      btnStartStop.addActionListener(al);
      pnl.add(btnStartStop);
      setContentPane(pnl);

      setSize(300, 80);
      setVisible(true);
   }

   public static void main(String[] args)
   {
      EventQueue.invokeLater(() -> new Counter("Counter"));
   }
}