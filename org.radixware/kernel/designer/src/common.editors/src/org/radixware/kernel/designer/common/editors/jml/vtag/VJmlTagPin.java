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

package org.radixware.kernel.designer.common.editors.jml.vtag;

import org.radixware.kernel.common.jml.JmlTagPin;


public class VJmlTagPin extends VJmlTag<JmlTagPin> {

    public VJmlTagPin(JmlTagPin tag) {
        super(tag);
    }

    @Override
    public String getTokenName() {
        return "tag-pin";
    }

    @Override
    public String getTitle() {
        /*
        JmlTagPin tagPin = getTag();
        if (getEnv().getContext() instanceof ProgramNode) {
        ProgramNode node = (ProgramNode)getEnv().getContext();
        for (Pin pin : node.getPins()) {
        if (pin.getId().equals(tagPin.getId())) {
        return pin.toString();
        }
        }
        }
        return "<html>unknown pin [<B>" + tagPin.getId().toString() + "</B>]</html>";
         */
        return super.getTitle();
    }

    @Override
    public String getToolTip() {
        /*
        JmlTagPin tagPin = getTag();
        if (getEnv().getContext() instanceof ProgramNode) {
        ProgramNode node = (ProgramNode)getEnv().getContext();
        for (Pin pin : node.getPins()) {
        if (pin.getId().equals(tagPin.getId())) {
        return "<html><B>#" + pin.getName() + "</B></html>";
        }
        }
        }
        return "<html>unknown pin [<B>" + tagPin.getId().toString() + "</B>]</html>";
         */
        return super.getToolTip();
    }
}
