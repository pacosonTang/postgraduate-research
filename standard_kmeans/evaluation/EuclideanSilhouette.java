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
	public double computeSilhouette(int[][] clusterResults)
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
	public void sameDiffClusterDistance(int[][] clusterResults)
	{
		double item[];
		Date date;
		
		sameClusterDistance = new double[clusterResults.length][];
		diffClusterDistance = new double[clusterResults.length][];
		
		// compute same cluster distances and diff cluster distances
		for (int i = 0; i < clusterResults.length; i++)  
		{
			sameClusterDistance[i] = new double[clusterResults[i].length];
			diffClusterDistance[i] = new double[clusterResults[i].length];
			 for (int j = 0; j < clusterResults[i].length; j++)
			{
				 sameClusterDistance[i][j] = 0;
				 for (int k = 0; k < clusterResults[i].length; k++) //1st step:compute the avg distance between item under index j and other items in same cluster
				 {
					 sameClusterDistance[i][j] += distanceOfSampleToSample(clusterResults[i][j], clusterResults[i][k]);
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
					 miniDistance += distanceOfSampleToSample(clusterResults[i][j], clusterResults[miniIndex][j2]);
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
						 diffClusterDistance[i][j] += distanceOfSampleToSample(clusterResults[i][j], clusterResults[i2][j2]);
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
	 
	public double distanceOfSampleToSample(int firstRowid, int secondRowid)
	 {
		 double distance;
		 double[] one;
		 double[] two;
		 
		 distance = 0;
		 one = ClusterData.items[firstRowid];
		 two = ClusterData.items[secondRowid];
		 
		 for (int i = 0; i < one.length - 1; i++) 
		 {
			 distance += pow(one[i] - two[i], 2);
		 }
		 return sqrt(distance);
	 }
	 
}
