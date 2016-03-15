package com.research.timeseries;

public class DTW {

	public void DTW(double[] x, double[] y) {
		int xLen = x.length+1; // x == short array == row
		int yLen = y.length+1; // y == long array == column
		double[][] distance = null;
		double[][] result = new double[xLen][yLen];
		
		distance = init_distance(x, y);
		
		// init the result array from distance array.
		for (int i = 0; i < xLen; i++) {
			System.arraycopy(distance[i], 0, result[i], 0, yLen);
		}
		
		// compute DTW distance.
		for (int i = 1; i < xLen; i++) {
			for (int j = 1; j < yLen; j++) {
				result[i][j] = min(min(result[i-1][j], result[i][j-1]),result[i-1][j-1]) + distance[i][j];
			}
		}
		System.out.println("\n\n===±¾Éí´æ´¢DTW¾àÀëµÄÁÚ½Ó¾ØÕó===");
		printArray(result);
		
		// find the optimal path
	}
	
	// init the distance array between the given array x and y. 
	public double[][] init_distance(double[] x, double[] y){
		int xLen = x.length+1;
		int yLen = y.length+1;
		double[][] distance = new double[xLen][yLen];
		
		// compute the distance between points
		for (int i = 1; i < xLen; i++) {			
			for (int j = 1; j < yLen; j++) {				
				distance[i][j] = distance_f(x[i-1], y[j-1]);
			} 
		}
		for (int i = 1; i < xLen; i++) {
			distance[i][0] = Double.MAX_VALUE;
		}
		for (int i = 1; i < yLen; i++) {
			distance[0][i] = Double.MAX_VALUE;
		}
		distance[0][0] = 0;
		// compute over.
		System.out.println("===±¾Éí´æ´¢¾àÀëµÄÁÚ½Ó¾ØÕó===");
		printArray(distance);
		
		return distance;
	}
	
	// distance function.
	public double distance_f(double one, double two) {
		return Math.abs(one - two);
	}
	
	// print the given array .
	public void printArray(double[][] array){
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[i].length; j++) {
				if(array[i][j] == Double.MAX_VALUE){
					System.out.printf("%5s", "¡Þ");
					continue;
				}
				System.out.printf("%5.0f", array[i][j]);
			}
			System.out.println();
		}
	}
	
	 //conpute the minimal between x and y .
	public double min(double x, double y){
		return x > y ? y : x;
	}
}

