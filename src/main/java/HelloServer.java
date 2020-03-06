import com.linecorp.armeria.common.HttpHeaderNames;
import com.linecorp.armeria.common.HttpMethod;
import com.linecorp.armeria.common.SessionProtocol;
import com.linecorp.armeria.common.grpc.GrpcSerializationFormats;
import com.linecorp.armeria.server.Server;
import com.linecorp.armeria.server.ServerBuilder;
import com.linecorp.armeria.server.docs.DocService;
import com.linecorp.armeria.server.grpc.GrpcService;
import com.linecorp.armeria.common.grpc.protocol.GrpcHeaderNames;

import hello.HelloService;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

import com.linecorp.armeria.server.cors.CorsServiceBuilder;

public class HelloServer {

    public static void main(String[] args) {

        final CorsServiceBuilder corsBuilder =
                CorsServiceBuilder
                        .forOrigin("http://localhost:8081")
                        .allowCredentials()
                        .allowNullOrigin()
                        .allowRequestMethods(HttpMethod.POST, HttpMethod.GET, HttpMethod.OPTIONS)
                        .allowRequestHeaders(HttpHeaderNames.CONTENT_TYPE, HttpHeaderNames.of("x-user-agent"), HttpHeaderNames.of("x-grpc-web"), HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN)
                        .exposeHeaders(GrpcHeaderNames.GRPC_STATUS, GrpcHeaderNames.GRPC_MESSAGE, GrpcHeaderNames.ARMERIA_GRPC_THROWABLEPROTO_BIN)
                        .preflightResponseHeader("x-preflight-cors", "Hello CORS");

        ServerBuilder sb = Server.builder();
        sb.service(GrpcService.builder()
                        .addService(new HelloService())
                        .supportedSerializationFormats(GrpcSerializationFormats.values())
                        .build(),
                corsBuilder.newDecorator());
        sb.http(new InetSocketAddress("localhost", 9090));



        sb.serviceUnder("/docs", new DocService());

        System.out.println("SERVER STARTED");
        Server server = sb.build();

        CompletableFuture<Void> future = server.start();
        future.join();

    }
}