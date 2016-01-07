package com.research.Alg;

import java.io.IOException;
import java.util.Arrays;

import com.research.evaluation.EuclideanSilhouette;
import com.research.pojo.ClusterData;
import com.research.pojo.ClusterParam;

import static java.lang.Math.log;
import static java.lang.Math.pow;
import static java.lang.System.out;

/**
 * kmeans alg class
 * @author Rong Tang
 * @version 1.0
 * @since 20150911
 */ 

public class I_KmeansAlg extends ClusterAlg{
	
	private double[] itemTag;		
	
	public I_KmeansAlg()
	{
		super(new EuclideanSilhouette(), "i_kmeans");
		
		// computing haar wavelet coefficients
		for (int i = 0; i < ClusterData.rowno; i++)
		{
			ClusterData.items[i] = HaarDecompose(ClusterData.items[i]);
		}
	}
	
	/**
	 * executing haar transform towards given array
	 * @param rawitem raw array
	 * @return double[] who is haar decomposed
	 */
	public static double[] HaarDecompose(double[] rawitem)
	{
		double[] temp = new double[rawitem.length];
		double height;
		int resolution;
		
		height =log(rawitem.length) / log(2);
		resolution = rawitem.length;
		for (int i = (int)height; i > 0; i--)
		{
			for (int j = 0; j < resolution / 2; j++)
			{
				//average = (rawitem[2*j] + rawitem[2*j+1]) / 2;
				//difference = (rawitem[2*j] - rawitem[2*j+1]) / 2;
				//temp[j] = average;
				//temp[resolution/2+j] = difference;
				temp[j] = (rawitem[2*j] + rawitem[2*j+1]) / 2;
				temp[resolution/2+j] = (rawitem[2*j] - rawitem[2*j+1]) / 2;
			}
			rawitem = temp.clone();
			resolution /= 2;
		}
		return temp;
	}
	/**
	 * cluster alg: first update centroids,and then assign a centroid for every item 
	 * @param ith  cluster executing order
	 * @throws IOException 
	 */
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
	 */
	public void refine() 
	{
		int clusterId;
		
		resetArrayToZeros(ClusterData.centroid); // reset centroid array zeros

		for (int i = 0; i < ClusterData.rowno; i++) 
		{
			clusterId = ClusterData.rownoWithClusterid[i];
			for (int j = 0; j < ClusterData.dimension; j++) 
				ClusterData.centroid[clusterId][j] += ClusterData.items[i][j];
		}
		
		//update centroids(refinement procedure)
		for (int i = 0; i < ClusterParam.clusterNum; i++)  
			for (int j = 0; j < ClusterData.dimension; j++)  
				ClusterData.centroid[i][j] /= ClusterData.clusterMemberNum[i];
	}
	
	/**
	 * compute the centroid the item should be assigned to 
	 * @param index refers to item index
	 * @return cluster id whose has the smallest distance between centroid and the item
	 */
	public int assign(int index)
	{
		double sum;
		double miniSum;
		int miniIndex;
		double[] item;
		
		sum = 0;
		miniSum = 0;
		miniIndex = 0;
		item = ClusterData.items[index];
		
		for (int j = 0; j < ClusterData.dimension; j++)  
			sum += pow(item[j] - ClusterData.centroid[0][j], 2.0);
		 
		miniSum = sum;		
		for (int i = 1; i < ClusterParam.clusterNum; i++) 
		{
			sum = 0;
			for (int j = 0; j < ClusterData.dimension; j++) 
				sum += Math.pow(item[j] - ClusterData.centroid[i][j], 2.0);
			if(miniSum > sum) 
			{
				miniSum = sum;
				miniIndex = i;
			}
		}
		
		ClusterData.clusterMemberNum[miniIndex]++;
		
		return miniIndex;
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
}

 