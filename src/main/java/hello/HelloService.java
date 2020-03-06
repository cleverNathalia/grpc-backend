package hello;

import com.example.grpc.hello.Hello;
import com.example.grpc.hello.HelloServiceGrpc;
import io.grpc.stub.StreamObserver;


public class HelloService extends HelloServiceGrpc.HelloServiceImplBase {

    public void hello(Hello.HelloRequest req, StreamObserver<Hello.HelloReply> replyStreamObserver){

        System.out.println("INSIDE SERVICE");

        Hello.HelloReply.Builder response = Hello.HelloReply.newBuilder();

        Hello.Person.Builder person = Hello.Person.newBuilder();

        if (req.getRole().equals("admin")){
            person.setId("960712834959");
            person.setName("Jane Johnson");
            person.setAddress("8 Brood Street, Cape Town");
            person.setPhone("0825943219");

            response.setPeople(person);

        }else {
            person.setId("920343204032");
            person.setName("John Doe");
            person.setAddress(" ");
            person.setPhone(" ");

            response.setPeople(person);
        }
        replyStreamObserver.onNext(response.build());
        replyStreamObserver.onCompleted();


    }
}