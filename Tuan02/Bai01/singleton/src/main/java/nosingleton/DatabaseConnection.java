package nosingleton;

public class DatabaseConnection {

    public DatabaseConnection() {
        System.out.println("Create new DB Connection: " + this.hashCode());
    }

    public void connect() {
        System.out.println("Connected to database");
    }
}
