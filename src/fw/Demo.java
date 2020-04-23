package fw;

import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;

import ui.ConsumerFactory;
import ui.MapElementFactory;
import ui.MapPanel;
import ui.NodeFactory;
import ui.ProducerFactory;

/**
 * @author Alberto
 *
 */
public class Demo implements AbstractDemo {

    private final MapPanel          mapPanel;
    private final int               numberOfConsumers;
    private final Producer          producer;
    private final MapElementFactory nodeHandler;
    private final List<Consumer>    consumers = new LinkedList<Consumer>();

    public Demo(JPanel mp, final DemoPolicy demoPolicy, int nc) {
        super();

        this.mapPanel = (MapPanel) mp;
        this.numberOfConsumers = nc;

        mapPanel.clearAll();

        MapElementFactory producerHandler = new ProducerFactory(mapPanel);
        MapElementFactory consumerHandler = new ConsumerFactory(mapPanel);
        nodeHandler = new NodeFactory(mapPanel);

        producer = new Producer(nodeHandler, producerHandler, demoPolicy);

        /*
         * Create and start consumers
         */
        for (int c = 0; c < numberOfConsumers; c++) {

            Consumer consumer = new Consumer(consumerHandler, producer.getQueue(), demoPolicy,
                    producer, c);

            consumers.add(consumer);

            /*
             * Register the consumer to the producer in order to stop it when
             * producer is stopped.
             */
            producer.registerConsumer(consumer);
        }
    }

    @Override
    public void start(int stopElement) {

        /*
         * Create and start consumers
         */
        for (int c = 0; c < numberOfConsumers; c++) {

            consumers.get(c).setStopElement(stopElement);

            /*
             * Start this consumer
             */
            try {
                consumers.get(c).start();
            } catch (Exception e) {
            }
        }

        /*
         * Start the producer
         */
        producer.start();
    }

    @Override
    public void stop() {
        producer.interrupt();
    }

    @Override
    public void clear() {
        mapPanel.clearAll();
    }

    @Override
    public void setProducerRate(int rate) {
        producer.setRate(rate);
    }

    @Override
    public void setConsumerRate(int rate) {
        producer.setConsumerRate(rate);
    }

    @Override
    public void setTransitionTime(int time) {
        ((NodeFactory) nodeHandler).setTransitionTime(time);
    }
}
