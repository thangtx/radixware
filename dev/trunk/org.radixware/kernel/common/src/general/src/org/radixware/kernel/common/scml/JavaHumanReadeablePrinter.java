package org.radixware.kernel.common.scml;

public class JavaHumanReadeablePrinter extends JavaPrinter implements IHumanReadablePrinter{
    private int dept = 0;

    @Override
    public CodePrinter println(char[] text) {
        if (dept > 0) {
            return println();
        } else {
            return super.println(text);
        }
    }

    @Override
    public CodePrinter print(char c) {
        if (dept > 0 && c != '\n' && c != '\t') {
            return this;
        } else {
            return super.print(c);
        }
    }

    @Override
    public CodePrinter print(int l) {
        if (dept > 0) {
            return this;
        } else {
            return super.print(l);
        }
    }

    @Override
    public CodePrinter print(long l) {
        if (dept > 0) {
            return super.print(l);
        } else {
            return super.print(l);
        }
    }

    @Override
    public CodePrinter enterHumanUnreadableBlock(int dept) {
        this.dept += dept;
        return this;
    }

    @Override
    public CodePrinter enterHumanUnreadableBlock() {
        return enterHumanUnreadableBlock(1);
    }

    @Override
    public CodePrinter leaveHumanUnreadableBlock(int dept) {
        this.dept -= dept;
        if (this.dept < 0) {
            throw new IllegalStateException("Stack is empty");
        }
        return this;
    }

    @Override
    public CodePrinter leaveHumanUnreadableBlock() {
        return leaveHumanUnreadableBlock(1);
    }
    

}
