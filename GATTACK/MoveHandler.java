import javax.swing.*;
import java.awt.*;
import java.util.Random;

class MoveHandler {
        public static void executeMove(Wrestler user, Wrestler opponent, Wrestler.Move move) {
            int staminaCost = getStaminaCost(move);

            if (user.stamina < staminaCost) {
                System.out.println(user.name + " Doesn't have enough stamina to perform " + move);
                return;
            }

            user.stamina -= staminaCost;

            switch (move) {
                case LIGHT_ATTACK, HEAVY_ATTACK -> {
                    int damage = getMoveDamage(move, user.random);
                    opponent.takeDamage(damage);
                    System.out.println(user.name + " performs a " + move + " for " + damage + " damage!");
                    // Do NOT try to access GUI here
                }
                case BLOCK -> user.block();
                case RECOVER -> user.recover();
            }
        }

        private static int getStaminaCost(Wrestler.Move move) {
            return switch (move) {
                case LIGHT_ATTACK -> 10;
                case HEAVY_ATTACK -> 25;
                default -> 0;
            };
        }

        private static int getMoveDamage(Wrestler.Move move, Random random) {
            return switch (move) {
                case LIGHT_ATTACK -> random.nextInt(8) + 5;  // 10-20
                case HEAVY_ATTACK -> random.nextInt(13) + 10; // 20-40
                default -> 0;
            };
        }
    }


