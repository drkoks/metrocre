package com.metrocre.game;

public class Process {
    private ProcessManager processManager;
    private Process attachedProcess = null;
    private State state = State.Init;

    private void init() {

    }

    public void attachProcess(Process process) {
        attachedProcess = process;
    }

    public void run() {
        init();
        state = State.Running;
    }

    public void update(float delta) {

    }

    public void interrupt() {
        state = State.Interrupted;
    }

    public void complete() {
        state = State.Complete;
        if (attachedProcess != null) {
            processManager.addProcess(attachedProcess);
        }
    }

    public State getState() {
        return state;
    }

    public enum State {
        Init,
        Running,
        Interrupted,
        Complete,
    }
}
