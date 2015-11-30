/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nbb.demetra.tutorial.plugin.output.seats;

import ec.satoolkit.seats.SeatsResults;
import ec.tstoolkit.sarima.SarimaModel;
import ec.tstoolkit.ucarima.UcarimaModel;

/**
 *
 * @author Jean Palate
 */
public class SeatsSummary {
    
    private final UcarimaModel ucm_;
    private final double ser_;
    private final boolean changed_, cutoff_, mean_;
    private final String name_;
    
    public SeatsSummary(String name, SeatsResults rslts){
        name_=name;
        if (rslts != null){
        ucm_=rslts.getUcarimaModel();
        ser_=rslts.getModel().getSer();
        changed_=rslts.getModel().isChanged();
        cutoff_=rslts.getModel().isCutOff();
        mean_=rslts.getModel().isMeanCorrection();
        }else{
            ucm_=null;
            ser_=0;
            changed_=false;
            cutoff_=false;
            mean_=false;
        }
    }
    
    public String getName(){
        return name_;
    }
    
    public boolean isValid(){
        return ucm_ != null;
    }
    
    public UcarimaModel getUcarima(){
        return ucm_;
    }
    
    public boolean isChanged(){
        return changed_;
    }

    public boolean isCutOff(){
        return cutoff_;
    }
    
    public boolean isMeanCorrection(){
        return mean_;
    }
    
    public double getSer(){
        return ser_;
    }
}
