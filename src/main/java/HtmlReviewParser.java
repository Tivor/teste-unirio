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

    /*

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

//    private static final int[] controle = {1,1,1,1,5,0,1,  1,2,2,2,2,2,2,2,2,2,2,2,2,  1,1,1,1,2,2,   1,2,2,2,2};
//    private static final String url = "http://3d-tv-review.toptenreviews.com/";

//    private static final int[] controle = {0,1,1,1,-1,-5,  1,1,1,1,1,2,2,2,1,1,  1,1,1,1,1,2,  1,1,1,1,4,2,  2,2,2,2,2,2,2,2,2,2,2,  0,0,0,0};
//    private static final String url = "http://android-phone-review.toptenreviews.com/";

//    private static final int[] controle = {1,1,2,2,2, 1,-1,1,2,2,  1,2,2,2,2,2,2,2,  1,1,1,2,  1,2,2,2,2,2,2};
//    private static final String url = "http://bluetooth-headset-review.toptenreviews.com/";

//    private static final int[] controle = {1,-1,1,2,2,2,2,  1,1,1,2,2,2,2,2,  1,1,1,2,2,2,  0,0,0,0, 1,2,2,2,2,2,2};
//    private static final String url = "http://cell-phone-booster-review.toptenreviews.com/";

//    private static final int[] controle = {1,1,1,2,2,-1,1,2,2,2,1,2,2,2,2,2,2,2,2,  2,2,2,2,2,2,2,2,2,  2,2,2,2,2,2,  1,1,1,2,  2,2,2, 0,0};
//    private static final String url = "http://emergency-cell-phone-review.toptenreviews.com/";

//    private static final int[] controle = {0,0,2,2,2,2,1, 2,2,2,2,  2,2,2,2,2,2};
//    private static final String url = "http://iphone-lens-kits-review.toptenreviews.com/";

//    private static final int[] controle = {0,0,  1,1,1,2,2,2,2,  1,1,2,2,2,2,2,2,2,  1,2,2,2,2,2,  1,1,1,1};
//    private static final String url = "http://iphone-screen-protector-review.toptenreviews.com/";

//    private static final int[] controle = {0,1,1,2,2,  1,2,2,2,2,2,9,  0,1,-1,-1,-1,-1};
//    private static final String url = "http://cell-phones.toptenreviews.com/pay-as-you-go/";

//    private static final int[] controle = {-1,-1,2,1,2,  1,1,-1,1,0,  1,-1,1,2,-5,0,  4,2,2,2,2};
//    private static final String url = "http://solar-phone-charger-review.toptenreviews.com/";

//    private static final int[] controle = {1,1,1,1,1,-1,1,0,1,1,3,1,2,2,  1,1,1,-1,-5,1,1,2,2,2,2,  1,1,1,2,2,2,2,2,2,  1,2,2,2,2,2};
//    private static final String url = "http://digital-cameras.toptenreviews.com/compact/";

//    private static final int[] controle = {1,2,2,2,  1,1,1,1,2,2,2,2,   1,1,1,2,2,2,2,2,  1,2,2,2,2};
//    private static final String url = "http://digital-cameras-for-children-review.toptenreviews.com/";

//    private static final int[] controle = {1,1,1,1,1,1,2,  1,1,1,1,1,1,  -1,-5,1,2,2,2,2,2,  1,2,2,2,2,2};
//    private static final String url = "http://entry-level-dslr-camera-review.toptenreviews.com/";

//    private static final int[] controle = {1,1,1,1,  1,1,-1,-1,  2,2,2,2,  -5,1,2,2,  1,1,2,2};
//    private static final String url = "http://game-camera-review.toptenreviews.com/";

//    private static final int[] controle = {5,1,1,0,  1,1,1,2,  2,2,2,2,2,2,2,2,  1,-1,2,2,2,2,1,1,  4,2,2,2,2};
//    private static final String url = "http://infrared-cameras-review.toptenreviews.com/";

//    private static final int[] controle = {4,1,1,1,1,2,  1,4,1,1,1,14,14,2,  1,2,2,2,2,2,-5,-1,2,2,  1,2,2,2,2};
//    private static final String url = "http://point-and-shoot-camera-review.toptenreviews.com/";

//    private static final int[] controle = {0,1,1,1,1,1,1,1,  1,1,1,1,1,1,4,2,  -1,-5,1,1,2,2,2,2,2,2,2,  2,2,2,2,2,  1,2,2,2,2,2};
//    private static final String url = "http://digital-cameras.toptenreviews.com/professional-dslr/";

//    private static final int[] controle = {1,1,2,1,2,2,2,2,  8,8,1,-1,1,1,1,1,1,2,2,2,  1,2,2,2,2,2,2,2,2,2,2,2,  1,1,0,0,  1,-1,-1,-1,-1,  1,2,2,2,2,2};
//    private static final String url = "http://waterproof-camera-review.toptenreviews.com/";

//    private static final int[] controle = {-1,2,2,2,  2,2,2,2,  1,-1,-5,2,2,2,2,  1,2,2,2,2};
//    private static final String url = "http://3d-blu-ray-player-review.toptenreviews.com/";

//    private static final int[] controle = {8,8,8,1,5,1,1,1,  1,1,2,2,2,2,2,2,2,  1,1,1,1,-1,-1,-1,-1,  1,2,2,2};
//    private static final String url = "http://dvd-players.toptenreviews.com/portable/";

//    private static final int[] controle = {0,0,0,0,   0,1,0,1,0,1,2,2,2,2,2,2,2,2,  0,1,0,0,0,0,1,0,2,2,2,2,  1,2,2,2,2,2,2,2,2,2,2,  2,2,2,2,2,2,  1,2,2,2,2,2,2,  0,0,0,0,  0,0,0,0,0};
//    private static final String url = "http://video-game-consoles-review.toptenreviews.com/";

//    private static final int[] controle = {1,1,2,2,2,2,2,2,2,  1,1,1,2,2,2,2,2,  2,2,2,2,2,2,2,  1,2,2,2,2,   0,0,0,0,0};
//    private static final String url = "http://gaming-steering-wheel-review.toptenreviews.com/";

//    private static final int[] controle = {1,1,1,1,0,1,0,0,0,0,2,2,2,2,2,  0,0,1,1,2,2,2,2,  2,2,2,2,2,2,2,  2,2,2,2,2,  0,0,0};
//    private static final String url = "http://handheld-game-console-review.toptenreviews.com/";

//    private static final int[] controle = {-1,1,1,-1,1,1,-1,1,  -1,1,1,1,2,2,2,  2,2,2,2,2,2,   1,2,2,2,2,2};
//    private static final String url = "http://www.toptenreviews.com/computers/gaming/best-pc-gaming-headsets/";

//    private static final int[] controle = {1,0,2,2,2,2,2,2,2,2,2,  2,2,2,2,2,2,  1,2,2,2,2,2,  0,0,0,0,0,0,0,0};
//    private static final String url = "http://www.toptenreviews.com/computers/peripherals/best-vhs-to-dvd-converters/";

//    private static final int[] controle = {1,1,2,2,2,2,2,  0,2,2,2,  1,1,1,2,2,2,2,2,2,2,2,2,  1,2,2,2,  1,2,2,2};
//    private static final String url = "http://alarm-clocks-review.toptenreviews.com/";

//    private static final int[] controle = {2,2,2,2,2,2,2,  2,2,2,2,2,  2,2,2,2,2,  1,2,2};
//    private static final String url = "http://baby-sound-machine-review.toptenreviews.com/";

//    private static final int[] controle = {1,1,1,5,1,2,2,2,1,  1,1,1,1,1,2,2,2,2,2,  2,2,2,1,  1,2,2};
//    private static final String url = "http://baby-video-monitor-review.toptenreviews.com/";

//    private static final int[] controle = {1,1,1,1,1,1,2,2,2,  2,2,2,2,2,2,2,  1,1,1,1,2,  1,2,2,2,2,2};
//    private static final String url = "http://cordless-phones-review.toptenreviews.com/";

//    private static final int[] controle = {1,1,1,2,2,2,2,2,2,2,2,2,2,2,   1,2,2,2,2,2,-1,  2,2,2,2,2,2,2,2,2,2,2,2,  1,2,2,2,2,2};
//    private static final String url = "http://die-cutting-machines-review.toptenreviews.com/";

//    private static final int[] controle = {1,1,2,2,0,1,-1,-1,-3,2,  1,0,0,2,2,2,2,  1,2,2,2,2,2,2,  0,0,0,0,0,0};
//    private static final String url = "http://digital-pen-review.toptenreviews.com/";

//    private static final int[] controle = {0,0,  1,1,5,1,0,2,2,2,2,2,2,  1,1,2,2,   1,1,2,2,2,2,   1,2,2,2};
//    private static final String url = "http://digital-photo-frame-review.toptenreviews.com/";

//    private static final int[] controle = {1,1,2,2,2,2,   2,2,2,2,2,2,2,2,2,2,2,2,  -1,-5,2,2,2,  2,2,2,2,2,2,2,2,  1,2,2,2,2};
//    private static final String url = "http://electronic-translator-review.toptenreviews.com/";

//    private static final int[] controle = {1,1,-1,1,1,2,2,2,  1,-5,-1,1,1,0,  2,2,2,2,2,  1,2,2,2,2};
//    private static final String url = "http://external-battery-pack-review.toptenreviews.com/";

//    private static final int[] controle = {1,0,2,2,2,2,2,2,2,2,2,2,  1,2,2,2,2,2,  1,2,2,2,2,2,2,   0,0,0,0,0,0,0,0,0,0,0,0,0,0};
//    private static final String url = "http://home-automation-systems-review.toptenreviews.com/";

//    private static final int[] controle = {0,0,0,  1,2,2,2,2,2,2,2,2,  0,2,2,  1,2,2,2,2,2,2,2,2,2,1,  1,2,2,2,2,2,2,2,  1,0,2,2,2,2,2,2,2};
//    private static final String url = "http://home-security-systems-review.toptenreviews.com/";

//    private static final int[] controle = {2,2,2,2,2,  2,2,2,2,2,2,2,2,  1,1,1,2,0,-1,5,1,0,0,0,1,1};
//    private static final String url = "http://nanny-cam-review.toptenreviews.com/";

//    private static final int[] controle = {0,0,-1,1,2,2,2,1, 1,1,1,1,1,2,2,  1,-1,0,1,2,2,2,2,2,0,  1,2,2,2,2,2,2};
//    private static final String url = "http://metal-detector-review.toptenreviews.com/";

//    private static final int[] controle = {0,1,1,1,2,2, 1,2,0,2,2,2,  2,0,1,1,0,  0,2,2,2,0,  1,1,2,2,2};
//    private static final String url = "http://smart-locks-review.toptenreviews.com/";

//    private static final int[] controle = {8, 8, 8, 8, 0, 0, 1, 0, 5, 1, 0, -1, -1, 1, 0, 0, 2, 2, 2, 0, 1, 1, 1, 1, 0, 0, 0, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2};
//    private static final String url = "http://smart-watch-review.toptenreviews.com/";

//    private static final int[] controle = {1,2,2,0,0,2,2,2,2,2,2,2,2,1,2,  2,2,2,2,2,2,2,2,2,  2,2,2,0,0,0,0,0,2,2,2,  2,2,2,2,2,2,  0,0,0,0,0,0,0};
//    private static final String url = "http://universal-remote-review.toptenreviews.com/";

//    private static final int[] controle = {8,0,2,2,2,2,2,2,  0,2,2,1,2,2,2,2,2,0,  0,2,2,2,  1,2,2,2, 0,0,0,0};
//    private static final String url = "http://vinyl-converter-turntable-review.toptenreviews.com/";

//    private static final int[] controle = {1,1,2,2,2,2,  2,2,2,2,2,2,2,  -1,0,2,2,2,2,2,2,2,  1,2,2,2,2};
//    private static final String url = "http://walkie-talkie-review.toptenreviews.com/";

//    private static final int[] controle = {1,2,2,2,  0,1,-5,    1,2,2,2};
//    private static final String url = "http://wireless-charger-review.toptenreviews.com/";

//    private static final int[] controle = {1,2,   1,1,5,1,0,2,2,2,2,2,2,2,2,2,2,2,2,2,  1,1,2,2,2,2,2,  1,1,2,2,2,2,2,2,  1,2,2,2};
//    private static final String url = "http://wireless-digital-frames-review.toptenreviews.com/";

//    private static final int[] controle = {2,2,2,2,2,2,2,  1,1,-1,1,2,2,2,2,2,2,2,  1,1,1,1,1,1,2,2,2,  1,2,2,2,2,  0,0,0,0};
//    private static final String url = "http://av-receiver-review.toptenreviews.com/";

//    private static final int[] controle = {1,1,1,2,2,  1,1,2,2,  2,2,2,2,2,2,2,2,0,  1,2,2,2,2,2,2};
//    private static final String url = "http://bluetooth-speakers-review.toptenreviews.com/";

//    private static final int[] controle = {1,1,-1,1,1,14,  1,0,-1,-1,-1,-1,0,  1,0,1,0,1, 1,2,2,2,2,2};
//    private static final String url = "http://bookshelf-speakers-review.toptenreviews.com/";

//    private static final int[] controle = {1,1,1,1,1,2,2,2,2,  2,2,2,2,2,2,2,2,2,2,  2,2,2,2,2,2,2,2,  2,2,2,2,2,2,  1,2,2,2,2,2,2};
//    private static final String url = "http://car-audio-review.toptenreviews.com/";

//    private static final int[] controle = {-1,1,0,1,  1,1,-1,1,-1,1,2,  -1,-1,-1,-1,  1,2,2,2};
//    private static final String url = "http://center-speaker-review.toptenreviews.com/";

//    private static final int[] controle = {1,-1,1,2,2,2,2,  1,1,1,1,1,2,2,2,  1,2,2,2,2,  0,0,0,0,   1,2,2,2,2};
//    private static final String url = "http://crank-radio-review.toptenreviews.com/";

//    private static final int[] controle = {8,8,8,8,0,1,  1,1,-1,1,0,1,  1,0,1,1,1,0,0,0,0,  1,2,2,2,  0,0,0};
//    private static final String url = "http://floor-standing-speakers.toptenreviews.com/";

//    private static final int[] controle = {8,8,-1,1,3,  1,1,  1,-5,-1,0,0,0,2,2,   1,1,2,2,2};
//    private static final String url = "http://home-subwoofers-review.toptenreviews.com/";

//    private static final int[] controle = {1,1,1,1,1,  1,1,1,1,1,1,1,2,2,2,2,2,2,2,2,2,2,2,  1,2,2,2,2,  0,0,0,0,0,0,0,0};
//    private static final String url = "http://home-audio.toptenreviews.com/home-theater-in-a-box/";

//    private static final int[] controle = {8,8,8,8,1,  0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,  1,2,2,2};
//    private static final String url = "http://home-audio.toptenreviews.com/home-theater-speakers/";

//    private static final int[] controle = {1,1,2,1,1,  1,1,2,  1,1,1,1,-5,-1,  1,2,2,2,2,2,  1,2,2,2,2};
//    private static final String url = "http://portable-speakers-review.toptenreviews.com/";
//    private static final int[] controle = {8,8,8,8,1,1,2,   2,1,1,1,2,2,2,2,2,2,  8,1,1,0,2,  1,2,2,2,  0,0,0,0,0,0,0};
//    private static final String url = "http://sound-bar-review.toptenreviews.com/";

//    private static final int[] controle = {8,1,1,1,1,  8,8,2,2,2,2,2,2,2,   -5,-1,0,0,  1,2,2,2,2};
//    private static final String url = "http://wireless-speakers-review.toptenreviews.com/";

//    private static final int[] controle = {8,8,3,1,   2,2,2,2,2,  2,2,2,2,2,0,0,-5,   1,2,2,2,2, 2,2,2,2,2,2,2,2,2,1};
//    private static final String url = "http://sound-machine-review.toptenreviews.com/";

//    private static final int[] controle = {1,1,1,2,  1,1,1,-1,0,-1,1,1,2,2,2,2,  1,2,2,2,2,2,2,2,    1,2,2,2};
//    private static final String url = "http://carpet-cleaners-review.toptenreviews.com/";

//    private static final int[] controle = {1,2,2,2,2,2,2,2,  -1,2,2,2,2,  2,2,-14,-14,-14,  1,1,2,2,2};
//    private static final String url = "http://central-air-conditioning-units-review.toptenreviews.com/";

    private static final int[] controle = {1,-1,-1,-1,-1,-1,  0,1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,  2,2,2,2,2,2,2,2,  2,2,2,2,2,2,  0,0,0,0,0};
    private static final String url = "http://chain-saws-review.toptenreviews.com/";

    private static final int[] controle = {8,1,0,0,1,-1,2,2,2,2,  8,-1,1,0,2,2,2,2,2,  2,2,2,2};
    private static final String url = "http://binoculars.toptenreviews.com/binoculars-review/";

    private static final int[] controle = {0,1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,  2,2,2,2,2,2,2,2,2,2,2,2,  2,2,  2,2,2,2};
    private static final String url = "http://chemistry-sets-review.toptenreviews.com/";

    private static final int[] controle = {1,1,1,1,1,1,2,2,2,1,1,2,  1,2,2,2,2,2,2,  1,2,2,2,2,2,  1,2,2,2,2};
    private static final String url = "http://microscopes.toptenreviews.com/kids-microscope-review/";

    private static final int[] controle = {0,0,  1,1,2,1,1,1,1,  1,1,-1,2,2,2,2,2,  1,0,1,2,2,2,2,  1,2,2,2,2};
    private static final String url = "http://telescopes.toptenreviews.com/telescopes-for-beginners-review/";

    private static final int[] controle = {8,1,1,1,1,0,2,  2,2,2,2,  1,0,2,2,2,2,  2,2,2,2,2,2,2,2,  1,1,2,2,  2,2,2,1};
    private static final String url = "http://train-sets.toptenreviews.com/";

    private static final int[] controle = {0,1,1,1,0,2,1,1,-4,1,2,2,2,  0,0,0,0,1,2,2,2,2,2,  0,-1,0,2,2,2,2,2,  1,2,2,2,2};
    private static final String url = "http://rc-drones-review.toptenreviews.com/";

    private static final int[] controle = {8,8,8,-1,6,1,2,2,2,   2,2,2,2,2,2,2,1,  1,0,2,2,0,  0,0,0,0,0,0,0,0};
    private static final String url = "http://remote-control-airplanes-review.toptenreviews.com/";

    private static final int[] controle = {1,1,1,1,1,1,1,2,   0,0,0,2,0,2,2,2,2,   0,1,2,1,1,1,1,2,1,  1,2,2,2,2,2,2,2,2};
    private static final String url = "http://radio-controlled-toys.toptenreviews.com/remote-control-boat-stores/";

    private static final int[] controle = {1,1,1,0,2,2,   1,0,4,2,2,2,5,1,   1,0,0,0,2,2,  1,2,2,2,2,2,2};
    private static final String url = "http://radio-controlled-toys.toptenreviews.com/remote-control-car-stores/";

    private static final int[] controle = {8,8,0,0,1,-1,1,2,2,0,  0,2,2,2,2,   0,1,0,2,2,2,  8,0,0,0,0,0,0};
    private static final String url = "http://remote-control-helicopters-review.toptenreviews.com/";

    private static final int[] controle = {2,2,2,2,2,2,2,1,0,1,  1,2,2,2,2,2,2,2,  2,2,2,2,2,  1,1,2,2,2,  0,0,0,0,0};
    private static final String url = "http://baby-car-seats-review.toptenreviews.com/";

    private static final int[] controle = {1,1,1,2,2,2,2,  2,2,2,2,2,2,2,2,2,  1,1,2,2,  0,0,0,0,  1,2,2,2,2,2,2};
    private static final String url = "http://bluetooth-car-kit-review.toptenreviews.com/";

    private static final int[] controle = {1,1,1,1,1,1,1,2,2,1,2,2,2,2,2,2,2,0,  2,2,2,2,1};
    private static final String url = "http://car-alarm-review.toptenreviews.com/";

    private static final int[] controle = {1,2,2,2,2,2,2,2,2,1,1,  4,4,1,1,  2,2,2,2,2,2,2,2,2,  2,2,2,2,2,  -1,1,2,2,2,-1,-1,-1};
    private static final String url = "http://car-battery-charger-review.toptenreviews.com/";

    private static final int[] controle = {2,2,1,2,1,2,2,2,2,2,1,1,1,0,2,2,2,2,2,2,  1,-1,-1,-1,-1,  2,2,2,1,2};
    private static final String url = "http://car-inverter-review.toptenreviews.com/";

    private static final int[] controle = {8,8,8,8,1,  8,1,1,1,1,   2,2,2,2,2,2,  2,2,2,2,2,2,2,2,  1,2,2,2,2};
    private static final String url = "http://car-speakers-review.toptenreviews.com/";

    private static final int[] controle = {1,-1,1,  1,1,1,0,  1,-1,-1,-1,2,  0,0,0,0,  1,2,2,2,2};
    private static final String url = "http://car-subwoofer-review.toptenreviews.com/";

    private static final int[] controle = {1,2,2,2,2,2,2,2,  2,2,2,2,2,  1,1,2,2,2,2,  1,2,2,2};
    private static final String url = "http://in-dash-navigation-review.toptenreviews.com/";

    private static final int[] controle = {1,1,2,2,2,2,  2,2,2,2,2,2,2,2,2,  2,2,2,2,2,2,2,2,  0,0,0,   1,2,2,2,2,2};
    private static final String url = "http://radar-detectors-review.toptenreviews.com/";

    private static final int[] controle = {1,2,2,2,2,  1,1,2,2,2,  5,1,2,2,  1,2,2,2};
    private static final String url = "http://rear-view-cameras-review.toptenreviews.com/";

    private static final int[] controle = {1,1,1,-1,1,   1,1,1,  1,1,2,2,2,2,2,2,  0,0,2,2,2,2,2,  1,2,2,2,2,2};
    private static final String url = "http://marine-speakers-review.toptenreviews.com/";

    private static final int[] controle = {-1,-1,-1,0,2,   1,1,1,  0,0,0,0,0,0,0,   2,2,2,2,2,2};
    private static final String url = "http://chest-freezers-review.toptenreviews.com/";

    private static final int[] controle = {-1,-1,-1,-1,1,1,1,1,2,2,2,2,2,2,  1,1,1,1,2,2,   1,2,2,  1,2,2,2,2,2};
    private static final String url = "http://circular-saws-review.toptenreviews.com/";

    private static final int[] controle = {1,2,2,2,2,2,2,2,2,2,2,   2,2,2,2,2,2,  2,2,2,2,  0,0,0,0,  1,2,2,2};
    private static final String url = "http://coffee-makers-review.toptenreviews.com/";

    private static final int[] controle = {1,1,1,2,2,2,2,2,   1,1,1,2,2,2,2,2,2,2,  1,1,1,2,2, 0,0,0,0,0};
    private static final String url = "http://compact-microwave-review.toptenreviews.com/";

    private static final int[] controle = {1,1,1,1,1,1,2,  -1,-1,-1,-1,2,2,2,2,2,2,  0,1,1,1,2,  1,1,2,2,2,2};
    private static final String url = "http://cordless-drill-review.toptenreviews.com/";

    private static final int[] controle = {0,0,0,   1,0,1,1,-1,1,2,   1,2,2,2,2,2,  1,2,2};
    private static final String url = "http://curling-irons-review.toptenreviews.com/";

    private static final int[] controle = {1,-1,1,1,2,1,1,   1,1,1,2,2,2,2,2,2,2,2,2,  -1,2,2,2,  1,2,2,2,  0,0,0,0};
    private static final String url = "http://deep-fryers-review.toptenreviews.com/";

    private static final int[] controle = {1,1,1,-1,2,2,2,    2,2,2,2,2,2,2,2,2,  1,1,1,  -1,-1,-1,-1,-1};
    private static final String url = "http://dehumidifiers-review.toptenreviews.com/";

    private static final int[] controle = {1,1,-1,-1,1,  1,1,1,1,1,0,0,2,0,2,2,2,2,2,2,2,2,2,2,  1,2,2,2};
    private static final String url = "http://digital-grill-thermometers-review.toptenreviews.com/";

    private static final int[] controle = {1,-1,2,  1,2,2,2,2,2,2,2,2,2,2,2,2,  1,1,1,2,2,  1,2,2,2,2,2,2, -1,1,1,0,2,2,2,-14,-14,-14,  1,1,1,1,2,2};
    private static final String url = "http://dishwasher-review.toptenreviews.com/";

    private static final int[] controle = {8,1,1,-1,-1,   1,2,2,2,1,   1,1,2,2,   1,2,2};
    private static final String url = "http://donut-makers-review.toptenreviews.com/";

    private static final int[] controle = {1,1,1,2,2,2,2,2,2,2,2,2,  2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,0,1,-14,-14,-14,-1,  1,2,2,2,2,  1,1,2,2,2,2,2};
    private static final String url = "http://dryers-review.toptenreviews.com/";

    private static final int[] controle = {1,1,1,4,4,2,2,  1,1,1,1,1,2,2,2,  -1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,  1,2,2,2,2,2,  0,0,0,0,0};
    private static final String url = "http://electric-range-review.toptenreviews.com/";

    private static final int[] controle = {2,2,2,1,1,2,2,2,2,   0,1,2,0,2,2,  2,2,2,  2,1,2,2,2,2};
    private static final String url = "http://electric-cooktop-review.toptenreviews.com/";

    private static final int[] controle = {1,1,1,1,  0,0,0,0,1,1,2,2,2,  2,2,2,2,2,  1,2,2,2,2};
    private static final String url = "http://electric-fireplace-review.toptenreviews.com/";

    private static final int[] controle = {1,0,1,2,2,  1,1,-1,1,2,2,2,2,2,2,2,2,  2,2,2,2,2,  1,2,2,2};
    private static final String url = "http://electric-hair-clippers-review.toptenreviews.com/";

    private static final int[] controle = {1,1,1,2,2,  0,1,-1,-1,2,2,2,2,2,  1,2,2};
    private static final String url = "http://electric-head-shaver-review.toptenreviews.com/";

    private static final int[] controle = {4,-1,1,0,1,-1,2,2,  1,1,1,1,1,  0,2,2,2,2,  -1,1,1,1,2,  1,2,2,2,2,2};
    private static final String url = "http://electric-lawn-mowers-review.toptenreviews.com/";

    private static final int[] controle = {1,1,2,2,1,   0,-1,-1,2,2,2,2,2,2,   2,2,2,  -1,-1,-1,   1,2,2,2,2};
    private static final String url = "http://electric-razors-review.toptenreviews.com/";

    private static final int[] controle = {-1,2,2,2,2,2,2,  1,1,1,1,1,2,  1,0,1,1,  1,2,2,2};
    private static final String url = "http://electric-string-trimmer-review.toptenreviews.com/";

    private static final int[] controle = {0,   1,1,1,-1,1,   1,2,2,2,2,2,2,2,2,2,2,  1,2,2,2,2};
    private static final String url = "http://electric-toothbrushes-review.toptenreviews.com/";

    private static final int[] controle = {1,1,1,  1,1,2,2,2,  -1,1,2,2,2,2,2,2,2,2,     1,2,2,2,2,   0,0,0};
    private static final String url = "http://double-ovens-review.toptenreviews.com/";

    private static final int[] controle = {1,1,1,1,1,1,1,   2,2,2,2,2,  0,0,0,0,0,0,  1,1,2,2,2,2};
    private static final String url = "http://electric-wheelchairs-review.toptenreviews.com/";

    private static final int[] controle = {1,1,-1,1,1,1,0,1,2,2,2,2,2,    1,2,2,2,2,2,2,2,2,  1,1,2,2,2,2,2,2,  2,2,2,2,2,2,2,  1,1,1,2,2,2,2,  0,0,0,0};
    private static final String url = "http://elliptical-machines-review.toptenreviews.com/";

    private static final int[] controle = {0,1,1,1,1,1,2,2,2,2,  1,1,1,-1,2,2,2,2,2,2,2,2,2,  -1,2,2,  1,2,2,   0,0,0,0,0,  1,2,2};
    private static final String url = "http://espresso-machine-review.toptenreviews.com/";

    private static final int[] controle = {0,1,1,2,2,  2,2,2,2,2,2,2,2,  0,0,0,0,0,0,0,  1,1,1,2,2,2,2};
    private static final String url = "http://exercise-bike-review.toptenreviews.com/";

    private static final int[] controle = {1,1,1,1,1,0,  2,2,2,2,2,2,1,   8,1,1,1,1,   1,1,0,1,-1,1,  1,2,2,2,2};
    private static final String url = "http://fitness-trackers-review.toptenreviews.com/";

    private static final int[] controle = {1,1,1,1,1,1,  1,1,1,-1,2,  1,1,1,1,   1,1,2,2,2};
    private static final String url = "http://food-processors-review.toptenreviews.com/";

    private static final int[] controle = {0,0,0,0,0,0,   2,2,2,2,2,2,   1,2,2,2,2,2,  1,2,2,2,2,2,2,  1,2,2,2,2};
    private static final String url = "http://pos-systems-review.toptenreviews.com/android/";

    private static final int[] controle = {0,0,0,0,0,  2,2,2,2,2,2,2,2,   2,2,2,2,2,2,2,  2,2,2,2,2,2,2,2,  1,1,2,2,2,2,2,2,2,  2,2,2,2,2,2  };
    private static final String url = "http://pos-systems-review.toptenreviews.com/ipad/";

    private static final int[] controle = {0,0,0,0,0,0,0,0,0,0,0,0,  2,2,2,2,2,2,2,2,2,2,2,2,2,2,  1,1,1,  2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,  2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,  2,2,2,2,2};
    private static final String url = "http://pos-systems-review.toptenreviews.com/ipad-restaurant/";

    private static final int[] controle = {0,0,0,  0,0,0,0,0,   2,2,2,2,2,  1,1,  2,2,2,2,2,2,2,2,2,2,  2,2,2,2,2,2,2,2,2,  2,2,2,2};
    private static final String url = "http://pos-systems-review.toptenreviews.com/online-pos/";

    private static final int[] controle = {0,  2,2,2,2,2,2,2,2,2,  2,2,2,2,2,2,2,2,  2,2,2,2,2,  2,2,2,2,2,2,2};
    private static final String url = "http://pos-systems-review.toptenreviews.com/hardware-software/";

    private static final int[] controle = {0,0,0,0,0,0,0,  2,2,2,2,2,2,2,2,2,2,2,2,  1,1,1,  2,2,2,2,2,2,2,2,2,2,2,2,  2,2,2,2,2,2,2,2,2,2,   2,2,2,2,2,2,2,2,  2,2,2,2,2,2,2,2,2,2,2,2,2,  2,2,2,2,2,2,2,2};
    private static final String url = "http://pos-systems-review.toptenreviews.com/restaurant/";

    private static final int[] controle = {0,0,  0,0,0,0,0,0,  1,1,1,1,  2,2,2,2,2,2,2,  2,2,2,2,2,2,  2,2,2,2,2,  2,2,2,2,2  };
    private static final String url = "http://pos-systems-review.toptenreviews.com/retail/";

    private static final int[] controle = {1,1,-1,1,2,2,  1,1,1,1,0,0,-1,2,2,   2,2,2,2,2,2,   1,2,2,2,2};
    private static final String url = "http://food-scales-review.toptenreviews.com/";

    private static final int[] controle = {1,1,1,1,1,  1,1,-1,1,1,2,2,2,2,2,2,2,  1,2,2,  1,2,2,   0,0,0};
    private static final String url = "http://food-steamers-review.toptenreviews.com/";

    private static final int[] controle = {1,1,1,  1,-1,-1,-1,2,  0,0,0,0,0,  1,1,0,0,2,2,  1,1,2,2,  1,1,0,  0,1,1,  1,2,2,  1,1,1,0,2,2,2,2};
    private static final String url = "http://french-door-refrigerator-review.toptenreviews.com/";

    private static final int[] controle = {-1,-1,-1,-1,-1,-1,2,  1,2,2,2,2,2,2,  1,1,2,2,2,2,2,2,2,  1,1,1,1,-1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,  0,0,0,0,0,0,  1,1,1,2,2,2};
    private static final String url = "http://front-load-washer-review.toptenreviews.com/";

    private static final int[] controle = {0,1,1,0,1,0,2,2,   2,2,2,2,2,2,  1,1,2,2,1,1,1,   1,1,1,2,2,2,2};
    private static final String url = "http://garage-door-openers-review.toptenreviews.com/";

    private static final int[] controle = {1,1,2,2,  0,2,2,2,2,  2,2,2,2,2,2,  1,2,2,2};
    private static final String url = "http://gas-cooktop-review.toptenreviews.com/";

    private static final int[] controle = {1,1,1,   1,1,1,1,1,1,   1,1,1,2,2,  0,0,0};
    private static final String url = "http://gas-furnaces-review.toptenreviews.com/";

    private static final int[] controle = {1,-6,1,2,2,  1,1,1,1,   1,1,1,1,1,1,   0,1,1,2,2,2,2,   1,1,2,2,  0,0,0,0};
    private static final String url = "http://gas-grills-review.toptenreviews.com/";

     private static final int[] controle = {1,1,1,-1,1,1,1,   1,1,1,2,2,2,   -1,0,1,2,   1,2,2,2};
    private static final String url = "http://gas-lawn-mower-review.toptenreviews.com/";

    private static final int[] controle = {1,1,1,2,2,1,   1,1,1,1,1,2,2,2,  2,2,2,2,2,2,  1,1,2,2,2,  0,0,0,0,0 };
    private static final String url = "http://gas-ranges-review.toptenreviews.com/";

    private static final int[] controle = {1,1,1,1,1,2,2,2,1,   -6,-6,-1,-1,2,2,2,2,   -1,1,2,2,2,2,-1,-1,-1,  1,2,2,2};
    private static final String url = "http://juicers-review.toptenreviews.com/";

    private static final int[] controle = {1,1,1,2,  1,-1,1,0,2,2,   1,2,2,2,  1,2,2};
    private static final String url = "http://hair-dryers-review.toptenreviews.com/";

    private static final int[] controle = {8,8,8,   8,1,-1,1,-1,1,1,2,2,2,2,2,2,  8,-1,1,1,2,2,2,2,2,2,  1,2,2,2,2,2,2,2 };
    private static final String url = "http://hair-straighteners-review.toptenreviews.com/";

    private static final int[] controle = {-1,-1,1,1,1,  1,-1,-1,2,2,2,   -1,2,2,2,2,  1,2,2,2,2};
    private static final String url = "http://handheld-vacuum-review.toptenreviews.com/";

    private static final int[] controle = {1,1,1,1,2,2,2,2,  -1,1,-1,1,2,2,2,2,2,   0,1,-1,2,  1,2,2,2,  1,2,2,2};
    private static final String url = "http://hardwood-floor-vacuum-cleaners-review.toptenreviews.com/";

    private static final int[] controle = {2,2,2,2,2,2,2,2,   1,0,5,14,1,1,2,2,2,   2,1,0,1,1,2,2,2,    1,2,2,2,2};
    private static final String url = "http://heart-rate-monitor-watches-review.toptenreviews.com/";

    private static final int[] controle = {0,1,-1,1,1,1,   -1,-1,-1,-1,1,2,2,2,   2,2,2,2,2,2,2,  2,2,2,2,  1,1,2,2,2};
    private static final String url = "http://home-gyms-review.toptenreviews.com/";

    private static final int[] controle = {-6,-6,1,1,1,   1,2,2,2,2,  1,2,2,  0,0,0,0,0,0,0};
    private static final String url = "http://hot-plates-review.toptenreviews.com/";

    private static final int[] controle = {1,1,1,0,2,   2,2,2,2,2,2,2,   -1,-1,-1,-1,2,2,   1,2,2,2};
    private static final String url = "http://humidifier-review.toptenreviews.com/";

    private static final int[] controle = {-1,1,1,0,  1,-1,-1,2,  0,1,-1,-1,-1,-1, 0,0,0,0};
    private static final String url = "http://ice-cream-makers-review.toptenreviews.com/";

    private static final int[] controle = {1,1,1,-1,-1,2,2,2,  -1,-1,2,2,2,2,   1,2,2,2,2,1,  1,2,2,2, -1,-1,-1};
    private static final String url = "http://indoor-electric-grills-review.toptenreviews.com/";

    private static final int[] controle = {1,1,1,1,2,  1,1,1,2,2,   1,2,2,2,  1,2,2,2};
    private static final String url = "http://induction-cooktop-review.toptenreviews.com/";

    private static final int[] controle = {1,1,1,   -6,1,1,   1,2,2,2,1,1,  1,0,1,2,2,2,   0,0,0,0,  1,1,1,1,1,2,2,2};
    private static final String url = "http://infrared-grills-review.toptenreviews.com/";

    private static final int[] controle = {8,8,8,8,8,8,  1,1,0,-1,0,1,1,-1,1,2,2,2,2,2,2,2,2,   1,-1,-1,-1,-1,1,1,1,2,2,2,2,2,2,2,2,2,2,  -1,-1,-1, 1,2,2,2,2,2};
    private static final String url = "http://irons-review.toptenreviews.com/";

    private static final int[] controle = {1,-1,2,2,2,2,  1,1,1,1,2,2,  -1,-1,-1,-1,0,1,2,2,2,2,2,2,  1,2,2,2,2,2};
    private static final String url = "http://laser-line-level-review.toptenreviews.com/";

    private static final int[] controle = {0,-1,-1,-1,1,1,   2,2,2,2,  1,2,2,2,  1,2,2,2};
    private static final String url = "http://leaf-blowers-review.toptenreviews.com/";

    private static final int[] controle = {1,-1,-1,1,1,1,2,2,   1,1,0,0,2,2,2,  1,1,1,  1,2,2,2};
    private static final String url = "http://led-light-bulbs-review.toptenreviews.com/";

    private static final int[] controle = {1,1,1,2,1,2,2,2,2,2,  1,1,1,2,2,2,2,2,   1,1,2,2,2,2,  1,1,1,2,2,  0,0,0};
    private static final String url = "http://over-the-range-microwave-review.toptenreviews.com/";

    private static final int[] controle = {1,1,1,1,2,   2,2,2,2,2,  0,2,2,2,2,2,  1,2,2,1,1,1,  -1,-1,-1,-1,  1,2};
    private static final String url = "http://meat-grinders-review.toptenreviews.com/";

    private static final int[] controle = {1,1,1,1,  0,0,1,1,2,  1,1,0,-1,2,2,2,2,  0,0,0,0, 1,2,2,2,2};
    private static final String url = "http://meat-slicers-review.toptenreviews.com/";

    private static final int[] controle = {1,-1,1,   1,1,0,2,-1,2,2,2,2,  1,2,2,2};
    private static final String url = "http://meat-thermometers-review.toptenreviews.com/";

    private static final int[] controle = {1,1,1,1,1,1,1,2,  2,2,2,2,2,2, 0,0,0,0,0,0,0,  2,2,2,2,2,2,2,2,2, 1,2,2,2};
    private static final String url = "http://mixer-review.toptenreviews.com/";

    private static final int[] controle = {0,0,0,  1,1,1,2,2,2,2,2,2,2,2,2,  2,1,2,  2,2,2,2,2,  2,2,2,2};
    private static final String url = "http://mold-test-kits-review.toptenreviews.com/";

    private static final int[] controle = {1,1,1,-1,-1,  1,1,1,1,2,  1,1,1,1,1,  2,2,2,  1,2,2,2,2,2};
    private static final String url = "http://table-saws-review.toptenreviews.com/";

    private static final int[] controle = {1,-1,2,2,  2,2,2,2,  2,2,2,2,2,2,2,  2,2,2,2,2};
    private static final String url = "http://teeth-whiteners-review.toptenreviews.com/";

    private static final int[] controle = {1,1,-6,1,2,   1,1,0,2,2,2,    2,2,-1,-1,1,    1,1,1,2,2,2,2,2,  0,0,0,0};
    private static final String url = "http://toaster-ovens-review.toptenreviews.com/";

    private static final int[] controle = {1,1,1,2,2,2,2,2,2,2,   2,2,2,2,2,2,  1,1,-1,-1,-1,-1,2,2,2,  1,2,2};
    private static final String url = "http://toaster-review.toptenreviews.com/";

    private static final int[] controle = {-1,-1,-1,-1,1,-1,-1,2,   1,1,1,1,1,2,  2,2,2,2,2,2,2,2,2,2,2,2,  2,2,2,2,2,2,2,2,2,2,  1,1,1,2,2,  0,0,0,0,0};
    private static final String url = "http://top-load-washing-machine-review.toptenreviews.com/";

    private static final int[] controle = {1,1,1,1,1,2,2,2,  -1,1,1,1,2,   1,2,2,2,2,2,  0,0,0,0};
    private static final String url = "http://tower-fan-review.toptenreviews.com/";

    private static final int[] controle = {1,1,1,1,1,1,1,   1,1,2,2,2,2,2,2,2,  1,2,2,2,2,2,2,  1,1,1,1,1,  2,2,2,2,2,  0,0,0,0};
    private static final String url = "http://treadmill-review.toptenreviews.com/";

    private static final int[] controle = {1,1,1,1,1,-1,1,   1,2,2,2,2,2,2,  2,2,2,2,  1,2,2,2,2,2};
    private static final String url = "http://turkey-fryers-review.toptenreviews.com/";

    private static final int[] controle = {-1,-1,0,2,   1,0,0,0,1,  0,2,2,2,2,2,2,2,2,2,   1,1,1,2};
    private static final String url = "http://upright-freezer-review.toptenreviews.com/";

    private static final int[] controle = {-1,-1,-1,  1,1,2,2,2,2,2,  1,1,-1,0,1,1,-1,1,0,2,2,2,  2,2,2,2,2,2,2,  2,2,2,2,   1,2,2,2};
    private static final String url = "http://upright-vacuum-cleaner-review.toptenreviews.com/";

    private static final int[] controle = {-1,-1,-1, 1,1,1,1,-1,1,2,2,2,2,2,2,  1,-1,-1,0,0,-1,2,2,2,2,2,2,2,2,  2,2,2,2,2,   1,2,2,2};
    private static final String url = "http://robot-vacuum-review.toptenreviews.com/";

    private static final int[] controle = {1,2,2,2,2,2,  1,1,1,1,   8,-6,-6,-6,2,2,2,   1,2,2,2,-1,-1,-1,  1,2,2,2};
    private static final String url = "http://waffle-maker-review.toptenreviews.com/";

    private static final int[] controle = {0,-1,1,  1,1,1,2,2,2,2,2,   2,2,2,2,2,2,2,2,  2,2,-1,1,1,1,  1,1,2,2,2};
    private static final String url = "http://wall-air-conditioners-review.toptenreviews.com/";

    private static final int[] controle = {1,1,1,1,-1,2,  2,2,2,2,2,2,2,2,   2,2,2,2,2,  2,2,2,2,2,2,2,   1,1,1,2,2,  0,0,0};
    private static final String url = "http://washer-dryer-combo-review.toptenreviews.com/";

    private static final int[] controle = {1,2,2,2,2,2,2,   1,1,1,1,1,2,   2,2,2,1,-1,-1,-1,-1,   1,2,2,2};
    private static final String url = "http://water-filter-systems-review.toptenreviews.com/";

    private static final int[] controle = {1,1,1,1,1,  -1,1,1,-1,1,1,2,2,2,2,   1,1,1,2,   1,1,2,2,2,2};
    private static final String url = "http://weed-whacker-review.toptenreviews.com/";

    private static final int[] controle = {0,1,-1,0,-11,  1,1,1,2,2,2,2,2,  2,2,2,2,2,2,2,2,  2,2,-1,-1,-11,-11,-11,  1,1,2,2,2};
    private static final String url = "http://window-air-conditioners-review.toptenreviews.com/";

    private static final int[] controle = {0,  1,-1,2,2,2,2,2,2,2,2,2,2,2,2,   1,2,2,2,2,2,2,1,  -1,-1,-1,-1,  1,2,2};
    private static final String url = "http://wine-cooler-review.toptenreviews.com/";

    private static final int[] controle = {1,0,1,1,0,1,2,2,2,2,2,2,2,2,  1,-1,2,2,2,2,2,2,2,2,  1,2,2,2,2,2,2,2,2,  1,2,2,2,2};
    private static final String url = "http://serger-sewing-machine-review.toptenreviews.com/";

    private static final int[] controle = {1,1,1,1,1,1,0,0,2,2,2,2,  2,2,2,2,2,2,2,2,2,1,-1,-1,-1,-1,  1,2,2,2,2,2,2,2,  1,2,2,2};
    private static final String url = "http://sewing-machine-review.toptenreviews.com/";

    private static final int[] controle = {1,1,1,1,1,2,2,2,2,  -1,-11,11,1,-1,2,2,   1,2,2,2,2,2,  1,2,2,2,2,2,2,2};
    private static final String url = "http://shop-vac-review.toptenreviews.com/";

    private static final int[] controle = {1,1,1,  1,-1,-1,-1,2,  0,0,0,0,  1,1,1,1,2,2,2,2,2,2,2,  1,1,1,  1,0,2,2,2,2,2,2,2,2,  1,1,1,2,2,2};
    private static final String url = "http://side-by-side-refrigerator-review.toptenreviews.com/";

    private static final int[] controle = {0,0,0,0,  1,0,1,1,4,1,1,2,2,  1,1,1,1,1,1,2,2,2,2,2,2,  1,2,2,2,  1,1,2,2,2,2};
    private static final String url = "http://slide-in-range-review.toptenreviews.com/";

    private static final int[] controle = {1,-1,-1,-1,-1,-1,0,2,   2,2,2,2,2,2,2,2,2,2,  2,2,2,2,  1,2,2,  0,0,0,0,0,0,0};
    private static final String url = "http://slow-cookers-review.toptenreviews.com/";

    private static final int[] controle = {2,2,2,  2,2,1,  2,2,2,2,2,  1,2,2,2};
    private static final String url = "http://smoke-detectors-review.toptenreviews.com/";

    private static final int[] controle = {1,1,1,1,1,1,0,  -1,1,1,2,2,  1,2,2,2,  1,2,2,2,2,2,2};
    private static final String url = "http://snow-blowers-review.toptenreviews.com/";

    private static final int[] controle = {-1,-1, 1,1,1,1,0,1,   1,1,1,1,1,1,1,   0,1,0,0,  -1,-1,-1,-1,  2,2,2,2,2, 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    private static final String url = "http://solar-panels-review.toptenreviews.com/";

    private static final int[] controle = {8,8,1,1,   -1,2,2,2,2,  2,2,2,2,  1,2,2,  0,0,0,0};
    private static final String url = "http://space-heaters-review.toptenreviews.com/";

    private static final int[] controle = {-1,-1,1,2,1,   2,2,2,2,2,2,2,2,2,2,  -1,1,-1,-1,1,1,1,  1,2,2,2,2};
    private static final String url = "http://stick-vacuum-review.toptenreviews.com/";

    private static final int[] controle = {1,1,0,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,   2,2,2,1,1,   -1,-1,-1,1,1,  2,2,2,2};
    private static final String url = "http://pedometers-review.toptenreviews.com/";

    private static final int[] controle = {4,14,-1,0,-1,1,0,  1,2,1,2,1,   2,2,2,  0,0,0,0,   1,2,2,2,2,2};
    private static final String url = "http://popcorn-maker-review.toptenreviews.com/";

    private static final int[] controle = {1,1,1,1,1,2,   1,-1,2,2,  2,2,2,2,2,2,  -11,-11,-11,-1,-11,1,1,1,2,2,   1,1,2,2,2,2,2};
    private static final String url = "http://portable-air-conditioners-review.toptenreviews.com/";

    private static final int[] controle = {1,1, 0,1,1,1,1,1,2,   2,2,2,2,2,2,2,-1,-1,  1,1,1,2,2,2,2,   1,2,2,2,2,2,2,2};
    private static final String url = "http://portable-generators-review.toptenreviews.com/";

    private static final int[] controle = {0,0,0,0,  -1,-1,-1,-1,1,1,2,2,2,2,2,2,  1,1,2,2,2,2,2,  2,2,2,2,2,  1,2,2,2,2,2};
    private static final String url = "http://portable-washing-machine.toptenreviews.com/";

    private static final int[] controle = {1,1,1,1,2,   1,0,-1,1,1,1,2,  1,1,-1,1,1,2,2,2,2,2,   1,1,1,1};
    private static final String url = "http://power-scooter-review.toptenreviews.com/";

    private static final int[] controle = {1,1,1,2,2,2,  2,2,2,2,2,2,  2,2,2,2,  1,1,2,2,1,1,0,  1,2,2,2,  1,2,2,2,2};
    private static final String url = "http://programmable-thermostats-review.toptenreviews.com/";
*/

    private static final int[] controle = {1,1,1,1,2,   1,0,-1,1,1,1,2,  -1,-1,-1,1,1,2,2,2,2,2,   1,1,1,1};
    private static final String url = "http://power-scooter-review.toptenreviews.com/";


    private static int num = 235;

//ZERO ignora,
    // -1 inverte,
    // 1 = igual,
    // 2 = true/false,
    // 3 = modulo(subtracao),
    // 4- max, -4 -> min,
    // 14 (inverso do maximo), -14 (inverso do minimo)
    // 5 - multiplicar, -5 -> invert(multiplica),
    // 6 - time, -6 (inverso do tempo)
    // 7 - dividir,
    // 8 - A+ - scale
    // 9 - GB
    //11 - numero + fracao (x/y)

    private static Path pathAlternatives = Paths.get("input/Alternatives" + num + ".dat");
    private static Path pathFeatures = Paths.get("input/Features" + num + ".dat");

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

    public static void main(String... args) throws IOException {

        Document document = Jsoup.connect(url).get();

        String text = document.select("h1[class=main_title]").text();
        String name = text.substring(0, text.length() - 8);
        Files.write(pathAlternatives, (name + "\n").getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);

        //Produtos
        String alternatives = printTen(document, "div[class=prodItemRow title]", 2, 0, false, null);
        Files.write(pathAlternatives, (alternatives + "\n").getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);

        //Overall ratings
        String ratings = printTen(document, "div[class=score]", 1, 0, false, null);
        Files.write(pathAlternatives, (ratings + "\n").getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);

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
        Files.write(pathFeatures, (allFeatures + "\n").getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);

        //Criteria
        String criteria = printTen(document, "li[class=carousel_column] > a", 1, 1, false, null);
        Files.write(pathFeatures, (criteria + "\n").getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);

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

            if (strOut.replaceAll("£", " ").trim().length() > 0) {

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
                        case -3: {
                            value = razaoInversa(subtrai(value), ",");
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
                        case 14: {
                            value = razaoInversa(getMax(value, true), ",");
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
                        case -6: {
                            value = razaoInversa(minutes(value), ",");
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
                        case 11: {
                            value = multiplicaRazao(value);
                            break;
                        }
                        case -11: {
                            value = razaoInversa(multiplicaRazao(value), ",");
                            break;
                        }
                        default: {
                            value = value.substring(0, value.length() - 3);
                            break;
                        }
                    }
                    value = value.replaceAll(VALUE_SEPARATOR, ",");
                    System.out.println(value);
                    value = (value.length() > 0) ? value + "\n" : "";
                    Files.write(pathAlternatives, value.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                    pos[0]++;
                }
            }
        } else {
            System.out.println(strOut);
        }

        return strOut;


    }

    private static String multiplicaRazao(String value) {
        String[] values = value.split(VALUE_SEPARATOR);
        String valorTratado = "";

        for (String val : values) {
            String[] split = val.split(" ");
            double val0 = Double.valueOf(split[0]);

            double val1 = 0d;
            if (split.length > 1) {
                double dividendo = Double.valueOf(split[1].split("/")[0]);
                double divisor = Double.valueOf(split[1].split("/")[1]);
                val1 = dividendo / divisor;
            }

            valorTratado += (val0 + val1) + ",";
        }
        value = valorTratado.substring(0, valorTratado.length() - 1);
        return value;

    }

    private static String minutes(String value) {
        String[] values = value.split(VALUE_SEPARATOR);
        String valorTratado = "";

        for (String val : values) {
            String[] split = val.split(":");
            double val0 = (!"".equals(split[0])) ? Double.valueOf(split[0]) : 0d;

            double val1 = split.length > 1 ? Double.valueOf(split[1]) : 0d;

            valorTratado += ((val0 * 60) + val1) + ",";
        }
        value = valorTratado.substring(0, valorTratado.length() - 1);
        return value;
    }

    private static String multiplica(String value) {
        String[] values = value.split(VALUE_SEPARATOR);
        String valorTratado = "";

        for (String val : values) {


            String regex = val.contains("x") ? "x" : "-";


            String[] split = val.split(regex);

//            try {
                double val0 = Double.valueOf(split[0].replaceAll(" ", "").trim());

                if (val0 == 0) {

                    valorTratado += "0,";

                } else {


                    double val1 = (split.length == 2) ? Double.valueOf(split[1].replaceAll(" ", "").trim()) : val0;

                    double val2 = (split.length == 3) ? Double.valueOf(split[2].replaceAll(" ", "").trim()) : 1;

                    valorTratado += df.format(val0 * val1 * val2) + ",";
                }
//            } catch (Exception e) {
//                valorTratado += "0,";
//            }


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


            String regex = val.contains("-") ? "-" : "to";
            String[] strings = val.split(regex);
            double val0 = Double.valueOf(strings[0].trim());
            double val1 = Double.valueOf(strings[1].trim());

            valorTratado += Math.abs(val0 - val1) + ",";
        }
        value = valorTratado.substring(0, valorTratado.length() - 1);
        return value;
    }

    private static String razaoInversa(String value, String regex) {
        String[] values = value.split(regex);
        String valorTratado = "";

        for (String val : values) {
            val = val.replaceAll(",", "").replaceAll("NA", "0");

//            try {
            double v = Double.valueOf(val.trim()).doubleValue();

            if (v == 0) {
                valorTratado += "0,";
            } else {
                valorTratado += df.format(1 / v) + ",";
            }
//            }catch(Exception e){
//                valorTratado += "0,";
//            }


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

//            split = val.split(" ");
            if (val.contains("-")) {
                split = val.split("-");
            } else if (val.contains("/")) {
                split = val.split("/");
            } else if (val.contains(",")) {
                split = val.split(",");
            } else if (val.contains(":")) {
                split = val.split(":");
            } else {
                split = val.split(" ");
            }

            int i = asc ? split.length - 1 : 0;
            valorTratado += split[i].trim() + ",";
        }
        value = valorTratado.substring(0, valorTratado.length() - 1);
        return value;
    }

    private static String clean(String text) {
        return text.
                replaceAll("no value disclosed","0").replaceAll("Not Available", "0").replaceAll("N/A", "0").replaceAll("n/a", "0").replaceAll(" sec", "").replaceAll(" dB", "").
                replaceAll("Not Listed", "0").replaceAll("None", "0").replaceAll("Not Recommended", "0").
                replaceAll("Not specified", "0").replaceAll("Not Specified", "0").
                replaceAll("\\(13-inch\\)", "").replaceAll("\\(1 For Roku\\)", "").

                replaceAll("\\(incl. 1x USB-C\\)", "").replaceAll("f\\/", "").replaceAll("\\(package of two\\)","").
                replaceAll("\\(charging port\\)", "").
                replaceAll("via Dongle", "0.5").replaceAll(" in\\.", "").

//                replaceAll("x", "").
        replaceAll(" Year", "").replaceAll(" in", "").replaceAll(" in\\.", "").replaceAll(" oz\\.", "").replaceAll("oz", "").replaceAll("lbs", "").replaceAll("lb", "").replaceAll("ft\\.", "").replaceAll("ohms", "").replaceAll("Distributor", "0").
                replaceAll("%", "").replaceAll("X", "x").replaceAll("\\$", "").
                replaceAll("GHz", "").replaceAll("W", "").replaceAll(" ms", "").
                replaceAll("p", "").replaceAll("\\*", "").
                replaceAll("None", "0").
                replace('\uE60B', '1');
    }

}
