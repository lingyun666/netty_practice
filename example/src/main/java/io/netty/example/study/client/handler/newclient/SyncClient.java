package io.netty.example.study.client.handler.newclient;

import io.netty.example.study.common.order.OrderOperation;
import io.netty.example.study.common.order.OrderOperationResult;
import io.netty.example.study.common.order.QueryOrderOperation;
import io.netty.example.study.common.order.QueryOrderOperationResult;

import javax.net.ssl.SSLException;
import java.util.concurrent.TimeUnit;

/**
 * just to demo sync implement without auth/ssl/etc
 */
public class SyncClient implements SyncOperationable {

    private AsyncClient asyncClient;

    SyncClient(String serverAddress, int serverPort) throws SSLException, InterruptedException {
            this.asyncClient = new AsyncClient(serverAddress, serverPort);
    }

    @Override
    public OrderOperationResult order(OrderOperation operation) {
        try {
            return asyncClient.order(operation).get(5, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    };

    @Override
    public QueryOrderOperationResult queryOrder(QueryOrderOperation operation) {
        try {
            return asyncClient.queryOrder(operation).get(5, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws Exception {
        SyncClient syncClient = new SyncClient("127.0.0.1", 8090);
        OrderOperationResult orderOperationResult = syncClient.order(new OrderOperation(1001, "tudou"));
        System.out.println(orderOperationResult);
    }

}
