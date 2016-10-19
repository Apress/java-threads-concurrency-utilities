public class DeadlockDemo
{
   private final Object lock1 = new Object();
   private final Object lock2 = new Object();

   public void instanceMethod1()
   {
      synchronized(lock1)
      {
         synchronized(lock2)
         {
            System.out.println("first thread in instanceMethod1");
            // critical section guarded first by
            // lock1 and then by lock2
         }
      }
   }

   public void instanceMethod2()
   {
      synchronized(lock2)
      {
         synchronized(lock1)
         {
            System.out.println("second thread in instanceMethod2");
            // critical section guarded first by
            // lock2 and then by lock1
         }
      }
   }

   public static void main(String[] args)
   {
      final DeadlockDemo dld = new DeadlockDemo();
      Runnable r1 = new Runnable()
                    {
                       @Override
                       public void run()
                       {
                          while(true)
                          {
                             dld.instanceMethod1();
                             try
                             {
                                Thread.sleep(50);
                             }
                             catch (InterruptedException ie)
                             {
                             }
                          }
                       }
                    };
      Thread thdA = new Thread(r1);
      Runnable r2 = new Runnable()
                    {
                       @Override
                       public void run()
                       {
                          while(true)
                          {
                             dld.instanceMethod2();
                             try
                             {
                                Thread.sleep(50);
                             }
                             catch (InterruptedException ie)
                             {
                             }
                          }
                        }
                    };
      Thread thdB = new Thread(r2);
      thdA.start();
      thdB.start();
   }
}