package org.radixware.kernel.common.jml;

import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.scml.IHumanReadablePrinter;

public class JmlTagWriter extends JavaSourceSupport.CodeWriter {
    private final Jml.Tag tag;
    
    public JmlTagWriter(JavaSourceSupport support, JavaSourceSupport.UsagePurpose usagePurpose, Jml.Tag tag) {
        super(support, usagePurpose);
        this.tag = tag;
    }

    
    
    @Override
    public boolean writeCode(CodePrinter printer) {
        if (printer instanceof IHumanReadablePrinter) {
            if (tag == null) {
                printer.printError();
            } else {
                printer.print(tag.getDisplayName());
            }
            return true;
        }
        return false;
    }

    @Override
    public void writeUsage(CodePrinter printer) {
    }
    
}
