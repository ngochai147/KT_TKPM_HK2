public interface Stock {
    void attach(Observer o);
    void detach(Observer o);
    void notifyObservers();
}
