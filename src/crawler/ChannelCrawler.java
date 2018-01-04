package crawler;

import data.Message;
import data.Team;
import data.Writable;

import java.util.ArrayList;

public class ChannelCrawler extends DataCrawler {

    public ChannelCrawler(String domain) {
        url = "https://api.slackarchive.io/v1/channels";
        this.domain = domain;
    }

    @Override
    protected DataParser getParser() {
        return new ChannelParser();
    }

    public static void main(String[] args) throws InterruptedException {
        ArrayList<Team> teams = Team.all();
        for(int i=0; i<teams.size(); i++) {
            Team t = teams.get(i);
            if(!Team.check(t.id)) continue;
            System.out.println("Index: " + i + "/" + teams.size());
            System.out.println("Team: " + t.domain);
            ChannelCrawler crawler = new ChannelCrawler(t.domain);
            ArrayList<Writable> list = crawler.crawl();
            int success = 0;
            for (Writable c : list) {
                if (c.write(true) > 0) {
                    success++;
                }
            }
            System.out.println("Result: " + success + "/" + list.size());
            Thread.sleep(2000);
        }
        System.out.println("Finished!");
    }

}
