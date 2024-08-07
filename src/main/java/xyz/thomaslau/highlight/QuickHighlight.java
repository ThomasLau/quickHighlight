package xyz.thomaslau.highlight;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class QuickHighlight {
    public static int[][] merge(int[][] intervals) {
        int intLen = intervals.length;
        Arrays.sort(intervals, new Comparator<int[]>() {
            @Override
            public int compare(int[] a, int[] b) {
                return a[0] - b[0];
            }
        });
        List<int[]> list = new ArrayList<>();
        for (int i = 0; i < intLen; i++) {
            int left = intervals[i][0], right = intervals[i][1];
            if (list.size() == 0 || list.get(list.size() - 1)[1] < left) {
                list.add(new int[] { left, right });
            } else {
                list.get(list.size() - 1)[1] = Math.max(right, list.get(list.size() - 1)[1]);
            }
        }
        return list.toArray(new int[list.size()][]);
    }

    public static List<int[]> mergeList(List<int[]> intervals) {
        int intLen = intervals.size();
        Collections.sort(intervals, new Comparator<int[]>() {
            @Override
            public int compare(int[] a, int[] b) {
                return a[0] - b[0];
            }
        });
        List<int[]> list = new ArrayList<>();
        for (int i = 0; i < intLen; i++) {
            int left = intervals.get(i)[0], right = intervals.get(i)[1];
            if (list.size() == 0 || list.get(list.size() - 1)[1] < left) {
                list.add(new int[] { left, right });
            } else {
                list.get(list.size() - 1)[1] = Math.max(right, list.get(list.size() - 1)[1]);
            }
        }
        return list;
    }

    private static List<int[]> find(String source, List<String> patterns) {
        List<int[]> intervs = new ArrayList<>();
        patterns.forEach(s -> {
            if (null == s || "".equals(s)) {
                return;
            }
            int stidx = 0;
            int found = -1;
            while ((found = source.indexOf(s, stidx)) != -1) {
                stidx = found + s.length();
                intervs.add(new int[] { found, stidx });
            }
        });
        List<int[]> retlist = mergeList(intervs);
        return retlist;
    }

    /**
     * 原文highlight
     */
    public static String highlight(String source, List<String> patterns, String left, String right) {
        return highlight(source, null, patterns, left, right);
    }

    private static String highlight(String source, String target, List<String> patterns, String left, String right) {
        if (null == source || "".equals(source)) {
            return source;
        }
        List<int[]> retlist = find(null == target ? source : target, patterns);
        StringBuilder sb = new StringBuilder();
        AtomicInteger lastPos = new AtomicInteger(0);
        retlist.forEach(ar -> {
            if (lastPos.get() != ar[0]) {
                sb.append(source.subSequence(lastPos.get(), ar[0]));
            }
            sb.append(left);
            sb.append(source.subSequence(ar[0], ar[1]));
            sb.append(right);
            lastPos.set(ar[1]);
        });
        if (lastPos.get() < source.length()) {
            sb.append(source.subSequence(lastPos.get(), source.length()));
        }
        return sb.toString();
    }

    /**
     * 统一小写(default locale)、简体 高亮
     */
    public static String highlightTradition(String source, List<String> patterns, String left, String right) {
        String target = source.toLowerCase();
        target = SimpleChineseHelper.simplified(target);
        return highlight(source, target, patterns, left, right);
    }

}
