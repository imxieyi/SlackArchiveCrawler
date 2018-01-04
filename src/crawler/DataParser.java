package crawler;

import data.Writable;

import java.util.ArrayList;

public interface DataParser {
    public ArrayList<Writable> parse(String json);
}
