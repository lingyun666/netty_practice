package io.netty.example.study.client.handler.newclient;

import io.netty.example.study.common.order.OrderOperation;
import io.netty.example.study.common.order.OrderOperationResult;
import io.netty.example.study.common.order.QueryOrderOperation;
import io.netty.example.study.common.order.QueryOrderOperationResult;

public interface SyncOperationable {

    OrderOperationResult order(OrderOperation operation) ;
    QueryOrderOperationResult queryOrder(QueryOrderOperation operation);

}
