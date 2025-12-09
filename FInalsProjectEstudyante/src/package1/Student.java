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
            System.out.println("[5] Manage Account");
            System.out.println("[6] Logout");
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
            	accountMan();
            }

            else if (choice == 6) {
                return;
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
        QueueSystem qs = new QueueSystem(request.getId(), price, system.genDate(1));
        
        System.out.println("Request submitted.");
    }

    private void viewRequests() 
    {
        List<DocumentRequest> all = dm.loadAll();
        ArrayList<DocumentRequest> requestL = new ArrayList<>();
        
        boolean found = false;
        
        System.out.println("=== MY REQUESTS ===");
        
        for (DocumentRequest req : all) 
        {
            if (req.getStudentNum() != null && req.getStudentNum().equals(record.getStudentNum())) 
            {
                requestL.add(req);
            }
        }

        Collections.reverse(requestL);
        if (requestL.isEmpty())
        {
        	System.out.println("No requests found.");
            return;
        }

        int pageCount = (requestL.size() + 6 ) / 7;
        int page = 1;
        while (true) {
            int items = 0;

            for (DocumentRequest request : requestL) {
                items++;
                
                if (items < (page - 1) * 7) continue;
                else if (items >= page * 7) break;

                System.out.println    ("     ╔═════════════════════════════════════════════════════════════╗");
                System.out.printf("     ║  Request ID: %-10s               %20s  ║%n", request.getId(), request.getDate());
                System.out.println    ("     ║─────────────────────────────────────────────────────────────║");
                System.out.println    ("     ║                                                             ║");
                System.out.printf("     ║  Document Requested: %-30s         ║%n", request.getDocName());
                System.out.printf("     ║  State: %-30s                      ║%n", request.getStatus());
                System.out.println    ("     ║                                                             ║");
                System.out.println    ("     ╚═════════════════════════════════════════════════════════════╝");

            }
            System.out.println("----------------------------");

            if (pageCount == 1) System.out.println(" Viewing page " + page);
            else System.out.println(" Viewing page " + page + " out of " + pageCount);

            int input = system.validate().minMaxXChoice("Go to page ('x' to go back):", 1, pageCount);
            if (input == page) {
                System.out.println("Already on page " + input);
                continue;
            }

            if (input == -1) {
                return;
            }
            
            page = input;
        }


            
    }

    private void helpdesk() 
    {

        String issue = "";
        
        while (true) 
        {

            System.out.println("=== CREATE HELP DESK TICKET ===");
            System.out.println("[1] Wrong student information");
            System.out.println("[2] Grade correction");
            System.out.println("[3] Payment concern");
            System.out.println("[4] Document follow-up");
            System.out.println("[5] Others (type your own issue)");
            System.out.println("[6] Go Back");

            int choice = system.validate().menuChoice("Select: ", 6);

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
    
            
            else if (choice == 5)
            {
                issue = system.validate().requireText("Describe your issue: ");
            }

            else if (choice == 6)
            {
                return;
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

        HelpdeskTicket t = new HelpdeskTicket(id, record.getStudentNum(), issue, "Pending", date, "");
        hm.addTicket(t);

        System.out.println("Helpdesk ticket created. Ticket ID: " + id);
    }

    private void viewHelpdeskReplies() 
    {
        List<HelpdeskTicket> tickets = hm.loadTickets();
        List<HelpdeskResponse> responses = hrm.loadAll();

        ArrayList<HelpdeskTicket> myTickets = new ArrayList<>();
        for (HelpdeskTicket t : tickets) 
        {
            if (t.getStudentNum() != null && t.getStudentNum().equals(record.getStudentNum())) 
            {
                myTickets.add(t);
            }
        }

        if (myTickets.isEmpty()) 
        {
            System.out.println("You have no helpdesk tickets.");
            return;
        }

        System.out.println("=== MY HELP DESK TICKETS ===");

        System.out.printf("%-4s | %-8s | %-14s | %-30s | %-16s | %-18s | %-12s%n", 
                "No.", "TicketID", "Student", "Issue", "Window", "Last Modified", "Status");
            System.out.println("-------------------------------------------------------------------------------------------------------------------------------------");

            int idx = 1;
            
            for (HelpdeskTicket t : tickets)
            {
                String window = t.getAssignedWindow();
                
                if (window == null || window.isEmpty())
                {
                    window = "";
                }

                String lastModified = t.getDate();
                if (lastModified == null)
                {
                	lastModified = "";
                }
                System.out.printf("%-4d | %-8d | %-14s | %-30s | %-16s | %-12s | %-12s%n", idx, t.getId(), t.getStudentNum(), t.getIssue(), window, lastModified, t.getStatus());
                
                idx++;
            }

        int choose = system.validate().minMaxXChoice("Select ticket to view (X to go back): ", 1, myTickets.size());
        if (choose == -1) return;

        HelpdeskTicket selected = myTickets.get(choose - 1);


        ArrayList<HelpdeskResponse> respList = new ArrayList<>();
        System.out.println("\n--- RESPONSES ---");
        for (HelpdeskResponse r : responses) 
        {
            if (r.getTicketId() == selected.getId()) 
            {
                respList.add(r);
                System.out.println("From: " + r.getRespond() + " (" + r.getTime() + ")");
                System.out.println(" -> " + r.getMessage());
                
                if (r.getRating() > 0) 
                {
                    System.out.println(" Rating: " + r.getRating());
                } 
                else 
                {
                    System.out.println(" Rating: Not yet rated");
                }

                System.out.println("-------------------------------------");
            }
        }

        if (respList.isEmpty()) 
        {
            System.out.println("No responses yet.");
            return;
        }


        System.out.println("\nRATE A RESPONSE");
        System.out.println("[1] Rate a response");
        System.out.println("[2] Go Back");
        int action = system.validate().menuChoice("Choose: ", 2);
        
        if (action == 2) 
        {
        	return;
        }

        for (int i = 0; i < respList.size(); i++) 
        {
            System.out.println("[" + (i+1) + "] Response from " + respList.get(i).getRespond());
        }

        int rIndex = system.validate().menuChoice("Select response: ", respList.size());
        HelpdeskResponse target = respList.get(rIndex - 1);

        int rating = system.validate().menuChoice("Rate 1–5: ", 5);

        boolean ok = hrm.updateRating(
            target.getTicketId(),
            target.getRespond(),
            target.getTime(),
            rating
        );

        if (ok) 
        {
            System.out.println("Thank you! Your rating has been recorded.");
        } 
        else 
        {
            System.out.println("Error saving rating.");
        }
    }


   private void accountMan() {
        
        while (true) 
        {

            System.out.println("=== Manage Account ===");
            System.out.println("[1] Change Username");
            System.out.println("[2] Change Password");
            System.out.println("[3] Delete Account");
            System.out.println("[4] Go Back");

            int choice = system.validate().menuChoice("Select: ", 4);

            if (choice == 1) 
            {
            	changeUser();
            }
            
            else if (choice == 2)
            {
            	changePassword();
            }
            
            else if (choice == 3)
            {
            	deleteAccount();
            }
            
            else if (choice == 4) 
            {
            	return;
            }

        }

    }

    private void changeUser() {
        while (true) {

        String user = system.validate().requireText("New Username or X to go back: ");

        if (user.matches("[xX]")) {
            break;
        }

        if (system.userManager().usernameExists(user)) {
            System.out.println("Username already exists.");
            continue;
        }

        System.out.println("= Confirm =");
        System.out.println("Former Username: " + record.getUsername());
        System.out.println("New Username: " + user);

        String confirm = system.validate().editCancelContinue();

        if (confirm.equals("EDIT")) {
            continue;
        } 

        else if (confirm.equals("CANCEL")) {
            return;
        }

        System.out.println("Sucessfully changed username");
        system.userManager().changeUser(record.getUsername(), user);
        return;

        }
    }

    private void changePassword() {
        while (true) {

            String pass = system.validate().requireText("New Password or X to go back: ");
            if (pass.matches("[xX]")) {
                break;
            }

            String conPass = system.validate().requireText("Confirm Password (Type 'x' to go back) : "); // Confirm password
            if (conPass.matches("[xX]")) {
                continue;
            }

            // Confirms if password and confirmed password is the same
            if (!pass.equals(conPass)) {
                System.out.println("Incorrect password, type your password again to confirm");
                continue;
            }

            boolean confirm = system.validate().confirm("Confirm change password? ");

            if (confirm) system.userManager().changePass(record.getUsername(), pass);
            else continue;
            System.out.println("Sucessfully changed password");
            return;
        }
    }

    private void deleteAccount() 
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

        } 
    }
}
