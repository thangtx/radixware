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

package org.radixware.kernel.common.client.env;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLStreamException;
import org.radixware.kernel.common.client.IClientApplication;
import org.radixware.kernel.common.client.errors.WrongDefinitionFormatError;
import org.radixware.kernel.common.client.meta.Definition;
import org.radixware.kernel.common.client.meta.RadDomainPresentationDef;
import org.radixware.kernel.common.client.meta.RadEnumPresentationDef;
import org.radixware.kernel.common.client.meta.explorerItems.RadParagraphDef;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EEventSource;
import org.radixware.kernel.common.environment.IEnvironmentAccessor;
import org.radixware.kernel.common.environment.IRadixEnvironment;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.meta.RadMlStringBundleDef;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.starter.meta.LayerMeta;
import org.radixware.kernel.starter.radixloader.RadixLoaderException;


public final class Release {

    private final static String META_FIELD_NAME = "rdxMeta";
    private final IAdsClassLoader classLoader;
    private final long version;
    public final Map<String, Object> userData = new HashMap<>();
    private final Map<Id, Definition> definitions = new HashMap<>(256);
    private final Map<Id, RadMlStringBundleDef> stringBundles = new HashMap<>(256);
    private Map<Id, DefinitionError> loadErrors = new HashMap<>();
    private final IClientApplication env;
    private final Object defLock = new Object();
    private final Object domainLock = new Object();
    private Collection<RadParagraphDef> explorerRoots;

    public Release(final IClientApplication env, final long version, final boolean showWaitDialog) {
        this.version = version;
        this.env = env;
        if (showWaitDialog) {
            final Callable<IAdsClassLoader> task = new Callable<IAdsClassLoader>() {

                @Override
                public IAdsClassLoader call() throws Exception {
                    return createClassLoader();
                }
            };
            final ITaskWaiter waiter = env.newTaskWaiter();
            waiter.setMessage(env.getMessageProvider().translate("ExplorerMessage", "Loading Components"));
            try {
                classLoader = waiter.runAndWait(task);
                // classLoader = createClassLoader();
            } catch (InterruptedException ex) {
                throw new RadixError("Can\'t create ads class loader: creation was interrupted", ex);
            } catch (ExecutionException ex) {
                throw new RadixError("Can\'t create ads class loader", ex);
            } finally {
                waiter.close();
            }
        } else {
            try {
                classLoader = createClassLoader();
            } catch (IOException | XMLStreamException ex) {
                throw new RadixError("Can\'t create ads class loader", ex);
            }
        }
    }

    ReleaseRepository getRevisionMeta() {
        return classLoader.getRepository();
    }

    private IAdsClassLoader createClassLoader() throws IOException, XMLStreamException {
        Iterator ps = javax.imageio.spi.ServiceRegistry.lookupProviders(IAdsClassLoader.class);
        if (ps.hasNext()) {
            return (IAdsClassLoader) ps.next();
        } else {
            return AdsClassLoader.createInstance(env, version);
        }
    }

    public Definition getDefinition(final Id id) {
        if (id==null){
            throw new DefinitionNotFoundError(null);
        }
        synchronized (defLock) {
            Definition result = definitions.get(id);
            if (result != null) {
                return result;
            }
            checkLoadError(id);
            result = (Definition) loadDefinition(id, META_FIELD_NAME);
            if (result != null) {
                if (id.getPrefix() != EDefinitionIdPrefix.USER_DEFINED_REPORT) {
                    definitions.put(id, result);
                }
                return result;
            } else {
                checkLoadError(id);
            }
            throw new DefinitionNotFoundError(id);
        }
    }

    public RadEnumPresentationDef getEnumDef(final Id id) {
        synchronized (defLock) {//Loading enumdef in RadPropertyDef constructor
            Definition result = definitions.get(id);
            if (result != null) {
                return (RadEnumPresentationDef) result;
            }
            checkLoadError(id);
            final org.radixware.kernel.common.meta.RadEnumDef dbu =
                    (org.radixware.kernel.common.meta.RadEnumDef) loadDefinition(id, META_FIELD_NAME);
            if (dbu == null) {
                checkLoadError(id);
                throw new DefinitionNotFoundError(id);
            }
            result = new RadEnumPresentationDef(dbu);
            if (result != null) {
                definitions.put(id, result);
                return (RadEnumPresentationDef) result;
            } else {
                checkLoadError(id);
            }
            throw new DefinitionNotFoundError(id);
        }
    }

    public RadDomainPresentationDef getDomainPresentationDef(final Id id) {
        synchronized (domainLock) {
            Definition result = definitions.get(id);
            if (result != null) {
                return (RadDomainPresentationDef) result;
            }
            checkLoadError(id);
            final org.radixware.kernel.common.meta.RadDomainDef dbu =
                    (org.radixware.kernel.common.meta.RadDomainDef) loadDefinition(id, META_FIELD_NAME);
            if (dbu == null) {
                checkLoadError(id);
                throw new DefinitionNotFoundError(id);
            }
            result = new RadDomainPresentationDef(dbu);
            if (result != null) {
                definitions.put(id, result);
                return (RadDomainPresentationDef) result;
            } else {
                checkLoadError(id);
            }
            throw new DefinitionNotFoundError(id);
        }
    }

   public RadMlStringBundleDef getMlStringBundle(final Id id) {
        synchronized (defLock) {
            RadMlStringBundleDef result = stringBundles.get(id);
            if (result != null) {
                return result;
            }
            checkLoadError(id);
            result = (RadMlStringBundleDef) loadDefinition(id, META_FIELD_NAME);
            if (result != null) {
                String prefixSuffix = id.toString().substring(3, 6);
                if (!EDefinitionIdPrefix.USER_DEFINED_REPORT.getValue().equals(prefixSuffix)) {
                    stringBundles.put(id, result);
                }
                return result;
            } else {
                checkLoadError(id);
            }
            throw new DefinitionNotFoundError(id);
        }
    }


    private static class DelegateClassLoader extends ClassLoader implements IEnvironmentAccessor {

        private final IClientApplication env;

        public DelegateClassLoader(ClassLoader parent, IClientApplication env) {
            super(parent);
            this.env = env;
        }

        @Override
        public IRadixEnvironment getEnvironment() {
            return env;
        }
    }

    private Object loadDefinition(final Id id, final String metaFieldName) {        
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (!(cl instanceof IEnvironmentAccessor) || ((IEnvironmentAccessor) cl).getEnvironment() != env) {
            Thread.currentThread().setContextClassLoader(new DelegateClassLoader(cl, env));
        }

        final Class defClass;
        try {
            defClass = classLoader.loadMetaClassById(id);
        } catch (ClassNotFoundException e) {
            if(e.getCause() instanceof RadixLoaderException) {
                //loading of definition failed, could be that SVN is temporary unavailable, 
                //so retry may succeed later
                throw new DefinitionNotFoundError(id, e.getCause());
            }
            return null;
        }
        try {
            final Field f = defClass.getField(metaFieldName);
            final Object meta = f.get(null);

            if (id.getPrefix() == EDefinitionIdPrefix.ADS_LOCALIZING_BUNDLE) {
                if (!(meta instanceof RadMlStringBundleDef)) {
                    registerLoadError(id, new WrongDefinitionFormatError(id, "Field " + metaFieldName + " in class " + defClass.getName() + " is not string bundle"));
                    return null;
                }
            } else if (id.getPrefix() == EDefinitionIdPrefix.ADS_ENUMERATION) {
                if (!(meta instanceof org.radixware.kernel.common.meta.RadEnumDef)) {
                    registerLoadError(id, new WrongDefinitionFormatError(id, "Field " + metaFieldName + " in class " + defClass.getName() + " is not enumeration"));
                    return null;
                }
            } else if (id.getPrefix() == EDefinitionIdPrefix.DOMAIN) {
                if (!(meta instanceof org.radixware.kernel.common.meta.RadDomainDef)) {
                    registerLoadError(id, new WrongDefinitionFormatError(id, "Field " + metaFieldName + " in class " + defClass.getName() + " is not domain"));
                    return null;
                }
            } else if (!(meta instanceof Definition)) {
                Class c = meta.getClass();
                while (c != null && c != Object.class) {
                    c = c.getSuperclass();
                }
                c = Definition.class;
                while (c != null && c != Object.class) {
                    c = c.getSuperclass();
                }
                registerLoadError(id, new WrongDefinitionFormatError(id, "Field " + metaFieldName + " in class " + defClass.getName() + " is not definition"));
                return null;
            }
            return meta;
        } catch (NoSuchFieldException e) {
            registerLoadError(id, new WrongDefinitionFormatError(id, " Field " + metaFieldName + " was not fownd in class " + defClass.getName()));
        } catch (ExceptionInInitializerError e) {
            final Throwable cause = e.getCause() != null ? e.getCause() : e;
            registerLoadError(id, new DefinitionError("Can't get an instance of definition #" + id.toString(), cause));
        } catch (Throwable e) {
            registerLoadError(id, new DefinitionError("Can't get an instance of definition #" + id.toString(), e));
        }
        return null;
    }

    private void registerLoadError(final Id defId, final DefinitionError err) {
        if (loadErrors == null) {
            loadErrors = new HashMap<Id, DefinitionError>();
        }
        loadErrors.put(defId, err);
        final String message = env.getMessageProvider().translate("TraceMessage", "Can't load definition #%s");
        env.getTracer().error(String.format(message, defId), err, EEventSource.EXPLORER);
    }

    private void checkLoadError(final Id defId) throws DefinitionError {
        if (loadErrors != null) {
            final RadixError err = loadErrors.get(defId);
            if (err != null) {
                throw err;
            }
        }
    }

    public long getRevisionVersion() {
        return version;
    }

    public IAdsClassLoader getClassLoader() {
        return classLoader;
    }

    public Collection<RadParagraphDef> getExplorerRoots() {
        if (explorerRoots == null) {
            final Map<String,String[]> accessibleExplorerRootIdsByLayerUri = 
                classLoader.getRadixClassLoader().getRevisionMeta().getAccessibleExplorerRootIds();
            final Collection<ReleaseRepository.DefinitionInfo> paragraphDefinitions =
                    getClassLoader().getRepository().getDefinitions(EDefType.PARAGRAPH);
            RadParagraphDef paragraphDef;
            explorerRoots = new LinkedList<>();
            for (ReleaseRepository.DefinitionInfo definitionInfo : paragraphDefinitions) {
                try {
                    paragraphDef = (RadParagraphDef) getDefinition(definitionInfo.id);                    
                    if (paragraphDef.isRoot()){
                        final String[] accessibleExplorerRootIds = 
                                accessibleExplorerRootIdsByLayerUri.get(paragraphDef.getLayerUri());
                        if (accessibleExplorerRootIds==null || 
                            Arrays.asList(accessibleExplorerRootIds).contains(paragraphDef.getId().toString())){
                            explorerRoots.add(paragraphDef);
                        }
                    }
                } catch (DefinitionError ex) {
                    //just ignore
                    Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                }
            }
        }
        return Collections.unmodifiableCollection(explorerRoots);
    }

    void clearCache() {
        userData.clear();
        definitions.clear();
        stringBundles.clear();
        loadErrors.clear();
    }
}
