public class Await
{
   static volatile int count;

   public static void main(String[] args)
   {
      Runnable r = () ->
                   {
                      Thread curThread = Thread.currentThread();
                      System.out.printf("%s has entered runnable and is " +
                                        "waiting%n", curThread.getName());
                      synchronized(Await.class)
                      {
                         count++;
                         try
                         {
                            Thread.sleep(2000);
                            while (count < 3)
                               Await.class.wait();
                         }
                         catch (InterruptedException ie)
                         {
                         }
                      }
                      System.out.printf("%s has woken up and is " +
                                        "terminating%n", 
                                        curThread.getName());
                   };
      Thread thdA = new Thread(r, "thdA");
      Thread thdB = new Thread(r, "thdB");
      Thread thdC = new Thread(r, "thdC");
      thdA.start();
      thdB.start();
      thdC.start();
      r = new Runnable()
              {
                 @Override
                 public void run()
                 {
                    try
                    {
                       while (count < 3)
                          Thread.sleep(100);
                       synchronized(Await.class)
                       {
                          Await.class.notifyAll();
                       }
                    }
                    catch (InterruptedException ie)
                    {
                    }
                 }
              };
      Thread thd = new Thread(r);
      thd.start();
   }
}