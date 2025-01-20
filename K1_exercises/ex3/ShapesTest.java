package K1_exercises.ex3;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

enum Color {
    RED, GREEN, BLUE
}

interface Scalable {
    void scale(float scaleFactor);
}

interface Stackable extends Comparable<Stackable> {
    float weight();

    @Override
    default int compareTo(Stackable o) {
        return Float.compare(this.weight(), o.weight());
    }

}

class Shape implements Scalable, Stackable {
    String id;
    Color color;

    public Shape(String id, Color color) {
        this.id = id;
        this.color = color;
    }


    @Override
    public void scale(float scaleFactor) {
    }

    @Override
    public float weight() {
        return 0;
    }
}

class Circle extends Shape {
    float radius;

    public Circle(String id, Color color, float radius) {
        super(id, color);
        this.radius = radius;
    }

    @Override
    public void scale(float scaleFactor) {
        radius *= scaleFactor;
    }

    @Override
    public float weight() {
        return (float) (radius * radius * Math.PI);
    }

    @Override
    public String toString() {
        String[] Colors = {"RED", "GREEN", "BLUE"};
        //C: [id:5 spaces] [color:10 spaces] [weight:10.2 format]
        return String.format("C: %-5s%-10s%10.2f", id, Colors[color.ordinal()], weight());
    }
}

class Rectangle extends Shape {

    float width;
    float height;

    public Rectangle(String id, Color color, float width, float height) {
        super(id, color);
        this.width = width;
        this.height = height;
    }

    @Override
    public void scale(float scaleFactor) {
        width *= scaleFactor;
        height *= scaleFactor;
    }

    @Override
    public float weight() {
        return width * height;
    }

    @Override
    public String toString() {
        String[] Colors = {"RED", "GREEN", "BLUE"};
        return String.format("R: %-5s%-10s%10.2f", id, Colors[color.ordinal()], weight());
    }
}

class Canvas {

    List<Shape> shapeList;

    public Canvas() {
        shapeList = new ArrayList<>();
    }

    public Canvas(List<Shape> shapeList) {
        this.shapeList = shapeList;
    }

    public void add(String id, Color color, float radius) {
        Circle c = new Circle(id, color, radius);
        insertShape(c);

    }

    public void add(String id, Color color, float width, float height) {
        Rectangle r = new Rectangle(id, color, width, height);
        insertShape(r);
    }

    public void insertShape(Shape shape) {
        if (shapeList.isEmpty()) {
            shapeList.add(shape);
            return;
        }
        for (int i = 0; i < shapeList.size(); i++) {
            if (shapeList.get(i).weight() < shape.weight()) {
                shapeList.add(i, shape);
                return;
            }
        }
        shapeList.add(shape);
    }

    public void scale(String id, float scaleFactor) {
        for (int i = 0; i < shapeList.size(); i++) {
            if (shapeList.get(i).id.equals(id)) {
                Shape s = shapeList.get(i);
                shapeList.remove(s);
                s.scale(scaleFactor);
                insertShape(s);
                break;
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Shape shape : shapeList) {
            sb.append(shape.toString()).append("\n");
        }
        return sb.toString();
    }
}

public class ShapesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Canvas canvas = new Canvas();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            int type = Integer.parseInt(parts[0]);
            String id = parts[1];
            if (type == 1) {
                Color color = Color.valueOf(parts[2]);
                float radius = Float.parseFloat(parts[3]);
                canvas.add(id, color, radius);
            } else if (type == 2) {
                Color color = Color.valueOf(parts[2]);
                float width = Float.parseFloat(parts[3]);
                float height = Float.parseFloat(parts[4]);
                canvas.add(id, color, width, height);
            } else if (type == 3) {
                float scaleFactor = Float.parseFloat(parts[2]);
                System.out.println("ORIGNAL:");
                System.out.print(canvas);
                canvas.scale(id, scaleFactor);
                System.out.printf("AFTER SCALING: %s %.2f\n", id, scaleFactor);
                System.out.print(canvas);
            }

        }
    }
}