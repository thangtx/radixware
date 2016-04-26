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

package org.radixware.kernel.designer.common.dialogs.utils;

import java.io.File;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.resources.icons.RadixIcon;


class MoveTransformationFactory implements IMoveTransformationFactory {

    private static Layer getLayer(RadixObject radixObject) {
        while (radixObject != null) {
            if (radixObject instanceof Layer) {
                return (Layer) radixObject;
            }
            radixObject = radixObject.getContainer();
        }
        return null;
    }

    private static String getTitle(RadixObject radixObject) {
        final Definition def = radixObject.getDefinition();
        if (def != null) {
            radixObject = def;
        }
        return radixObject.getTypeTitle().toLowerCase() + " '" + radixObject.getQualifiedName() + "'";
    }

    private static String getUpperTitle(RadixObject radixObject) {
        final String title = getTitle(radixObject);
        return title.substring(0, 1).toUpperCase() + title.substring(1);
    }

    private static File getFileInDestination(final Definition movedDef, final RadixObject destination) {
        if (!movedDef.isSaveable()) {
            return null;
        }

        final File movedDefFile = movedDef.getFile();
        if (movedDefFile == null) {
            return null;
        }

        final String fileName = movedDefFile.getName();
        final String parentName = movedDefFile.getParentFile().getName();

        if (movedDef instanceof Module) {
            final Layer destLayer = getLayer(destination);
            if (destLayer != null) {
                return new File(new File(destLayer.getDirectory(), parentName), fileName);
            }
        } else if (movedDef instanceof AdsDefinition) {
            final Module destModule = destination.getModule();
            if (destModule != null) {
                return new File(new File(destModule.getDirectory(), parentName), fileName);
            }
        }

        return null;
    }

    private static class UnableToMove implements IMoveTransformation {

        private final String displayName;
        private final String cause;
        private final RadixIcon icon;

        public UnableToMove(Definition movedDef, RadixObject destination, String cause) {
            this.displayName = "Unable to move " + getTitle(movedDef) + " to " + getTitle(destination);
            this.cause = cause;
            icon = movedDef.getIcon();
        }

        @Override
        public boolean isPossible() {
            return false;
        }

        @Override
        public String getDisplayName() {
            return displayName;
        }

        @Override
        public String getCause() {
            return cause;
        }

        @Override
        public void perform() {
            // NOTHING
        }

        @Override
        public RadixIcon getIcon() {
            return icon;
        }
    }

    @Override
    public IMoveTransformation findTransformation(final RadixObject externalRadixObject, final Definition movedDef, final RadixObject destination) {
        final Layer externalLayer = getLayer(externalRadixObject);
        final Layer sourceLayer = getLayer(movedDef);
        final Layer destinationLayer = getLayer(destination);

        if (externalLayer != null && destinationLayer != null && destinationLayer.isHigherThan(externalLayer)) {
            return new UnableToMove(movedDef, destination, getUpperTitle(externalRadixObject) + " is refer to it.");
        }

        final File file = getFileInDestination(movedDef, destination);
        if (file != null && file.exists()) {
            return new UnableToMove(movedDef, destination, "File '" + file.getAbsolutePath() + "' already exist.");
        }

        final Module externalModule = externalRadixObject.getModule();
        final Module sourceModule = movedDef.getModule();
        final Module destModule = destination.getModule();

        if (externalModule != null
                && sourceModule != null
                && destModule != null
                && externalModule.getId() != destModule.getId()
                && sourceModule.getId() != destModule.getId()
                && !externalModule.getDependences().contains(destModule)) {
            return new IMoveTransformation() {

                @Override
                public boolean isPossible() {
                    return true;
                }

                @Override
                public String getDisplayName() {
                    return "Add dependence into " + getTitle(externalModule) + " to " + getTitle(destModule);
                }

                @Override
                public String getCause() {
                    return getUpperTitle(externalRadixObject) + " refer to " + getTitle(movedDef);
                }

                @Override
                public void perform() {
                    externalModule.getDependences().add(destModule);
                }

                @Override
                public RadixIcon getIcon() {
                    return externalModule.getIcon();
                }
            };
        }

        return null;
    }

    public boolean matches(Definition def) {
        return false;
    }
}
