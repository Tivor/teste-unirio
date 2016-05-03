import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Igor on 26/04/2016.
 */
public class HtmlReviewParser {


    private static DecimalFormat df = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
    static {
        df.setMaximumFractionDigits(10);
    }

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

    public static final String VALUE_SEPARATOR = "@@@";

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

//    private static final int[] controle = {8,8,0,1,0,1,1,0, 8,8,6,-1,-5,2, 5,1,2,1,1, 1,1,2,1,2, 8,1,2,2,2};
//    private static final String url = "http://www.toptenreviews.com/computers/laptops/best-ultrabooks/";

//    private static final int[] controle = {8,1,-1,-5,1,2,2,      0,2,2,2,   8,8,5,6,   1,2,2,2,2,2};
//    private static final String url = "http://tablets-review.toptenreviews.com/kids-tablets/";

//    private static final int[] controle = {8,5,1,1,1,2,   8,-1,1,2,-5,  2,2,2,2,  1,2,2,2,2};
//    private static final String url = "http://ebook-reader-review.toptenreviews.com/";
//
//    private static final int[] controle = {0,0,2,2,2,  4,5,1,  1,-5,-1,1,1,4,0,2,2,2,   1,2};
//    private static final String url = "http://best-kindle-comparison-review.toptenreviews.com/";

//    private static final int[] controle = {8,8,1,  1,1,2,2,2,2,2,2,2,2,   8,2,2,2,   8,1,0,-5,-1,2,   1,1,1,1,2,   8,1,2,2,2,   0,8,8};
//    private static final String url = "http://gps-tracker-review.toptenreviews.com/";

//    private static final int[] controle = {2,2,2,2,2,2,2,2,   1,2,2,2,2,2,2,2,2,2,2,  5,1,1,2,2,2,-1,-1,-1,-1,  1,1,2,2,2};
//    private static final String url = "http://gps.toptenreviews.com/navigation/";

//    private static final int[] controle = {1,0,1,-1,1,2,2,  4,0,-1,0,1,2,2,        1,2,2,2,2,  1,2,2,2,2,2};
//    private static final String url = "http://www.toptenreviews.com/headphones/bluetooth-headphones-review/";

//    private static final int[] controle = {1, 0, 0, -4, 4, 4, 4,   4, 0,2,2,2,2,   1, 0, -4, 2,2,2,2,4,2,2, 1,2,2,2,2,2};
//    private static final String url = "http://www.toptenreviews.com/headphones/noise-cancelling-headphones-review/";

//    private static final int[] controle = {8,8,1,0,-1,0, 8,1,8,0,1,  1,1,-1,1,1,  1,2,2,2,2,2,2,1, 1,2,2,2,2};
//    private static final String url = "http://www.toptenreviews.com/headphones/wireless-earbuds-review/";

//    private static final int[] controle = {0,-1, 1,-1,1,2,0,1,2,2,2,  2,0,1,2,  1, 2,2,2,2};
//    private static final String url = "http://www.toptenreviews.com/headphones/wireless-headphones-review/";

//    private static final int[] controle = {0,1,1,5,5,1,2,2,   5,1,1,2,1,2,1,1,  2,2,2,2,2,1,0,0,0,0,0,  1,1,2,2,2,2};
//    private static final String url = "http://www.toptenreviews.com/computers/printers/best-all-in-one-printers";

//    private static final int[] controle = {1,1,1,1,1,1,5,5,   2,2,2,2,2,2,2,2,2,2,2,2, 1,1,-1,-1,-1,-1,5,-1,2,2,  1,2,2,2,2,2,2,2};
//    private static final String url = "http://www.toptenreviews.com/computers/printers/best-wireless-all-in-one-inkjet-printers/";

//    private static final int[] controle = {0,0, 1,1,1,-1,1,-5,1,2,2,2,2,2,2, 2,2,2,2,2,1,-1,-1,-1,-1,  -1,-1,-1,2,  1,2,2,2,2, 0,0,0,0,0,0,0,0,0,0};
//    private static final String url = "http://www.toptenreviews.com/computers/printers/best-wireless-laser-printers/";

//    private static final int[] controle = {1,4,1,1,4,-4,2,2,   2,2,2,2,-1,0,1,   1,-14,-1,-1,-1,0,0,2,2,0,  0,2,1,2,2,2, 1,2,2,2,2,2,2,-4,  0,0,0,0,0,0};
//    private static final String url = "http://www.toptenreviews.com/computers/3d-printers/best-3d-printers/";

//    private static final int[] controle = {1,1,5,0,1,1,-4,1,1,  2,2,2,2,2,2,2,2,2,0,0, 1,1,1,1,1, 0,0,0,  1,1,2,2,2,2,2};
//    private static final String url = "http://tv.toptenreviews.com/big-screen/";

//    private static final int[] controle = {1,1,1,-4,5,5,  0,0,0,0,1,0,2,2,2,2,2,2,2,2,1,1,  2,2,2,2,2,  1,2,2,2};
//    private static final String url = "http://budget-micro-projectors-review.toptenreviews.com/";

//    private static final int[] controle = {9,1,1,1,2,2,2,2,2,2,2,2,2,2,2,0,0,0,0, 2,2,2,2,2,  2,2,2,2,2,2,2,2,2,2,2,2,2,2,  1,2,2,2,2,2,  0,0,0,0,0,0,0,0,0,0,0};
//    private static final String url = "http://dvr-review.toptenreviews.com/";

//    private static final int[] controle = {2,2,2,2,2,  1,1,1,1,2,  1,1,2,2,2,-5, 1,2,2,2,2};
//    private static final String url = "http://hdmi-switcher-review.toptenreviews.com/";

//    private static final int[] controle = {2,2,2,1,  1,  2,2,2,-1,-1,-1,-1,  1,2,2,2,1};
//    private static final String url = "http://hdtv-antenna-review.toptenreviews.com/";

//    private static final int[] controle = {2,2,2,2,1,0, 2,2,2,-4,0,5,3,3,  2,2,2,2,2,2,2,2,2,2,0,0,0,0,  2,2,2,2,2};
//    private static final String url = "http://home-theater-projectors-review.toptenreviews.com/";

//    private static final int[] controle = {1,2,0,2,1,-1,2,2,0,0,0,2,  0,-1,5,1,-4,1,1,  2,2,2,2,2,2,2,2,  1,2,2,2,2};
//    private static final String url = "http://kitchen-tv-review.toptenreviews.com/";

//    private static final int[] controle = {8,1,1,1,0,2,2,1,1,1,  8,0,2,2,2,1,  8,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,  0,1,1,2,2,2,2,  0,0,0,0,0,0,0,0,0,0,  8,1,2,2,2,2};
//    private static final String url = "http://tv.toptenreviews.com/led-tv/";

//    private static final int[] controle = {0,1,4,1,5,5,-4,  2,2,2,2, 1,1,1,2,9,1,0,0,0,0};
//    private static final String url = "http://mini-projector-review.toptenreviews.com/";

//    private static final int[] controle = {5,1,0,0,1,  4,0,0,0,0,0,1,-1,0,2,2,2,2,2,2,2,1,2,1,2,2,2,  1,1,1,1,1,1,1,  1,2,2,2,2,2};
//    private static final String url = "http://tv.toptenreviews.com/flat-panel/plasma/";

//    private static final int[] controle = {1,2,2,2,2,2,2,2,2,2,2,2,0,  1,1,5,0,1,1,-4,1,1, 1,1,1,1,1,  0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,  1,1,2,2,2,2,2};
//    private static final String url = "http://internet-tvs-review.toptenreviews.com/";

//    private static final int[] controle = {1,3,1,1,1,  2,2,2,2,  2,2,2,  1,2,2,2};
//    private static final String url = "http://tv-wall-mounts-review.toptenreviews.com/";

    private static final int[] controle = {1,1,1,1,5,0,1,  1,2,2,2,2,2,2,2,2,2,2,2,2,  1,1,1,1,2,2,   1,2,2,2,2};
    private static final String url = "http://3d-tv-review.toptenreviews.com/";

    private static int num = 51;

//ZERO ignora,
    // -1 inverte,
    // 1 = igual,
    // 2 = true/false,
    // 3 = modulo(subtracao),
    // 4- max, -4 -> min,
    // 14 (inverso do maximo), -14 (inverso do minimo)
    // 5 - multiplicar, -5 -> invert(multiplica),
    // 6 - time,
    // 7 - dividir,
    // 8 - A+ - scale
    // 9 - GB

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
                        case -14: {
                            value = razaoInversa(getMax(value, false), ",");
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

            if (val0 == 0) {

                valorTratado += "0,";

            } else {

                double val1 = (split.length == 2) ? Double.valueOf(split[1].replaceAll(" ", "").trim()) : val0;

                double val2 = (split.length == 3) ? Double.valueOf(split[2].replaceAll(" ", "").trim()) : 1;

                valorTratado += df.format(val0 * val1 * val2) + ",";

            }



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
            double val0 = Double.valueOf(val.split("-")[0].trim());
            double val1 = Double.valueOf(val.split("-")[1].trim());

            valorTratado += Math.abs(val0 - val1)  + ",";
        }
        value = valorTratado.substring(0, valorTratado.length() - 1);
        return value;
    }

    private static String razaoInversa(String value, String regex) {
        String[] values = value.split(regex);
        String valorTratado = "";

        for (String val : values) {
            val = val.replaceAll(",", "");

            double v = Double.valueOf(val.trim()).doubleValue();

            if (v == 0) {
                valorTratado += "0,";
            } else {
                valorTratado += df.format(1 / v) + ",";
            }


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
            } else if (val.contains(",")) {
                split = val.split(",");
            } else {
                split = val.split(":");
            }

            int i = asc ? split.length - 1 : 0;
            valorTratado += split[i].trim() + ",";
        }
        value = valorTratado.substring(0, valorTratado.length() - 1);
        return value;
    }

    private static String clean(String text) {
        return text.
                replaceAll("N/A", "0").replaceAll("n/a", "0").
                replaceAll("Not Listed", "0").
                replaceAll("Not specified", "0").replaceAll("Not Specified", "0").
                replaceAll("\\(13-inch\\)", "").replaceAll("\\(1 For Roku\\)", "").

        replaceAll("\\(incl. 1x USB-C\\)", "").
                replaceAll("\\(charging port\\)", "").
                replaceAll("via Dongle", "0.5").replaceAll(" in\\.", "").

//                replaceAll("x", "").
        replaceAll(" Year", "").replaceAll(" oz\\.", "").replaceAll("oz", "").replaceAll("lbs", "").replaceAll("lb", "").replaceAll("ft\\.", "").replaceAll("ohms", "").replaceAll("Distributor", "0").
                replaceAll("%", "").replaceAll("X", "x").replaceAll("\\$", "").
                replaceAll("GHz", "").replaceAll("W", "").replaceAll(" ms", "").
                replaceAll("p", "").replaceAll("\\*", "").
                replaceAll("None", "0").
                replace('\uE60B', '1');
    }

}
