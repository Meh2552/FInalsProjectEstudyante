package package1;

import java.util.*;

public class Registrar extends Employee 
{

    private DocumentManager dm;

    public Registrar(MainSystem system, UserRecord record) 
    {
        super(system, record);
        this.dm = system.documentManager();
    }

    @Override
    public void employeeMenu() 
    {
        while (true) 
        {
            System.out.println("=== REGISTRAR MENU ===");
            System.out.println("[1] View Approved Requests");
            System.out.println("[2] Release Document");
            System.out.println("[3] Logout");
            int choice = system.validate().menuChoice("Choose: ", 3);
            
            if (choice == 1)
            {
            	viewApproved();
            }
            
            else if (choice == 2)
            {
            	releaseDocument();
            }
            else if (choice == 3)
            {
                //TODO : EDTO
                ArrayList<String> historyTag = new ArrayList<>();
                historyTag.add("ACCOUNTING");
                historyTag.add("Approved");
                history(1, historyTag);
            }
            else if (choice == 4) {
                return;
            }
        }
    }

    private void viewApproved() 
    {
        List<DocumentRequest> all = dm.loadAll();
        
        boolean found = false;
        
        System.out.println("=== APPROVED ===");
        
        for (DocumentRequest r : all) 
        {
            if ("Approved".equals(r.getStatus())) 
            {
                System.out.println("- " + r.getDocName() + " | " + r.getStudentNum());
                found = true;
            }
        }
        
        if (!found) System.out.println("None.");
    }

    private void releaseDocument() 
    {
        List<DocumentRequest> all = dm.loadAll();
        
        ArrayList<DocumentRequest> approved = new ArrayList<DocumentRequest>();
        
        for (DocumentRequest req : all) 
        {
            if ("Approved".equals(req.getStatus()))
            {
            	approved.add(req);
            }
        }
        
        if (approved.size() == 0) 
        {
            System.out.println("No approved.");
            return;
        }
        
        for (int i = 0; i < approved.size(); i++) {
            System.out.println("[" + (i+1) + "] " + approved.get(i).getDocName() + " (" + approved.get(i).getStudentNum() + ")");
        }
        
        int sel = system.validate().menuChoice("Select: ", approved.size());
        
        approved.get(sel - 1).setStatus("Released");
        
        dm.saveAll(all);
        
        System.out.println("Released.");
    }

    @Override
    public void displayRequest() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
