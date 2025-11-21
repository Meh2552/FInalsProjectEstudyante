package package1;

public class Queue {

    private QueueManager queueMan;
    private QueueHandler queueHand;

    public Queue() {
        this.queueMan = new QueueManager();
        this.queueHand = new QueueHandler();
    }


    public QueueManager getQueueMan() {
        return queueMan;
    }

    public QueueHandler getQueueHand() {
        return queueHand;
    }

    
    public class QueueManager{

    }

    public class QueueHandler {

    }
}
