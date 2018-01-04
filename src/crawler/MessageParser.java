package crawler;

import data.Channel;
import data.Message;
import data.Writable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MessageParser implements DataParser {

    @Override
    public ArrayList<Writable> parse(String json) {
        JSONObject obj = new JSONObject(json);
        ArrayList<Writable> result = new ArrayList<>();
        JSONArray arr = obj.getJSONArray("messages");
        for (int i = 0; i < arr.length(); i++) {
            JSONObject o = arr.getJSONObject(i);
            // Ignore bot messages
            if(o.has("subtype")) {
                if(o.getString("subtype").equals("bot_message")) {
                    continue;
                }
            }
            Message m = new Message();
            m.timestamp = (long)Math.floor(o.getDouble("ts") * 1000000);
            m.text = o.getString("text");
            m.channel = o.getString("channel");
            m.user = o.getString("user");
            m.team = o.getString("team");
            result.add(m);
        }
        return result;
    }

}
