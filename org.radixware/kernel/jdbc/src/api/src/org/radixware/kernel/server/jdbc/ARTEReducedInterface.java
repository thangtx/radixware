/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.radixware.kernel.server.jdbc;

import org.radixware.kernel.common.exceptions.WrongFormatError;
import org.radixware.kernel.common.trace.IRadixTrace;

/**
 *
 * @author achernomyrdin
 */
public interface ARTEReducedInterface {

    boolean needBreak();

    boolean suppressDbForciblyCloseErrors();

    RadixConnection getRadixConnection();

    @Deprecated
    void markActive();

    @Deprecated
    void markInactive();
    
    /**
     * 
     * @param comment will be included to debug tracing
     */
    void markActive(final String comment);
    
    /**
     * 
     * @param comment will be included to debug tracing
     */
    void markInactive(final String comment);

    long staticCalcSpNesting(final String spId) throws WrongFormatError;

    IRadixTrace getRadixTrace();

    Thread getProcessorThread();

    String getRequestClientId();
}
