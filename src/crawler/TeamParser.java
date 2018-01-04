package crawler;

import data.Team;
import data.Writable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class TeamParser implements DataParser {

    @Override
    public ArrayList<Writable> parse(String json) {
        JSONObject obj = new JSONObject(json);
        JSONArray arr = obj.getJSONArray("team");
        ArrayList<Writable> result = new ArrayList<>();
        for(int i=0; i<arr.length(); i++) {
            JSONObject o = arr.getJSONObject(i);
            Team t = new Team();
            t.id = o.getString("team_id");
            t.domain = o.getString("domain");
            t.name = o.getString("name");
            t.is_disabled = o.getBoolean("is_disabled");
            t.is_hidden = o.getBoolean("is_hidden");
            result.add(t);
        }
        return result;
    }

}
