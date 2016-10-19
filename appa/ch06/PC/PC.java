import java.util.concurrent.Semaphore;

public class PC
{
   public static void main(String[] args)
   {
      Shared s = new Shared();
      Semaphore semCon = new Semaphore(0);
      Semaphore semPro = new Semaphore(1);
      new Producer(s, semPro, semCon).start();
      new Consumer(s, semPro, semCon).start();
   }
}

class Shared
{
   private char c;

   void setSharedChar(char c)
   {
      this.c = c;
   }

   char getSharedChar()
   {
      return c;
   }
}

class Producer extends Thread
{
   private final Shared s;
   private final Semaphore semPro, semCon;

   Producer(Shared s, Semaphore semPro, Semaphore semCon)
   {
      this.s = s;
      this.semPro = semPro;
      this.semCon = semCon;
   }

   @Override
   public void run()
   {
      for (char ch = 'A'; ch <= 'Z'; ch++)
      {
         try
         {
            semPro.acquire();
         }
         catch (InterruptedException ie)
         {
         }
         s.setSharedChar(ch);
         System.out.println(ch + " produced by producer.");
         semCon.release();
      }
   }
}
class Consumer extends Thread
{
   private final Shared s;
   private final Semaphore semPro, semCon;

   Consumer(Shared s, Semaphore semPro, Semaphore semCon)
   {
      this.s = s;
      this.semPro = semPro;
      this.semCon = semCon;
   }

   @Override
   public void run()
   {
      char ch;
      do
      {
         try
         {
            semCon.acquire();
         }
         catch (InterruptedException ie)
         {
         }
         ch = s.getSharedChar();
         System.out.println(ch + " consumed by consumer.");
         semPro.release();
      }
      while (ch != 'Z');
   }
}