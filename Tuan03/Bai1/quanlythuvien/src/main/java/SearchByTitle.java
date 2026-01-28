import java.util.List;
import java.util.stream.Collectors;

public class SearchByTitle implements SearchStrategy {
    public List<Book> search(List<Book> books, String keyword) {
        return books.stream()
                .filter(b -> b.getTitle().contains(keyword))
                .collect(Collectors.toList());
    }
}



