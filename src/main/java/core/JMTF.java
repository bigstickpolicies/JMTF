package core;


import exception.IncorrectStageException;

import util.SegmentedArray;

import java.util.Random;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import java.util.logging.Level;


public class JMTF {
    private ExecutionStage stage;
    private JMTFSettings settings;
    private ScheduledThreadPoolExecutor executors;
    private ObjectResourceManager objectResourceManager;

    private Throwable flag;

    private static JMTF singleton;
    Random random;

    private JMTF() {
        stage=ExecutionStage.CONFIGURATION;
        settings=new JMTFSettings();
        objectResourceManager =new ObjectResourceManager();
    }
    public static JMTF get() {
        if(singleton==null) {
            singleton=new JMTF();
        }
        return singleton;
    }
    public JMTFSettings getSettings() {
        return settings.clone();
    }
    public void setSettings(JMTFSettings settings) {
        if(stage!=ExecutionStage.CONFIGURATION) throw new IncorrectStageException();
        this.settings=settings.clone();
    }
    private void setStage(ExecutionStage stage) {
        if(settings.getVerbose()) {
            settings.getLogger().log(Level.INFO,"JMTF switching to stage "+stage.toString());
        }
        this.stage=stage;
    }
    public ExecutionStage getStage() {
        return stage;
    }
    public void init() {
        setStage(ExecutionStage.INITIALIZATION);
        random=new Random();
        executors=new ScheduledThreadPoolExecutor(settings.getCorePoolSize(),(r) -> {
            return new Thread(r);
        });
        settings.getLogger().log(Level.INFO,"Thread pool started with "+settings.getCorePoolSize()+" threads");
        setStage(ExecutionStage.EXECUTION);
    }
    public <T> ReturnValue<T> startTask(Task<T> t)  {
        if(stage!=ExecutionStage.EXECUTION) {
            throw new IncorrectStageException();
        }
        ReturnValue rv=new ReturnValue();
        executors.execute(() -> {
            t.wrapperRun(rv);
        });
        return rv;
    }

    public ObjectResourceManager getObjectResourceManager() {
        return objectResourceManager;
    }
    public <T> SegmentedArray<T> newSegmentedArray(Class<T> cls,int length,int partSize) {
        return new SegmentedArray(cls,length,partSize);
    }
    public void shutdown()  {
        setStage(ExecutionStage.TERMINATING);
        executors.shutdown();
    }
    public void shutdown(Throwable t) {
        setStage(ExecutionStage.TERMINATING);
        if(t==null) {
            executors.shutdown();
        }else{
            executors.shutdown();

            flag =t;
        }
    }
    public void awaitTermination() {

    }
    public void awaitTermination(boolean propagateErrors) throws Error{
        try {
            executors.awaitTermination(9999999, TimeUnit.DAYS);
        }catch(InterruptedException e) {
            settings.getLogger().log(Level.WARNING,"Thread awaiting termination was interrupted");
            return;
        }
        System.out.println(flag);
        if(flag!=null) {
            getSettings().getLogger().log(Level.SEVERE,"Exited with exception: ",flag);
            if(flag instanceof Error) {
                throw (Error) flag;
            }
        }


    }

}
