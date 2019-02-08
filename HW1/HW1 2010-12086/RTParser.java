package kr.ac.snu.imlab.ia.hw1;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import sun.net.www.http.HttpClient;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class RTParser {

    static String url = "http://www.rottentomatoes.com/m/inside_out_2015/";  // 자신에게 배정된 URL 사용

    public static void main(String[] args) throws SocketTimeoutException {
        try {
            Document doc = Jsoup.connect(url).get();

            /**
             * 1-1. 영화 제목 및 영화 포스터 이미지 소스 URL 출력하기
             */
            Elements titles = doc.select("h1.title.hidden-xs"); // TODO: 적절한 selector 입력
            Elements images = doc.select("img.posterImage"); // TODO: 적절한 selector 입력
            System.out.println("영화 제목: " + titles.text());
            System.out.println("영화 포스터 URL: " + images.attr("src")); // TODO: 적절한 attribute 입력

            System.out.println("The first task is done");
            /**
             * 1-2. href 속성에 '/celebrity/'를 포함하는 모든 하이퍼링크의 reference URL 출력하기
             *      각 하이퍼링크를 통해 배우 페이지로 이동한 뒤, 페이지 상의 배우 이름 출력히기
             */

            //Elements casts = doc.select("div.cast-item.media.inlineBlock div.media-body a");   // TODO: 적절한 selector 입력 // Only Casts
            Elements casts = doc.select("a[href*=/celebrity/]");   // TODO: 적절한 selector 입력 // All Celebs
            Socket socket = new Socket();
            socket.setSoTimeout(9999999);

            String hyperlinkUrl = null;
            for (Element cast : casts) {
                hyperlinkUrl = cast.attr("href");   // TODO: 적절한 selector 입력
                System.out.println(hyperlinkUrl);

                Document doc2 =
                    Jsoup.connect("http://www.rottentomatoes.com" + hyperlinkUrl).timeout(10*10000).get();
                // TODO: 적절한 코드 입력
                Elements celebrityName = doc2.select("div.col-full-xs.col-sm-17.celeb_name h1");
                System.out.println(celebrityName.text());
            }
            System.out.println("The second task is done");
            System.out.println("Code is done");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
