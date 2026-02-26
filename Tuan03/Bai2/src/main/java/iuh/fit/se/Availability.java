package iuh.fit.se;

/**
 * Availability - Kiểm tra tính sẵn sàng của service
 */
public class Availability {
    private boolean serviceUp = true;

    public boolean isServiceUp() {
        return serviceUp;
    }

    public void setServiceUp(boolean serviceUp) {
        this.serviceUp = serviceUp;
    }

    public String handleRequest(String request) {
        if (!serviceUp) {
            throw new RuntimeException("Service is DOWN");
        }
        return "OK: " + request;
    }
}

