package K2_exercises.ex13;

import java.io.*;
import java.util.*;

/**
 * January 2016 Exam problem 1
 */
class Records implements Comparable<Records>{
    String code;
    String direction;
    int [] grades;

    public Records(String code, String direction, int[] grades) {
        this.code = code;
        this.direction = direction;
        this.grades = grades;
    }

    public static Records createRecord(String line){
        /**
         * cuhjd3 PET 8 6 10 8 7 9 10 9
         */
        String [] parts = line.split("\\s+");
        String code = parts[0];
        String direction = parts[1];
        int [] grades;
        grades = Arrays.stream(parts)
                .skip(2)
                .mapToInt(Integer::parseInt)
                .toArray();
        return new Records(code,direction,grades);
    }

    public double getAverageGrade(){
        return Arrays.stream(grades).mapToDouble(x -> x).average().orElse(0.0);
    }

    public String getCode() {
        return code;
    }

    public String getDirection() {
        return direction;
    }

    public int[] getGrades() {
        return grades;
    }

    @Override
    public String toString() {
        return String.format("%s %.2f",code,getAverageGrade());
    }

    @Override
    public int compareTo(Records o) {
        Comparator<Records> comparator = Comparator.comparing(Records::getAverageGrade).thenComparing(Records::getCode);
        return comparator.compare(this,o);
    }
}

class StudentRecords{
    List<Records> studentRecords;

    public StudentRecords() {
        this.studentRecords = new ArrayList<>();
    }
    int readRecords(InputStream inputStream){
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        return (int) br.lines().map(Records::createRecord).count();
    }
    void writeTable(OutputStream outputStream){
        PrintWriter pw = new PrintWriter(outputStream);

        Map<String,List<Records>> groupedByDirection = new HashMap<>();
        for (Records record : studentRecords){
            groupedByDirection.computeIfAbsent(record.direction, k -> new ArrayList<>()).add(record);
        }
    }
}

public class StudentRecordsTest {
    public static void main(String[] args) {
        System.out.println("=== READING RECORDS ===");
        StudentRecords studentRecords = new StudentRecords();
        int total = studentRecords.readRecords(System.in);
        System.out.printf("Total records: %d\n", total);
        System.out.println("=== WRITING TABLE ===");
        studentRecords.writeTable(System.out);
        System.out.println("=== WRITING DISTRIBUTION ===");
        studentRecords.writeDistribution(System.out);
    }
}

// your code here