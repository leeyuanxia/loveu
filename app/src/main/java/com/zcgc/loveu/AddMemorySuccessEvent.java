package com.zcgc.loveu;

public class AddMemorySuccessEvent {
    private int addType; //0 本地 1 远端

    public AddMemorySuccessEvent(int addType) {
        this.addType = addType;
    }

    public int getAddType() {
        return addType;
    }

    public void setAddType(int addType) {
        this.addType = addType;
    }
}
