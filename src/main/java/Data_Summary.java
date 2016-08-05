import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import java.util.ArrayList;

public class Data_Summary extends SummaryStatistics {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ArrayList<Double> values = new ArrayList<Double>();
	private averageAtN aatn = new averageAtN();  
	
	
	public void addValue(double value){
		super.addValue(value);
		aatn.increment(value);
		this.values.add(value);
	}
	
	public double getAverageAtN(int N){
		return this.aatn.getResultAtN(N);
	}
	
	public double[] getValues(){
		double[] res = new double[this.values.size()];
		for(int i = 0; i < this.values.size(); i++)
			res[i] = this.values.get(i);
		return res;
	}
}
