package com.research.tools9;

import java.math.BigDecimal;
import java.math.RoundingMode;

// compute the numeric integration.
public class Integration {
	
	public Integration(){}
	
	// apply simpson rule to approximately compute the integration. 
	public double simpsonRule(double upper, double lower, int n, Function df) {
		double result = 0;
		
		double unit = (upper-lower)/n;
		double factor1 = unit / 3;
		double[] x = new double[n+1];
		
		for (int i = 0; i < x.length; i++) {
			x[i] = lower + unit*i;
		}
		for (int i = 0; i < x.length; i++) {
			if(i==0 || i==x.length-1) {
				result += df.fun(x[i]);
			}else if(i%2 == 0) { // if i is even num.
				result += 2*df.fun(x[i]);
			}else { // if i is odd num.  
				result += 4*df.fun(x[i]);
			}
		}				
		
		result *= factor1;
		return result;
	}
	// compute the standard normal distribution integration
	// refer to the integration table in p382 of "probability and statistics" from ZheJiang University.
	public double stdGaussValue(double realUpper) {
		Integration integration = new Integration();
		double upper = 1.0;
		double lower = 0.0;
		int n = 50;
		// double realUpper = 0.03;
		
		double result = 
				integration.simpsonRule(upper, lower, n, new Function() {
					@Override
					public double fun(double x) {
						if(x==0) {
							return 0;
						}
						double t =  realUpper-(1-x)/x;
						return Math.pow(Math.E, -0.5*t*t) / (x*x);			
					}
				});
		result /= Math.pow(2*Math.PI, 0.5);
		result = new BigDecimal(result).
				setScale(4, RoundingMode.HALF_UP).doubleValue();
		return result;
	}
}