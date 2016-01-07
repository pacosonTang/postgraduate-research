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
	public abstract double computeSilhouette(int[][] items);
	
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
	  
