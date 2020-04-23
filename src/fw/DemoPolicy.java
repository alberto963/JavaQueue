package fw;

import java.util.concurrent.BlockingQueue;

public interface DemoPolicy {
    public BlockingQueue<Object> newQueue();

    public Object getObject(BlockingQueue<Object> queue) throws InterruptedException;

    public void setObject(BlockingQueue<Object> queue, Object object) throws InterruptedException;

    public int getX(int v);

    public int getY(int v);

    public boolean isHidden(int v);

    public Object beforeSetting(Object v, Object p) throws InterruptedException;

    public void stopActionPerformed();
}
