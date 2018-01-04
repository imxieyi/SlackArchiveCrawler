package data;

import db.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class Channel extends Writable {
    public String id;
    public String name;
    public String team;
    public boolean is_channel;
    public boolean is_archived;
    public boolean is_general;
    public boolean is_group;
    public boolean is_starred;
    public boolean is_member;
    public String purpose;
    public int num_members;

    private static String sql = "insert into channel(id, name, team, is_channel, is_Archived, is_general, is_group, is_starred, is_member, purpose, num_members) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    @Override
    protected PreparedStatement prepare(Connection conn) throws Exception {
        PreparedStatement stat = conn.prepareStatement(sql);
        stat.setString(1, id);
        stat.setString(2, name);
        stat.setString(3, team);
        stat.setBoolean(4, is_channel);
        stat.setBoolean(5, is_archived);
        stat.setBoolean(6, is_general);
        stat.setBoolean(7, is_group);
        stat.setBoolean(8, is_starred);
        stat.setBoolean(9, is_member);
        stat.setString(10, purpose);
        stat.setInt(11, num_members);
        return stat;
    }

    public static ArrayList<Channel> all(String team) {
        ArrayList<Channel> result = new ArrayList<>();
        try {
            Connection conn = DBHelper.getConnection();
            PreparedStatement stat = conn.prepareStatement("select * from team where domain = '" + team + "'");
            ResultSet rs = stat.executeQuery();
            rs.next();
            String teamid = rs.getString("id");
            rs.close();
            stat.close();
            stat = conn.prepareStatement("select * from channel where team = '" + teamid + "'");
            rs = stat.executeQuery();
            while(rs.next()) {
                Channel c = new Channel();
                c.id = rs.getString("id");
                c.name = rs.getString("name");
                c.team = rs.getString("team");
                c.is_channel = rs.getBoolean("is_channel");
                c.is_archived = rs.getBoolean("is_archived");
                c.is_general = rs.getBoolean("is_general");
                c.is_group = rs.getBoolean("is_group");
                c.is_starred = rs.getBoolean("is_starred");
                c.is_member = rs.getBoolean("is_member");
                c.purpose = rs.getString("purpose");
                result.add(c);
            }
            rs.close();
            stat.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean check(String team, String channel) {
        Connection conn = null;
        PreparedStatement stat = null;
        try {
            conn = DBHelper.getConnection();
            String sql;
            if(team != null && team.length() > 0 && channel != null && channel.length() > 0) {
                sql = "select * from channel where team = ? and id = ?";
                stat = conn.prepareStatement(sql);
                stat.setString(1, team);
                stat.setString(2, channel);
            } else {
                conn.close();
                return false;
            }
            ResultSet rs = stat.executeQuery();
            if(rs.next()) {
                DBHelper.close(conn, stat);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelper.close(conn, stat);
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("Channel: %s", id);
    }

}
