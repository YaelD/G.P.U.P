public enum RunResults {

    SUCCESS("success"),
    WARNING("warning"),
    FAILURE("failure"),
    SKIPPED("skipped");

    private String status;

    RunResults(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
