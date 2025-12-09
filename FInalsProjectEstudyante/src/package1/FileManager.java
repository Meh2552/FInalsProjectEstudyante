package package1;

import java.util.List;

public interface FileManager
{

	List<String> readAll(String fileName);
	  
    void ensureFileExists(String fileName);

    void writeAll(String fileName, List<String> lines);

    void appendLine(String fileName, String line);
    
}

