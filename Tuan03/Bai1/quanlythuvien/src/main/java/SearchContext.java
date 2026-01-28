import java.util.List;

public class SearchContext {
    private SearchStrategy strategy;

    public void setStrategy(SearchStrategy strategy) {
        this.strategy = strategy;
    }

    public List<Book> executeSearch(List<Book> books, String keyword) {
        return strategy.search(books, keyword);
    }
}
