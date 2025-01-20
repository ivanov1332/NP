package K1_exercises.ex9;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

class InvalidOperationException extends Exception {
    public InvalidOperationException(char option) {
        super(String.format("%c is not allowed option for this question", option));
    }

    public InvalidOperationException(String s) {
        super(s);
    }
}

class Question implements Comparable<Question> {
    String typeQuestion;
    String text;
    int points;

    public Question(String typeQuestion, String text, int points) {
        this.typeQuestion = typeQuestion;
        this.text = text;
        this.points = points;
    }

    public static Question createQuestion(String line) throws InvalidOperationException {
        String[] parts = line.split(";");
        String typeQuestion = parts[0];
        String text = parts[1];
        int points = Integer.parseInt(parts[2]);
        String answer = parts[3];

        if (typeQuestion.equals("TF")) {
            boolean correctAnswer = Boolean.parseBoolean(answer);
            return new TrueFalseQuestion(typeQuestion, text, points, correctAnswer);
        } else {
            if (!"ABCDE".contains(answer)) {
                throw new InvalidOperationException(answer.charAt(0));
            }
            char correctAnswer = answer.charAt(0);
            return new MultipleQuestion(typeQuestion, text, points, correctAnswer);
        }
    }

    public double totalPoints(String givenAnswer) {
        return 0;
    }

    @Override
    public int compareTo(Question o) {
        return Double.compare(this.points, o.points);
    }
}

class TrueFalseQuestion extends Question {

    boolean answer;

    public TrueFalseQuestion(String typeQuestion, String text, int points, boolean answer) {
        super(typeQuestion, text, points);
        this.answer = answer;
    }

    public TrueFalseQuestion(String typeQuestion, String text, int points) {
        super(typeQuestion, text, points);
    }

    @Override
    public double totalPoints(String givenAnswer) {
        boolean ans = Boolean.parseBoolean(givenAnswer);
        if (ans == answer) {
            return points;
        }
        return 0;
    }

    @Override
    public String toString() {
        return String.format("True/False Question: %s Points: %d Answer: %b",text,points,answer);
    }
}

class MultipleQuestion extends Question {

    char answer;


    public MultipleQuestion(String typeQuestion, String text, int points, char answer) {
        super(typeQuestion, text, points);
        this.answer = answer;
    }

    @Override
    public double totalPoints(String givenAnswer) {
        char ans = givenAnswer.charAt(0);
        if (answer == ans) {
            return points;
        }
        return points * (-0.2);
    }

    @Override
    public String toString() {
        return String.format("Multiple Choice Question: %s Points %d Answer: %c",text,points,answer);
    }
}

class Quiz {

    public List<Question> questions;

    public Quiz(List<Question> questions) {
        this.questions = questions;
    }

    Quiz() {
        this.questions = new ArrayList<>();
    }


    public void addQuestion(String questionData) {
        try {
            Question question = Question.createQuestion(questionData);
            questions.add(question);
        } catch (InvalidOperationException e) {
            System.out.println(e.getMessage());
        }
    }

    public void printQuiz(PrintStream out) {
        PrintWriter pw = new PrintWriter(out);
        questions
                .stream()
                .sorted(Comparator.reverseOrder())
                .forEach(pw::println);
        pw.flush();
    }

    public void answerQuiz(List<String> answers, PrintStream out) throws InvalidOperationException {
        if (answers.size() != questions.size()){
            throw new InvalidOperationException("Answers and questions must be of same length!");
        }

        PrintWriter pw = new PrintWriter(out);
        double totalScore = 0;

        for (int i = 0 ; i < questions.size() ; i++){
            Question q = questions.get(i);
            String answer = answers.get(i);
            double pointsEarned = q.totalPoints(answer);
            totalScore += pointsEarned;
            pw.println(String.format("%d. %.2f",i + 1,pointsEarned));
        }
        pw.println(String.format("Total points: %.2f",totalScore));
        pw.flush();
    }
}

public class QuizTest {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        Quiz quiz = new Quiz();

        int questions = Integer.parseInt(sc.nextLine());

        for (int i = 0; i < questions; i++) {
            quiz.addQuestion(sc.nextLine());
        }

        List<String> answers = new ArrayList<>();

        int answersCount = Integer.parseInt(sc.nextLine());

        for (int i = 0; i < answersCount; i++) {
            answers.add(sc.nextLine());
        }

        int testCase = Integer.parseInt(sc.nextLine());

        if (testCase == 1) {
            quiz.printQuiz(System.out);
        } else if (testCase == 2) {
            try {
                quiz.answerQuiz(answers, System.out);
            } catch (InvalidOperationException e) {
                System.out.println(e.getMessage());
//                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Invalid test case");
        }
    }
}
