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

package org.radixware.kernel.designer.api.editors;

import java.awt.GridBagConstraints;
import org.radixware.kernel.common.defs.RadixObject;


class DefaultApiEditorBuilder<T extends RadixObject> extends ApiEditorBuilder<T> {

    public DefaultApiEditorBuilder(T object, BrickFactory componentFactory) {
        super(object, componentFactory);
    }

    @Override
    public ApiEditorModel<T> buildModel() {
        final ApiEditorModel<T> editorModel = new ApiEditorModel<>(getSource());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.LINE_START;

        Brick brick = getBrickFactory().create(BrickFactory.OVERVIEW, getSource(), constraints);
        editorModel.getBricks().add(brick);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.LINE_START;

        brick = getBrickFactory().create(BrickFactory.MEMBERS, getSource(), constraints);
        editorModel.getBricks().add(brick);

        return editorModel;
    }
}
