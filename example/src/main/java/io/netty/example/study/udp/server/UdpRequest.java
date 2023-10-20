package io.netty.example.study.udp.server;

import io.netty.example.study.common.RequestMessage;
import lombok.Data;

import java.net.InetSocketAddress;

@Data
public class UdpRequest {
    private RequestMessage requestMessage;
    private InetSocketAddress sender;
}
