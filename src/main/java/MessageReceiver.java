import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class MessageReceiver implements ExceptionListener {
    // URL of the JMS server
    private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;

    public MessageReceiver() {
        try {
            // Create a ConnectionFactory
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);

            // Create a Connection
            Connection connection = connectionFactory.createConnection();
            connection.start();

            connection.setExceptionListener(this);

            // Create a Session
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Create the destination (Topic or Queue)
            Destination destination = session.createQueue("TEST.FOO");

            // Create a MessageConsumer from the Session to the Topic or Queue
            MessageConsumer consumer = session.createConsumer(destination);

            // Wait for a message
            MessageListener messageListenerIml = new MessageListenerIml();
            consumer.setMessageListener(messageListenerIml);

        } catch (Exception e) {
            System.out.println("Caught: " + e);
            e.printStackTrace();
        }
    }

    public synchronized void onException(JMSException ex) {
        System.out.println("JMS Exception occured.  Shutting down client.");
    }

}
