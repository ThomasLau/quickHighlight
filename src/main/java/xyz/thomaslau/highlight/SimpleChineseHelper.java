package xyz.thomaslau.highlight;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleChineseHelper {
    private static Logger log = LoggerFactory.getLogger(SimpleChineseHelper.class);

    private static volatile Map<String, String> TRAD_SIMPL_MAP = new HashMap<String, String>();
    @SuppressWarnings("unused")
    private static SimpleChineseHelper instance = new SimpleChineseHelper();

    private SimpleChineseHelper() {
        readChineseResource();
    }

    private static void readCpResource(String path, Consumer<String> processor) {
        String line = null;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(SimpleChineseHelper.class.getResourceAsStream(path), "UTF-8"))) {
            while ((line = br.readLine()) != null) {
                processor.accept(line);
            }
        } catch (Exception e) {
            log.error("err_line: " + line, e);
        }
    }

    private static void readChineseResource() {
        Map<String, String> map = new HashMap<String, String>();
        readCpResource("/chinese.dict", line -> {
            String[] tokens = line.split("=");
            map.put(tokens[0], tokens[1]);
        });
        synchronized (SimpleChineseHelper.class) {
            TRAD_SIMPL_MAP = map;
        }
        log.info("traditional chinese:{}", TRAD_SIMPL_MAP.size());
    }

    public static char conv2SimplifiedChinese(char c) {
        String simpChineseCh = TRAD_SIMPL_MAP.get(String.valueOf(c));
        return (null == simpChineseCh) ? c : simpChineseCh.charAt(0);
    }

    public static String simplified(String str) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0, len = str.length(); i < len; i++) {
            char c = str.charAt(i);
            sb.append(conv2SimplifiedChinese(c));
        }
        return sb.toString();
    }
}
