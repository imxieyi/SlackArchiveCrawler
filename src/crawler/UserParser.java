package crawler;

import data.Message;
import data.User;
import data.Writable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Set;

public class UserParser implements DataParser {

    @Override
    public ArrayList<Writable> parse(String json) {
        JSONObject obj = new JSONObject(json);
        ArrayList<Writable> result = new ArrayList<>();
        JSONObject userObj = obj.getJSONObject("related").getJSONObject("users");
        Set<String> userSet = userObj.keySet();
        for (String user : userSet) {
            JSONObject o = userObj.getJSONObject(user);
            User u = new User();
            u.id = o.getString("user_id");
            u.name = o.getString("name");
            u.team = o.getString("team");
            u.deleted = o.getBoolean("deleted");
            u.color = o.getString("color");
            o = o.getJSONObject("profile");
            u.first_name = o.getString("first_name");
            u.last_name = o.getString("last_name");
            u.real_name = o.getString("real_name");
            if(o.has("image_24")) {
                u.image_24 = o.getString("image_24");
            } else {
                u.image_24 = null;
            }
            if(o.has("image_32")) {
                u.image_32 = o.getString("image_32");
            } else {
                u.image_32 = null;
            }
            if(o.has("image_48")) {
                u.image_48 = o.getString("image_48");
            } else {
                u.image_48 = null;
            }
            if(o.has("image_72")) {
                u.image_72 = o.getString("image_72");
            } else {
                u.image_72 = null;
            }
            if(o.has("image_192")) {
                u.image_192 = o.getString("image_192");
            } else {
                u.image_192 = null;
            }
            if(o.has("image_original")) {
                u.image_original = o.getString("image_original");
            } else {
                u.image_original = null;
            }
            u.title = o.getString("title");
            result.add(u);
        }
        return result;
    }

}
