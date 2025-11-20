package package1;

import java.util.*;

public class DocumentManager extends DocuHandler 
{

    @Override
    public String getFileName() 
    {
        return "documents.txt";
    }

    public DocumentManager() 
    { 
    	super(); 
    }

    public void addRequest(DocumentRequest request) 
    {
        append(request.toFileLine());
    }

    public List<DocumentRequest> loadAll() 
    {
        List<String> lines = read();
        
        ArrayList<DocumentRequest> list = new ArrayList<DocumentRequest>();
        
        for (String line : lines) 
        {
            DocumentRequest request = DocumentRequest.fromLine(line);
            if (request != null)
            {
            	list.add(request);
            }
        }
        
        return list;
    }

    public void saveAll(List<DocumentRequest> reqs) 
    {
        ArrayList<String> out = new ArrayList<String>();
        
        for (DocumentRequest r : reqs) 
        {
        	out.add(r.toFileLine());
        }
        
        write(out);
    }
}

