package crawler;

import data.Writable;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.util.ArrayList;

public abstract class DataCrawler {

    protected String url;
    protected String domain;

    protected abstract DataParser getParser();

    public ArrayList<Writable> crawl() {
        try {
            Connection conn = Jsoup.connect(url);
            if(domain!=null && domain.length() > 0) {
                conn = conn.header("Referer", "https://" + domain + ".slackarchive.io/");
            }
            Connection.Response resp = conn.method(Connection.Method.GET).execute();
            return getParser().parse(resp.body());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
