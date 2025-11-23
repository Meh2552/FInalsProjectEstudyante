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

    // Shows current ( Request at head of queue ), and pending
    @Override
    public void displayRequest() {
        System.out.println("----  Current  ----");
        if (cashierQ.isEmpty()) {
            System.out.println("No requests in queue");
        }

        LinkedList<QueueRequest> temp = new LinkedList<>();
        temp.add(cashierQ.peek());
        boolean found2 = qm.loadViewQueue(temp, true, "Pending Payment");
        if (!found2) {
            System.out.println("No pending payments.");
        }

        System.out.println("=== PENDING ===");
        boolean found = qm.loadViewQueue(cashierQ, false, "Pending Payment");
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

        System.out.println("----  PAID  ----");
        boolean found = qm.loadViewQueue(qs.getAccountQ(), false, "Paid");
        if (!found) {
            System.out.println("No paid payments.");
        }

    }

    @Override
    public void requestManager(String prompt) {
        while (true) {
            displayRequest();
            
            int select = system.validate().minMaxXChoice(prompt, 1, 5);
            if (select == -1) return;

            switch(select) {

                // Payment
                case 1:
                paid();
                break;

                // Pause
                case 2:
                if (pauseQ.isEmpty()) {
                System.out.println("No paused requests");
                break;
                }

                if (!system.validate().confirm("Are you sure you want to pause Request: " + cashierQ.peek().getId())) break;
                qm.pause(system.genDate());
                    System.out.println("Request at head paused");
                break;

                // Unpause
                case 3:
                unpause();
                break;

                // Cancel
                case 4:
                if (cashierQ.isEmpty()) {
                System.out.println("No pending requests");
                break;
                }

                if (!system.validate().confirm("Are you sure you want to cancel Request: " + cashierQ.peek().getId())) break;
                qm.dequeue("Cancelled", "CASHIER", system.genDate(), cashierQ);
                break;
            }

        }

    }

    private void paid() {
        while (true) {
            if (cashierQ.isEmpty()) {
                System.out.println("No pending requests");
                break;
            }

            int length = cashierQ.size();
            qm.loadViewQueue(cashierQ, true, "Pending Payment");
            int select = system.validate().minMaxXChoice("Type the index of the request you want to select, (x to go back)", 1, length);



        }
    }

    private void unpause() {
        while (true) {
            if (pauseQ.isEmpty()) {
                System.out.println("No paused requests");
                break;
            }

            int length = pauseQ.size();
            qm.loadViewQueue(pauseQ);
            int select2 = system.validate().minMaxXChoice("Type the index of the paused request you want to select, (x to go back)", 1, length);
            if (select2 == -1) {
                break;
            }

            while (true) {
                String select3 = system.validate().requireText("P - Unpause; Mark as paid, C - Cancel Request, X - Go back");
                if (!system.validate().confirm("Are you sure ?")) break;

                switch (select3) {
                    case "C", "c" -> {
                        qm.unpause("Cancelled", system.genDate(), false, (select2 - 1));
                        System.out.println("Succesfully cancelled request");
                        break;
                    }

                    case "P", "p" -> {
                        qm.unpause("Paid", system.genDate(), true, (select2 - 1));
                        System.out.println("Succesfully unpaused request");
                        break;
                    }

                    case "X", "x" -> {
                        break;
                    }

                    default -> {
                        System.out.println("Pick an option among the ones given");
                        continue;
                    }
                }
                break;
                
            }

        }
    }

    public class Reciept extends DocuHandler{

        @Override
        public String getFileName() {
            return "reciepts.txt";
        }



    }

}
