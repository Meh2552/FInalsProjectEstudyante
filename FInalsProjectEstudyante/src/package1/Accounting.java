package package1;

import java.util.*;

public class Accounting extends Employee 
{

    private DocumentManager dm;

    public Accounting(MainSystem system, UserRecord record) 
    {
        super(system, record);
        this.dm = system.documentManager();
    }

    @Override
    public void employeeMenu() 
    {
        while (true) 
        {
            System.out.println("\n=== ACCOUNTING MENU ===");
            System.out.println("[1] View Paid Requests");
            System.out.println("[2] Approve/Reject Payment");
            System.out.println("[3] Logout");
            int c = system.validate().menuChoice("Choose: ", 3);
            
            if (c == 1)
            {
            	viewPaid();
            }
            
            else if (c == 2)
            {
            	processPayment();
            }
            
            else if (c == 3)	
            {
            	return;
            }
        }
    }

    private void viewPaid() 
    {
        List<DocumentRequest> all = dm.loadAll();
        
        boolean found = false;
        
        System.out.println("=== PAID ===");
        
        for (DocumentRequest req : all)
        {
            if (req.getStatus().equals("Paid")) 
            {
                System.out.println("- " + req.getDocName() + " | " + req.getStudentNum());
                found = true;
            }
        }
        if (!found) System.out.println("No paid requests.");
    }

    private void processPayment() 
    {
        List<DocumentRequest> all = dm.loadAll();
        
        ArrayList<DocumentRequest> paid = new ArrayList<DocumentRequest>();
        
        for (DocumentRequest r : all) 
        {
            if ("Paid".equals(r.getStatus())) paid.add(r);
        }
        
        if (paid.size() == 0) 
        {
            System.out.println("No paid requests.");
            return;
        }
        
        for (int i = 0; i < paid.size(); i++) 
        {
            System.out.println("[" + (i+1) + "] " + paid.get(i).getDocName() + " (" + paid.get(i).getStudentNum() + ")");
        }
        
        int sel = system.validate().menuChoice("Select: ", paid.size());
        
        DocumentRequest req = paid.get(sel - 1);
        
        System.out.println("[1] Approve [2] Reject");
        int ans = system.validate().menuChoice("Choose: ", 2);
        
        if (ans == 1)
        {
        	req.setStatus("Approved");	
        }
        else 
        {
        	req.setStatus("Denied");
        }
        
        dm.saveAll(all);
        
        System.out.println("Updated.");
    }
}
