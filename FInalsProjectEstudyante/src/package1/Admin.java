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
        
        for (UserRecord u : list) 
        {
            System.out.println("- " + u.getUsername() + " (" + u.getRole() + ")");
        }
    }

    private void addEmployee() 
    {
        String user = system.validate().requireText("New username: ");
        
        if (um.usernameExists(user)) 
        {
            System.out.println("Username exists.");
            return;
        }
        
        String pass = system.validate().requireText("Password: ");
        
        System.out.println("[1] Cashier [2] Registrar [3] Accounting");
        int r = system.validate().menuChoice("Choose role: ", 3);
        
        UserRecord.Role role = null;
        
        if (r == 1)
        {
        	 role = UserRecord.Role.CASHIER;
        }
        
        else if (r == 2) 
        {
        	role = UserRecord.Role.REGISTRAR;
        }
        
        else if (r == 3) 
        {
        	role = UserRecord.Role.ACCOUNTING;
        }
        
        String name = system.validate().requireText("Full name: ");
        
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

