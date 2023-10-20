package io.netty.example.study.udp.server;

import io.netty.example.study.common.ResponseMessage;
import lombok.Data;

import java.net.InetSocketAddress;

@Data
public class UdpResponse {
    private ResponseMessage responseMessage;
    private InetSocketAddress receiver;
}
