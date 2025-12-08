package package1;

public class QueueRequest {

    private String id, stNum, document, state, date, window, price, priority, expiry;

    public QueueRequest (String id, String stNum, String document, String state, String window, String date, String price, String priority, String expiry){

        this.date = date;
        this.document = document;
        this.stNum = stNum;
        this.state = state;
        this.id = id;
        this.window = window;
        this.price = price;
        this.priority = priority;
        this.expiry = expiry;

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

    public void setWindow(String window) {
        this.window = window;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPrice() {
        return price;
    }

    public void setPricee(String price) {
        this.price = price;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
    
    public String toFileLine() {
        return getId() + "," + getStNum() + "," + getDocument() + "," + getState() + "," + getWindow() + "," + getDate() + "," + getPrice() + "," + getPriority() + "," + getExpiry();
    }

    public static QueueRequest fromLine(String line) {
        String[] p = line.split(",");
        if (p.length < 5) {
            return null;
        }
        return new QueueRequest(p[0], p[1], p[2], p[3], p[4], p[5], p[6], p[7]);
    }

}
