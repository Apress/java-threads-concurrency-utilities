import java.util.concurrent.atomic.AtomicLong;

class ID
{
   private static AtomicLong nextID = new AtomicLong(1);

   static long getNextID()
   {
      return nextID.getAndIncrement();
   }
}