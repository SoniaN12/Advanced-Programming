import java.util.Random;

abstract class Wrestler {
    protected String name;
    protected int health;
    protected static int stamina;
    protected boolean blocking;
    protected Random random = new Random();

    public Wrestler(String name, int health, int stamina) {
        this.name = name;
        this.health = health;
        this.stamina = stamina;
    }

    public boolean isAlive() { return health > 0; }
    public abstract void takeTurn(Wrestler opponent);

    // Shared methods...
    public void block() { /* same as before */ }
    public void recover() { /* same as before */ }
    public void takeDamage(int dmg) { /* same as before */ }
    public void endTurn() { blocking = false; }

    public boolean canPerformAnyMove() {
        // Check if the wrestler has enough stamina for at least one attack
        return stamina >= 5; // 5 is the minimum stamina for LIGHT_ATTACK
    }

    public enum Move {
        LIGHT_ATTACK(5, 10, 15),   // cost 5 stamina, 10–20 damage
        HEAVY_ATTACK(15, 20, 35), // cost 15 stamina, 20–40 damage
        BLOCK(5, 5, 10),           // 5 cost, 5 damage
        RECOVER(0, 0, 0);         // handled separately

        private final int staminaCost;
        private final int minDamage;
        private final int maxDamage;

        Move(int staminaCost, int minDamage, int maxDamage) {
            this.staminaCost = staminaCost;
            this.minDamage = minDamage;
            this.maxDamage = maxDamage;
        }



        public int getStaminaCost() { return staminaCost; }

        public int generateDamage(Random random) {
            if (maxDamage == 0) return 0;
            return random.nextInt(maxDamage - minDamage + 1) + minDamage;
        }
    }


    @Override
    public String toString() {
        return name + " [Health: " + health + " | Stamina: " + stamina + "]";
    }
}
