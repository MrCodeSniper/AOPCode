package com.me.codesniper;

public interface ViewInjector<M> {
    void inject(M m, Object object);
}