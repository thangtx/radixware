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
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.radixware.kernel.designer.ads.build;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import org.netbeans.modules.java.source.parsing.JavacParser;
import org.netbeans.modules.java.source.parsing.JavacParserFactory;
import org.netbeans.modules.parsing.api.Snapshot;
import org.netbeans.modules.parsing.api.Source;
import org.openide.filesystems.FileObject;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.ads.module.AdsModule;

public class RadixPreviewParserFactory extends JavacParserFactory {
    
    @Override
    public JavacParser createParser(final Collection<Snapshot> snapshots) {
        assert snapshots != null;
        Logger rootLogger = LogManager.getLogManager().getLogger("org.netbeans.modules.parsing.impl.SourceCache");
        rootLogger.setLevel(Level.WARNING);
        for (Snapshot snapshot : snapshots) {
            Source source = snapshot.getSource();
            FileObject fo = source == null ? null : source.getFileObject();
            if (fo != null) {
                String ext = fo.getExt();
                if ("java".equals(ext)) {
                    FileObject parent = fo.getParent();
                    if (parent != null && parent.isFolder() && Module.PREVIEW_DIR_NAME.equals(parent.getName())) {
                        parent = parent.getParent();
                        if (parent != null && parent.isFolder() && AdsModule.SOURCES_DIR_NAME.equals(parent.getName())) {
                            return null;
                        }
                    }
                }
            }
        }
        return super.createParser(snapshots);
    }

}
