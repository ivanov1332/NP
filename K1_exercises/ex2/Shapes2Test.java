package K1_exercises.ex2;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

class IrregularCanvasException extends Exception {
    public IrregularCanvasException(String id, double max_area) {
        super(String.format("Canvas %s has a shape with area larger than %.2f", id, max_area));
    }
}

enum TypeShape {
    C, S
}

class Shape implements Comparable<Shape> {
    double side;
    TypeShape typeShape;

    public Shape(double side, TypeShape typeShape) {
        this.side = side;
        this.typeShape = typeShape;
    }

    public TypeShape getTypeShape() {
        return typeShape;
    }

    public double getArea() {
        return 0;
    }

    @Override
    public int compareTo(Shape o) {
        return Double.compare(this.getArea(), o.getArea());
    }
}


class Square extends Shape {

    public Square(double side) {
        super(side, TypeShape.S);
    }

    @Override
    public double getArea() {
        return side * side;
    }

}

class Circle extends Shape {
    public Circle(double side) {
        super(side, TypeShape.C);
    }

    @Override
    public double getArea() {
        return side * side * Math.PI;
    }
}

class Canvas implements Comparable<Canvas> {
    String canvasId;
    List<Shape> shapes;

    public Canvas() {
        shapes = new ArrayList<>();
    }

    public Canvas(String canvasId, List<Shape> shapes) {
        this.canvasId = canvasId;
        this.shapes = shapes;
    }

    public double totalArea() {
        return shapes.stream().mapToDouble(Shape::getArea).sum();
    }

    @Override
    public String toString() {
        DoubleSummaryStatistics summaryStatistics = shapes.stream().mapToDouble(Shape::getArea).summaryStatistics();
        return String.format("%s %d %d %d %.2f %.2f %.2f", canvasId,
                shapes.size(),
                shapes.stream().filter(type -> type.getTypeShape().equals(TypeShape.C)).count(),
                shapes.stream().filter(type -> type.getTypeShape().equals(TypeShape.S)).count(),
                summaryStatistics.getMin(),
                summaryStatistics.getMax(),
                summaryStatistics.getAverage()
        );
    }


    public static Canvas createCanvas(String line, double maxArea) throws IrregularCanvasException {
        String[] parts = line.split("\\s+");
        String id = parts[0];
        ArrayList<Shape> shapeArrayList = new ArrayList<>();

        for (int i = 1; i < parts.length; i += 2) {
            String shapeType = parts[i];
            int dimension = Integer.parseInt(parts[i + 1]);

            Shape shape;

            if (shapeType.equals("C")) {
                shape = new Circle(dimension);
            } else {
                shape = new Square(dimension);
            }
            if (shape.getArea() > maxArea) {
                throw new IrregularCanvasException(id, maxArea);
            }
            shapeArrayList.add(shape);
        }
        return new Canvas(id, shapeArrayList);
    }


    @Override
    public int compareTo(Canvas o) {
        return Double.compare(o.totalArea(), this.totalArea());
    }
}

class ShapesApplication {

    double maxArea;
    List<Canvas> canvasList;


    public ShapesApplication(double maxArea) {
        this.maxArea = maxArea;
        canvasList = new ArrayList<>();
    }

    public void readCanvases(InputStream in) {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        canvasList = br.lines()
                .map(line -> {
                    try {
                        return Canvas.createCanvas(line, maxArea);
                    } catch (IrregularCanvasException e) {
                        System.out.println(e.getMessage());
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

    }

    public void printCanvases(PrintStream out) {
        PrintWriter pw = new PrintWriter(out);
        canvasList.stream().sorted((c1, c2) -> Double.compare(c2.totalArea(), c1.totalArea())).forEach(pw::println);
        pw.flush();
    }
}

public class Shapes2Test {

    public static void main(String[] args) {

        ShapesApplication shapesApplication = new ShapesApplication(10000);

        System.out.println("===READING CANVASES AND SHAPES FROM INPUT STREAM===");
        shapesApplication.readCanvases(System.in);

        System.out.println("===PRINTING SORTED CANVASES TO OUTPUT STREAM===");
        shapesApplication.printCanvases(System.out);


    }
}