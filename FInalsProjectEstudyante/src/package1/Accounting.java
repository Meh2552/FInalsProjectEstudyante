package package1;

import java.util.*;

public class Accounting extends Employee 
{

    private LinkedList<QueueRequest> accQ;
    private QueueSystem qs;
    private QueueSystem.QueueManager qm;

    public Accounting(MainSystem system, UserRecord record) 
    {
        super(system, record);
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
            System.out.println("[1] View All Requests");
            System.out.println("[2] Manage Requests");
            System.out.println("[3] Respond to Helpdesk");
            System.out.println("[4] View Helpdesk Responses");
            System.out.println("[5] See History");
            System.out.println("[6] Logout");
            
            int choice = system.validate().menuChoice("Choose: ", 6);
            
            if (choice == 1)
            {
            	requestStatus();
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
        System.out.println("----  Current  ----");
        if (qs.emptyDisplay(accQ, "No requests in queue")) return;

        LinkedList<QueueRequest> temp = new LinkedList<>();
        temp.add(accQ.peek());
        qm.loadViewQueue(temp, true, "Paid");

        System.out.println("=== PENDING ===");
        qm.loadViewQueue(accQ);

    }

    private void requestStatus() {

        qm.loadViewQueue(accQ);

        System.out.println("----  Approved  ----");
        boolean found = qm.loadViewQueue(qs.getRegistarQ(), false, "Accepted");
        if (!found) {
            System.out.println("No approved requests");
        }

    }

    @Override
    public void requestManager() {
        super.requestManager();

        while (true) { 

            if (qs.emptyDisplay(accQ, "No pending request")) return;

            int select = system.validate().minMaxXChoice("X - Go Back, 1 - Accept current, 2 - Deny current, 3 - Cancel request, 4 - View Requests", 1, 4);
            if (select == -1) return;

            switch(select) {

                // Accept
                case 1:
                if (!system.validate().confirm("Are you sure you want to accept Request: " + accQ.peek().getId())) break;
                qm.moveToWindow("Approved", "REGISTRAR", system.genDate(), accQ, qs.getRegistarQ());
                System.out.println("Current request accepted");
                break;

                // Deny
                case 2:
                if (!system.validate().confirm("Are you sure you want to deny Request: " + accQ.peek().getId())) break;
                qm.dequeue("Denied", "ACCOUNTING", system.genDate(), accQ, 0);
                System.out.println("Current request denied");
                break;

                // Cancel
                case 3:
                if (!system.validate().confirm("Are you sure you want to cancel Request: " + accQ.peek().getId())) break;
                qm.dequeue("Cancelled", "ACCOUNTING", system.genDate(), accQ, 0);
                System.out.println("Current request cancelled");
                break;

                // View
                case 4:
                super.requestManager();
                break;
            }

        }
        
    }

}
