package data;

import db.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class Team extends Writable {
    public String id;
    public String domain;
    public String name;
    public boolean is_disabled;
    public boolean is_hidden;

    private static String sql = "insert into team(id, domain, name, is_disabled, is_hidden) values (?, ?, ?, ?, ?)";

    @Override
    protected PreparedStatement prepare(Connection conn) throws Exception {
        PreparedStatement stat = conn.prepareStatement(sql);
        stat.setString(1, id);
        stat.setString(2, domain);
        stat.setString(3, name);
        stat.setBoolean(4, is_disabled);
        stat.setBoolean(5, is_hidden);
        return stat;
    }

    public static ArrayList<Team> all() {
        ArrayList<Team> result = new ArrayList<>();
        try {
            Connection conn = DBHelper.getConnection();
            PreparedStatement stat = conn.prepareStatement("select * from team");
            ResultSet rs = stat.executeQuery();
            while(rs.next()) {
                Team t = new Team();
                t.id = rs.getString("id");
                t.domain = rs.getString("domain");
                t.name = rs.getString("name");
                t.is_disabled = rs.getBoolean("is_disabled");
                t.is_hidden = rs.getBoolean("is_hidden");
                result.add(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean check(String team) {
        try {
            Connection conn = DBHelper.getConnection();
            String sql;
            PreparedStatement stat;
            if(team != null && team.length() > 0) {
                sql = "select * from message where team = ? limit 1";
                stat = conn.prepareStatement(sql);
                stat.setString(1, team);
            } else {
                conn.close();
                return false;
            }
            ResultSet rs = stat.executeQuery();
            if(rs.next()) {
                rs.close();
                stat.close();
                conn.close();
                return true;
            } else {
                rs.close();
                stat.close();
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("Team: %s", id);
    }

}
