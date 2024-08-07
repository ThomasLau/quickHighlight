package xyz.thomaslau.highlight;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.highlight.DefaultEncoder;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryTermScorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;

public class LuceneHighlight {

    public static void main(String[] args) throws IOException, InvalidTokenOffsetsException {

        // String[] keywords = new String[] { "lucene", "search", "64" };
        // String text = "The goal of 64 Apache Lucene is to provide world class search capabilities.";
        String[] keywords = Utils.keywords.toArray(new String[Utils.keywords.size()]);
        String text = Utils.text;

        // Lucene内置的标准分词器
        // StandardAnalyzer analyzer = new StandardAnalyzer();
        // Analyzer analyzer = new KeywordAnalyzer();
        Analyzer analyzer = new CJKAnalyzer();
        // 搜索field0中匹配短语(lucene search)且slop不超过1的文档
        PhraseQuery phraseQuery = new PhraseQuery(1, "field0", keywords);
        // Highlighter四大组件：formatter，encoder，scorer，fragmenter
        SimpleHTMLFormatter formatter = new SimpleHTMLFormatter();
        DefaultEncoder encoder = new DefaultEncoder();
        QueryTermScorer scorer = new QueryTermScorer(phraseQuery);
        Highlighter highlighter = new Highlighter(formatter, encoder, scorer);
        // 设置每个高亮分片的最大长度为10（字符个数）
        SimpleFragmenter fragmenter = new SimpleFragmenter(10);
        highlighter.setTextFragmenter(fragmenter);
        // 寻找最多5个高亮分片
        String[] fragments = highlighter.getBestFragments(analyzer, "field0", text, 5);
        for (String fragment : fragments) {
            System.out.println(fragment);
        }
    }
}
