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

package org.radixware.kernel.common.defs.ads.module;

import java.util.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsInterfaceClassDef;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.utils.IValueSet;
import org.radixware.schemas.product.Module.MetaInfServices;
import org.radixware.schemas.product.Module.MetaInfServices.Service.Implementation;


public final class MetaInfServicesCatalog {

    public static class ServiceImplementationVisitorProvider extends VisitorProvider {

        private final ERuntimeEnvironmentType environment;

        public ServiceImplementationVisitorProvider(ERuntimeEnvironmentType environment) {
            this.environment = environment;
        }

        @Override
        public boolean isTarget(RadixObject radixObject) {
            if (radixObject instanceof AdsClassDef) {
                final AdsClassDef cls = (AdsClassDef) radixObject;

                if (cls.getUsageEnvironment() != environment) {
                    return false;
                }

                if (cls.getAccessFlags().isAbstract()) {
                    return false;
                }

                switch (cls.getClassDefType()) {
                    case DYNAMIC:
                        return true;

                    default:
                        return false;
                }
            }
            return false;
        }
    }

    public static class ServiceInterfaceVisitorProvider extends VisitorProvider {

        private final ERuntimeEnvironmentType environment;

        public ServiceInterfaceVisitorProvider(ERuntimeEnvironmentType environment) {
            this.environment = environment;
        }

        @Override
        public boolean isTarget(RadixObject radixObject) {
            if (radixObject instanceof AdsInterfaceClassDef) {
                return ERuntimeEnvironmentType.compatibility(environment, ((AdsClassDef) radixObject).getUsageEnvironment());
            }
            return false;
        }
    }

    public final class Service {

        private final AdsPath interfaceIdPath;
        private final Set<AdsPath> implementations;

        private Service(AdsPath interfacePath) {
            this.interfaceIdPath = interfacePath;
            this.implementations = new HashSet<>();
        }

        private Service(AdsPath interfacePath, Collection<AdsPath> implementations) {
            this.interfaceIdPath = interfacePath;
            this.implementations = new HashSet<>(implementations);
        }

        public Set<AdsPath> getImplementations(ERuntimeEnvironmentType environment) {

            if (environment == null) {
                return getAllImplementations();
            }

            final Set<AdsPath> impls = new HashSet<>();

            for (final AdsPath adsPath : implementations) {
                final Definition result = adsPath.resolve(module).get();
                if (result instanceof AdsClassDef) {
                    final AdsClassDef cls = (AdsClassDef) result;

                    if (cls.getUsageEnvironment() == environment) {
                        impls.add(adsPath);
                    }
                }
            }

            return impls;
        }

        public Set<AdsPath> getAllImplementations() {
            return Collections.unmodifiableSet(implementations);
        }

        public AdsPath getInterfaceIdPath() {
            return interfaceIdPath;
        }

        public boolean addImplementation(AdsPath implementation) {
            return implementations.add(implementation);
        }

        private void removeImplementation(AdsPath implementation) {
            implementations.remove(implementation);
        }
        public static final String FILE_NAME_KEY = "FILE_NAME_KEY";
        public static final String SERVICE_IMPLEMENTATION_KEY = "SERVICE_IMPL_KEY";

        public IValueSet<String, String> getContent(ERuntimeEnvironmentType environment) {

            final StringBuilder builder = new StringBuilder();

            for (final AdsPath path : getImplementations(environment)) {
                builder.append(JavaSourceSupport.getClassFullQualifiedJavaName(path, module, false)); //TODO:!!!
                builder.append("\n");
            }

            return new ServiceContent(JavaSourceSupport.getClassFullQualifiedJavaName(interfaceIdPath, module, false), builder.toString());
        }
    }

    private static class ServiceContent implements IValueSet<String, String> {

        final String file;
        final String content;

        public ServiceContent(String file, String content) {
            this.file = file;
            this.content = content;
        }

        @Override
        public String get(String key) {
            switch (key) {
                case Service.FILE_NAME_KEY:
                    return file;
                case Service.SERVICE_IMPLEMENTATION_KEY:
                    return content;
                default:
                    return null;
            }
        }
    }
    private final List<Service> services = new LinkedList<>();
    private final AdsModule module;
    private final List<ChangeListener> listeners = new LinkedList<>();

    MetaInfServicesCatalog(AdsModule module, MetaInfServices services) {
        this(module);

        for (final MetaInfServices.Service serviceRecord : services.getServiceList()) {
            for (final Implementation implementation : serviceRecord.getImplementationList()) {
                addService(new AdsPath(serviceRecord.getIdPath()), new AdsPath(implementation.getIdPath()));
            }
        }
    }

    MetaInfServicesCatalog(AdsModule module) {
        this.module = module;
    }

    public void appendTo(MetaInfServices services) {
        for (final Service service : this.services) {
            final MetaInfServices.Service serviceRecord = services.addNewService();
            serviceRecord.setIdPath(service.getInterfaceIdPath().asList());
            List<AdsPath> sorted = new ArrayList<>(service.implementations);
            Collections.sort(sorted, new Comparator<AdsPath>() {
                @Override
                public int compare(AdsPath o1, AdsPath o2) {
                    return o1.toString().compareTo(o2.toString());
                }
            });
            for (final AdsPath path : sorted) {
                final Implementation implementation = serviceRecord.addNewImplementation();
                implementation.setIdPath(path.asList());
            }
        }
    }

    public void addService(AdsPath aInterface, AdsPath implementation) {
        Service service = findService(aInterface);
        if (service == null) {
            service = new Service(aInterface);
            services.add(service);
        }

        if (service.addImplementation(implementation)) {
            fireChanges();
        }
    }

    public void removeService(AdsPath aInterface, AdsPath implementation) {
        final Service service = findService(aInterface);

        if (service != null) {
            service.removeImplementation(implementation);

            if (service.implementations.isEmpty()) {
                services.remove(service);
            }

            fireChanges();
        }

    }

    public void removeService(AdsPath aInterface, ERuntimeEnvironmentType environment) {
        final Service service = findService(aInterface);
        if (service != null) {
            if (environment == null) {
                services.remove(service);
            } else {
                for (final AdsPath path : service.getImplementations(environment)) {
                    removeService(aInterface, path);
                }
            }

            fireChanges();
        }
    }

    public List<Service> getServices(final ERuntimeEnvironmentType environment) {

        if (environment == null) {
            return getAllServices();
        }

        final List<Service> servicesByEnvironment = new ArrayList<>();

        for (final Service service : services) {
            final Definition result = service.getInterfaceIdPath().resolve(module).get();

            if (result instanceof AdsClassDef) {
                final AdsClassDef cls = (AdsClassDef) result;
                if (ERuntimeEnvironmentType.compatibility(environment, cls.getUsageEnvironment())) {
                    if (!service.getImplementations(environment).isEmpty()) {
                        servicesByEnvironment.add(service);
                    }
                }
            }
        }

        return servicesByEnvironment;
    }

    public List<Service> getAllServices() {
        return Collections.unmodifiableList(services);
    }

    public Service findService(AdsPath idPath) {
        for (Service service : services) {
            if (Objects.equals(service.getInterfaceIdPath(), idPath)) {
                return service;
            }
        }
        return null;
    }

    public boolean isEmpty() {
        return services.isEmpty();
    }

    public void addChangeListener(ChangeListener listener) {
        listeners.add(listener);
    }

    public boolean removeChangeListener(ChangeListener o) {
        return listeners.remove(o);
    }

    private void fireChanges() {
        module.setEditState(RadixObject.EEditState.MODIFIED);

        for (final ChangeListener listener : new ArrayList<>(listeners)) {
            listener.stateChanged(new ChangeEvent(this));
        }
    }
}
