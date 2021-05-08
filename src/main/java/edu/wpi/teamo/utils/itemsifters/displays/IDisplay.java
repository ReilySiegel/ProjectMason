package edu.wpi.teamo.utils.itemsifters.displays;

import java.util.stream.Stream;

/**
 * A method or class to be called every time the sifter updates. Acts as an observer.
 * These interfaces will be bound to the sifter with ItemSifter.addDisplay().
 * This can be used for updating listviews etc.
 */
public interface IDisplay<T> {
    void update(Stream<T> items);
}