import java.util.Set;
import java.util.TreeSet;

public final class Planets 
{
   private final Set<String> planets = new TreeSet<>();

   public Planets() 
   {
      planets.add("Mercury");
      planets.add("Venus");
      planets.add("Earth");
      planets.add("Mars");
      planets.add("Jupiter");
      planets.add("Saturn");
      planets.add("Uranus");
      planets.add("Neptune");
   }

   public boolean isPlanet(String planetName) 
   {
      return planets.contains(planetName);
   }
}