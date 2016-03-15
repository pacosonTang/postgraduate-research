package com.research.timeseries;

public class DTWTest {
	
	public static void main(String[] args) {
		// 长序列作为列，短序列作为行
		double[] long_seq = {1, 1, 1, 10, 2, 3};
		double[] short_seq = {1, 1, 1, 2, 10, 3};
		DTW dtw = new DTW();
		
		dtw.DTW(short_seq, long_seq);
	}
}