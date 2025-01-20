package K2_exercises.ex8;

import java.util.*;

import java.util.Scanner;

class DuplicateNumberException extends Exception {
    public DuplicateNumberException(String number) {
        super(String.format("Duplicate number: %s", number));
    }
}

class Contact implements Comparable<Contact> {
    String name;
    String number;

    public Contact(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return String.format("%s %s", name, number);
    }

    @Override
    public int compareTo(Contact o) {
        Comparator<Contact> comparator = Comparator.comparing(Contact::getName).thenComparing(Contact::getNumber);
        return comparator.compare(this, o);
    }
}

class PhoneBook {
    Set<String> allPhoneNumbers;
    Map<String, Set<Contact>> contactsBySubString;
    Map<String,Set<Contact>> contactsByName;

    public PhoneBook() {
        this.contactsBySubString = new HashMap<>();
        this.allPhoneNumbers = new HashSet<>();
        this.contactsByName = new HashMap<>();
    }

    private List<String> getSubstring(String phone) {
        List<String> resultList = new ArrayList<>();
        for (int len = 3; len <= phone.length(); len++) {
            for (int j = 0; j <= phone.length() - len; j++) {
                resultList.add(phone.substring(j, j + len));
            }
        }
        return resultList;
    }

    public void addContact(String name, String number) throws DuplicateNumberException {
        if (allPhoneNumbers.contains(number)) {
            throw new DuplicateNumberException(number);
        } else {
            allPhoneNumbers.add(number);
            Contact c = new Contact(name, number);
            List<String> subNumbers = getSubstring(number);
            for (String subNumber : subNumbers) {
                contactsBySubString.putIfAbsent(subNumber, new TreeSet<>());
                contactsBySubString.get(subNumber).add(c);
            }
            contactsByName.putIfAbsent(name,new TreeSet<>());
            contactsByName.get(name).add(c);
        }

    }

    public void contactsByNumber(String number) {
        Set<Contact> contacts = contactsBySubString.get(number);
        if (contacts == null) {
            System.out.println("NOT FOUND");
            return;
        }
        contacts.forEach(System.out::println);
    }

    public void contactsByName(String name) {
        Set<Contact> contacts = contactsByName.get(name);
        if (contacts == null) {
            System.out.println("NOT FOUND");
            return;
        }
        contacts.forEach(System.out::println);
    }
}


public class PhoneBookTest {

    public static void main(String[] args) {
        PhoneBook phoneBook = new PhoneBook();
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            try {
                phoneBook.addContact(parts[0], parts[1]);
            } catch (DuplicateNumberException e) {
                System.out.println(e.getMessage());
            }
        }
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.println(line);
            String[] parts = line.split(":");
            if (parts[0].equals("NUM")) {
                phoneBook.contactsByNumber(parts[1]);
            } else {
                phoneBook.contactsByName(parts[1]);
            }
        }
    }

}

