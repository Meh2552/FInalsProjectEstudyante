package package1;

import java.util.*;

public abstract class DocuHandler 
{

    protected TextFileManager fm;

    public DocuHandler()
    {
        this.fm = new TextFileManager();
    }

    public abstract String getFileName();

    public void ensure() 
    {
        fm.ensureFileExists(getFileName());
    }

    public List<String> read() 
    {
        return fm.readAll(getFileName());
    }

    public void write(List<String> lines) 
    {
        fm.writeAll(getFileName(), lines);
    }

    public void append(String line) 
    {
        fm.appendLine(getFileName(), line);
    }
}

