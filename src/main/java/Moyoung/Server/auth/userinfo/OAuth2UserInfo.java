package Moyoung.Server.auth.userinfo;

public interface OAuth2UserInfo {
    String getProviderId();
    String getProvider();
    String getEmail();
    String getProfileImage();
}
