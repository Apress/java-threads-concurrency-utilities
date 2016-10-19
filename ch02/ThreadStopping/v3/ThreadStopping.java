public class ThreadStopping
{
   public static void main(String[] args)
   {
      class StoppableThread extends Thread
      {
         private boolean stopped; // defaults to false

         @Override
         public void run()
         {
            boolean _stopped = false;
            while (!_stopped)
            {
               synchronized(this)
               {
                  _stopped = stopped;
               }
               System.out.println("running");
            }
         }

         synchronized void stopThread()
         {
            stopped = true;
         }
      }
      StoppableThread thd = new StoppableThread();
      thd.start();
      try
      {
         Thread.sleep(1000); // sleep for 1 second
      }
      catch (InterruptedException ie)
      {
      }
      thd.stopThread();
   }
}