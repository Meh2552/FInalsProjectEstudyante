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

        boolean isSignUp = true;
        String user = "", pass = "", conPass, stNum = "", name = "";


        int step = 1;

        do 
        { 
            switch (step) {

                // Username
                case 1:
                System.out.println("=== SIGN UP ===");
                user = system.validate().requireText("Username (Type 'x' to go back) : ");
            
                if (user.matches("[xX]")) 
                {
                    return;
                }

                // Confirms if username is already in the system
                if (system.userManager().usernameExists(user)) {
                    System.out.println("Username exists.");
                    continue;
                }

                step++;
                break;


                // Password
                case 2:
                pass = system.validate().requireText("Password (Type 'x' to go back) : ");
                if (pass.matches("[xX]")) {
                    step--;
                    break;
                }
                step++;
                break;


                // Confirm Passowrd
                case 3:
                conPass = system.validate().requireText("Confirm Password (Type 'x' to go back) : "); // Confirm password
                if (conPass.matches("[xX]")) {
                    step--;
                    break;
                }

                // Confirms if password and confirmed password is the same
                if (pass.equals(conPass)) step++;
                else System.out.println("Incorrect password, type your password again to confirm");
                break;


                // Full Name
                case 4:

                // Keeps generating random student numbers until one that isn't occupied
                do {
                    stNum = system.userManager().studentNumGenerate();
                } while (system.userManager().studentNumExists(stNum));
                // End of pass loop

                name = system.validate().requireText("Full name: (X to go back) ");
                if (name.matches("[xX]")) {
                    step = 2;
                    break;
                }
                step++;
                break;


                // Confirm
                case 5:
                System.out.println("= Confirm =");
                System.out.println("Username: " + user);
                System.out.println("Full name: " + name);
                System.out.println("Generated Student Number: " + stNum);

                String confirm = system.validate().editCancelContinue();
                if (confirm.equals("EDIT")) {
                    step = 1;
                    continue;
                } 
                
                else if (confirm.equals("CANCEL")) {
                    return;
                }

                step++;
                break;
            }
        } 
        
        while (step <= 5); // end of signup loop
        
        UserRecord r = new UserRecord(user, pass, stNum, name);
        system.userManager().addUser(r);
        
        System.out.println("Account created.");
    }

    private void login() 
    {
        // Login loop
        while (true) { 

            System.out.println("=== LOGIN ===");

            // Input prompt loop
            String user, pass;
            while (true) {

                user = system.validate().requireText("Username (Type 'x' to go back) : ");
                if (user.matches("[xX]")) {
                    return;
                }

                pass = system.validate().requireText("Password (Type 'x' to go back) : ");
                if (pass.matches("[xX]")) {
                    continue;
                }

                break;
            }

            UserRecord record = system.userManager().verify(user, pass);

            if (record == null) {
                System.out.println("Wrong credentials.");
                continue;
            }

            launchUser(record);
            return;
        }

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

