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

package org.radixware.kernel.common.compiler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProviderFactory;
import org.radixware.kernel.common.defs.ads.build.Make;
import org.radixware.kernel.common.defs.ads.common.AdsVisitorProviders;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.types.Id;


public class EntireRadixCompileTest {

    @Test
    public void execute() throws IOException {
        final Branch branch = new BranchLoader().getBranch();
        branch.visit(new IVisitor() {
            @Override
            public void accept(RadixObject radixObject) {
            }
        }, VisitorProviderFactory.createDefaultVisitorProvider());

        long total = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            long t = System.currentTimeMillis();
          //  compile(branch, ERuntimeEnvironmentType.COMMON);
            compile(branch, ERuntimeEnvironmentType.EXPLORER);
          //  compile(branch, ERuntimeEnvironmentType.SERVER);
            t = System.currentTimeMillis() - t;
            System.err.println("Done: " + t);
        }
        total = System.currentTimeMillis() - total;
        System.err.println("Total time: " + total);
    }

    private void compile(Branch root, ERuntimeEnvironmentType env) {
        final Collection<RadixObject> radixObjects = new ArrayList<>();
        root.visit(new IVisitor() {
            @Override
            public void accept(RadixObject radixObject) {
                radixObjects.add(radixObject);
            }
        }, AdsVisitorProviders.newCompileableDefinitionsVisitorProvider(env));
        Make make = new Make();
        final Map<Layer, List<Definition>> layer2DefList = new HashMap<>();
        for (RadixObject radixObject : radixObjects) {
            if (radixObject instanceof Definition) {
                final Layer layer = radixObject.getLayer();
                List<Definition> list = layer2DefList.get(layer);
                if (list == null) {
                    list = new ArrayList<>();
                    layer2DefList.put(layer, list);
                }
                list.add((Definition) radixObject);
            }
        }
        final List<Branch> branches = new ArrayList<>(3);
        if (!layer2DefList.isEmpty()) {
            for (Layer l : layer2DefList.keySet()) {
                final Branch branch = l.getBranch();
                if (!branches.contains(branch)) {
                    branches.add(branch);
                }
            }
        }
        for (final Branch branch : branches) {
            List<Layer> layers = branch.getLayers().getInOrder();
            for (final Layer l : layers) {
                final List<Definition> definitions = layer2DefList.get(l);
                if (definitions != null && !definitions.isEmpty()) {
                    make.compile(l,
                            definitions,
                            env,
                            new IProblemHandler() {
                        @Override
                        public void accept(RadixProblem problem) {
                            System.err.println(problem.getMessage());
                        }
                    },
                            new HashMap<Id, Id>(),
                            false,
                            false,
                            null,
                            null);
                }
            }
        }

    }
}
