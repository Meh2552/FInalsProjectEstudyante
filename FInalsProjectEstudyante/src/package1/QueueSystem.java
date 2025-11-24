package package1;

import java.util.*;

public class QueueSystem {

    private static boolean loaded = false;

    private DocumentManager docuMan;
    private QueueManager queueMan;
    private HistoryManager histMan;

    public HistoryManager getHistoryManager() {
        return histMan;
    }

    public DocumentManager getDocumentManager() {
        return docuMan;
    }

    public QueueManager getQueueManager() {
        return queueMan;
    }

    public QueueSystem() {
        this.docuMan = new DocumentManager();
        this.queueMan = new QueueManager();
        this.histMan = new HistoryManager();

        if (!loaded) {
            this.queueMan.loadQ();
            loaded = true;
        }

    }

    public QueueSystem(String id, String price) {
        this();

        this.queueMan.enqueueNew(id, price);
    }

    public QueueSystem(int page, List<String> statements) {
        this();

        List<String> list = getHistoryManager().read();
        HistoryManager.ViewHistory vh = new HistoryManager.ViewHistory(list, page, statements);
    }

    public QueueSystem(int page) {
        this();

        List<String> list = getHistoryManager().read();
        HistoryManager.ViewHistory vh = new HistoryManager.ViewHistory(list, page);
    }


    private static LinkedList<QueueRequest> casQ = new LinkedList<>();
    private static LinkedList<QueueRequest> pauseQ = new LinkedList<>();
    private static LinkedList<QueueRequest> regQ = new LinkedList<>();
    private static LinkedList<QueueRequest> accQ = new LinkedList<>();

    public static LinkedList<QueueRequest> getCashierQ() {
        return casQ;
    }

    public static LinkedList<QueueRequest> getPauseQ() {
        return pauseQ;
    }

    public static LinkedList<QueueRequest> getRegistarQ() {
        return regQ;
    }

    public static LinkedList<QueueRequest> getAccountQ() {
        return accQ;
    }

    // Used for display when queue is empty
    protected boolean emptyDisplay(LinkedList<QueueRequest> queue, String message) {
        if (queue.isEmpty()) {
            System.out.println(message);
            return true;
        }
        return false;
    }

    // ~~~~~~~~~~~~~~~~~~~~
    // Queue Manager Class
    // ~~~~~~~~~~~~~~~~~~~~
    public class QueueManager extends DocuHandler {

        @Override
        public String getFileName() {
            return "queue.txt";
        }
        
        private void writeQ() {

            ArrayList<String> lines = new ArrayList<>();
            if (!getCashierQ().isEmpty()) {
                for (QueueRequest req : getCashierQ()) {
                    String form = req.toFileLine();
                    lines.add(form);
                }
            }

            if (!getPauseQ().isEmpty()) {
                for (QueueRequest req : getPauseQ()) {
                    String form = req.toFileLine();
                    lines.add(form);
                }
            }

            if (!getAccountQ().isEmpty()) {
                for (QueueRequest req : getAccountQ()) {
                    String form = req.toFileLine();
                    lines.add(form);
                }
            }

            if (!getRegistarQ().isEmpty()) {
                for (QueueRequest req : getRegistarQ()) {
                    String form =  req.toFileLine();
                    lines.add(form);
                }
            }

            write(lines);
        }

        public void writeChange(QueueRequest request) { // Writes to queue file, history file and docu
            writeQ();
            getHistoryManager().appendHist(request);
            getDocumentManager().changeState(request.getId(), request.getState());
        }

        private void loadQ() {

            List<String> lines = read();

            for (String line : lines) {

                if (line.contains("CASHIER")) {

                    getCashierQ().add(QueueRequest.fromLine(line));
                } else if (line.contains("ACCOUNTING")) {

                    getAccountQ().add(QueueRequest.fromLine(line));
                } else if (line.contains("PAUSED")) {

                    getPauseQ().add(QueueRequest.fromLine(line));
                } else if (line.contains("REGISTRAR")) {

                    getRegistarQ().add(QueueRequest.fromLine(line));
                }

            }
        }

        // Updates relevant information inside a request
        private void updateInfo(QueueRequest request, String state, String window, String date) {
            request.setState(state);
            request.setWindow(window);
            request.setDate(date);
        }

        // Pauses a request
        public void pause(String date) {

            if (emptyDisplay(getCashierQ(), "No such requests found")) return;

            QueueRequest pos = getCashierQ().poll();
            updateInfo(pos, "Paused", "PAUSED", date);
            getPauseQ().add(pos);
            writeChange(pos);

        }

        // Unpauses the 'n'th request that is in pause queue, use menuChoice for index
        // example index is 3, it removes the 'n'th (3rd) request in the pause queue
        public QueueRequest unpause(String state, String date, boolean wasPaid, int index) {

            if (emptyDisplay(getPauseQ(), "No such requests found")) return null;

            QueueRequest pos = getPauseQ().get(index);
            
            if (wasPaid) {
                return pos;
            }

            else updateInfo(pos, state, "CASHIER", date);
            
            getPauseQ().remove(index);
            writeChange(pos);
            return null;
        }

        // Move request to next window/request state 
        public void moveToWindow(String state, String window, String date, LinkedList<QueueRequest> from, LinkedList<QueueRequest> to) {

            if (emptyDisplay(from, "No such requests found")) return;

            QueueRequest pos = from.poll();
            to.add(pos);
            updateInfo(pos, state, window, date);
            writeChange(pos);
            
        }

        // Move request in cashier or paused queue, 0 for cashier, other means pause
        public void moveToWindow(String date, int index) {

            QueueRequest pos;
            if (index == -1) pos = getCashierQ().poll();
            else pos = getPauseQ().remove(index);
            
            getAccountQ().add(pos);
            updateInfo(pos, "Paid", "ACCOUNTING", date);
            writeChange(pos);

        }

        // Dequeues request at head
        public void dequeue(String state, String window, String date, LinkedList<QueueRequest> queue) {

            if (emptyDisplay(queue, "No such requests found")) return;

            QueueRequest pos = queue.poll();
            updateInfo(pos, state, window, date);
            writeChange(pos);

        }

        public void enqueueNew(String id, String price) {

            List<String> all = getDocumentManager().read();

            for (String lines : all) {
                String[] part = lines.split(",");

                if (part.length > 0 && part[5].equals(id)) {
                    QueueRequest qr = new QueueRequest(id, part[1], part[2], part[3], "CASHIER", part[4], price);
                    getCashierQ().add(qr);
                    writeQ();
                    getHistoryManager().appendHist(qr, price);
                    break;
                }

            }
            
        }


        public List<QueueRequest> lookForState(String state, LinkedList<QueueRequest> queue) {
            ArrayList<QueueRequest> qr = new ArrayList<>();

            for (QueueRequest request : queue) {
                if (emptyDisplay(queue, "No such requests found")) return null;
                else if (request.getState().equals(state)) {
                    qr.add(request);
                }
            }
            if (qr.isEmpty()) return null;
            return qr;
        }

        public List<List<QueueRequest>> queueStates(List<QueueRequest> queue) {
            ArrayList<String> states = new ArrayList<>();
            ArrayList<QueueRequest> qr;
            ArrayList<List<QueueRequest>> stateList = new ArrayList<>();

            for (QueueRequest request : queue) {
                if (queue == null ||queue.isEmpty()) {
                    return null; 
                }
                
                else if (!states.contains(request.getState())) {
                    qr = new ArrayList<>();
                    String compare = request.getState();

                    for (QueueRequest request2 : queue) {
                        if (request.getState().equals(compare)) {
                            qr.add(request2);
                        }
                    }

                    stateList.add(qr);
                    states.add(request.getState());
                }
            }

            Collections.sort(stateList, (a, b) ->
                a.get(0).getState().compareToIgnoreCase(b.get(0).getState())
            );
            return stateList;
        }

        // Load certain states of request in queue, returns true if queue isn't empty
        public boolean loadViewQueue(LinkedList<QueueRequest> queue, boolean willDisplayHeader, String state) {
            if (queue == null || queue.isEmpty()) return false;
            List<QueueRequest> filtered = lookForState(state, queue);
            if (filtered == null || filtered.isEmpty()) return false;
            ViewQueue vq = new ViewQueue(filtered, willDisplayHeader);
            return true;
        }


        // Load all states of a queue, returns true if queue isn't empty
        public boolean loadViewQueue(List<QueueRequest> queue) {
            if (queue == null || queue.isEmpty()) return false;
            ViewQueue vq = new ViewQueue(queueStates(queue));
            return true;
        }


        public static class ViewQueue {

            public ViewQueue(List<QueueRequest> queue, boolean willDisplayHeader) {

                if (willDisplayHeader) viewDisplay();

                for (QueueRequest request : queue) {
                    requestFormat(request);
                }

            }

            public ViewQueue(List<List<QueueRequest>> queue) {

                viewDisplay();

                for (List<QueueRequest> stateList : queue) {

                    for (QueueRequest request : stateList) {
                        requestFormat(request);
                    }
                }
            }

            // UI
            public void viewDisplay() {
                System.out.printf("  %-7s  |  %-12s  |  %-25s  |   %-20s  |  %-7s  |  %s%n", "Request", "Student","Document", "Date & Time", "Price", " Status");
            }


            public void requestFormat(QueueRequest request) {

                System.out.printf("  %-7s  |  %-12s  |  %-25s  |   %-20s  |  %-7s  |  %s%n", request.getId(), request.getStNum(), request.getDocument(), request.getDate(), "", request.getState());

            }

        }
            
    }

    // ~~~~~~~~~~~~~~~~~~~~
    // History Manager Class
    // ~~~~~~~~~~~~~~~~~~~~
    public class HistoryManager extends DocuHandler {

        @Override
        public String getFileName() {
            return "history.txt";
        }

        public void appendHist(QueueRequest request) {
            String line = String.format("%s,%s,%s,%s,%s,%s", request.getWindow(), request.getId(), request.getStNum(),request.getDocument(), request.getDate(), request.getState());
            append(line);
        }

        // For cashier payments
        public void appendHist(QueueRequest request, String payment) {
            String line = String.format("%s,%s,%s,%s,%s,%s,%s", "CASHIER", request.getId(), request.getStNum(),request.getDocument(), request.getDate(), request.getState(), payment);
            append(line);
        }

        public int countEntry(List<String> statements) {
            List<String> list = read();

            int i = 0;
            for (String line : list) {
                for (String tag : statements) {
                if (line.contains(tag)) {
                    i++;
                    break;
                }
                }
            }
            i = (i + 24) / 25;
            return i;
        }

        public int countEntry() {
            List<String> list = read();

            int i = (list.size() + 24) / 25;
            return i;
        }

        // TODO: backwards display para latest una
        public static class ViewHistory {

            // TODO: history chage to medyo 
            // Display all
            public ViewHistory(List<String> list, int page) {

                if (list.isEmpty()) {
                    empty();
                    return;
                }

                viewDisplay();
                int pageCount = (list.size() + 24) / 25; // ceiling division idk nahanap ko lang
                int items = 0;
                for (String line : list) {
                    items++;

                    // Checks items' upperbound and lowerbound
                    if (items < (page - 1) * 25) {
                        continue; 
                    }else if (items >= page * 25) {
                        break;
                    }

                    request(line);

                }

                prompt(pageCount, page);
            }

            // Display some, filters for lines that contain ONE of the statements, not 
            public ViewHistory(List<String> list, int page, List<String> statements) {

                if (list.isEmpty()) {
                    empty();
                    return;
                }

                ArrayList<String> filtered = new ArrayList<>();

                for (String line : list) {
                    for (String tag : statements) {
                    if (checkStatement(tag, line)) {
                        filtered.add(line);
                        break;
                    }
                    }
                }


                if (filtered.isEmpty()) {
                    empty();
                    return;
                }

                viewDisplay();
                
                int items = 0;
                int pageCount = (filtered.size() + 24) / 25;

                for (String line : filtered) {

                    items++;

                    // Checks items' upperbound and lowerbound
                    if (items < (page - 1) * 25) {
                        continue; 
                    }else if (items >= page * 25) {
                        break;
                    }

                    request(line);

                }

                prompt(pageCount, page);
            }

            // UI
            private void viewDisplay() {
                System.out.printf("  %-13s  |  %-7s  |  %-12s  |  %-15s  |   %-15s  |  %-7s  |  %s%n", "Window", "Request", "Student", "Document", "Date & Time", "Price", " Status");
            }

            private void empty() {
                System.out.println("----------------------------");
                System.out.println("     No items in history to be read");
                System.out.println("-----------------------------");
            }

            private void request(String item) {
                String parts[] = item.split(",");

                if (parts.length == 7) {
                    System.out.printf("  %-13s  |  %-7s  |  %-12s  |  %-25s  |   %-20s  |  %-7s  |  %s%n", parts[0], parts[1], parts[2], parts[3], parts[4], parts[6], parts[5]);
                } else {
                    System.out.printf("  %-13s  |  %-7s  |  %-12s  |  %-25s  |   %-20s  |  %-7s  |  %s%n", parts[0], parts[1], parts[2], parts[3], parts[4], "", parts[5]);
                }
            }

            private void prompt(int pageCount, int page) {
                System.out.println("----------------------------");
                if (pageCount == 1) {
                    System.out.println(" Viewing page " + page);
                } else {
                    System.out.println(" Viewing page " + page + " out of " + pageCount);
                }

            }

            private boolean checkStatement(String text, String line) {
                if (line.contains(text)) {
                    return true; 
                }else {
                    return false;
                }
            }

        }
    }
}




