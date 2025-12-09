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

    public void changeState(String id, String state) {

        List<DocumentRequest> list = loadAll();
        ArrayList<DocumentRequest> temp = new ArrayList<>();

        for (DocumentRequest request : list) {
            if (request != null) {
                if (request.getId().equals(id)) request.setStatus(state);
                temp.add(request);
                
            }
        }

        saveAll(temp);

    }

    public void saveAll(List<DocumentRequest> reqs) 
    {
        ArrayList<String> out = new ArrayList<>();
        
        for (DocumentRequest r : reqs) 
        {
        	out.add(r.toFileLine());
        }
        
        write(out);
    }

    public String genId() {
        List<DocumentRequest> all = loadAll();

        int i = 1;
        for (DocumentRequest req : all) {

            int check = Integer.parseInt(req.getId());
            if (i <= check) {
                i = check;
            }

            i++;

        }

        String result = String.format("%04d", i);
        return result;
    }

}

