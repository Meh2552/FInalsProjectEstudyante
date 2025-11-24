package package1;

import java.util.*;

public class Registrar extends Employee 
{

    private DocumentManager dm;
    private LinkedList<QueueRequest> regQ;
    private QueueSystem qs;
    private QueueSystem.QueueManager qm;

    public Registrar(MainSystem system, UserRecord record) 
    {
        super(system, record);
        this.dm = system.documentManager();
        this.regQ = QueueSystem.getRegistarQ();
        this.qs = system.queueSystem();
        this.qm = system.queueSystem().getQueueManager();
    }

    @Override
    public void employeeMenu() 
    {
        while (true) 
        {
            System.out.println("=== REGISTRAR MENU ===");
            System.out.println("[1] View Requests");
            System.out.println("[2] Manage Requests");
            System.out.println("[3] Respond to Helpdesk");
            System.out.println("[4] View Helpdesk Responses");
            System.out.println("[5] See History");
            System.out.println("[6] Logout");
            
            int choice = system.validate().menuChoice("Choose: ", 5);
            
            if (choice == 1)
            {
            	displayRequest();
            }
            
            else if (choice == 2)
            {
            	requestManager();
            }
            
            else if (choice == 3)
            {
            	respondToTicket();
            }
            
            else if (choice == 4)
            {
            	viewResponse();
            }
            
            else if (choice == 5)
            {
            ArrayList<String> historyTag = new ArrayList<>();
            historyTag.add("ACCOUNTING");
            historyTag.add("Approved");
            history(1, historyTag);
            }
            else if (choice == 6) {
                return;
            }
        }
    }

    @Override
    public void displayRequest() {
        qm.loadViewQueue(regQ);
    }

    @Override
    public void requestManager() {
        while (true) {

            if (qs.emptyDisplay(regQ, "No pending requests")) return;
            super.requestManager();
            
            int select = system.validate().minMaxXChoice("X - Go Back, 1 - Mark for processing, 2 - Release Document, 3 - Mark for Pick-Up, 4 - Complete Request, 5 - Cancel Request", 1, 5);
            if (select == -1) return;

            int index;
            switch(select) {

                // Processing
                case 1:
                index = selectRequest("Approved");
                if (index == -1) break;
                
                qm.moveToWindow("Processing", system.genDate(), index);
                System.out.println("Successfully marked for processing");
                break;

                // Released
                case 2:
                index = selectRequest("Processing");
                if (index == -1) break;
                qm.moveToWindow("Released", system.genDate(), index);
                System.out.println("Request released");
                break;

                // Pick-Up
                case 3:
                index = selectRequest("Released");
                if (index == -1) break;
                qm.moveToWindow("Ready for Pick-Up", system.genDate(), index);
                System.out.println("Request marked for 'Pick-up'");
                break;

                // Complete
                case 4:
                index = selectRequest("Pick-Up");
                if (index == -1) break;
                qm.dequeue("Completed", "REGISTRAR", system.genDate(), regQ, index);
                System.out.println("Request resolved");
                break;

                // Cancel
                case 5:
                index = selectRequest();
                if (index == -1) break;
                qm.dequeue("Cancelled", "REGISTRAR", system.genDate(), regQ, index);
                System.out.println("Cancelled successfully");
                break;
            }

        }

    }

    private int selectRequest(String state) {
        if (qs.emptyDisplay(regQ, "No pending requests")) return -1;
        
        LinkedList<QueueRequest> requests = new LinkedList<>();
        List<QueueRequest> r = qm.lookForState(state, regQ);

        if (r == null) {
            System.out.println("No pending requests");
            return -1;
        }
        requests.addAll(r);

        int length = requests.size();
        qm.loadViewQueue(requests, true, state);

        int select = -1, index;
        while (true) { 
            select = system.validate().minMaxXChoice("Type the index of the request you want to select, (x to go back)", 1, length);
            if (select == -1) {
                return -1;
            }

            index = regQ.indexOf(requests.get(select - 1));
            if (system.validate().confirm("Are you sure you want to select Request: " + regQ.get(index).getId())) break;
            
        }


        return index;
    }

    // Selects from whole list 
    private int selectRequest() {
        displayRequest();

        int select = 0;
        while (true) { 
            select = system.validate().minMaxXChoice("Type the index of the request you want to select, (x to go back)", 1, regQ.size());
            if (select == -1) {
                return -1;
            }

            if (system.validate().confirm("Are you sure you want to select Request: " + regQ.get(select - 1).getId())) break;
            
        }

        return select - 1;
    }

}
