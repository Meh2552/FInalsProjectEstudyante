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
        ArrayList<HelpdeskTicket> list = new ArrayList<>();

        for (String line : lines) 
        {
            HelpdeskTicket t = HelpdeskTicket.fromLine(line);
            if (t != null) list.add(t);
        }

        // priority based on arrival time
        Collections.sort(list, new Comparator<HelpdeskTicket>() 
        {
            @Override
            public int compare(HelpdeskTicket a, HelpdeskTicket b) 
            {
                
                String sA = a.getStatus();
                String sB = b.getStatus();

                // pending first
                if (sA.equals("Pending") && !sB.equals("Pending"))
                    return -1;

                if (!sA.equals("Pending") && sB.equals("Pending"))
                    return 1;

                // if same status prioritize by arrival time
                return a.getDate().compareTo(b.getDate());
            }
        });

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

