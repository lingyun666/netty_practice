package io.netty.example.study.common.order;


import com.google.common.util.concurrent.Uninterruptibles;
import io.netty.example.study.common.Operation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Data
@Slf4j
public class QueryOrderOperation extends Operation {

    private int tableId;

    public QueryOrderOperation(int tableId) {
        this.tableId = tableId;
    }

    @Override
    public QueryOrderOperationResult execute() {
        log.info("query order's executing startup with: " + toString());
        //execute order logic
        Uninterruptibles.sleepUninterruptibly(3, TimeUnit.SECONDS);
        log.info("query order's executing complete");
        QueryOrderOperationResult queryOrderOperationResult = new QueryOrderOperationResult(Arrays.asList("toudou", "xihongshi"));
        return queryOrderOperationResult;
    }
}
