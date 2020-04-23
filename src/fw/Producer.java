package fw;

import java.awt.Dimension;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import ui.MapElement;
import ui.MapElementFactory;

public class Producer implements Runnable {
    private final MapElementFactory     nodeHandler;
    private final MapElementFactory     producerHandler;
    private final BlockingQueue<Object> queue;
    private final DemoPolicy            demoPolicy;
    private int                         produced = 0;
    private final List<Consumer>        consumers;
    private final int                   x0       = 400;
    private final int                   y0       = 50;
    private final MapElement            producerMapElement;

    private int                         rate     = 100;
    private final Thread                t;

    public Producer(MapElementFactory nodeHandler, MapElementFactory producerHandler,
            DemoPolicy demoPolicy) {
        this.nodeHandler = nodeHandler;
        this.producerHandler = producerHandler;
        this.queue = demoPolicy.newQueue();
        this.demoPolicy = demoPolicy;
        this.consumers = new LinkedList<Consumer>();

        this.producerHandler.setX(x0);
        this.producerHandler.setY(y0);
        this.producerMapElement = this.producerHandler.create("Producer");

        this.nodeHandler.setX(x0);
        this.nodeHandler.setY(y0);

        t = new Thread(this);
    }

    @Override
    public void run() {
        try {
            while (true) {

                Object object = produce(demoPolicy);

                demoPolicy.setObject(queue, object);

                Thread.sleep(1000 / rate);
            }
        } catch (InterruptedException ex) {
            String msg = producerMapElement.getLabel() + " INTERRUPTED";
            // System.out.println(msg);
            Logger.getInstance().log(msg);

            /*
             * Interrupt all consumers
             */
            for (int i = 0; i < consumers.size(); i++) {
                Consumer consumer = consumers.get(i);
                msg = "INTERRUPT consumer queue.size()=" + queue.size();
                // System.out.println(msg);
                Logger.getInstance().log(msg);

                consumer.interrupt();
            }

            /*
             * Print out queued elements
             */
            String s = "";
            Iterator<Object> it = queue.iterator();
            while (it.hasNext()) {
                String i = it.next().toString();
                s += " " + i;
            }
            msg = "Queued elements: " + s;
            // System.out.println(msg);
            Logger.getInstance().log(msg);

            demoPolicy.stopActionPerformed();
        }
    }

    private Object produce(DemoPolicy producerPolicy) throws InterruptedException {
        Integer v = new Integer(produced++);

        MapElement node = null;
        try {
            node = nodeHandler.create(v.toString());

            producerPolicy.beforeSetting(node,
                    new Dimension(producerPolicy.getX(v), producerPolicy.getY(v)));

        } catch (InterruptedException e) {

            /*
             * If it is drifting, remove the node (from view)
             */
            if (node != null) {
                nodeHandler.remove(node);
            }

            throw (e);
        }

        String msg = producerMapElement.getLabel() + " --> " + v;
        // System.out.println(msg);
        Logger.getInstance().log(msg);

        return node;
    }

    public void registerConsumer(Consumer e) {
        consumers.add(e);
    }

    public void start() {
        t.start();
    }

    public void interrupt() {
        t.interrupt();
    }

    public void setRate(int rate) {
        String msg = "Setting Producer rate=" + rate;
        // System.out.println(msg);
        Logger.getInstance().log(msg);

        this.rate = rate;
    }

    public void setConsumerRate(int rate) {
        String msg = "Setting Consumer rate=" + rate;
        // System.out.println(msg);
        Logger.getInstance().log(msg);

        for (int i = 0; i < consumers.size(); i++) {
            Consumer consumer = consumers.get(i);

            consumer.setRate(rate);
        }
    }

    public BlockingQueue<Object> getQueue() {
        return queue;
    }

}
