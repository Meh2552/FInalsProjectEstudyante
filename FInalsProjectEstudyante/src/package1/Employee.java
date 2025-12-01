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

    public void viewResponse() {
        String name = record.getFullName();
        List<HelpdeskResponse> list = system.helpdeskResponseManager().loadByResponder(name);

        if (list.size() == 0) {
            System.out.println("You have not responded to any tickets yet.");
            return;
        }

        System.out.println("=== YOUR HELP DESK RESPONSE HISTORY ===");

        for (HelpdeskResponse r : list) {
            System.out.println("-".repeat(50));
            System.out.println("Ticket ID: " + r.getTicketId());
            System.out.println("Date: " + r.getTime());
            System.out.println("Message:");
            System.out.println("  " + r.getMessage());
        }
    }

    public abstract void displayRequest();

    public void requestManager() {

        displayRequest();

    }

    public void history(int page, List<String> statements) {

        QueueSystem qs = new QueueSystem(1, statements);
        int current = 1;

        while (true) {
            int max = system.queueSystem().getHistoryManager().countEntry(statements);
            int input = system.validate().minMaxXChoice("Go to page ('x' to go back):", 1, max);
            if (input == current) {
                System.out.println("Already on page " + input);
                continue;
            }
            if (input == -1) {
                return;
            }

            qs = new QueueSystem(input, statements);
            current = input;

        }

    }

    public void createRequest() {
        
    }

}
