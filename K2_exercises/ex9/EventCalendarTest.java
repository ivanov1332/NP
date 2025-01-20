package K2_exercises.ex9;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class WrongDateException extends Exception {
    public WrongDateException(Date date) {
        super(String.format("Wrong date: %s", date));
    }
}

class Event {
    String name;
    String location;
    Date date;

    public Event(String name, String location, Date date) {
        this.name = name;
        this.location = location;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public Date getDate() {
        return date;
    }


    @Override
    public String toString() {
        //dd MMM, YYY HH:mm at [location], [name]
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM, yyy HH:mm");
        return String.format("%s at %s, %s", formatter.format(date), location, name);
    }

    public int getMonth() {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.MONTH);
    }
}

class EventCalendar {
    int year;

    Set<Event> events;


    public EventCalendar(int year) {
        this.year = year;
        this.events = new TreeSet<>(Comparator.comparing(Event::getDate).thenComparing(Event::getName));
    }

    public void addEvent(String name, String location, Date date) throws WrongDateException {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int eventYear = c.get(Calendar.YEAR);
        if (eventYear != year) {
            throw new WrongDateException(date);
        }
        events.add(new Event(name, location, date));
    }

    public void listEvents(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        String targetDate = sdf.format(date);
        List<Event> filtered = events.stream()
                .filter(event -> sdf.format(event.getDate())
                        .equals(targetDate)).collect(Collectors.toList());
        if (filtered.isEmpty()){
            System.out.println("No events on this day!");
            return;
        }
        filtered.forEach(System.out::println);

    }

    public void listByMonth() {
        IntStream.range(0, 12).forEach(i -> {
            long count = events.stream().filter(x -> x.getMonth() == i).count();
            System.out.printf("%d : %d%n", (i + 1), count);
        });
    }
}


public class EventCalendarTest {
    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        int year = scanner.nextInt();
        scanner.nextLine();
        EventCalendar eventCalendar = new EventCalendar(year);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            String name = parts[0];
            String location = parts[1];
            Date date = df.parse(parts[2]);
            try {
                eventCalendar.addEvent(name, location, date);
            } catch (WrongDateException e) {
                System.out.println(e.getMessage());
            }
        }
        Date date = df.parse(scanner.nextLine());
        eventCalendar.listEvents(date);
        eventCalendar.listByMonth();
    }
}

// vashiot kod ovde