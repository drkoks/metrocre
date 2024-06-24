package com.metrocre.game;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class ProcessManager {
    private List<Process> processes = new ArrayList<>();

    public void addProcess(Process process) {
        processes.add(process);
    }

    public void update(float delta) {
        for (ListIterator<Process> iterator = processes.listIterator(); iterator.hasNext(); ) {
            Process process = iterator.next();
            process.update(delta);
            if (process.getState() == Process.State.Complete) {
                iterator.remove();
            }
        }
    }
}
