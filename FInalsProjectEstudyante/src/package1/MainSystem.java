package package1;

import java.util.*;

public class MainSystem 
{
	public static void main(String[] args) 
	{
		MainSystem system = new MainSystem();
		UserAuth ua = new UserAuth(system);
		
		ua.start();
		
	}
	
	Scanner scan = new Scanner (System.in);
	
	public Scanner scan()
	{
		return scan;
	}
	
	 private Validator validate;
	 private UserManager userManager;
	 private DocumentManager documentManager;
	 private HelpdeskManager helpdeskManager;

	 public Validator validate()
	 { 
		 return validate; 
	 }
	 
	 public UserManager userManager() 
	 { 
		 return userManager; 
	 }
	 
	 public DocumentManager documentManager() 
	 { 
		 return documentManager; 
	 }
	 
	 public HelpdeskManager helpdeskManager() 
	 {
		 return helpdeskManager;
	 }

	 public MainSystem() 
	 {
		 this.userManager = new UserManager();
		 this.documentManager = new DocumentManager();
		 this.helpdeskManager = new HelpdeskManager();
		 this.validate = new Validator(this);

		 userManager.ensure();
		 documentManager.ensure();
		 helpdeskManager.ensure();
	    }
}
