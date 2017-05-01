import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

public abstract class FileManager 
{
	public static String[] scanFiles(String directoryIn)
	{
		File dir = new File(directoryIn);
		
		List<String> tempFileList = new ArrayList<String>();

		List<File> files = (List<File>) FileUtils.listFiles(dir, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
		
		for (File file : files) 
		{
			try 
			{
				tempFileList.add(file.getCanonicalPath());
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		
		String[] output = new String [tempFileList.size()];
		tempFileList.toArray(output);
		return output;
    }
	
	public static List<String> compare(String sourceIn, String targetIn)
	{
		System.out.println("In compare function");
		String[] sourceFiles = scanFiles(sourceIn);
		String[] targetFiles = scanFiles(targetIn);
		List<String> output = new ArrayList<String>();
		
		//If there are no files in the target folder
		if (targetFiles.length == 0)
		{
			for (String file : sourceFiles)
			{
				output.add(file);
			}
			return output;
		}
		
		//If there are files in the target folder
		else
		{
			List<String> tempList = new ArrayList<String>();
			for(String fileTarget : targetFiles)
			{
				tempList.add((fileTarget.substring(targetIn.length(), fileTarget.length())));
			}
			String[] targetList = new String [tempList.size()];
			tempList.toArray(targetList);
			
			for(String sourceFile : sourceFiles)
			{
				if (Arrays.asList(targetList).contains(sourceFile.substring(sourceIn.length(), sourceFile.length())))
				{
					continue;
				}
				else
				{
					output.add(sourceFile);
				}
			}
		}
		
		return output;
	}
	
	public static void merge(List<String> filesIn, String sourceIn, String targetIn)
	{
		List<String> targetList = new ArrayList<String>();
		
		for (String file : filesIn)
		{
			targetList.add(targetIn + (file.substring(sourceIn.length(), file.length())));
		}
		
		for (int i = 0; i < (targetList).size(); i++)
		{
			File source = new File(filesIn.get(i));
			File target = new File(targetList.get(i));
			try 
			{
				FileUtils.copyFile(source, target);
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}
}
