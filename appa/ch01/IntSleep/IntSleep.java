public class IntSleep
{
   public static void main(String[] args)
   {
      Runnable r = new Runnable()
                   {
                      @Override
                      public void run()
                      {
                         while (true)
                         {
                            System.out.println("hello");
                            try
                            {
                               Thread.sleep(100);
                            }
                            catch (InterruptedException ie)
                            {
                               System.out.println("interrupted");
                               break;
                            }
                         }
                      }
                   };
      Thread t = new Thread(r);
      t.start();
      try
      {
         Thread.sleep(2000);
      }
      catch (InterruptedException ie)
      {
      }
      t.interrupt();
   }
}