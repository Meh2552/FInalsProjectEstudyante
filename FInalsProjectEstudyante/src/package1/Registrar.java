package package1;

import displays.*;
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
            qm.expire(system.genDate());

            ShowRegistrarMenuDisplay.RegistrarMenuDisplay();
            System.out.println("");
            int choice = system.validate().menuChoice("Choose: ", 7);
            
            if (choice == 1)
            {
                System.out.println("\n                                        \u001B[32m- Loading requests\u001B[0m");
                System.out.println("\n                                        -----------------------------------------------------------------------------------------------------------------------------\n");
            	displayRequest();
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
            ShowRegistrarMenuDisplay.RegistrarHistoryDisplay();

            ArrayList<String> historyTag = new ArrayList<>();
            historyTag.add("ACCOUNTING");
            historyTag.add("Approved");
            history(1, historyTag, false);
            }
            else if (choice == 7) {
                System.out.println("\n                                        \u001B[32m- Returning...\u001B[0m");
                System.out.println("\n                                        -----------------------------------------------------------------------------------------------------------------------------\n");
                return;
            }
        }
    }

    @Override
    public void displayRequest() {

        ShowRegistrarMenuDisplay.OngoingRequestsDisplay();
        if (!qm.loadViewQueue(regQ, false, false, false, 0)) {
            System.out.println("                                        \u001B[31mNo ongoing requests\u001B[0m");
        }

    }

    private void currentDisplay(int index) {

        ShowEmployeeDisplay.CurrentRequestsDisplay();
        qm.loadViewQueue(regQ.get(index), true);

    }

    @Override
    public void requestManager() {
        while (true) {

            if (regQ.isEmpty()) {
                System.out.println("                                        \u001B[31mNo items in queue\u001B[0m");
                return;
            }
            
            displayRequest();
            int index = selectRequest();
            if (index == -1) return;

            while (true) {
            currentDisplay(index);
            ShowRegistrarMenuDisplay.RegistrarRequestManagerDisplay();
            String select = system.validate().requireText("Choose: ");

            switch(select) {

                // Update State
                case "U": case "u":
                updateState(index);
                break;

                // Send to Window
                case "S": case "s":
                sendToWindow(index);
                break;

                // Change Expiry
                case "E": case "e":
                changeExpiry(regQ.get(index));
                break;

                // Finish
                case "F": case "f":
                if (!system.validate().confirm("Finishing a request will remove it, finish request? " + regQ.get(index).getId())) continue;
                qm.dequeue("Completed", "REGISTRAR", system.genDate(), regQ, index);
                System.out.println("                                                                  ╔════════════════════════════════════════════════════════════════════╗");
                System.out.println("                                                                  ║                           \u001B[32mRequest resolved\u001B[0m                         ║");
                System.out.println("                                                                  ╚════════════════════════════════════════════════════════════════════╝");
                break;

                // Cancel
                case "C": case "c":
                if (!system.validate().confirm("Are you sure you want to cancel Request: " + regQ.get(index).getId())) continue;
                qm.dequeue("Cancelled", "REGISTRAR", system.genDate(), regQ, index);
                System.out.println("                                                                  ╔════════════════════════════════════════════════════════════════════╗");
                System.out.println("                                                                  ║                           \u001B[32mRequest cancelled\u001B[0m                        ║");
                System.out.println("                                                                  ╚════════════════════════════════════════════════════════════════════╝");
                break;

                // View Requests
                case "V": case "v":
                displayRequest();
                continue;

                // Back
                case "X": case "x":
                System.out.println("\n                                        \u001B[32m- Returning...\u001B[0m");
                System.out.println("\n                                        -----------------------------------------------------------------------------------------------------------------------------\n");
                break;

                default:
                System.out.println("                                        \u001B[31mInvalid Selection\u001B[0m");
                continue;

            }

            break;

            }

        }

    }

    // Selects from whole list 
    private int selectRequest() {

        int select = 0;
        while (true) { 
            select = system.validate().minMaxXChoice("                                        Type the index of the request you want to select, (x to go back)", 1, regQ.size());
            if (select == -1) {
                return -1;
            }

            if (system.validate().confirm("                                        Are you sure you want to select Request: " + regQ.get(select - 1).getId())) break;
            
        }

        return select - 1;
    }

    private void updateState(int index) {
        while (true) {
            int expiry = 0;
            currentDisplay(index);

            String select = system.validate().requireText("                                        X - Go Back, P - Processing, R - Release, U - Ready for Pick-Up, Type for custom");

            switch(select) {

                // Mark for Processing
                case "P": case "p":
                expiry = requireExpiry();
                if (expiry == -7) continue;

                if (!system.validate().confirm("                                        Mark Request " + regQ.get(index).getId() + " as 'Processing' ")) continue;
                qm.moveToWindow("Processing", "REGISTRAR", system.genDate(), regQ, regQ, index, system.genDate(expiry));
                System.out.println("                                                                  ╔════════════════════════════════════════════════════════════════════╗");
                System.out.println("                                                                  ║                  \u001B[32mSuccessfully marked for processing\u001B[0m                ║");
                System.out.println("                                                                  ╚════════════════════════════════════════════════════════════════════╝");
                break;

                // Mark for Release
                case "R": case "r":
                expiry = requireExpiry();
                if (expiry == -7) continue; 

                if (!system.validate().confirm("                                        Mark Request " + regQ.get(index).getId() + " as 'Released' ")) continue;
                qm.moveToWindow("Released", "REGISTRAR", system.genDate(), regQ, regQ, index, system.genDate(expiry));
                System.out.println("                                                                  ╔════════════════════════════════════════════════════════════════════╗");
                System.out.println("                                                                  ║                           \u001B[32mRequest released\u001B[0m                         ║");
                System.out.println("                                                                  ╚════════════════════════════════════════════════════════════════════╝");
                break;

                // Mark for Pick-Up
                case "U": case "u":
                expiry = requireExpiry();
                if (expiry == -7) continue;

                if (!system.validate().confirm("                                        Mark Request " + regQ.get(index).getId() + " as 'Ready for Pick-Up' ")) continue;
                qm.moveToWindow("Ready for Pick-Up", "REGISTRAR", system.genDate(), regQ, regQ, index, system.genDate(expiry));
                System.out.println("                                                                  ╔════════════════════════════════════════════════════════════════════╗");
                System.out.println("                                                                  ║                    \u001B[32mSucessfully marked for pick-up\u001B[0m                  ║");
                System.out.println("                                                                  ╚════════════════════════════════════════════════════════════════════╝");
                break;

                // Back
                case "X": case "x":
                break;

                default:
                expiry = requireExpiry();
                if (expiry == -7) continue;

                if (!system.validate().confirm("                                        Mark Request " + regQ.get(index).getId() + " as '" + select + "'")) continue;
                qm.moveToWindow(select, "REGISTRAR", system.genDate(), regQ, regQ, index, system.genDate(expiry));
                System.out.println("                                                                  ╔════════════════════════════════════════════════════════════════════╗");
                System.out.printf("                                                                  ║                 \u001B[32mRequest marked as %-20s\u001B[0m             ║%n", select);
                System.out.println("                                                                  ╚════════════════════════════════════════════════════════════════════╝");
                break;

            }

            break;
        }
    }

    // Method for send to window
    private void sendToWindow(int index) {
        while (true) { 

            ShowRegistrarMenuDisplay.SendWindowAccountingDisplay();

            int input = system.validate().minMaxXChoice("                                        Choose which window", 1, 2);
            QueueRequest request = regQ.get(index);

            switch(input) {

                // Cashier
                case 1:
                cashierSendTo(request);
                break;

                // Accounting
                case 2:
                accountingSendTo(request);
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
            System.out.println("From: Registrar    To: Cashier");
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

        qm.moveToWindow(state, "CASHIER", system.genDate(), regQ, QueueSystem.getCashierQ(), regQ.indexOf(request), system.genDate(expiry));
        System.out.println("                                                                  ╔════════════════════════════════════════════════════════════════════╗");
        System.out.println("                                                                  ║                       \u001B[32mRequest sent to cashier\u001B[0m                      ║");
        System.out.println("                                                                  ╚════════════════════════════════════════════════════════════════════╝");
        
    }

    private void accountingSendTo(QueueRequest request) {

        System.out.println("\n                                        \u001B[32m- Accounting Selected\u001B[0m");
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
            System.out.println("From: Registrar    To: Accounting");
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

        qm.moveToWindow(state, "ACCOUNTING", system.genDate(), regQ, QueueSystem.getAccountQ(), regQ.indexOf(request), system.genDate(expiry));
        System.out.println("                                                                  ╔════════════════════════════════════════════════════════════════════╗");
        System.out.println("                                                                  ║                      \u001B[32mRequest sent to accounting\u001B[0m                    ║");
        System.out.println("                                                                  ╚════════════════════════════════════════════════════════════════════╝");

    }

}
