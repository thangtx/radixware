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

package org.radixware.kernel.explorer.dialogs.settings;

import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.core.Qt.Alignment;
import com.trolltech.qt.gui.QSizePolicy.Policy;
import com.trolltech.qt.gui.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.explorer.editors.valeditors.ValStrEditor;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerSettings;
import org.radixware.kernel.explorer.utils.EQtStyle;


public class AppearanceStyleSettingWidget extends SettingsWidget {
    private QWidget tab = null;
    private final IClientEnvironment environment;
    private final QButtonGroup radios = new QButtonGroup(this);
    private final List<QWidget> testWidgets = new LinkedList<>();
    private QStyle currentStyle = null;
    
    public AppearanceStyleSettingWidget(final IClientEnvironment environment, final QWidget parent) {
        super(environment, parent, SettingNames.APP_STYLE, null, SettingNames.STYLENAME);
        this.environment = environment;
        this.setLayout(createStylesPreview());
        currentStyle = QStyleFactory.create(Application.getDefaultStyleName());
    }
    
    private QLayout createStylesPreview() {
        final QLayout ltPreview = new QHBoxLayout(this);
        ltPreview.setMargin(0);
        final QLayout ltRadios = new QVBoxLayout();
        ltRadios.setAlignment(new Alignment(Qt.AlignmentFlag.AlignTop));
        
        final String byDefaultSign = Application.translate("ExplorerSettings", "(default)");        
        final String defaultStyleName = Application.getDefaultStyleName();
        final String currentStyleName = Application.getCurrentStyleName();
        environment.getTracer().debug("Current application style is "+currentStyleName);
        for(EQtStyle style: EQtStyle.getSupported()) {
            final QRadioButton r = new QRadioButton(style.getTitle(), this);
            r.clicked.connect(this, "onRadioClick()");
            
            if(style.getTitle().equalsIgnoreCase(currentStyleName)) {
                r.setChecked(true);
            }
            
            if(style.getTitle().equalsIgnoreCase(defaultStyleName)) {
                r.setText(r.text() + " " + byDefaultSign);
            }
            radios.addButton(r);
            ltRadios.addWidget(r);
        }
        
        radios.setExclusive(true);
        
        final String availableStyles = Application.translate("ExplorerSettings", "Available styles");
        final QGroupBox selectStylesGroup = new QGroupBox(availableStyles, this);
        selectStylesGroup.setLayout(ltRadios);
        
        final String previewTitle = Application.translate("ExplorerSettings", "Preview");
        final String tabSign = Application.translate("ExplorerSettings", "Tab");
        final QGroupBox previewBox = new QGroupBox(previewTitle, this);
        previewBox.setLayout(new QVBoxLayout());
        tab = new QTabWidget(this);
        final QTabWidget tabWidget = (QTabWidget)tab;
        QWidget tabContent = createTabContent(tab);
        tabWidget.addTab(tabContent, tabSign + " 1");
        testWidgets.add(tabContent);
        
        tabContent = new QWidget(tab);
        tabWidget.addTab(tabContent, tabSign + " 2");
        testWidgets.add(tabContent);
        previewBox.layout().addWidget(tab);
        
        ltPreview.addWidget(selectStylesGroup);
        ltPreview.addWidget(previewBox);
        
        return ltPreview;
    }

    private QWidget createTabContent(final QWidget parent) {
        final QWidget tabContent = new QWidget(parent);
        final QLayout tabLayout = new QVBoxLayout();
        tabLayout.setAlignment(new Alignment(Qt.AlignmentFlag.AlignTop));
        String ctrlText = Application.translate("ExplorerSettings", "Button");
        QWidget widget = new QPushButton(ctrlText, tabContent);
        testWidgets.add(widget);
        tabLayout.addWidget(widget);
        
        ctrlText = Application.translate("ExplorerSettings", "Radio button");
        widget = new QRadioButton(ctrlText, tabContent);
        testWidgets.add(widget);
        tabLayout.addWidget(widget);
        
        ctrlText = Application.translate("ExplorerSettings", "Label");
        widget = new QLabel(ctrlText, tabContent);
        testWidgets.add(widget);
        tabLayout.addWidget(widget);
        
        ctrlText = Application.translate("ExplorerSettings", "Text box");
        widget = new ValStrEditor(environment, tabContent);
        ((ValStrEditor)widget).setValue(ctrlText);
        testWidgets.add(widget);
        tabLayout.addWidget(widget);
        
//        ctrlText = Application.translate("ExplorerSettings", "Tree");
        widget = createTreeWidget();
        testWidgets.add(widget);
        tabLayout.addWidget(widget);
        
        tabContent.setLayout(tabLayout);
        return tabContent;
    }
    
    private QWidget createTreeWidget() {
        final String treeSign = Application.translate("ExplorerSettings", "Tree");
        final QTreeWidget treeWidget = new QTreeWidget();
        treeWidget.setColumnCount(1);
        treeWidget.setSizePolicy(Policy.Expanding, Policy.Fixed);
        treeWidget.headerItem().setText(0, treeSign);
        final List<QTreeWidgetItem> items = new ArrayList<>();
        for (int i = 0; i < 3; ++i) {
            final List<String> lst = new ArrayList<>();
            lst.add("item: " + (i+1));
            items.add(new QTreeWidgetItem((QTreeWidgetItem) null, lst));
        }
        treeWidget.insertTopLevelItems(0, items);
        final List<String> childs = new ArrayList<>();
        childs.add("1");
        childs.add("2");
        final QTreeWidgetItem leaf = treeWidget.topLevelItem(0);
        leaf.addChild(new QTreeWidgetItem(childs));
        leaf.setExpanded(true);
        return treeWidget;
    }
    
    @SuppressWarnings("unused")
    private void onRadioClick() {
        final String byDefaultSign = Application.translate("ExplorerSettings", "(default)");
        final QAbstractButton checkedButton = radios.checkedButton();
        if(checkedButton != null) {
            String styleName = checkedButton.text();
            if(styleName.contains(byDefaultSign)) {
                styleName = styleName.substring(0, styleName.indexOf(byDefaultSign) - 1);
            }
            currentStyle = QStyleFactory.create(styleName);
            final List<QObject> wdgts = tab.children();
            for(QObject o : wdgts) {
                ((QWidget)o).setStyle(currentStyle);
            }
            tab.setStyle(currentStyle);
            for(QWidget w : testWidgets) {
                w.setStyle(currentStyle);
            }
        }
    }
    
    public QStyle getStyle() {
        return currentStyle;
    }

    @Override
    public void readSettings(final ExplorerSettings src) {
        currentStyle = QStyleFactory.create(src.readString(getSettingCfgName(),Application.getDefaultStyleName()));
        onRadioClick();
    }

    @Override
    public void writeSettings(final ExplorerSettings dst) {
        
        if(currentStyle != null) {
            dst.writeString(getSettingCfgName(), currentStyle.objectName());
        }
    }

    @Override
    public void restoreDefaults() {
        currentStyle = QStyleFactory.create(Application.getDefaultStyleName());
        onRadioClick();
    }
}
