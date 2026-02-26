package iuh.fit.se;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FaultToleranceTest {

    @Test
    void testRetryAndFallback() {
        FaultTolerance ft = new FaultTolerance(3, "fallback-data");

        // Luôn fail -> trả về fallback sau 3 lần retry
        String result = ft.execute(() -> { throw new RuntimeException("fail"); });
        assertEquals("fallback-data", result);

        // Thành công ngay lần đầu -> trả về kết quả
        String ok = ft.execute(() -> "success");
        assertEquals("success", ok);
    }
}

