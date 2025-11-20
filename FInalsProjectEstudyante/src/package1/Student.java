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
            System.out.println("[5] Delete Account");
            int choice = system.validate().menuChoice("Choose: ", 5);
            
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

            else if (choice == 5) {
                deleteAccount();
            }
        }
    }

    private void requestDocument() 
    {

        String doc;
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

        DocumentRequest request = new DocumentRequest(record.getUsername(), record.getStudentNum(), doc, "Pending Payment", dm.genDate());
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
                System.out.println("---------------------------------------------");
                System.out.println("   REQUEST: " + req.getDate());
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
        String issue = system.validate().requireText("Describe your concern: ");
        
        List<HelpdeskTicket> tickets = hm.loadTickets();
        
        int id = tickets.size() + 1;
        HelpdeskTicket t = new HelpdeskTicket(id, record.getStudentNum(), issue, "Open");
        hm.addTicket(t);
        
        System.out.println("Helpdesk ticket submitted.");
    }

    private void deleteAccount() {
        String user = record.getUsername();
        System.out.println("= CONFIRM DELETE ACCOUNT =");
        System.out.println(" - User: " + user);
        System.out.println("= THIS USER WILL BE DELETED ");


        if (system.validate().confirm("Are you sure? ")) { //1st confirm

            // second con
            if (system.validate().confirm("Warning: this user will be permanently deleted, you will be put back onto the login screen. "))  {
                system.userManager().deleteUser(user);
                System.out.println(user + " was deleted");
                new UserAuth(system).start();
            }
            else return;

        } else return;
    }
}
