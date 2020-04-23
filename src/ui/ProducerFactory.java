/**
 * 
 */
package ui;

/**
 * @author apetazzi
 * 
 */
public class ProducerFactory extends MapElementFactory {

    /**
     * @param viewport
     * 
     */
    public ProducerFactory(MapPanel viewport) {
        super(viewport);
    }

    @Override
    protected MapElement newElement() {
        Producer p = new Producer(x, y);
        viewport.addElement(p);

        return p;
    }

    @Override
    protected MapElement newElement(String label) {
        Producer p = new Producer(x, y);
        p.setLabel(label);
        viewport.addElement(p);

        return p;
    }

    @Override
    protected void disposeElement(MapElement element) {
        return;
    }
}
