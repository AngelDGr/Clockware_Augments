package mors.clockware.item;

public enum ClockwareType {
    LEG("leg", 0),
    BODY("body", 1),
    ARM("arm", 2),
    HEAD("head", 3);

    private final String name;
    private final int index;
    ClockwareType(String name, int index){
        this.name=name;
        this.index=index;
    }
    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }
}
