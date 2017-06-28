import java.io.File
import scala.io.Source
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.mllib.feature.{Word2Vec, Word2VecModel}

/**
  * Created by Mayanka on 19-06-2017.
  */
object W2V {
  def main(args: Array[String]): Unit = {

    System.setProperty("hadoop.home.dir", "C:\\winutils")

    val sparkConf = new SparkConf().setAppName("SparkWordCount").setMaster("local[*]")
      .set("spark.driver.memory", "6g").set("spark.executor.memory", "6g")

    val sc = new SparkContext(sparkConf)
    //val tfid : TF_IDF=new TF_IDF;
    val input = sc.textFile("data/Article.txt").map(line => line.split(" ").toSeq)
    val words = "data/topwords.txt"
    val modelFolder = new File("myModelPath")

    if (modelFolder.exists()) {
      val sameModel = Word2VecModel.load(sc, "myModelPath")
      for (line <- Source.fromFile(words).getLines) {

        val wr = sameModel.findSynonyms(line, 40)
        for ((synonym, cosineSimilarity) <- wr) {
          println(s"$synonym $cosineSimilarity")
        }
      }

    }

    else {
      val word2vec = new Word2Vec().setVectorSize(1000)

      val model = word2vec.fit(input)
      //words.foreach{w}

    for(line<- Source.fromFile(words).getLines){
      val in=model.findSynonyms(line,40)
      for ((synonym, cosineSimilarity) <- in) {
        println(s"$synonym $cosineSimilarity")
      }
      model.getVectors.foreach(f => println(f._1 + ":" + f._2.length))

      // Save and load model
      model.save(sc, "myModelPath")

    }

  }

}
}