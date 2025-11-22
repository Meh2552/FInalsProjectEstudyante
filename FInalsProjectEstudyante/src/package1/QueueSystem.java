package package1;

import java.util.*;

public class QueueSystem {

    private DocumentManager docuMan;
    private QueueManager queueMan;
    private HistoryManager histMan;

    public HistoryManager getHistMan() {
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

        getQueueManager().loadQ();

    }

    public QueueSystem(String id) {
        this();

        getQueueManager().enqueueNew(id);
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
        if (getCashierQ().isEmpty()) {
            System.out.println("The queue is empty");
            return true;
        }
        return false;
    }

    // ~~~~~~~~~~~~~~~~~~~~
    // Queue Manager Class
    // ~~~~~~~~~~~~~~~~~~~~
    public class QueueManager extends DocuHandler {

        private QueueSystem.HistoryManager  histMan = new HistoryManager();

        public QueueSystem.HistoryManager getHistMan() {
            return histMan;
        }

        @Override
        public String getFileName() {
            return "queue.txt";
        }
        
        private void writeQ() {

            ArrayList<String> lines = new ArrayList<>();
            if (!getCashierQ().isEmpty()) {
                for (QueueRequest req : getCashierQ()) {
                    String form = "C:" + req.toFileLine();
                    lines.add(form);
                }
            }

            if (!getPauseQ().isEmpty()) {
                for (QueueRequest req : getPauseQ()) {
                    String form = "P:" + req.toFileLine();
                    lines.add(form);
                }
            }

            if (!getAccountQ().isEmpty()) {
                for (QueueRequest req : getAccountQ()) {
                    String form = "A:" + req.toFileLine();
                    lines.add(form);
                }
            }

            if (!getRegistarQ().isEmpty()) {
                for (QueueRequest req : getRegistarQ()) {
                    String form = "R:" + req.toFileLine();
                    lines.add(form);
                }
            }

            write(lines);
        }

        public void writeChange(QueueRequest request) { // Writes to queue file and history file
            writeQ();
            getHistMan().appendHist(request);
        }

        private void loadQ() {

            List<String> lines = read();

            for (String line : lines) {

                if (line.contains("C:")) {

                    getCashierQ().add(QueueRequest.fromLine(line));
                } else if (line.contains("A:")) {

                    getAccountQ().add(QueueRequest.fromLine(line));
                } else if (line.contains("P:")) {

                    getPauseQ().add(QueueRequest.fromLine(line));
                } else if (line.contains("R:")) {

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

            QueueRequest pos = getCashierQ().peek();
            updateInfo(pos, "Paused", "CASHIER", date);
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

    }

    // ~~~~~~~~~~~~~~~~~~~~
    // Queue Manager Class
    // ~~~~~~~~~~~~~~~~~~~~
    public class HistoryManager extends DocuHandler {

        @Override
        public String getFileName() {
            return "history.txt";
        }

        //TODO: eto`
        public void displayHistory(String fromWindow) {
            read();
        }

        public void appendHist(QueueRequest request) {
            String line = String.format("%s,%s,%s,%s,%s", request.getWindow(), request.getId(), request.getStNum(), request.getDate(),request.getState());
            append(line);
        }

        // For cashier payments TODO:ibahin data type payment if necessary
        public void appendHist(QueueRequest request, float payment) {
            String line = String.format("%s,%s,%s,%s,%s,%f", "CASHIER", request.getId(), request.getStNum(), request.getDate(), request.getState(), payment);
            append(line);
        }
    }

}

