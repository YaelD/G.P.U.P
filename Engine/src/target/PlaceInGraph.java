package target;

public enum PlaceInGraph {

    LEAF("leaf"),
    MIDDLE("middle"),
    ROOT("root"),
    INDEPENDENT("independent");

    private String place;

    PlaceInGraph(String place) {
        this.place = place;
    }

    public String getPlace() {
        return place;
    }
}
