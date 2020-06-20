abstract public class AllProducts {
    private String object;
    public AllProducts(String object) {
        this.object = object;
    }
    public String getObject() {
        return object;
    }
    abstract public String toString();
    abstract public String toLV();
    abstract public String getDisponibilitate();
    abstract public void setDisponibilitate(String disponibilitate);
}
