package package1;

import java.util.*;

public class Cashier extends Employee 
{

    private DocumentManager dm;

    public Cashier(MainSystem system, UserRecord record)
    {
        super(system, record);
        this.dm = system.documentManager();
    }


    @Override
    public void employeeMenu() 
    {
        while (true)
        {
            System.out.println("\n=== CASHIER MENU ===");
            System.out.println("[1] View Pending Payments");
            System.out.println("[2] Manage Requests");
            System.out.println("[3] Manage History");
            System.out.println("[4] Logout");
            int choice = system.validate().menuChoice("Choose: ", 3);
            
            if (choice == 1)
            {
            	viewPending();
            }
            
            else if (choice == 2)
            {
            	markPaid();
            }

            else if (choice == 3)
            {
            	historyMenu();
            }
            
            else if (choice == 4)
            {
            	return;
            }
        }
    }

    private void viewPending() 
    {
        List<DocumentRequest> all = dm.loadAll();
        
        boolean found = false;
   
        System.out.println("=== PENDING ===");
        
        for (DocumentRequest r : all) 
        {
            if ("Pending Payment".equals(r.getStatus())) 
            {
                System.out.println("- " + r.getDocName() + " | " + r.getStudentNum());
                found = true;
            }
        }
        
        if (!found)
        {
        	System.out.println("No pending payments.");
        }
    }

    private void markPaid() 
    {
        List<DocumentRequest> all = dm.loadAll();
        
        ArrayList<DocumentRequest> pending = new ArrayList<DocumentRequest>();
        
        for (DocumentRequest req : all) 
        {
            if ("Pending Payment".equals(req.getStatus()))
            {
            	pending.add(req);
            }
        }
        
        if (pending.size() == 0) 
        {
            System.out.println("No pending.");
            return;
        }
        
        for (int i = 0; i < pending.size(); i++) 
        {
            System.out.println("[" + (i+1) + "] " + pending.get(i).getDocName() + " (" + pending.get(i).getStudentNum() + ")");
        }
        
        int sel = system.validate().menuChoice("Select: ", pending.size());
        
        DocumentRequest selReq = pending.get(sel - 1);
        
        selReq.setStatus("Paid");
        dm.saveAll(all);
        
        System.out.println("Marked Paid.");
    }
}
