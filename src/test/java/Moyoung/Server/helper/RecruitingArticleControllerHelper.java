package Moyoung.Server.helper;

public interface RecruitingArticleControllerHelper extends ControllerHelper {
    String RECRUITING_ARTICLE_URL = "/recruit";
    String RECRUITING_ARTICLE_RESOURCE_ID = "/{recruit-id}";
    String RECRUITING_ARTICLE_RESOURCE_URI = RECRUITING_ARTICLE_URL + RECRUITING_ARTICLE_RESOURCE_ID;
    String RECRUITING_ARTICLE_NON_LOGIN_GET_URL = "/recruit-article";
    String RECRUITING_ARTICLE_ENTER_URL = RECRUITING_ARTICLE_RESOURCE_URI + "/enter";
}
