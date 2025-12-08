package package1;

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
            System.out.println("\n=== CASHIER MENU ===");
            System.out.println("[1] View Request Status");
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
            	respondToTicket();
            }   

            else if (choice == 5)
            {
                viewResponse();
            }
            
            else if (choice == 6)
            {
                ArrayList<String> historyTag = new ArrayList<>();
                historyTag.add("CASHIER");
                historyTag.add("PAUSED");
                historyTag.add("Paid");
                history(1, historyTag, true);
            }
            
            else if (choice == 7)
            {
            	reciDisplay();
            }

            else if (choice == 8)
            {
            	return;
            }
        }
    }

    // Shows current ( Request at head of queue ), and pending
    @Override
    public void displayRequest() {
        if (cashierQ.isEmpty()){
            System.out.println("No items in queue");
            return;
        }

        System.out.println("===== Current =====");
        qm.loadViewQueue(cashierQ.peek(), true);
    }

    private void requestStatus() {

        System.out.println("----  PENDING  ----");
        qm.loadViewQueue(qs.PQtoList(cashierQ), false, true, false, 0);

        System.out.println("----  PAUSED  ----");
        if (!qm.loadViewQueue(pauseQ, false, true, false, 10)) {
            System.out.println("No paused payments.");
        }

    }

    @Override
    public void requestManager() {
        while (true) {
            if (pauseQ.isEmpty() && cashierQ.isEmpty()) {
                System.out.println("No items in queue");
                return;
            }

            super.requestManager();
            
            String select = system.validate().requireText("X - Go Back, G - Go to payment, P - Pause, U - Unpause request, E - Change Expiry V - View Request List, C - Cancel");

            switch(select) {

                // Payment
                case "G": case "g":
                paid(null, 0);
                break;

                // Pause
                case "P": case "p":
                if (cashierQ.isEmpty()){
                    System.out.println("No items in queue");
                    break;
                }

                if (!system.validate().confirm("Are you sure you want to pause Request: " + cashierQ.peek().getId())) break;
                qm.pause(system.genDate());
                System.out.println("Request paused");
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
                    System.out.println("No items in queue");
                    return;
                }

                if (!system.validate().confirm("Are you sure you want to cancel Request: " + cashierQ.peek().getId())) break;
                qm.dequeue("Cancelled", "CASHIER", system.genDate(), cashierQ);
                System.out.println("Current request cancelled");
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

    private void paid(QueueRequest cur, int index) {

        while (true) {

            if (index == 0 && cashierQ.isEmpty()) {
                System.out.println("No pending Requests");
                break;
            }
            if (index == 0) cur = cashierQ.peek();

            System.out.println("== Confirm Payment ==");
            Cashier.Reciept rc = new Reciept(cur);

            double payment = 0;
            while (true) {
                payment = system.validate().requireDouble("Input payment ('x' to go back)");
                if (payment == -7) return;

                if (Double.parseDouble(cur.getPrice()) > payment) {
                    System.out.println("Insufficient Payment");
                    continue;
                }

                if (!system.validate().confirm("Confirm payment? ")) continue;
                break;
            }

            if (index == 0) qm.moveToWindow("Paid", "ACCOUNTING", system.genDate(), cashierQ, QueueSystem.getAccountQ(), system.genDate(1));
            else  qm.moveToWindow("Paid", "ACCOUNTING", system.genDate(), pauseQ, QueueSystem.getAccountQ(), index - 1, system.genDate(1));
            
            System.out.println("Successfully paid, pending approval.");
            Cashier.Reciept rec = new Reciept(cur, String.valueOf(payment));
            rec.appendReci(cur, String.valueOf(payment));
            if (!system.validate().confirm("Load next? ")) return;
            index = 0;
        }
    }

    private void unpause() {
        while (true) {
            System.out.println("----  PAUSED  ----");
            if (!qm.loadViewQueue(pauseQ, false, true, false, 0)) {
                System.out.println("No paused payments.");
                return;
            }

            int length = pauseQ.size();

            int select2 = system.validate().minMaxXChoice("Type the index of the paused request you want to select, (x to go back)", 1, length); //TODO: confirm selected
            if (select2 == -1) {
                break;
            }

            while (true) {
                String select3 = system.validate().requireText("G - Go to payment, C - Cancel, X - Go back");

                switch (select3) {
                    case "C", "c" -> {
                        if (!system.validate().confirm("Are you sure ?")) break;
                        qm.dequeue("Cancelled", "CASHIER", system.genDate(), pauseQ, (select2 - 1));
                        System.out.println("Succesfully unpaused request");
                        break;
                    }

                    case "G", "g" -> {
                        if (!system.validate().confirm("Are you sure ?")) break;
                        paid(pauseQ.get((select2 - 1)), select2);
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

    // TODO: display for no reciepts
    public void reciDisplay() {
        Reciept r = new Reciept();

        List<String> list = r.read();
        int i = (list.size() + 9) / 10;
        int current = 1;

        r.display(list, 1, i);

        while (true) {
            int input = system.validate().minMaxXChoice("Go to page ('x' to go back):", 1, i);

            if (input == current) {
                System.out.println("Already on page " + input);
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
        public Reciept(QueueRequest request) {
            System.out.printf("   REQUEST NO: %-10s            %s%n%n", request.getId(), request.getDate()); // %s is string, %n is enter?, %-10s (-10 means indented 10 spaces with '-' meaning to the left)
            System.out.printf("   %-20s-%10s%n", "Document", "Fee");
            System.out.printf("   %-20s-%10s%n%n", request.getDocument(), request.getPrice());
        }

        // Final Reciept
        public Reciept(QueueRequest request, String payment) {
            this(request);

            System.out.println("-".repeat(50));
            System.out.printf("   %-20s-%10s%n", request.getDocument(), "Paid: " + payment);

            double change = Double.parseDouble(payment) - Double.parseDouble(request.getPrice());
            System.out.printf("   %-20s-%10s%n", "", "Change: " + change);

        }

        public Reciept() {}


        public final void appendReci(QueueRequest request, String payment) {
            String line = String.format("%s,%s,%s,%s,%s,%s,%s,%s,dummy", request.getId(), request.getStNum(), request.getDocument(), request.getState(), request.getWindow(), request.getDate(), request.getPrice(), payment);
            append(line);
        }

        private void display(List<String> read, int page, int maxPage) {
            
            Collections.reverse(read);
            int items = 0;

            if (read != null) {
                for (String line : read) {

                    items++;
                    if (items < (page - 1) * 10) continue;
                    else if (items > page * 10) break;

                    String parts[] = line.split(",");

                    QueueRequest request = QueueRequest.fromLine(line);
                    Reciept r = new Reciept(request, parts[7]);
                }
            }

            if (maxPage == 1) {
                System.out.println(" Viewing page " + page);
            } else {
                System.out.println(" Viewing page " + page + " out of " + maxPage);
            }
        }

    }

}
