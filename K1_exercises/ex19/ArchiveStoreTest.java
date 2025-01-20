package K1_exercises.ex19;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

class NonExistingItemException extends Exception {
    public NonExistingItemException(String message) {
        super(message);
    }

    public NonExistingItemException(int id) {
        super(String.format("Item with id %d doesn't exist",id));
    }
}

class Archive {
    int id;
    LocalDate dateArchived;

    public Archive(int id, LocalDate dateArchived) {
        this.id = id;
        this.dateArchived = dateArchived;
    }

    Archive(int id) {
        this.id = id;
    }


    public int getId() {
        return id;
    }

    public LocalDate getDateArchived() {
        return dateArchived;
    }

    public void setDateArchived(LocalDate dateArchived) {
        this.dateArchived = dateArchived;
    }
}

class LockedArchive extends Archive {
    LocalDate dateToOpen;

    public LockedArchive(int id, LocalDate dateArchived, LocalDate dateToOpen) {
        super(id, dateArchived);
        this.dateToOpen = dateToOpen;
    }

    LockedArchive(int id, LocalDate dateToOpen) {
        super(id);
        this.dateToOpen = dateToOpen;
    }

    public LocalDate getDateToOpen() {
        return dateToOpen;
    }
}

class SpecialArchive extends Archive {
    int maxOpen;
    int currentOpens;

    public SpecialArchive(int id, LocalDate dateArchived, int maxOpen) {
        super(id, dateArchived);
        this.maxOpen = maxOpen;
    }

    SpecialArchive(int id, int maxOpen) {
        super(id);
        this.maxOpen = maxOpen;
    }

    public int getMaxOpen() {
        return maxOpen;
    }

    public void open() {
        currentOpens++;
    }

    public int getCurrentOpens() {
        return currentOpens;
    }
}

class ArchiveStore {
    List<Archive> archivesItems;
    List<String> logs;

    public ArchiveStore() {
        this.archivesItems = new ArrayList<>();
        logs = new ArrayList<>();
    }

    public void archiveItem(Archive item, LocalDate date) {
        archivesItems.add(item);
        archivesItems.get(archivesItems.size() - 1).setDateArchived(date);
        logs.add("Item " + item.getId() + " archived at " + date);
    }

    void openItem(int id, LocalDate date) throws NonExistingItemException {
        Archive archiveItem = archivesItems.stream().filter(item -> item.getId() == id).findFirst().orElse(null);

        if (archiveItem == null) {
            throw new NonExistingItemException(id);
        }

        if (archiveItem instanceof LockedArchive) {
            LockedArchive locked = (LockedArchive) archiveItem;
            if (date.isBefore(locked.getDateToOpen())) {
                logs.add("Item " + locked.getId() + " cannot be opened before " + locked.getDateToOpen());
                return;
            }
        } else if (archiveItem instanceof SpecialArchive) {
            SpecialArchive specialArchiveItem = (SpecialArchive) archiveItem;

            if (specialArchiveItem.getCurrentOpens() >= specialArchiveItem.getMaxOpen()) {
                logs.add("Item " + specialArchiveItem.getId() + " cannot be opened more than " + specialArchiveItem.getMaxOpen() + " times");
                return;
            }
            specialArchiveItem.open();
        }
        logs.add("Item " + archiveItem.getId() + " opened at " + date);
    }

    String getLog() {
        StringBuilder sb = new StringBuilder();
        logs.forEach(item -> sb.append(item).append("\n"));
        return sb.toString();
    }
}

public class ArchiveStoreTest {
    public static void main(String[] args) {
        ArchiveStore store = new ArchiveStore();
        LocalDate date = LocalDate.of(2013, 10, 7);
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        int n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        int i;
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            long days = scanner.nextLong();

            LocalDate dateToOpen = date.atStartOfDay().plusSeconds(days * 24 * 60 * 60).toLocalDate();
            LockedArchive lockedArchive = new LockedArchive(id, dateToOpen);
            store.archiveItem(lockedArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            int maxOpen = scanner.nextInt();
            SpecialArchive specialArchive = new SpecialArchive(id, maxOpen);
            store.archiveItem(specialArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        while (scanner.hasNext()) {
            int open = scanner.nextInt();
            try {
                store.openItem(open, date);
            } catch (NonExistingItemException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println(store.getLog());
    }
}