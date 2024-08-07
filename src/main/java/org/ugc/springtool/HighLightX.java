package org.ugc.springtool;

import com.hankcs.hanlp.collection.AhoCorasick.AhoCorasickDoubleArrayTrie;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

public class HighLightX {

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

	public static void main(String[] args) {
		// String text = "我想买苹果手机，iphone6 请问哪里可以买苹果手机";
		// List<String> keywords = Arrays.asList("苹果", "苹果手机", "哪里","6","iphonec6");
		String text = "形态什么？学习 lgbt？还是学习快乐教育？不知道江泽民怎么看习近平易近人是不是";
		List<String> keywords = Arrays.asList("习", "lgbt", "江泽民", "江", "习近平", "平易");
		AhoCorasickDoubleArrayTrie<String> acdtX = HighLightX.buildAcdt(keywords);
		String resultX = HighLightX.highLight(text, acdtX);
		System.out.println(resultX);

		long start = System.currentTimeMillis();
		for (int i = 0; i < 100000; i++) {
			AhoCorasickDoubleArrayTrie<String> acdt = HighLightX.buildAcdt(keywords);
			String result = HighLightX.highLight(text, acdt);
			// System.out.println("inpu:" + text);
			// System.out.println("result:" + result);
			// result = HighLightX.highLight(text, acdt);
		}
		System.out.println("took:" + (System.currentTimeMillis() - start) + "ms");

	}

}
