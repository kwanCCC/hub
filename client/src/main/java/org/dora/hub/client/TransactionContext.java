package org.dora.hub.client;

import java.util.UUID;

/**
 * Created by SDE on 2018/6/12.
 */
public class TransactionContext {

    public static final String GLOBAL_TRANSACTION_ID = "Global-Transaction-ID";
    public static final String CURRENT_TRANSACTION_ID = "Current-Transaction-ID";

    private final ThreadLocal<String> globalTxId = new InheritableThreadLocal<>();
    private final ThreadLocal<String> currentTxId = new InheritableThreadLocal<>();





    public String newGlobalTxId() {
        String globalTxId = UUID.randomUUID().toString();
        setGlobalTxId(globalTxId);
        return globalTxId;
    }

    public void setGlobalTxId(String globalTxId) {
        this.globalTxId.set(globalTxId);
    }

    public void newCurrentTxId() {
        setCurrentTransactionId(UUID.randomUUID().toString());
    }

    public void setCurrentTransactionId(String localTxId) {
        this.currentTxId.set(localTxId);
    }

    public String getGlobalTransactionId() {
        return globalTxId.get();
    }

    public String getCurrentTransactionId() {
        return currentTxId.get();
    }

    public void clear() {
        globalTxId.remove();
        currentTxId.remove();
    }
}
