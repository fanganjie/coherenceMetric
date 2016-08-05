import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.stat.descriptive.AbstractStorelessUnivariateStatistic;
import org.apache.commons.math3.stat.descriptive.StorelessUnivariateStatistic;
import org.apache.commons.math3.stat.descriptive.WeightedEvaluation;
import org.apache.commons.math3.stat.descriptive.moment.Mean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;


public class averageAtN extends AbstractStorelessUnivariateStatistic
implements Serializable, WeightedEvaluation{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ArrayList<Double> data = new ArrayList<Double>();
	
	

	public long getN() {
		// TODO Auto-generated method stub
		return this.data.size();
	}


	public double evaluate(double[] values, double[] weights) throws MathIllegalArgumentException {
		// TODO Auto-generated method stub
		return 0;
	}


	public double evaluate(double[] values, double[] weights, int begin, int length)
			throws MathIllegalArgumentException {
		// TODO Auto-generated method stub
		return 0;
	}


	public StorelessUnivariateStatistic copy() {
		// TODO Auto-generated method stub
		return null;
	}


	public void clear() {
		// TODO Auto-generated method stub
		
	}


	public double getResult() {
		// TODO Auto-generated method stub
		return this.getResultAtN(this.data.size());
	}
	
	public double getResultAtN(int N) {
		// TODO Auto-generated method stub
		
		N = (N > this.data.size()) ? this.data.size() : N;
		
		Collections.sort(this.data, Collections.reverseOrder());
		double[] tmp = new double[N];
		
		int count = 0;
		for(double d: this.data){
			tmp[count++] = d;
			if(count == N)
				break;
		}
		
		Mean mean = new Mean();
		return mean.evaluate(tmp);
	}

	@Override
	public void increment(double d) {
		// TODO Auto-generated method stub
		this.data.add(d);
	}
	
	public static void main(String[] args) {
		averageAtN aatn = new averageAtN();
		
		aatn.increment(2);
		aatn.increment(8);
		aatn.increment(5);
		aatn.increment(7);
		aatn.increment(1);
		
		System.out.println(aatn.getResultAtN(3));
	}
}