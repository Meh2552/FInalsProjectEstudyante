package package1;

import java.util.*;

public class Accounting extends Employee 
{

    private PriorityQueue<QueueRequest> accQ;
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
            System.out.println("[5] View Reciepts");
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
            else if (choice == 6)
            {
                Cashier.Reciept r = new Cashier.Reciept();
            }
        }
    }

    @Override
    public void displayRequest() {
        if (accQ.isEmpty()) {
            System.out.println("No items in queue");
            return;
        }

        System.out.println("===== Current =====");
        qm.loadViewQueue(accQ.peek(), true);

    }

    private void requestStatus() {

        System.out.println("----  PENDING  ----");
        qm.loadViewQueue(qs.PQtoList(accQ), false, false, false, 0);

        System.out.println("----  Approved  ----");
        if (!qm.loadViewQueue(QueueSystem.getRegistarQ(), false, true, true, 5)) {
            System.out.println("No paused payments.");
        }

    }

    @Override
    public void requestManager() {
        while (true) { 
            if (accQ.isEmpty()) {
                System.out.println("No items in queue");
                return;
            }

            super.requestManager();

            String select = system.validate().requireText("X - Go Back, A - Accept, D - Deny, S - Send to Window, C - Cancel, V - View Requests");

            switch(select) {

                // Accept
                case "A": case "a":
                if (!system.validate().confirm("Are you sure you want to accept Request: " + accQ.peek().getId())) break;
                qm.moveToWindow("Approved", "REGISTRAR", system.genDate(), accQ, qs.getRegistarQ());
                System.out.println("Current request accepted");
                break;

                // Deny
                case "D": case "d":
                if (!system.validate().confirm("Are you sure you want to deny Request: " + accQ.peek().getId())) break;
                qm.dequeue("Denied", "ACCOUNTING", system.genDate(), accQ);
                System.out.println("Current request denied");
                break;

                // Send to Window
                case "S": case "s":
                System.out.println("----  PENDING  ----");
                qm.loadViewQueue(qs.PQtoList(accQ), false, false, false, 0);
                break;

                // Cancel
                case "C": case "c":
                if (!system.validate().confirm("Are you sure you want to cancel Request: " + accQ.peek().getId())) break;
                qm.dequeue("Cancelled", "ACCOUNTING", system.genDate(), accQ);
                System.out.println("Current request cancelled");
                break;

                // View
                case "V": case "v":
                System.out.println("----  PENDING  ----");
                qm.loadViewQueue(qs.PQtoList(accQ), false, false, false, 0);
                break;

                // Back
                case "X": case "x":
                return;

                default:
                System.out.println("Invalid Selection");
                break;
            }

        }
        
    }

}
