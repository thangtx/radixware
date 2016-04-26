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

package org.radixware.kernel.designer.common.editors.branch;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;


final class BaseDistUriManager {

    static final class DistributableLayersModel {

        private final Set<String> baseDistrUris;
        private final Set<String> excludeFromRelease;
        private final Branch branch;

        public DistributableLayersModel(Branch branch) {
            this.branch = branch;
            baseDistrUris = new HashSet<>(branch.getBaseDistURIs());
            excludeFromRelease = new HashSet<>();

            for (Layer layer : branch.getLayers()) {
                if (!layer.isForRelease()) {
                    excludeFromRelease.add(layer.getURI());
                }
            }
        }
        
        private DistributableLayersModel(DistributableLayersModel model) {
            this.branch = model.branch;
            baseDistrUris = new HashSet<>(model.baseDistrUris);
            excludeFromRelease = new HashSet<>(model.excludeFromRelease);
        }


        public Branch getBranch() {
            return branch;
        }
        
        public Set<String> getBaseDistrUris() {
            return baseDistrUris;
        }

        boolean isExcludedFromRelease(String uri) {
            return excludeFromRelease.contains(uri);
        }

        boolean isBaseDistrUri(String uri) {
            return baseDistrUris.contains(uri);
        }

        public void apply() {
            if (branch != null) {
                branch.setBaseDistURIs(new ArrayList<>(baseDistrUris));

                for (final Layer layer : branch.getLayers()) {
                    layer.setForRelease(!excludeFromRelease.contains(layer.getURI()));
                }
            }
        }
        
        void setExcludedFromRelease(String uri, boolean exclude) {
            if (exclude) {
                excludeFromRelease.add(uri);
            } else {
                excludeFromRelease.remove(uri);
            }
        }
        
        void setBaseDistrUri(String uri, boolean isBase) {
            if (isBase) {
                baseDistrUris.add(uri);
            } else {
                baseDistrUris.remove(uri);
            }
        }
        
        DistributableLayersModel getCopy() {
            return new DistributableLayersModel(this);
        }
    }
    
    private DistributableLayersModel model;
    public BaseDistUriManager(Branch branch) {

        model = new DistributableLayersModel(branch);
    }

    public void apply() {
        model.apply();
    }
    
    void setModel(DistributableLayersModel model) {
        this.model = model;
    }

    public DistributableLayersModel getModel() {
        return model;
    }
}
