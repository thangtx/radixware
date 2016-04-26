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

package org.radixware.kernel.designer.common.editors.jml.actions;

import java.util.Collections;
import java.util.List;
import javax.swing.Icon;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.module.AdsImageDef;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.common.jml.JmlTagId;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.scml.Scml.Tag;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlEditor;
import org.radixware.kernel.designer.common.dialogs.scmlnb.tags.ScmlInsertTagAction;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.editors.module.images.ChooseImagesDialog;


public class JmlPopupActions {

    public static final String INSERT_ADS_ICON_ID_TAG_ACTION = "insert-ads-icon-id-tag-action";

    public static abstract class JmlInsertTagAction extends ScmlInsertTagAction {

        public JmlInsertTagAction(String name, ScmlEditor<Jml> editor) {
            super(name, editor);
        }

        @Override
        public String getAcceleratorKey() {
            return null;
        }

        @Override
        protected Class getClassForBundle() {
            return JmlPopupActions.class;
        }

        @Override
        public boolean isAvailable(Scml scml) {
            return isJml(scml);
        }
    }

    public static class InsertAdsIconIdTagAction extends JmlInsertTagAction {

        public InsertAdsIconIdTagAction(final ScmlEditor<Jml> editor) {
            super(INSERT_ADS_ICON_ID_TAG_ACTION, editor);
        }

        @Override
        public Icon getIcon() {
            return AdsDefinitionIcon.CUSTOM_SIGNAL.getIcon();
        }

        @Override
        public int getGroupType() {
            return 0;
        }

        @Override
        public int getPosition() {
            return 0;
        }

        @Override
        protected List<Tag> createTags() {
            if (getEditor().getSource() instanceof Jml) {
                final Jml jml = (Jml) getEditor().getSource();
                if (jml.getModule() instanceof AdsModule) {
                    final ChooseImagesDialog dialog = ChooseImagesDialog.getInstanceFor((AdsModule) jml.getModule());
                    if (dialog.chooseImage()) {
                        final AdsImageDef imageDef = dialog.getSelectedAdsImageDef();
                        if (imageDef == null) {
                            throw new IllegalStateException("Dialog returned null image");
                        } else {
                            final Module jmlModule = jml.getModule();
                            final Module imageModule = imageDef.getModule();
                            if(jmlModule != null && imageModule != null) {
                                jmlModule.getDependences().add(imageModule);
                            }
                            else {
                                final String message = "Can not add dependency on image module. This may cause tag to become unresolved. Press Ignore to continue or cancel to interrupt operation.";
                                if(!DialogUtils.messageErrorWithIgnore(message)) {
                                    return null;
                                }
                            }
                            final Scml.Tag tag = new JmlTagId(imageDef);
                            return Collections.singletonList(tag);
                        }
                    }
                }
            }
            return null;
        }
    }
}
