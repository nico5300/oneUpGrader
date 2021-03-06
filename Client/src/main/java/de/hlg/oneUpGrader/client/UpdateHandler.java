package de.hlg.oneUpGrader.client;

/**
 * Created by nico on 22.04.17.
 */

@FunctionalInterface
public interface UpdateHandler<T> {     // Observer-Pattern
    public void handle(T obj);
}
