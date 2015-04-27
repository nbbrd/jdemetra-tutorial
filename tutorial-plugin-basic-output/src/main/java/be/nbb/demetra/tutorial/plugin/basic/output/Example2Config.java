/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nbb.demetra.tutorial.plugin.basic.output;

import java.io.File;
import org.openide.util.Exceptions;

/**
 * Contains the properties used in the creation of the output.
 * Must be cloneable
 * @author Jean Palate
 */
public class Example2Config implements Cloneable{
    
    public static final String NAME="series-";

    private File folder_ ;
    private String prefix_=NAME;

    public Example2Config () {
    }

    public File getFolder() {
        return folder_;
    }

    public void setFolder(File value) {
        folder_ = value;
    }

     public String getFilePrefix() {
        return prefix_;
    }
    public void setFilePrefix(String value) {
        prefix_ = value;
    }

    public Example2Config clone(){
        try {
            return (Example2Config) super.clone();
        } catch (CloneNotSupportedException ex) {
            Exceptions.printStackTrace(ex);
            return null;
        }
    }
    
}
