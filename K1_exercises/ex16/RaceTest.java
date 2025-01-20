package K1_exercises.ex16;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class Race implements Comparable<Race> {
    int idOfParticipant;
    int startTime;
    int endTime;

    int getTotalRaceTime() {
        return endTime - startTime;
    }

    @Override
    public String toString() {
        int totalTime = getTotalRaceTime();
        int hours = totalTime / 3600;
        int minutes = (totalTime % 3600) / 60;
        int seconds = totalTime % 60;
        return String.format("%d %02d:%02d:%02d", idOfParticipant, hours,minutes,seconds);
    }

    public static Race createRace(String line) {
        String[] parts = line.split("\\s+");
        int id = Integer.parseInt(parts[0]);

        int startTime = convertToSeconds(parts[1]);
        int endTime = convertToSeconds(parts[2]);

        return new Race(id, startTime, endTime);
    }

    public static int convertToSeconds(String time) {
        String[] parts = time.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        int seconds = Integer.parseInt(parts[2]);
        return seconds + hours * 3600 + minutes * 60;
    }

    public Race(int idOfParticipant, int startTime, int endTime) {
        this.idOfParticipant = idOfParticipant;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public int compareTo(Race o) {
        return Integer.compare(this.getTotalRaceTime(), o.getTotalRaceTime());
    }
}

class TeamRace {

    static List<Race> races;

    public TeamRace() {
    }

    public static void findBestTeam(InputStream in, PrintStream out) {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        PrintWriter pw = new PrintWriter(out);

        races = br.lines().map(Race::createRace).sorted().limit(4).collect(Collectors.toList());

        int totalTime = races.stream().mapToInt(Race::getTotalRaceTime).sum();

        races.forEach(pw::println);
        pw.println(String.format("%02d:%02d:%02d", totalTime / 3600, (totalTime % 3600) / 60, totalTime % 60));

        pw.flush();
    }
}


public class RaceTest {
    public static void main(String[] args) {
        TeamRace.findBestTeam(System.in, System.out);
    }
}