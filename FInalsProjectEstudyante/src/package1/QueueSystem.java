package package1;

import java.time.*;
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

    public QueueSystem(String id, String price, String date) {
        this();

        this.queueMan.enqueueNew(id, price, date);
    }

    private static PriorityQueue<QueueRequest> casQ = new PriorityQueue<>(( a, b ) -> Integer.parseInt(a.getPriority()) - Integer.parseInt(b.getPriority()));
    private static LinkedList<QueueRequest> pauseQ = new LinkedList<>();
    private static LinkedList<QueueRequest> regQ = new LinkedList<>();
    private static PriorityQueue<QueueRequest> accQ = new PriorityQueue<>((a, b) -> Integer.parseInt(a.getPriority()) - Integer.parseInt(b.getPriority()));

    public static PriorityQueue<QueueRequest> getCashierQ() {
        return casQ;
    }

    public static LinkedList<QueueRequest> getPauseQ() {
        return pauseQ;
    }

    public static LinkedList<QueueRequest> getRegistarQ() {
        return regQ;
    }

    public static PriorityQueue<QueueRequest> getAccountQ() {
        return accQ;
    }

    protected List<QueueRequest> PQtoList(PriorityQueue<QueueRequest> queue) {
        ArrayList<QueueRequest> list = new ArrayList<>(queue);
        return list;
    }

    // ~~~~~~~~~~~~~~~~~~~~
    // Queue Manager Class
    // ~~~~~~~~~~~~~~~~~~~~
    public class QueueManager extends DocuHandler {

        @Override
        public String getFileName() {
            return "queue.txt";
        }

        public String genPriority(List<QueueRequest> queue) {
            int i = 1;
            for (QueueRequest request : queue) {

                int check = Integer.parseInt(request.getPriority());
                if (i <= check) {
                    i = check;
                }

                i++;

            }

            String out = String.valueOf(i);
            return out;
        }
        
        public void writeQ() {

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
            getCashierQ().clear();
            getAccountQ().clear();
            getRegistarQ().clear();

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
        private void updateInfo(QueueRequest request, String state, String window, String date, String priority, String expiry) {
            request.setState(state);
            request.setWindow(window);
            request.setDate(date);
            request.setPriority(priority);
            request.setExpiry(expiry);
        }

        // Pauses a request
        public void pause(String date) {

            if (getCashierQ().isEmpty()) return;

            QueueRequest pos = getCashierQ().poll();
            updateInfo(pos, "Paused", "PAUSED", date, genPriority(getPauseQ()), pos.getExpiry());
            getPauseQ().add(pos);
            writeChange(pos);

        }


        // Move request to next window/request state 
        public void moveToWindow(String state, String window, String date, PriorityQueue<QueueRequest> from, PriorityQueue<QueueRequest> to, String expiry) {

            QueueRequest pos = from.poll();
            updateInfo(pos, state, window, date, genPriority(PQtoList(to)), expiry);
            to.add(pos);
            writeChange(pos);
            
        }

        public void moveToWindow(String state, String window, String date, PriorityQueue<QueueRequest> from, LinkedList<QueueRequest> to, String expiry) {

            QueueRequest pos = from.poll();
            updateInfo(pos, state, window, date, genPriority(to), expiry);
            to.add(pos);
            writeChange(pos);

        }

        public void moveToWindow(String state, String window, String date, LinkedList<QueueRequest> from, PriorityQueue<QueueRequest> to, int index, String expiry) {

            QueueRequest pos = from.remove(index);
            
            to.add(pos);
            updateInfo(pos, state, window, date, genPriority(PQtoList(to)), expiry);
            writeChange(pos);

        }

        public void moveToWindow(String state, String window, String date, LinkedList<QueueRequest> from, LinkedList<QueueRequest> to, int index, String expiry) {

            QueueRequest pos = from.remove(index);
            
            to.add(pos);
            updateInfo(pos, state, window, date, genPriority(to), expiry);
            writeChange(pos);

        }


        // Dequeues request at head
        public void dequeue(String state, String window, String date, LinkedList<QueueRequest> queue, int index) {

            QueueRequest pos = queue.remove(index);
            updateInfo(pos, state, window, date, "0", pos.getExpiry());
            writeChange(pos);

        }

        public void dequeue(String state, String window, String date, PriorityQueue<QueueRequest> queue) {

            QueueRequest pos = queue.poll();
            updateInfo(pos, state, window, date, "0", pos.getExpiry());
            writeChange(pos);

        }

        public void enqueueNew(String id, String price, String expiry) {

            List<String> all = getDocumentManager().read();

            for (String lines : all) {
                String[] part = lines.split(",");

                if (part.length > 0 && part[5].equals(id)) {
                    QueueRequest qr = new QueueRequest(id, part[1], part[2], part[3], "CASHIER", part[4], price, genPriority(PQtoList(casQ)), expiry);
                    getCashierQ().add(qr);
                    writeQ();
                    getHistoryManager().appendHist(qr, price);
                    break;
                }

            }
            
        }


        public List<QueueRequest> lookForState(String state, List<QueueRequest> queue) {
            ArrayList<QueueRequest> qr = new ArrayList<>();

            for (QueueRequest request : queue) {
                if (queue.isEmpty()) return null;
                else if (request.getState().equals(state)) {
                    qr.add(request);
                }
            }
            if (qr.isEmpty()) return null;
            return qr;
        }


        // Load certain states of request in queue, returns true if queue isn't empty
        public boolean loadViewQueue(List<QueueRequest> queue, boolean willDisplayHeader, String state, boolean skipFirst, boolean showPrice, boolean reverse, int limit) {
            if (queue == null || queue.isEmpty()) return false;
            List<QueueRequest> filtered = lookForState(state, queue);
            if (filtered == null || filtered.isEmpty()) return false;

            if (reverse) Collections.reverse(filtered);
            ViewQueue vq = new ViewQueue(filtered, willDisplayHeader, skipFirst, showPrice, limit);
            return true;
        }


        // Load all states of a queue, returns true if queue isn't empty
        public boolean loadViewQueue(List<QueueRequest> queue, boolean skipFirst, boolean showPrice, boolean reverse, int limit) {
            if (queue == null || queue.isEmpty()) return false;
            if (reverse) Collections.reverse(queue);
            ViewQueue vq = new ViewQueue(queue, true, skipFirst, showPrice, limit);
            return true;
        }

        // Load all states of a queue, returns true if queue isn't empty
        public void loadViewQueue(QueueRequest request, boolean showPrice) {
            
            ViewQueue vq = new ViewQueue(request, showPrice);
            
        }


        public static class ViewQueue {

            public ViewQueue(List<QueueRequest> queue, boolean willDisplayHeader, boolean skipFirst, boolean showPrice, int limit) {

                if (willDisplayHeader) viewDisplay(showPrice);
                int count = 0;
                int index = 1;

                for (QueueRequest request : queue) {
                    count++;

                    if (skipFirst) {
                        skipFirst = false;
                        continue;
                    }

                    if (showPrice) requestFormat(request, index, showPrice);
                    else requestFormat(request, index);
                    
                    index++;
                    if (limit != 0 && count >= limit) break;
                }

            }

            // Used to display current request in a queue
            public ViewQueue(QueueRequest request, boolean showPrice) {

                System.out.println("====Current Request====");
                System.out.printf("   REQUEST: %-10s            %s%n", request.getId(), request.getDate()); 
                System.out.printf("%n   REQUESTED DOCUMENT: %-15s  %s%n", request.getDocument(), request.getState());

                if (showPrice) System.out.printf("   Price: %-15s  %n", request.getPrice());
                System.out.printf("%n  Will expire in: %-15s%n", request.getExpiry());
            }

            // UI
            public void viewDisplay(boolean showPrice) {
                
                if (showPrice) System.out.printf("      %-7s  ¦  %-12s  ¦  %-25s  ¦   %-20s  -   %-15s   ¦  %-10s  ¦  %s%n", "Request", "Student","Document", "Last Modified", "Expiry", "Price", " Status");
                else System.out.printf("      %-7s  ¦  %-12s  ¦  %-25s  ¦   %-20s  -   %-15s   ¦  %s%n", "Request", "Student","Document", "Last Modified", "Expiry", " Status");
            }


            public void requestFormat(QueueRequest request, int index) {

                System.out.printf("%4d. %-7s  ¦  %-12s  ¦  %-25s  ¦   %-20s  -   %-15s   ¦  %s%n",index , request.getId(), request.getStNum(), request.getDocument(), request.getDate(), request.getExpiry(), request.getState());

            }

            public void requestFormat(QueueRequest request, int index, boolean showPrice) {

                System.out.printf("%4d. %-7s  ¦  %-12s  ¦  %-25s  ¦   %-20s  -   %-15s   ¦  %-10s  ¦  %s%n", index, request.getId(), request.getStNum(), request.getDocument(), request.getDate(), request.getExpiry() , request.getPrice(), request.getState());

            }

        }

        // The expiry date can be changed
        public void expire(String now) {

            Expire e = new Expire();
            e.expireP(casQ, now);
            e.expireL(pauseQ, now);
            e.expireP(accQ, now);
            e.expireL(regQ, now);

        }


        protected class Expire {

            // Cycles through one of the queues, then, if request date is more than daysToExpire, then it will label the request as expired
            // daysToExpire starts at 1, basically days elapsed, so if current date is 12 and expiry is in 1 day then it expires at the 13th
            public void expireP(PriorityQueue<QueueRequest> queue, String now) {
                LocalDate time = LocalDate.now();
                Iterator<QueueRequest> iterate = queue.iterator();

                while (iterate.hasNext()) {
                    QueueRequest request = iterate.next();

                    String expParts[] = request.getExpiry().split("-");
                    LocalDate expDate = LocalDate.of(Integer.parseInt(expParts[2]), Integer.parseInt(expParts[1]), Integer.parseInt(expParts[0]));

                    if (time.isAfter(expDate) || time.isEqual(expDate)) {

                        iterate.remove();
                        updateInfo(request, "EXPIRED", request.getWindow(), now, "0", request.getExpiry());
                        getHistoryManager().appendHist(request);
                        getDocumentManager().changeState(request.getId(), request.getState());

                    }
                }
                writeQ();
            }

            public void expireL(LinkedList<QueueRequest> queue, String now) {
                LocalDate time = LocalDate.now();

                Iterator<QueueRequest> iterate = queue.iterator();

                while (iterate.hasNext()) {
                    QueueRequest request = iterate.next();
                    
                    String expParts[] = request.getExpiry().split("-");
                    LocalDate expDate = LocalDate.of(Integer.parseInt(expParts[2]), Integer.parseInt(expParts[1]), Integer.parseInt(expParts[0]));

                    if (time.isAfter(expDate) || time.isEqual(expDate)) {
                        iterate.remove();
                        updateInfo(request, "EXPIRED", request.getWindow(), now, "0", request.getExpiry());
                        getHistoryManager().appendHist(request);
                        getDocumentManager().changeState(request.getId(), request.getState());
                    }
                }
                writeQ();
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

        public void displayViewHistory(int page, List<String> statements, boolean showPrice) {

            List<String> list = getHistoryManager().read();
            Collections.reverse(list);
            ViewHistory vh = new ViewHistory(list, page, statements, showPrice);

        }

        public void displayViewHistory(int page, boolean showPrice) {

            List<String> list = read();
            Collections.reverse(list);
            ViewHistory vh = new ViewHistory(list, page, showPrice);
        }

        public static class ViewHistory {

            // Display all
            public ViewHistory(List<String> list, int page, boolean showPrice) {

                if (list.isEmpty()) {
                    empty();
                    return;
                }

                viewDisplay(showPrice);
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

                    request(line, showPrice);

                }

                if (showPrice) System.out.println("     ╚" + "═".repeat(144) + "╝");
                else System.out.println("     ╚" + "═".repeat(128) + "╝");
                prompt(pageCount, page);
            }

            // Display some, filters for lines that contain ONE of the statements, not 
            public ViewHistory(List<String> list, int page, List<String> statements, boolean showPrice) {

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

                viewDisplay(showPrice);
                
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

                    request(line, showPrice);

                }

                if (showPrice) System.out.println("     ╚" + "═".repeat(144) + "╝");
                else System.out.println("     ╚" + "═".repeat(128) + "╝");
                prompt(pageCount, page);
            }

            // UI
            private void viewDisplay(boolean showPrice) {    

                if (showPrice) {
                    System.out.println("     ╔" + "═".repeat(144) + "╗");
                    System.out.printf("     ║ %-15s   │ %-7s   │ %-12s │ %-25s   │ %-18s │ %-12s  │ %-25s    ║%n", "Window", "Request", "Student", "Document", "Date & Time", "Price", "Status");
                    String bottom = String.format("║ %-15s   ┼ %-7s   ┼ %-12s ┼ %-25s   ┼ %-18s ┼ %-12s  ┼ %-25s    ║", "", "", "", "", "", "", "").replace(" ", "─");
                    System.out.println("     " + bottom);
                }

                else {
                    System.out.println("     ╔" + "═".repeat(128) + "╗");
                    System.out.printf("     ║ %-15s   │ %-7s   │ %-12s │ %-25s   │ %-18s │ %-25s    ║%n", "Window", "Request", "Student", "Document", "Date & Time", " Status");
                    String bottom = String.format("║ %-15s   ┼ %-7s   ┼ %-12s ┼ %-25s   ┼ %-18s ┼ %-25s    ║", "", "", "", "", "", "", "").replace(" ", "─");
                    System.out.println("     " + bottom);
                }
            
            }

            private void empty() {
                System.out.println("----------------------------");
                System.out.println("     No items in history to be read");
                System.out.println("-----------------------------");
            }

            private void request(String item, boolean showPrice) {
                String parts[] = item.split(",");

                if (showPrice) {
                    if (parts.length == 7) System.out.printf("     ║ %-15s   │ %-7s   │ %-12s │ %-25s   │ %-18s │ %-12s  │ %-25s    ║%n", parts[0], parts[1], parts[2], parts[3], parts[4], parts[6], parts[5]);
                    else System.out.printf("     ║ %-15s   │ %-7s   │ %-12s │ %-25s   │ %-18s │ %-12s  │ %-25s    ║%n", parts[0], parts[1], parts[2], parts[3], parts[4], "-", parts[5]);
                }

                else {
                    System.out.printf("     ║ %-15s   │ %-7s   │ %-12s │ %-25s   │ %-18s │ %-25s    ║%n", parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]);
                }
            }

            private void prompt(int pageCount, int page) {
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




