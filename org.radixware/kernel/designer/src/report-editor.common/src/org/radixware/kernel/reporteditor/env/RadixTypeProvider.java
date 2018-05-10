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

package org.radixware.kernel.reporteditor.env;

import javax.swing.Icon;
import org.netbeans.spi.jumpto.type.TypeDescriptor;
import org.netbeans.spi.jumpto.type.TypeProvider;
import org.openide.filesystems.FileObject;
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
    }

    private static class Collector implements IVisitor {

        private final Result result;
        private final NameMatcher matcher;

        public Collector(final Result result, final NameMatcher matcher) {
            this.result = result;
            this.matcher = matcher;
        }

        @Override
        public void accept(final RadixObject radixObject) {
            if (radixObject instanceof IAdsTypeSource) {
                final String name = radixObject.getName();
                if (matcher.accept(name)) {
                    final TypeDescriptor typeDescriptor = new RadixObjectTypeDescriptor(radixObject);
                    result.addResult(typeDescriptor);
                }
            }
        }
    }

    @Override
    public void computeTypeNames(final Context context, final Result result) {
        final NameMatcher matcher = NameMatcherFactory.createNameMatcher(context.getText(), SearchTypeConverter.convertNb2RdxSearchType(context.getSearchType()));
        if (matcher == null) {
            return;
        }

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
