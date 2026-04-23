package service;

import javax.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;


public class OTPProducer {

    private static final String BROKER_URL = "tcp://localhost:61616";
    private static final String QUEUE_NAME = "OTP_QUEUE";

    public static void sendOTP(String memberId, String otp) {
        try {
            ConnectionFactory factory = new ActiveMQConnectionFactory(BROKER_URL);
            Connection connection = factory.createConnection();
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = session.createQueue(QUEUE_NAME);

            MessageProducer producer = session.createProducer(queue);
            TextMessage message = session.createTextMessage(
                    "OTP for member " + memberId + ": " + otp
            );

            producer.send(message);

            session.close();
            connection.close();

            System.out.println("📨 OTP sent to queue: " + otp);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
