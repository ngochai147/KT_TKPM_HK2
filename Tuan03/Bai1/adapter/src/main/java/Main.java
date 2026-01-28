public class Main {
    public static void main(String[] args) {
        XmlSystem xmlSystem = new XmlSystem();
        JsonService jsonService = new WebApiService();

        XmlToJsonAdapter adapter =
                new XmlToJsonAdapter(xmlSystem, jsonService);

        adapter.process();
    }
}
