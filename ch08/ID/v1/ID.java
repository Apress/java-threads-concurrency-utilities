class ID
{
   private static volatile long nextID = 1;

   static synchronized long getNextID()
   {
      return nextID++;
   }
}