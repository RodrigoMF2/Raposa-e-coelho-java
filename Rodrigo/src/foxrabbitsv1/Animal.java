package foxrabbitsv1;

import java.awt.*;

public abstract class Animal {

    private boolean alive;

    private Localizacao location;

    private Campo field;

    public Animal(Localizacao location, Campo field) {
        this.location = location;
        this.field = field;
    }

    protected Localizacao getLocation() {
        return location;
    }

    protected void setLocation(Localizacao location) {
        if(location != null) {
            field.clear(location);
        }
        location = newLocation;
        field.place(this, newLocation);

    }

    protected Campo getField() {
        return field;
    }

    protected void setField(Campo field) {
        this.field = field;
    }


    protected boolean isAlive()
    {
        return alive;
    }
    protected void setDead() {
        alive = false;
        if (location != null) {
            field.clear(location);
            location = null;
            field = null;
        }
    }

    public abstract void act(List<Animal> newAnimal);