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
            System.out.println("[3] Delete User");
            System.out.println("[4] Helpdesk");
            System.out.println("[5] Logout");
            int choice = system.validate().menuChoice("Choose: ", 4);
            
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
            	deleteUser();
            }
            
            else if (choice == 4)
            {
            	//helpdesk to wala pa
            }
            
            else if (choice == 5)
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

    private void deleteUser() 
    {
        String user = system.validate().requireText("Username to delete: ");
        
        um.deleteUser(user);
        
        System.out.println("Deleted.");
    }
}

