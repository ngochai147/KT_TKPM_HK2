package iuh.fit.se;

import iuh.fit.se.consumer.MessageConsumer;
import iuh.fit.se.producer.MessageProducer;

public class Main {

    public static void main(String[] args) {
        new Thread(() -> {
            MessageConsumer consumer = new MessageConsumer();
            consumer.start();
        }).start();
        MessageProducer producer = new MessageProducer();
        producer.sendMessage("Hello Ngoc Hai");

    }
}
