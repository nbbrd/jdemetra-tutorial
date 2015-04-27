/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nbb.demetra.tutorial.plugin.basic.output;

import ec.tss.sa.ISaOutputFactory;

/**
 * The output factory must provide an output object corresponding to the 
 * current configuration, if any.
 * It must also provide a read/write object representing that configuration
 * @author Jean Palate
 */
public class Example1OutputFactory implements ISaOutputFactory{
    //public static final CsvOutputFactory Default = new CsvOutputFactory();

    public static final String NAME = "Basic output (example1)";
    private Example1Config config_;
    private boolean enabled_ = true;

    public Example1OutputFactory() {
        config_ = new Example1Config();
    }

    public Example1OutputFactory(Example1Config config) {
        config_ = config;
    }

    public Example1Config getConfiguration() {
        return config_;
    }

    @Override
    public void dispose() {
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getDescription() {
        return NAME;
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled_;
    }

    @Override
    public void setEnabled(boolean enabled) {
        enabled_ = enabled;
    }

    @Override
    public Object getProperties() {
        try {
            return config_.clone();
        }
        catch (Exception ex) {
            return null;
        }
    }

    @Override
    public void setProperties(Object obj) {
        Example1Config config = (Example1Config) obj;
        if (config != null) {
            try {
                config_ =  config.clone();
            }
            catch (Exception ex) {
                config_ = null;
            }
        }
    }

    @Override
    public Example1Output create() {
        return new Example1Output(config_);
    }
}
