package com.research.pojo;

/**
 * 时间序列类
 * @author Rong Tang
 * @version 1.0
 * @since 20150911
 */
public class TimeSeries extends ClusterData
{
	public TimeSeries(double[][] items, String[] itemTag, double[][] centroid, int[] clusterMemberNum, int rowno, int dimension, String directory, int[] rwc)
	{
		super(items, itemTag, centroid, clusterMemberNum, rowno, dimension, directory, rwc);
	}
	
	public TimeSeries(String dir, int rowno, int clusterNum, int dimension)
	{
		super(dir, rowno, clusterNum, dimension);
	}
}
