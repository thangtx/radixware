package org.radixware.kernel.license;

/**
 *
 * @author dsafonov
 */
public interface ILicenseInfo {
    
    public String getName();
    
    public long getMillisLeftFromNanoTime(final long fromNanoTime);
    
    public boolean wasAvailable();

}
