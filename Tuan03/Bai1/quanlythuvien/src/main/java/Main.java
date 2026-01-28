public class Main {
    public static void main(String[] args) {
        Library library = Library.getInstance();

        library.addObserver(new Librarian("Anh Minh"));

        Book b1 = BookFactory.createBook("PAPER", "Java Core", "James", "IT");
        Book b2 = BookFactory.createBook("EBOOK", "Design Patterns", "GoF", "IT");

        library.addBook(b1);
        library.addBook(b2);

        SearchContext search = new SearchContext();
        search.setStrategy(new SearchByAuthor());
        System.out.println("Kết quả tìm kiếm: " +
                search.executeSearch(library.getBooks(), "GoF").size());

        Borrow borrow = new BasicBorrow();
        borrow = new ExtendedTimeBorrow(borrow);
        borrow = new SpecialVersionBorrow(borrow);

        System.out.println(borrow.borrowInfo());
    }
}
