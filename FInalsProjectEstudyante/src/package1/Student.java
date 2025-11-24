package package1;

import java.util.*;

public class Student extends User 
{
    private DocumentManager dm;
    private HelpdeskManager hm;
    private HelpdeskResponseManager hrm;

    public Student(MainSystem system, UserRecord record) 
    {
        super(system, record);
        this.dm = system.documentManager();
        this.hm = system.helpdeskManager();
        this.hrm = system.helpdeskResponseManager();
    }

    @Override
    public void start() 
    {
        while (true) 
        {
            System.out.println("=== STUDENT MENU ===");
            System.out.println("[1] Request Document");
            System.out.println("[2] View My Requests");
            System.out.println("[3] Ask Concern (Helpdesk)");
            System.out.println("[4] View Helpdesk Replies");
            System.out.println("[5] Logout");
            System.out.println("[6] Delete Account");
            int choice = system.validate().menuChoice("Choose: ", 6);
            
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
            	viewHelpdeskReplies();
            }
            
            else if (choice == 5) 
            {
            	return;
            }

            else if (choice == 6) {
                deleteAccount();
            }
        }
    }

    private void requestDocument() 
    {

        // TODO: change the price
        String doc, price = "0";
        
        while (true) {

            System.out.println("[1] SF10");
            System.out.println("[2] Form137");
            System.out.println("[3] Good Moral");
            System.out.println("[4] Enrollment Certificate");
            System.out.println("[5] Go Back");
            int choice = system.validate().menuChoice("Select: ", 5);
        
            doc = "";
        
            if (choice == 1) 
            {
                doc = "SF10";
                price = "100";
            } 
            
            else if (choice == 2) 
            {
                doc = "Form137";
                price = "101";
            } 
            
            else if (choice == 3) 
            {
                doc = "Good Moral";
                price = "102";
            } 
            
            else if (choice == 4) 
            {
                doc = "Enrollment Certificate";
                price = "104";
            }

            else if (choice == 5) {
                return;
            }

            System.out.println("=CONFIRM REQUEST FOR: =");
            System.out.println(doc);

            switch (system.validate().editCancelContinue()) {

                case "EDIT" -> {
                    continue;
                }

                case "CANCEL" -> {
                    return;
                }
                
                case "CONTINUE" -> {
                    
                }
            }

            break;
        }

        DocumentRequest request = new DocumentRequest(record.getUsername(), record.getStudentNum(), doc, "Pending Payment", system.genDate(), dm.genId());
        dm.addRequest(request);
        QueueSystem qs = new QueueSystem(request.getId(), price);
        
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
                System.out.println("-".repeat(50));
                System.out.printf("   REQUEST: %-10s            %s%n", req.getId(), req.getDate()); //TODO: change to
                System.out.printf("%n   REQUESTED DOCUMENT: %-15s  %s%n"  ,req.getDocName(), req.getStatus());
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

        String issue = "";
        
        while (true) 
        {

            System.out.println("\n=== CREATE HELP DESK TICKET ===");
            System.out.println("[1] Wrong student information");
            System.out.println("[2] Grade correction");
            System.out.println("[3] Payment concern");
            System.out.println("[4] Document follow-up");
            System.out.println("[5] Others (type your own issue)");

            int choice = system.validate().menuChoice("Select issue type: ", 6);

            if (choice == 1) 
            {
            	issue = "Wrong student information";
            }
            
            else if (choice == 2)
            {
            	issue = "Grade correction";
            }
            
            else if (choice == 3)
            {
            	issue = "Payment concern";
            }
            
            else if (choice == 4) 
            {
            	issue = "Document follow-up";
            }
            
            else 
            {
                issue = system.validate().requireText("Describe your issue: ");
            }

            String step = system.validate().editCancelContinue();
            if (step.equals("EDIT")) 
            {
                continue;
            }
            if (step.equals("CANCEL")) 
            {
                return;
            }
            
            break;
        }

        List<HelpdeskTicket> tickets = hm.loadTickets();
        int id = tickets.size() + 1;
        
        String date = system.genDate();

        HelpdeskTicket t = new HelpdeskTicket(id, record.getStudentNum(), issue, "Pending", date);
        hm.addTicket(t);

        System.out.println("Helpdesk ticket created. Ticket ID: " + id);
    }

    private void viewHelpdeskReplies() 
    {
        List<HelpdeskTicket> tickets = hm.loadTickets();
        
        List<HelpdeskResponse> responses = hrm.loadAll();

        boolean foundAny = false;
        
        System.out.println("=== MY HELP DESK TICKETS & RESPONSES ===");
        
        for (HelpdeskTicket t : tickets) 
        {
        	
            if (t.getStudentNum() != null && t.getStudentNum().equals(record.getStudentNum())) 
            {
                foundAny = true;
                
                System.out.println("Ticket ID: " + t.getId());
                System.out.println("Issue: " + t.getIssue());
                System.out.println("Date: " + t.getDate());
                System.out.println("Status: " + t.getStatus());

                boolean foundResp = false;
                
                for (HelpdeskResponse r : responses) 
                {
                	
                    if (r.getTicketId() == t.getId()) 
                    {
                        foundResp = true;
                        
                        System.out.println(" - Reply from " + r.getRespond() + " " + r.getTime());
                        System.out.println("-> " + r.getMessage());
                    }
                }
                
                if (!foundResp) 
                {
                    System.out.println(" - No replies yet.");
                }
            }
        }
        
        if (!foundAny)
        {
        	System.out.println("You have no tickets.");
        }
    }

    private void deleteAccount() // TODO: possibly gawin to manage acc not sure
    {
        String user = record.getUsername();
        System.out.println("= CONFIRM DELETE ACCOUNT =");
        System.out.println(" - User: " + user);
        System.out.println("= THIS USER WILL BE DELETED ");


        if (system.validate().confirm("Are you sure? ")) 
        { //1st confirm

            // second con
            if (system.validate().confirm("Warning: this user will be permanently deleted, you will be put back onto the login screen. "))  
            {
                system.userManager().deleteUser(user);
                System.out.println(user + " was deleted");
                new UserAuth(system).start();
            }
            else return;

        } 
        else return;
    }
}
