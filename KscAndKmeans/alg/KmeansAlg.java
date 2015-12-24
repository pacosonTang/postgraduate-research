package com.research.Alg;

import com.research.evaluation.EuclideanSilhouette;
import com.research.pojo.ClusterData;
import com.research.pojo.ClusterParam;
import static java.lang.Math.pow;

/**
 * kmeans alg class
 * @author Rong Tang
 * @version 1.0
 * @since 20150911
 */ 

public class KmeansAlg extends ClusterAlg{
	
	private double[] itemTag;		
	
	public KmeansAlg()
	{
		super(new EuclideanSilhouette(), "kmeans");
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
			clusterId = (int)ClusterData.items[i][ClusterData.dimension];
			for (int j = 0; j < ClusterData.dimension; j++) 
				ClusterData.centroid[clusterId][j] += ClusterData.items[i][j];
		}
		
		//update centroids(refinement procedure)
		for (int i = 0; i < ClusterParam.cluster; i++)  
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
		for (int i = 1; i < ClusterParam.cluster; i++) 
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
}

 