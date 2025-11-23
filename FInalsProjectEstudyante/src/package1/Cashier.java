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
            System.out.println("[1] View Request Status");
            System.out.println("[2] Manage Requests");
            System.out.println("[3] Manage History");
            // TODO: reciepts
            System.out.println("[4] Logout");
            int choice = system.validate().menuChoice("Choose: ", 4);
            
            if (choice == 1)
            {
            	requestStatus();
            }
            
            else if (choice == 2)
            {
                requestManager("X - Go Back, 1 - Select request at top, 2 - Pause request at top, 3 - Unpause request, 4 - Cancel request");
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

    LinkedList<QueueRequest> cashierQ = QueueSystem.getCashierQ();
    LinkedList<QueueRequest> pauseQ = QueueSystem.getPauseQ();
    QueueSystem qs = system.queueSystem();
    QueueSystem.QueueManager qm = qs.getQueueManager();

    @Override
    public void displayRequest() {
        System.out.println("=== PENDING ===");
        boolean found = qm.loadViewQueue(cashierQ, true, "Pending Payment");
        if (!found) {
            System.out.println("No pending payments.");
        }
    }

    private void requestStatus() {
        displayRequest();

        System.out.println("----  PAUSED  ----");
        boolean found2 = qm.loadViewQueue(pauseQ);
        if (!found2) {
            System.out.println("No paused payments.");
        }

        boolean found = qm.loadViewQueue(cashierQ, true, "Paid");
        if (!found) {
            System.out.println("No pending payments.");
        }
    }

    @Override
    public void requestManager(String prompt) {
        while (true) {

            super.requestManager(prompt);
            int select = system.validate().minMaxXChoice(prompt, 1, 5);
            if (select == -1) return;

            switch(select) {

                case 1:
                break;
            }

        }

    }
}
