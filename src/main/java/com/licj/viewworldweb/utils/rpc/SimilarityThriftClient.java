package com.licj.viewworldweb.utils.rpc;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

/**
 * Created by Administrator on 2017/12/20.
 */
public class SimilarityThriftClient {

    public static void main(String[] args) {
        try {
            TTransport transport;
            transport = new TSocket("127.0.0.1", 9090);
            transport.open();

            TProtocol protocol = new TBinaryProtocol(transport);
            ChatSimilarityService.Client client = new ChatSimilarityService.Client(protocol);
            perform(client);
            transport.close();

        } catch (TException e) {
            e.printStackTrace();
        }
    }
    private static void perform(ChatSimilarityService.Client client)throws TException {
        String chat1 = "hello python!";
        String chat2 = "hello java!";
        double ratio = client.similarity(chat1, chat2);
        System.out.println(ratio);
    }
}
