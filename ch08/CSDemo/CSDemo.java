import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class CSDemo
{
   public static void main(String[] args) throws Exception
   {
      ExecutorService es = Executors.newFixedThreadPool(10);
      CompletionService<BigDecimal> cs = 
         new ExecutorCompletionService<BigDecimal>(es);
      cs.submit(new CalculateE(17));
      cs.submit(new CalculateE(170));
      Future<BigDecimal> result = cs.take();
      System.out.println(result.get());
      System.out.println();
      result = cs.take();
      System.out.println(result.get());
      es.shutdown();
   }
}

class CalculateE implements Callable<BigDecimal>
{
   final int lastIter;

   public CalculateE(int lastIter)
   {
      this.lastIter = lastIter;
   }

   @Override
   public BigDecimal call()
   {
      MathContext mc = new MathContext(100, RoundingMode.HALF_UP);
      BigDecimal result = BigDecimal.ZERO;
      for (int i = 0; i <= lastIter; i++)
      {
         BigDecimal factorial = factorial(new BigDecimal(i));
         BigDecimal res = BigDecimal.ONE.divide(factorial, mc);
         result = result.add(res);
      }
      return result;
   }

   private BigDecimal factorial(BigDecimal n)
   {
      if (n.equals(BigDecimal.ZERO))
         return BigDecimal.ONE;
      else
         return n.multiply(factorial(n.subtract(BigDecimal.ONE)));
   }
}