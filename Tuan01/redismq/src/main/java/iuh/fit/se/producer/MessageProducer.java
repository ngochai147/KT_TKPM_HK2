package iuh.fit.se.producer;

import iuh.fit.se.config.RedisConfig;
import redis.clients.jedis.Jedis;

public class MessageProducer {

    private static final String QUEUE_NAME = "message_queue";

    public void sendMessage(String message) {
        try (Jedis jedis = RedisConfig.getJedis()) {
            jedis.lpush(QUEUE_NAME, message);
            System.out.println("✅ Sent message: " + message);
        }
    }
}
