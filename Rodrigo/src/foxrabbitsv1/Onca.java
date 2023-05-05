package foxrabbitsv1;

import java.util.Random;

public class Onca {

    private static final int BREEDING_AGE = 20;
    // The age to which a jaguar can live.
    private static final int MAX_AGE = 200;
    // The likelihood of a jaguar breeding.
    private static final double BREEDING_PROBABILITY = 0.07;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 1;
    // The food value of a single fox. In effect, this is the
    // number of steps a jaguar can go before it has to eat again.
    private static final int FOX_FOOD_VALUE = 10;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();

    private int age;

    private int foodLevel;

    /**
     * Create a jaguar. A jaguar can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     *
     * @param randomAge If true, the jaguar will have random age and hunger level.
     * @param field     The field currently occupied.
     * @param location  The location within the field.
     */
    public Onca(boolean randomAge, Campo field, Localizacao location) {
        age = 0;
        alive = true;
        this.field = field;
        setLocation(location);
        if (randomAge) {
            age = rand.nextInt(MAX_AGE);
            foodLevel = rand.nextInt(FOX_FOOD_VALUE);
        } else {
            // leave age at 0
            foodLevel = FOX_FOOD_VALUE;
        }
    }

    /**
     * This is what the jaguar does most of the time: it hunts for
     * foxes or rabbits. In the process, it might breed, die of hunger,
     * or die of old age.
     *
     * @param field      The field currently occupied.
     * @param newJaguars A list to return newly born jaguars.
     */
    public void hunt(List<Onca> newJaguars) {
        incrementAge();
        incrementHunger();
        if (alive) {
            giveBirth(newJaguars);
            // Move towards a source of food if found.
            Localizacao newLocation = findFood();
            if (newLocation == null) {
                // No food found - try to move to a free location.
                newLocation = field.freeAdjacentLocation(location);
            }
            // See if it was possible to move.
            if (newLocation != null) {
                setLocation(newLocation);
            } else {
                // Overcrowding.
                setDead();
            }
        }
    }

    /**
     * Check whether the jaguar is alive or not.
     *
     * @return True if the jaguar is still alive.
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Return the jaguar's location.
     *
     * @return The jaguar's location.
     */
    public Localizacao getLocation() {
        return location;
    }

}
