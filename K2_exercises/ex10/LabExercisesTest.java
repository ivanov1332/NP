package K2_exercises.ex10;

import java.util.*;
import java.util.stream.Collectors;

class Student implements Comparable<Student> {
    String index;
    List<Integer> pointsFromLaboratory;

    public Student(String index, List<Integer> pointsFromLaboratory) {
        this.index = index;
        this.pointsFromLaboratory = pointsFromLaboratory;
    }

    public boolean haveSignature() {
        long count = pointsFromLaboratory.stream().filter(p -> p == 0).count();
        return count <= 2;
    }

    public int getYear() {
        int yearOfIndex = Integer.parseInt(index.substring(0, 2));
        return 2022 - yearOfIndex;
    }

    public String getIndex() {
        return index;
    }

    public double getSumOfPoints() {
        return pointsFromLaboratory.stream().mapToInt(Integer::intValue).sum() / 10.0;
    }

    @Override
    public int compareTo(Student o) {
        Comparator<Student> comparator = Comparator.comparing(Student::getSumOfPoints)
                .thenComparing(Student::getIndex);
        return comparator.compare(this, o);
    }

    @Override
    public String toString() {
        return String.format("%s %s %.2f", index, haveSignature() ? "YES" : "NO", getSumOfPoints());
    }
}

class LabExercises {
    List<Student> students;

    public LabExercises() {
        this.students = new ArrayList<>();
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public void printByAveragePoints(boolean ascending, int n) {
        if (!ascending) {
            students = students.stream().sorted(Comparator.comparing(Student::getSumOfPoints).thenComparing(Student::getIndex).reversed()).collect(Collectors.toList());
        } else {
            students = students.stream().sorted(Comparator.comparing(Student::getSumOfPoints).thenComparing(Student::getIndex)).collect(Collectors.toList());
        }
        students.stream().limit(n).forEach(System.out::println);
    }

    public List<Student> failedStudents() {
        return students.stream()
                .filter(student -> !student.haveSignature())
                .sorted(Comparator.comparing(Student::getIndex)
                        .thenComparing(Student::getSumOfPoints))
                .collect(Collectors.toList());
    }

    public Map<Integer, Double> getStatisticsByYear() {
        Map<Integer, Double> mapOfStudentsByYear = new HashMap<>();
        mapOfStudentsByYear = students.stream()
                .filter(Student::haveSignature)
                .collect(Collectors.groupingBy(Student::getYear,
                        Collectors.averagingDouble(Student::getSumOfPoints)));
        return mapOfStudentsByYear;
    }
}

public class LabExercisesTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        LabExercises labExercises = new LabExercises();
        while (sc.hasNextLine()) {
            String input = sc.nextLine();
            String[] parts = input.split("\\s+");
            String index = parts[0];
            List<Integer> points = Arrays.stream(parts).skip(1)
                    .mapToInt(Integer::parseInt)
                    .boxed()
                    .collect(Collectors.toList());

            labExercises.addStudent(new Student(index, points));
        }

        System.out.println("===printByAveragePoints (ascending)===");
        labExercises.printByAveragePoints(true, 100);
        System.out.println("===printByAveragePoints (descending)===");
        labExercises.printByAveragePoints(false, 100);
        System.out.println("===failed students===");
        labExercises.failedStudents().forEach(System.out::println);
        System.out.println("===statistics by year");
        labExercises.getStatisticsByYear().entrySet().stream()
                .map(entry -> String.format("%d : %.2f", entry.getKey(), entry.getValue()))
                .forEach(System.out::println);

    }
}