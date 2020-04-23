/**
 * 
 */
package ui;

/**
 * @author apetazzi
 * 
 */
public class NodeFactory extends MapElementFactory {

    /**
     * 
     */
    private int transitionTime = 100;

    /**
     * 
     */
    public NodeFactory(MapPanel viewport) {
        super(viewport);
    }

    public int getTransitionTime() {
        return transitionTime;
    }

    public void setTransitionTime(int transitionTime) {
        this.transitionTime = transitionTime;
    }

    @Override
    protected MapElement newElement() {
        Node node = new Node(x, y, transitionTime);

        return node;
    }

    @Override
    protected MapElement newElement(String label) {
        Node node = new Node(x, y, transitionTime);
        node.setLabel(label);

        return node;
    }

    @Override
    protected void disposeElement(MapElement element) {

        // System.out.println(x + " " + y + " " + ex + " " + ey + " " +
        // element.getLabel());
    }
}
