package package1;

import displays.*;
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
            ShowAccountingMenuDisplay.accountingMenuDisplay();
            System.out.println("");
            int choice = system.validate().menuChoice("                                        Choose: ", 8);
            
            if (choice == 1)
            {
                System.out.println("\n                                        \u001B[32m- Loading requests\u001B[0m");
                System.out.println("\n                                        -----------------------------------------------------------------------------------------------------------------------------\n");
            	requestStatus();
            }
            
            else if (choice == 2)
            {
                System.out.println("\n                                        \u001B[32m- Going to create request menu\u001B[0m");
                System.out.println("\n                                        -----------------------------------------------------------------------------------------------------------------------------\n");
            	createRequest();
            }

            else if (choice == 3)
            {
                System.out.println("\n                                        \u001B[32m- Going to request manager\u001B[0m");
                System.out.println("\n                                        -----------------------------------------------------------------------------------------------------------------------------\n");
            	requestManager();
            }
            
            else if (choice == 4)
            {
                System.out.println("\n                                        \u001B[32m- Going to helpdesk\u001B[0m");
                System.out.println("\n                                        -----------------------------------------------------------------------------------------------------------------------------\n");
                respondToTicket();
            }
            
            else if (choice == 5)
            {
                System.out.println("\n                                        \u001B[32m- Loading ticket responses\u001B[0m");
                System.out.println("\n                                        -----------------------------------------------------------------------------------------------------------------------------\n");
                viewResponse();
            }

            else if (choice == 6)	
            {
                System.out.println("\n                                        \u001B[32m- Loading accounting history\u001B[0m");
                System.out.println("\n                                        -----------------------------------------------------------------------------------------------------------------------------\n");
                ShowAccountingMenuDisplay.AccountingHistoryMenuDisplay();
                
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
                System.out.println("\n                                        \u001B[32m- Returning...\u001B[0m");
                System.out.println("\n                                        -----------------------------------------------------------------------------------------------------------------------------\n");
                return;
            }
        }
    }

    @Override
    public void displayRequest() {
        if (accQ.isEmpty()) {
            System.out.println("                                        \u001B[31mNo items in queue\u001B[0m");
            return;
        }

        ShowEmployeeDisplay.CurrentRequestsDisplay();
        System.out.println("");
        qm.loadViewQueue(accQ.peek(), true);

    }

    private void requestStatus() {

        ShowEmployeeDisplay.PendingRequestsDisplay();
        if (!qm.loadViewQueue(qs.PQtoList(accQ), false, false, false, 0)) {
            System.out.println("                                        \u001B[31mNo pending payments\u001B[0m");
        }

        ShowAccountingMenuDisplay.ApprovedRequestDisplay();
        if (!qm.loadViewQueue(QueueSystem.getRegistarQ(), false, "Approved", false, false, false, 5)) {
            System.out.println("                                        \u001B[31mNo approved\u001B[0m");
        }

    }

    @Override
    public void requestManager() {
        while (true) { 
            if (accQ.isEmpty()) {
                System.out.println("                                        \u001B[31mNo items in queue\u001B[0m");
                return;
            }

            super.requestManager();

            ShowAccountingMenuDisplay.AccountingRequestManagerDisplay();
            String select = system.validate().requireText("                                        Choose: ");

            switch(select) {

                // Accept
                case "A": case "a":
                if (!system.validate().confirm("                                        Are you sure you want to accept Request: " + accQ.peek().getId())) break;
                qm.moveToWindow("Approved", "REGISTRAR", system.genDate(), accQ, qs.getRegistarQ(), system.genDate(7));
                System.out.println("                                                                  ╔════════════════════════════════════════════════════════════════════╗");
                System.out.println("                                                                  ║                       \u001B[32mCurrent request accepted\u001B[0m                     ║");
                System.out.println("                                                                  ╚════════════════════════════════════════════════════════════════════╝");
                break;

                // Deny
                case "D": case "d":
                if (!system.validate().confirm("                                        Are you sure you want to deny Request: " + accQ.peek().getId())) break;
                qm.dequeue("Denied", "ACCOUNTING", system.genDate(), accQ);
                System.out.println("                                                                  ╔════════════════════════════════════════════════════════════════════╗");
                System.out.println("                                                                  ║                         \u001B[32mCurrent request denied\u001B[0m                     ║");
                System.out.println("                                                                  ╚════════════════════════════════════════════════════════════════════╝");
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
                if (!system.validate().confirm("                                        Are you sure you want to cancel Request: " + accQ.peek().getId())) break;
                qm.dequeue("Cancelled", "ACCOUNTING", system.genDate(), accQ);
                System.out.println("                                                                  ╔════════════════════════════════════════════════════════════════════╗");
                System.out.println("                                                                  ║                       \u001B[32mCurrent request cancelled\u001B[0m                    ║");
                System.out.println("                                                                  ╚════════════════════════════════════════════════════════════════════╝");
                break;

                // View
                case "V": case "v":
                ShowEmployeeDisplay.PendingRequestsDisplay();
                qm.loadViewQueue(qs.PQtoList(accQ), false, false, false, 0);
                break;

                // Back
                case "X": case "x":
                System.out.println("\n                                        \u001B[32m- Returning...\u001B[0m");
                System.out.println("\n                                        -----------------------------------------------------------------------------------------------------------------------------\n");
                return;

                default:
                System.out.println("                                        \u001B[31mInvalid Selectio\u001B[0m");
                break;
            }

        }
        
    }

    private void sendToWindow() {
        while (true) { 

            ShowAccountingMenuDisplay.SendWindowAccountingDisplay();

            int input = system.validate().minMaxXChoice("                                        Choose which window", 1, 2);
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

        System.out.println("\n                                        \u001B[32m- Cashier Selected\u001B[0m");
        System.out.println("\n                                        -----------------------------------------------------------------------------------------------------------------------------\n");
        String state = "";
        double price = 0;
        int expiry = 0;

        int step = 1;
        while (step <= 3) {

            switch (step) {

                // State
                case 1:
                state = system.validate().requireText("                                        Enter request state (X to go back)");
                if (state.matches("[Xx]")) return;
                step++;
                continue;

                // Price
                case 2:
                price = system.validate().requireDouble("                                        Input payment (X to go back)");
                if (price == -7) {
                    step--;
                    continue;
                }

                else if (price <= 0) {
                    System.out.println("                                        \u001B[31mInvalid price\u001B[0m");
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
        System.out.println("                                                                  ╔════════════════════════════════════════════════════════════════════╗");
        System.out.println("                                                                  ║                       \u001B[32mRequest sent to cashier\u001B[0m                      ║");
        System.out.println("                                                                  ╚════════════════════════════════════════════════════════════════════╝");
    }

    private void registrarSendTo(QueueRequest request) {

        System.out.println("\n                                        \u001B[32m- Registrar Selected\u001B[0m");
        System.out.println("\n                                        -----------------------------------------------------------------------------------------------------------------------------\n");
        String state = "";
        int expiry = 0;

        int step = 1;
        while (step <= 2) {

            switch (step) {

                // State
                case 1:
                state = system.validate().requireText("                                        Enter request state (X to go back)");
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
        System.out.println("                                                                  ╔════════════════════════════════════════════════════════════════════╗");
        System.out.println("                                                                  ║                      \u001B[32mRequest sent to registrar\u001B[0m                     ║");
        System.out.println("                                                                  ╚════════════════════════════════════════════════════════════════════╝");
    }

}
