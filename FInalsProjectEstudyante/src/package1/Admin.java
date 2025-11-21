package package1;

import java.util.*;

public class Admin extends User 
{

    private UserManager um;

    public Admin(MainSystem system, UserRecord record) {
        super(system, record);
        this.um = system.userManager();
    }

    @Override
    public void start() {
        while (true) {
            System.out.println("=== ADMIN MENU ===");
            System.out.println("[1] View Users");
            System.out.println("[2] Add Employee");
            System.out.println("[3] Respond to Concern");
            System.out.println("[4] View All Helpdesk Responses");
            System.out.println("[5] Delete User");
            System.out.println("[6] Logout");
            int choice = system.validate().menuChoice("Choose: ", 6);
            
            if (choice == 1)
            {
            	viewUsers();
            }
            
            else if (choice == 2)
            {
            	addEmployee();
            }
            
            else if (choice == 3)
            {
            	respondToTicketAdmin();
            }
            
            else if (choice == 4)
            {
            	viewAllResponses();
            }
            
            else if (choice == 5)
            {
            	deleteUser();
            }
            
            else if (choice == 6)
            {
            	return;
            }
        }
    }

    private void viewUsers() 
    {
        List<UserRecord> list = um.loadAll();
        
        System.out.println("=== LIST OF USERS ===");
        
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

            user = system.validate().requireText("New username (Input 'x' to go back) : ");
            if (user.equalsIgnoreCase("x")) return;

            if (um.usernameExists(user)) {
                System.out.println("Username exists.");
                continue;
            }
            
            Boolean back = false;
            while (true) {

                pass = system.validate().requireText("Password (Input 'x' to go back): ");
                if (pass.equalsIgnoreCase("x")) {
                    back = true;
                    break; 
                }

                System.out.println("[1] Cashier [2] Registrar [3] Accounting [4] Admin, [5] to go back)"); //TODO: change the promgt
                int r = system.validate().menuChoice("Choose role: ", 5);

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

            name = system.validate().requireText("Full name: ");

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
        
        System.out.println("Employee created.");
    }
    
    private void respondToTicketAdmin() 
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
                
                System.out.println("[" + (i+1) + "] ID:" + t.getId() + " (" + t.getStudentNum() + ") " + t.getIssue() + " | Status: " + t.getStatus());
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

    private void viewAllResponses() 
    {
        List<HelpdeskResponse> rlist = system.helpdeskResponseManager().loadAll();
        
        if (rlist.size() == 0) 
        {
            System.out.println("No helpdesk responses yet.");
            
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
        String user = system.validate().requireText("Username to delete: ");
        
        um.deleteUser(user);
        
        System.out.println("Deleted.");
    }
}

