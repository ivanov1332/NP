package K1_exercises.ex15;

import java.util.*;

class CategoryNotFoundException extends Exception{
    public CategoryNotFoundException(String categoryName) {
        super(String.format("Category %s was not found",categoryName));
    }
}

abstract class NewsItem {
    String title;
    Date date;
    Category category;

    public NewsItem(String title, Date date, Category category) {
        this.title = title;
        this.date = date;
        this.category = category;
    }

    public abstract String getTeaser();
}

class MediaNewsItem extends NewsItem {
    String url;
    int views;

    public MediaNewsItem(String title, Date date, Category category, String url, int views) {
        super(title, date, category);
        this.url = url;
        this.views = views;
    }

    @Override
    public String getTeaser() {
        long minutesAgo = (new Date().getTime() - date.getTime()) / 60000;
        return String.format("%s\n%d\n%s\n%d",title,minutesAgo,url,views);
    }
}

class TextNewsItem extends NewsItem {
    String textOfTheNews;

    public TextNewsItem(String title, Date date, Category category, String textOfTheNews) {
        super(title, date, category);
        this.textOfTheNews = textOfTheNews;
    }

    @Override
    public String getTeaser() {
        long minutesAgo = (new Date().getTime() - date.getTime()) / 60000;
        String teaserText = textOfTheNews.length() > 80 ? textOfTheNews.substring(0, 80) : textOfTheNews;
        return String.format("%s\n%d\n%s", title, minutesAgo, teaserText);
    }
}


class Category {
    String nameOfCategory;

    public Category(String nameOfCategory) {
        this.nameOfCategory = nameOfCategory;
    }

    public String getNameOfCategory() {
        return nameOfCategory;
    }

    public void setNameOfCategory(String nameOfCategory) {
        this.nameOfCategory = nameOfCategory;
    }
}

class FrontPage {
    List<NewsItem> news;
    Category[] categories;

    FrontPage() {
        this.news = new ArrayList<>();
    }

    public FrontPage(Category[] categories) {
        this.categories = categories;
    }

    public void addNewsItem(NewsItem newsItem) {
        if (news != null){
            news.add(newsItem);
        }

    }

    public List<NewsItem> listByCategory(Category category) {
        List<NewsItem> itemsWithGivenCategory = new ArrayList<>();
        for (NewsItem newsItem : news) {
            if (newsItem.category.equals(category)) {
                itemsWithGivenCategory.add(newsItem);
            }
        }
        return itemsWithGivenCategory;
    }

    public List<NewsItem> listByCategoryName(String category) throws CategoryNotFoundException {

        boolean categoryExists = Arrays.stream(categories)
                .anyMatch(c -> c.nameOfCategory.equals(category));

        if (!categoryExists){
            throw new CategoryNotFoundException(category);
        }

        List<NewsItem> itemsWithGivenCategoryName = new ArrayList<>();
        for (NewsItem newsItem : news){
            for (Category category1 : categories){
                if (newsItem.category.nameOfCategory.equals(category)){
                    itemsWithGivenCategoryName.add(newsItem);
                }
            }
        }
        return itemsWithGivenCategoryName;
    }
}

public class FrontPageTest {
    public static void main(String[] args) {
        // Reading
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        String[] parts = line.split(" ");
        Category[] categories = new Category[parts.length];
        for (int i = 0; i < categories.length; ++i) {
            categories[i] = new Category(parts[i]);
        }
        int n = scanner.nextInt();
        scanner.nextLine();
        FrontPage frontPage = new FrontPage(categories);
        Calendar cal = Calendar.getInstance();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            cal = Calendar.getInstance();
            int min = scanner.nextInt();
            cal.add(Calendar.MINUTE, -min);
            Date date = cal.getTime();
            scanner.nextLine();
            String text = scanner.nextLine();
            int categoryIndex = scanner.nextInt();
            scanner.nextLine();
            TextNewsItem tni = new TextNewsItem(title, date, categories[categoryIndex], text);
            frontPage.addNewsItem(tni);
        }

        n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            int min = scanner.nextInt();
            cal = Calendar.getInstance();
            cal.add(Calendar.MINUTE, -min);
            scanner.nextLine();
            Date date = cal.getTime();
            String url = scanner.nextLine();
            int views = scanner.nextInt();
            scanner.nextLine();
            int categoryIndex = scanner.nextInt();
            scanner.nextLine();
            MediaNewsItem mni = new MediaNewsItem(title, date, categories[categoryIndex], url, views);
            frontPage.addNewsItem(mni);
        }
        // Execution
        String category = scanner.nextLine();
        System.out.println(frontPage);
        for (Category c : categories) {
            System.out.println(frontPage.listByCategory(c).size());
        }
        try {
            System.out.println(frontPage.listByCategoryName(category).size());
        } catch (CategoryNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
