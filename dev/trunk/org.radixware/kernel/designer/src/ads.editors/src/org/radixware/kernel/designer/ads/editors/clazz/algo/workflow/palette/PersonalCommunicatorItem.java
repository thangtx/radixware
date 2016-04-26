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

package org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.palette;

import java.util.Arrays;
import java.util.List;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.AdsAppObject;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.ObjectFactory;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.AdsPin;
import org.radixware.kernel.common.utils.RadixResourceBundle;
import org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.dialog.*;
import org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.dialog.EditorDialog.EditorPanel;


class PersonalCommunicatorItem extends AppItem {

    public static final String CLASS_NAME = AdsAppObject.PERSO_COM_CLASS_NAME;

    public PersonalCommunicatorItem() {
        super(CLASS_NAME, Group_.GROUP_APPBLOCKS, RadixResourceBundle.getMessage(PersonalCommunicatorItem.class, "PersonalCommunicator"), AdsDefinitionIcon.WORKFLOW.PERSONAL_COMMUNICATOR);
    }

    @Override
    public boolean init(AdsAppObject node) {
        super.init(node);
        node.setSourcePins(Arrays.asList(
                ObjectFactory.createPin(Item.ICON_OK)
                ));
        return true;
    }

    @Override
    public EditorPanel<AdsAppObject> getPanel(AdsAppObject node) {
        return new TabbedPanel(node,
                Arrays.asList(
                (EditorDialog.EditorPanel)new PreExecutePanel(node),
                (EditorDialog.EditorPanel)new PostExecutePanel(node),
                (EditorDialog.EditorPanel)new PersonalCommunicatorPanel(node)
                ));
    }

    @Override
    public void sync(AdsAppObject node) {
        List<AdsPin> pins = node.getSourcePins();
        AdsAppObject.Prop timeout = node.getPropByName("responseTimeout");
        if (timeout.getValue() != null) {
            if (pins.size() < 2) {
                pins.add(ObjectFactory.createPin(Item.ICON_TIMEOUT));
                node.setSourcePins(pins);
            }
        } else {
            if (pins.size() == 2) {
                pins.remove(1);
                node.setSourcePins(pins);
            }
        }
    }
}

