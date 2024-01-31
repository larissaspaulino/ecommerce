package br.com.kafka.ecommerce;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class NewOrderMain {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        var producer = new KafkaProducer<String, String>(properties());

        Callback callback = (data, err) -> {
            if (err != null) {
                System.out.println(err.getMessage());
            } else {
                System.out.println("sucesso enviando mensagem: " + data.topic() + ":::partition: " + data.partition() + ":::offset: " + data.offset());
            }
        };
        var email = "Thank you! We are processing your order!";
        var record = new ProducerRecord("ECOMMERCE_NEW_ORDER", "123", "123");
        var emailRecord = new ProducerRecord("ECOMMERCE_SEND_EMAIL", email, email);
        producer.send(record, callback).get();
        producer.send(emailRecord, callback).get();
    }

    private static Properties properties() {
        var properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return properties;
    }
}
