/**
 * 
 */
package ui;

/**
 * @author apetazzi
 * 
 */
public class ConsumerFactory extends MapElementFactory {

    /**
     * @param viewport
     * 
     */
    public ConsumerFactory(MapPanel viewport) {
        super(viewport);
    }

    @Override
    protected MapElement newElement() {
        Consumer c = new Consumer(x, y);
        c.setHidden(isHidden());

        return c;
    }

    @Override
    protected MapElement newElement(String label) {
        Consumer c = new Consumer(x, y);
        c.setLabel(label);
        c.setHidden(isHidden());

        return c;
    }

    @Override
    protected void disposeElement(MapElement element) {
        return;
    }
}
