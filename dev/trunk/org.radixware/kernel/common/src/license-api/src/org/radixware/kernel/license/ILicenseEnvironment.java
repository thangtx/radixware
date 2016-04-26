package org.radixware.kernel.license;

import java.util.Map;

public interface ILicenseEnvironment {
    
    public String getLicenseServerAddress();
    
    public Map<String, byte[]> getLicenseFilesByLayerURIs();
    
    public long getLatestVersion();
}
