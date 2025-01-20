package K1_exercises.ex1;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

class Square {
    int side;

    public Square(int side) {
        this.side = side;
    }

    public int getPerimeter() {
        return 4 * side;
    }

}

class Canvas implements Comparable<Canvas> {
    //364fbe94 24 30 22 33 32 30 37 18 29 27 33 21 27 26
    String windowId;
    List<Square> squareList;

    public Canvas(String windowId, List<Square> squareList) {
        this.windowId = windowId;
        this.squareList = squareList;
    }

    public static Canvas createCanvas(String line) {
        String[] parts = line.split("\\s+");

        String id = parts[0];

        List<Square> squares = Arrays.stream(parts)
                .skip(1)
                .map(Integer::parseInt)
                .map(Square::new)
                .collect(Collectors.toList());

        return new Canvas(id, squares);
    }

    @Override
    public String toString() {
        return String.format("%s %d %d", windowId
                , squareList.size()
                , squareList.stream().mapToInt(Square::getPerimeter).sum());
    }

    public int compareTo(Canvas o) {
        return Integer.compare(this.squareList.stream().mapToInt(Square::getPerimeter).sum(),
                o.squareList.stream().mapToInt(Square::getPerimeter).sum());
    }
}

class ShapesApplication {

    public List<Canvas> canvasList;

    public ShapesApplication() {
        canvasList = new ArrayList<>();
    }

    public ShapesApplication(List<Canvas> canvasList) {
        this.canvasList = canvasList;
    }

    public int readCanvases(InputStream in) {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        canvasList = br.lines().map(Canvas::createCanvas).collect(Collectors.toList());
        return canvasList.stream().mapToInt(canvas -> canvas.squareList.size()).sum();
    }

    public void printLargestCanvasTo(PrintStream out) {
        PrintWriter pw = new PrintWriter(out);
        Canvas max = canvasList.stream().max(Comparator.naturalOrder()).get();
        pw.println(max);
        pw.flush();
    }
}

public class Shapes1Test {

    public static void main(String[] args) {
        ShapesApplication shapesApplication = new ShapesApplication();

        System.out.println("===READING SQUARES FROM INPUT STREAM===");
        System.out.println(shapesApplication.readCanvases(System.in));
        System.out.println("===PRINTING LARGEST CANVAS TO OUTPUT STREAM===");
        shapesApplication.printLargestCanvasTo(System.out);

    }
}