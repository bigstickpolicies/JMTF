package core;

public enum ExecutionStage {
    CONFIGURATION(0,"Configuration"),
    INITIALIZATION(1,"Initialization"),
    EXECUTION(2,"Execution"),
    TERMINATING(3,"Terminating"),
    TERMINATED(4,"Terminated");


    int stage;
    String name;
    private ExecutionStage(int stage,String name) {
        this.stage=stage;
        this.name=name;
    }
    public int getStage() {
        return stage;
    }
    @Override
    public String toString() {
        return name;
    }
}

