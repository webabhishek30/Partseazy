package com.partseazy.partseazy_eventbus;

import de.greenrobot.event.EventBus;

/**
 * Created by Pumpkin Guy on 06/12/16.
 */

public class PartsEazyEventBus {

    private static PartsEazyEventBus partsEazyEventBus;

    private EventBus eventBus;

    private PartsEazyEventBus() {
        eventBus = new EventBus();
    }

    public static PartsEazyEventBus getInstance() {
        if (partsEazyEventBus == null) {
            synchronized (PartsEazyEventBus.class) {
                if (partsEazyEventBus == null) {
                    partsEazyEventBus = new PartsEazyEventBus();
                }
            }
        }
        return partsEazyEventBus;
    }

    public void postEvent(int id, Object... objects) {
        eventBus.post(new com.partseazy.partseazy_eventbus.EventObject(id, objects));
    }

    public void addObserver(Object observer) {
        if (!eventBus.isRegistered(observer))
            eventBus.register(observer);
    }

    public void removeObserver(Object observer) {
        if (eventBus.isRegistered(observer))
            eventBus.unregister(observer);
    }
}

