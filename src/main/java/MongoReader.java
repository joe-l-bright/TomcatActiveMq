import com.mongodb.*;

import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Created by joe_l_bright on 6/21/14.
 */
public class MongoReader {
    private DBCollection table;
    private Thread thread;

    public MongoReader() {
        MongoClient mongo = null;
        try {
            mongo = new MongoClient("localhost", 27017);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        DB db = mongo.getDB("testdb");
        table = db.getCollection("texts");
    }

    public String read() {
        List<String> newList = newArrayList();
        if (thread == null || !thread.isAlive()) {
            thread = new Thread(() -> new GetValues(newList).invoke());
            thread.start();
            return "thread started";
        }
        return "thread is running";
    }

    private class GetValues {
        private List<String> newList;

        public GetValues(List<String> newList) {
            this.newList = newList;
        }

        public void invoke() {
            System.out.println("runnable running");
            BasicDBObject searchQuery = new BasicDBObject();
            searchQuery.put("text", java.util.regex.Pattern.compile("text"));
            DBCursor cursor = table.find(searchQuery);
            newList.add(null);
            while (cursor.hasNext()) {
                DBObject next = cursor.next();
                Map map = next.toMap();
                newList.add(String.valueOf(map.get("text")));
            }
//            newList.stream().forEachOrdered((s) -> System.out.println("s = [" + s + "]"));
        }
    }
}
