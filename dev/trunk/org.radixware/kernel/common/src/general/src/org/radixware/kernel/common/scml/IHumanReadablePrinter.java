package org.radixware.kernel.common.scml;

public interface IHumanReadablePrinter {

    public CodePrinter enterHumanUnreadableBlock(int dept);

    public CodePrinter enterHumanUnreadableBlock();

    public CodePrinter leaveHumanUnreadableBlock(int dept);

    public CodePrinter leaveHumanUnreadableBlock();

}
