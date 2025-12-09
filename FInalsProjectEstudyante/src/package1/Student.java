package package1;

import displays.*;
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
            ShowStudentMenuDisplay.studentMenuDisplay();
            System.out.println("");
            int choice = system.validate().menuChoice("                                        Choose: ", 6);
            
            if (choice == 1) 
            {
                System.out.println("\n                                        \u001B[32m- Loading request document\u001B[0m");
                System.out.println("\n                                        -----------------------------------------------------------------------------------------------------------------------------\n");
            	requestDocument();
            }
            
            else if (choice == 2)
            {
                System.out.println("\n                                        \u001B[32m- Loading request list\u001B[0m");
                System.out.println("\n                                        -----------------------------------------------------------------------------------------------------------------------------\n");
            	viewRequests();
            }
            
            else if (choice == 3)
            {
                System.out.println("\n                                        \u001B[32m- Going to helpdesk\u001B[0m");
                System.out.println("\n                                        -----------------------------------------------------------------------------------------------------------------------------\n");
            	helpdesk();
            }
            
            else if (choice == 4)
            {
                System.out.println("\n                                        \u001B[32m- Loading helpdesk replies\u001B[0m");
                System.out.println("\n                                        -----------------------------------------------------------------------------------------------------------------------------\n");
            	viewHelpdeskReplies();
            }
            
            else if (choice == 5) 
            {
                System.out.println("\n                                        \u001B[32m- Going to account management\u001B[0m");
                System.out.println("\n                                        -----------------------------------------------------------------------------------------------------------------------------\n");
            	accountMan();
            }

            else if (choice == 6) {
                System.out.println("\n                                        \u001B[32m- Returning...\u001B[0m");
                System.out.println("\n                                        -----------------------------------------------------------------------------------------------------------------------------\n");
                return;
            }
        }
    }

    private void requestDocument() 
    {

        // TODO: change the price
        String doc, price = "0";
        
        while (true) {

            ShowStudentMenuDisplay.requestDocumentMenuDisplay();
            System.out.println("");
            int choice = system.validate().menuChoice("                                        Select: ", 5);
        
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
        
        System.out.println("                                                                  ╔════════════════════════════════════════════════════════════════════╗");
        System.out.println("                                                                  ║                         \u001B[32mRequest Submitted!\u001B[0m                         ║");
        System.out.println("                                                                  ╚════════════════════════════════════════════════════════════════════╝");
    }

    private void viewRequests() 
    {
        List<DocumentRequest> all = dm.loadAll();
        ArrayList<DocumentRequest> requestL = new ArrayList<>();
        
        boolean found = false;
        
        ShowStudentMenuDisplay.myRequestDisplay();
        
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
        	System.out.println("                                        \u001B[31mNo requests found.\u001B[0m");
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

                System.out.println    ("                                                                       ╔═════════════════════════════════════════════════════════════╗");
                System.out.printf("                                                                       ║  Request ID: %-10s               %20s  ║%n", request.getId(), request.getDate());
                System.out.println    ("                                                                       ║─────────────────────────────────────────────────────────────║");
                System.out.println    ("                                                                       ║                                                             ║");
                System.out.printf("                                                                       ║  Document Requested: %-30s         ║%n", request.getDocName());
                System.out.printf("                                                                       ║  State: %-30s                      ║%n", request.getStatus());
                System.out.println    ("                                                                       ║                                                             ║");
                System.out.println    ("                                                                       ╚═════════════════════════════════════════════════════════════╝");

            }
            System.out.println("\n                                        -----------------------------------------------------------------------------------------------------------------------------\n");

            if (pageCount == 1) System.out.println("                                        - Viewing page " + page);
            else System.out.println("                                        - Viewing page " + page + " out of " + pageCount);

            int input = system.validate().minMaxXChoice("                                        Go to page ('x' to go back):", 1, pageCount);
            if (input == page) {
                System.out.println("                                        Already on page " + input);
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

            ShowStudentMenuDisplay.helpdeskMenuDisplay();
            System.out.println("");

            int choice = system.validate().menuChoice("                                        Select: ", 6);

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

        System.out.println("                                                                  ╔════════════════════════════════════════════════════════════════════╗");
        System.out.printf("                                                                  ║             \u001B[32mHelpdesk ticket created. Ticket ID: %04d!\u001B[0m              ║%n", id);
        System.out.println("                                                                  ╚════════════════════════════════════════════════════════════════════╝");
                                                
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
            System.out.println("                                        \u001B[31mYou have no helpdesk tickets.\u001B[0m");
            return;
        }

       
        while (true) 
        {
        	ShowStudentMenuDisplay.helpdeskRepliesDisplay();

            System.out.printf(" ".repeat(41) +"╔" + "═".repeat(121) + "╗%n");

            System.out.printf(" ".repeat(41) +"║ %-4s ║ %-8s ║ %-14s ║ %-30s ║ %-24s ║ %-24s ║%n",
                "No.", "TicketID", "Student", "Issue", "Last Modified", "Status");

            System.out.printf(" ".repeat(41) +"║" + "─".repeat(121) + "║%n");
            
            int idx = 1;
            
            for (HelpdeskTicket t : myTickets)  
            {
                String lastModified = t.getDate();
                if (lastModified == null)
                {
                    lastModified = " ";
                }
                System.out.printf(" ".repeat(41) +"║ %-4d ║ %-8d ║ %-14s ║ %-30s ║ %-24s ║ %-24s ║%n", idx, t.getId(), t.getStudentNum(), t.getIssue(), lastModified, t.getStatus());
                idx++;
            }

            System.out.printf(" ".repeat(41) +"╚" + "═".repeat(121) + "╝%n");
            
            int choose = system.validate().minMaxXChoice(" ".repeat(41) +"Select ticket to view (X to go back): ", 1, myTickets.size());

            if (choose == -1) return;

            HelpdeskTicket selected = myTickets.get(choose - 1);   
        
            while (true)  
            {
                ArrayList<HelpdeskResponse> respList = new ArrayList<>();
           
                System.out.println("\n " + " ".repeat(69) +"--- RESPONSES ---"); // need i box
           
                for (HelpdeskResponse r : responses) 
                {
                    if (r.getTicketId() == selected.getId()) 
                    {
                        respList.add(r);

                        System.out.println(" ".repeat(69) +"╔" + "═".repeat(61) + "╗");
                        System.out.printf(" ".repeat(69) +"║  Ticket ID: %-4d                       %-15s   ║%n", selected.getId(), r.getTime());
                        System.out.println(" ".repeat(69) +"║" + "─".repeat(61) + "║");
                        System.out.println(" ".repeat(69) +"║" + " ".repeat(61) + "║");
                        System.out.printf(" ".repeat(69) +"║  From: %-51s  ║%n", r.getRespond());
                        System.out.printf(" ".repeat(69) +"║  Message: %-48s  ║%n", r.getMessage());
                        
                        if (r.getRating() > 0) 
                        {
                            System.out.printf(" ".repeat(69) +"║  Rating: %-49s  ║%n", r.getRating());
                        }
                        else 
                        {
                            System.out.printf(" ".repeat(69) +"║  Rating: %-49s  ║%n", "Not yet rated");
                        }
                        System.out.println(" ".repeat(69) +"║" + " ".repeat(61) + "║");
                        System.out.println(" ".repeat(69) +"╚" + "═".repeat(61) + "╝");
                    }
                }

                if (respList.isEmpty()) 
                {
                    System.out.println(" ".repeat(69) +"No responses yet.");
                    break; 
                }

                System.out.println(" ".repeat(69) +"\nRATE A RESPONSE");
                System.out.println(" ".repeat(69) +"[1] Rate a response");
                System.out.println(" ".repeat(69) +"[x] Go Back");
                String choice = system.validate().requireText(" ".repeat(69) +"Choose: ");  
                if (choice.equalsIgnoreCase("x")) 
                {
                    break;  
                } 
                else if (choice.equals("1")) 
                {
                    for (int i = 0; i < respList.size(); i++) 
                    {
                        System.out.println(" ".repeat(69) +"[" + (i + 1) + "] Response from " + respList.get(i).getRespond());
                    }
                    while (true) 
                    {
                        String selStr = system.validate().requireText(" ".repeat(69) +"Select response (or X to go back): ");
                        
                        if (selStr.equalsIgnoreCase("x")) 
                        {
                            break;  
                        }
                        
                        int rIndex;
                        try 
                        {
                            rIndex = Integer.parseInt(selStr);
                            if (rIndex < 1 || rIndex > respList.size()) 
                            {
                            	System.out.println("                                        \u001B[31mInvalid selection. Try again.\u001B[0m");

                                continue;
                            }
                        }
                        catch (NumberFormatException e) 
                        {
                        	System.out.println("                                        \u001B[31mInvalid selection. Try again.\u001B[0m");

                            continue;
                        }
                        
                        HelpdeskResponse target = respList.get(rIndex - 1);
                        
                        if (target.getRating() > 0) 
                        {
                            String updateChoice = "";
                            while (true) 
                            {
                                System.out.println(" ".repeat(69) +"This response is already rated with " + target.getRating() + ". Do you want to update it? (y/n)");
                                updateChoice = system.validate().requireText(" ".repeat(69) +"Choose: ");
                                if (updateChoice.equalsIgnoreCase("y") || updateChoice.equalsIgnoreCase("n")) 
                                {
                                    break;
                                } 
                                else 
                                {
                                    System.out.println("Invalid choice. Please enter y or n.");
                                }
                            }
                            if (updateChoice.equalsIgnoreCase("n")) 
                            {
                                continue;
                            }
                        }
                        
                        int rating = system.validate().menuChoice(" ".repeat(69) +"Rate 1–5: ", 5);

                        boolean ok = hrm.updateRating(
                            target.getTicketId(),
                            target.getRespond(),
                            target.getTime(),
                            rating
                        );

                        if (ok) 
                        {
                            System.out.println("Thank you! Your rating has been recorded.");
                            responses = hrm.loadAll();  
                            break;  
                        } 
                        else 
                        {
                            System.out.println("Error saving rating.");
                            break; 
                        }
                    }
                }
                else 
                {
                    System.out.println("Invalid choice. Please enter 1 or x.");              
                }
            }
        }
    }


   private void accountMan() {
        
        while (true) 
        {

            ShowStudentMenuDisplay.ManageAccountDisplay();
            System.out.println("");
            int choice = system.validate().menuChoice("                                        Select: ", 4);

            if (choice == 1) 
            {
                System.out.println("\n                                        \u001B[32m- Going to username change menu\u001B[0m");
                System.out.println("\n                                        -----------------------------------------------------------------------------------------------------------------------------\n");
            	changeUser();
            }
            
            else if (choice == 2)
            {
                System.out.println("\n                                        \u001B[32m- Going to password change menu\u001B[0m");
                System.out.println("\n                                        -----------------------------------------------------------------------------------------------------------------------------\n");
            	changePassword();
            }
            
            else if (choice == 3)
            {
                System.out.println("\n                                        \u001B[32m- Going to delete account menu\u001B[0m");
                System.out.println("\n                                        -----------------------------------------------------------------------------------------------------------------------------\n");
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

        String user = system.validate().requireText("                                        New Username or X to go back: ");

        if (user.matches("[xX]")) {
            break;
        }

        if (system.userManager().usernameExists(user)) {
            System.out.println("                                        \u001B[31mUsername already exists.\u001B[0m");
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

        System.out.println("                                                                  ╔════════════════════════════════════════════════════════════════════╗");
        System.out.println("                                                                  ║                    \u001B[32mSucessfully changed username\u001B[0m                    ║");
        System.out.println("                                                                  ╚════════════════════════════════════════════════════════════════════╝");
        system.userManager().changeUser(record.getUsername(), user);
        record.setUsername(user);
        return;

        }
    }

    private void changePassword() {
        while (true) {

            String pass = system.validate().requireText("                                       New Password or X to go back: ");
            if (pass.matches("[xX]")) {
                break;
            }

            String conPass = system.validate().requireText("                                       Confirm Password (Type 'x' to go back) : "); // Confirm password
            if (conPass.matches("[xX]")) {
                continue;
            }

            // Confirms if password and confirmed password is the same
            if (!pass.equals(conPass)) {
                System.out.println("                                        \u001B[31mIncorrect password, type your password again to confirm\u001B[0m");
                continue;
            }

            boolean confirm = system.validate().confirm("                                       Confirm change password? ");

            if (confirm) system.userManager().changePass(record.getUsername(), pass);
            else continue;
            System.out.println("                                                                  ╔════════════════════════════════════════════════════════════════════╗");
            System.out.println("                                                                  ║                    \u001B[32mSucessfully changed password\u001B[0m                    ║");
            System.out.println("                                                                  ╚════════════════════════════════════════════════════════════════════╝");
            return;
        }
    }

    private void deleteAccount() 
    {
        String user = record.getUsername();
        System.out.println("= CONFIRM DELETE ACCOUNT =");
        System.out.println(" - User: " + user);
        System.out.println("= THIS USER WILL BE DELETED ");


        if (system.validate().confirm("                                        Are you sure? ")) 
        { //1st confirm

            // second con
            if (system.validate().confirm("                                        \\u001B[31mWarning: this user will be permanently deleted, you will be put back onto the login screen. Confirm? \\u001B"))  
            {
                system.userManager().deleteUser(user);
                System.out.println("                                                                  ╔════════════════════════════════════════════════════════════════════╗");
                System.out.printf("                                                                  ║ \u001B[32m%40s was deleted\u001B[0m               ║%n", "User: " + user);
                System.out.println("                                                                  ╚════════════════════════════════════════════════════════════════════╝");
                new UserAuth(system).start();
            }

        } 
    }
}
