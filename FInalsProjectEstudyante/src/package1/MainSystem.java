package package1;

import java.time.*;
import java.time.format.*;
import java.util.*;

// TODO: kailangan ensure yung new files, idk how 2 tho
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
		this.queueSystem = new QueueSystem();

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
	private QueueSystem queueSystem;
	
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

	public QueueSystem queueSystem() {
		return queueSystem;
	}

	 
	 
	public String genDate() 
	{

		LocalDateTime time = LocalDateTime.now();
		DateTimeFormatter form1 = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		DateTimeFormatter form2 = DateTimeFormatter.ofPattern("HH:mm");

		String formDate = "[" + time.format(form1) + "] " + time.format(form2);
		return formDate;
	        
	}

	public String genDate(int days) {

        LocalDate date = LocalDate.now();
		date = date.plusDays(days);
		DateTimeFormatter form = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        return form.format(date);

    }

}
