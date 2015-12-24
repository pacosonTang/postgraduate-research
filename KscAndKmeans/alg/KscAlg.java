package com.research.Alg;


import java.io.IOException;
import java.util.Arrays;
import Jama.EigenvalueDecomposition;
import Jama.Matrix;
import com.research.evaluation.TimeSeriesSilhouette;
import com.research.pojo.ClusterData;
import com.research.pojo.ClusterParam;

/**
 * @author lenovo
 * conducting cluster based on ksc alg
 */

public class KscAlg extends ClusterAlg{
	
	private double[] itemTag;
	private double[][][] itemsMatrixM; //matrix M corresponding every item
	private double[][][] centroidMatrixM;// matrix M correspondig every cluster
	
	public KscAlg()
	{
		super(new TimeSeriesSilhouette(), "ksc");
		itemsMatrixM = new double[ClusterData.rowno][ClusterData.dimension][ClusterData.dimension];
		centroidMatrixM = new double[ClusterParam.cluster][ClusterData.dimension][ClusterData.dimension];
	}

	@Override
	public void clusterAlg() throws IOException
	{
		double[][] silhouette;
		
		silhouette = new double[1][ClusterParam.clusterExeNum];
		
		randomRefineInitialCentroid(); // refine the init centroid(update the centroid)	
		Arrays.fill(ClusterData.clusterMemberNum, 0); // reset clusterMemberNum array zeros
		
		// compute matrix M corresponding every item
		for (int i = 0; i < ClusterData.rowno; i++) 
			this.itemsMatrixM[i] = computeItemMatrixM(ClusterData.items[i]);
		
		//assign the centroid for every item
		for (int i = 0; i < ClusterData.rowno; i++) 
			ClusterData.items[i][ClusterData.dimension] = assign(i);
		gatherClusterResults();
		silhouette[0][0] = super.silhouette.computeSilhouette(tempClusterResults); //compute silhouette of clusters
		
		//1.update centroids(refinement step)
		//2.assign a centorid for every item(assignment step)
		for (int i = 2; i <= ClusterParam.clusterExeNum; i++)  
			silhouette[0][i-1] = refineAndAssign(i);
		
		// finally write the silhouette into xlsx
		super.writeSilhouette.writeSilhouette(silhouette, ClusterAlg.algName + ClusterParam.cluster +  "_silhouette.xlsx", "ClusterExeOrdinal");
	}
	
	/**
	 *  update init centroid
	 */
	public void refine() 
	{
		int clusterId;		
		
		resetArrayToZeros(ClusterData.centroid);// reset centroid array zeros 
		
		for (int i = 0; i < ClusterData.rowno; i++)  
		{
			clusterId = (int)ClusterData.items[i][ClusterData.dimension]; 
			computeCentroidMatrixM(i, clusterId); 
		}// compute every centroid matrix M over 
		 
		// compute the smallest vigenvector of every centorid matrix M ,who is treated as the centroid
		for (int i = 0; i < ClusterParam.cluster; i++)  			
			computeSmallestEigenvectorOfM(i, centroidMatrixM[i]);
	}
	
	/**
	 * compute the centroid the item should be assigned to 
	 * @param index refers to item index
	 * @return cluster id whose has the smallest distance between centroid and the item
	 */
	public int assign(int index)
	{
		double[][] tempCentroid;
		int miniIndex;
		double miniEigenvalue;
		double temp;
		
		miniIndex = 0;
		tempCentroid = new double[1][]; // packing data(centroid) into tempCentroid
		
		tempCentroid[0] = ClusterData.centroid[0];
		miniEigenvalue = eigenvalueOfEigenvector(itemsMatrixM[index], tempCentroid);
		
		//compute the eigenvalue ¦Ë respect to Ax = ¦Ëx ,whose A is matrix itemMatrixM and x is centroid ¦Ì 
		for (int i = 1; i < ClusterParam.cluster; i++)
		{
			tempCentroid[0] = ClusterData.centroid[i];
			temp = eigenvalueOfEigenvector(itemsMatrixM[index], tempCentroid);
			if(miniEigenvalue > temp)
			{
				miniEigenvalue = temp;
				miniIndex = i;
			}
		}
				
		ClusterData.clusterMemberNum[miniIndex]++;
		
		return miniIndex;
	}
	
	/**
	 * compute matrix M corresponding every item
	 * @param index every item has an index for identity
	 * @param data content of an item under index 
	 */
	//public void computeItemMatrixM(int index, double[] data) // checked
	public double[][] computeItemMatrixM(double[] data) // checked
	{
		double[][] temp;
		double level2NormSquare;
		
		// 1st step:compute matrix M,M=(I - xixiT/||xi||^2)
		level2NormSquare = -l2normSquare(data); //1.1 compute ||xi||^2;
		temp = new double[ClusterData.dimension][ClusterData.dimension];
		
		for (int i = 0; i < ClusterData.dimension; i++)
		{
			for (int j = 0; j < ClusterData.dimension; j++)
			{
				temp[i][j] = data[i] * data[j];
				temp[i][j] /= level2NormSquare;
				if(i == j)
					temp[i][j] += 1;
			}
		}
		//this.itemsMatrixM[index] = temp;
		return temp;
	}
	
	/**
	 * compute matrix M corresponding every item
	 * @param index every item has an index for identity
	 * @param data content of an item under index 
	 */
	public void computeCentroidMatrixM(int itemId, int clusterId)
	{
		Matrix A;
		Matrix B;
		
		A = new Matrix(centroidMatrixM[clusterId], ClusterData.dimension,ClusterData.dimension);
		B = new Matrix(itemsMatrixM[itemId], ClusterData.dimension,ClusterData.dimension);
		
		A.plusEquals(B);
		centroidMatrixM[clusterId] = A.getArrayCopy();
	}
	
	/**
	 * compute the eigenvalue corresponding to the eigenvector
	 * @param initMatrix left matrix
	 * @param eigenvector right matrix
	 * @return eigenvalue ¦Ë from initMatrix*eigenvector = ¦Ë*eigenvector 
	 */
	public double eigenvalueOfEigenvector(double[][] initMatrix, double[][] eigenvector) // checked
	{
		Matrix matrix;
		Matrix vector;
		double eigenvalue;
		
		matrix = new Matrix(initMatrix, ClusterData.dimension, ClusterData.dimension);
		vector = new Matrix(eigenvector, 1, ClusterData.dimension);
		vector = vector.transpose(); 
		//vector = new Matrix(eigenvector, eigenvector.length, 1);
		
		vector = matrix.times(vector);
		eigenvalue = vector.get(0, 0) / eigenvector[0][0];
		
		return Math.abs(eigenvalue);
	}
	
	/**
	 * compute the smallest eigenvector of matrix M under index
	 * @param clusterId cluster index 
	 */
	public void computeSmallestEigenvectorOfM(int clusterId, double[][] data) //checked
	{ 
		double temp;
		double miniEigenvalues;
		int miniIndex;
		Matrix matrix2d;
		EigenvalueDecomposition ed;
		double normalization;
		
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
		ClusterData.centroid[clusterId] = ed.getV().getArrayCopy()[miniIndex];
		
		// normalization for vectors ClusterData.centroid
		normalization = ClusterData.centroid[clusterId][0];
		for (int i = 0; i < ClusterData.centroid[clusterId].length; i++)
		{
			ClusterData.centroid[clusterId][i] /= normalization;
		} // normalization over
	}
	
	/**
	 * compute the level 2 norm of given array data
	 * @param data an array
	 * @return norm 
	 */
	public double l2normSquare(double[] data)
	{
		double result = 0;
		
		for (int i = 0; i < ClusterData.dimension; i++)  
			result += Math.pow(data[i], 2.0); 
		return result;
	}
}