package iuh.fit.se;

/**
 * FaultTolerance - Tự động retry khi gặp lỗi, có fallback
 */
public class FaultTolerance {
    private final int maxRetries;
    private final String fallback;

    public FaultTolerance(int maxRetries, String fallback) {
        this.maxRetries = maxRetries;
        this.fallback = fallback;
    }

    @FunctionalInterface
    public interface Task<T> {
        T run() throws Exception;
    }

    public <T> T execute(Task<T> task) {
        for (int i = 0; i < maxRetries; i++) {
            try {
                return task.run();
            } catch (Exception e) {
                // retry
            }
        }
        return (T) fallback;
    }
}

