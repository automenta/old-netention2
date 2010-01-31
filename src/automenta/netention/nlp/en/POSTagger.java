/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.nlp.en;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author seh
 */
public class POSTagger {
    //slow and accurate
    //String modelFile = "./lib/stanford-pos/models/bidirectional-distsim-wsj-0-18.tagger";
    
    //faster
    String modelFile = "./lib/stanford-pos/models/left3words-wsj-0-18.tagger";

    private final MaxentTagger tagger;

    public POSTagger() throws Exception {
        super();
        //String modelFile = new File(".").getAbsolutePath();

        tagger = new MaxentTagger(modelFile);

    }

    public List<TaggedWord> getWords(String text) throws Exception {
        List<TaggedWord> words = new LinkedList();

        List<Sentence<? extends HasWord>> sentences = tagger.tokenizeText(new StringReader(text));
        for (Sentence<? extends HasWord> sentence : sentences) {
            Sentence<TaggedWord> tSentence = tagger.tagSentence(sentence);
            for (int i = 0; i < tSentence.length(); i++) {
                TaggedWord tw = tSentence.get(i);
                words.add(tw);
                //System.out.println(PorterStemming.stem(tw.word()) + " " + tw.word() + " " + tw.tag());
            }
        }
        
        return words;
    }
    
}
