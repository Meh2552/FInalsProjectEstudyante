package package1;

public class UserAuth 
{

    private MainSystem system;

    public UserAuth(MainSystem system) 
    {
        this.system = system;
    }

    public void start() 
    {
        while (true) 
        {
            System.out.println("=== E-STUDYANTE ===");
            System.out.println("[1] Sign Up");
            System.out.println("[2] Login");
            System.out.println("[3] Exit");
            int c = system.validate().menuChoice("Choose: ", 3);
            
            if (c == 1) 
            {
            	signupStudent();
            }
            
            else if (c == 2)
            {
            	login();
            }
            
            else if (c == 3)
            {
                System.out.println("Thank you for using E-STUDYANTE! ");
                System.exit(0);
            }
        }
    }

    private void signupStudent() 
    {
        System.out.println("=== SIGN UP ===");
        String user = system.validate().requireText("Username: ");
        String pass = system.validate().requireText("Password: ");
        if (system.userManager().usernameExists(user)) 
        {
            System.out.println("Username exists.");
            return;
        }
        
        String stNum;
        while (true) 
        {
            stNum = system.validate().requireText("Student Number (11 digits): ");
            
            if (!stNum.matches("[0-9]{11}")) 
            {
                System.out.println("Invalid. Must be 11 digits.");
                continue;
            }
            
            if (system.userManager().studentNumExists(stNum)) 
            {
                System.out.println("Student number used.");
                continue;
            }
            
            break;
        }
        
        String name = system.validate().requireText("Full name: ");
        
        UserRecord r = new UserRecord(user, pass, stNum, name);
        system.userManager().addUser(r);
        
        System.out.println("Account created.");
    }

    private void login() 
    {
        System.out.println("=== LOGIN ===");
        
        String user = system.validate().requireText("Username: ");
        String pass = system.validate().requireText("Password: ");
        
        UserRecord record = system.userManager().verify(user, pass);
        
        if (record == null) 
        {
            System.out.println("Wrong credentials.");
            return;
        }
        
        launchUser(record);
    }

    private void launchUser(UserRecord record) 
    {
        User u = null;
        if (record.getRole() == UserRecord.Role.STUDENT) 
        {
        	u = new Student(system, record);
        }
        
        else if (record.getRole() == UserRecord.Role.ADMIN)
        {
        	u = new Admin(system, record);
        }
        
        else if (record.getRole() == UserRecord.Role.CASHIER) 
        {
        	u = new Cashier(system, record);
        }
        
        else if (record.getRole() == UserRecord.Role.REGISTRAR)
        {
        	u = new Registrar(system, record);
        }
        
        else if (record.getRole() == UserRecord.Role.ACCOUNTING)
        {
        	u = new Accounting(system, record);
        }
        
        else 
        {
            System.out.println("invalid role.");
            return;
        }
        
        System.out.println("Welcome, " + record.getFullName());
        
        u.start();
    }
}

