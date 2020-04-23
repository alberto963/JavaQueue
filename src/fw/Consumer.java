package fw;

import java.util.concurrent.BlockingQueue;

import ui.MapElement;
import ui.MapElementFactory;
import ui.Node;

public class Consumer implements Runnable {
    private final MapElementFactory     consumerHandler;
    private final BlockingQueue<Object> queue;
    private final BlockingQueue<Object> deliveryQueue;
    private final DemoPolicy            consumerPolicy;
    private final Producer              producer;
    private final int                   position;
    private int                         consumed    = 0;

    private final int                   x0          = 100;
    private final int                   y0          = 150;

    private int                         stopElement = 11;
    private int                         rate        = 100;

    private final MapElement            consumerMapElement;
    private final Thread                t;

    public Consumer(MapElementFactory consumerHandler, BlockingQueue<Object> q, DemoPolicy cp,
            Producer p, int n) {
        this.consumerHandler = consumerHandler;
        this.queue = q;
        this.deliveryQueue = cp.newQueue();
        this.consumerPolicy = cp;
        this.producer = p;
        this.position = n;
        this.consumerHandler.setX(x0 + n * 100);
        this.consumerHandler.setY(y0);
        this.consumerMapElement = this.consumerHandler.create("Consumer " + n);

        t = new Thread(this);
    }

    @Override
    public void run() {
        try {
            while (true) {

                Thread.sleep(1000 / rate);

                Object object = consumerPolicy.getObject(queue);
                consume(object);
                consumerPolicy.setObject(deliveryQueue, object);
            }
        } catch (InterruptedException ex) {
            String msg = consumerMapElement.getLabel() + " INTERRUPTED";
            // System.out.println(msg);
            Logger.getInstance().log(msg);
            Logger.getInstance().consumerInterrupted();
        }
    }

    private void consume(Object x) throws InterruptedException {

        MapElement node = (MapElement) x;
        String label = node.getLabel();
        // System.out.println(label);

        int i = Integer.valueOf(label);
        String msg = consumerMapElement.getLabel() + " <-- " + i;
        // System.out.print(msg);
        Logger.getInstance().log(msg);

        /*
         * Move the node from its current position to its next one.
         */
        int ex = x0 * (position + 1);
        int ey = y0 + ((++consumed) * 12);
        try {
            if (node != null) {
                ((Node) node).drift(ex, ey);
            }
        } catch (InterruptedException e) {
            /*
             * If it is drifting, terminate the drift
             */
            ((Node) node).drift(ex, ey);

            throw (e);
        }

        /*
         * STOP condition
         */
        if (i >= stopElement) {
            msg = consumerMapElement.getLabel() + " INTERRUPT Producer queue.size()="
                    + queue.size();

            // System.out.println(msg);
            Logger.getInstance().log(msg);

            producer.interrupt();

            return;
        }
    }

    public void start() {
        t.start();
    }

    public void interrupt() {
        t.interrupt();
    }

    public int getStopElement() {
        return stopElement;
    }

    public void setStopElement(int stopElement) {
        this.stopElement = stopElement;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public BlockingQueue<Object> getQueue() {
        return deliveryQueue;
    }

}
