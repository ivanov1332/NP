package K2_exercises.ex3;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

class File implements Comparable<File> {
    String name;
    int size;
    LocalDateTime createdAt;

    public int getYear(){
        return createdAt.getYear();
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public String getMonthAndDay() {
        return createdAt.getMonth() + "-" + createdAt.getDayOfMonth();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public File(String name, int size, LocalDateTime createdAt) {
        this.name = name;
        this.size = size;
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return String.format("%-10s %5dB %s", name, size, createdAt);
    }

    @Override
    public int compareTo(File o) {
        Comparator<File> comparator = Comparator.comparing(File::getCreatedAt)
                .thenComparing(File::getName)
                .thenComparing(File::getSize);
        return comparator.compare(this, o);
    }
}

class FileSystem {

    Map<Character, TreeSet<File>> folders;

    public FileSystem() {
        this.folders = new HashMap<>();
    }

    public void addFile(char folder, String name, int size, LocalDateTime createdAt) {
        folders.computeIfAbsent(folder, x -> new TreeSet<>());
        folders.get(folder).add(new File(name, size, createdAt));
    }

    public List<File> findAllHiddenFilesWithSizeLessThen(int size){
        return folders.values()
                .stream()
                .flatMap(Set::stream)
                .filter(folder -> folder.name.startsWith(".") && folder.size < size)
                .collect(Collectors.toList());
    }
    public int totalSizeOfFilesFromFolders(List<Character> f){
        return f.stream()
                .map(folders::get)
                .flatMap(Set::stream)
                .mapToInt(File::getSize)
                .sum();
    }

    public Map<Integer, Set<File>> byYear(){
        return folders.values()
                .stream()
                .flatMap(Set::stream)
                .collect(Collectors.groupingBy(File::getYear, Collectors.toSet()));
    }

    public Map<String, Long> sizeByMonthAndDay() {
        return folders.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.groupingBy(
                        File::getMonthAndDay,
                        Collectors.summingLong(File::getSize)
                ));
    }

}

public class FileSystemTest {
    public static void main(String[] args) {
        FileSystem fileSystem = new FileSystem();
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; i++) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            fileSystem.addFile(parts[0].charAt(0), parts[1],
                    Integer.parseInt(parts[2]),
                    LocalDateTime.of(2016, 12, 29, 0, 0, 0).minusDays(Integer.parseInt(parts[3]))
            );
        }
        int action = scanner.nextInt();
        if (action == 0) {
            scanner.nextLine();
            int size = scanner.nextInt();
            System.out.println("== Find all hidden files with size less then " + size);
            List<File> files = fileSystem.findAllHiddenFilesWithSizeLessThen(size);
            files.forEach(System.out::println);
        } else if (action == 1) {
            scanner.nextLine();
            String[] parts = scanner.nextLine().split(":");
            System.out.println("== Total size of files from folders: " + Arrays.toString(parts));
            int totalSize = fileSystem.totalSizeOfFilesFromFolders(Arrays.stream(parts)
                    .map(s -> s.charAt(0))
                    .collect(Collectors.toList()));
            System.out.println(totalSize);
        } else if (action == 2) {
            System.out.println("== Files by year");
            Map<Integer, Set<File>> byYear = fileSystem.byYear();
            byYear.keySet().stream().sorted()
                    .forEach(key -> {
                        System.out.printf("Year: %d\n", key);
                        Set<File> files = byYear.get(key);
                        files.stream()
                                .sorted()
                                .forEach(System.out::println);
                    });
        } else if (action == 3) {
            System.out.println("== Size by month and day");
            Map<String, Long> byMonthAndDay = fileSystem.sizeByMonthAndDay();
            byMonthAndDay.keySet().stream().sorted()
                    .forEach(key -> System.out.printf("%s -> %d\n", key, byMonthAndDay.get(key)));
        }
        scanner.close();
    }
}

// Your code here

