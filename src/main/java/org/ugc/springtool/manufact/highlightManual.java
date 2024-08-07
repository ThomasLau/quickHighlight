package org.ugc.springtool.manufact;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class highlightManual {
	public static int[][] merge(int[][] intervals) {
		int m = intervals.length;

		Arrays.sort(intervals, new Comparator<int[]>() {
			@Override
			public int compare(int[] a, int[] b) {
				return a[0] - b[0];
			}
		});
		List<int[]> list = new ArrayList<>();
		for (int i = 0; i < m; i++) {
			int l = intervals[i][0], r = intervals[i][1];
			if (list.size() == 0 || list.get(list.size() - 1)[1] < l) {// 前一个区间的右端点小于当前区间的左端点
				list.add(new int[] { l, r });
			} else {
				list.get(list.size() - 1)[1] = Math.max(r, list.get(list.size() - 1)[1]);
			}
		}
		return list.toArray(new int[list.size()][]);
	}

	public static List<int[]> mergeList(List<int[]> intervals) {
		int m = intervals.size();
		Collections.sort(intervals, new Comparator<int[]>() {
			@Override
			public int compare(int[] a, int[] b) {
				return a[0] - b[0];
			}
		});
		List<int[]> list = new ArrayList<>();
		for (int i = 0; i < m; i++) {
			int l = intervals.get(i)[0], r = intervals.get(i)[1];
			if (list.size() == 0 || list.get(list.size() - 1)[1] < l) {// 前一个区间的右端点小于当前区间的左端点
				list.add(new int[] { l, r });
			} else {
				list.get(list.size() - 1)[1] = Math.max(r, list.get(list.size() - 1)[1]);
			}
		}
		return list;
	}

	private static List<int[]> find(String source, List<String> patterns) {
		List<int[]> intervs = new ArrayList<>();
		patterns.forEach(s -> {
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

	public static String highlight(String source, List<String> patterns, String left, String right) {
		List<int[]> retlist = find(source, patterns);
		// System.out.println(Arrays.deepToString(retlist.toArray(new
		// int[retlist.size()][])));
		StringBuilder sb = new StringBuilder();
		AtomicInteger lastPos = new AtomicInteger(0);
		retlist.forEach(ar -> {
			// sb.insert(ar[0], "<em>");
			// sb.insert(ar[1], "</em>");
			if (lastPos.get() != ar[0]) {
				sb.append(source.subSequence(lastPos.get(), ar[0]));
			}
			sb.append(left);
			sb.append(source.subSequence(ar[0], ar[1]));
			sb.append(right);
			lastPos.set(ar[1]);
		});
		// System.out.println(lastPos.get());
		if (lastPos.get() < source.length()) {
			sb.append(source.subSequence(lastPos.get(), source.length()));
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		int[][] timeIntervals = { { 1, 3 }, { 2, 6 }, { 8, 10 }, { 15, 18 } };
//        int [][] timeIntervals = {{4,5}, {1,4}, {0,1}};
//        int [][] timeIntervals = {{1, 4}, {4, 5}};
		int[][] result = merge(timeIntervals);
		System.out.println(Arrays.deepToString(result));

		String text = "形态什么？学习 lgbt？还是学习快乐教育？不知道江泽民怎么看习近平易近人是不是";
		List<String> keywords = Arrays.asList("习", "lgbt", "江泽民", "江", "习近平", "平易近人");
		System.out.println(highlight(text, keywords, "<em>", "</em>"));
		long start = System.currentTimeMillis();
		for (int i = 0; i < 100000; i++) {
			String ss = highlight(text, keywords, "<em>", "</em>");
		}
		System.out.println("took:" + (System.currentTimeMillis() - start) + "ms");
	}
}


