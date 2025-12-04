class PlayerWrestler extends Wrestler {

        public PlayerWrestler() {
                super("Player", 100, 200); // Start at 50, consistent with MoveHandler
        }

        @Override
        public void takeTurn(Wrestler opponent) {
                // GUI triggers moves via buttons
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
                if (stamina > 50) stamina = 50; // Cap stamina
        }

        public void recover() {
                int recovery = random.nextInt(10) + 5;
                stamina += recovery;
                if (stamina > 50) stamina = 50; // Cap stamina
        }
}
