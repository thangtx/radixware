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

package org.radixware.kernel.explorer.editors.sqmleditor.sqmltags;

import java.util.ArrayList;
import java.util.List;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EDefinitionDisplayMode;
import org.radixware.kernel.common.client.env.ITaskWaiter;
import org.radixware.kernel.common.client.meta.sqml.ISqmlEventCodeDef;
import org.radixware.kernel.common.client.meta.sqml.impl.SqmlEventCodeImpl;
import org.radixware.kernel.common.client.views.IDialog;
import org.radixware.kernel.common.meta.RadMlStringBundleDef;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.editors.sqmleditor.tageditors.EventCodeChoiceDialog;
import org.radixware.kernel.explorer.editors.xscmleditor.TagInfo;
import org.radixware.kernel.explorer.editors.xscmleditor.XscmlEditor;
import org.radixware.schemas.xscml.Sqml;


public final class SqmlTag_EventCode extends SqmlTag {
    private static final String path = "org.radixware.explorer/S_E/SYNTAX_SQML/SQML_EVENT_CODE";
    private ISqmlEventCodeDef definition;
    private Sqml.Item.EventCode eventCode;
    
    private SqmlTag_EventCode(final IClientEnvironment env, final SqmlTag_EventCode source) {
        super(env, source);
        definition = source.definition;
        setEventCodeXml();
    }
    
    public SqmlTag_EventCode(final IClientEnvironment env, final long pos, final ISqmlEventCodeDef eventCodeDef, EDefinitionDisplayMode displayMode) {
        super(env, pos,eventCodeDef==null?false :eventCodeDef.isDeprecated());
        this.definition = eventCodeDef;
        setEventCodeXml();
        setDisplayedInfo(displayMode);
    }
    
    public SqmlTag_EventCode(final IClientEnvironment env, final long pos, final Sqml.Item.EventCode eventCode, final EDefinitionDisplayMode displayMode) {
        super(env,pos);
        final Id mlStringId = eventCode.getStringId();
        final Id bundleId = eventCode.getOwnerId();
        final RadMlStringBundleDef bundleDef = env.getDefManager().getMlStringBundleDef(bundleId);
        
        this.definition = new SqmlEventCodeImpl(env, bundleDef, mlStringId);
        setIsDeprecated(definition.isDeprecated());
        setEventCodeXml();
        setDisplayedInfo(displayMode);
    }
    
    @Override
    public void addTagToSqml(XmlObject itemTag) {
        Sqml.Item tag = (Sqml.Item) itemTag;
        tag.setEventCode(eventCode);
    }

    @Override
    public boolean showEditDialog(XscmlEditor editText, EDefinitionDisplayMode showMode) {
        if(editText.isReadOnly()) {
            return false;
        } else {
            final ITaskWaiter taskWaiter = environment.getApplication().newTaskWaiter();
            final List<ISqmlEventCodeDef> eventCodes = new ArrayList<>();
            try {
                taskWaiter.runAndWait(new Runnable() {
                    @Override
                    public void run() {
                        eventCodes.addAll(environment.getSqmlDefinitions().getEventCodes());
                    }
                });
            } catch (InterruptedException ex) {
                // leave the event codes list empty if error
            }finally{
                taskWaiter.close();
            }
            final EventCodeChoiceDialog dialog = new EventCodeChoiceDialog(environment, editText, eventCodes);
            if(dialog.execDialog() == IDialog.DialogResult.ACCEPTED) {
                definition = (ISqmlEventCodeDef) dialog.getSelectedEventCode();
                setEventCodeXml();
                setDisplayedInfo(showMode);
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    protected String getSettingsPath() {
        return path;
    }

    @Override
    public boolean setDisplayedInfo(final EDefinitionDisplayMode showMode) {
        if(isValid()) {
            final String tooltip = environment.getMessageProvider().translate("SqmlTag_EventCode", "Event code");
            final StringBuilder sb = new StringBuilder();
            sb.append("eventCode[");
            sb.append(definition.getDisplayableText(showMode));
            sb.append(']');
            setDisplayedInfo("<b>" + tooltip + "</b>", sb.toString());
            return true;
        } else {
            return false;
        }
        
    }

    private void setEventCodeXml() {
        eventCode = Sqml.Item.EventCode.Factory.newInstance();
        eventCode.setStringId(definition.getId());
        eventCode.setOwnerId(definition.getOwner());
    }

    @Override
    public TagInfo copy() {
        return new SqmlTag_EventCode(environment, this);
    }
    
    
}
