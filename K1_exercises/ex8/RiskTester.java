package K1_exercises.ex8;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Game {

    List<Integer> attacker;
    List<Integer> defender;
    int counterAttacker;
    int counterDefender;

    public Game(List<Integer> attacker, List<Integer> defender) {
        this.attacker = attacker;
        this.defender = defender;
    }

    public Game(List<Integer> attacker, List<Integer> defender, int counterAttacker, int counterDefender) {
        this.attacker = attacker;
        this.defender = defender;
        this.counterAttacker = counterAttacker;
        this.counterDefender = counterDefender;
    }

    @Override
    public String toString() {
        return String.format("%d %d", counterAttacker, counterDefender);
    }

    public static Game createGame(String line) {
        //5 3 4;2 4 1
        String[] parts = line.split(";");
        String[] attackerParts = parts[0].split("\\s+");
        String[] defenderParts = parts[1].split("\\s+");
        List<Integer> attackers = new ArrayList<>();
        List<Integer> defenders = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            attackers.add(Integer.parseInt(attackerParts[i]));
            defenders.add(Integer.parseInt(defenderParts[i]));
        }

        Collections.sort(attackers, Collections.reverseOrder());
        Collections.sort(defenders, Collections.reverseOrder());
        return new Game(attackers, defenders);
    }

    public void play() {
        for (int i = 0; i < 3; i++) {
            if (attacker.get(i) > defender.get(i)) {
                counterAttacker++;
            } else {
                counterDefender++;
            }
        }
    }
}


class Risk {

    List<Game> games;

    Risk() {
        this.games = new ArrayList<>();
    }

    public void processAttacksData(InputStream in) {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        br.lines()
                .map(Game::createGame)
                .peek(Game::play)
                .forEach(System.out::println);
    }
}

public class RiskTester {
    public static void main(String[] args) {
        Risk risk = new Risk();
        risk.processAttacksData(System.in);
    }
}
