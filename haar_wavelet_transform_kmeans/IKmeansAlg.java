package com.research.alg6;

import static java.lang.Math.log;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

import java.io.IOException;

import com.research.io6.Result;
import com.research.pojo6.ClusterData;
import com.research.pojo6.ClusterParam;
import com.research.reduceDimension6.HaarTransform;

public class IKmeansAlg extends ClusterAlg {

	public IKmeansAlg(String algName){
		super(algName);
		haar_decompose(); // 对全体items 进行分解降维
	}
	
	public double distance(double[] one, double[] two) {
		 double distance = 0;
		 
		 for (int i = 0; i < ClusterData.resolutionLength; i++) {
			 distance += pow(one[i] - two[i], 2);
		 }
		 return sqrt(distance);
	 }
	
	@Override
	void refine(Result result, int round) {
		int clusterId;

		for (int i = 0; i < ClusterData.rowno; i++) {
			clusterId = ClusterData.rownoWithClusterid[i];
			for (int j = 0; j < ClusterData.resolutionLength; j++)
				ClusterData.centroid[clusterId][j] += ClusterData.items[i][j];
		}
		
		//update centroids(refinement procedure)
		for (int i = 0; i < ClusterParam.clusterNum; i++) {
			for (int j = 0; j < ClusterData.resolutionLength; j++) {
				ClusterData.centroid[i][j] /= ClusterData.clusterMemberNum[i];
			}
			
			// update centroids from lower resolution to higher resolution
			// 当分辨率级别对应维度小于 全维度时才对质心进行重构
			if(ClusterData.resolutionLength < ClusterData.dimension) {
				for (int j = ClusterData.resolutionLength-1; j >= 0 ; j--) {
					ClusterData.centroid[i][2*j] = ClusterData.centroid[i][j];
					ClusterData.centroid[i][2*j+1] = ClusterData.centroid[i][j];
				}
			}
		}
		
		// 输出聚类结果到终端
		result.print(round+1);
		try {
			result.writeCentroid(round+1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 收集每轮聚类系数（聚类效果评估系数）
		result.gatherSilhouette(round);
		
		if(ClusterData.resolutionLength < ClusterData.dimension) {			
			haar_reconstruct(); // 对全体items 进行重构升维
		}
	}
	
	// 对全体item进行哈儿小波分解降维
	public void haar_decompose() {
		ClusterData.resolution = 0;
		ClusterData.resolutionLength = (int)pow(2, ClusterData.resolution);
		HaarTransform.maxResolution = (int)(log(ClusterData.dimension) / log(2));
		for (int i = 0; i < ClusterData.rowno; i++)		{
			ClusterData.items[i] = HaarTransform.haarDecompose(
					ClusterData.items[i], ClusterData.resolution);			
		}
	}
	// 对全体item进行哈儿小波重构升维
	public void haar_reconstruct(){
		for (int i = 0; i < ClusterData.rowno; i++) {
			HaarTransform.haarReconstruct(ClusterData.items[i], ClusterData.resolution);
			// haarReconstruct(double[], int cur_resolution) // 注意是当前维度，所以维度++还在其后面 
		}
		ClusterData.resolution++;
		ClusterData.resolutionLength = (int)pow(2, ClusterData.resolution);
	}

	@Override
	void refine() {
	}
}
