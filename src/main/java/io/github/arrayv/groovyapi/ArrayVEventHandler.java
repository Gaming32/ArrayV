package io.github.arrayv.groovyapi;

import io.github.arrayv.main.ArrayVisualizer;

/**
 * Represents an event handler. This is basically an {@link EventType}&ndash;{@link Runnable} pair with
 * special {@link #register} and {@link #unregister()} methods.
 */
public final class ArrayVEventHandler {
    /**
     * The type of event. Event handlers are registered under these types and called when the type is called.
     */
    public enum EventType {
        /**
         * Run when all scripts embedded in ArrayV and located in the scripts folder are loaded.
         * This is useful when you want to wait till all scripts are loaded before invoking some
         * sort of action dependent on other scripts.
         */
        DEFAULT_SCRIPTS_INSTALLED,

        /**
         * This is run when the main thread has finished setting everything up and is about to terminate.
         */
        ARRAYV_FULLY_LOADED
    }

    private final EventType eventType;
    private final Runnable callback;

    /**
     * Construct an event handler object
     * @param eventType {@link #getEventType}
     * @param cb {@link #getCallback}
     */
    public ArrayVEventHandler(EventType eventType, Runnable cb) {
        this.eventType = eventType;
        this.callback = cb;
    }

    /**
     * The type of the event to handle
     * @return The event type of this wrapper
     */
    public EventType getEventType() {
        return eventType;
    }

    /**
     * The event callback
     * @return The callback of the wrapper
     */
    public Runnable getCallback() {
        return callback;
    }

    /**
     * Run the callback. This is generally for use by {@link ScriptManager#runEventHandlers}
     * @see ScriptManager#runEventHandlers
     */
    public void handle() {
        callback.run();
    }

    /**
     * Register this event handler. This is equivalent to (but preferred over)
     * {@code arrayv.scriptManager.registerEventHandlers(this)}.
     * @see ScriptManager#registerEventHandlers
     */
    public void register() {
        ArrayVisualizer.getInstance().getScriptManager().registerEventHandlers(this);
    }

    /**
     * Unregister this event handler. This is equivalent to (but preferred over)
     * {@code arrayv.scriptManager.unregisterEventHandlers(this)}.
     * @see ScriptManager#unregisterEventHandlers
     */
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
