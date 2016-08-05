import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.List;

import org.gla.terrier.csvLib;

/**
 * Created by anjiefang on 05/08/2016.
 */
public class metric {

    private SM_LSA smodel = null;
    private Boolean isStemmed = true;

    public void setStemmed(Boolean stemmed) {
        isStemmed = stemmed;
    }

    private  void loadingSmodel(String metric_filename){
        try {
            System.out.println("Reading Twitter Word Embedding ...");
            ObjectInputStream ois;
            ois = new ObjectInputStream (new BufferedInputStream(new FileInputStream(metric_filename)));
            this.smodel = (SM_LSA) ois.readObject();
            ois.close();
            System.out.println("Twitter Word Embedding loaded!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new IllegalArgumentException ("Read scource failed!");
        }
    }

    private double returnCoherenceScore(String[] words){
        int count = 0;
        if(words.length < 10){
            System.out.println("Size Error!");
        }
            //throw new IllegalStateException("Topic Size Wrong!");
        double sum = 0;
        for(int i = 0; i < words.length; i++){
            String A = words[i];
            for(int j = i + 1; j < words.length; j++){
                String B = words[j];
                if(!this.isStemmed)
                    sum += this.smodel.getSimilarity(A, B);
                else
                    sum += this.smodel.getSimilarityStem(A, B);
                count++;
            }
        }
        return sum/(double)count;
    }

    private double returnCoherenceScore(String sentence) {
        String[] words = sentence.split(" ");
        return  this.returnCoherenceScore(words);
    }

    public metric(String metric_filename) {
        loadingSmodel(metric_filename);
    }

    private void evaPerModel(String filename, csvLib csvlib, String des){
        List<String> data = csvlib.readFileLineByLine(filename);

        Data_Summary scores = new Data_Summary();

        for(String line: data){
            String text = line.split("\t")[1];
            scores.addValue(this.returnCoherenceScore(text));
        }

        String string_line = new File(filename).getName() + "," + scores.getMin() + "," +
                scores.getMax() + "," + scores.getMax() + "," + scores.getAverageAtN(3) + "," + scores.getAverageAtN(5);

        csvlib.writeLine(des, string_line);

    }

    public static void main(String[] args) {
        csvLib csvlib = new csvLib();

//      input folder
        String folder = args[1];

//      the output res path (full)
        String des = args[2];

//      the path of twitter.we
        metric m = new metric(args[0]);
        m.setStemmed(true);

        for(File file: new File(folder).listFiles()){
            if(file.isFile()) continue;
            for(File subFile: file.listFiles()){
                if(subFile.isDirectory()) continue;
                System.out.println("Processing :" + subFile.getPath());
                m.evaPerModel(subFile.getPath(), csvlib, des);
            }
        }

        System.out.println("Results saved in: " + des);
        csvlib.closeWriters();

    }
}
