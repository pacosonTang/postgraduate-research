package com.research.debug;

import static java.lang.Math.*; 
import static java.lang.System.*;

import java.lang.reflect.Array;
import java.util.Arrays;

public class IKmeansDebug
{
	public static void main(String[] args)
	{
		double[] rawdata = new double[]{2, 5, 8, 9, 7, 4, -1, 1};
		
		printArray(rawdata, 4);
		out.println("\n=== after haar wavelet decomposition ===");
		rawdata = HaarDecompose(rawdata);
		
		out.println("\n=== after reconstruct haar wavelet ===");
		printArray(rawdata, 1);
		for (int i = 1; i < rawdata.length / 2; i++)
		{
			HaarReconstruct(rawdata, i);
			printArray(rawdata, i+1);
		}
	}
	
	public static void HaarReconstruct(double[] rawitem, int curResolution)
	{
		double[] temp = new double[(int) pow(2, curResolution)];
		int span;
		
		span = (int) pow(2, curResolution-1);
		for (int i = 0; i < span; i++)
		{
			temp[2*i] = rawitem[i] + rawitem[i+span];
			temp[2*i+1] = rawitem[i] - rawitem[i+span];
		}
		System.arraycopy(temp, 0, rawitem, 0, temp.length);
	}
	
	public static double[] HaarDecompose(double[] rawitem)
	{
		double[] temp = new double[rawitem.length];
		double height;
		int resolution;
		
		height =log(rawitem.length) / log(2);
		resolution = rawitem.length;
		for (int i = (int)height; i > 0; i--)
		{
			for (int j = 0; j < resolution / 2; j++)
			{
				//average = (rawitem[2*j] + rawitem[2*j+1]) / 2;
				//difference = (rawitem[2*j] - rawitem[2*j+1]) / 2;
				//temp[j] = average;
				//temp[resolution/2+j] = difference;
				temp[j] = (rawitem[2*j] + rawitem[2*j+1]) / 2;
				temp[resolution/2+j] = (rawitem[2*j] - rawitem[2*j+1]) / 2;
			}
			rawitem = temp.clone();
			resolution /= 2;
			printArray(rawitem, i);
		}
		return temp;
	}
	
	public static void printArray(double[] temp, int resolution)
	{
		out.print("resolution " + resolution + ">> ");
		for (double d : temp)
		{
			out.print(d + " ");
		}
		out.println();
	}
}
