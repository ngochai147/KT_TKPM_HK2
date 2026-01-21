package nosingleton;

public class TestNoSingleton {
    public static void main(String[] args) {
        DatabaseConnection db1 = new DatabaseConnection();
        DatabaseConnection db2 = new DatabaseConnection();
        DatabaseConnection db3 = new DatabaseConnection();

        db1.connect();
        db2.connect();
        db3.connect();
    }
}
