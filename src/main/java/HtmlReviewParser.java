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

    //-1 inverte, ZERO ignora, 1 = igual, 2 = true/false
    private static final int[] controle = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, -1, -1, -1, -1, -1, -1, -1, 1, 2, 2, 2, 2, 2, 2, 2, 2};

    private static final String url = "http://www.toptenreviews.com/computers/gaming/best-graphics-cards/";

    public static void main(String... args) throws IOException {

        Document document = Jsoup.connect(url).get();

        //Produtos
        printTen(document, "div[class=prodItemRow title]", 2, 0, false, null);

        //Overall ratings
        printTen(document, "div[class=score]", 1, 0, false, null);

        //Criteria
        printTen(document, "li[class=carousel_column] > a", 1, 1, false, null);

        Elements elems = document.select("div[id=full_feature_section] > div[class=main_row center mainMtx]");

        String all = "";
        int[] posFeatures = {0};
        int[] posValues = {0};
        for (Element e : elems) {
            //Features
            all += printTen(e, "div[class=mtxRow] > div[class=mtx_info] > span[class=rowLabel]", 1, 0, false, posFeatures) + ",";
            //Valores
            printTen(e, "div[class=mtxRow] > div[class=carousel clip fixedwidth flex]", 1, 0, true, posValues);
        }
        String substring = all.substring(0, all.length() - 1);
        System.out.println(substring);
    }

    private static String printTen(Element document, String query, int razao, int init, boolean group, int[] pos) {

        Elements elements = document.select(query);

        String strOut = "";
        for (int i = init; i < elements.size() / razao; i++) {
            if (pos == null || (!group && controle[pos[0]] != 0) || group) {
                Element e = elements.get(i);
                if (group) {
                    Elements spans = e.select("span");
                    for (Element span : spans) {
                        strOut += span.hasText() ? span.text() : "0";
                        strOut += "@@@";
                    }

                    strOut = strOut.trim() + ",";
                } else {
                    strOut += e.text() + ",";
                }
            }
            if (pos != null && !group) pos[0]++;
        }
        strOut = strOut.substring(0, strOut.length() - 1);

        if (group) {
            String[] broken = strOut.split(",");
            for (String value : broken) {
                value = value.replaceAll("@@@", ",");

                if (pos != null) {
                    switch (controle[pos[0]]) {
                        case -1: {
                            String[] values = value.split(",");
                            String valorTratado = "";

                            for (String val : values) {
                                if ("N/A".equals(val)) {
                                    valorTratado += "0,";
                                } else {
                                    valorTratado += (1 / Double.valueOf(val).doubleValue()) + ",";
                                }
                            }
                            value = valorTratado.substring(0, valorTratado.length()-1);
                            break;
                        }
                        case 0: {
                            value = "";
                            break;
                        }
                        case 2: {
                            //True/False
                            String[] values = value.split(",");
                            String valorTratado = "";
                            for (String val : values) {
                                if ("\uE60B".equals(val)) {
                                    valorTratado += "1,";
                                } else {
                                    valorTratado += "0,";
                                }
                            }
                            value = valorTratado.substring(0, valorTratado.length()-1);
                            break;
                        }
                        default: {
                            break;
                        }
                    }

                }
                System.out.println(value);
                pos[0]++;
            }
        } else {
            System.out.println(strOut);
        }

        return strOut;


    }


}
