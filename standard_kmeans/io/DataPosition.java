package com.research.io;

public class DataPosition
{
	private String folder;
	private String filename;
	
	public DataPosition(String folder, String filename) 
	{
		this.folder = folder;
		this.filename = filename;
	}

	public String getFolder()
	{
		return folder;
	}

	public String getFilename()
	{
		return filename;
	}
	
}
