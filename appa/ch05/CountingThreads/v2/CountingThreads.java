import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;

public class CountingThreads
{
   public static void main(String[] args)
   {
      Runnable r = new Runnable()
                   {
                      @Override
                      public void run()
                      {
                         String name = Thread.currentThread().getName();
                         int count = 0;
                         while (true)
                            System.out.println(name + ": " + count++);
                      }
                   };
      ExecutorService es = 
         Executors.newSingleThreadExecutor(new NamedThread("A"));
      es.submit(r);
      es = Executors.newSingleThreadExecutor(new NamedThread("B"));
      es.submit(r);
   }
}

class NamedThread implements ThreadFactory
{
   private volatile String name; // newThread() could be called by a 
                                 // different thread

   NamedThread(String name)
   {
      this.name = name;
   }

   @Override
   public Thread newThread(Runnable r)
   {
      return new Thread(r, name);
   }
}