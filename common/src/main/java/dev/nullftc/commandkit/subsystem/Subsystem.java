package dev.nullftc.commandkit.subsystem;

public interface Subsystem {
    void periodic();
    void read();
    void write();
    void reset();
}
