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
package org.radixware.kernel.common.builder.api.userext;

import java.io.IOException;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.ERepositorySegmentType;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.Segment;
import org.radixware.kernel.common.repository.fs.IRepositoryLayer;

public class UDSLayer extends Layer {

    private final UDSLayerRepository repository;
    private List<EIsoLanguage> languages = null;
    final UDSDefCustomLoader loader;

    public UDSLayer(UDSLayerRepository repository, UDSDefCustomLoader loader) throws IOException {
        this.repository = repository;
        this.loader = loader;
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
                languages = Arrays.asList(EIsoLanguage.ENGLISH, EIsoLanguage.RUSSIAN);
            }
            return languages;
        }

    }

    @Override
    public boolean isReadOnly() {
        return false;
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected Segment createSegment(final ERepositorySegmentType type) {
        if (type == ERepositorySegmentType.ADS) {
            return new UDSAdsSegment(this);
        }
        return super.createSegment(type);
    }
}
