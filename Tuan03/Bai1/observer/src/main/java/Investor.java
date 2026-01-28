// ConcreteObserver
public class Investor implements Observer {
    private String name;

    public Investor(String name) {
        this.name = name;
    }

    @Override
    public void update(double price) {
        System.out.println(name + " nhận thông báo: Giá mới = " + price);
    }
}
