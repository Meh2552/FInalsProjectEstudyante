package package1;

import java.util.*;

public class QueueSystem {

    private static DocumentManager docuMan;
    private static QueueManager queueMan;

    public DocumentManager getDocumentManager() {
        return docuMan;
    }

    public QueueManager getQueueManager() {
        return queueMan;
    }

    public QueueSystem(String id) {
        this.docuMan = new DocumentManager();
        this.queueMan = new QueueManager();

        getQueueManager().readQ();

        getQueueManager().enqueueNew(id);
    }

    private static LinkedList<QueueRequest> casQ = new LinkedList<>();
    private static LinkedList<QueueRequest> regQ = new LinkedList<>();
    private static LinkedList<QueueRequest> accQ = new LinkedList<>();

    public static LinkedList<QueueRequest> getCashierQ() {
        return casQ;
    }

    public static LinkedList<QueueRequest> getRegistarQ() {
        return regQ;
    }

    public static LinkedList<QueueRequest> getAccountQ() {
        return accQ;
    }

    public class QueueManager extends DocuHandler {

        @Override
        public String getFileName() {
            return "queue.txt";
        }

        public void skip(LinkedList<QueueRequest> from) {

            QueueRequest pos = from.peek();
            pos.setWaiting(true);
            writeQ();

        }

        // Dequeue to next window/request state 
        public void dequeue(String state, LinkedList<QueueRequest> from, LinkedList<QueueRequest> to) {

            QueueRequest pos = from.poll();
            pos.setState(state);
            to.add(pos);
            writeQ();
            
        }

        public void enqueueNew(String id) {

            List<String> all = getDocumentManager().read();
            for (String lines : all) {
                String[] part = lines.split(",");

                if (part.length > 0 && part[5].equals(id)) {

                    getCashierQ().add(new QueueRequest(id, part[1], part[2], part[3], "CASHIER", part[4]));
                    break;
                }

            }
            writeQ();
        }

        private void writeQ() {

            ArrayList<String> lines = new ArrayList<>();
            if (!getCashierQ().isEmpty()) {
                for (QueueRequest req : getCashierQ()) {
                    String form = "C:" + req.toFileLine();
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

        private void readQ() {

            List<String> lines = read();

            for (String line : lines) {

                if (line.contains("C:")) {

                    getCashierQ().add(QueueRequest.fromLine(line));
                }

                else if (line.contains("A:")) {

                    getAccountQ().add(QueueRequest.fromLine(line));
                }

                else if (line.contains("R:")) {

                    getRegistarQ().add(QueueRequest.fromLine(line));
                }

            }

        }

    }

}

