public class ID
{
   private static int counter; // initialized to 0 by default

   public static synchronized int getID()
   {
      return counter++;
   }

   public static void main(String[] args)
   {
      System.out.println(ID.getID());
   }
}