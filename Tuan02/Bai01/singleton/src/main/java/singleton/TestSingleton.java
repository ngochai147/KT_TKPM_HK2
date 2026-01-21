package singleton;

public class TestSingleton {
    public static void main(String[] args) {
        DatabaseConnection db1 = DatabaseConnection.getInstance();
        DatabaseConnection db2 = DatabaseConnection.getInstance();
        DatabaseConnection db3 = DatabaseConnection.getInstance();

        System.out.println(db1.hashCode());
        System.out.println(db2.hashCode());
        System.out.println(db3.hashCode());
    }
}
