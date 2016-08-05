import net.sf.snowball.ext.PorterStemmer;
import org.gla.terrier.csvLib;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class SM_LSA implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public String model_name;
    public HashMap<String, double[]> matrix;
    String split;

    public SM_LSA() {

    }

    public SM_LSA(String filename1, String filename2, String split) {
        csvLib csvlib = new csvLib();
        List<String> matrix_val = csvlib.readFileLineByLine(filename1);
        List<String> terms = csvlib.readFileLineByLine(filename2);
        this.split = split;
        matrix = new HashMap<String, double[]>();

        if (matrix_val.size() != terms.size())
            throw new IllegalStateException("Error in Size " + this.getClass().getName());

        for (int i = 0; i < terms.size(); i++)
            this.matrix.put(terms.get(i).trim().toLowerCase(), getVal(matrix_val.get(i)));
    }

    public SM_LSA(String filename1, String split) {
        csvLib csvlib = new csvLib();
        List<String> matrix_val = csvlib.readFileLineByLine(filename1);
        this.split = split;
        matrix = new HashMap<String, double[]>();
        for (int i = 0; i < matrix_val.size(); i++) {
            String line = matrix_val.get(i);
            String term = line.split(this.split)[0];
            this.matrix.put(term.trim().toLowerCase(), getVal(line, 1));
        }
    }

    public String getStemmedString(String s) {
        String res = "";
        PorterStemmer ps = new PorterStemmer();
        ps.setCurrent(s);
        ps.stem();
        res = ps.getCurrent();
        return res;
    }

    public double[] getWordVec(String s) {
        return this.matrix.get(s);
    }

    public double[] getVal(String s) {
        String[] tmp = s.split(this.split);
        double[] res = new double[tmp.length];
        for (int i = 0; i < tmp.length; i++)
            res[i] = Double.parseDouble(tmp[i]);
        return res;
    }

    public double[] getVal(String s, int from) {
        String[] tmp = s.split(this.split);
        double[] res = new double[tmp.length - from];
        for (int i = from; i < tmp.length; i++)
            res[i - from] = Double.parseDouble(tmp[i]);
        return res;
    }


    public double getSimilarity(String term1, String term2) {
        double res = 0;

        if (!this.matrix.containsKey(term1.toLowerCase().trim()) || !this.matrix.containsKey(term2.toLowerCase().trim()))
            return res;
        else {
            double tmp = this.returnConsine(matrix.get(term1.toLowerCase().trim()), matrix.get(term2.toLowerCase().trim()));
            if (tmp < 0)
                return res;
            else
                return tmp;
        }

    }

    public double getSimilarityStem(String term1, String term2) {
        double res = 0;


        if (!this.matrix.containsKey(getStemmedString(term1.toLowerCase().trim())) || !this.matrix.containsKey(getStemmedString(term2.toLowerCase().trim())))
            return res;
        else {
            double tmp = this.returnConsine(matrix.get(getStemmedString(term1.toLowerCase().trim())), matrix.get(getStemmedString(term2.toLowerCase().trim())));

            if (tmp < 0)
                return res;
            else
                return tmp;
        }

    }

    public boolean isExisted(String s) {
        if (this.matrix.containsKey(s))
            return true;
        else
            return false;
    }

    public double returnEcludiean(double[] a1, double[] a2) {
        double sum = 0;
        for (int i = 0; i < a1.length; i++) {
            sum += Math.pow((a1[i] - a2[i]), 2);
        }
        return Math.sqrt(sum);
    }

    public double returnConsine(double[] a1, double[] a2) {
        double sum = 0;
        double a1_d = 0;
        double a2_d = 0;
        for (int i = 0; i < a1.length; i++) {
            sum += a1[i] * a2[i];
            a1_d += Math.pow(a1[i], 2);
            a2_d += Math.pow(a2[i], 2);
        }
        return sum / (Math.sqrt(a1_d) * Math.sqrt(a2_d));
    }

    public double LSA_score(String t1, String t2) {
        return LSA_score(t1.split(" "), t2.split(" "));
    }

    public double LSA_score(String[] t1, String[] t2) {
        double res = 0;
        int count = 0;

        for (int i = 0; i < t1.length; i++) {
            for (int j = 0; j < t2.length; j++) {
                res += getSimilarity(t1[i], t2[j]);
                count++;
            }
        }
        return res / (double) count;
    }

    public double LSA_score(String[] t1, String[] t2, double[] v1, double[] v2) {
        double res = 0;
        int count = 0;

        for (int i = 0; i < t1.length; i++) {
            for (int j = 0; j < t2.length; j++) {
                res += getSimilarity(t1[i], t2[j]) * (v1[i] + v2[j]);
                count++;
            }
        }
        return res / (double) (count);
    }


    public static void main(String[] args) {
        String file_model;
        String file_voc;
        String output;
        String split;


        SM_LSA sl = null;

        file_model = args[0];
        file_voc = args[1];
        output = args[2];
        System.out.println(output);
        split = args[3];
        sl = new SM_LSA(file_model, file_voc, split);


        try {
            ObjectOutputStream oos =
                    new ObjectOutputStream(new FileOutputStream(output));
            oos.writeObject(sl);
            oos.close();

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }

}
