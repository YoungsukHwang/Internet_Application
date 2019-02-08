package kr.ac.snu.imlab.ia.hw1;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class EPLParser {
    static String url = "http://www.premierleague.com/en-gb/matchday/league-table.html";

    public static void main(String[] args) {
        try {
            /**
             * 2. 2015-2016 잉글랜드 프리미어리그 순위표 출력 프로그램 작성하기
             */
            Document doc = Jsoup.connect(url).get();
            Elements clubRows = doc.select("table.leagueTable tbody tr.club-row");  // TODO: 적절한 selector 입력

            String[] items = {"순위", "팀명", "경기수", "승", "무", "패", "득점", "실점", "골득실", "승점"};

            StringBuilder builder = new StringBuilder();
            for (String item : items) {
                builder.append(item);
                builder.append(" ");
            }
            builder.append("\n");
            int i=0;
            for (Element clubRow : clubRows) {
                // TODO: 1등에서 10등까지, items 배열에 나온 순서대로 출력
                // 한 행이 아래와 같은 형태로 출력되도록 할 것:
                // 1 Leicester City 29 17 9 3 52 31 21 60

                // 여기에 코드 작성
                builder.append(clubRow.select("td.col-pos").text());
                builder.append(" ");
                builder.append(clubRow.select("td.col-club").text());
                builder.append(" ");
                builder.append(clubRow.select("td.col-p").text());
                builder.append(" ");
                builder.append(clubRow.select("td.col-w").text());
                builder.append(" ");
                builder.append(clubRow.select("td.col-d").text());
                builder.append(" ");
                builder.append(clubRow.select("td.col-l").text());
                builder.append(" ");
                builder.append(clubRow.select("td.col-gf").text());
                builder.append(" ");
                builder.append(clubRow.select("td.col-ga").text());
                builder.append(" ");
                builder.append(clubRow.select("td.col-gd").text());
                builder.append(" ");
                builder.append(clubRow.select("td.col-pts").text());
                builder.append("\n");
                i++;
                if(i==10)
                    break;
            }

            System.out.println(builder.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
