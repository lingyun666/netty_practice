package io.netty.example.study.common.order;

import io.netty.example.study.common.OperationResult;
import lombok.Data;

import java.util.List;

@Data
public class QueryOrderOperationResult extends OperationResult {

    private final List<String> dish;

}
