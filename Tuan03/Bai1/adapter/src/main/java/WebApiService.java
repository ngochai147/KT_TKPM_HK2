public class WebApiService implements JsonService {
    @Override
    public void processJson(String json) {
        System.out.println("Xử lý JSON: " + json);
    }
}
