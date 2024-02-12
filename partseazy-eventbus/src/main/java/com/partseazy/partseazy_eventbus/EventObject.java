package com.partseazy.partseazy_eventbus;

/**
 * Created by Pumpkin Guy on 06/12/16.
 */

public class EventObject {
    public int id;
    public Object[] objects;

    public EventObject(int id, Object... objects) {
        this.id = id;
        this.objects = objects;
    }
}
