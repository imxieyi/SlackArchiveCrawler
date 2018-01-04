package data;

import db.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class User extends Writable {
    public String id;
    public String name;
    public String team;
    public boolean deleted;
    public String color;
    public String first_name;
    public String last_name;
    public String real_name;
    public String image_24;
    public String image_32;
    public String image_48;
    public String image_72;
    public String image_192;
    public String image_original;
    public String title;

    private static String sql = "insert into user(id, name, team, deleted, color, first_name, last_name, real_name, image_24, image_32, image_48, image_72, image_192, image_original, title) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    @Override
    protected PreparedStatement prepare(Connection conn) throws Exception {
        PreparedStatement stat = conn.prepareStatement(sql);
        stat.setString(1, id);
        stat.setString(2, name);
        stat.setString(3, team);
        stat.setBoolean(4, deleted);
        stat.setString(5, color);
        stat.setString(6, first_name);
        stat.setString(7, last_name);
        stat.setString(8, real_name);
        stat.setString(9, image_24);
        stat.setString(10, image_32);
        stat.setString(11, image_48);
        stat.setString(12, image_72);
        stat.setString(13, image_192);
        stat.setString(14, image_original);
        stat.setString(15, title);
        return stat;
    }

    @Override
    public String toString() {
        return String.format("User: %s", id);
    }

}
