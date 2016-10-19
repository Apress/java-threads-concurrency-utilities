import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PC
{
   public static void main(String[] args)
   {
      final BlockingQueue<Character> bq;
      bq = new ArrayBlockingQueue<Character>(26);
      final ExecutorService executor = Executors.newFixedThreadPool(2);
      Runnable producer = () ->
                          {
                             for (char ch = 'A'; ch <= 'Z'; ch++)
                             {
                                try
                                {
                                   bq.put(ch);
                                   System.out.printf("%c produced by " +
                                                     "producer.%n", ch);
                                }
                                catch (InterruptedException ie)
                                {
                                }
                             }
                          };
      executor.execute(producer);
      Runnable consumer = () ->
                          {
                             char ch = '\0';
                             do
                             {
                                try
                                {
                                   ch = bq.take();
                                   System.out.printf("%c consumed by " +
                                                     "consumer.%n", ch);
                                }
                                catch (InterruptedException ie)
                                {
                                }
                             }
                             while (ch != 'Z');
                             executor.shutdownNow();
                          };
      executor.execute(consumer);
   }
}