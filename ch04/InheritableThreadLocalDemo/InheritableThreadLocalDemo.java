public class InheritableThreadLocalDemo
{
   private static final InheritableThreadLocal<Integer> intVal =
      new InheritableThreadLocal<Integer>();

   public static void main(String[] args)
   {
      Runnable rP = () ->
                    {
                       intVal.set(new Integer(10));
                       Runnable rC = () ->
                                     {
                                        Thread thd = Thread.currentThread();
                                        String name = thd.getName();
                                        System.out.printf("%s %d%n", name,
                                                          intVal.get());
                                     };
                       Thread thdChild = new Thread(rC);
                       thdChild.setName("Child");
                       thdChild.start();
                    };
      new Thread(rP).start();
   }
}