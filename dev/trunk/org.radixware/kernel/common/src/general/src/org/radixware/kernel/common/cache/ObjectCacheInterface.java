/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.radixware.kernel.common.cache;

/**
 *
 * @author achernomyrdin
 */
public interface ObjectCacheInterface {
    public <T> T get(final String key, final Class<T> clazz);
    public Object get(final String key);
    public boolean putExpiringSinceLastUsage(final String key, final Object data, final int expirationSeconds);
    public boolean putExpiringSinceCreation(final String key, final Object data, final int expirationSeconds);
    public void remove(final String key);
    public void clear();
    public void maintenance();
}
