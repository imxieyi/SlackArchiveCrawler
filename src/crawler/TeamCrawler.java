package crawler;

import data.Team;
import data.Writable;

import java.util.ArrayList;

public class TeamCrawler extends DataCrawler {

    public TeamCrawler() {
        url = "https://api.slackarchive.io/v1/team";
        domain = "";
    }

    @Override
    protected DataParser getParser() {
        return new TeamParser();
    }

    public static void main(String[] args) {
        TeamCrawler crawler = new TeamCrawler();
        ArrayList<Writable> list = crawler.crawl();
        int success = 0;
        for(Writable t: list) {
            if(t.write(true) > 0) {
                success++;
            }
        }
        System.out.println("Result: " + success + "/" + list.size());
        System.out.println("Finished!");
    }

}
