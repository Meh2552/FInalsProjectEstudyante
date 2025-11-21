package package1;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    public String genDate() {

        LocalDateTime time = LocalDateTime.now();
        DateTimeFormatter form1 = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter form2 = DateTimeFormatter.ofPattern("HH:mm:ss");

        String formDate = "[" + time.format(form1) + "] " + time.format(form2);
        return formDate;

    }

}

