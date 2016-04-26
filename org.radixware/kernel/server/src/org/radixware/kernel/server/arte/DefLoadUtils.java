/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. This Source Code is distributed
 * WITHOUT ANY WARRANTY; including any implied warranties but not limited to
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Mozilla Public License, v. 2.0. for more details.
 */

package org.radixware.kernel.server.arte;

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.environment.IRadixClassLoader;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.starter.radixloader.RadixClassLoader;


public class DefLoadUtils {

    private static final String META_FIELD_NAME = "rdxMeta";

    public static Object loadDef(final Id id, final IRadixClassLoader metaClassLoader, final ILoadErrorsHandler errorHandler) {
        try {
            
            final Class c = metaClassLoader.loadMetaClassById(id);
//            Class.forName(c.getName(), true, c.getClassLoader());
//            if (c.getClassLoader() instanceof RadixClassLoader) {
//                Object cached = ((RadixClassLoader) c.getClassLoader()).getLoadedObject(getMetaKeyForClassloaderCache(c));
//                if (cached != null) {
//                    return cached;
//                }
//            }
            final Field f = c.getField(META_FIELD_NAME);
            return f.get(null);
        } catch (NoSuchFieldException ex) {
            //RADIX-1813, RADIX-1831 publications, common classes, algo and interfaces don't have meta
            //if (id.getPrefix() != EDefinitionIdPrefix.ADS_ALGORITHM_CLASS && id.getPrefix() != EDefinitionIdPrefix.ADS_INTERFACE_CLASS)//dacMeta not generated for algorithm classes
            //{
            //    registerLoadError(id, new DefinitionNotFoundError(id, e));
            //}
            Logger.getLogger(DefLoadUtils.class.getName()).log(Level.FINE, ex.getMessage(), ex);
        } catch (DefinitionNotFoundError e) {
            if (e.getDefinitionId() == id) {
                //TWRBS-1028: in some cases it's normal to search for an unexisting definition
                //for example, when we are finding out if presentation adapter is defined
                throw e; //akrylov 5.10.2010: do not register and trace as warning this kind of error
            } else {
                // definition is defined but some problems occured on load let's register them
                if (errorHandler != null) {
                    errorHandler.registerLoadError(e.getDefinitionId(), e);
                }
            }
        } catch (ExceptionInInitializerError e) {
            final Throwable cause = e.getCause() != null ? e.getCause() : e;
            if (errorHandler != null) {
                errorHandler.registerLoadError(id, new DefinitionNotFoundError(id, cause));
            }
        } catch (Throwable e) {
            if (errorHandler != null) {
                errorHandler.registerLoadError(id, new DefinitionNotFoundError(id, e));
            }
        }
        return null;
    }

}
