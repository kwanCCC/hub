package org.dora.hub.client;

/**
 * Created by <-chan 666 on 2018/6/12.
 */
public interface TransactionStartProcessor {

    /**
     * @param globalTxID
     * @param timeout
     * @param retries
     */
    void preIntercept(String globalTxID, int timeout, int retries);

    /**
     * @param globalTxID
     */
    void postIntercept(String globalTxID);

    void onError();
}
