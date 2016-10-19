public class ExceptionThread
{
   public static void main(String[] args)
   {
      Runnable r = new Runnable()
                   {
                      @Override
                      public void run()
                      {
                         int x = 1 / 0; // Line 10
                      }
                   };
      Thread thd = new Thread(r);
      thd.start();
   }
}