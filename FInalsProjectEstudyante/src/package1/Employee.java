package package1;

import java.util.*;

public abstract class Employee extends User
{

    private DocumentManager dm;
    private QueueSystem qs;
    private QueueSystem.QueueManager qm;

    public Employee(MainSystem system, UserRecord record) 
    {
        super(system, record);
        this.dm = system.documentManager();
        this.qs = system.queueSystem();
        this.qm = system.queueSystem().getQueueManager();
    }

    public abstract void employeeMenu();

    @Override
    public void start() 
    {
        employeeMenu();
    }
    
    public void respondToTicket() 
    {

        List<HelpdeskTicket> tickets = system.helpdeskManager().loadTickets();
        
        if (tickets.size() == 0) 
        {
            System.out.println("No tickets.");
            
            return;
        }

        while (true) 
        {
            System.out.println("=== SELECT TICKET TO RESPOND ===");
            
            for (int i = 0; i < tickets.size(); i++) 
            {
                HelpdeskTicket t = tickets.get(i);
                
                System.out.println("[" + (i+1) + "] ID:" + t.getId() + " (" + t.getStudentNum() + ") " + t.getIssue() + " | Status: " + t.getStatus() + " | " + t.getDate());
            }

            int sel = system.validate().menuChoice("Select ticket: ", tickets.size());            
            String action = system.validate().editCancelContinue();
            
            if (action.equals("EDIT"))
            {
            	continue;
            }
            
            if (action.equals("CANCEL"))
            {
            	return;
            }

            HelpdeskTicket chosen = tickets.get(sel - 1);

            String respMsg = system.validate().requireText("Type your response message: ");

            String ts = system.genDate();

            String responderName = record.getFullName();

            HelpdeskResponse resp = new HelpdeskResponse(chosen.getId(), responderName, respMsg, ts);
            system.helpdeskResponseManager().addResponse(resp);

            chosen.setStatus("Answered");
            system.helpdeskManager().saveTickets(tickets);

            System.out.println("Response saved and ticket marked Answered.");
            return;
        }
    }

    public void viewResponse() {
        String name = record.getFullName();
        List<HelpdeskResponse> list = system.helpdeskResponseManager().loadByResponder(name);

        if (list.size() == 0) {
            System.out.println("You have not responded to any tickets yet.");
            return;
        }

        System.out.println("=== YOUR HELP DESK RESPONSE HISTORY ===");

        for (HelpdeskResponse r : list) {
            System.out.println("-".repeat(50));
            System.out.println("Ticket ID: " + r.getTicketId());
            System.out.println("Date: " + r.getTime());
            System.out.println("Message:");
            System.out.println("  " + r.getMessage());
        }
    }

    public abstract void displayRequest();

    public void requestManager() {

        displayRequest();

    }

    public void history(int page, List<String> statements) {

        QueueSystem qs = new QueueSystem(1, statements);
        int current = 1;

        while (true) {
            int max = system.queueSystem().getHistoryManager().countEntry(statements);
            int input = system.validate().minMaxXChoice("Go to page ('x' to go back):", 1, max);
            if (input == current) {
                System.out.println("Already on page " + input);
                continue;
            }
            if (input == -1) {
                return;
            }

            qs = new QueueSystem(input, statements);
            current = input;

        }

    }

    public void createRequest() {
        String user, stNum = "", doc = "";

        while (true) {

            System.out.println("=== Create Request ===");
            user = system.validate().requireText("Username, Type 'D' to label as walk-in (Type 'x' to go back) : "); //TODO: ask this idk 

            if (user.matches("[xX]")) return;
            else if (user.matches("[dD]")) user = "Walk-in";

            // Confirms if username is already in the system
            else if (!system.userManager().usernameExists(user)) { //TODO: if stnum already exists, st num input is skipped
                System.out.println("Username does not exist.");
                continue;
            }

            while (true) {
            stNum = createStNum(stNum);
            if (stNum.matches("[xX]")) break;

            doc = system.validate().requireText("Type of document (Type 'x' to go back) : ");
            if (stNum.matches("[xX]")) continue;
            break;
            }

            switch (createWindow(user, stNum, doc)) {

            case 1: case -2:
            return;

            case -1: case 0:
            continue;

            }

        }

    }

    private String createStNum(String stNum) {
        while (true) {

            stNum = system.validate().requireText("Student Number (Type 'x' to go back) : ");
            if (stNum.matches("[xX]")) return "X"; 

            // Confirms if stNum is in system
            else if (!system.userManager().studentNumExists(stNum)) {
                System.out.println("Invalid Student Number");
                continue;
            }

            return stNum;

        }
    }

    private int createWindow(String user, String stNum, String doc) {
        String state = "", price = "";
        double priceD = 0;
        while (true) {

            System.out.println("----  Send to Window  ----");
            System.out.println("[1] Cashier");
            System.out.println("[2] Accounting");
            System.out.println("[3] Registrar");
            System.out.println("[X] Back");

            int input = system.validate().minMaxXChoice("Choose which window", 1, 3);

            switch(input) {

                // Cashier
                case 1:
                while (true) {
                state = system.validate().requireText("State (Type 'x' to go back) :");
                if (state.matches("[xX]")) break;

                price = system.validate().requireText("Input payment (X to go back)");
                if (price.matches("[Xx]")) continue;
                
                try {
                priceD = Double.parseDouble(price);
                } 
                
                catch (Exception e) {
                System.out.println("Invalid input. Try again.");
                continue;
                }
                break;
                }
                
                int result1 = createConfirm(doc , state, price, "Cashier", user, stNum);
                if (result1 == -2) return -2;
                else if (result1 == 0) return 0;

                createAdd(qs.getCashierQ(), user, stNum, doc, state, price, "CASHIER");
                System.out.println("Request Sucessfully created in cashier");
                return 1;


                // Accounting
                case 2:
                state = system.validate().requireText("State (Type 'x' to go back) :");
                if (state.matches("[xX]")) continue;
                
                int result = createConfirm(doc , state, "N/A", "Accounting", user, stNum);
                if (result == -2) return -2;
                else if (result == 0) return 0;

                createAdd(qs.getAccountQ(), user, stNum, doc, state, "0", "ACCOUNTING");
                System.out.println("Request Sucessfully created in accounting");
                return 1;


                // Registrar
                case 3:
                state = system.validate().requireText("State (Type 'x' to go back) :");
                if (state.matches("[xX]")) continue;
                
                int result2 = createConfirm(doc , state, "N/A", "Registrar", user, stNum);
                if (result2 == -2) return -2;
                else if (result2 == 0) return 0;

                createAdd(qs.getRegistarQ(), user, stNum, doc, state, "0", "REGISTRAR");
                System.out.println("Request Sucessfully created in registrar");
                return 1;

                // Back
                case -1:
                return -1;

            }

        }
    }

    private int createConfirm(String doc, String state, String price, String window, String user, String stNum) {
            System.out.println("----  Confirm Create Request----");
            System.out.println("Student: " + stNum + "     Username: " + user);
            System.out.println("Requesting: " + doc + "     State: " + state);
            System.out.println("Price " + price);
            System.out.println("Send to window: " + window);

            switch (system.validate().editCancelContinue()) {

                case "EDIT" -> {
                return 0;
                }

                case "CANCEL" -> {
                return -2;
                }

                case "CONTINUE" -> {
                
                }
            }
            return 1;
    }

    private void createAdd(LinkedList<QueueRequest> queue, String user, String stNum, String doc, String state, String price, String window) {

        String id = dm.genId();
        String date = system.genDate();

        DocumentRequest request = new DocumentRequest(user, stNum, doc, state, system.genDate(), id);
        dm.addRequest(request);

        QueueRequest qr = new QueueRequest(id, stNum, doc, state, window, date, price, qm.genPriority(queue));
        queue.add(qr);

        qm.writeQ();
        qs.getHistoryManager().appendHist(qr, price);
    }

    private void createAdd(PriorityQueue<QueueRequest> queue, String user, String stNum, String doc, String state, String price, String window) {

        String id = dm.genId();
        String date = system.genDate();

        DocumentRequest request = new DocumentRequest(user, stNum, doc, state, system.genDate(), id);
        dm.addRequest(request);

        QueueRequest qr = new QueueRequest(id, stNum, doc, state, window, date, price, qm.genPriority(qs.PQtoList(queue)));
        queue.add(qr);

        qm.writeQ();
        qs.getHistoryManager().appendHist(qr, price);
    }

}
