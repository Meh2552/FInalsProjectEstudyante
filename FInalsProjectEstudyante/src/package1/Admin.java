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
        List<HelpdeskTicket> tickets = system.helpdeskManager().loadTickets();
        if (tickets.size() == 0)
        {
            System.out.println("                                        \u001B[31mNo tickets.\u001B[0m");
            return;
        }

        while (true)
        {
            System.out.println("=== SELECT TICKET TO RESPOND / ENDORSE ===");

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

            String selStr = system.validate().requireText("                                        Select ticket (or X to go back): ");
            
            if (selStr.equalsIgnoreCase("X")) return;

            int sel;
            
            try 
            {
                sel = Integer.parseInt(selStr);
                if (sel < 1 || sel > tickets.size()) 
                {
                    System.out.println("                                        \u001B[31mInvalid selection. Try again.\u001B[0m");
                    continue;
                }
            } 
            
            catch (NumberFormatException e) 
            {
                System.out.println("                                        \u001B[31mInvalid input. Try again.\u001B[0m");
                continue;
            }
            
            HelpdeskTicket chosen = tickets.get(sel - 1);

            ShowAdminMenuDisplay.RespondToConcernChoice();
            String action = system.validate().requireText("                                        Choose: ");
            if (action.equalsIgnoreCase("C")) return;

            if (action.equalsIgnoreCase("E")) {
                // Endorse menu now accepts X to go back
                while (true) 
                {
                    System.out.println("[1] Cashier [2] Accounting [3] Registrar [X] Go back");
                    String windowSel = system.validate().requireText("Choose window: ");
                    
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
                } 
                else 
                {
                    System.out.println("                                        \u001B[31mFailed to endorse ticket.\u001B[0m");
                }
                
                return;
                }
            }
            
            else if (action.equalsIgnoreCase("R"))
            {
                String respMsg = system.validate().requireText("                                        Type your response message: ");
                String ts = system.genDate();
                String responderName = record.getFullName();
                HelpdeskResponse resp = new HelpdeskResponse(chosen.getId(), responderName, respMsg, ts);
                system.helpdeskResponseManager().addResponse(resp);

                chosen.setStatus("Answered");
                system.helpdeskManager().saveTickets(tickets);

                System.out.println("                                                                  ╔════════════════════════════════════════════════════════════════════╗");
                System.out.println("                                                                  ║              \u001B[32mResponse saved and ticket marked Answered.\u001B[0m            ║");
                System.out.println("                                                                  ╚════════════════════════════════════════════════════════════════════╝");
                return;
            }
            else
            {
                System.out.println("                                        \u001B[31mInvalid option.\u001B[0m");
                continue;
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
            System.out.println("TicketID:" + r.getTicketId() + " | " + r.getRespond() + " " + r.getTime());
            System.out.println("-> " + r.getMessage());
        }
    }

    private void deleteUser() 
    {
        String username = system.validate().requireText("                                        Username to delete: ");
        if (!system.validate().confirm("                                        Confirm delete? ")) return;
        
        if (um.usernameExists(username)) 
        {
            um.deleteUser(username);

            System.out.println("                                                                  ╔════════════════════════════════════════════════════════════════════╗");
            System.out.println("                                                                  ║                    \u001B[32mUser was sucessfully deleted\u001B[0m                    ║");
            System.out.println("                                                                  ╚════════════════════════════════════════════════════════════════════╝");
        }
        else 
        {
            System.out.println("                                        \u001B[31mUser does not exist.\u001B[0m");
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

