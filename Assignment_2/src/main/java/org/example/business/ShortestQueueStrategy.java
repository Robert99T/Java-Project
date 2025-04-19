package org.example.business;

import org.example.model.Client;
import org.example.model.Queue;

import java.util.List;


public class ShortestQueueStrategy implements Strategy{

    @Override
    public void addClient(List<Queue> queues, Client client) {
        Queue best = queues.get(0);
        for(Queue q: queues) {
            if(q.getClients().size() < best.getClients().size()){
                best = q;
            }
        }
        best.addClient((client));
    }
}
