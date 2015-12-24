package com.research.evaluation;

import java.util.Date;

import Jama.Matrix;

import com.research.Alg.KscAlg;
import com.research.pojo.ClusterData;

public class TimeSeriesSilhouette extends Silhouette
{
	public TimeSeriesSilhouette()
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
		
		date = new Date();		
		System.out.println("before: " + date.toString());
		sameDiffClusterDistance(clusterResults);
		System.out.println("after: " + date.toString());
		
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
		Date date;
		
		sameClusterDistance = new double[clusterResults.length][];
		diffClusterDistance = new double[clusterResults.length][];
		
		for (int i = 0; i < clusterResults.length; i++) // compute same cluster distances and diff cluster distances 
		{
			System.out.println("i = " + i);
			date = new Date();		
			System.out.println("now: " + date.toString());
			sameClusterDistance[i] = new double[clusterResults[i].length];
			diffClusterDistance[i] = new double[clusterResults[i].length];
			 for (int j = 0; j < clusterResults[i].length; j++)
			{
				 item = clusterResults[i][j];
				 sameClusterDistance[i][j] = 0;
				 for (int k = 0; k < clusterResults[i].length; k++) //1st step:compute the avg distance between item under index j and other items in same cluster
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
	
	@Override
	public double distanceOfSampleToSample(double[] xVector, double[] yVector)
	{
		double[][] temp;
		double[][] temp2;
		double distance;
		KscAlg alg;
		Matrix M;
		Matrix xMatrix;
		Matrix xTransMatrix;
		Matrix yMatrix;
		
		temp = new double[1][];
		temp2 = new double[1][];
		alg = new KscAlg();
		
		temp[0] = xVector;
		xMatrix = new Matrix(temp, 1, ClusterData.dimension);
		xTransMatrix = xMatrix.transpose();
		
		temp2[0] = yVector;
		yMatrix = new Matrix(temp2, ClusterData.dimension, 1);
		M = new Matrix(alg.computeItemMatrixM(yVector), ClusterData.dimension, ClusterData.dimension);
		
		distance = xMatrix.times(M).times(xTransMatrix).get(0, 0);
		distance /= alg.l2normSquare(xVector);
		
		return Math.sqrt(distance);
	}
}





















