package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;

public class Mention extends Writable {
    public long timestamp;// In microseconds
    public String team;
    public String channel;
    public String from_user;
    public String to_user;

    private static String sql = "insert into mention(timestamp, team, channel, from_user, to_user) values (?, ?, ?, ?, ?)";

    @Override
    protected PreparedStatement prepare(Connection conn) throws Exception {
        PreparedStatement stat;
        stat = conn.prepareStatement(sql);
        Timestamp ts = new Timestamp(timestamp / 1000);
        ts.setNanos((int)(timestamp % 1000) * 1000);
        stat.setTimestamp(1, ts);
        stat.setString(2, team);
        stat.setString(3, channel);
        stat.setString(4, from_user);
        stat.setString(5, to_user);
        return stat;
    }

    @Override
    public String toString() {
        return String.format("Team: %s\tChannel: %s\tFrom: %s\tTo: %s", team, channel, from_user, to_user);
    }
}
