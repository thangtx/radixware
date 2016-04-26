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

package org.radixware.kernel.designer.common.general.displaying;

import java.awt.Color;
import java.util.Collections;
import java.util.List;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileSystem;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IDirectoryRadixObject;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObject.EditStateChangeListener;
import org.radixware.kernel.common.defs.RadixObject.EditStateChangedEvent;
import org.radixware.kernel.common.defs.RadixObject.RenameEvent;
import org.radixware.kernel.common.defs.RadixObject.RenameListener;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.type.AdsAccessFlags;
import org.radixware.kernel.common.enums.ENamingPolicy;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.common.utils.XmlColor;
import org.radixware.kernel.common.utils.events.IRadixEventListener;
import org.radixware.kernel.common.utils.events.RadixEvent;
import org.radixware.kernel.common.utils.events.RadixEventSource;
import org.radixware.kernel.designer.common.general.filesystem.RadixFileUtil;

/**
 * Support to listen definition HTML display name changed event.
 *
 */
public class HtmlNameSupport extends RadixEventSource<IRadixEventListener<RadixEvent>, RadixEvent> {

    private final RadixObject radixObject;
    private final RenameListener renameListener = new RadixObject.RenameListener() {

        @Override
        public void onEvent(RenameEvent e) {
            HtmlNameSupport.this.fireChanged();
        }
    };
    private final EditStateChangeListener editStateChangesListener = new RadixObject.EditStateChangeListener() {

        @Override
        public void onEvent(EditStateChangedEvent e) {
            HtmlNameSupport.this.fireChanged();
        }
    };

    private static class NameState implements AutomaticTreeUpdater.State {

        private final RadixObject object;
        private String oldName;

        public NameState(RadixObject object) {
            this.object = object;
            this.oldName = object.getName();
        }

        @Override
        public boolean wasChanged() {
            final String newName = object.getName();
            return !Utils.equals(oldName, newName);
        }

        @Override
        public void update() {
            final String newName = object.getName();
            object.setName(newName);
            oldName = newName;
        }

        @Override
        public RadixObject getRadixObject() {
            return object;
        }
    }

    protected HtmlNameSupport(RadixObject radixObject) {
        super();
        this.radixObject = radixObject;

        if (radixObject.getNamingPolicy() == ENamingPolicy.CALC) {
            AutomaticTreeUpdater.getDefault().register(new NameState(radixObject));
        }

        radixObject.addRenameListener(renameListener);
        radixObject.addEditStateListener(editStateChangesListener);
    }

    protected void fireChanged() {
        this.fireEvent(new RadixEvent());
    }

    private class HtmlNameStyler implements IHtmlStyled {

        @Override
        public Color getColor() {
            return HtmlNameSupport.this.getColor();
        }

        @Override
        public String getDisplayName() {
            return HtmlNameSupport.this.getDisplayName();
        }

        @Override
        public boolean isBold() {
            return HtmlNameSupport.this.isBold();
        }

        @Override
        public boolean isItalic() {
            return HtmlNameSupport.this.isItalic();
        }

        @Override
        public boolean isStrikeOut() {
            return HtmlNameSupport.this.isStrikeOut();
        }
    }
    private final HtmlNameStyler styler = new HtmlNameStyler();

    public final String getHtmlName() {
        HtmlNameProcessor processor = HtmlNameProcessor.getDefault();
        return processor.calcHtmlDisplayName(styler);
    }

    private class TreeHtmlNameStyler implements IHtmlStyled {

        @Override
        public Color getColor() {
            return HtmlNameSupport.this.getEditorColor();
        }

        @Override
        public String getDisplayName() {
            return HtmlNameSupport.this.getTreeDisplayName();
        }

        @Override
        public boolean isBold() {
            return HtmlNameSupport.this.isBold();
        }

        @Override
        public boolean isItalic() {
            return HtmlNameSupport.this.isItalic();
        }

        @Override
        public boolean isStrikeOut() {
            return HtmlNameSupport.this.isStrikeOut();
        }
    }
    private final TreeHtmlNameStyler treeStyler = new TreeHtmlNameStyler();

    public final String getTreeHtmlName() {
        HtmlNameProcessor processor = HtmlNameProcessor.getDefault();
        return processor.calcHtmlDisplayName(treeStyler);
    }

    private class EditorHtmlNameStyler implements IHtmlStyled {

        @Override
        public Color getColor() {
            return HtmlNameSupport.this.getEditorColor();
        }

        @Override
        public String getDisplayName() {
            return HtmlNameSupport.this.getEditorDisplayName();
        }

        @Override
        public boolean isBold() {
            return HtmlNameSupport.this.isEditorBold();
        }

        @Override
        public boolean isItalic() {
            return HtmlNameSupport.this.isEditorItalic();
        }

        @Override
        public boolean isStrikeOut() {
            return HtmlNameSupport.this.isStrikeOut();
        }
    }
    private final EditorHtmlNameStyler editorStyler = new EditorHtmlNameStyler();

    public final String getEditorHtmlName() {
        final HtmlNameProcessor processor = HtmlNameProcessor.getDefault();
        final String result = "<html>" + processor.calcHtmlDisplayName(editorStyler);
        return result;
    }

    private final Color extractColorFromHtmlName(final String htmlName) {
        if (htmlName != null) {
            int pos = htmlName.indexOf("font color=\"");
            if (pos > 0) {
                int pos1 = htmlName.indexOf('\"', pos + 1);
                int pos2 = htmlName.indexOf('\"', pos1 + 1);
                if (pos1 > 0 && pos2 > 0) {
                    String colorAsStr = htmlName.substring(pos1 + 1, pos2);
                    if (colorAsStr.length() == 6) {
                        colorAsStr = "#" + colorAsStr; // RADIX-3842
                    }
                    return XmlColor.parseColor(colorAsStr);
                }
            }
        }
        return null;
    }

    public Color getColor() {
        if (radixObject.isSaveable() && !(radixObject instanceof IDirectoryRadixObject)) {
            final List<FileObject> fileObjects = RadixFileUtil.getVersioningFileObjects(radixObject);
            if (fileObjects != null && !fileObjects.isEmpty()) {
                final FileSystem.Status fileSystemStatus = RadixFileUtil.getFileSystem().getStatus();
                if (fileSystemStatus instanceof FileSystem.HtmlStatus) {
                    final FileSystem.HtmlStatus fileSystemHtmlStatus = (FileSystem.HtmlStatus) fileSystemStatus;
                    for (FileObject fo : fileObjects) {
                        final String fileHtmlName = fileSystemHtmlStatus.annotateNameHtml("stub", Collections.singleton(fo)); // netbeans unable to annotabe several files at once
                        final Color fileColor = extractColorFromHtmlName(fileHtmlName);
                        if (fileColor != null) {
                            return fileColor;
                        }
                    }
                }
            }
        }
        return radixObject.getEditState().getColor();
    }
    private Color BACK_HUCK_COLOR = new Color(1, 0, 0);

    public Color getEditorColor() {
        final Color color = getColor();
        if (Utils.equals(Color.BLACK, color)) {
            return BACK_HUCK_COLOR; // RADIX-3930 :-(
        } else {
            return color;
        }
    }

    public String getTreeDisplayName() {
        return getDisplayName();
    }

    public String getDisplayName() {
        return radixObject.getName();
    }

    public String getEditorDisplayName() {
        final Module module = radixObject.getModule();
        if (module != null) {
            return radixObject.getQualifiedName(module);
        } else {
            return radixObject.getName();
        }
    }

    public boolean isBold() {
        return false;
    }

    public boolean isEditorBold() {
        return (radixObject.getEditState() != RadixObject.EEditState.NONE);
    }

    public boolean isItalic() {
        return false;
    }

    public boolean isEditorItalic() {
        return isItalic() || radixObject.isReadOnly();
    }

    public RadixObject getRadixObject() {
        return radixObject;
    }

    public boolean isStrikeOut() {
        if (radixObject instanceof Definition) {
            return ((Definition) radixObject).isDeprecated();
        } else {
            return false;
        }
    }
}
