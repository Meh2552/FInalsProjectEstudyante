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
            qm.expire(system.genDate());
            System.out.println("\n=== ACCOUNTING MENU ===");
            System.out.println("[1] View All Requests");
            System.out.println("[2] Create Request");
            System.out.println("[3] Manage Requests");
            System.out.println("[4] Respond to Helpdesk");
            System.out.println("[5] View Helpdesk Responses");
            System.out.println("[6] See History");
            System.out.println("[7] View Reciepts");
            System.out.println("[8] Logout");
            
            int choice = system.validate().menuChoice("Choose: ", 8);
            
            if (choice == 1)
            {
            	requestStatus();
            }
            
            else if (choice == 2)
            {
            	createRequest();
            }

            else if (choice == 3)
            {
            	requestManager();
            }
            
            else if (choice == 4)
            {
                respondToTicketWindow();
            }
            
            else if (choice == 5)
            {
                viewResponse();
            }

            else if (choice == 6)	
            {
                ArrayList<String> historyTag = new ArrayList<>();
                historyTag.add("ACCOUNTING");
                historyTag.add("Approved");
                history(1, historyTag, false);
            }

            else if (choice == 7) {
                Cashier c = new Cashier(system, record);
                c.reciDisplay();
            }

            else if (choice == 8)
            {
                return;
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
        if (!qm.loadViewQueue(qs.PQtoList(accQ), false, false, false, 0)) {
            System.out.println("No pending payments.");
        }

        System.out.println("----  Approved  ----");
        if (!qm.loadViewQueue(QueueSystem.getRegistarQ(), false, "Approved", false, false, false, 5)) {
            System.out.println("No approved payments.");
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

            String select = system.validate().requireText("X - Go Back, A - Accept, D - Deny, S - Send to Window, E - Change Expiry, C - Cancel, V - View Requests");

            switch(select) {

                // Accept
                case "A": case "a":
                if (!system.validate().confirm("Are you sure you want to accept Request: " + accQ.peek().getId())) break;
                qm.moveToWindow("Approved", "REGISTRAR", system.genDate(), accQ, qs.getRegistarQ(), system.genDate(7));
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
                sendToWindow();
                break;

                // Change Expiry
                case "E": case "e":
                changeExpiry(accQ.peek());
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

    private void sendToWindow() {
        while (true) { 

            System.out.println("----  Send to Window  ----");
            System.out.println("[1] Cashier");
            System.out.println("[2] Registrar");
            System.out.println("[X] Back");

            int input = system.validate().minMaxXChoice("Choose which window", 1, 2);
            QueueRequest request = accQ.peek();

            switch(input) {

                // Cashier
                case 1:
                cashierSendTo(request);
                break;

                // Registrar
                case 2:
                registrarSendTo(request);
                break;

                // Back
                case -1:
                return;

            }

            break;
        }
    }

    private void cashierSendTo(QueueRequest request) {

        System.out.println("- Cashier Selected \n");
        String state = "";
        double price = 0;
        int expiry = 0;

        int step = 1;
        while (step <= 3) {

            switch (step) {

                // State
                case 1:
                state = system.validate().requireText("Enter request state (X to go back)");
                if (state.matches("[Xx]")) return;
                step++;
                continue;

                // Price
                case 2:
                price = system.validate().requireDouble("Input payment (X to go back)");
                if (price == -7) {
                    step--;
                    continue;
                }

                else if (price <= 0) {
                    System.out.println("Invalid price");
                }
                
                step++;
                continue;
                // Expire
                case 3:
                expiry = requireExpiry();
                if (expiry == -7) {
                    step--;
                    continue;
                }

                step++;
                break;
            }

            System.out.println("----  Confirm Send ----");
            System.out.println("From: Accounting    To: Cashier");
            System.out.println("ID: " + request.getId() + "     Requesting: " + request.getDocument());
            System.out.println("State " + state + "    Price " + price);
            System.out.println("Will expire in " + expiry + " day/s" );

            switch (system.validate().editCancelContinue()) {

                case "EDIT" -> {
                step = 1;
                continue;
                }

                case "CANCEL" -> {
                return;
                }

                case "CONTINUE" -> {}
            }

            break;

        }

        request.setPricee(String.valueOf(price));

        qm.moveToWindow(state, "CASHIER", system.genDate(), accQ, QueueSystem.getCashierQ(), system.genDate(expiry));
        System.out.println("Request sent to cashier");
    }

    private void registrarSendTo(QueueRequest request) {

        System.out.println("- Registrar Selected \n");
        String state = "";
        int expiry = 0;

        int step = 1;
        while (step <= 2) {

            switch (step) {

                // State
                case 1:
                state = system.validate().requireText("Enter request state (X to go back)");
                if (state.matches("[Xx]")) return;
                step++;
                continue;

                // Expire
                case 2:
                expiry = requireExpiry();
                if (expiry == -7) {
                    step--;
                    continue;
                }

                step++;
                break;
            }

            System.out.println("----  Confirm Send ----");
            System.out.println("From: Accounting    To: Registrar");
            System.out.println("ID: " + request.getId() + "     Requesting: " + request.getDocument());
            System.out.println("State: " + state);
            System.out.println("Will expire in " + expiry + " day/s" );

            switch (system.validate().editCancelContinue()) {

                case "EDIT" -> {
                step = 1;
                continue;
                }

                case "CANCEL" -> {
                return;
                }

                case "CONTINUE" -> {}
            }

            break;

        }

        qm.moveToWindow(state, "REGISTRAR", system.genDate(), accQ, QueueSystem.getRegistarQ(), system.genDate(expiry));
        System.out.println("Request sent to registrar");
    }

}
