package io.netty.example.study.udp.client.jdk;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;
import io.netty.example.study.common.RequestMessage;
import io.netty.example.study.common.ResponseMessage;
import io.netty.example.study.common.order.OrderOperation;
import io.netty.example.study.util.IdUtil;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.IOException;
import java.net.*;
import java.util.Random;

@Slf4j
public class JdkUdpClient {

    public static void main(String[] args) throws IOException {

        DatagramSocket datagramSocket = new DatagramSocket();
        datagramSocket.setSoTimeout(10000);

        //“连接服务器”
        datagramSocket.connect(InetAddress.getByName("localhost"), 8090);

        //发送请求
        RequestMessage requestMessage = new RequestMessage(IdUtil.nextId(), new OrderOperation(1001, RandomStringUtils.randomNumeric(2048)));
        byte[] data = convertToBytes(requestMessage);
        DatagramPacket sendPacket = new DatagramPacket(data, data.length);
        datagramSocket.send(sendPacket);

        //接受响应
        byte[] buffer = new byte[2048];
        DatagramPacket  receivedPacket = new DatagramPacket(buffer, buffer.length);
        datagramSocket.receive(receivedPacket);

        ResponseMessage responseMessage = convertToResponse(receivedPacket);
        log.info(responseMessage.toString());

        //断开“连接”
        datagramSocket.disconnect();
    }

    private static byte[] convertToBytes(RequestMessage requestMessage) {
        ByteBuf bufForRequest = ByteBufAllocator.DEFAULT.buffer();
        requestMessage.encode(bufForRequest);
        return ByteBufUtil.getBytes(bufForRequest);
    }

    private static ResponseMessage convertToResponse(DatagramPacket receivedPacket) {
        ByteBuf bufForResponse = ByteBufAllocator.DEFAULT.buffer();
        bufForResponse.writeBytes(receivedPacket.getData(),receivedPacket.getOffset(), receivedPacket.getLength());
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.decode(bufForResponse);
        return responseMessage;
    }
}
