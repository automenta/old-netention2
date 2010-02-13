/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.nlp.en;

import automenta.netention.edge.ReffedBy;
import automenta.netention.edge.Refs;
import automenta.netention.edge.Next;
import automenta.netention.edge.Previous;
import automenta.netention.node.Concept;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.uci.ics.jung.graph.DirectedGraph;
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
    boolean bidirectional = true;
    boolean sequenceLinks = true;
    boolean bidirectionalSequence = true;

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

    public void tag(Object m, String text, DirectedGraph pg) throws Exception {
        List<TaggedWord> twl = getWords(text);

        Concept prevConcept = null;
        for (TaggedWord tw : twl) {
            String w = tw.word();
            String pos = tw.tag();

            if (!w.startsWith("#")) {
                if (!w.startsWith("@")) {
                    if (w.length() > 2) {
                        if (isValidPOS(pos)) {
                            String psw = PorterStemming.stem(w);
                            Concept c = new Concept(psw);

                            //System.out.println(pos + " : " + c + " nodes=" + pg.getVertexCount() + " edges=" + pg.getEdgeCount());
                            
                            pg.addVertex(c);

                            if (sequenceLinks) {
                                if (prevConcept!=null) {
                                    pg.addEdge(new Next(), prevConcept, c);
                                    if (bidirectionalSequence) {
                                        pg.addEdge(new Previous(), c, prevConcept);
                                    }
                                }
                            }

                            pg.addEdge(new Refs(), m, c);
                            if (bidirectional) {
                                pg.addEdge(new ReffedBy(), c, m);
                            }

                            prevConcept = c;
                        }
                    }
                }
            }

        }

    }

        public boolean isValidPOS(String tag) {
        if (tag.equals("NN")) {
            return true;
        }
        if (tag.equals("NNS")) {
            return true;
        }
        if (tag.equals("NNP")) {
            return true;
        }
        if (tag.equals("VB")) {
            return true;
        }
        if (tag.equals("VBG")) {
            return true;
        }
        return false;
    }


}
