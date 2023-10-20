package io.netty.example.study.client.handler.newclient;

import io.netty.example.study.common.order.OrderOperation;
import io.netty.example.study.common.order.OrderOperationResult;
import io.netty.example.study.common.order.QueryOrderOperation;
import io.netty.example.study.common.order.QueryOrderOperationResult;
import reactor.core.publisher.Mono;

public interface ReactiveOperationable {

    Mono<OrderOperationResult> order(OrderOperation orderOperation) ;
    Mono<QueryOrderOperationResult> queryOrder(QueryOrderOperation queryOrderOperation);

}
