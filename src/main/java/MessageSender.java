import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ScheduledMessage;

import javax.jms.*;


public class MessageSender {
    // URL of the JMS server
    private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;

    public MessageSender(String message) {
        try {
            // Create a ConnectionFactory
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);

            // Create a Connection
            Connection connection = connectionFactory.createConnection();
            connection.start();

            // Create a Session
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Create the destination (Topic or Queue)
            Destination destination = session.createQueue("TEST.FOO");

            // Create a MessageProducer from the Session to the Topic or Queue
            MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            for (int x = 1; x < 1000000; x++) {
                // Create a messages
                String text = "This is the text message number: " + x;
                TextMessage textMessage = session.createTextMessage(text);
                textMessage.setJMSType("Text Message");

                // Tell the producer to send the message
                producer.send(textMessage);
            }
            // Create a message
            TextMessage sessionMessage = session.createTextMessage("delayed");
            sessionMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, 5000);
            sessionMessage.setJMSType("Regular Message");

            producer.send(sessionMessage);

            // Clean up
            session.close();
            connection.close();
        } catch (Exception e) {
            System.out.println("Caught: " + e);
            e.printStackTrace();
        }
    }
}