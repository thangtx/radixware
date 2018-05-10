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

package org.radixware.wps.views.paragraph;

import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.explorerItems.RadParagraphDef;
import org.radixware.kernel.common.client.models.CleanModelController;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.models.ParagraphModel;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.types.ViewRestrictions;
import org.radixware.kernel.common.client.views.IParagraphEditor;
import org.radixware.kernel.common.client.views.IView;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.icons.WpsIcon;
import org.radixware.wps.rwt.Container;
import org.radixware.wps.rwt.PictureBox;
import org.radixware.wps.views.ViewSupport;


public class ParagraphView extends Container implements IParagraphEditor {

    private ParagraphModel model;
    private final WpsEnvironment env;
    private final ViewSupport<ParagraphView> viewSupport;
    private final ViewRestrictions restrictions;
    private final PictureBox pictureBox = new PictureBox();

    public ParagraphView(WpsEnvironment env) {
        this.env = env;
        this.viewSupport = new ViewSupport<>(this);
        restrictions = new ViewRestrictions(this);
        add(pictureBox);
        pictureBox.getHtml().setAttr("role", "logo");
        html.layout("$RWT.paragEditor.layout");
    }

    @Override
    public void open(Model model) {
        if (model instanceof ParagraphModel) {
            this.model = (ParagraphModel) model;
            try {
                RadParagraphDef def = this.model.getParagraphDef();
                Icon icon = def.getLogo();
                if (icon instanceof WpsIcon) {
                    pictureBox.setWidth(300);
                    pictureBox.setHeight(300);
                    pictureBox.setIcon((WpsIcon) icon);
                    pictureBox.setVisible(true);
                } else {
                    pictureBox.setVisible(false);
                }
            } catch (DefinitionError error) {
            }
            model.setView(this);
        }
        setObjectName("rx_parag_view_#"+model.getDefinition().getId());
    }

    @Override
    public void setFocus() {
    }

    @Override
    public boolean setFocusedProperty(Id id) {
        return false;
    }

    @Override
    public boolean close(boolean forced) {
        model.setView(null);
        return true;
    }

    @Override
    public void finishEdit() {
    }

    @Override
    public Model getModel() {
        return model;
    }

    @Override
    public void reread() throws ServiceClientException {
    }

    @Override
    public boolean hasUI() {
        return true;
    }

    @Override
    public boolean isDisabled() {
        return false;
    }

    @Override
    public IClientEnvironment getEnvironment() {
        return env;
    }

    @Override
    public IWidget getParentWindow() {
        return viewSupport.getParentWindow();
    }

    @Override
    public void visitChildren(IView.Visitor visitor, boolean recursively) {
        //no embedded views in standard paragraph
    }

    @Override
    public ViewRestrictions getRestrictions() {
        return restrictions;
    }        

    @Override
    public boolean canSafelyClose(final CleanModelController cleanController) {
        return true;
    }    

    @Override
    public IView findParentView() {
        return viewSupport.findParentView();
    }

    @Override
    protected String[] clientScriptsRequired() {
        String[] ss = super.clientScriptsRequired();
        String[] newArr;
        int idx;
        if (ss != null && ss.length > 0) {
            newArr = new String[ss.length + 1];
            System.arraycopy(ss, 0, newArr, 0, ss.length);
            idx = ss.length;
        } else {
            newArr = new String[1];
            idx = 0;
        }
        newArr[idx] = "org/radixware/wps/views/paragraph/paragEditor.js";
        return newArr;
    }
}
