package K2_exercises.ex6;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

class InvalidIDException extends Exception {
    public InvalidIDException(String id) {
        super(String.format("ID " + id + " is not valid"));
    }
}

class InvalidDimensionException extends Exception {
    InvalidDimensionException() {
        super("Dimension 0 is not allowed!");
    }
}


interface IShape {
    double getPerimeter();

    double getArea();

    void scale(double coefficient);

    String getUserId();
}

class Rectangle implements IShape {
    double a;
    double b;
    String userId;

    public Rectangle(double a, double b) {
        this.a = a;
        this.b = b;
    }

    public static Rectangle createRectangle(String line) {
        String[] parts = line.split("\\s+");
        //3 123456 12.4201 8.7382
        int type = Integer.parseInt(parts[0]);
        String id = parts[1];
        double a = Double.parseDouble(parts[2]);
        double b = Double.parseDouble(parts[3]);
        return new Rectangle(a, b);
    }

    @Override
    public String toString() {
        return String.format("Rectangle: -> Sides: %.2f, %.2f Area: %.2f Perimeter: %.2f", a, b, getArea(), getPerimeter());
    }

    @Override
    public double getPerimeter() {
        return 2 * a + 2 * b;
    }

    @Override
    public double getArea() {
        return a * b;
    }

    @Override
    public void scale(double coefficient) {
        this.a *= coefficient;
        this.b *= coefficient;
    }

    @Override
    public String getUserId() {
        return this.userId;
    }
}

class Square implements IShape {
    double side;
    String userId;

    public Square(double side) {
        this.side = side;
    }

    @Override
    public String toString() {
        return String.format("Square: -> Side: %.2f Area: %.2f Perimeter: %.2f", side, getArea(), getPerimeter());
    }

    public static Square createSquare(String line) {
        //1 123456 4.8835
        String[] parts = line.split("\\s+");
        int type = Integer.parseInt(parts[0]);
        String id = parts[1];
        double side = Double.parseDouble(parts[2]);
        return new Square(side);
    }

    @Override
    public double getPerimeter() {
        return 4 * side;
    }

    @Override
    public double getArea() {
        return side * side;
    }

    @Override
    public void scale(double coefficient) {
        this.side *= coefficient;
    }

    @Override
    public String getUserId() {
        return this.userId;
    }
}

class Circle implements IShape {
    double radius;
    String userId;

    public Circle(double radius) {
        this.radius = radius;
    }

    @Override
    public String toString() {
        return String.format("Circle -> Radius: %.2f Area: %.2f Perimeter: %.2f", radius, getArea(), getPerimeter());
    }

    public static Circle createCircle(String line) {
        String[] parts = line.split("\\s+");
        //1 123456 4.8835
        int type = Integer.parseInt(parts[0]);
        String id = parts[1];
        double radius = Double.parseDouble(parts[2]);
        return new Circle(radius);
    }

    @Override
    public double getPerimeter() {
        return 2 * radius * Math.PI;
    }

    @Override
    public double getArea() {
        return Math.pow(radius, 2) * Math.PI;
    }

    @Override
    public void scale(double coefficient) {
        this.radius *= coefficient;
    }

    @Override
    public String getUserId() {
        return this.userId;
    }
}

class Canvas {
    List<IShape> shapes;
    String id;

    public Canvas() {
        this.shapes = new ArrayList<>();
        this.id = "";
    }

    public boolean validId(String id) throws InvalidIDException {
        if (id.length() != 6 || !id.matches("[a-zA-Z0-9]+")) {
            throw new InvalidIDException(id);
        }
        return true;
    }

    void readShapes(InputStream is) {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        shapes = br.lines()
                .map(line -> {
                    try {
                        String[] parts = line.split("\\s+");
                        int type = Integer.parseInt(parts[0]);
                        String id = parts[1];

                        validId(id);

                        switch (type) {
                            case 1: // Circle
                                double radius = Double.parseDouble(parts[2]);
                                if (radius == 0) throw new InvalidDimensionException();
                                return new Circle(radius);
                            case 2: // Square
                                double side = Double.parseDouble(parts[2]);
                                if (side == 0) throw new InvalidDimensionException();
                                return new Square(side);
                            case 3: // Rectangle
                                double a = Double.parseDouble(parts[2]);
                                double b = Double.parseDouble(parts[3]);
                                if (a == 0 || b == 0) throw new InvalidDimensionException();
                                return new Rectangle(a, b);
                            default:
                                return null;
                        }
                    } catch (InvalidIDException | InvalidDimensionException e) {
                        System.out.println(e.getMessage());
                        return null; // Skip this shape if an exception occurs
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    void scaleShapes(String userID, double coef) {
        shapes.stream()
                .filter(shape -> shape.getUserId().equals(userID))
                .forEach(shape -> shape.scale(coef));
    }

    void printAllShapes(OutputStream os) {
        PrintWriter pw = new PrintWriter(os);
        shapes.stream().sorted(Comparator.comparing(IShape::getArea)).forEach(pw::println);
        pw.flush();
    }

    void printByUserId(OutputStream os) {
        PrintWriter pw = new PrintWriter(os);
        Map<String, List<IShape>> shapesByUser = new HashMap<>();
    }

    void statistics(OutputStream os) {
        //count: 5
        //sum: 852.06
        //min: 51.86
        //average: 170.41
        //max: 306.99
        DoubleSummaryStatistics summaryStatistics = shapes.stream().mapToDouble(IShape::getArea).summaryStatistics();
        PrintWriter pw = new PrintWriter(os);
        pw.println(String.format("count: %d\n" +
                        "sum: %.2f\n" +
                        "min: %.2f\n" +
                        "average: %.2f\n" +
                        "max: %.2f",
                summaryStatistics.getCount(),
                summaryStatistics.getSum(),
                summaryStatistics.getMin(),
                summaryStatistics.getAverage(),
                summaryStatistics.getMax()));
        pw.flush();
    }
}

public class CanvasTest {

    public static void main(String[] args) {
        Canvas canvas = new Canvas();

        System.out.println("READ SHAPES AND EXCEPTIONS TESTING");
        canvas.readShapes(System.in);

        System.out.println("BEFORE SCALING");
        canvas.printAllShapes(System.out);
        canvas.scaleShapes("123456", 1.5);
        System.out.println("AFTER SCALING");
        canvas.printAllShapes(System.out);

        System.out.println("PRINT BY USER ID TESTING");
        canvas.printByUserId(System.out);

        System.out.println("PRINT STATISTICS");
        canvas.statistics(System.out);
    }
}