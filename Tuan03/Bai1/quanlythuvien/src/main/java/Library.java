import java.util.*;

public class Library {
    private static Library instance;
    private List<Book> books = new ArrayList<>();
    private List<Observer> observers = new ArrayList<>();

    private Library() {}

    public static synchronized Library getInstance() {
        if (instance == null) {
            instance = new Library();
        }
        return instance;
    }

    public void addBook(Book book) {
        books.add(book);
        notifyObservers("Sách mới được thêm: " + book.getTitle());
    }

    public List<Book> getBooks() {
        return books;
    }

    // Observer
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    private void notifyObservers(String message) {
        for (Observer o : observers) {
            o.update(message);
        }
    }
}
