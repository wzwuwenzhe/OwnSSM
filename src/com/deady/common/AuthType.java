package com.deady.common;

/**
 * @author Andre.Z 2016-4-13 下午2:50:16<br>
 * 
 */
public enum AuthType {

    None(-1),
    Hardware(0),
    Password(1),
    Sms(3),
    Softdog(4),
    DynaPwd(5),
    EkKey(6);

    private int value = 0;

    private AuthType(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        this.value = value;
    }

}
