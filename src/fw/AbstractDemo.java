package fw;


public interface AbstractDemo {

    void stop();

    void clear();

    void setConsumerRate(int rate);

    void setTransitionTime(int time);

    void setProducerRate(int rate);

    void start(int stopElement);
}
