import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Igor on 26/04/2016.
 */
public class HtmlReviewParser {

      private static Map<String, String> gigasMap = new HashMap();
      static {
          gigasMap.put("128GB", "0.128");
          gigasMap.put("120GB", "0.12");
          gigasMap.put("250GB", "0.25");
          gigasMap.put("256GB", "0.256");
          gigasMap.put("512GB", "0.512");
          gigasMap.put("500GB", "0.5");
          gigasMap.put("1TB", "1");
      }

        private static Map<String, String> aPlusScaleMap = new HashMap();
        static {
            aPlusScaleMap.put("A+", "9");
            aPlusScaleMap.put("A", "8");
            aPlusScaleMap.put("A-", "7");
            aPlusScaleMap.put("B+", "6");
            aPlusScaleMap.put("B", "5");
            aPlusScaleMap.put("B-", "4");
            aPlusScaleMap.put("C+", "3");
            aPlusScaleMap.put("C", "2");
            aPlusScaleMap.put("C-", "1");
            aPlusScaleMap.put("D+", "0.7");
            aPlusScaleMap.put("D", "0.4");
            aPlusScaleMap.put("D-", "0.2");
        }



//    private static final int[] controle = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, -1, -1, -1, -1, -1, -1, -1, 1, 2, 2, 2, 2, 2, 2, 2, 2};
//    private static final String url = "http://www.toptenreviews.com/computers/gaming/best-graphics-cards/";

//    private static final int[] controle = {1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 2, 2, 2, 0, 0, 0, 0, 2, 2, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2};
//    private static final String url = "http://camcorders.toptenreviews.com/hdv/";

//    private static final int[] controle = {1, 1, 1, 1, 1, 1, 2,2,2, 1, 1, -1, -1, -1, -1, 2, 2, 2, 0, 1, 2, 2, 2, 1, 2, 2, 2};
//    private static final String url = "http://pocket-camcorder.toptenreviews.com/";

//    private static final int[] controle = {0,1,1,0,1,0,4,4,4,5,1,4,6,5,-1,1,1,2,2,2,1,2,2,2,2};
//    private static final String url = "http://www.toptenreviews.com/computers/gaming/best-gaming-laptops/";

//    private static final int[] controle = {0,0,4,1,1,1,2,0,1,0,0,0,2,2,4,4,1,4,5,1,1,2,2,2,1,2,2,2,2};
//    private static final String url = "http://www.toptenreviews.com/computers/gaming/best-gaming-pcs/";

//        private static final int[] controle = {5,0,1,1,1,7,-4,1,1,2,1,1,1,1,1,0,0,0,0,1,2,2,2,2,2,2};
//        private static final String url = "http://tv.toptenreviews.com/flat-panel/lcd/";

//        private static final int[] controle = {0,0,0,8,5,5,6,-1,-5,1,1,8,0,0,0,1,0,0,9,9,9,1,1,1,1,2,8,1,2,2,2};
//        private static final String url = "http://www.toptenreviews.com/computers/laptops/best-laptop-computers/";

    private static final int[] controle = {8,8,0,1,0,1,1,0, 8,8,6,-1,-5,2, 5,1,2,1,1, 1,1,2,1,2, 8,1,2,2,2};
    private static final String url = "http://www.toptenreviews.com/computers/laptops/best-ultrabooks/";
        public static final String VALUE_SEPARATOR = "@@@";

    //ZERO ignora,
    // -1 inverte,
    // 1 = igual,
    // 2 = true/false,
    // 3 = modulo(subtracao),
    // 4- max, -4 -> min,
    // 5 - multiplicar, -5 -> invert(multiplica),
    // 6 - time,
    // 7 - dividir,
    // 8 - A+ - scale
    // 9 - GB
    private static int num = 25;
    private static Path pathAlternatives = Paths.get("input/Alternatives" + num + ".dat");
    private static Path pathFeatures = Paths.get("input/Features" + num + ".dat");



    public static void main(String... args) throws IOException {

        Document document = Jsoup.connect(url).get();

        //Produtos
        String alternatives = printTen(document, "div[class=prodItemRow title]", 2, 0, false, null);
        Files.write(pathAlternatives, (alternatives+"\n").getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);

        //Overall ratings
        String ratings = printTen(document, "div[class=score]", 1, 0, false, null);
        Files.write(pathAlternatives, (ratings+"\n").getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);

        Elements elems = document.select("div[id=full_feature_section] > div[class=main_row center mainMtx]");

        String all = "";
        int[] posFeatures = {0};
        int[] posValues = {0};
        String auxFeatures = "";
        for (Element e : elems) {
            //Features
            String group = printTen(e, "div[class=mtxRow] > div[class=mtx_info] > span[class=rowLabel]", 1, 0, false, posFeatures);
            auxFeatures += (group.length() > 0) ? group + "\n" : "";

            all += group + ",";
            //Valores
            printTen(e, "div[class=mtxRow] > div[class=carousel clip fixedwidth flex]", 1, 0, true, posValues);
        }
        String allFeatures = all.substring(0, all.length() - 1);
        Files.write(pathFeatures, (allFeatures+"\n").getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);

        //Criteria
        String criteria = printTen(document, "li[class=carousel_column] > a", 1, 1, false, null);
        Files.write(pathFeatures, (criteria+"\n").getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);

        Files.write(pathFeatures, auxFeatures.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    private static String printTen(Element document, String query, int razao, int init, boolean group, int[] pos) throws IOException {

        Elements elements = document.select(query);

        String strOut = "";
        for (int i = init; i < elements.size() / razao; i++) {
            if (pos == null || (!group && controle[pos[0]] != 0) || group) {
                Element e = elements.get(i);
                if (group) {
                    Elements spans = e.select("span");
                    for (Element span : spans) {
                        strOut += span.hasText() ? clean(span.text()) : "0";
                        strOut += VALUE_SEPARATOR;
                    }

                    strOut = strOut.trim() + "£";
                } else {
                    strOut += e.text() + ",";
                }
            }
            if (pos != null && !group) pos[0]++;
        }
        if (strOut.trim().length() > 0) strOut = strOut.substring(0, strOut.length() - 1);

        if (group) {

            if (strOut.replaceAll("£"," ").trim().length() > 0) {

                String[] broken = strOut.split("£");
                for (String value : broken) {
                    switch (controle[pos[0]]) {
                        case -1: {
                            value = razaoInversa(value, VALUE_SEPARATOR);
                            break;
                        }
                        case 0: {
                            value = "";
                            break;
                        }
                        case 3: {
                            value = subtrai(value);
                            break;
                        }
                        case -4: {
                            value = getMax(value, false);
                            break;
                        }
                        case 4: {
                            value = getMax(value, true);
                            break;
                        }
                        case 5: {
                            value = multiplica(value);
                            break;
                        }
                        case -5: {
                            value = razaoInversa(multiplica(value), ",");
                            break;
                        }
                        case 6: {
                            value = minutes(value);
                            break;
                        }
                        case 7: {
                            value = divide(value);
                            break;
                        }
                        case 8: {
                            value = map(value, aPlusScaleMap);
                            break;
                        }
                        case 9: {
                            value = map(value, gigasMap);
                            break;
                        }
                        default: {
                            value = value.substring(0, value.length() - 3);
                            break;
                        }
                    }
                    value = value.replaceAll(VALUE_SEPARATOR, ",");
                    System.out.println(value);
                    value = (value.length() > 0) ? value+"\n" : "";
                    Files.write(pathAlternatives, value.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                    pos[0]++;
                }
            }
        } else {
            System.out.println(strOut);
        }

        return strOut;


    }

    private static String minutes(String value) {
        String[] values = value.split(VALUE_SEPARATOR);
        String valorTratado = "";

        for (String val : values) {
            String[] split = val.split(":");
            double val0 = Double.valueOf(split[0]);
            double val1 = Double.valueOf(split[1]);

            valorTratado += ((val0 * 60) + val1) + ",";
        }
        value = valorTratado.substring(0, valorTratado.length() - 1);
        return value;
    }

    private static String multiplica(String value) {
        String[] values = value.split(VALUE_SEPARATOR);
        String valorTratado = "";

        for (String val : values) {
            String[] split = val.split("x");
            double val0 = Double.valueOf(split[0].replaceAll(" ", "").trim());
            double val1 = Double.valueOf(split[1].replaceAll(" ", "").trim());
            double val2 = 1;

            if (split.length == 3) val2 = Double.valueOf(split[2].replaceAll(" ", "").trim());

            valorTratado += (val0 * val1 * val2)  + ",";
        }
        value = valorTratado.substring(0, valorTratado.length() - 1);
        return value;
    }

    private static String divide(String value) {
        String[] values = value.split(VALUE_SEPARATOR);
        String valorTratado = "";

        for (String val : values) {

            if ("0".equals(val)) {
                valorTratado += val + ",";
            } else {

                String[] split = val.split(":");
                double val0 = Double.valueOf(split[0].trim());
                double val1 = Double.valueOf(split[1].trim());

                valorTratado += (val0 / val1) + ",";
            }
        }
        value = valorTratado.substring(0, valorTratado.length() - 1);
        return value;
    }

    private static String subtrai(String value) {
        String[] values = value.split(VALUE_SEPARATOR);
        String valorTratado = "";

        for (String val : values) {
            double val0 = Double.valueOf(val.split(" - ")[0]);
            double val1 = Double.valueOf(val.split(" - ")[1]);

            valorTratado += Math.abs(val0 - val1)  + ",";
        }
        value = valorTratado.substring(0, valorTratado.length() - 1);
        return value;
    }

    private static String razaoInversa(String value, String regex) {
        String[] values = value.split(regex);
        String valorTratado = "";

        for (String val : values) {
            valorTratado += (1 / Double.valueOf(val).doubleValue()) + ",";
        }
        value = valorTratado.substring(0, valorTratado.length() - 1);
        return value;
    }

    private static String map(String value, Map map) {
        String[] values = value.split(VALUE_SEPARATOR);
        String valorTratado = "";

        for (String val : values) {
            if (map.containsKey(val)) {
                valorTratado += map.get(val) + ",";
            } else {
                valorTratado += val + ",";
            }
        }
        value = valorTratado.substring(0, valorTratado.length() - 1);
        return value;
    }

    private static String getMax(String value, boolean asc) {
        String[] values = value.split(VALUE_SEPARATOR);
        String valorTratado = "";

        for (String val : values) {

            String[] split;
            if (val.contains("-")) {
                split = val.split("-");
            } else if (val.contains("/")) {
                split = val.split("/");
            } else {
                split = val.split(",");
            }

            int i = asc ? split.length - 1 : 0;
            valorTratado += split[i].trim() + ",";
        }
        value = valorTratado.substring(0, valorTratado.length() - 1);
        return value;
    }

    private static String clean(String text) {
        return text.
                replaceAll("N/A", "0").
                replaceAll("Not Listed", "0").
                replaceAll("Not specified", "0").
                replaceAll("\\(13-inch\\)", "").
                replaceAll("\\(incl. 1x USB-C\\)", "").
                replaceAll("\\(charging port\\)", "").
                replaceAll("via Dongle", "0.5").



//                replaceAll("x", "").
        replaceAll(" Year", "").
                replaceAll("GHz", "").
                replaceAll("p", "").
                replaceAll("None", "0").
                replace('\uE60B', '1');
    }


}
