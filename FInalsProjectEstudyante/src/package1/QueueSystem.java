package package1;

import java.util.*;

public class QueueSystem {

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

        this.queueMan.loadQ();

    }

    public QueueSystem(String id) {
        this();

        this.queueMan.enqueueNew(id);
    }

    public QueueSystem(int page, String window) {
        this();

        List<String> list = getHistoryManager().read();
        HistoryManager.ViewHistory vh = new HistoryManager.ViewHistory(list, 1, window);
    }

    public QueueSystem(int page) {
        this();

        List<String> list = getHistoryManager().read();
        HistoryManager.ViewHistory vh = new HistoryManager.ViewHistory(list, 1);
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
    protected boolean emptyDisplay(LinkedList<QueueRequest> queue) {
        if (queue.isEmpty()) {
            System.out.println("The queue is empty");
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

        public void writeChange(QueueRequest request) { // Writes to queue file and history file
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

            if (emptyDisplay(getCashierQ())) return;

            QueueRequest pos = getCashierQ().poll();
            updateInfo(pos, "Paused", "CASHIER", date);
            getPauseQ().add(pos);
            writeChange(pos);

        }

        // Unpauses the 'n'th request that is in pause queue, use menuChoice for index
        // example index is 3, it removes the 'n'th (3rd) request in the pause queue
        public void unpause(String state, String date, boolean wasPaid, int index) {

            if (emptyDisplay(getPauseQ())) return;

            QueueRequest pos = getPauseQ().remove(index);
            
            if (wasPaid) {
                updateInfo(pos, "Paid", "ACCOUNTING", date);
                getAccountQ().add(pos);
            }

            else updateInfo(pos, state, "CASHIER", date);
            
            writeChange(pos);

        }

        // Move request to next window/request state 
        public void moveToWindow(String state, String window, String date, LinkedList<QueueRequest> from, LinkedList<QueueRequest> to) {

            if (emptyDisplay(from)) return;

            QueueRequest pos = from.poll();
            to.add(pos);
            updateInfo(pos, state, window, date);
            writeChange(pos);
            
        }

        // Dequeues request at head
        public void dequeue(String state, String window, String date, LinkedList<QueueRequest> queue) {

            if (emptyDisplay(queue)) return;

            QueueRequest pos = queue.poll();
            updateInfo(pos, state, window, date);
            writeChange(pos);

        }

        public void enqueueNew(String id) {

            List<String> all = getDocumentManager().read();

            for (String lines : all) {
                String[] part = lines.split(",");

                if (part.length > 0 && part[5].equals(id)) {
                    QueueRequest qr = new QueueRequest(id, part[1], part[2], part[3], "CASHIER", part[4]);
                    getCashierQ().add(qr);
                    writeChange(qr);
                    break;
                }

            }
            
        }


        public List<QueueRequest> lookForState(String state, LinkedList<QueueRequest> queue) {
            ArrayList<QueueRequest> qr = new ArrayList<>();

            for (QueueRequest request : queue) {
                if (emptyDisplay(queue)) return null;
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
                if (queue.isEmpty()) {
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

            return stateList;
        }

        public static class ViewQueue {

            public ViewQueue(List<QueueRequest> queue, boolean willDisplayHeader) {
                
                if (queue.isEmpty()) {
                    empty();
                    return;
                }

                if (willDisplayHeader) viewDisplay();

                for (QueueRequest request : queue) {
                    requestFormat(request);
                }

            }

            public ViewQueue(List<List<QueueRequest>> queue) {

                if (queue.isEmpty()) {
                    empty();
                    return;
                }

                viewDisplay();

                for (List<QueueRequest> stateList : queue) {

                    for (QueueRequest request : stateList) {
                        requestFormat(request);
                    }
                }
            }

            // UI
            private void viewDisplay() {
                System.out.println("------------------");
                System.out.println("     Request Queue");
                System.out.println("------------------");
                System.out.printf("  %-7s  |  %-12s  |  %-15s  |   %-15s  |  %-7s  |  %s%n", "Request", "Student","Document", "Date & Time", "Price", " Status");
            }

            private void empty() {
                System.out.println("----------------------------");
                System.out.println("     No items in queue to be read");
                System.out.println("-----------------------------");
            }

            private void requestFormat(QueueRequest request) {

                System.out.printf("  %-7s  |  %-12s  |  %-15s  |   %-15s  |  %-7s  |  %s%n", request.getId(), request.getStNum(), request.getDocument(), request.getDocument(), "", request.getState());

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

        // For cashier payments TODO:ibahin data type payment if necessary
        public void appendHist(QueueRequest request, float payment) {
            String line = String.format("%s,%s,%s,%s,%s,%s,%f", "CASHIER", request.getId(), request.getStNum(),request.getDocument(), request.getDate(), request.getState(), payment);
            append(line);
        }

        public int countEntry(String window) {
            List<String> list = read();

            int i = 0;
            for (String line : list) {
                if (line.contains(window)) {
                    i++;
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

        public static class ViewHistory {

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
                    if (items > (page - 1) * 25) {
                        continue; 
                    }else if (items <= page * 25) {
                        return;
                    }

                    request(line);

                }

                prompt(pageCount, page);
            }

            // Display some
            public ViewHistory(List<String> list, int page, String window) {

                if (list.isEmpty()) {
                    empty();
                    return;
                }

                ArrayList<String> filtered = new ArrayList<>();
                for (String line : list) {
                    if (checkWindow(window, line)) {
                        filtered.add(line);
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
                        return;
                    }

                    request(line);

                }

                prompt(pageCount, page);
            }

            // UI
            private void viewDisplay() {
                System.out.println("------------------");
                System.out.println("     History");
                System.out.println("------------------");
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
                    System.out.printf("  %-13s  |  %-7s  |  %-12s  |  %-15s  |   %-15s  |  %-7s  |  %s%n", parts[0], parts[1], parts[2], parts[3], parts[4], parts[6], parts[5]);
                } else {
                    System.out.printf("  %-13s  |  %-7s  |  %-12s  |  %-15s  |   %-15s  |  %-7s  |  %s%n", parts[0], parts[1], parts[2], parts[3], parts[4], "", parts[5]);
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

            private boolean checkWindow(String window, String line) {
                if (line.contains(window)) {
                    return true; 
                }else {
                    return false;
                }
            }

        }
    }
}




