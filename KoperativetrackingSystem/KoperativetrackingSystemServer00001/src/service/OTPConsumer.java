package service;

import javax.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * JMS Consumer — listens for OTP messages from the queue
 * Run this as a separate thread or process
 */
public class OTPConsumer {

    private static final String BROKER_URL = "tcp://localhost:61616";
    private static final String QUEUE_NAME = "OTP_QUEUE";

    public static void startListening() {
        try {
            ConnectionFactory factory = new ActiveMQConnectionFactory(BROKER_URL);
            Connection connection = factory.createConnection();
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = session.createQueue(QUEUE_NAME);

            MessageConsumer consumer = session.createConsumer(queue);

            System.out.println("📩 OTP Consumer listening on queue: " + QUEUE_NAME);

            consumer.setMessageListener(message -> {
                try {
                    if (message instanceof TextMessage) {
                        String text = ((TextMessage) message).getText();
                        System.out.println("📨 OTP Received: " + text);
                        // Process OTP — e.g. send SMS via Africa's Talking API
                    }
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            });

        } catch (Exception e) {
            System.err.println("❌ OTP Consumer failed to start");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        startListening();
    }
}
