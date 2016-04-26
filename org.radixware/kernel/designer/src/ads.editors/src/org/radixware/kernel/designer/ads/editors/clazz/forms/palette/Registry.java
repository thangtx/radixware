/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.designer.ads.editors.clazz.forms.palette;

import static org.radixware.kernel.designer.ads.editors.clazz.forms.palette.Item.register;
import static org.radixware.kernel.designer.ads.editors.clazz.forms.palette.Item.registerWeb;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.ActionItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.AdvancedSplitterItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.CheckBoxItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.ComboBoxItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.CommandPushButtonItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.CommandToolButtonItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.DateEditItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.DateTimeEditItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.DialogButtonBoxItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.DoubleSpinBoxItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.EditorPageItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.EmbeddedEditorItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.EmbeddedSelectorItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.FrameItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.GridLayoutItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.GroupBoxItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.HorizontalLayoutItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.HorizontalSpacerItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.LabelItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.LineEditItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.ListItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.ListViewItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.ProgressBarItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.PropEditorItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.PropLabelItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.PushButtonItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.RadioButtonItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.ScrollAreaItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.SpinBoxItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.SplitterItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.StackedWidgetItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.TabItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.TableItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.TableViewItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.TextEditItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.TimeEditItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.ToolBarItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.ToolButtonItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.TreeItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.TreeViewItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.ValBinEditorItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.ValBoolEditorItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.ValCharEditorItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.ValConstSetEditorItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.ValDataTimeEditorItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.ValFilePathEditorItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.ValIntEditorItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.ValListEditorItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.ValNumEditorItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.ValRefEditorItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.ValStrEditorItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.ValTimeIntervalEditorItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.VerticalLayoutItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.VerticalSpacerItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.web.RwtAbstractContainerItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.web.RwtActionItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.web.RwtButtonBoxItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.web.RwtCheckBoxItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.web.RwtCommandPushButtonItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.web.RwtContainerItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.web.RwtEditorPageItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.web.RwtEmbeddedEditorItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.web.RwtEmbeddedSelectorItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.web.RwtFileInputItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.web.RwtGridBoxContainerItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.web.RwtGridItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.web.RwtGroupBoxItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.web.RwtHBoxContainerItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.web.RwtInputBoxItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.web.RwtLabelItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.web.RwtLabeledEditGridItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.web.RwtListItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.web.RwtPropEditorItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.web.RwtPropLabelItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.web.RwtPropertiesGridItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.web.RwtPushButtonItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.web.RwtRadioButtonItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.web.RwtSplitterItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.web.RwtTabItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.web.RwtTextAreaItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.web.RwtTextFieldItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.web.RwtToolBarItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.web.RwtTreeItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.web.RwtVBoxContainerItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.web.RwtValEditorItem;

/**
 *
 * @author akrylov
 */
public class Registry {

    public static void initialize() {
        // GROUP_RADIX_WIDGETS

        register(PropLabelItem.DEFAULT);
        register(PropEditorItem.DEFAULT);
        register(CommandPushButtonItem.DEFAULT);
        register(EmbeddedEditorItem.DEFAULT);
        register(EmbeddedSelectorItem.DEFAULT);
        register(EditorPageItem.DEFAULT);

        // GROUP_LAYOUTS
        register(HorizontalLayoutItem.DEFAULT);
        register(VerticalLayoutItem.DEFAULT);
        register(GridLayoutItem.DEFAULT);
//
//        // GROUP_SPACERS
        register(HorizontalSpacerItem.DEFAULT);
        register(VerticalSpacerItem.DEFAULT);
        // GROUP_BUTTONS
        register(PushButtonItem.DEFAULT);
        register(ToolButtonItem.DEFAULT);
        register(RadioButtonItem.DEFAULT);
        register(CheckBoxItem.DEFAULT);
        register(DialogButtonBoxItem.DEFAULT);
        register(CommandToolButtonItem.DEFAULT);
        register(ActionItem.DEFAULT);

        // GROUP_CONTAINERS
        register(GroupBoxItem.DEFAULT);
        register(TabItem.DEFAULT);
        register(SplitterItem.DEFAULT);
        register(AdvancedSplitterItem.DEFAULT);
        register(FrameItem.DEFAULT);
        register(StackedWidgetItem.DEFAULT);
        register(ScrollAreaItem.DEFAULT);
        register(ToolBarItem.DEFAULT);

        // GROUP_DISPLAY_WIDGETS
        register(LabelItem.DEFAULT);
        register(ProgressBarItem.DEFAULT);
//
//        // GROUP_INPUT_WIDGETS
        register(ComboBoxItem.DEFAULT);
        register(LineEditItem.DEFAULT);
        register(TextEditItem.DEFAULT);
        register(SpinBoxItem.DEFAULT);
        register(DoubleSpinBoxItem.DEFAULT);
        register(TimeEditItem.DEFAULT);
        register(DateEditItem.DEFAULT);
        register(DateTimeEditItem.DEFAULT);
//
        register(ValBinEditorItem.DEFAULT);
        register(ValBoolEditorItem.DEFAULT);
        register(ValCharEditorItem.DEFAULT);
        register(ValStrEditorItem.DEFAULT);
        register(ValIntEditorItem.DEFAULT);
        register(ValNumEditorItem.DEFAULT);
        register(ValConstSetEditorItem.DEFAULT);
        register(ValListEditorItem.DEFAULT);
        register(ValTimeIntervalEditorItem.DEFAULT);
        register(ValDataTimeEditorItem.DEFAULT);
        register(ValFilePathEditorItem.DEFAULT);
        register(ValRefEditorItem.DEFAULT);

        // GROUP_ITEMS_WIDGETS
        register(ListItem.DEFAULT);
        register(TreeItem.DEFAULT);
        register(TableItem.DEFAULT);
        register(ListViewItem.DEFAULT);
        register(TreeViewItem.DEFAULT);
        register(TableViewItem.DEFAULT);

        registerWeb(RwtPushButtonItem.DEFAULT);
        registerWeb(RwtTextFieldItem.DEFAULT);
        registerWeb(RwtTextAreaItem.DEFAULT);
        registerWeb(RwtTreeItem.DEFAULT);
        registerWeb(RwtInputBoxItem.DEFAULT);
        registerWeb(RwtLabelItem.DEFAULT);
        registerWeb(RwtEditorPageItem.DEFAULT);
        //     registerWeb(RwtGridPanelItem.DEFAULT);
        registerWeb(RwtGroupBoxItem.DEFAULT);
        registerWeb(RwtGridBoxContainerItem.DEFAULT);
        registerWeb(RwtContainerItem.DEFAULT);
        registerWeb(RwtActionItem.DEFAULT);
        registerWeb(RwtToolBarItem.DEFAULT);
        registerWeb(RwtTabItem.DEFAULT);
        registerWeb(RwtEmbeddedEditorItem.DEFAULT);
        registerWeb(RwtEmbeddedSelectorItem.DEFAULT);
        registerWeb(RwtSplitterItem.DEFAULT);
        registerWeb(RwtListItem.DEFAULT);
        registerWeb(RwtGridItem.DEFAULT);
        registerWeb(RwtFileInputItem.DEFAULT);
        registerWeb(RwtLabeledEditGridItem.DEFAULT);
        registerWeb(RwtPropertiesGridItem.DEFAULT);
        registerWeb(RwtPropEditorItem.DEFAULT);
        registerWeb(RwtAbstractContainerItem.DEFAULT);
        registerWeb(RwtRadioButtonItem.DEFAULT);
        registerWeb(RwtCheckBoxItem.DEFAULT);
        registerWeb(RwtHBoxContainerItem.DEFAULT);
        registerWeb(RwtVBoxContainerItem.DEFAULT);
        registerWeb(RwtCommandPushButtonItem.DEFAULT);
        registerWeb(RwtPropLabelItem.DEFAULT);
        //val editors
        registerWeb(RwtValEditorItem.STR_EDITOR);
        registerWeb(RwtValEditorItem.BOOL_EDITOR);
        registerWeb(RwtValEditorItem.BIN_EDITOR);
        registerWeb(RwtValEditorItem.ENUM_EDITOR);
        registerWeb(RwtValEditorItem.INT_EDITOR);
        registerWeb(RwtValEditorItem.LIST_EDITOR);
        registerWeb(RwtValEditorItem.NUM_EDITOR);
        registerWeb(RwtValEditorItem.REF_EDITOR);
        registerWeb(RwtValEditorItem.TIME_EDITOR);
        registerWeb(RwtValEditorItem.TIME_INTERVAL_EDITOR);
        registerWeb(RwtValEditorItem.DATE_TIME_EDITOR);
        registerWeb(RwtValEditorItem.FILE_PATH_EDITOR);
        registerWeb(RwtValEditorItem.ARR_EDITOR);
        registerWeb(RwtButtonBoxItem.DEFAULT);

    }
}
