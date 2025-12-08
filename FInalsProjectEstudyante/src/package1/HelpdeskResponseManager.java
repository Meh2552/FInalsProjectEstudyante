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
        ArrayList<HelpdeskResponse> out = new ArrayList<>();
        
        for (String l : lines)
        {
            HelpdeskResponse resp = HelpdeskResponse.fromLine(l);
            if (resp != null) out.add(resp);
        }
        
        return out;
    }

    public List<HelpdeskResponse> loadByResponder(String responderName)
    {
        List<HelpdeskResponse> all = loadAll();
        ArrayList<HelpdeskResponse> result = new ArrayList<>();
        
        for (HelpdeskResponse r : all)
        {
            if (r.getRespond().equalsIgnoreCase(responderName)) result.add(r);
        }

        Collections.sort(result, (a,b) -> a.getTime().compareTo(b.getTime()));
        return result;
    }

    public List<HelpdeskResponse> loadByTicket(int ticketId)
    {
        List<HelpdeskResponse> all = loadAll();
        ArrayList<HelpdeskResponse> result = new ArrayList<>();
        for (HelpdeskResponse r : all)
        {
            if (r.getTicketId() == ticketId) result.add(r);
        }
        Collections.sort(result, (a,b) -> a.getTime().compareTo(b.getTime()));
        return result;
    }

    public boolean updateRating(int ticketId, String responderName, String timestamp, int newRating)
    {
        List<HelpdeskResponse> all = loadAll();
        boolean updated = false;
        
        for (HelpdeskResponse r : all)
        {
            if (r.getTicketId() == ticketId && r.getRespond().equalsIgnoreCase(responderName) && r.getTime().equals(timestamp))
            {
                r.setRating(newRating);
                updated = true;
                break;
            }
        }
        if (updated)
        {
            ArrayList<String> out = new ArrayList<>();
            for (HelpdeskResponse r : all) out.add(r.toFileLine());
            write(out);
        }
        return updated;
    }
}