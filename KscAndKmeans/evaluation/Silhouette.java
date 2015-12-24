package com.research.evaluation;

public abstract class Silhouette
{
	public double[][] sameClusterDistance;
	public double[][] diffClusterDistance;
	
	public Silhouette(){}

	/**
	 * compute silhouette which is the evaluation about cluster effects
	 * @param items an array for computing silhouette
	 */
	public abstract double computeSilhouette(double[][][] items);
	
	 /**
	  * compute distance of one sample to another sample
	  * @param one a vector
	  * @param another a vector
	  * @return euclidean distance between one and another
	  */
	 public abstract double distanceOfSampleToSample(double[] one, double[] another);
	 
	 /**
	  * return the maximum between a and b
	  * @param a 
	  * @param b
	  * @return maximum
	  */
	 public double maximum(double a, double b)
	 {
		 return a > b ? a : b;		 
	 }
}
	  
