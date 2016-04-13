package com.research.alg6;

import static java.lang.System.out;

import java.io.IOException;
import java.util.Arrays;

import com.research.evaluation6.EuclideanSilhouette;
import com.research.io6.Result;
import com.research.pojo6.ClusterData;
import com.research.pojo6.ClusterParam;
import com.research.reduceDimension6.HaarTransform;
import com.research.tools6.AlgTools;

public abstract class ClusterAlg {

	public static String algName;
	private double[][] last_centroids;
	abstract void refine();// 精炼质心
	abstract void refine(Result result, int round);// 精炼质心
	public abstract double distance(double[] one, double[] two); // distance func
	
	public ClusterAlg(String algName){
		this.algName = algName;
	}
	
	public final void cluster() { // 迭代预测所用的聚类方法		
		randomRefineInitialCentroid();
		//Result result = Result.getEuclideanInstance(this);
		Result result = new Result(this, new EuclideanSilhouette(this));
		
		for (int i = 0; i < ClusterParam.clusterExeTimes; i++) {
			Arrays.fill(ClusterData.clusterMemberNum, 0); // 清空聚类成员个数记录
			for (int j = 0; j < ClusterData.rowno; j++){  
				ClusterData.rownoWithClusterid[j] = assign(j);
			}
			clearDoubleArray(ClusterData.centroid); // 清空质心
			refine(result, i);
			
			AlgTools.printOneDimArray(result.getSilhouette().silhouetteArray);
			
			double[] temp = result.getSilhouette().silhouetteArray;
			if(i>0 &&(Math.abs(temp[i] - temp[i-1]) < 0.0001)){
				if(!ClusterParam.isIterative) {
					ClusterData.centroid = last_centroids; 
					break;
				}
				else {
					if(ClusterData.dimension==ClusterData.resolutionLength) {
						break;
					}
					else {
						continue;
					}
				}
			}
			last_centroids = ClusterData.centroid;
		}
		
		// 写入聚类系数（聚类效果评估系数）
		// result.writeSilhouette();
		// AlgTools.printOneDimArray(result.getSilhouette().silhouetteArray);
		Arrays.fill(result.getSilhouette().silhouetteArray, 0); // 清空聚类轮廓系数记录
	}
	
	/**
	 * compute the centroid the item should be assigned to
	 * @param index refers to item index
	 * @return cluster id whose has the smallest distance between centroid and
	 *         the item
	 */
	public int assign(int index) {
		double miniDistance = distance(ClusterData.centroid[0], ClusterData.items[index]);
		int miniClusterId = 0;
		double distance;
		
		for (int i = 1; i < ClusterParam.clusterNum; i++) {
			distance = distance(ClusterData.centroid[i], ClusterData.items[index]); 
			if(miniDistance > distance){
				miniDistance = distance;
				miniClusterId = i;
			}
		}
		ClusterData.clusterMemberNum[miniClusterId]++;
		
		return miniClusterId;
	}

	// reset centroid array zeros
	final void clearDoubleArray(double[][] data) {
		for (int i = 0; i < data.length; i++)
			Arrays.fill(data[i], 0);
	}
	
	// randomly update or refine init centroids
	final void randomRefineInitialCentroid() {
		
		int[] initCentorid = generateRandom(ClusterData.rowno, ClusterParam.clusterNum);
		ClusterData.init_centroid = initCentorid; 
				
		System.out.println("====  init centroids are as follows：");
		for (int i = 0; i < initCentorid.length; i++){
			System.arraycopy(ClusterData.items[initCentorid[i]], 0, ClusterData.centroid[i], 0, ClusterData.dimension);
			out.printf("%-8s", "item" + initCentorid[i]);
		}
		out.printf("\n============================================================================\n");
	}

	/**
	 * fabricate random array
	 * @param volumn , random number upper limit
	 * @param interval , interval number and there is a random number in every interval
	 * @return a random array
	 */
	final int[] generateRandom(int volume, int interval) {
		
		int[] r_data = new int[interval];
		int intervalVolume = volume / interval;

		for (int i = 0; i < interval; i++) {
			int r = (int) (Math.random() * intervalVolume);
			r_data[i] = r + intervalVolume * i;
		}

		 r_data[0] = 1;
		 r_data[1] = 301;
		 r_data[2] = 401;
		 r_data[3] = 501;
		 r_data[4] = 701;
		 r_data[5] = 891;
		 
		return r_data;
	}
}
