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
            System.out.println("=== HELPDESK QUEUE ===");
            for (int i=0;i<tickets.size();i++)
            {
                HelpdeskTicket t = tickets.get(i);
                String assigned = (t.getAssignedWindow() == null || t.getAssignedWindow().isEmpty()) ? "" : " | Assigned: " + t.getAssignedWindow();
                System.out.println("[" + (i+1) + "] ID:" + t.getId() + " (" + t.getStudentNum() + ") " + t.getIssue() + " | " + t.getDate() + " | Status: " + t.getStatus() + assigned);
            }

            int sel = system.validate().menuChoice("Select ticket: ", tickets.size());
            String action = system.validate().editCancelContinue();
            if (action.equals("EDIT")) continue;
            if (action.equals("CANCEL")) return;

            HelpdeskTicket chosen = tickets.get(sel - 1);

            System.out.println("Selected Ticket ID:" + chosen.getId() + " Issue: " + chosen.getIssue());
            String respMsg = system.validate().requireText("Type your response message: ");
            String ts = system.genDate();
            String responderName = record.getFullName();
            HelpdeskResponse resp = new HelpdeskResponse(chosen.getId(), responderName, respMsg, ts);
            system.helpdeskResponseManager().addResponse(resp);

            chosen.setStatus("Answered");
            system.helpdeskManager().saveTickets(tickets);

            System.out.println("=== RESPONSE CONFIRMED ===");
            System.out.println("You replied at: " + ts);
            System.out.println("Message: " + respMsg);
            System.out.println("-".repeat(50));
            return;
        }
    }

    public void viewResponse()
    {
        String name = record.getFullName();
        List<HelpdeskResponse> list = system.helpdeskResponseManager().loadByResponder(name);

        if (list.size() == 0)
        {
            System.out.println("You have not responded to any tickets yet.");
            return;
        }

        while (true)
        {
            System.out.println("=== YOUR HELP DESK RESPONSE HISTORY ===");
            System.out.println("[1] Sort by time (oldest first)");
            System.out.println("[2] Sort by time (newest first)");
            System.out.println("[3] Back");
            int choice = system.validate().menuChoice("Choose: ", 3);
            
            if (choice == 3) return;

            if (choice == 1)
            {
                Collections.sort(list, (a,b) -> a.getTime().compareTo(b.getTime()));
            }
            else
            {
                Collections.sort(list, (a,b) -> b.getTime().compareTo(a.getTime()));
            }

            for (HelpdeskResponse r : list)
            {
                System.out.println("-".repeat(50));
                System.out.println("Ticket ID: " + r.getTicketId());
                System.out.println("Date: " + r.getTime());
                System.out.println("Responder: " + r.getRespond());
                System.out.println("Rating: " + (r.getRating() >= 1 ? r.getRating() : "Unrated"));
                System.out.println("Message:");
                System.out.println("  " + r.getMessage());
            }
        }
    }

    public abstract void displayRequest();

    public void requestManager() 
    {

        displayRequest();

    }

    public void history(int page, List<String> statements, boolean showPrice) {

        QueueSystem.HistoryManager hm = qs.new HistoryManager();
        hm.displayViewHistory(1, statements, showPrice);
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

            hm.displayViewHistory(input, statements, showPrice);
            current = input;

        }

    }

    public int requireExpiry() {
        while (true) {
            int expiry = system.validate().requireInt("Days until expiry: (x to go back)");
            if (expiry == -7) return -7;

            else if (expiry < 1) {
                System.out.println("Input must be at least 1");
                continue;
            }

            return expiry;
        }

    }

    public void createRequest() {
        String user = "", stNum = "", doc = "", state = "";
        double price = 0;
        int expiry = 0;
        UserRecord userRec;

        int step = 1;
        int window = 0;
        System.out.println("=== Create Request ===");

        while (step <= 7) {

            switch (step) {

                // User step
                case 1:
                user = system.validate().requireText("Username, Type 'D' to label as walk-in (Type 'x' to go back) : "); 

                if (user.matches("[xX]")) return;
                else if (user.matches("[dD]")) {
                    user = "Walk-in";
                    step = 0;
                    break;
                }

                // Confirms if username is already in the system
                else if (system.userManager().usernameExists(user)) {  
                    userRec = loadRecord(user);
                    if (userRec == null) {
                        System.out.println("Invalid Username");
                        break;
                    }

                    step++;
                    stNum = userRec.getStudentNum();
                    System.out.println("Associated student number found: " + stNum);
                    break;
                }
                
                System.out.println("Username does not exist");
                break;


                // Student Number (0 because optional if the username is already found)
                case 0:
                stNum = system.validate().requireText("Student Number (Type 'x' to go back) : ");

                if (stNum.matches("[xX]")) {
                    step = 1;
                    break;
                }

                else if (stNum.matches("[0-9]{11}")) {
                    stNum = String.format("%s-%s", stNum.substring(0,5), stNum.substring(5));
                }

                // Confirms if stNum is in system
                if (system.userManager().studentNumExists(stNum)) {
                    step = 2;
                    break;
                }

                System.out.println("Invalid Student Number");
                break;


                // Document
                case 2:
                doc = system.validate().requireText("Type of document (Type 'x' to go back) : ");

                if (doc.matches("[xX]")) {
                    step = 1;
                    break;
                }

                step++;
                break;


                // Window
                case 3:
                window = createWinSelect();

                if (window != -1) step++;
                else step--;
        
                break;


                // State
                case 4:
                state = system.validate().requireText("Enter request state (X to go back)");
                if (state.matches("[Xx]")) return;
                step++;
                break;


                // Price
                case 5:
                if (window == 1) {
                    price = system.validate().requireDouble("Input payment (X to go back)");
                    if (price == -7) {
                        step--;
                        continue;
                    }

                    else if (price <= 0) {
                        System.out.println("Invalid price");
                    }
                }

                step++;
                break;


                // Expire
                case 6:
                expiry = requireExpiry();
                if (expiry == -7) {
                    step--;
                    continue;
                }

                step++;
                break;


                // Confirm created
                case 7:
                int result = -2;
                
                if (window == 1) {
                    result = createConfirm(doc, state, String.valueOf(price), "Cashier", user, stNum, expiry);
                }
                else if (window == 2) {
                    result = createConfirm(doc, state, "N/A", "Accounting", user, stNum, expiry);
                }
                else if (window == 3) {
                    result = createConfirm(doc, state, "N/A", "Registrar", user, stNum, expiry);
                }
                
                if (result == 0) {
                    System.out.println("Create request cancelled");
                    return;
                }
                else if (result == -1) {
                    step--;
                    break;
                }

                step++;
                break;
            }

        }

        switch (window) {

            // Cashier
            case 1:
            createAdd(qs.getCashierQ(), user, stNum, doc, state, String.valueOf(price), "CASHIER", system.genDate(expiry));
            System.out.println("Request sucessfully created in cashier");
            break;

            // Accounting
            case 2:
            createAdd(qs.getAccountQ(), user, stNum, doc, state, "0", "ACCOUNTING", system.genDate(expiry));
            System.out.println("Request sucessfully created in accounting");
            break;

            // Registrar
            case 3:
            createAdd(qs.getRegistarQ(), user, stNum, doc, state, "0", "REGISTRAR", system.genDate(expiry));
            System.out.println("Request sucessfully created in registrar");
            break;
        }

    }

    private int createWinSelect() {
        System.out.println("----  Send to Window  ----");
        System.out.println("[1] Cashier");
        System.out.println("[2] Accounting");
        System.out.println("[3] Registrar");
        System.out.println("[X] Back");

        int input = system.validate().minMaxXChoice("Choose which window", 1, 3);
        return input;
    }

    private int createConfirm(String doc, String state, String price, String window, String user, String stNum, int expiry) {
        System.out.println("----  Confirm Create Request----");
        System.out.println("Student: " + stNum + "     Username: " + user);
        System.out.println("Requesting: " + doc + "     State: " + state);
        System.out.println("Price " + price);
        System.out.println("Send to window: " + window);
        System.out.println("Will expire in " + expiry + " day/s");

        switch (system.validate().editCancelContinue()) {

            case "EDIT" -> {
            return -1;
            }

            case "CANCEL" -> {
            return 0;
            }

            case "CONTINUE" -> {
                
            }
        }
        return 1;
    }

    private void createAdd(LinkedList<QueueRequest> queue, String user, String stNum, String doc, String state, String price, String window, String expiry) {

        String id = dm.genId();
        String date = system.genDate();

        DocumentRequest request = new DocumentRequest(user, stNum, doc, state, system.genDate(), id);
        dm.addRequest(request);

        QueueRequest qr = new QueueRequest(id, stNum, doc, state, window, date, price, qm.genPriority(queue), expiry);
        queue.add(qr);

        qm.writeQ();
        qs.getHistoryManager().appendHist(qr, price);
    }

    private void createAdd(PriorityQueue<QueueRequest> queue, String user, String stNum, String doc, String state, String price, String window, String expiry) {

        String id = dm.genId();
        String date = system.genDate();

        DocumentRequest request = new DocumentRequest(user, stNum, doc, state, system.genDate(), id);
        dm.addRequest(request);

        QueueRequest qr = new QueueRequest(id, stNum, doc, state, window, date, price, qm.genPriority(qs.PQtoList(queue)), expiry);
        queue.add(qr);

        qm.writeQ();
        qs.getHistoryManager().appendHist(qr, price);
    }

    private UserRecord loadRecord(String username) {

        String line = system.userManager().readUserLine(username);
        String parts[] = line.split(",");
        if (!parts[2].equals("STUDENT")) return null;
        UserRecord rec = new UserRecord(parts[0], parts[1], parts[3], parts[4]);
        return rec;

    }

    public void changeExpiry(QueueRequest request) {
        while (true) {

            int days = requireExpiry();
            if (days == -7) {
                return;
            }

            String date = system.genDate(days);

            System.out.println("----  Confirm Changes ----");
            System.out.println("Former Expiry: " + request.getExpiry());
            System.out.println("New Expiry " + date);

            switch (system.validate().editCancelContinue()) {

                case "EDIT" -> {
                    continue;
                }

                case "CANCEL" -> {
                    return;
                }

                case "CONTINUE" -> {
                }
            }

            request.setExpiry(date);
            qm.writeQ();
            System.out.println("Request expiration date changed");
            return;

        }
    }
}
