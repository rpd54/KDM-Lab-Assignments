import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.lang.String;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;


public class test {

    protected StanfordCoreNLP pipeline;
    public test() {
                Properties props;
        props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");


        this.pipeline = new StanfordCoreNLP(props);
    }

    public String lemmatize(String documentText)
    {
        List<String> lemmas = new LinkedList<String>();
        List<String> wo = new LinkedList<String>();
        List<String> POS = new LinkedList<String>();
        List<String> NER = new LinkedList<String>();

        // Create an empty Annotation just with the given text
        Annotation document = new Annotation(documentText);
        // run all Annotators on this text
        this.pipeline.annotate(document);
        // Iterate over all of the sentences found
        List<CoreMap> sentences = document.get(SentencesAnnotation.class);
        for(CoreMap sentence: sentences) {
            // Iterate over all tokens in a sentence
            for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
                // Retrieve and add the lemma for each word into the
                // list of lemmas
                lemmas.add(token.get(LemmaAnnotation.class));
                wo.add(token.get(CoreAnnotations.TextAnnotation.class));
                POS.add(token.get(CoreAnnotations.PartOfSpeechAnnotation.class));
                NER.add(token.get(CoreAnnotations.NamedEntityTagAnnotation.class));
            }
        }
        System.out.println(wo);
        System.out.println(POS);
        System.out.println(NER);
        StringBuilder sb = new StringBuilder();
        for (String s : lemmas)
        {
            sb.append(s);
            sb.append(" ");
        }

        //System.out.println(sb.toString());
        //String s = sb.toString();
        return sb.toString();
    }
    public String ner(String documentText)
    {
        List<String> wo = new LinkedList<String>();
        List<String> POS = new LinkedList<String>();
        List<String> NER = new LinkedList<String>();

        // Create an empty Annotation just with the given text
        Annotation document = new Annotation(documentText);
        // run all Annotators on this text
        this.pipeline.annotate(document);
        // Iterate over all of the sentences found
        List<CoreMap> sentences = document.get(SentencesAnnotation.class);
        for(CoreMap sentence: sentences) {
            // Iterate over all tokens in a sentence
            for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
                // Retrieve and add the lemma for each word into the
                // list of lemmas
                wo.add(token.get(CoreAnnotations.TextAnnotation.class));
                POS.add(token.get(CoreAnnotations.PartOfSpeechAnnotation.class));
                NER.add(token.get(CoreAnnotations.NamedEntityTagAnnotation.class));
            }
        }

        System.out.println(wo);
        System.out.println(POS);
        System.out.println(NER);

        StringBuilder sb = new StringBuilder();
        for (String s : NER)
        {
            sb.append(s);
            sb.append(" ");
        }

        //System.out.println(sb.toString());
        //String s = sb.toString();
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println("Starting Stanford Lemmatizer");
        String text = "How answering you be seeing into my eyes like open doors? \n";

    }

}