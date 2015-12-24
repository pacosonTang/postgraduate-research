package com.research.evaluation;

import static java.lang.Math.sqrt;
import static java.lang.Math.pow;

import java.util.Arrays;
import java.util.Date;

import com.research.pojo.ClusterData;
import com.research.pojo.ClusterParam;

public class EuclideanSilhouette extends Silhouette 
{	
	public EuclideanSilhouette()
	{
		super();
	}
	
	@Override
	public double computeSilhouette(double[][][] clusterResults)
	{
		double tempDistance;
		double silhouette;
		Date date;
		
		tempDistance = 0;
		silhouette = 0;
		
		sameDiffClusterDistance(clusterResults);
		for (int i = 0; i < clusterResults.length; i++)
		{
			for (int j = 0; j < clusterResults[i].length; j++)
			{
				tempDistance = maximum(diffClusterDistance[i][j] , sameClusterDistance[i][j]);
				silhouette +=  (diffClusterDistance[i][j] - sameClusterDistance[i][j]) / tempDistance;
			}
		}
		silhouette /= ClusterData.rowno;
		
		return silhouette;
	}

	// compute same cluster distances and diff cluster distances
	public void sameDiffClusterDistance(double[][][] clusterResults)
	{
		double item[];
		
		sameClusterDistance = new double[clusterResults.length][];
		diffClusterDistance = new double[clusterResults.length][];
		
		for (int i = 0; i < clusterResults.length; i++) // compute same cluster distances and diff cluster distances 
		{			
			sameClusterDistance[i] = new double[clusterResults[i].length];
			diffClusterDistance[i] = new double[clusterResults[i].length];
			 for (int j = 0; j < clusterResults[i].length; j++)
			{
				 item = clusterResults[i][j];
				 sameClusterDistance[i][j] = 0;
				//1st step:compute the avg distance between item under index j and other items in same cluster
				 for (int k = 0; k < clusterResults[i].length; k++) 
				 {
					 sameClusterDistance[i][j] += distanceOfSampleToSample(item, clusterResults[i][k]);
				 }
				 sameClusterDistance[i][j] /= (clusterResults[i].length - 1);
				 // 1st step over
				 
				//2nd step:compute the avg distance between one and the other
				// but they belongs to different clusters
				 
				 //2.1 compute the minimal avg distance
				 int miniIndex;
				 double miniDistance;
				 
				 miniIndex = 1;
				 miniDistance = 0;
				 
				 if(miniIndex == i)
					 miniIndex = i - 1;
				 for (int j2 = 0; j2 < clusterResults[miniIndex].length; j2++)
				{
					 miniDistance += distanceOfSampleToSample(item, clusterResults[miniIndex][j2]);
				}
				 miniDistance /= (clusterResults[miniIndex].length - 1);				
				 // 2.1 step over
				 
				 //2.2 compute other avg distances in diff clusters
				for (int i2 = 0; i2 < clusterResults.length; i2++)
				{
					if(i2 == i || i2 == miniIndex)
						continue;			
					diffClusterDistance[i][j] = 0;
					for (int j2 = 0; j2 < clusterResults[i2].length; j2++)
					{
						//System.out.println("j2 = " + j2);
						 diffClusterDistance[i][j] += distanceOfSampleToSample(item, clusterResults[i2][j2]);
					}
					diffClusterDistance[i][j] /= (clusterResults[i2].length - 1);				
					if(miniDistance > diffClusterDistance[i][j])
					{
						miniDistance = diffClusterDistance[i][j];						
					}
				}
				diffClusterDistance[i][j] = miniDistance;
				// 2.2 step over
			}
		} // compute same cluster distances and diff cluster distances over
	}
	 
	 public double distanceOfSampleToSample(double[] one, double[] another)
	 {
		 double distance;
		 
		 distance = 0;
		 for (int i = 0; i < one.length - 1; i++) // Attention, it's one.length-1 not one.length
			distance += pow(one[i] - another[i], 2);
		 return sqrt(distance);
	 }
	 
}
