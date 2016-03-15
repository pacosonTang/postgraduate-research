package com.research.timeseries;

public class CopyTest {
	public static void main(String[] args) {
		double[] dim1 = new double[5];
		double[] dim1_copy = new double[5];
		
		dim1[0] = 9;
		System.arraycopy(dim1, 0, dim1_copy, 0, 5);
		dim1_copy[0] = -1;
		
		System.out.println(dim1[0] + "," + dim1_copy[0]);
	}
}
