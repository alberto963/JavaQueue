/**
 * 
 */
package ui;

/**
 * @author apetazzi
 * 
 */
public abstract class MapElementFactory {

    protected MapPanel viewport;

    /**
     * 
     */
    protected int      x;

    /**
     * 
     */
    protected int      y;

    /**
     * 
     */
    protected boolean  hidden = false;

    /**
     * 
     */
    public MapElementFactory(MapPanel viewport) {
        this.viewport = viewport;
    }

    /**
     * 
     */
    public MapElementFactory(MapPanel viewport, int x, int y) {
        this.viewport = viewport;
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /**
     * @param x
     *            the x to set
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * @param y
     *            the y to set
     */
    public void setY(int y) {
        this.y = y;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    /**
	 * 
	 */
    public MapElement create() {
        MapElement element = newElement();
        viewport.addElement(element);

        return element;
    }

    /**
     * 
     */
    public MapElement create(String label) {
        MapElement element = newElement(label);
        viewport.addElement(element);

        return element;
    }

    /**
     * 
     */
    public void remove(MapElement element) {
        viewport.removeElement(element);

        disposeElement(element);
    }

    /**
     * 
     */
    protected abstract MapElement newElement();

    /**
     * 
     */
    protected abstract MapElement newElement(String label);

    /**
     * 
     */
    protected abstract void disposeElement(MapElement element);
}
