package com.cmsc436.ms_diagnostic.step_counter_test;

/**
 * Created by laurenleach on 4/16/17.
 */

// Will listen to step alerts
public interface StepListener {
    void step(long timeNs);


}
