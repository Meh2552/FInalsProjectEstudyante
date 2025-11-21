package package1;

import java.util.*;

public class HelpdeskResponseManager extends DocuHandler 
{

    @Override
    public String getFileName() 
    {
        return "helpdesk_responses.txt";
    }

    public HelpdeskResponseManager() 
    { 
    	super(); 
    }

    public void addResponse(HelpdeskResponse resp) 
    {
        append(resp.toFileLine());
    }

    public List<HelpdeskResponse> loadAll() 
    {
        List<String> lines = read();
        
        ArrayList<HelpdeskResponse> out = new ArrayList<HelpdeskResponse>();
        
        for (String l : lines) 
        {
            HelpdeskResponse resp = HelpdeskResponse.fromLine(l);
            
            if (resp != null) out.add(resp);
        }
        
        return out;
    }
}
