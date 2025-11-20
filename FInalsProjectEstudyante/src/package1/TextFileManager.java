package package1;

import java.io.*;
import java.util.*;

public class TextFileManager implements FileManager 
{

    @Override
    public void ensureFileExists(String fileName) 
    {
        try 
        {
            File f = new File(fileName);
            if (!f.exists())
            {
                f.createNewFile();
            }
            
        } 
        catch (Exception e)
        {
            System.out.println("Error creating file: " + fileName);
        }
    }

    @Override
    public List<String> readAll(String fileName)
    {
        ensureFileExists(fileName);
        
        ArrayList<String> list = new ArrayList<String>();
        
        try 
        {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = br.readLine()) != null) 
            {
                if (!line.trim().isEmpty()) list.add(line);
            }

            br.close();
            
        }
        catch (Exception e) 
        {
            System.out.println("Error reading file: " + fileName);
        }
        
        return list;
    }

    @Override
    public void writeAll(String fileName, List<String> lines) 
    {
        ensureFileExists(fileName);
        
        try 
        {
            FileWriter fw = new FileWriter(fileName, false);
            for (String l : lines)
            {
            	fw.write(l + "\n");
            }
            
            fw.close();
            
        } 
        catch (Exception e)
        {
            System.out.println("Error writing file: " + fileName);
        }
    }

    @Override
    public void appendLine(String fileName, String line) 
    {
        ensureFileExists(fileName);
        
        try 
        {
            FileWriter fw = new FileWriter(fileName, true);
            fw.write(line + "\n");
            fw.close();
        } 
        catch (Exception e) 
        {
        	System.out.println("Error appending file: " + fileName);
        }
    }
}
