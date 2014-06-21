import com.mongodb.*;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.net.UnknownHostException;
import java.util.Date;

/**
 * Created by joe_l_bright on 3/2/14.
 */
public class MessageListenerIml implements MessageListener {
    private DBCollection table;

    public MessageListenerIml() {
        MongoClient mongo = null;
        try {
            mongo = new MongoClient("localhost", 27017);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        DB db = mongo.getDB("testdb");
        table = db.getCollection("texts");
    }

    @Override
    public void onMessage(Message message) {

        String jmsType = "";
        try {
            jmsType = message.getJMSType();
            System.out.println(jmsType);
        } catch (JMSException e) {
            e.printStackTrace();
        }
        if (message instanceof TextMessage) {
            if (jmsType.equals("Text Message")) {
                System.out.println("Received Text message: " + message);
                TextMessage textMessage = (TextMessage) message;
                String text = null;
                try {
                    text = textMessage.getText();
                } catch (JMSException e) {
                    e.printStackTrace();
                }

                try {
                    BasicDBObject document = new BasicDBObject();
                    document.put("text", text);
                    document.put("createdDate", new Date());
                    table.insert(document);

                } catch (MongoException e) {
                    e.printStackTrace();
                }
            }

            if (jmsType.equals("Regular Message")) {
                System.out.println("Received Message: " + message);

                BasicDBObject searchQuery = new BasicDBObject();
                searchQuery.put("text", "Text1");
                DBCursor cursor = table.find(searchQuery);
                while (cursor.hasNext()) {
                    System.out.println(cursor.next());
                }
                BasicDBObject query = new BasicDBObject();
                query.put("text", "Text1");
                BasicDBObject newDocument = new BasicDBObject();
                newDocument.put("text", "joe_l_bright-updated");
                BasicDBObject updateObj = new BasicDBObject();
                updateObj.put("$set", newDocument);
                table.update(query, updateObj);
                BasicDBObject searchQuery2 = new BasicDBObject().append("text", "joe_l_bright-updated");
                DBCursor cursor2 = table.find(searchQuery2);
                while (cursor2.hasNext()) {
                    System.out.println(cursor2.next());
                }
            }
        }
    }
}
