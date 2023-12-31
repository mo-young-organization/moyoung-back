package Moyoung.Server.helper;

import org.springframework.http.MediaType;
import org.springframework.restdocs.headers.HeaderDescriptor;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;

public interface ControllerHelper<T> {
    default List<HeaderDescriptor> getDefaultRequestHeaderDescriptor() {
        return List.of(
                headerWithName("Authorization").description("인증에 필요한 토큰 정보<br>Bearer Authentication(RFC 6750) " +
                        "Access Token (Ex.  <b>Bearer eyJhbG...</b>)<br> `Bearer ` 문자열을 access token 앞에 붙여야 한다.")
        );
    }
    default RequestBuilder postRequestBuilder(String url, String content) {
        return  post(url)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
    }

    default RequestBuilder postRequestBuilder(String url,
                                              String content,
                                              String accessToken) {
        return  post(url)
                .header("Authorization", "Bearer ".concat(accessToken))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
    }
    default RequestBuilder postRequestBuilder(String url,
                                              long resourceId,
                                              String accessToken) {
        return  post(url, resourceId)
                .header("Authorization", "Bearer ".concat(accessToken))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);
    }

    default RequestBuilder patchRequestBuilder(String url,
                                               String content,
                                               String accessToken) {
        return patch(url)
                .header("Authorization", "Bearer ".concat(accessToken))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
    }
    default RequestBuilder patchRequestBuilder(String url,
                                               long resourceId,
                                               String content,
                                               String accessToken) {
        return patch(url, resourceId)
                .header("Authorization", "Bearer ".concat(accessToken))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

    }

    default RequestBuilder getRequestBuilder(String url, long resourceId) {
        return get(url, resourceId)
                .accept(MediaType.APPLICATION_JSON);
    }

    default RequestBuilder getRequestBuilder(String url) {
        return get(url)
                .accept(MediaType.APPLICATION_JSON);
    }

    default RequestBuilder getRequestBuilderWithParams(String url, long resourceId, MultiValueMap<String, String> params) {
        return get(url, resourceId)
                .params(params)
                .accept(MediaType.APPLICATION_JSON);
    }

    default RequestBuilder getRequestBuilder(String url,
                                             MultiValueMap<String, String> queryParams) {
        return get(url)
                .params(
                        queryParams
                )
                .accept(MediaType.APPLICATION_JSON);
    }

    default RequestBuilder getRequestBuilder(String url, MultiValueMap<String, String> queryParams, String accessToken) {
        return get(url)
                .header("Authorization", "Bearer ".concat(accessToken))
                .params(
                        queryParams
                )
                .accept(MediaType.APPLICATION_JSON);
    }

    default RequestBuilder getRequestBuilder(String url, long resourceId, String accessToken) {
        return get(url, resourceId)
                .header("Authorization", "Bearer ".concat(accessToken))
                .accept(MediaType.APPLICATION_JSON);
    }

    default RequestBuilder deleteRequestBuilder(String url, String accessToken) {
        return delete(url)
                .header("Authorization", "Bearer ".concat(accessToken));
    }

    default RequestBuilder deleteRequestBuilder(String url, long resourceId) {
        return delete(url, resourceId);
    }

    default RequestBuilder deleteRequestBuilder(String url,
                                                long resourceId,
                                                String accessToken) {
        return delete(url, resourceId)
                .header("Authorization", "Bearer ".concat(accessToken));
    }

}
