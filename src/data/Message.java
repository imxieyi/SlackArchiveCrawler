package data;

import db.DBHelper;

import java.sql.*;
import java.util.Date;

public class Message extends Writable {
    public long timestamp;// In microseconds
    public String team;
    public String channel;
    public String user;
    public String text;

    // Messages sent by an unknown user is considered dirty
    private boolean dirty = false;

    private static String sql = "insert into message(timestamp, team, channel, user, text) values (?, ?, ?, ?, ?)";
    private static String dirtySql = "insert into dirty_message(timestamp, team, channel, user, text) values (?, ?, ?, ?, ?)";

    @Override
    protected PreparedStatement prepare(Connection conn) throws Exception {
        PreparedStatement stat;
        if(dirty) {
            stat = conn.prepareStatement(dirtySql);
        } else {
            stat = conn.prepareStatement(sql);
        }
        Timestamp ts = new Timestamp(timestamp / 1000);
        ts.setNanos((int)(timestamp % 1000) * 1000);
        stat.setTimestamp(1, ts);
        stat.setString(2, team);
        stat.setString(3, channel);
        stat.setString(4, user);
        stat.setString(5, text);
        return stat;
    }

    public void fuck() {
        this.dirty = true;
    }

    public static Date latest(String team, String channel) {
        Connection conn = null;
        PreparedStatement stat = null;
        Date result = new Date(0);
        try {
            conn = DBHelper.getConnection();
            stat = conn.prepareStatement("select timestamp from latest_message where team = ? and channel = ?");
            stat.setString(1, team);
            stat.setString(2, channel);
            java.sql.ResultSet rs = stat.executeQuery();
            if(rs.next()) {
                result = rs.getTimestamp("timestamp");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.close(conn, stat);
        }
        return result;
    }

    public boolean updateLatest() {
        boolean result = false;
        Connection conn = null;
        PreparedStatement stat = null;
        try {
            conn = DBHelper.getConnection();
            stat = conn.prepareStatement("update latest_message set timestamp = ? where team = ? and channel = ?");
            Timestamp ts = new Timestamp(timestamp / 1000);
            ts.setNanos((int)(timestamp % 1000) * 1000);
            stat.setTimestamp(1, ts);
            stat.setString(2, team);
            stat.setString(3, channel);
            result = (stat.executeUpdate() > 0);
            if(!result) {
                stat.close();
                stat = conn.prepareStatement("insert into latest_message(timestamp, team, channel) values(?, ?, ?)");
                stat.setTimestamp(1, ts);
                stat.setString(2, team);
                stat.setString(3, channel);
                result = (stat.executeUpdate() > 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.close(conn, stat);
        }
        return result;
    }

    @Override
    public String toString() {
        return String.format("Team: %s\tChannel: %s\tUser: %s", team, channel, user);
    }
}
