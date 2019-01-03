package com.microservices.msone.model;
public class BaseEntity<T> extends Entitys<T> {
    protected final boolean isModified;

    public BaseEntity(T id, String name) {
        super(id, name);
        this.isModified = false;
    }

    public boolean isModified() {
        return isModified;
    }
}