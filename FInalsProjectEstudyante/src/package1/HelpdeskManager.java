package package1;

import java.util.*;

public class HelpdeskManager extends DocuHandler 
{

    @Override
    public String getFileName() 
    {
        return "helpdesk.txt";
    }

    public HelpdeskManager() 
    { 
    	super(); 
    }

    public void addTicket(HelpdeskTicket ticket)
    {
        append(ticket.toFileLine());
    }

    public List<HelpdeskTicket> loadTickets()
    {
        List<String> lines = read();
        
        ArrayList<HelpdeskTicket> list = new ArrayList<HelpdeskTicket>();
        
        for (String line : lines) 
        {
            HelpdeskTicket ticket = HelpdeskTicket.fromLine(line);
            if (ticket != null)
            {
            	list.add(ticket);
            }
        }
        
        return list;
    }

    public void saveTickets(List<HelpdeskTicket> tickets) 
    {
        ArrayList<String> out = new ArrayList<String>();
        
        for (HelpdeskTicket t : tickets)
        {
        	out.add(t.toFileLine());
        }
        
        write(out);
    }
}

