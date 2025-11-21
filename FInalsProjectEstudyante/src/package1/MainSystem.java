package package1;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class MainSystem 
{
	public static void main(String[] args) 
	{
		MainSystem system = new MainSystem();
		UserAuth ua = new UserAuth(system);
		
		ua.start();
		
	}
	
	public MainSystem() 
	{
		this.scan = new Scanner (System.in);
        this.userManager = new UserManager();
        this.documentManager = new DocumentManager();
        this.helpdeskManager = new HelpdeskManager();
        this.helpdeskResponseManager = new HelpdeskResponseManager();
        this.validate = new Validator(this);

        userManager.ensure();
        documentManager.ensure();
        helpdeskManager.ensure();
        helpdeskResponseManager.ensure();
    }
	
	private Scanner scan;
	private Validator validate;
	private UserManager userManager;
	private DocumentManager documentManager;
	private HelpdeskManager helpdeskManager;
	private HelpdeskResponseManager helpdeskResponseManager;
	
	public Scanner scan()
	{
		return scan;
	}
	
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
	
	public HelpdeskResponseManager helpdeskResponseManager()
	{
		return helpdeskResponseManager;
	}

	 
	 
	public String genDate() 
	{

		LocalDateTime time = LocalDateTime.now();
		DateTimeFormatter form1 = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		DateTimeFormatter form2 = DateTimeFormatter.ofPattern("HH:mm:ss");

		String formDate = "[" + time.format(form1) + "] " + time.format(form2);
		return formDate;
	        
	}

	public String genDate(String pattern) {

        LocalDateTime time = LocalDateTime.now();
        DateTimeFormatter form1 = DateTimeFormatter.ofPattern(pattern);

        String formDate = time.format(form1);
        return formDate;

    }
}
