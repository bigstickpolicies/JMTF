package core;

import java.util.logging.*;

public class JMTFSettings implements Cloneable{
    private int corePoolSize;
    private boolean verbose;
    private Logger logger;
    public JMTFSettings() {
        corePoolSize =19191;
        verbose=false;
        logger=Logger.getLogger("jmtf-logger");
    }

    public int getCorePoolSize() {
        return corePoolSize;
    }
    public Logger getLogger() {
        return logger;
    }
    public boolean getVerbose() {
        return verbose;
    }

    public JMTFSettings setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
        return this;
    }
    public JMTFSettings setVerbose(boolean verbose) {
        this.verbose=verbose;
        return this;
    }
    public JMTFSettings setLogger(Logger logger) {
        this.logger=logger;
        return this;
    }
    @Override
    public JMTFSettings clone() {
        JMTFSettings settings=new JMTFSettings();
        settings.setCorePoolSize(getCorePoolSize());
        settings.setVerbose(getVerbose());
        settings.setLogger(getLogger());
        return settings;
    }
}

