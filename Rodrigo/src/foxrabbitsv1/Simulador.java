package foxrabbitsv1;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Color;

public class Simulador
{
    // Constants representing configuration information for the simulation.
    // The default width for the grid.
    private static final int DEFAULT_WIDTH = 120;
    // The default depth of the grid.
    private static final int DEFAULT_DEPTH = 80;
    // The probability that a fox will be created in any given grid position.
    private static final double FOX_CREATION_PROBABILITY = 0.02;
    // The probability that a rabbit will be created in any given position.
    private static final double RABBIT_CREATION_PROBABILITY = 0.08;    

    // Lists of animals in the field.
    private List<Coelho> rabbits;
    private List<Raposa> raposas;
    // The current state of the field.
    private Campo field;
    // The current step of the simulation.
    private int step;
    // A graphical view of the simulation.
    private SimulatorView view;
    
    /**
     * Construct a simulation field with default size.
     */
    public Simulador()
    {
        this(DEFAULT_DEPTH, DEFAULT_WIDTH);
    }
    
    /**
     * Create a simulation field with the given size.
     * @param depth Depth of the field. Must be greater than zero.
     * @param width Width of the field. Must be greater than zero.
     */
    public Simulador(int depth, int width)
    {
        if(width <= 0 || depth <= 0) {
            System.out.println("The dimensions must be >= zero.");
            System.out.println("Using default values.");
            depth = DEFAULT_DEPTH;
            width = DEFAULT_WIDTH;
        }
        
        rabbits = new ArrayList<>();
        raposas = new ArrayList<>();
        field = new Campo(depth, width);

        // Create a view of the state of each location in the field.
        view = new SimulatorView(depth, width);
        view.setColor(Coelho.class, Color.ORANGE);
        view.setColor(Raposa.class, Color.BLUE);
        
        // Setup a valid starting point.
        reset();
    }
    
    /**
     * Run the simulation from its current state for a reasonably long 
     * period (4000 steps).
     */
    public void runLongSimulation()
    {
        simulate(4000);
    }
    
    /**
     * Run the simulation for the given number of steps.
     * Stop before the given number of steps if it ceases to be viable.
     * @param numSteps The number of steps to run for.
     */
    public void simulate(int numSteps)
    {
        for(int step=1; step <= numSteps && view.isViable(field); step++) {
            simulateOneStep();
             //delay(1600);   // uncomment this to run more slowly
        }
    }
    
    /**
     * Run the simulation from its current state for a single step. Iterate
     * over the whole field updating the state of each fox and rabbit.
     */
    public void simulateOneStep()
    {
        step++;

        // Provide space for newborn rabbits.
        List<Coelho> newRabbits = new ArrayList<>();
        // Let all rabbits act.
         for(Iterator<Coelho> it = rabbits.iterator(); it.hasNext(); ) {
      //  for(Coelho rabbit: rabbits) {
            Coelho rabbit = it.next();
            rabbit.run(newRabbits);
            if(! rabbit.isAlive()) {
              it.remove();
            }
        }
        
        // Provide space for newborn raposas.
        List<Raposa> newRaposas = new ArrayList<>();
        // Let all raposas act.
        for(Iterator<Raposa> it = raposas.iterator(); it.hasNext(); ) {
            Raposa raposa = it.next();
            raposa.hunt(newRaposas);
            if(! raposa.isAlive()) {
                it.remove();
            }
        }
        
        // Add the newly born raposas and rabbits to the main lists.
        rabbits.addAll(newRabbits);
        raposas.addAll(newRaposas);

        view.showStatus(step, field);
    }
        
    /**
     * Reset the simulation to a starting position.
     */
    public void reset()
    {
        step = 0;
        rabbits.clear();
        raposas.clear();
        populate();
        
        // Show the starting state in the view.
        view.showStatus(step, field);
    }
    
    /**
     * Randomly populate the field with raposas and rabbits.
     */
    private void populate()
    {
        Random rand = Randomizer.getRandom();
        field.clear();
        for(int row = 0; row < field.getDepth(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {
                if(rand.nextDouble() <= FOX_CREATION_PROBABILITY) {
                    Localizacao location = new Localizacao(row, col);
                    Raposa raposa = new Raposa(true, field, location);
                    raposas.add(raposa);
                }
                else if(rand.nextDouble() <= RABBIT_CREATION_PROBABILITY) {
                    Localizacao location = new Localizacao(row, col);
                    Coelho rabbit = new Coelho(true, field, location);
                    rabbits.add(rabbit);
                }
                // else leave the location empty.
            }
        }
    }
    
    /**
     * Pause for a given time.
     * @param millisec  The time to pause for, in milliseconds
     */
    private void delay(int millisec)
    {
        try {
            Thread.sleep(millisec);
        }
        catch (InterruptedException ie) {
            // wake up
        }
    }
}
