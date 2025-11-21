package package1;

import java.util.*;

public abstract class Employee extends User
{

    public Employee(MainSystem system, UserRecord record) 
    {
        super(system, record);
    }

    public abstract void employeeMenu();

    @Override
    public void start() 
    {
        employeeMenu();
    }
    
    public void respondToTicket() 
    {

        List<HelpdeskTicket> tickets = system.helpdeskManager().loadTickets();
        
        if (tickets.size() == 0) 
        {
            System.out.println("No tickets.");
            
            return;
        }

        while (true) 
        {
            System.out.println("=== SELECT TICKET TO RESPOND ===");
            
            for (int i = 0; i < tickets.size(); i++) 
            {
                HelpdeskTicket t = tickets.get(i);
                
                System.out.println("[" + (i+1) + "] ID:" + t.getId() + " (" + t.getStudentNum() + ") " + t.getIssue() + " | Status: " + t.getStatus() + " | " + t.getDate());
            }

            int sel = system.validate().menuChoice("Select ticket: ", tickets.size());            
            String action = system.validate().editCancelContinue();
            
            if (action.equals("EDIT"))
            {
            	continue;
            }
            
            if (action.equals("CANCEL"))
            {
            	return;
            }

            HelpdeskTicket chosen = tickets.get(sel - 1);

            String respMsg = system.validate().requireText("Type your response message: ");

            String ts = system.genDate();

            String responderName = record.getFullName();

            HelpdeskResponse resp = new HelpdeskResponse(chosen.getId(), responderName, respMsg, ts);
            system.helpdeskResponseManager().addResponse(resp);

            chosen.setStatus("Answered");
            system.helpdeskManager().saveTickets(tickets);

            System.out.println("Response saved and ticket marked Answered.");
            return;
        }
    }
}
