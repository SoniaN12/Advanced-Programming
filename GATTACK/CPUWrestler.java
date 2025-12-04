import java.util.Random;
class CPUWrestler extends Wrestler {

    public CPUWrestler() {
        super("CPU", 100, 150); // Start with 50 stamina, same as Player
    }

    @Override
    public void takeTurn(Wrestler opponent) {
        // CPU randomly selects a move it has enough stamina for
        Wrestler.Move[] moves = Wrestler.Move.values();
        Random random = new Random();
        Wrestler.Move move;

        // Keep selecting until a move CPU can perform is found
        do {
            move = moves[random.nextInt(moves.length)];
        } while (!canPerform(move));

        switch (move) {
            case LIGHT_ATTACK -> lightAttack(opponent);
            case HEAVY_ATTACK -> heavyAttack(opponent);
            case BLOCK -> block();
            case RECOVER -> recover();
        }
    }

    private boolean canPerform(Wrestler.Move move) {
        return switch (move) {
            case LIGHT_ATTACK -> stamina >= 10;
            case HEAVY_ATTACK -> stamina >= 25;
            default -> true; // block and recover always possible
        };
    }

    public void lightAttack(Wrestler opponent) {
        int damage = random.nextInt(8) + 5;
        opponent.takeDamage(damage);
    }

    public void heavyAttack(Wrestler opponent) {
        int damage = random.nextInt(13) + 10;
        opponent.takeDamage(damage);
    }

    public void block() {
        blocking = true;
        stamina += 5;
        if (stamina > 50) stamina = 50; // cap at max stamina
    }

    public void recover() {
        int recovery = random.nextInt(10) + 5;
        stamina += recovery;
        if (stamina > 50) stamina = 50;
    }
}
