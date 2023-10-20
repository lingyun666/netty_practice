package io.netty.example.study.client.handler.newclient;

import io.netty.example.study.common.order.OrderOperation;
import io.netty.example.study.common.order.OrderOperationResult;
import io.netty.example.study.common.order.QueryOrderOperation;
import io.netty.example.study.common.order.QueryOrderOperationResult;
import reactor.core.publisher.Mono;

public class ReactiveClient implements ReactiveOperationable{

    private SyncOperationable syncOperationable;

    ReactiveClient(SyncOperationable syncOperationable){
        this.syncOperationable = syncOperationable;
    }

    @Override
    public Mono<OrderOperationResult> order(OrderOperation orderOperation) {
        return Mono.just(this.syncOperationable.order(orderOperation));
    }

    @Override
    public Mono<QueryOrderOperationResult> queryOrder(QueryOrderOperation queryOrderOperation) {
        return Mono.just(this.syncOperationable.queryOrder(queryOrderOperation));
    }
}
