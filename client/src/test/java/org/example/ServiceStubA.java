package org.example;

import lombok.Getter;
import org.dora.hub.client.annotation.TransactionStart;

/**
 * Created by SDE on 2018/6/12.
 */
public class ServiceStubA {

    @Getter
    private int a = 1;

    @TransactionStart
    public void a_add_1() {
        a++;
    }
}
