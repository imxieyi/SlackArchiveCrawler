package crawler;

import data.Channel;
import data.Team;
import data.Writable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChannelParser implements DataParser {

    @Override
    public ArrayList<Writable> parse(String json) {
        JSONObject obj = new JSONObject(json);
        ArrayList<Writable> result = new ArrayList<>();
        if(obj.getInt("total") > 0 ) {
            JSONArray arr = obj.getJSONArray("channels");
            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);
                Channel c = new Channel();
                c.id = o.getString("channel_id");
                c.name = o.getString("name");
                c.team = o.getString("team");
                c.is_channel = o.getBoolean("is_channel");
                c.is_archived = o.getBoolean("is_archived");
                c.is_general = o.getBoolean("is_general");
                c.is_group = o.getBoolean("is_group");
                c.is_starred = o.getBoolean("is_starred");
                c.is_member = o.getBoolean("is_member");
                c.purpose = o.getJSONObject("purpose").getString("value");
                c.num_members = o.getInt("num_members");
                result.add(c);
            }
        }
        return result;
    }

}
