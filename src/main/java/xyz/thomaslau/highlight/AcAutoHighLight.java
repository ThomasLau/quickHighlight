package xyz.thomaslau.highlight;

import com.hankcs.hanlp.collection.AhoCorasick.AhoCorasickDoubleArrayTrie;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class AcAutoHighLight {

    public static AhoCorasickDoubleArrayTrie<String> buildAcdt(List<String> keywords) {
        AhoCorasickDoubleArrayTrie<String> acdt = new AhoCorasickDoubleArrayTrie<>();
        TreeMap<String, String> map = new TreeMap<>();
        for (String keyword : keywords) {
            map.put(keyword, keyword);
        }
        acdt.build(map);
        return acdt;
    }

    public static String highLight(String originText, AhoCorasickDoubleArrayTrie<String> acdt) {
        List<int[]> hitLocationList = new ArrayList<>();
        acdt.parseText(originText, (begin, end, value) -> {
            int[] indexPair = new int[2];
            indexPair[0] = begin;
            indexPair[1] = end - 1;
            hitLocationList.add(indexPair);
        });
        byte[] posStatus = new byte[originText.length()];
        for (int[] item : hitLocationList) {
            posStatus[item[0]] = 1;
            for (int i = item[0]; i <= item[1]; i++) {
                posStatus[i] = 1;
            }
        }
        int lastStatus = 0;
        char[] charArray = originText.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < posStatus.length; i++) {
            if (posStatus[i] == lastStatus) {
                sb.append(charArray[i]);
            } else if (0 == lastStatus) {
                sb.append("<em>").append(charArray[i]);
                lastStatus = 1;
            } else if (1 == lastStatus) {
                sb.append("</em>").append(charArray[i]);
                lastStatus = 0;
            }
            if (i == posStatus.length - 1 && 1 == lastStatus) {
                sb.append("</em>");
            }
        }
        return sb.toString();
    }

}
