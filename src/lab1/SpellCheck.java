package lab1;

/**
 * Created by xymeow on 16/01/15.
 */

import java.io.IOException;

import java.nio.file.Paths;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.spell.PlainTextDictionary;
import org.apache.lucene.search.spell.SpellChecker;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

public class SpellCheck {
    private static String dicpath = "dictionary.dic";
    private Directory directory = new RAMDirectory();
    /**
     * 拼写检查器
     */
    private SpellChecker spellchecker;
    public String search(String word) {
        directory = new RAMDirectory();
        String correct = null;
        try {
            IndexWriterConfig config = new IndexWriterConfig(new IKAnalyzer());
            spellchecker = new SpellChecker(directory);
            spellchecker.indexDictionary(new PlainTextDictionary(Paths.get(dicpath)), config, true);
            String[] suggests = spellchecker.suggestSimilar(word, 1);
            if (suggests != null) {
                for(String sug:suggests){
                    if(sug != word)
                        correct = sug;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return correct;
    }
}

