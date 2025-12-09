package package1;

import displays.*;
import java.util.*;

public class Admin extends User 
{

    private UserManager um;
    private QueueSystem qs;

    public Admin(MainSystem system, UserRecord record) {
        super(system, record);
        this.um = system.userManager();
        this.qs = system.queueSystem();
    }

    @Override
    public void start() {
        while (true) {
            ShowAdminMenuDisplay.AdminMenuDisplay();
            System.out.println("");
            int choice = system.validate().menuChoice("                                        Choose: ", 7     );
            
            if (choice == 1)
            {
                System.out.println("\n                                        \u001B[32m- Loading users\u001B[0m");
                System.out.println("\n                                        -----------------------------------------------------------------------------------------------------------------------------\n");
            	viewUsers();
            }
            
            else if (choice == 2)
            {
                System.out.println("\n                                        \u001B[32m- Going to add employee menu\u001B[0m");
                System.out.println("\n                                        -----------------------------------------------------------------------------------------------------------------------------\n");
            	addEmployee();
            }
            
            else if (choice == 3)
            {
                System.out.println("\n                                        \u001B[32m- Going to ADMIN helpdesk response\u001B[0m");
                System.out.println("\n                                        -----------------------------------------------------------------------------------------------------------------------------\n");
            	respondToTicketAdmin();
            }
            
            else if (choice == 4)
            {
                System.out.println("\n                                        \u001B[32m- Loading helpdesk responses\u001B[0m");
                System.out.println("\n                                        -----------------------------------------------------------------------------------------------------------------------------\n");
            	viewAllResponses();
            }
            
            else if (choice == 5) {
                System.out.println("\n                                        \u001B[32m- Going to ADMIN history menu\u001B[0m");
                System.out.println("\n                                        -----------------------------------------------------------------------------------------------------------------------------\n");
                AdminHistory ah = new AdminHistory();
            }

            else if (choice == 6)
            {
                System.out.println("\n                                        \u001B[32m- Going to delete user menu\u001B[0m");
                System.out.println("\n                                        -----------------------------------------------------------------------------------------------------------------------------\n");
            	deleteUser();
            }
            
            else if (choice == 7)
            {
                System.out.println("\n                                        \u001B[32m- Returning...\u001B[0m");
                System.out.println("\n                                        -----------------------------------------------------------------------------------------------------------------------------\n");
            	return;
            }
        }
    }

    private void viewUsers() 
    {
        List<UserRecord> list = um.loadAll();
        
        ShowAdminMenuDisplay.ListOfUsersDisplay();
        
        System.out.println("- Students -");
        displayUser(UserRecord.Role.STUDENT, list);

        System.out.println("- Employees -");
        displayUser(UserRecord.Role.CASHIER, list);
        displayUser(UserRecord.Role.REGISTRAR, list);
        displayUser(UserRecord.Role.ACCOUNTING, list);

        System.out.println("- Administrators -");
        displayUser(UserRecord.Role.ADMIN, list);

    }

    private void displayUser(UserRecord.Role role, List<UserRecord> list) {
        for (UserRecord u : list) {
            if (u.getRole().equals(role)) {
                System.out.println("- " + u.getUsername() + " (" + u.getRole() + ")");
            }
        }
    }

    private void addEmployee() 
    {
        String user, pass, name;
        UserRecord.Role role = null;

        while (true) {

            user = system.validate().requireText("                                        New username (Input 'x' to go back) : ");
            if (user.equalsIgnoreCase("x")) return;

            if (um.usernameExists(user)) {
                System.out.println("                                        \u001B[31mUsername already exists.\u001B[0m");
                continue;
            }
            
            Boolean back = false;
            while (true) {

                pass = system.validate().requireText("                                        Password (Input 'x' to go back): ");
                if (pass.equalsIgnoreCase("x")) {
                    back = true;
                    break; 
                }

                System.out.println("                                        [1] Cashier [2] Registrar [3] Accounting [4] Admin, [5] to go back)"); 
                int r = system.validate().menuChoice("                                        Choose role: ", 5);

                if (r == 1) {
                    role = UserRecord.Role.CASHIER;
                } else if (r == 2) {
                    role = UserRecord.Role.REGISTRAR;
                } else if (r == 3) {
                    role = UserRecord.Role.ACCOUNTING;
                } else if (r == 4) {
                    role = UserRecord.Role.ADMIN;
                } else if (r == 5) continue;

                break;
            }

            if (back) continue;

            name = system.validate().requireText("                                        Full name: ");

            System.out.println("= CONFIRM EMPLOYEE CREATION: =");
            System.out.println("- Username: " + user);
            System.out.println("- Password: " + pass);
            System.out.println("- Position: " + role);
            System.out.println("- Full Name: " + name);

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
        
        um.addUser(new UserRecord(user, pass, role, name));
        
        System.out.println("                                                                  ╔════════════════════════════════════════════════════════════════════╗");
        System.out.println("                                                                  ║                          \u001B[32mEmployee created\u001B[0m                          ║");
        System.out.println("                                                                  ╚════════════════════════════════════════════════════════════════════╝");
    }
    
    public void respondToTicketAdmin()
    {
    	while (true)
    	{
        List<HelpdeskTicket> tickets = system.helpdeskManager().loadTickets();
        if (tickets.size() == 0)
        {
            System.out.println("                                        \u001B[31mNo tickets.\u001B[0m");
            return;
        }

        System.out.println(" ".repeat(27) +"=== SELECT TICKET TO RESPOND / ENDORSE ===");

        System.out.printf(" ".repeat(27) +"╔" + "═".repeat(148) + "╗%n");

        System.out.printf(" ".repeat(27) + "║ %-4s ║ %-8s ║ %-14s ║ %-30s ║ %-24s ║ %-24s ║ %-24s ║%n",
            "No.", "TicketID", "Student", "Issue","Last Modified", "Window", "Status");

        System.out.printf(" ".repeat(27) + "║" + "─".repeat(148) + "║%n");

        int idx = 1;
        
        for (HelpdeskTicket t : tickets) 
        {
            String window = t.getAssignedWindow();

            if (window == null) 
            {
                window = "";
            }
            
            String lastModified = t.getDate();
            
            if (lastModified == null) 
            {
                lastModified = "";
            }

            System.out.printf(" ".repeat(27) + "║ %-4d ║ %-8d ║ %-14s ║ %-30s ║ %-24s ║ %-24s ║ %-24s ║%n", idx, t.getId(), t.getStudentNum(), t.getIssue(), lastModified, window, t.getStatus());
            idx++;
        }

        System.out.printf(" ".repeat(27) + "╚" + "═".repeat(148) + "╝%n");

        String selStr = system.validate().requireText(" ".repeat(27) +"Select ticket (or X to go back): ");
        
        if (selStr.equalsIgnoreCase("X")) return;

        int sel;
        try 
        {
            sel = Integer.parseInt(selStr);
            if (sel < 1 || sel > tickets.size()) 
            {
                System.out.println(" ".repeat(27) +"Invalid selection. Try again.");
                continue;
            }
        }
        catch (NumberFormatException e) 
        {
            System.out.println(" ".repeat(27) +"Invalid input. Try again.");
            continue;
        }

            
        HelpdeskTicket chosen = tickets.get(sel - 1);

        while (true) 
        {
            String action = system.validate().requireText(" ".repeat(27) + "Type [E]ndorse to window, [X] to go back: ");
            
            if (action.equalsIgnoreCase("X")) 
            {
                break;  
            }

            if (action.equalsIgnoreCase("E")) 
            {
                while (true) 
                {
                    System.out.println(" ".repeat(27) + "[1] Cashier [2] Accounting [3] Registrar [X] Go back");
                    String windowSel = system.validate().requireText(" ".repeat(27) +"Choose window: ");

                    if (windowSel.equalsIgnoreCase("X")) break;

                    int w;
                    try 
                    {
                        w = Integer.parseInt(windowSel);
                        if (w < 1 || w > 3) 
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

                    String window = "";
                    if (w == 1) window = "CASHIER";
                    if (w == 2) window = "ACCOUNTING";
                    if (w == 3) window = "REGISTRAR";

                    String date = system.genDate();
                    boolean ok = system.helpdeskManager().endorseTicket(chosen.getId(), window, date);

                
                if (ok) 
                {
                    System.out.println("                                                                  ╔════════════════════════════════════════════════════════════════════╗");
                    System.out.printf("                                                                  ║            \u001B[32mSucessfully endorsed ticket to %-15s\u001B[0m          ║%n", window);
                    System.out.println("                                                                  ╚════════════════════════════════════════════════════════════════════╝");
                    tickets = system.helpdeskManager().loadTickets();
                    
                    System.out.println("\n                                        -----------------------------------------------------------------------------------------------------------------------------\n");
                    
                    
                    System.out.printf(" ".repeat(27) +"╔" + "═".repeat(148) + "╗%n");
                    System.out.printf(" ".repeat(27) +"║ %-4s ║ %-8s ║ %-14s ║ %-30s ║ %-24s ║ %-24s ║ %-24s ║%n",
                        "No.", "TicketID", "Student", "Issue","Last Modified", "Window", "Status");
                    System.out.printf(" ".repeat(27) +"║" + "─".repeat(148) + "║%n");
                    idx = 1;
                    for (HelpdeskTicket t : tickets) 
                    {
                        String win = t.getAssignedWindow();
                        if (win == null) win = "";
                        String lastMod = t.getDate();
                        if (lastMod == null) lastMod = "";
                        System.out.printf(" ".repeat(27) +"║ %-4d ║ %-8d ║ %-14s ║ %-30s ║ %-24s ║ %-24s ║ %-24s ║%n", idx, t.getId(), t.getStudentNum(), t.getIssue(), lastMod, win, t.getStatus());
                        idx++;
                    }
                    System.out.printf(" ".repeat(27) +"╚" + "═".repeat(148) + "╝%n");
                    
                    System.out.println("\n                                        -----------------------------------------------------------------------------------------------------------------------------\n");
                    
                    
                    return;
                }  
                
                else 
                {
                    System.out.println("                                        \u001B[31mFailed to endorse ticket.\u001B[0m");
                }
                
                break;
                }
            }
            
            else 
            {
                System.out.println(" ".repeat(27) +"Invalid option.");
            }
        }
    }
}           

    private void viewAllResponses() 
    {
        List<HelpdeskResponse> rlist = system.helpdeskResponseManager().loadAll();
        
        if (rlist.size() == 0) 
        {
            System.out.println("                                        \u001B[31mNo helpdesk responses yet.\u001B[0m");
            return;
        }
        
        System.out.println("=== ALL HELP DESK RESPONSES ===");
        
        for (HelpdeskResponse r : rlist) 
        {
            System.out.println(" ".repeat(69) + "╔" + "═".repeat(61) + "╗");
            System.out.printf( " ".repeat(69) + "║  Ticket ID: %-4d                       %-15s   ║%n", r.getTicketId(), r.getTime());
            System.out.println(" ".repeat(69) + "║" + "─".repeat(61) + "║");
            System.out.println(" ".repeat(69) + "║" + " ".repeat(61) + "║");
            System.out.printf( " ".repeat(69) + "║  From: %-51s  ║%n", r.getRespond());
            System.out.printf( " ".repeat(69) + "║  Message: %-48s  ║%n", r.getMessage());
            
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
        
        System.out.println("\n                                        -----------------------------------------------------------------------------------------------------------------------------\n");
    }

    private void deleteUser() 
    {
        while (true) 
        {
            List<UserRecord> userRecords = um.loadAll();
            
            if (userRecords.isEmpty()) 
            {
                System.out.println("No users to delete.");
                return;
            }
            
           
            System.out.println(" ".repeat(69) +"╔" + "═".repeat(61) + "╗");
            System.out.println(" ".repeat(69) +"║" + " ".repeat(61) + "║");
            System.out.printf(" ".repeat(69) +"║  LIST OF USERS %-43s  ║%n", "");
            System.out.println(" ".repeat(69) +"║" + "─".repeat(61) + "║");
            for (int i = 0; i < userRecords.size(); i++) 
            {
                System.out.printf(" ".repeat(69) +"║  %d. %-54s  ║%n", (i + 1), userRecords.get(i).getUsername());
            }
            System.out.println(" ".repeat(69) +"╚" + "═".repeat(61) + "╝");
            
            String username = system.validate().requireText(" ".repeat(69) +"Username to delete (or X to go back): ");
            
            if (username.equalsIgnoreCase("X")) 
            {
                return;
            }
            
            if (um.usernameExists(username)) 
            {
                String confirm;
                while (true) 
                {
                    confirm = system.validate().requireText(" ".repeat(69) +"Are you sure you want to delete '" + username + "'? (Y/N or Yes/No): \n");
                    
                    if (confirm.equalsIgnoreCase("Y") || confirm.equalsIgnoreCase("Yes")) 
                    {
                        um.deleteUser(username);
                        System.out.println("                                                                  ╔════════════════════════════════════════════════════════════════════╗");
                        System.out.println("                                                                  ║                    \u001B[32mUser was sucessfully deleted\u001B[0m                    ║");
                        System.out.println("                                                                  ╚════════════════════════════════════════════════════════════════════╝");

                        System.out.println("\n                                        -----------------------------------------------------------------------------------------------------------------------------\n");
   
                        userRecords = um.loadAll();
                        System.out.println(" ".repeat(69) +"╔" + "═".repeat(61) + "╗");
                        System.out.println(" ".repeat(69) +"║" + " ".repeat(61) + "║");
                        System.out.printf(" ".repeat(69) +"║  UPDATED LIST OF USERS %-35s  ║%n", "");
                        System.out.println(" ".repeat(69) +"║" + "─".repeat(61) + "║");
                        for (int i = 0; i < userRecords.size(); i++) 
                        {
                            System.out.printf(" ".repeat(69) +"║  %d. %-54s  ║%n", (i + 1), userRecords.get(i).getUsername());
                        }
                        System.out.println(" ".repeat(69) +"╚" + "═".repeat(61) + "╝");
                        return;  
                    }
                    else if (confirm.equalsIgnoreCase("N") || confirm.equalsIgnoreCase("No")) 
                    {
                        System.out.println(" ".repeat(69) + "Deletion cancelled.");
                        System.out.println("\n                                        -----------------------------------------------------------------------------------------------------------------------------\n");
                        break; 
                    }
                    else 
                    {
                    	System.out.println("                                        \u001B[31mInvalid selection. Please Enter Y/N or Yes/No.\u001B[0m");
                    }
                }
            }
            else 
            {
            	System.out.println("                                        \u001B[31mUser does not exist.\u001B[0m");

            }
        }
    }

    public class AdminHistory {

        public AdminHistory() {
            historyMenu();
        }

        public void historyMenu() {

            while (true) {

                ShowAdminMenuDisplay.SeeHistoryAdminDisplay();
                System.out.println("");
                switch (system.validate().menuChoice("Choose: ", 5)) {

                case 1:
                System.out.println("\n                                        \u001B[32m- Loading cashier history\u001B[0m");
                System.out.println("\n                                        -----------------------------------------------------------------------------------------------------------------------------\n");

                ArrayList<String> historyTag = new ArrayList<>();
                historyTag.add("CASHIER");
                historyTag.add("PAUSED");
                history(1, historyTag, true);
                break;

                case 2:
                System.out.println("\n                                        \u001B[32m- Loading accounting history\u001B[0m");
                System.out.println("\n                                        -----------------------------------------------------------------------------------------------------------------------------\n");
                    
                ArrayList<String> historyTag2 = new ArrayList<>();
                historyTag2.add("ACCOUNTING");
                history(1, historyTag2, false);
                break;

                case 3:
                System.out.println("\n                                        \u001B[32m- Loading registrar history\u001B[0m");
                System.out.println("\n                                        -----------------------------------------------------------------------------------------------------------------------------\n");

                ArrayList<String> historyTag3 = new ArrayList<>();
                historyTag3.add("REGISTRAR");
                history(1, historyTag3, false);
                break;

                case 4:
                System.out.println("\n                                        \u001B[32m- Loading request history\u001B[0m");
                System.out.println("\n                                        -----------------------------------------------------------------------------------------------------------------------------\n");

                historyDis();
                break;

                case 5:
                System.out.println("\n                                        \u001B[32m- Returning....\u001B[0m");
                System.out.println("\n                                        -----------------------------------------------------------------------------------------------------------------------------\n");

                return;

                }

            }

        }

        public void history(int page, List<String> statements, boolean showPrice) {

            QueueSystem.HistoryManager hm = qs.new HistoryManager();
            hm.displayViewHistory(1, statements, showPrice);
            int current = 1;

            while (true) {
                int max = system.queueSystem().getHistoryManager().countEntry(statements);
                int input = system.validate().minMaxXChoice("                                        Go to page ('x' to go back):", 1, max);

                if (input == current) {
                    System.out.println("                                        Already on page " + input);
                    continue;
                }

                if (input == -1) {
                    return;
                }

                hm.displayViewHistory(input, statements, showPrice);
                current = input;

            }

        }

        private void historyDis() {
            QueueSystem.HistoryManager hm = qs.new HistoryManager();
            hm.displayViewHistory(1, true);
            int current = 1;

            while (true) {

                int max = system.queueSystem().getHistoryManager().countEntry();
                int input = system.validate().minMaxXChoice("                                        Go to page ('x' to go back):", 1, max);
                if (input == current) {
                    System.out.println("                                        Already on page " + input);
                }
                if (input == -1) {
                    return;
                }

                hm.displayViewHistory(input, true);
                current = input;

            }
        }

    }

}

