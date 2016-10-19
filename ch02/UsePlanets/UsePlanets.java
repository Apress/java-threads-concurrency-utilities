public class UsePlanets
{
   public static void main(String[] args)
   {
      Planets planets = new Planets();
      System.out.println(planets.isPlanet("Earth"));
      System.out.println(planets.isPlanet("Vulcan"));
   }
}