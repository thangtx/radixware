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
package org.radixware.kernel.common.userreport.extrepository;

import java.io.IOException;
import java.util.List;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.ERepositorySegmentType;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.Segment;
import org.radixware.kernel.common.repository.fs.IRepositoryLayer;
import org.radixware.kernel.common.userreport.common.UserExtensionManagerCommon;

public class UserExtLayer extends Layer {

    private final UserExtLayerRepository repository;
    private List<EIsoLanguage> languages = null;

    public UserExtLayer(UserExtLayerRepository repository) throws IOException {
        this.repository = repository;
        reloadDescriptionFromRepository(repository);
    }

    @Override
    public IRepositoryLayer getRepository() {
        return repository;
    }

    @Override
    public List<EIsoLanguage> getLanguages() {
        synchronized (this) {

            if (languages == null) {
                languages = UserExtensionManagerCommon.getInstance().getUFRequestExecutor().getLanguages();
                /* final CountDownLatch lock = new CountDownLatch(1);
                 UserExtensionManagerCommon.getInstance().getRequestExecutor().submitAction(new RequestExecutor.ExplorerAction() {
                 @Override
                 public void execute(IClientEnvironment env) {
                 try {
                 languages = new UserDefinitionSettings(env).getSupportedLanguages();
                 } finally {
                 lock.countDown();
                 }
                 }
                 });
                 try {
                 lock.await();
                 } catch (InterruptedException ex) {
                 }*/
            }
            return languages;
        }

    }

    @Override
    public boolean isReadOnly() {
        return false;
    }

    @Override
    public boolean isUserExtension() {
        return true;
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected Segment createSegment(final ERepositorySegmentType type) {
        if (type == ERepositorySegmentType.ADS) {
            return new UserExtAdsSegment(this);
        }
        return super.createSegment(type);
    }
}
