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
	public int[][] irregularRownoWithClusterid;
	protected DataWrite writeSilhouette;
	
	public ClusterAlg(Silhouette silhouette, String algName)
	{
		this.silhouette = silhouette;
		this.algName = algName; 
		irregularRownoWithClusterid = new int[ClusterParam.clusterNum][];
		writeSilhouette = new DataWrite(ClusterData.directory);
	}
	
	/**
	 * copy items into the  irregular cluster result array by its subordinate clusterId
	 */
	public abstract void gatherClusterResults(int round);
	
	/**
	 * cluster alg: first update centroids,and then assign a centroid for every item 
	 * @param ith  cluster executing order
	 * @throws IOException 
	 */
	public abstract double refineAndAssign(int ith) throws IOException;
	
	// update centroids
	public abstract void refine(); 
	
	// assign a centroid for every item
	public abstract int assign(int index); 
	
	public void clusterAlg() throws IOException
	{
		double[][] silhouette;
		
		silhouette = new double[1][ClusterParam.clusterExeNum];
		
		// after reading data from xlsx to memory, we first update init centroids randomly 
		randomRefineInitialCentroid(); 	
		
		// reset clusterMemberNum array zeros
		Arrays.fill(ClusterData.clusterMemberNum, 0); 
		
		//assign a centroid for every shifted item initially
		for (int i = 0; i < ClusterData.rowno; i++) 
		{
			ClusterData.rownoWithClusterid[i] = assign(i);
		}
		
		gatherClusterResults(1);
		silhouette[0][0] = this.silhouette.computeSilhouette(irregularRownoWithClusterid);
		 
		//1.update centroids(refinement step)
		//2.assign a centorid for every item(assignment step)
		for (int i = 2; i <= ClusterParam.clusterExeNum; i++) 
			silhouette[0][i-1] = refineAndAssign(i);
		
		// finally write the silhouette into xlsx
		writeSilhouette.writeSilhouette(silhouette, ClusterAlg.algName + ClusterParam.clusterNum +  "_silhouette.xlsx", "ClusterExeOrdinal");
	}
		
	// randomly update or refine init centroids
	public void randomRefineInitialCentroid() 
	{
		int[] initCentorid;
		
		initCentorid = generateRandom(ClusterData.rowno, ClusterParam.clusterNum);
		for (int i = 0; i < ClusterParam.clusterNum; i++)
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
		
		r_data[0] = 1;
		r_data[1] = 101;
		r_data[2] = 301;
		r_data[3] = 501;
		r_data[4] = 701;
		r_data[5] = 901;
		
		return r_data;
	}
	
	// reset centroid array zeros
	public void resetArrayToZeros(double[][] data)
	{
		for (int i = 0; i < data.length; i++)
			Arrays.fill(data[i], 0);
	}

	public int[][] getIrregularRownoWithClusterid()
	{
		return irregularRownoWithClusterid;
	}
}
