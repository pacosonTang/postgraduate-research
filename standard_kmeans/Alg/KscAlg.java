package com.research.Alg;


import java.io.IOException;
import java.util.Arrays;
import Jama.EigenvalueDecomposition;
import Jama.Matrix;
import com.research.evaluation.TimeSeriesSilhouette;
import com.research.pojo.ClusterData;
import com.research.pojo.ClusterParam;
import static java.lang.System.*;
import static java.lang.Math.*;

/**
 * @author lenovo
 * conducting cluster based on ksc alg
 */

public class KscAlg extends ClusterAlg{
	
	private double[] itemTag;
	public static double[][][] itemsMatrixM; //matrix M corresponding every shifted item
	public static double[][][] centroidMatrixM;// matrix M correspondig every cluster
	public static double[][] shiftedItems;
	private static double[] shiftedY;
	
	public KscAlg()
	{
		super(new TimeSeriesSilhouette(), "ksc");
		itemsMatrixM = new double[ClusterData.rowno][ClusterData.dimension][ClusterData.dimension];
		centroidMatrixM = new double[ClusterParam.clusterNum][ClusterData.dimension][ClusterData.dimension];
		shiftedItems = new double[ClusterData.rowno][ClusterData.dimension];
	}

	/**
	 * cluster alg: first update centroids,and then assign a centroid for every item 
	 * @param ith  cluster executing order
	 * @throws IOException 
	 */
	@Override
	public double refineAndAssign(int ith) throws IOException
	{		
		double silhouette;
		
		//1.update centroids (refinement-process for updating centroids)
		refine();
		
		//2.assign a centroid for every item
		Arrays.fill(ClusterData.clusterMemberNum, 0); // reset clusterMemberNum array zeros
		for (int i = 0; i < ClusterData.rowno; i++) 
		{
			ClusterData.rownoWithClusterid[i] = assign(i);
		}
		
		//computing silhouette begins
		gatherClusterResults(ith);
		silhouette = this.silhouette.computeSilhouette(irregularRownoWithClusterid); 
		// computing silhouette over
		
		return silhouette;
	}
	
	/**
	 *  update init centroid
	 *  there is a problem updating centroids 
	 */
	@Override
	public void refine() 
	{
		int clusterId;		
		
		// reset centroid array zeros
		resetArrayToZeros(ClusterData.centroid); 
		
		// compute the smallest vigenvector of every centorid matrix M,
		// who is treated as the centroid
		for (int i = 0; i < ClusterParam.clusterNum; i++)  		
		{
			ClusterData.centroid[i] = computeSmallestEigenVector(centroidMatrixM[i]);
			resetArrayToZeros(centroidMatrixM[i]); // attention for let array centroidMatrixM[i] be zero 
		}
	}
	
	/**
	 * compute the centroid of the item  
	 * @param index refers to item index
	 * @return cluster id who has the smallest distance between centroid and the item
	 */
	public int assign(int index)
	{
		int miniIndex;
		double miniDistanceItemToCentroid;
		//double euclideanDistance;
		
		miniIndex = 0;
		miniDistanceItemToCentroid = distanceXAndY(ClusterData.centroid[miniIndex], ClusterData.items[index]);
		//euclideanDistance = euclideanDistance(ClusterData.centroid[miniIndex], ClusterData.items[index]);
		
		shiftedItems[index] = shiftedY;
		
		for (int i = 1; i < ClusterParam.clusterNum; i++)
		{
			double tempDistance =  distanceXAndY(ClusterData.centroid[i], ClusterData.items[index]);
			//euclideanDistance = euclideanDistance(ClusterData.centroid[i], ClusterData.items[index]);
			
			 if(miniDistanceItemToCentroid > tempDistance)
			 {
				 miniDistanceItemToCentroid = tempDistance;
				 miniIndex = i;
				 shiftedItems[index] = shiftedY; // update the shifted items under index
			 }
		}
		
		// after assignment, computing matrix M of shifted item not row item		
		centroidMatrixM[miniIndex] = matrixPlus(centroidMatrixM[miniIndex], computeItemMatrixM(shiftedItems[index]));
		
		ClusterData.clusterMemberNum[miniIndex]++;	
		
		return miniIndex;
	}
	
	/**
	 * execute addtion between a matrix and another matrix
	 * @param one a matrix
	 * @param two another matrix
	 * @return one
	 */
	public double[][] matrixPlus(double[][] one, double[][] two)
	{
		for (int i = 0; i < one.length; i++)
		{
			for (int j = 0; j < one.length; j++)
			{
				one[i][j] += two[i][j];
			}
		}
		
		return one;
	}
	
	/**
	 * compute minimum distance of vector x and y using new distance measure
	 * @param x the vector who keeps fixed
	 * @param y the vector who is performed scaling and shifting operation
	 * @param yid the index identifying the array y 
	 * @return compute minimum distance 
	 */
	public static double distanceXAndY(double[] x, double[] y)
	{
		double distance;
		double shift_q; 
		double scale_a;
		double sum;
		double[] temp;
		
		distance = 0;
		shift_q = x[42] - y[42];
		shiftedY = new double[y.length];
		sum = 0;
		temp = new double[y.length]; 
		
		// compute scale factor begining 
		for (int i = 0; i < shiftedY.length; i++)
		{
			shiftedY[i] = y[i] + shift_q;
			sum += x[i] * shiftedY[i];
		}
		scale_a = sum / l2normSquare(shiftedY);
		// compute scale factor over
		
		// compute the distance by the given formula
		for (int i = 0; i < shiftedY.length; i++)
		{
			temp[i] = x[i] - scale_a * shiftedY[i];
		}
		
		return Math.sqrt(l2normSquare(temp) / l2normSquare(x));
	}
	
	// compute euclidean distance
	public static double euclideanDistance(double[] x, double[] y)
	{
		double distance;
		
		distance = 0;
		for (int i = 0; i < x.length; i++)
		{
			distance += pow(x[i] - y[i], 2);
		}
		
		return sqrt(distance);	
	}
	
	/**
	 * copy items into the  irregular cluster result array by its subordinate clusterId
	 */
	@Override
	public void gatherClusterResults(int round)
	{
		int clusterId;
		int[] counter;
		
		counter = new int[ClusterParam.clusterNum];
		for (int i = 0; i < ClusterParam.clusterNum; i++)//initialize temp array for storing cluster results
		{
			//tempClusterResults[i] = new double[ClusterData.clusterMemberNum[i]][];
			irregularRownoWithClusterid[i] = new int[ClusterData.clusterMemberNum[i]];
		}
		  
		for (int i = 0; i < ClusterData.rowno; i++)
		{
			clusterId = ClusterData.rownoWithClusterid[i];
			// array irregularRownoWithClusterid[clusterId][counter[clusterId]] stores the row id of items[i];
			irregularRownoWithClusterid[clusterId][counter[clusterId]++] = i; 
		}
		
		out.println("\n=== this is the cluster results of round " + round + " ===");
		printClusterResult();
	}
	
	/**
	 * compute matrix M corresponding every item
	 * @param data content of an item under certain index 
	 */
	public double[][] computeItemMatrixM(double[] data)  
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
				{
					temp[i][j] += 1;
				}
			}
		}
		//this.itemsMatrixM[index] = temp;
		return temp;
	}
	
	// print cluster results
	public void printClusterResult()
	{
		for (int i = 0; i < irregularRownoWithClusterid.length; i++)
		{
			out.print("cluster " + (i+1) + " >> ");
			for (int j = 0; j < irregularRownoWithClusterid[i].length; j++)
			{
				out.print(irregularRownoWithClusterid[i][j] + " ");
			}
			out.print("\n");
		}
	}
	
	/**
	 * compute the level 2 norm of given array data
	 * @param data an array
	 * @return norm 
	 */
	public static double l2normSquare(double[] data)
	{
		double result = 0;
		
		for (int i = 0; i < data.length; i++)
		{
			result += Math.pow(data[i], 2.0);
		}
		
		return result;
	}
	
	/**
	 * compute the smallest eigenvector of matrix M
	 * @param clusterId cluster tag
	 * @param data  matrix M of centroid with clusterId index
	 */
	public double[] computeSmallestEigenVector(double[][] data) 
	{ 
		double temp = 0, minimal = 0;
		int[] column = {0};
		int i = 0;  
		double[] array;
		
		array = new double[data.length];
		Matrix matrix2d = new Matrix(data);
		EigenvalueDecomposition ed = new EigenvalueDecomposition(matrix2d);
		
		//minimal == smallest eigenValue , column == column with smallest eigenValue 
		minimal = ed.getD().get(i, i); 
		for (i = 1; i < data.length; i++) 
		{
			temp = ed.getD().get(i, i);
			if (temp < minimal) 
			{
				minimal = temp;
				column[0] = i;
			}
		}
		// 通过最小特征值 的 所属列， 找出 对应的 最小特征向量
		array = ed.getV().getMatrix(0, data.length - 1, column).getColumnPackedCopy();
		//对特征向量进行整理
		temp = array[0]; 
		if(temp != 0.0)
		{
			for (int j = 0; j < data.length; j++)
			{
				array[j] /= temp;
			}
		}
		 
		return array; 
	}
}