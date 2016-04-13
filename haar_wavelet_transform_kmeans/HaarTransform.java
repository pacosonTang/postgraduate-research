package com.research.reduceDimension6;

import static java.lang.Math.pow;
import com.research.pojo6.ClusterData;

public class HaarTransform{
	public static int maxResolution;
	/**
	 * executing haar reconstruction transform towards given array
	 * @param rawitem raw array
	 * @param curResolution current resolution
	 */
	public static void haarReconstruct(double[] rawitem, int curResolution)	{
		double[] temp = new double[(int) pow(2, curResolution+1)];
		int span;
		
		span = (int) pow(2, curResolution);
		for (int i = 0; i < span; i++)
		{
			temp[2*i] = rawitem[i] + rawitem[i+span];
			temp[2*i+1] = rawitem[i] - rawitem[i+span];
		}
		System.arraycopy(temp, 0, rawitem, 0, temp.length);
	}
	
	/**
	 * @param rawitem is a array
	 * @param leastResolution is the least resolution  allowed be zero.
	 * @param maxResolution is equals to log(full dimension)
	 * @return
	 */
	public static double[] haarDecompose(double[] rawitem, int leastResolution)	{
		double[] temp = new double[rawitem.length];
		int resolutionLength = ClusterData.dimension;
		int maxResolution = HaarTransform.maxResolution;
		
		// you know least resolution equals to 0.
		for (int i = maxResolution; i > leastResolution; i--)
		{
			for (int j = 0; j < resolutionLength / 2; j++)
			{
				temp[j] = (rawitem[2*j] + rawitem[2*j+1]) / 2;
				temp[resolutionLength/2+j] = (rawitem[2*j] - rawitem[2*j+1]) / 2;
			}
			rawitem = temp.clone();
			resolutionLength /= 2;
		}
		return temp;
	}
}
