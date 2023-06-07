package com.justedlev.taskexec.executor.manager;

public interface TaskManager {
    Runnable assign(String taskName);
}
