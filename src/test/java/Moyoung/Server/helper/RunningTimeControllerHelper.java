package Moyoung.Server.helper;

public interface RunningTimeControllerHelper extends ControllerHelper {
    String RUNNING_TIME_URL = "/runningTime";
    String RUNNING_TIME_RESOURCE_ID = "/{runningTime-id}";
    String RUNNING_TIME_RESOURCE_URI = RUNNING_TIME_URL + RUNNING_TIME_RESOURCE_ID;
    String RUNNING_TIME_ARTICLE_URI = RUNNING_TIME_RESOURCE_URI + "/article";
}
