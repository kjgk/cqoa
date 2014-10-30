package com.withub.server;


import java.util.List;

public interface OAServer {

    public void commitTask(String currentUserId, String taskId, String result, String opinion, List<String> nextHandlerList) throws Exception;
}
