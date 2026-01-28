public class XmlToJsonAdapter {
    private XmlSystem xmlSystem;
    private JsonService jsonService;

    public XmlToJsonAdapter(XmlSystem xmlSystem, JsonService jsonService) {
        this.xmlSystem = xmlSystem;
        this.jsonService = jsonService;
    }

    public void process() {
        String xml = xmlSystem.getXmlData();
        String json = convertXmlToJson(xml);
        jsonService.processJson(json);
    }

    private String convertXmlToJson(String xml) {
        // demo đơn giản
        return "{ \"name\": \"Hải\" }";
    }
}

