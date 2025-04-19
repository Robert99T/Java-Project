package org.example.business;

import org.example.model.Client;
import org.example.model.Queue;

import java.util.List;

public class TimeStrategy implements Strategy {
    @Override
    public void addClient(List<Queue> queues, Client client) {
        Queue best = queues.get(0);
        for(Queue q: queues) {
            if(q.getCurrentWaiting() < best.getCurrentWaiting()) {
                best = q;
            }
        }
        best.addClient(client);
    }

}
