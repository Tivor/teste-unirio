import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * Created by Igor on 26/04/2016.
 */
public class HtmlReviewParser {

    public static void main(String... args) throws IOException {

        String url = "http://www.toptenreviews.com/computers/gaming/best-graphics-cards/";

        Document document = Jsoup.connect(url).get();



        //Produtos
        printTen(document, "div[class=prodItemRow title]", 2, 0);

        //Overall ratings
        printTen(document, "div[class=score]", 1, 0);

        //Criteria
        printTen(document, "li[class=carousel_column] > a", 1, 1);

        Elements elems = document.select("div[id=full_feature_section] > div[class=main_row center mainMtx]");

        for (Element e : elems) {

            printTen(e, "div[class=mtxRow] > div[class=mtx_info] > span[class=rowLabel]", 1, 0);



        }



//            String cellDetail = BASE_WEB_SITE + cellPhone.attr("href");
//            Document document = Jsoup.connect(cellDetail).get();
//
//            Elements elements = document.select("div [class=iconos clearfix] > div[class=item]");
//
//
//            String featureKey = element.select("i").attr("class");
//
//            String span = element.select("span").text();
//            allFeatures.add(span);
//
//            String strong = element.select("strong").text();


    }

    private static void printTen(Element document, String query, int razao, int init) {

        Elements alternatives = document.select(query);

        String strOut = "";
        for (int i = init; i < alternatives.size()/razao; i++) {
            Element e = alternatives.get(i);
            strOut += e.text() + ",";
        }
        strOut = strOut.substring(0, strOut.length()-1);
        System.out.println(strOut);
    }


}
