//package Moyoung.Server.stomp;
//
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.messaging.simp.config.MessageBrokerRegistry;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
//import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
//import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
//import org.springframework.web.socket.server.standard.ServerEndpointExporter;
//
//@Configuration
//@EnableWebSocketMessageBroker
//public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {
//
//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//
//        // socketJs 클라이언트가 WebSocket 핸드셰이크를 하기 위해 연결할 endpoint를 지정할 수 있다.
//        registry.addEndpoint("/chatting")
//                .setAllowedOriginPatterns("*") // cors 허용을 위해 꼭 설정해주어야 함. setCredential() 설정시에 AllowedOrigin 과 같이 사용될 경우 오류가 날 수 있으므로 OriginPatterns 설정으로 사용하였음
////                .addInterceptors(new StompHandshakeInterceptor()) // 핸드쉐이크시 인터셉터를 넣을 수 있음
//                .withSockJS();
////                .setDisconnectDelay(30 * 1000)
////                .setClientLibraryUrl("https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"); // 프론트에서 사용하는 sockjs 라이브러리와 동일하게 사용하였음
//    }
//
//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry registry) {
//        // 클라이언트로부터 메시지를 받을 api의 prefix를 설정함
//        // publish
//        registry.setApplicationDestinationPrefixes("/pub");
//
//        // 메모리 기반 메시지 브로커가 해당 api를 구독하고 있는 클라이언트에게 메시지를 전달함
//        // to subscriber
//        registry.enableSimpleBroker("/sub");
//
//    }
//}
//
////@Component
////public class WebSocketConfiguration {
////    @Bean
////    public ServerEndpointExporter serverEndpointExporter() {
////        return new ServerEndpointExporter();
////    }
////}