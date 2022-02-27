package constants;

public class Constants {

    public final static String BASE_DOMAIN = "localhost";
    private final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    private final static String CONTEXT_PATH = "/ex03_318421534_313300717";
    private final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;

    public final static String LOGIN_PAGE = FULL_SERVER_PATH + "/admin/login";
    public final static String UPLOAD_FILE_PAGE = FULL_SERVER_PATH + "/admin/upload_file";
    public final static String GRAPH_LIST = FULL_SERVER_PATH + "/graphs_list";
    public final static String USERS_LIST = FULL_SERVER_PATH + "/users_list";
    public final static String TASK_LIST = FULL_SERVER_PATH + "/tasks_list";
    public final static String FIND_PATH = FULL_SERVER_PATH + "/get_paths";
    public final static String WHAT_IF = FULL_SERVER_PATH + "/what_if";
    public final static String FIND_CYCLE = FULL_SERVER_PATH + "/find_cycle";
    public final static String TASK_EXECUTION = FULL_SERVER_PATH + "/task_execution";
    public final static String UPDATE_TARGET_RUN_STATUS = FULL_SERVER_PATH + "/target";
    public final static String UNREGISTER_EXECUTION = FULL_SERVER_PATH + "/unregister_execution";

    //QUERY PARAMS:
    public static final String USERNAME = "username";
    public static final String USER_TASKS = "userTasks";
    public static final String USER_TYPE = "userType";
    public static final String ADMIN = "admin";
    public static final String WORKER = "worker";
    public static final String SOURCE_TARGET = "srcTarget";
    public static final String DESTINATION_TARGET = "destTarget";
    public static final String GRAPH_NAME = "graphName";
    public static final String DEPENDENCY = "dependency";
    public static final String TASK_NAME = "taskName";
    public static final String TASK_STATUS = "taskStatus";
    public static final String NUMBER_OF_TARGETS = "numOfTargets";


    public static final int TWO_SECS = 2000;




}
