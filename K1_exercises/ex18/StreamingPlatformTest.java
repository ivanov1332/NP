package K1_exercises.ex18;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

interface Rating {
    double getRating();
    boolean sameGenre(String genre);
}

class Film implements Rating {
    String filmName;
    List<String> genres;
    List<Integer> ratings;

    public Film(String filmName, List<String> genres, List<Integer> ratings) {
        this.filmName = filmName;
        this.genres = genres;
        this.ratings = ratings;
    }

    public static Film createFilm(String line) {
        String[] parts = line.split(";");
        String filmName = parts[0];
        List<String> genres = new ArrayList<>();
        genres = Arrays.stream(parts[1].split(",")).collect(Collectors.toList());
        List<Integer> ratings = new ArrayList<>();
        ratings = Arrays.stream(parts[2].split("\\s+")).map(Integer::parseInt).collect(Collectors.toList());

        return new Film(filmName, genres, ratings);
    }

    @Override
    public String toString() {
        //Movie Spider-Man: No Way Home 8.9200
        double averageRatings = ratings.stream().mapToDouble(Integer::intValue).sum() / ratings.size();
        return String.format("Movie %s %.4f", filmName, getRating());
    }

    @Override
    public double getRating() {

        if (ratings.isEmpty()) {
            return 0.0;
        }

        double averageRating = ratings.stream().mapToInt(Integer::intValue).average().orElse(0.0);
        double factor = Math.min(ratings.size() / 20.0, 1.0);
        return averageRating * factor;
    }

    @Override
    public boolean sameGenre(String genre) {
        return genres.stream().anyMatch(movie -> movie.equals(genre));
    }
}

class Series implements Rating {
    String seriesName;
    List<String> genres;
    List<Episode> episodes;

    public Series(String seriesName, List<String> genres, List<Episode> episodes) {
        this.seriesName = seriesName;
        this.genres = genres;
        this.episodes = episodes;
    }

    public static Series createSeries(String line) {
        String[] parts = line.split(";");
        String seriesName = parts[0];
        List<String> genres = new ArrayList<>();
        List<Episode> episodes = new ArrayList<>();

        genres = Arrays.stream(parts[1].split(",")).collect(Collectors.toList());
        episodes = Arrays.stream(parts[2].split("\\s+")).map(Episode::new).collect(Collectors.toList());

        return new Series(seriesName, genres, episodes);
    }

    @Override
    public String toString() {
        //TV Show Friends 8.8067 (6 episodes)
        return String.format("TV Show %s %.4f (%d episodes)", seriesName, getRating(), episodes.size());
    }

    @Override
    public double getRating() {

        if (episodes.isEmpty()){
            return 0.0;
        }
        int limit = Math.min(3,episodes.size());
        List<Episode> bestEpisodes = new ArrayList<>();
        bestEpisodes = episodes.stream().sorted(Comparator.comparing(Episode::getEpisodeRating).reversed()).limit(limit).collect(Collectors.toList());

        return bestEpisodes.stream().mapToDouble(Episode::getEpisodeRating).average().orElse(0.0);

    }

    @Override
    public boolean sameGenre(String genre) {
        return genres.stream().anyMatch(s -> s.equals(genre));
    }
}

class Episode {
    List<Integer> ratings;

    public Episode(String line) {
        String[] parts = line.split("\\s+");
        ratings = Arrays.stream(parts).skip(1).map(Integer::parseInt).collect(Collectors.toList());
    }

    public double getEpisodeRating() {

        if (ratings.isEmpty()) {
            return 0.0;
        }
        double avgRating = ratings.stream().mapToInt(Integer::intValue).average().orElse(0.0);
        double factor = Math.min(ratings.size() / 20.0, 1.0);
        return avgRating * factor;
    }
}

class StreamingPlatform {

    List<Rating> ratings;

    public StreamingPlatform(List<Rating> ratings) {
        this.ratings = ratings;
    }

    public StreamingPlatform() {
        this.ratings = new ArrayList<>();
    }

    public void addItem(String data) {
        String[] parts = data.split(";");

        if (parts.length > 3) {
            // series
            ratings.add(Series.createSeries(data));
        } else {
            // film
            ratings.add(Film.createFilm(data));
        }
    }

    public void listAllItems(PrintStream out) {
        PrintWriter pw = new PrintWriter(out);
        ratings = ratings.stream()
                .sorted(Comparator.comparing(Rating::getRating).reversed())
                .collect(Collectors.toList());
        ratings.forEach(pw::println);
        pw.flush();
    }

    public void listFromGenre(String data, PrintStream out) {
        PrintWriter pw = new PrintWriter(out);
        ratings.stream()
                .filter(genre -> genre.sameGenre(data))
                .sorted(Comparator.comparing(Rating::getRating).reversed())
                .forEach(pw::println);
        pw.flush();
    }
}

public class StreamingPlatformTest {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        StreamingPlatform sp = new StreamingPlatform();
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split(" ");
            String method = parts[0];
            String data = Arrays.stream(parts).skip(1).collect(Collectors.joining(" "));
            if (method.equals("addItem")) {
                sp.addItem(data);
            } else if (method.equals("listAllItems")) {
                sp.listAllItems(System.out);
            } else if (method.equals("listFromGenre")) {
                System.out.println(data);
                sp.listFromGenre(data, System.out);
            }
        }

    }
}
