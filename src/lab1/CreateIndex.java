package lab1;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by xymeow on 15/11/15.
 */
public class CreateIndex {
    public static void main(String[] args) throws IOException {
        /**========================= 网页预处理 ============================
         * 将txt文档中的文本先全部读入buffer中,建立Doc类数组,将提取到的信息存储为
         * 每个Doc类的对应属性的值
         */
        String buffer, buffer2, buffer3;
        buffer = Txt2String.readFile("新浪新闻/2012.q1.txt") + Txt2String.readFile("新浪新闻/2012.q2.txt");
        buffer2 = Txt2String.readFile("新浪新闻/2012.q3.txt") + Txt2String.readFile("新浪新闻/2012.q4.txt");
        buffer3 = Txt2String.readFile("新浪新闻/2013.q1.txt");
        Html2Text retyper = new Html2Text();
        int docsNumber1,docsNumber2,docsNumber3;
        docsNumber1 = retyper.countDocs(buffer);
        docsNumber2 = retyper.countDocs(buffer2);
        docsNumber3 = retyper.countDocs(buffer3);
        //docsNumber = docsNumber1 + docsNumber2 + docsNumber3;
        Doc[] docs = new Doc[docsNumber1];
        Doc[] docs2 = new Doc[docsNumber2];
        Doc[] docs3 = new Doc[docsNumber3];
        List<String> temp,temp2,temp3;
        temp = retyper.matchDoc(buffer);
        for (int i = 0; i < docsNumber1; i++) {
            docs[i] = new Doc();
            docs[i].buffer = temp.get(i);
            docs[i].url = retyper.matchLabel(docs[i].buffer, "url");
            docs[i].description = retyper.match(docs[i].buffer, "description");
            docs[i].keyword = retyper.match(docs[i].buffer, "keywords");
            docs[i].title = retyper.matchLabel(docs[i].buffer, "title");
            docs[i].publishid = retyper.match(docs[i].buffer, "\"publishid\"");
            docs[i].subjectid = retyper.match(docs[i].buffer, "\"subjectid\"");
            docs[i].content = retyper.getcontent(docs[i].buffer);
        }
        temp2 = retyper.matchDoc(buffer2);
        for (int i = 0; i < docsNumber2; i++) {
            docs2[i] = new Doc();
            docs2[i].buffer = temp2.get(i);
            docs2[i].url = retyper.matchLabel(docs2[i].buffer, "url");
            docs2[i].description = retyper.match(docs2[i].buffer, "description");
            docs2[i].keyword = retyper.match(docs2[i].buffer, "keywords");
            docs2[i].title = retyper.matchLabel(docs2[i].buffer, "title");
            docs2[i].publishid = retyper.match(docs2[i].buffer, "\"publishid\"");
            docs2[i].subjectid = retyper.match(docs2[i].buffer, "\"subjectid\"");
            docs2[i].content = retyper.getcontent(docs2[i].buffer);
        }
        temp3 = retyper.matchDoc(buffer3);
        for (int i = 0; i < docsNumber3; i++) {
            docs3[i] = new Doc();
            docs3[i].buffer = temp3.get(i);
            docs3[i].url = retyper.matchLabel(docs3[i].buffer, "url");
            docs3[i].description = retyper.match(docs3[i].buffer, "description");
            docs3[i].keyword = retyper.match(docs3[i].buffer, "keywords");
            docs3[i].title = retyper.matchLabel(docs3[i].buffer, "title");
            docs3[i].publishid = retyper.match(docs3[i].buffer, "\"publishid\"");
            docs3[i].subjectid = retyper.match(docs3[i].buffer, "\"subjectid\"");
            docs3[i].content = retyper.getcontent(docs3[i].buffer);
        }
        /**======================== 创建索引 ===============================
         *
         */
        Path indexDir = Paths.get("Index");
        Directory directory = FSDirectory.open(indexDir);
        Analyzer analyzer = new IKAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter indexWriter = new IndexWriter(directory, config);
        Document[] document = new Document[docsNumber1];
        Document[] document2 = new Document[docsNumber2];
        Document[] document3 = new Document[docsNumber3];
        for (int i = 0; i < docsNumber1; i++) {
            document[i] = new Document();
            document[i].add(new Field("url", docs[i].url, StringField.TYPE_STORED));
            document[i].add(new Field("description", docs[i].description, StringField.TYPE_STORED));
            document[i].add(new Field("keywords", docs[i].keyword, StringField.TYPE_STORED));
            document[i].add(new Field("title", docs[i].title, StringField.TYPE_STORED));
            document[i].add(new Field("publishid", docs[i].publishid, StringField.TYPE_STORED));
            document[i].add(new Field("subjectid", docs[i].subjectid, StringField.TYPE_STORED));
            document[i].add(new Field("content", docs[i].content, TextField.TYPE_STORED));
            indexWriter.addDocument(document[i]);
        }
        for (int i = 0; i < docsNumber2; i++) {
            document2[i] = new Document();
            document2[i].add(new Field("url", docs2[i].url, StringField.TYPE_STORED));
            document2[i].add(new Field("description", docs2[i].description, StringField.TYPE_STORED));
            document2[i].add(new Field("keywords", docs2[i].keyword, StringField.TYPE_STORED));
            document2[i].add(new Field("title", docs2[i].title, StringField.TYPE_STORED));
            document2[i].add(new Field("publishid", docs2[i].publishid, StringField.TYPE_STORED));
            document2[i].add(new Field("subjectid", docs2[i].subjectid, StringField.TYPE_STORED));
            document2[i].add(new Field("content", docs2[i].content, TextField.TYPE_STORED));
            indexWriter.addDocument(document2[i]);
        }
        for (int i = 0; i < docsNumber3; i++) {
            document3[i] = new Document();
            document3[i].add(new Field("url", docs3[i].url, StringField.TYPE_STORED));
            document3[i].add(new Field("description", docs3[i].description, StringField.TYPE_STORED));
            document3[i].add(new Field("keywords", docs3[i].keyword, StringField.TYPE_STORED));
            document3[i].add(new Field("title", docs3[i].title, StringField.TYPE_STORED));
            document3[i].add(new Field("publishid", docs3[i].publishid, StringField.TYPE_STORED));
            document3[i].add(new Field("subjectid", docs3[i].subjectid, StringField.TYPE_STORED));
            document3[i].add(new Field("content", docs3[i].content, TextField.TYPE_STORED));
            indexWriter.addDocument(document3[i]);
        }
        indexWriter.close();
    }
}
