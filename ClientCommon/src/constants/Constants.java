package constants;

public class Constants {

    public final static String BASE_DOMAIN = "localhost";
    private final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    private final static String CONTEXT_PATH = "/WebServer_war_exploded";
    private final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;

    public final static String LOGIN_PAGE = FULL_SERVER_PATH + "/admin/login";
    public final static String UPLOAD_FILE_PAGE = FULL_SERVER_PATH + "/admin/upload_file";
    public final static String GRAPH_LIST = FULL_SERVER_PATH + "/graphs_list";
    public final static String USERS_LIST = FULL_SERVER_PATH + "/users_list";

}