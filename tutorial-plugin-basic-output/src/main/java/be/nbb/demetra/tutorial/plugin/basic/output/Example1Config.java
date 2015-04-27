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
public class Example1Config implements Cloneable{
    
    public static final String NAME="example1";

    private File folder_ ;
    private String name_=NAME;

    public Example1Config () {
    }

    public File getFolder() {
        return folder_;
    }

    public void setFolder(File value) {
        folder_ = value;
    }

     public String getFileName() {
        return name_;
    }
    public void setFileName(String value) {
        name_ = value;
    }

    public Example1Config clone(){
        try {
            return (Example1Config) super.clone();
        } catch (CloneNotSupportedException ex) {
            Exceptions.printStackTrace(ex);
            return null;
        }
    }
    
}
