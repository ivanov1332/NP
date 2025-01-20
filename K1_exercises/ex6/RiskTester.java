package K1_exercises.ex6;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Game {
    private final List<Integer> attacker;
    private final List<Integer> defender;

    public Game(String line){
        String []parts=line.split(";");
        attacker=parseDice(parts[0]);
        defender=parseDice(parts[1]);
    }
    public List<Integer> parseDice(String line){
        return Arrays.stream(line.split("\\s+")).map(Integer::parseInt).sorted(Comparator.reverseOrder()).collect(Collectors.toList());
    }
    public boolean isWinner(){
        return IntStream.range(0,attacker.size()).allMatch(i->attacker.get(i)>defender.get(i));
    }
}

class Risk {

    List<Game> games;

    public Risk(List<Game> games) {
        this.games = games;
    }

    Risk(){
        this.games = new ArrayList<>();
    }

    public int processAttacksData(InputStream in) {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        games = br.lines().map(Game::new).collect(Collectors.toList());
        return (int) games.stream().filter(Game::isWinner).count();
    }
}

public class RiskTester {
    public static void main(String[] args) {

        Risk risk = new Risk();

        System.out.println(risk.processAttacksData(System.in));

    }
}