public enum RunResults {

    SUCCESS("success"),
    WARNING("warning"),
    FAILURE("failure"),
    FROZEN("frozen");

    private String status;

    RunResults(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
