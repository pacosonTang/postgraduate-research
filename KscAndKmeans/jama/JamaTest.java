package com.research.jama;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;

import com.research.Alg.KscAlg;
import com.research.pojo.ClusterData;

public class JamaTest
{
	public static void main(String[] args)
	{
		KscAlg alg;
		
		double[][] A = {
				{3, -1},
				{-1, 3},				
		};
		double[][] B = {
				{-1},
				{1},				
		};
		//alg = new KscAlg();
		
		//System.out.println(alg.eigenvalueOfEigenvector(A, B));
		computeSmallestEigenvectorOfM(0, A);
		
	}
	
	public static void computeSmallestEigenvectorOfM(int clusterId, double[][] data) 
	{ 
		double[] eigenvalues;
		double temp;
		double miniEigenvalues;
		int miniIndex;
		Matrix matrix2d;
		EigenvalueDecomposition ed;
		
		matrix2d = new Matrix(data, data.length, data.length);
		ed = new EigenvalueDecomposition(matrix2d);
		
		
		//miniEigenvalues == smallest eigenValue 
		miniEigenvalues = ed.getD().get(0, 0);
		miniIndex = 0;
		for (int i = 1; i < data.length; i++) 
		{
			if (miniEigenvalues > ed.getD().get(i, i))
			{
				miniEigenvalues = ed.getD().get(i, i);
				miniIndex = i;
			}
		}
		
		// update centroid 
		data[clusterId] = ed.getV().getArrayCopy()[miniIndex];
	}
}
