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
package org.radixware.kernel.designer.environment.navigation;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.swing.Icon;
import org.netbeans.spi.jumpto.type.SearchType;
import org.netbeans.spi.jumpto.type.TypeDescriptor;
import org.netbeans.spi.jumpto.type.TypeProvider;
import org.openide.filesystems.FileObject;
import org.openide.util.Lookup;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.VisitorProviderFactory;
import org.radixware.kernel.common.defs.ads.type.IAdsTypeSource;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.utils.namefilter.NameMatcher;
import org.radixware.kernel.common.utils.namefilter.NameMatcherFactory;
import org.radixware.kernel.designer.common.dialogs.chooseobject.SearchTypeConverter;
import org.radixware.kernel.designer.common.general.filesystem.RadixFileUtil;

public class RadixTypeProvider implements TypeProvider {

    public RadixTypeProvider() {
    }
    private VisitorProvider provider = null;
    private static Lock runLock = new ReentrantLock();
    //GoToTypeAction is called several times with different context

    @Override
    public void cancel() {
        synchronized (this) {
            if (provider != null) {
                provider.cancel();
            }
        }
    }

    @Override
    public void cleanup() {
        // NOTHING - no cashing
    }

    private static class RadixObjectTypeDescriptor extends TypeDescriptor {

        private final RadixObjectDescriptorAdapter adapter;

        public RadixObjectTypeDescriptor(final RadixObject radixObject) {
            adapter = new RadixObjectDescriptorAdapter(radixObject);
        }

        @Override
        public void open() {
            adapter.open();
        }

        @Override
        public String getTypeName() {
            return adapter.getTypeName();
        }

        @Override
        public String getSimpleName() {
            return adapter.getSimpleName();
        }

        @Override
        public String getProjectName() {
            return adapter.getProjectName();
        }

        @Override
        public Icon getProjectIcon() {
            return adapter.getProjectIcon();
        }

        @Override
        public String getOuterName() {
            return adapter.getOuterName();
        }

        @Override
        public int getOffset() {
            return adapter.getOffset();
        }

        @Override
        public Icon getIcon() {
            return adapter.getIcon();
        }

        @Override
        public FileObject getFileObject() {
            return adapter.getFileObject();
        }

        @Override
        public String getContextName() {
            return adapter.getContextName();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof RadixObjectTypeDescriptor) {
                if (((RadixObjectTypeDescriptor) obj).adapter.equals(adapter)) {
                    return true;
                }
            }
            return super.equals(obj);
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 59 * hash + Objects.hashCode(this.adapter);
            return hash;
        }       
        
    }

    private class Collector implements IVisitor {

        private final Result result;
        private final NameMatcher matcher;

        public Collector(Result result, NameMatcher matcher) {
            this.result = result;
            this.matcher = matcher;
        }

        @Override
        public void accept(RadixObject radixObject) {
            if (radixObject instanceof IAdsTypeSource) {
                final String name = radixObject.getName();
                if (matcher.accept(name) ) {
                    final TypeDescriptor typeDescriptor = new RadixObjectTypeDescriptor(radixObject);
                    result.addResult(typeDescriptor);
                }
            }
        }
    }

    @Override
    public void computeTypeNames(final Context context, final Result result) {
        if (runLock.tryLock()) {
            try {
                final NameMatcher matcher = NameMatcherFactory.createNameMatcher(context.getText(), SearchTypeConverter.convertNb2RdxSearchType(context.getSearchType()));
                if (matcher == null) {
                    return;
                }
                Collection<? extends TypeProvider> providers = Lookup.getDefault().lookupAll(TypeProvider.class);
                synchronized (this) {
                    provider = VisitorProviderFactory.createDefaultVisitorProvider();
                }

                for (Branch branch : RadixFileUtil.getOpenedBranches()) {
                    final Collector visitor = new Collector(result, matcher);
                    branch.visit(visitor, provider);
                }

                synchronized (this) {
                    provider = null;
                }
            } finally {
                runLock.unlock();
            }
        }
    }

    @Override
    public String getDisplayName() {
        return "Radix Classes";
    }

    @Override
    public String name() {
        return "Radix";
    }
}
