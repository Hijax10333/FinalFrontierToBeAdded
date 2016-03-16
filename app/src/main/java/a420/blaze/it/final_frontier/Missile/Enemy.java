package a420.blaze.it.final_frontier.Missile;


//abstract class which different kinds of enemies can take attributes from
public abstract class Enemy {

    //enemy attributes

    //name designed to define each kind of enemy
    protected String name;
    //enemy health goes down when it is involved in a collision or attacked by the player
    protected int health;
    //player can gain points for killing an enemy
    protected int pointsEarned;

    public Enemy(String name, int health, int pointsEarned) {
        this.name = name;
        this.health = health;
        this.pointsEarned = pointsEarned;
    }

    //METHODS
    //getter and setter methods to access the attributes


    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    //each enemy has points which the player earns for killing the enemy
    public int getPointsEarned() {
        return pointsEarned;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setPointsEarned(int pointsEarned) {
        this.pointsEarned = pointsEarned;
    }


    //method to take health away from an enemy if the player's bullet attacks him
    public int loseHealth() {
        int newHealth;
        newHealth = health - 1;
        health = newHealth;
        return health;
    }


}

