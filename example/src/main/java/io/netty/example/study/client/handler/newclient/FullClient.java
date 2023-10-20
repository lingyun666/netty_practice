package io.netty.example.study.client.handler.newclient;

import com.sun.xml.internal.bind.v2.runtime.output.StAXExStreamWriterOutput;
import io.netty.example.study.client.handler.dispatcher.OperationResultFuture;
import io.netty.example.study.common.order.OrderOperation;
import io.netty.example.study.common.order.OrderOperationResult;
import lombok.Getter;
import reactor.core.publisher.Mono;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.TimeUnit;

/**
 * just to demo implement without auth/ssl/etc
 */
public class FullClient {

    @Getter
    private AsyncOperationable asyncOperation;
    @Getter
    private SyncOperationable syncOperation;
    @Getter
    private ReactiveOperationable reactiveOperation;

    private FullClient(String serverAddress, int serverPort) {
        this.asyncOperation = new AsyncClient(serverAddress, serverPort);
        this.syncOperation = createSyncOperation(asyncOperation);
        this.reactiveOperation = new ReactiveClient(this.syncOperation);
    }

    private SyncOperationable createSyncOperation(AsyncOperationable asyncOperation) {
        return (SyncOperationable) Proxy.newProxyInstance(asyncOperation.getClass().getClassLoader(), new Class[]{SyncOperationable.class}, new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        Method methodForAsync = asyncOperation.getClass().getMethod(method.getName(), method.getParameterTypes());
                        OperationResultFuture operationResultFuture = (OperationResultFuture) methodForAsync.invoke(asyncOperation, args);
                        return operationResultFuture.get(5, TimeUnit.SECONDS);
                    }
                }
        );
    }


    public static void main(String[] args) throws Exception {
        FullClient fullClient = new FullClient("127.0.0.1", 8090);
        AsyncOperationable asyncClient = fullClient.asyncOperation;
        OperationResultFuture<OrderOperationResult> future = asyncClient.order(new OrderOperation(1001, "tudou"));
        System.out.println(future.get(5, TimeUnit.SECONDS));

        SyncOperationable syncOperationable = fullClient.syncOperation;
        OrderOperationResult result = syncOperationable.order(new OrderOperation(1002, "tudou"));
        System.out.println(result);

        ReactiveOperationable reactiveOperation = fullClient.reactiveOperation;
        Mono<OrderOperationResult> mono = reactiveOperation.order(new OrderOperation(1003, "tudou"));
        mono.subscribe(orderOperationResult -> System.out.println(orderOperationResult));
    }

}
