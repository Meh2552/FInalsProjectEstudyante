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
        Collections.sort(list, new Comparator<HelpdeskTicket>() {
            @Override
            public int compare(HelpdeskTicket a, HelpdeskTicket b)
            {
                // Normalize status ordering
                int orderA = statusRank(a.getStatus());
                int orderB = statusRank(b.getStatus());
                if (orderA != orderB) return Integer.compare(orderA, orderB);

                return a.getDate().compareTo(b.getDate());
            }

            private int statusRank(String s)
            {
                if (s == null) 
                {
                	return 4;
                }
                
                if (s.equalsIgnoreCase("Pending")) 
                {
                	return 1;
                }
                if (s.startsWith("Endorsed")) 
                {
                	return 2;
                }
                
                if (s.equalsIgnoreCase("Answered")) 
                {
                	return 3;
                }
                
                if (s.equalsIgnoreCase("Closed")) 
                {
                	return 5;
                }
                
                return 4;
            }
        });

        return list;
    }

    public void saveTickets(List<HelpdeskTicket> tickets)
    {
        ArrayList<String> out = new ArrayList<>();
        for (HelpdeskTicket t : tickets)
        {
            out.add(t.toFileLine());
        }
        write(out);
    }

    public boolean endorseTicket(int ticketId, String window, String date)
    {
        List<HelpdeskTicket> tickets = loadTickets();
        boolean found = false;
        for (HelpdeskTicket t : tickets)
        {
            if (t.getId() == ticketId)
            {
                t.setAssignedWindow(window);
                t.setStatus("Endorsed -> " + window);
                t.setDate(date); 
                found = true;
                break;
            }
        }
        
        if (found) 
        {
            saveTickets(tickets);
        }
        return found;
    }

}