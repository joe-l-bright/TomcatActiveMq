import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * Created by joe_l_bright on 3/2/14.
 */
public class MessageListenerIml implements MessageListener {
    @Override
    public void onMessage(Message message) {

        if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            String text = null;
            try {
                text = textMessage.getText();
            } catch (JMSException e) {
                e.printStackTrace();
            }
            System.out.println("Received: " + text);
        } else {
            System.out.println("Received: " + message);
        }

    }
}
