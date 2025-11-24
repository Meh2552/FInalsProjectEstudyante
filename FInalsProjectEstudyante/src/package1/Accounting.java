package package1;

import java.util.*;

public class Accounting extends Employee 
{

    private DocumentManager dm;
    private LinkedList<QueueRequest> accQ;
    private QueueSystem qs;
    private QueueSystem.QueueManager qm;

    public Accounting(MainSystem system, UserRecord record) 
    {
        super(system, record);
        this.dm = system.documentManager();
        this.accQ = QueueSystem.getAccountQ();
        this.qs = system.queueSystem();
        this.qm = system.queueSystem().getQueueManager();
    }

    @Override
    public void employeeMenu() 
    {
        while (true) 
        {
            System.out.println("\n=== ACCOUNTING MENU ===");
            System.out.println("[1] View Paid Requests");
            System.out.println("[2] Approve/Reject Payment");
            System.out.println("[3] View History");
            System.out.println("[4] Logout");
            int c = system.validate().menuChoice("Choose: ", 3);
            
            if (c == 1)
            {
            	displayRequest();
            }
            
            else if (c == 2)
            {
            	requestManager();
            }
            
            else if (c == 3)	
            {
                ArrayList<String> historyTag = new ArrayList<>();
                historyTag.add("ACCOUNTING");
                historyTag.add("Approved");
                history(1, historyTag);
            	return;
            }
            else if (c == 4) {
                return;
            }
        }
    }

    @Override
    public void displayRequest() {
        System.out.println("----  Current  ----");
        if (qs.emptyDisplay(accQ, "No requests in queue")) return;

        LinkedList<QueueRequest> temp = new LinkedList<>();
        temp.add(accQ.peek());
        qm.loadViewQueue(temp, true, "");

        System.out.println("=== PENDING ===");
        qm.loadViewQueue(accQ);

    }

    @Override
    public void requestManager() {
        while (true) { 

            super.requestManager();
            
        }
        
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
