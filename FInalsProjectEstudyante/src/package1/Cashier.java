package package1;

import displays.*;
import java.util.*;

public class Cashier extends Employee 
{

    private PriorityQueue<QueueRequest> cashierQ;
    private LinkedList<QueueRequest> pauseQ;
    private QueueSystem qs;
    private QueueSystem.QueueManager qm;

    public Cashier(MainSystem system, UserRecord record)
    {
        super(system, record);
        this.cashierQ = QueueSystem.getCashierQ();
        this.pauseQ = QueueSystem.getPauseQ();
        this.qs = system.queueSystem();
        this.qm = system.queueSystem().getQueueManager();
    }


    @Override
    public void employeeMenu() 
    {
        while (true)
        {
            qm.expire(system.genDate());
            
            ShowCashierMenuDisplay.CashierMenuDisplay();
            System.out.println("");
            int choice = system.validate().menuChoice("                                        Choose: ", 8);
            
            if (choice == 1)
            {
                System.out.println("\n                                        \u001B[32m- Loading Requests\u001B[0m");
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
            	respondToTicketWindow();
            }   

            else if (choice == 5)
            {
                System.out.println("\n                                        \u001B[32m- Loading helpdesk respones\u001B[0m");
                System.out.println("\n                                        -----------------------------------------------------------------------------------------------------------------------------\n");
                viewResponse();
            }
            
            else if (choice == 6)
            {
                System.out.println("\n                                        \u001B[32m- Loading cashier history\u001B[0m");
                System.out.println("\n                                        -----------------------------------------------------------------------------------------------------------------------------\n");
                ShowCashierMenuDisplay.CashierHistoryDisplay();
                
                ArrayList<String> historyTag = new ArrayList<>();
                historyTag.add("CASHIER");
                historyTag.add("PAUSED");
                historyTag.add("Paid");
                history(1, historyTag, true);
            }
            
            else if (choice == 7)
            {
                System.out.println("\n                                        \u001B[32m- Loading reciepts\u001B[0m");
                System.out.println("\n                                        -----------------------------------------------------------------------------------------------------------------------------\n");
            	reciDisplay();
            }

            else if (choice == 8)
            {
                System.out.println("\n                                        \u001B[32m- Returning...\u001B[0m");
                System.out.println("\n                                        -----------------------------------------------------------------------------------------------------------------------------\n");
            	return;
            }
        }
    }

    // Shows current ( Request at head of queue ), and pending
    @Override
    public void displayRequest() {
        if (cashierQ.isEmpty()){
            System.out.println("                                        \u001B[31mNo items in queue\u001B[0m");
            return;
        }

        ShowEmployeeDisplay.CurrentRequestsDisplay();
        System.out.println("");
        qm.loadViewQueue(cashierQ.peek(), true);
    }

    private void requestStatus() {

        ShowEmployeeDisplay.PendingRequestsDisplay();
        System.out.println("");
        qm.loadViewQueue(qs.PQtoList(cashierQ), false, true, false, 0);

        ShowEmployeeDisplay.PausedRequestsDisplay();
        System.out.println("");
        if (!qm.loadViewQueue(pauseQ, false, true, false, 10)) {
            System.out.println("                                        \u001B[31mNo paused payments.\u001B[0m");
        }

    }

    @Override
    public void requestManager() {
        while (true) {
            if (pauseQ.isEmpty() && cashierQ.isEmpty()) {
                System.out.println("                                        \u001B[31mNo items in queue\u001B[0m");
                return;
            }

            super.requestManager();
            
            ShowCashierMenuDisplay.CashierRequestManagerDisplay();

            String select = system.validate().requireText("                                        Choose: ");

            switch(select) {

                // Payment
                case "G": case "g":
                paid(null, 0);
                break;

                // Pause
                case "P": case "p":
                if (cashierQ.isEmpty()){
                    System.out.println("                                        \u001B[31mNo items in queue\u001B[0m");
                    break;
                }

                if (!system.validate().confirm("                                        Are you sure you want to pause Request: " + cashierQ.peek().getId())) break;
                qm.pause(system.genDate());
                System.out.println("                                                                  ╔════════════════════════════════════════════════════════════════════╗");
                System.out.println("                                                                  ║                            \u001B[32mRequest paused\u001B[0m                          ║");
                System.out.println("                                                                  ╚════════════════════════════════════════════════════════════════════╝");
                break;

                // Unpause
                case "U": case "u":
                unpause();
                break;

                // Change Expiry
                case "E": case "e":
                changeExpiry(cashierQ.peek());
                break;

                // View Request
                case"V": case "v":
                requestStatus();
                break;

                // Cancel
                case "C": case "c":
                if (cashierQ.isEmpty()) {
                    System.out.println("                                        \u001B[31mNo items in queue\u001B[0m");
                    return;
                }

                if (!system.validate().confirm("                                        Are you sure you want to cancel Request: " + cashierQ.peek().getId())) break;
                qm.dequeue("Cancelled", "CASHIER", system.genDate(), cashierQ);
                System.out.println("                                                                  ╔════════════════════════════════════════════════════════════════════╗");
                System.out.println("                                                                  ║                       \u001B[32mCurrent request cancelled\u001B[0m                    ║");
                System.out.println("                                                                  ╚════════════════════════════════════════════════════════════════════╝");
                System.out.println("");
                break;

                // Back
                case "X": case "x":
                System.out.println("\n                                        \u001B[32m- Returning...\u001B[0m");
                System.out.println("\n                                        -----------------------------------------------------------------------------------------------------------------------------\n");
                return;

                default:
                System.out.println("                                        \u001B[31m- Invalid Selection\u001B[0m");
                break;
            }

        }

    }

    private void paid(QueueRequest cur, int index) {

        while (true) {

            if (index == 0 && cashierQ.isEmpty()) {
                System.out.println("                                        \u001B[31m- No pending Requests\u001B[0m");
                break;
            }
            if (index == 0) cur = cashierQ.peek();

            ShowCashierMenuDisplay.ConfirmPaymentDisplay();
            System.out.println("");
            Cashier.Reciept rc = new Reciept(cur, false, false);

            double payment = 0;
            while (true) {
                payment = system.validate().requireDouble("                                        Input payment ('x' to go back)");
                if (payment == -7) return;

                if (Double.parseDouble(cur.getPrice()) > payment) {
                    System.out.println("                                        \u001B[31mInsufficient Payment\u001B[0m");
                    continue;
                }

                if (!system.validate().confirm("                                        Confirm payment? ")) continue;
                break;
            }

            if (index == 0) qm.moveToWindow("Paid", "ACCOUNTING", system.genDate(), cashierQ, QueueSystem.getAccountQ(), system.genDate(1));
            else  qm.moveToWindow("Paid", "ACCOUNTING", system.genDate(), pauseQ, QueueSystem.getAccountQ(), index - 1, system.genDate(1));
            
            System.out.println("                                                                  ╔════════════════════════════════════════════════════════════════════╗");
            System.out.println("                                                                  ║                 \u001B[32mSuccessfully paid, pending approval\u001B[0m                ║");
            System.out.println("                                                                  ╚════════════════════════════════════════════════════════════════════╝");
            System.out.println("");
            

            Cashier.Reciept rec = new Reciept(cur, String.valueOf(payment), false);

            System.out.println("                                                                       ╚═════════════════════════════════════════════════════════════╝");

            rec.appendReci(cur, String.valueOf(payment));
            if (!system.validate().confirm("                                        Load next? ")) return;
            index = 0;
        }
    }

    private void unpause() {
        while (true) {
            ShowEmployeeDisplay.PausedRequestsDisplay();
            System.out.println("");
            if (!qm.loadViewQueue(pauseQ, false, true, false, 0)) {
                System.out.println("                                        \u001B[31mNo paused payments\u001B[0m");
                return;
            }

            int length = pauseQ.size();

            int select2 = system.validate().minMaxXChoice("                                        Type the index of the paused request you want to select, (x to go back)", 1, length); //TODO: confirm selected
            if (select2 == -1) {
                break;
            }

            while (true) {
                String select3 = system.validate().requireText("                                        G - Go to payment, C - Cancel, X - Go back");

                switch (select3) {
                    case "C", "c" -> {
                        if (!system.validate().confirm("                                        Are you sure ?")) break;
                        qm.dequeue("Cancelled", "CASHIER", system.genDate(), pauseQ, (select2 - 1));
                        
                        System.out.println("                                                                  ╔════════════════════════════════════════════════════════════════════╗");
                        System.out.println("                                                                  ║                      \u001B[32mSelected request cancelled\u001B[0m                      ║");
                        System.out.println("                                                                  ╚════════════════════════════════════════════════════════════════════╝");
                        break;
                    }

                    case "G", "g" -> {
                        if (!system.validate().confirm("                                        Are you sure ?")) break;
                        System.out.println("\n                                        \u001B[32m- Succesfully unpaused request\u001B[0m");
                        System.out.println("\n                                        -----------------------------------------------------------------------------------------------------------------------------\n");
                        paid(pauseQ.get((select2 - 1)), select2);
                        break;
                    }

                    case "X", "x" -> {
                        System.out.println("\n                                        \u001B[32m- Returning...\u001B[0m");
                        System.out.println("\n                                        -----------------------------------------------------------------------------------------------------------------------------\n");
                        break;
                    }

                    default -> {
                        System.out.println("                                        \u001B[31mPick an option among the ones given\u001B[0m");
                        continue;
                    }
                }
                break;
                
            }

        }
    }

    // TODO: display for no reciepts
    public void reciDisplay() {
        Reciept r = new Reciept();

        List<String> list = r.read();
        int i = (list.size() + 4) / 5;
        int current = 1;

        ShowCashierMenuDisplay.ReceiptsDisplay();
        System.out.println("");
        r.display(list, 1, i);

        while (true) {
            int input = system.validate().minMaxXChoice("                                        Go to page ('x' to go back):", 1, i);

            if (input == current) {
                System.out.println("                                        Already on page " + input);
                continue;
            }
            if (input == -1) {
                return;
            }

            r.display(list, input, i);
            current = input;

        }
    }


    public static class Reciept extends DocuHandler{

        @Override
        public String getFileName() {
            return "reciepts.txt";
        }


        // Total, not final output
        public Reciept(QueueRequest request, boolean hasBefore, boolean hasAfter) {
            if (hasBefore) System.out.println("                                                                       ╠═════════════════════════════════════════════════════════════╣");
            else System.out.println    ("                                                                       ╔═════════════════════════════════════════════════════════════╗");

            System.out.printf("                                                                       ║  Request Number: %-10s           %20s  ║%n", request.getId(), request.getDate());
            System.out.println    ("                                                                       ║─────────────────────────────────────────────────────────────║");
            System.out.println    ("                                                                       ║  Document                       Fee                         ║");

            String price = String.format("Php %.2f", Double.valueOf(request.getPrice()));
            System.out.printf("                                                                       ║  %-30s %-20s        ║%n", request.getDocument(), price);

            if (hasAfter) System.out.println("                                                                       ╟-------------------------------------------------------------╢");
            else System.out.println         ("                                                                       ╚═════════════════════════════════════════════════════════════╝");
        }

        // Final Reciept
        public Reciept(QueueRequest request, String payment, boolean hasBefore) {
            this(request, hasBefore, true);

            String paid = String.format("Php %.2f", Double.valueOf(payment));
            String change = String.format("Php %.2f", (Double.parseDouble(payment) - Double.parseDouble(request.getPrice())));
            System.out.println    ("                                                                       ║                                                             ║");
            System.out.printf("                                                                       ║  %-30s %-25s   ║%n", "Paid: " + paid, "Change: " + change);

        }

        public Reciept() {}


        public final void appendReci(QueueRequest request, String payment) {
            String line = String.format("%s,%s,%s,%s,%s,%s,%s,%s,dummy", request.getId(), request.getStNum(), request.getDocument(), request.getState(), request.getWindow(), request.getDate(), request.getPrice(), payment);
            append(line);
        }

        private void display(List<String> read, int page, int maxPage) {
            
            Collections.reverse(read);
            int items = 0;
            boolean hasBefore = false;

            if (read != null) {
                for (String line : read) {

                    items++;
                    if (items < (page - 1) * 5) continue;
                    else if (items > page * 5) {
                        System.out.println("                                                                       ╚═════════════════════════════════════════════════════════════╝");
                        break;
                    }

                    String parts[] = line.split(",");

                    QueueRequest request = QueueRequest.fromLine(line);
                    Reciept r = new Reciept(request, parts[7], hasBefore);
                    hasBefore = true;
                }
                if (read.size() == items) System.out.println("                                                                       ╚═════════════════════════════════════════════════════════════╝");
            }

            System.out.println("\n                                        -----------------------------------------------------------------------------------------------------------------------------\n");

            if (maxPage == 1) {
                System.out.println("                                        Viewing page " + page);
            } else {
                System.out.println("                                        Viewing page " + page + " out of " + maxPage);
            }
        }

    }

}
