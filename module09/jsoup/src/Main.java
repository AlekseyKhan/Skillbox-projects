import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    private static Pattern HTTP = Pattern.compile("^(http)");
    private static String myUserAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36";
    private static String downloadFolder = "download/";
    private static int downloads = 0;

    public static void main(String[] args) throws IOException {

        Document document = parseFile("https://lenta.ru");

        if (document != null) {
            downloadImages(document);
        }
    }

    private static void downloadImages(Document document) {
        Elements elements = document.select("img");

        System.out.println("Процесс скачивания всех картинок с " + document.title());
        elements.forEach(element -> {
            String link = element.attr("src");
            System.out.printf("%d) %s\t", ++downloads, link);

            URL url = getURL(link);
            if (url == null) {
                System.out.printf("Не удалось найти файл, по указанному адресу %s\n", link);
                return;
            }

            String fileName = getFileName(url);
            try (ReadableByteChannel rbc = Channels.newChannel(url.openStream());
                 FileOutputStream fos = new FileOutputStream(downloadFolder + fileName);
            ) {
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                System.out.printf("\tЗагружен файл: %s\n", fileName);

            } catch (Exception e) {
                System.out.printf("Ошибка при загрузке %s\n", fileName);
            }
        });

        System.out.println("Всего ссылок " + elements.size() + ". Загружено " + downloads);
    }

    private static URL getURL(String link) {
        URL url;
        String fullLink;
        Matcher matcher = HTTP.matcher(link);
        if (!matcher.find()) {
            fullLink = "https://" + link.replaceFirst("^(\\/\\/|\\/)", "");
        } else {
            fullLink = link;
        }

        try {
            url = new URL(fullLink);
        } catch (MalformedURLException e) {
            System.out.println("Некорректная ссылка " + fullLink);
            url = null;
        }

        return url;
    }

    private static String getFileName(URL url) {
        String fileName;
        String src = url.getPath();
        String[] split = src.split("\\/");
        fileName = split[split.length - 1];

        return fileName.trim();
    }

    private static Document parseFile(String path) {
        Document document = null;
        try {
            document = Jsoup
                    .connect(path)
                    .userAgent(myUserAgent)
                    .referrer("https://google.com")
                    .maxBodySize(0)
                    .get();
        } catch (IOException e) {
            System.out.printf("При подключении к сайту '%s' возникла ошибка\n", path);
        }
        return document;
    }
}
