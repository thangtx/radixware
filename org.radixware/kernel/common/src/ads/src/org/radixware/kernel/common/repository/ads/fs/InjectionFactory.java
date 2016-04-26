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

package org.radixware.kernel.common.repository.ads.fs;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.repository.fs.RepositoryInjection.ModuleInjectionInfo;
import org.radixware.kernel.common.exceptions.RadixPrivateException;
import org.radixware.kernel.common.repository.fs.RepositoryInjection;


public class InjectionFactory {

    public static RepositoryInjection newInstance(File upgradeFile) throws IOException {
        UpgradeInjection info = new UpgradeInjection(upgradeFile);
        return new RepositoryInjection(info.prepare());
    }

    public static RepositoryInjection newInstance(List<File> upgradeFiles) throws IOException, RadixPrivateException {
        List<ModuleInjectionInfo> infos = new LinkedList<ModuleInjectionInfo>();
        for (File file : upgradeFiles) {
            UpgradeInjection info = new UpgradeInjection(file);
            Collection<? extends ModuleInjectionInfo> list = info.prepare();
            for (ModuleInjectionInfo m : list) {
                for (ModuleInjectionInfo added : infos) {
                    if (added.getLayerURI().equals(m.getLayerURI()) && added.getName().equals(m.getName())) {
                        throw new RadixPrivateException("Multiple entries of module " + m.getName() + " from layer " + m.getLayerURI() + " found");
                    }
                }
                infos.add(m);
            }
        }
        return new RepositoryInjection(infos);
    }
}
