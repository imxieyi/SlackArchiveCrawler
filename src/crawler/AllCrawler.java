package crawler;

public class AllCrawler {

    public static void main(String[] args) {
        System.out.println("Start crawling teams");
        TeamCrawler.main(args);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ignored) {
        }
        System.out.println("Start crawling channels");
        try {
            ChannelCrawler.main(args);
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Start crawling messages");
        try {
            MessageCrawler.main(args);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
