package com.research.Alg;

import static java.lang.System.out;

import java.io.IOException;
import java.util.Arrays;

import com.research.evaluation.Silhouette;
import com.research.io.DataWrite;
import com.research.pojo.ClusterData;
import com.research.pojo.ClusterParam;

public abstract class ClusterAlg
{	
	public static String algName;
	protected Silhouette silhouette;
	public double[][][] tempClusterResults;
	public abstract void refine(); // update centroids
	public abstract int assign(int index);// assign a centroid for every item
	protected DataWrite writeSilhouette;
	
	public ClusterAlg(Silhouette silhouette, String algName)
	{
		this.silhouette = silhouette;
		this.algName = algName; 
		tempClusterResults = new double[ClusterParam.cluster][][];
		writeSilhouette = new DataWrite(ClusterData.directory);
	}
	
	public void clusterAlg() throws IOException
	{
		double[][] silhouette;
		
		silhouette = new double[1][ClusterParam.clusterExeNum];
		
		randomRefineInitialCentroid();// after reading data from xlsx to memory, we update init centroids	
		
		Arrays.fill(ClusterData.clusterMemberNum, 0); // reset clusterMemberNum array zeros
		
		//assign a centroid for every item initially
		for (int i = 0; i < ClusterData.rowno; i++) 
			ClusterData.items[i][ClusterData.dimension] = assign(i); 
		gatherClusterResults();
		silhouette[0][0] = this.silhouette.computeSilhouette(tempClusterResults); //compute silhouette of clusters
		 
		//1.update centroids(refinement step)
		//2.assign a centorid for every item(assignment step)
		for (int i = 2; i <= ClusterParam.clusterExeNum; i++) 
			silhouette[0][i-1] = refineAndAssign(i);
		
		// finally write the silhouette into xlsx
		writeSilhouette.writeSilhouette(silhouette, ClusterAlg.algName + ClusterParam.cluster +  "_silhouette.xlsx", "ClusterExeOrdinal");
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
			ClusterData.items[i][ClusterData.dimension] = assign(i);
		gatherClusterResults();
		silhouette = this.silhouette.computeSilhouette(tempClusterResults); //compute silhouette of clusters
		
		return silhouette;
	}
	
	// randomly update or refine init centroids
	public void randomRefineInitialCentroid() 
	{
		int[] initCentorid;
		
		initCentorid = generateRandom(ClusterData.rowno, ClusterParam.cluster);
		for (int i = 0; i < ClusterParam.cluster; i++)
			ClusterData.centroid[i] = ClusterData.items[initCentorid[i]].clone();

		System.out.println("====  init centroids are as follows£º");
		for (int i = 0; i < initCentorid.length; i++)  
			out.printf("%-8s", ClusterData.itemTag[initCentorid[i]]);
		out.printf("\n============================================================================\n");
	}
	/**
	 * fabricate random numbers 
	 * @param volumn Random number upper limit
	 * @param interval interval number and there is a random number in every interval
	 * @return a random array
	 */
	public int[] generateRandom(int volume, int interval)
	{
		int[] r_data;
		int intervalVolume;
		
		r_data = new int[interval];
		intervalVolume = volume / interval;
		for (int i = 0; i < interval; i++) 
		{
			int r = (int)(Math.random() * intervalVolume);
			r_data[i] = r + intervalVolume * i; 
		}
		return r_data;
	}
	
	// reset centroid array zeros
	public void resetArrayToZeros(double[][] data)
	{
		for (int i = 0; i < data.length; i++)
			Arrays.fill(data[i], 0);
	}
	/**
	 * copy items into the cluster result array by its subordinate clusterId
	 */
	public void gatherClusterResults()
	{
		int clusterId;
		int[] counter;
		
		counter = new int[ClusterParam.cluster];
		for (int i = 0; i < ClusterParam.cluster; i++)//initialize temp array for storing cluster results
			tempClusterResults[i] = new double[ClusterData.clusterMemberNum[i]][];
		  
		for (int i = 0; i < ClusterData.rowno; i++)
		{
			clusterId = (int)ClusterData.items[i][ClusterData.dimension];
			tempClusterResults[clusterId][counter[clusterId]++] = ClusterData.items[i];
		}
	}
	
	/*
	public void printResults(int ith) 
	{
		int k = 0;
		
		System.out.println("==== after " + ith + "th order cluster executing, reuslts are as follows ====");
		for (int i = 0; i < ClusterParam.cluster; i++) 
		{
			k = 0;
			System.out.print("items assigned to cluster" + (i+1) + " below : ");
			for (int j = 0; j < ClusterData.rowno; j++) 
			{
				if(ClusterData.items[j][ClusterData.dimension] == i) 
					out.printf("%-8s", ClusterData.itemTag[j]);
			}
			out.printf("\n");
		}
		
		out.println("==== " + ith + "th cluster executing over====\n");
	}*/
}
