package org.radixware.kernel.designer.common.dialogs.utils;

import java.awt.Color;
import java.io.IOException;
import org.openide.util.Exceptions;
import org.openide.windows.IOColorLines;
import org.openide.windows.IOColorPrint;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;

public class InputOutputPrinter {
    private volatile InputOutput inputOutput;
    public Color COLOR_ERROR = Color.RED.darker();
    public Color COLOR_WARRNING = new Color(243, 111, 20);
    public Color COLOR_INFO = Color.BLUE;

    public InputOutputPrinter(String title) {
        this.inputOutput = IOProvider.getDefault().getIO(title, false);
    }
    
    public InputOutputPrinter(String title, boolean isNewTab) {
        this.inputOutput = IOProvider.getDefault().getIO(title, isNewTab);
    }
    
    public void reset() throws IOException{
        inputOutput.getOut().reset();
    }
    
    public void select(){
        inputOutput.select();
    }
    
    public void print(String text) throws IOException{
        inputOutput.getOut().print(text);
    }
    
    public void printError(String text) throws IOException{
        print(text, COLOR_ERROR);
    }
    
    public void printWarning(String text) throws IOException{
        print(text, COLOR_WARRNING);
    }
    
    public void printInfo(String text) throws IOException{
        print(text, COLOR_INFO);
    }
    
    public void print(String text, Color color) throws IOException{
        IOColorPrint.print(inputOutput, text, color);
    }
    
    public void println(String text) throws IOException{
        inputOutput.getOut().println(text);
    }
    
    public void printlnError(String text) throws IOException{
        println(text, COLOR_ERROR);
    }
    
    public void printlnWarning(String text) throws IOException{
        println(text, COLOR_WARRNING);
    }
    
    public void printlnInfo(String text) throws IOException{
        println(text, COLOR_INFO);
    }
    
    public void println(String text, Color color) throws IOException{
        IOColorLines.println(inputOutput, text, color);
    }
}
