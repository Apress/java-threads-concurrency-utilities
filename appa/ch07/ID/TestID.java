public class TestID
{
   public static void main(String[] args)
   {
      Runnable r = () ->
                   {
                      Thread ct = Thread.currentThread();
                      while (true)
                         System.out.printf("%s: %d%n", ct.getName(), 
                                           ID.getID());
                   };
      Thread thdA = new Thread(r, "A");
      Thread thdB = new Thread(r, "B");
      thdA.start();
      thdB.start();
   }
}