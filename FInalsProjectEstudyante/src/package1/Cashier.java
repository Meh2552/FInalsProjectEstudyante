package package1;

import java.util.*;

public class Cashier extends Employee 
{

    private LinkedList<QueueRequest> cashierQ;
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
            System.out.println("\n=== CASHIER MENU ===");
            System.out.println("[1] View Request Status");
            System.out.println("[2] Manage Requests");
            System.out.println("[3] See History");
            System.out.println("[4] View Reciepts");
            System.out.println("[5] Respond to Helpdesk");
            System.out.println("[6] View Helpdesk Responses");

            System.out.println("[7] Logout");
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
                ArrayList<String> historyTag = new ArrayList<>();
                historyTag.add("CASHIER");
                historyTag.add("PAUSED");
                historyTag.add("Paid");
                history(1, historyTag);
            }
            
            else if (choice == 4)
            {
            	Reciept r = new Reciept();
            }

            else if (choice == 5)
            {
            	respondToTicket();
            }
            
            else if (choice == 6)
            {
            	viewResponse();
            }
            
            else if (choice == 7)
            {
            	return;
            }
        }
    }

    // Shows current ( Request at head of queue ), and pending
    // TODO: limit for paid
    @Override
    public void displayRequest() {
        System.out.println("----  Current  ----");
        if (qs.emptyDisplay(cashierQ, "No requests in queue")) return;

        LinkedList<QueueRequest> temp = new LinkedList<>();
        temp.add(cashierQ.peek());
        qm.loadViewQueue(temp, true, "Pending Payment");

        System.out.println("=== PENDING ===");
        qm.loadViewQueue(cashierQ, false, "Pending Payment");
    }

    private void requestStatus() {
        displayRequest();

        System.out.println("----  PAUSED  ----");
        boolean found2 = qm.loadViewQueue(pauseQ, false, "Paused");
        if (!found2) {
            System.out.println("No paused payments.");
        }

        System.out.println("----  PAID  ----");
        boolean found = qm.loadViewQueue(QueueSystem.getAccountQ(), false, "Paid");
        if (!found) {
            System.out.println("No paid payments.");
        }

    }

    @Override
    public void requestManager() {
        while (true) {
            super.requestManager();
            
            int select = system.validate().minMaxXChoice("X - Go Back, 1 - Select request at top, 2 - Pause request at top, 3 - Unpause request, 4 - Cancel request", 1, 4);
            if (select == -1) return;

            switch(select) {

                // Payment
                case 1:
                paid(null, 0);
                break;

                // Pause
                case 2:
                if (qs.emptyDisplay(cashierQ, "No pending request")) break;
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
                if (qs.emptyDisplay(cashierQ, "No pending request")) break;

                if (!system.validate().confirm("Are you sure you want to cancel Request: " + cashierQ.peek().getId())) break;
                qm.dequeue("Cancelled", "CASHIER", system.genDate(), cashierQ);
                break;
            }

        }

    }

    private void paid(QueueRequest cur, int index) {

        while (true) {

            if (index == 0 && qs.emptyDisplay(cashierQ, "No pending request")) break;
            if (index == 0) cur = cashierQ.peek();

            System.out.println("== Confirm Payment ==");
            Cashier.Reciept rc = new Reciept(cur);

            double payment = 0;
            String input = "";
            while (true) {
                input = system.validate().requireText("Input payment ('x' to go back)");
                if (input.matches("[xX]")) return;

                try {
                    payment = Double.parseDouble(input);
                } 
                
                catch (Exception e) {
                    System.out.println("Invalid input. Try again.");
                    continue;
                }

                if (Double.parseDouble(cur.getPrice()) > payment) {
                    System.out.println("Insufficient Payment");
                    continue;
                }

                if (!system.validate().confirm("Confirm payment? ")) continue;
                break;
            }

            qm.moveToWindow(system.genDate() ,(index - 1));
            System.out.println("Successfully Paid, sent to accounting");
            Cashier.Reciept rec = new Reciept(cur, String.valueOf(payment));
            rec.appendReci(cur, input);
            if (system.validate().confirm("Exit? ")) return;
            index = 0;
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

                switch (select3) {
                    case "C", "c" -> {
                        if (!system.validate().confirm("Are you sure ?")) break;
                        qm.unpause("Cancelled", system.genDate(), false, (select2 - 1));
                        System.out.println("Succesfully unpaused request");
                        break;
                    }

                    case "P", "p" -> {
                        if (!system.validate().confirm("Are you sure ?")) break;
                        paid(qm.unpause(null, null, true, (select2 - 1)), select2);
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


    // TODO:pages for reciepts
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

        // Reciept and append to file
        public Reciept(QueueRequest request, String payment) {
            this(request);

            System.out.println("-".repeat(50));
            System.out.printf("   %-20s-%10s%n", request.getDocument(), "Paid: " + payment);

            double change = Double.parseDouble(payment) - Double.parseDouble(request.getPrice());
            System.out.printf("   %-20s-%10s%n", "", "Change: " + change);

        }

        // Reciept and append to file
        public Reciept() {
            display();
        }


        public final void appendReci(QueueRequest request, String payment) {
            String line = String.format("%s,%s,%s,%s,%s,%s,%s,%s", request.getId(), request.getStNum(), request.getDocument(), request.getState(), request.getWindow(), request.getDate(), request.getPrice(), payment);
            append(line);
        }

        private void display() {
            List<String> read = read();

            if (read != null) {
                for (String line : read) {
                    String parts[] = line.split(",");

                    System.out.println("-".repeat(50));
                    QueueRequest request = QueueRequest.fromLine(line);
                    Reciept r = new Reciept(request, parts[7]);
                }
            }
        }

    }

}
