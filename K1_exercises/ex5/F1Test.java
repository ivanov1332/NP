package K1_exercises.ex5;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

class Lap {
    int minutes;
    int seconds;
    int milliseconds;

    public Lap(int minutes, int seconds, int milliseconds) {
        this.minutes = minutes;
        this.seconds = seconds;
        this.milliseconds = milliseconds;
    }

    public int totalMilliSeconds() {
        return (seconds * 1000) + (minutes * 60000) + milliseconds;
    }

    @Override
    public String toString() {
        return String.format("%d:%02d:%03d", minutes, seconds, milliseconds);
    }


}

class Race implements Comparable<Race> {
    String name;
    List<Lap> laps;

    Race() {
        this.name = "";
        this.laps = new ArrayList<>();
    }

    public Race(String name, List<Lap> laps) {
        this.name = name;
        this.laps = laps;

    }

    public Lap getBestLap() {
        return laps.stream().min(Comparator.comparingDouble(Lap::totalMilliSeconds)).orElse(null);
    }

    @Override
    public String toString() {
        return String.format("%-10s%10s", name, getBestLap().toString());
    }

    public String getName() {
        return name;
    }

    public static Race createRace(String line) {
        //Webber 2:32:103 2:49:182 2:18:132
        String[] parts = line.split("\\s+");
        String name = parts[0];
        List<Lap> laps = new ArrayList<>();
        for (int i = 1; i < parts.length; i++) {
            String[] lapParts = parts[i].split(":");
            int minutes = Integer.parseInt(lapParts[0]);
            int seconds = Integer.parseInt(lapParts[1]);
            int milliseconds = Integer.parseInt(lapParts[2]);
            laps.add(new Lap(minutes, seconds, milliseconds));
        }
        return new Race(name, laps);
    }

    @Override
    public int compareTo(Race o) {
        return Double.compare(this.getBestLap().totalMilliSeconds(), o.getBestLap().totalMilliSeconds());
    }
}

class F1Race {

    List<Race> races;

    F1Race() {
        this.races = new ArrayList<>();
    }

    public F1Race(List<Race> races) {
        this.races = races;
    }

    public void readResults(InputStream in) {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        races = br.lines().map(Race::createRace).collect(Collectors.toList());
    }

    public void printSorted(PrintStream out) {
        PrintWriter pw = new PrintWriter(out);
        int j = 0;
        races = races.stream()
                .sorted(Comparator.naturalOrder())
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        for (int i = 0; i < races.size(); i++) {
            pw.printf("%d. %s", i + 1, races.get(i).toString());
            pw.println();
        }
        pw.flush();
    }

}

public class F1Test {

    public static void main(String[] args) {
        F1Race f1Race = new F1Race();
        f1Race.readResults(System.in);
        f1Race.printSorted(System.out);
    }

}