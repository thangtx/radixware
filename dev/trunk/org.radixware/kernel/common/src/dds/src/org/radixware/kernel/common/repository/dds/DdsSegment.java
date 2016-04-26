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

package org.radixware.kernel.common.repository.dds;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.radixware.kernel.common.defs.ClipboardSupport;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.dds.DdsModule;
import org.radixware.kernel.common.defs.Module.Factory;
import org.radixware.kernel.common.defs.dds.DdsDefinitionIcon;
import org.radixware.kernel.common.enums.ERepositorySegmentType;
import org.radixware.kernel.common.repository.Segment;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.RadixObjectsUtils;
import org.radixware.kernel.common.utils.XmlUtils;
import org.radixware.kernel.common.utils.events.RadixEventSource;

/**
 * DDS Segment.
 *
 */
public class DdsSegment extends Segment<DdsModule> {

    @Override
    protected Factory<DdsModule> getModuleFactory() {
        return DdsModule.Factory.getDefault();
    }

    protected DdsSegment(Layer layer) {
        super(layer);
    }

    @Override
    public ERepositorySegmentType getType() {
        return ERepositorySegmentType.DDS;
    }

    @Override
    public RadixIcon getIcon() {
        return DdsDefinitionIcon.SEGMENT;
    }
    private DdsScripts scripts = null;

    public DdsScripts getScripts() {
        synchronized (this) {
            if (scripts == null) {
                scripts = new DdsScripts(this);
                try {
                    scripts.reload();
                } catch (IOException ex) {
                    getRepository().processException(ex);
                }
            }
            return scripts;
        }
    }
    private RadixEventSource modelSupport = new RadixEventSource();

    /**
     * Get support to listen loading of DdsModelDef. Оповещает о факте загрузки
     * DDS модели, даже если загрузка не удалась (чтобы подсветить модуль
     * красным).
     */
    public RadixEventSource getModelSupport() {

        return modelSupport;

    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        getScripts().visit(visitor, provider);
    }

    public void saveDirectoryXml() throws IOException {
        final List<DdsModule> modules = getModules().list();
        RadixObjectsUtils.sortByName(modules);

        final org.radixware.schemas.product.DirectoryDocument doc = org.radixware.schemas.product.DirectoryDocument.Factory.newInstance();
        final org.radixware.schemas.product.Directory directoryEntry = doc.addNewDirectory();
        final org.radixware.schemas.product.Directory.Includes includes = directoryEntry.addNewIncludes();

        for (DdsModule module : modules) {
            final org.radixware.schemas.product.Directory.Includes.Include include = includes.addNewInclude();
            include.setFileName(module.getName() + "/" + FileUtils.DIRECTORY_XML_FILE_NAME);
        }

        final File dir = getDirectory();
        final File directoryXmlFile = new File(dir, FileUtils.DIRECTORY_XML_FILE_NAME);

        XmlUtils.saveXmlPretty(doc, directoryXmlFile);
    }

    @Override
    public ClipboardSupport<? extends Segment> getClipboardSupport() {
        return new SegmentClipboardSupport(DdsModule.class);
    }
}
