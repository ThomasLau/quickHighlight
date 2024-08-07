package xyz.thomaslau.highlight;

import java.util.Arrays;

import com.hankcs.hanlp.collection.AhoCorasick.AhoCorasickDoubleArrayTrie;

public class TestHilight {
	public static void main(String[] args) {
		int[][] timeIntervals = { { 1, 3 }, { 2, 6 }, { 8, 10 }, { 15, 18 } };
		int[][] result = QuickHighlight.merge(timeIntervals);
		System.out.println(Arrays.deepToString(result));
		System.out.println(QuickHighlight.highlight(Utils.text, Utils.keywords, "<em>", "</em>"));
		long start = System.currentTimeMillis();
		for (int i = 0; i < 100000; i++) {
			String ss = QuickHighlight.highlight(Utils.text, Utils.keywords, "<em>", "</em>");
		}
		System.out.println("took:" + (System.currentTimeMillis() - start) + "ms");

		System.out.println(QuickHighlight.highlightTradition(Utils.text_Trad, Utils.keywords, "<em>", "</em>"));
		System.out.println(QuickHighlight.highlightTradition(Utils.text_Trad, Arrays.asList("？"), "<em>", "</em>"));
		System.out.println(QuickHighlight.highlightTradition(Utils.text_dirty, Utils.keywords, "<em>", "</em>"));
		start = System.currentTimeMillis();
		for (int i = 0; i < 100000; i++) {
			String ss = QuickHighlight.highlightTradition(Utils.text_Trad, Utils.keywords, "<em>", "</em>");
		}
		System.out.println("took:" + (System.currentTimeMillis() - start) + "ms");

		AhoCorasickDoubleArrayTrie<String> acdtX = AcAutoHighLight.buildAcdt(Utils.keywords);
		String resultX = AcAutoHighLight.highLight(Utils.text, acdtX);
		System.out.println(resultX);

		start = System.currentTimeMillis();
		for (int i = 0; i < 10; i++) {
			AhoCorasickDoubleArrayTrie<String> acdt = AcAutoHighLight.buildAcdt(Utils.keywords);
			String resultx = AcAutoHighLight.highLight(Utils.text, acdt);
			// System.out.println("inpu:" + text);
			// System.out.println("result:" + result);
			// result = HighLightX.highLight(text, acdt);
		}
		System.out.println("took:" + (System.currentTimeMillis() - start) + "ms");

	}
}
