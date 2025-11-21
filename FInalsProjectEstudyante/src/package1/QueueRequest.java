package package1;

public class QueueRequest {

    private String id, stNum, document, state, date, window;
    private boolean waiting = false;

    public QueueRequest (String id, String stNum, String document, String state, String window, String date){

        this.date = date;
        this.document = document;
        this.stNum = stNum;
        this.state = state;
        this.id = id;
        this.window = window;

    }

    public QueueRequest(String id, String stNum, String document, String state, String window, String date, String waiting) {

        this(id, stNum, document, state, window, date);
        if (waiting.equals("true")) this.waiting = true;

        else this.waiting = false;

    }

    public String getId() {
        return id;
    }

    public String getStNum() {
        return stNum;
    }

    public String getDocument() {
        return document;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getWindow() {
        return window;
    }

    public String getDate() {
        return date;
    }

    public boolean isWaiting() {
        return this.waiting;
    }

    public void setWaiting(boolean waiting) {
        this.waiting = waiting;
    }
    
    public String toFileLine() {
        return getId() + "," + getStNum() + "," + getDocument() + "," + getState() + "," + getWindow() + "," + getDate() + "," + isWaiting();
    }

    public static QueueRequest fromLine(String line) {
        String[] p = line.split(",", 6);
        if (p.length < 5) {
            return null;
        }
        return new QueueRequest(p[0], p[1], p[2], p[3], p[4], p[5], p[6]);
    }

}
