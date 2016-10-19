import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.awt.image.BufferedImage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.Timer;

class Projector
{
   private volatile List<Slide> slides;
   private Screen s;
   private Timer t;
   private volatile int slideIndexC, slideIndexN;
   private volatile float weight;

   Projector(List<Slide> slides, Screen s)
   {
      this.slides = slides;
      this.s = s;
      t = new Timer(1500, null);
      t.setDelay(3000);
      slideIndexC = 0;
      slideIndexN = 1;
   }

   void start()
   {
      s.drawImage(Slide.blend(slides.get(0), null, 1.0f));
      ActionListener al = (ae) ->
      {
         weight = 1.0f;
         Timer t2 = new Timer(0, null);
         t2.setDelay(10);
         ActionListener al2 = (ae2) ->
         {
            Slide slideC = slides.get(slideIndexC);
            Slide slideN = slides.get(slideIndexN);
            BufferedImage bi = Slide.blend(slideC, slideN, weight);
            s.drawImage(bi);
            weight -= 0.01f;
            if (weight <= 0.0f)
            {
               t2.stop();
               slideIndexC = slideIndexN;
               slideIndexN = (slideIndexN + 1) % slides.size();
            }
         };
         t2.addActionListener(al2);
         t2.start();
      };
      t.addActionListener(al);
      t.start();
   }

   void stop()
   {
      t.stop();
   }
}

class Screen extends JComponent
{
   private Dimension d;
   private BufferedImage bi;
   private String text;

   Screen(int width, int height)
   {
      d = new Dimension(width, height);
   }

   void drawImage(BufferedImage bi)
   {
      this.bi = bi;
      repaint();
   }

   @Override
   public Dimension getPreferredSize()
   {
      return d;
   }

   @Override
   public void paint(Graphics g)
   {
      int w = getWidth();
      int h = getHeight();
      g.drawImage(bi, Slide.WIDTH <= w ? (w - Slide.WIDTH) / 2 : 0,
                  Slide.HEIGHT <= h ? (h - Slide.HEIGHT) / 2 : 0, null);
   }
}

class Slide
{
   static int WIDTH, HEIGHT;

   private static int TEXTBOX_WIDTH, TEXTBOX_HEIGHT, TEXTBOX_X, TEXTBOX_Y;

   private BufferedImage bi;
   private String text;
   private static Font font;

   private Slide(BufferedImage bi, String text)
   {
      this.bi = bi;
      this.text = text;
      font = new Font("Arial", Font.BOLD, 20);
   }

   static BufferedImage blend(Slide slide1, Slide slide2, float weight)
   {
      BufferedImage bi1 = slide1.getBufferedImage();
      BufferedImage bi2 = (slide2 != null)
                          ? slide2.getBufferedImage()
                          : new BufferedImage(Slide.WIDTH, Slide.HEIGHT,
                                              BufferedImage.TYPE_INT_RGB);
      BufferedImage bi3 = new BufferedImage(Slide.WIDTH, Slide.HEIGHT,
                                            BufferedImage.TYPE_INT_RGB);
      Graphics2D g2d = bi3.createGraphics();
      g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                                                  weight));
      g2d.drawImage(bi1, 0, 0, null);
      g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                                                  1.0f - weight));
      g2d.drawImage(bi2, 0, 0, null);
      g2d.setColor(Color.BLACK);
      g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                           RenderingHints.VALUE_ANTIALIAS_ON);
      g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                                                  0.5f));

      g2d.fillRect(TEXTBOX_X, TEXTBOX_Y, TEXTBOX_WIDTH, TEXTBOX_HEIGHT);
      g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                                                  weight));
      g2d.setColor(Color.WHITE);
      g2d.setFont(font);
      FontMetrics fm = g2d.getFontMetrics();
      g2d.drawString(slide1.getText(), TEXTBOX_X + (TEXTBOX_WIDTH - 
                     fm.stringWidth(slide1.getText())) / 2,
                     TEXTBOX_Y + TEXTBOX_HEIGHT / 2 + fm.getHeight() / 4);
      g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                                                  1.0f - weight));
      if (slide2 != null)
         g2d.drawString(slide2.getText(), TEXTBOX_X + (TEXTBOX_WIDTH - 
                        fm.stringWidth(slide2.getText())) / 2, TEXTBOX_Y + 
                        TEXTBOX_HEIGHT / 2 + fm.getHeight() / 4);
      g2d.dispose();
      return bi3;
   }

   BufferedImage getBufferedImage()
   {
      return bi;
   }

   String getText()
   {
      return text;
   }

   static List<Slide> loadSlides(String imagesPath) throws IOException
   {
      File imageFilesPath = new File(imagesPath);
      if (!imageFilesPath.isDirectory())
          throw new IOException(imagesPath + " identifies a file");
      List<Slide> slides = new ArrayList<>();
      try (FileReader fr = new FileReader(imagesPath + "/index");
           BufferedReader br = new BufferedReader(fr))
      {
         String line;
         while ((line = br.readLine()) != null)
         {
            String[] parts = line.split(",");
            File file = new File(imageFilesPath + "/" + parts[0] + ".jpg");
            System.out.println(file);
            BufferedImage bi = ImageIO.read(file);
            if (WIDTH == 0)
            {
               WIDTH = bi.getWidth();
               HEIGHT = bi.getHeight();
               TEXTBOX_WIDTH = WIDTH / 2 + 10;
               TEXTBOX_HEIGHT = HEIGHT / 10;
               TEXTBOX_Y = HEIGHT - TEXTBOX_HEIGHT - 5;
               TEXTBOX_X = (WIDTH - TEXTBOX_WIDTH) / 2;
            }
            slides.add(new Slide(bi, parts[1]));
         }
      }
      if (slides.size() < 2)
         throw new IOException("at least one image must be loaded");
      return slides;
   }
}

public class SlideShow
{
   public static void main(String[] args) throws IOException
   {
      if (args.length != 1)
      {
         System.err.println("usage: java SlideShow ssdir");
         return;
      }
      List<Slide> slides = Slide.loadSlides(args[0]);
      final Screen screen = new Screen(Slide.WIDTH, Slide.HEIGHT);
      final Projector p = new Projector(slides, screen);
      Runnable r = () ->
      {
         final JFrame f = new JFrame("Slide Show");
         WindowAdapter wa = new WindowAdapter()
                            {
                               @Override
                               public void windowClosing(WindowEvent we)
                               {
                                  p.stop();
                                  f.dispose();
                               }
                            };
         f.addWindowListener(wa);
         f.setContentPane(screen);
         f.pack();
         f.setVisible(true);
         p.start();
      };
      EventQueue.invokeLater(r);
   }
}