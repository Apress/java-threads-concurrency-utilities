import java.util.concurrent.locks.ReentrantLock;

public class ID
{
   private static int counter; // initialized to 0 by default

   private final static ReentrantLock lock = new ReentrantLock();

   public static int getID()
   {
      lock.lock();
      try
      {
         int temp = counter + 1;
         try
         {
            Thread.sleep(1);
         } 
         catch (InterruptedException ie)
         {
         }
         return counter = temp;
      }
      finally
      {
         lock.unlock();
      }
   }
}