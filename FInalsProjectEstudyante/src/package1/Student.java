package package1;

import java.util.*;

public class Student extends User 
{

    private DocumentManager dm;
    private HelpdeskManager hm;

    public Student(MainSystem system, UserRecord record) 
    {
        super(system, record);
        this.dm = system.documentManager();
        this.hm = system.helpdeskManager();
    }

    @Override
    public void start() 
    {
        while (true) 
        {
            System.out.println("=== STUDENT MENU ===");
            System.out.println("[1] Request Document");
            System.out.println("[2] View My Requests");
            System.out.println("[3] Helpdesk");
            System.out.println("[4] Logout");
            int choice = system.validate().menuChoice("Choose: ", 4);
            
            if (choice == 1) 
            {
            	requestDocument();
            }
            
            else if (choice == 2)
            {
            	viewRequests();
            }
            
            else if (choice == 3)
            {
            	helpdesk();
            }
            else if (choice == 4) 
            {
            	return;
            }
        }
    }

    private void requestDocument() 
    {
        System.out.println("[1] SF10");
        System.out.println("[2] Form137");
        System.out.println("[3] Good Moral");
        System.out.println("[4] Enrollment Certificate");
        int choice = system.validate().menuChoice("Select: ", 4);
        
        String doc = "";
        
        if (choice == 1)
        {
        	doc = "SF10";
        }
        
        else if (choice == 2) 
        {
        	doc = "Form137";
        }
        
        else if (choice == 3)
        {
        	doc = "Good Moral";
        }
        else if (choice == 4)
        {
        	doc = "Enrollment Certificate";
        }
        
        DocumentRequest request = new DocumentRequest(record.getUsername(), record.getStudentNum(), doc, "Pending Payment");
        dm.addRequest(request);
        
        System.out.println("Request submitted.");
    }

    private void viewRequests() 
    {
        List<DocumentRequest> all = dm.loadAll();
        
        boolean found = false;
        
        System.out.println("=== MY REQUESTS ===");
        
        for (DocumentRequest req : all) 
        {
            if (req.getStudentNum() != null && req.getStudentNum().equals(record.getStudentNum())) 
            {
                System.out.println("- " + req.getDocName() + " | " + req.getStatus());
                found = true;
            }
        }
        
        if (!found)
        {
        	System.out.println("No requests found.");
        }
    }

    private void helpdesk() 
    {
        String issue = system.validate().requireText("Describe your concern: ");
        
        List<HelpdeskTicket> tickets = hm.loadTickets();
        
        int id = tickets.size() + 1;
        HelpdeskTicket t = new HelpdeskTicket(id, record.getStudentNum(), issue, "Open");
        hm.addTicket(t);
        
        System.out.println("Helpdesk ticket submitted.");
    }
}
