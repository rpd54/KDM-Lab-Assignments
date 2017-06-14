
import edu.stanford.nlp.hcoref.CorefCoreAnnotations;
import edu.stanford.nlp.hcoref.data.CorefChain;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;

import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String args[]) throws FileNotFoundException {
        {
            Properties props = new Properties();
            props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
            StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
            Scanner scan = new Scanner( new File("C:/Users/user/Desktop/bbcsport.txt") );
            String data1 = scan.useDelimiter("\\A").next();
            scan.close(); // Put this call in a finally block
            Annotation document = new Annotation(data1);

            pipeline.annotate(document);

            List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);

            for (CoreMap sentence : sentences) {
                // traversing the words in the current sentence
                // a CoreLabel is a CoreMap with additional token-specific methods
                for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {

                    System.out.println("\n" + token);

                    // this is the text of the token
                    String word = token.get(CoreAnnotations.TextAnnotation.class);
                    System.out.println("Text Annotation");
                    System.out.println(token + ":" + word);
                    // this is the POS tag of the token

                    String lemma = token.get(CoreAnnotations.LemmaAnnotation.class);
                    System.out.println("Lemma Annotation");
                    System.out.println(token + ":" + lemma);
                    // this is the Lemmatized tag of the token


                    String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                    System.out.println("POS");
                    System.out.println(token + ":" + pos);

                    // this is the NER label of the token
                    String ne = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
                    System.out.println("NER");
                    System.out.println(token + ":" + ne);

                    System.out.println("\n\n");
                }

                Tree tree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
                System.out.println(tree);
                SemanticGraph dependencies = sentence.get(SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation.class);
                System.out.println(dependencies.toString());

                Map<Integer, CorefChain> graph =
                        document.get(CorefCoreAnnotations.CorefChainAnnotation.class);
                System.out.println(graph.values().toString());
            }

        }
    }
}
