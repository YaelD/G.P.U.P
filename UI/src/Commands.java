public enum Commands {

    READ_FILE(1),
    GRAPH_INFO(2),
    TARGET_INFO(3),
    PATH(4),
    RUN_TASK(5),
    EXIT(6);

    private int choice;

    Commands(int choice) {
        this.choice = choice;
    }

    public int getChoice() {
        return choice;
    }
}
