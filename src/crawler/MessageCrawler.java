package crawler;

import com.sun.java.browser.plugin2.DOM;
import data.*;
import matcher.MentionMatcher;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.Date;

public class MessageCrawler {

    private String url;
    private String domain;
    private static Date newLatest;

    public MessageCrawler(String domain, String channel, int size, int offset) {
        url = String.format("https://api.slackarchive.io/v1/messages?size=%d&channel=%s&offset=%d", size, channel, offset);
        this.domain = domain;
    }

    private DataParser messageParser = new MessageParser();
    private DataParser userParser = new UserParser();
    private MentionMatcher mentionMatcher = new MentionMatcher();

    public int[] crawlAndWrite(Date latest) {
        int[] result = new int[5];
        result[0] = 0;
        result[1] = 0;
        result[2] = 0;
        result[3] = 0;
        result[4] = 0;
        try {
            Connection conn = Jsoup.connect(url);
            if(domain!=null && domain.length() > 0) {
                conn = conn.header("Referer", "https://" + domain + ".slackarchive.io/");
            }
            Connection.Response resp = conn.maxBodySize(0).method(Connection.Method.GET).execute();
            System.out.println("Response length: " + resp.body().length());
            ArrayList<Writable> messages = messageParser.parse(resp.body());
            ArrayList<Writable> users = userParser.parse(resp.body());
            result[0] = messages.size();
            result[1] = users.size();
            int count = 0;
            for(Writable w : users) {
                int r = w.write(true);
                if(r>0) {
                    count++;
                }
            }
            result[3] = count;
            count = 0;
            for(Writable w : messages) {
                if(((Message)w).timestamp <= latest.getTime() * 1000) {
                    result[4] = 1;
                    break;
                } else if(((Message)w).timestamp > newLatest.getTime() * 1000) {
                    ((Message)w).updateLatest();
                    newLatest = new Date(((Message)w).timestamp / 1000);
                }
                int r = w.write(true);
                if(r>0) {
                    // Known user, generate mentions
                    count++;
                    Message m = (Message)w;
                    Mention mt = new Mention();
                    mt.timestamp = m.timestamp;
                    mt.team = m.team;
                    mt.channel = m.channel;
                    mt.from_user = m.user;
                    for(String mention : mentionMatcher.match(m.text)) {
                        mt.to_user = mention;
                        mt.write(true);
                    }
                } else {
                    // Unknown user, fuck it
                    //System.out.println("Dirty message: " + w);
                    ((Message)w).fuck();
                    r = w.write(true);
                    if(r>0) {
                        count++;
                    }
                }
            }
            result[2] = count;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static final String DOMAIN = "kubernetes";
    // flatartagency

    public static void main(String[] args) throws InterruptedException {
        ArrayList<Team> teams = Team.all();
        for(Team team : teams) {
            if(!Team.check(team.id)) continue;
            System.out.println("Domain: " + team.domain);
            ArrayList<Channel> channels = Channel.all(team.domain);
            System.out.println("Channels: " + channels);
            for (Channel channel : channels) {
//            if(!channel.name.equals("github")) continue;
                Date latest = Message.latest(team.id, channel.id);
                System.out.println("Channel: " + channel.name);
                System.out.println("Latest: " + latest);
                newLatest = latest;
                // SlackArchive keeps at most 10000 messages per channel
                for (int offset = 0; offset <= 9000; offset += 1000) {
                    MessageCrawler crawler = new MessageCrawler(team.domain, channel.id, 1000, offset);
                    int[] result = crawler.crawlAndWrite(latest);
                    System.out.printf("Messages: %d/%d\tUsers: %d/%d\n", result[2], result[0], result[3], result[1]);
                    if ((result[0] <= 0 && result[1] <= 0) || result[4] == 1) {
                        break;
                    }
                    Thread.sleep(2000);
                }
                System.out.println("New latest: " + Message.latest(team.id, channel.id));
            }
        }
        System.out.println("Finished!");
    }

}
