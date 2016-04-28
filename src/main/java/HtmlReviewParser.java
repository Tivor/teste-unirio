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

    //-1 inverte, ZERO ignora, 1 = igual, 2 = true/false, 3 = modulo(subtracao), 4- max, 5 - multiplicar, 6 - time
//    private static final int[] controle = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, -1, -1, -1, -1, -1, -1, -1, 1, 2, 2, 2, 2, 2, 2, 2, 2};
//    private static final String url = "http://www.toptenreviews.com/computers/gaming/best-graphics-cards/";

//    private static final int[] controle = {1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 2, 2, 2, 0, 0, 0, 0, 2, 2, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2};
//    private static final String url = "http://camcorders.toptenreviews.com/hdv/";

//    private static final int[] controle = {1, 1, 1, 1, 1, 1, 2,2,2, 1, 1, -1, -1, -1, -1, 2, 2, 2, 0, 1, 2, 2, 2, 1, 2, 2, 2};
//    private static final String url = "http://pocket-camcorder.toptenreviews.com/";

//    private static final int[] controle = {0,1,1,0,1,0,4,4,4,5,1,4,6,5,-1,1,1,2,2,2,1,2,2,2,2};
//    private static final String url = "http://www.toptenreviews.com/computers/gaming/best-gaming-laptops/";

    private static final int[] controle = {0,0,4,1,1,1,2,0,1,0,0,0,2,2,4,4,1,4,5,1,1,2,2,2,1,2,2,2,2};
    private static final String url = "http://www.toptenreviews.com/computers/gaming/best-gaming-pcs/";

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
                        strOut += span.hasText() ? clean(span.text()) : "0";
                        strOut += "@@@";
                    }

                    strOut = strOut.trim() + ",";
                } else {
                    strOut += e.text() + ",";
                }
            }
            if (pos != null && !group) pos[0]++;
        }
        if (strOut.trim().length() > 0) strOut = strOut.substring(0, strOut.length() - 1);

        if (group) {

            if (strOut.replaceAll(","," ").trim().length() > 0) {

                String[] broken = strOut.split(",");
                for (String value : broken) {
                    value = value.replaceAll("@@@", ",");

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
                            value = valorTratado.substring(0, valorTratado.length() - 1);
                            break;
                        }
                        case 0: {
                            value = "";
                            break;
                        }
                        case 3: {
                            String[] values = value.split(",");
                            String valorTratado = "";

                            for (String val : values) {
                                double val0 = Double.valueOf(val.split(" - ")[0]);
                                double val1 = Double.valueOf(val.split(" - ")[1]);

                                valorTratado += Math.abs(val0 - val1)  + ",";
                            }
                            value = valorTratado.substring(0, valorTratado.length() - 1);
                            break;
                        }
                        case 4: {
                            String[] values = value.split(",");
                            String valorTratado = "";

                            for (String val : values) {

                                String[] split;
                                if (val.contains("-")) {
                                    split = val.split("-");
                                } else {
                                    split = val.split("/");
                                }

                                valorTratado += split[split.length-1].trim() + ",";
                            }
                            value = valorTratado.substring(0, valorTratado.length() - 1);
                            break;
                        }
                        case 5: {
                            String[] values = value.split(",");
                            String valorTratado = "";

                            for (String val : values) {
                                String[] split = val.split(" x ");
                                double val0 = Double.valueOf(split[0]);
                                double val1 = Double.valueOf(split[1]);
                                double val2 = 1;

                                if (split.length == 3) val2 = Double.valueOf(split[2]);

                                valorTratado += (val0 * val1 * val2)  + ",";
                            }
                            value = valorTratado.substring(0, valorTratado.length() - 1);
                            break;
                        }
                        case 6: {
                            String[] values = value.split(",");
                            String valorTratado = "";

                            for (String val : values) {
                                String[] split = val.split(":");
                                double val0 = Double.valueOf(split[0]);
                                double val1 = Double.valueOf(split[1]);

                                valorTratado += ((val0 * 60) + val1) + ",";
                            }
                            value = valorTratado.substring(0, valorTratado.length() - 1);
                            break;
                        }
                        default: {
                            value = value.substring(0, value.length() - 1);
                            break;
                        }
                    }
                    System.out.println(value);
                    pos[0]++;
                }
            }
        } else {
            System.out.println(strOut);
        }

        return strOut;


    }

    private static String clean(String text) {
        return text.
                replaceAll("Not Listed", "0").
//                replaceAll("x", "").
                replaceAll(" Year", "").
                replaceAll("GHz", "").
                replaceAll("p", "").
                replaceAll("None", "0").
                replace('\uE60B', '1');
    }


}
