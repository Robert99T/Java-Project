package org.example.business;

import org.example.model.Client;
import org.example.model.Queue;

import java.util.List;

public interface Strategy {
    void addClient(List<Queue> queues, Client client);
}
