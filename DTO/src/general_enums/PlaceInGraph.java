package general_enums;

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
