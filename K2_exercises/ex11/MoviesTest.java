package K2_exercises.ex11;

import java.util.*;
import java.util.stream.Collectors;

class Movie implements Comparable<Movie> {
    String title;
    int[] ratings;

    public Movie(String title, int[] ratings) {
        this.title = title;
        this.ratings = ratings;
    }

    public double getRatingCoefficient(int maxRatings) {
        return getAverageRating() * ratings.length / (double) maxRatings;
    }

    public String getTitle() {
        return title;
    }

    public int[] getRatings() {
        return ratings;
    }

    @Override
    public String toString() {
        return String.format("%s (%.2f) of %d ratings", title, getAverageRating(), ratings.length);
    }

    public double getAverageRating() {
        double sum = 0;
        double average = 0;
        for (int i = 0; i < ratings.length; i++) {
            sum += ratings[i];
        }
        average = sum / ratings.length;
        return average;
    }

    @Override
    public int compareTo(Movie o) {
        Comparator<Movie> comparator = Comparator.comparing(Movie::getAverageRating).thenComparing(Movie::getTitle);
        return comparator.compare(this, o);
    }
}

class MoviesList {
    List<Movie> movies;

    public MoviesList() {
        this.movies = new ArrayList<>();
    }

    public void addMovie(String title, int[] ratings) {
        movies.add(new Movie(title, ratings));
    }

    public List<Movie> top10ByAvgRating() {
        return movies.stream()
                .sorted(Comparator.comparingDouble(Movie::getAverageRating)
                        .reversed()
                        .thenComparing(Movie::getTitle))
                .limit(10)
                .collect(Collectors.toList());
    }

    public List<Movie> top10ByRatingCoef() {
        int maxRatings = movies.stream()
                .mapToInt(movie -> movie.getRatings().length)
                .max()
                .orElse(1);  // Finding the maximum number of ratings among all movies

        return movies.stream()
                .sorted(Comparator.comparingDouble((Movie movie) -> movie.getRatingCoefficient(maxRatings))
                        .reversed()
                        .thenComparing(Movie::getTitle))
                .limit(10)
                .collect(Collectors.toList());
    }
}

public class MoviesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MoviesList moviesList = new MoviesList();
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            int x = scanner.nextInt();
            int[] ratings = new int[x];
            for (int j = 0; j < x; ++j) {
                ratings[j] = scanner.nextInt();
            }
            scanner.nextLine();
            moviesList.addMovie(title, ratings);
        }
        scanner.close();
        List<Movie> movies = moviesList.top10ByAvgRating();
        System.out.println("=== TOP 10 BY AVERAGE RATING ===");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
        movies = moviesList.top10ByRatingCoef();
        System.out.println("=== TOP 10 BY RATING COEFFICIENT ===");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
    }
}

// vashiot kod ovde