package lab1;

/**
 * Created by xymeow on 15/10/27.
 */

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queries.mlt.MoreLikeThis;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Search {
    private String getTime(String url) throws IOException{
        String reg = "[//s//S]*?/(\\d{4}-\\d{2}-\\d{2})/[//s//S]*?";
        Pattern p = Pattern.compile(reg,Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(url);
        if (m.find())
            return m.group(1);
        reg = "[//s//S]*?/(\\d{4})(\\d{2})(\\d{2})/[//s//S]*?";
        p = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
        m = p.matcher(url);
        if(m.find()){
            String time = m.group(1)+"-"+m.group(2)+"-"+m.group(3);
            return time;
        }
        return null;
    }

    public Result search(String queryStr, int maxResults) throws IOException {
        /**
         *  查询
         */
        Analyzer analyzer = new IKAnalyzer();
        Path indexDir = Paths.get("/Users/apple/Documents/web/lab/lab1/Index"); //绝对路径,或者将Index放入tomcat/bin中用相对路径
        Directory directory = FSDirectory.open(indexDir);
        IndexReader reader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(reader);
        String[] fields = { "content", "keywords", "title", "description"};
        MoreLikeThis mlt = new MoreLikeThis(reader);
        mlt.setFieldNames(new String[]{"content", "keywords"});//相似性搜索
        mlt.setMinTermFreq(0);
        mlt.setMinDocFreq(0);
        mlt.setAnalyzer(analyzer);
        MultiFieldQueryParser mp = new MultiFieldQueryParser(fields, analyzer);
        BooleanClause.Occur[] flags = {BooleanClause.Occur.SHOULD};
        Query q = null;
        try {
            q = mp.parse(queryStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        TopDocs topDocs = searcher.search(q, maxResults);
        Result results = new Result();
        SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<font color='red'>","</font>");//高亮关键词
        Highlighter highlighter = new Highlighter(simpleHTMLFormatter,new QueryScorer(q));
        highlighter.setTextFragmenter(new SimpleFragmenter(300));
        results.contents = new ArrayList<>();
        results.urls = new ArrayList<>();
        results.titles = new ArrayList<>();
        results.publishTime = new ArrayList<>();
        results.moreLike = new ArrayList<>();
        results.moreURL = new ArrayList<>();
        results.totalDocs = topDocs.totalHits;
        maxResults = Math.min(maxResults, topDocs.scoreDocs.length);

        for (int i = 0; i < maxResults; i++){
            int docID = topDocs.scoreDocs[i].doc;
            String text = searcher.doc(docID).getField("content").stringValue();
            String url = searcher.doc(docID).getField("url").stringValue();
            String title = searcher.doc(docID).getField("title").stringValue();
            String time = getTime(url);
            try {
                results.contents.add(highlighter.getBestFragment(analyzer, "content", text));
                results.urls.add(url);
                results.publishTime.add(time);
                if (highlighter.getBestFragment(analyzer,"title",title)!=null)
                    results.titles.add(highlighter.getBestFragment(analyzer,"title",title));
                else
                    results.titles.add(title);
                Query more = mlt.like(docID);
                TopDocs similarDocs = searcher.search(more, 2);
                Document doc;
                int j = similarDocs.scoreDocs.length-1;
                if (similarDocs.scoreDocs[j].doc != docID) {
                        doc = reader.document(similarDocs.scoreDocs[j].doc);
                        results.moreLike.add(doc.getField("title").stringValue());
                        results.moreURL.add(doc.getField("url").stringValue());
                    }
                else{
                        results.moreLike.add(null);
                        results.moreURL.add(null);
                }
            } catch (InvalidTokenOffsetsException e) {
                e.printStackTrace();
            }
        }
        return results;
    }
}
