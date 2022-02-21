package io.github.arrayv.groovyapi;

import io.github.arrayv.main.ArrayVisualizer;

public final class ArrayVEventHandler {
    public static enum EventType {
        SCRIPTS_INSTALLED,
        ARRAYV_FULLY_LOADED
    }

    private final EventType eventType;
    private final Runnable callback;

    public ArrayVEventHandler(EventType eventType, Runnable cb) {
        this.eventType = eventType;
        this.callback = cb;
    }

    public EventType getEventType() {
        return eventType;
    }

    public Runnable getCallback() {
        return callback;
    }

    public void handle() {
        callback.run();
    }

    public void register() {
        ArrayVisualizer.getInstance().getScriptManager().registerEventHandlers(this);
    }

    public void unregister() {
        ArrayVisualizer.getInstance().getScriptManager().unregisterEventHandlers(this);
    }

    @Override
    public int hashCode() {
        return 31 * eventType.hashCode() + callback.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof ArrayVEventHandler)) return false;
        ArrayVEventHandler other = (ArrayVEventHandler)o;
        return eventType.equals(other.eventType) && callback.equals(other.callback);
    }
}
