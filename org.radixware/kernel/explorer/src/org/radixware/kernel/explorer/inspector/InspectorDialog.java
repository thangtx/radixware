/*
 * Copyright (coffee) 2008-2016, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.explorer.inspector;

import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QCloseEvent;
import com.trolltech.qt.gui.QFrame;
import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QLayout;
import com.trolltech.qt.gui.QMainWindow;
import com.trolltech.qt.gui.QSplitter;
import com.trolltech.qt.gui.QStackedWidget;
import com.trolltech.qt.gui.QToolBar;
import com.trolltech.qt.gui.QTreeWidgetItem;
import com.trolltech.qt.gui.QWidget;
import java.io.StringWriter;
import java.io.Writer;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.explorer.editors.xml.new_.XmlEditor;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.webdriver.elements.finders.xmlwrapper.DocumentWrapper;

/**
 *
 * @author szotov
 */
public class InspectorDialog<T> extends QMainWindow {

    private final static ClientIcon COPY_XPATH_ICON = new ClientIcon("classpath:images/copy_xpath.svg", true) {
    };

    private final QStackedWidget qStackedWidget;
    private final PropertyBrowser<T> propBrowser;
    private final QFrame qLabelDeletedFrame;
    private final WidgetInspector<T> qWidgetInspector;
    private final TreeWdgt<T> treeWdgt;
    private final IClientEnvironment environment;
    private final QAction copyXPathAction;
    private final QAction showXml;
    private final QIcon qShowXmlIcon = ExplorerIcon.getQIcon(ExplorerIcon.ValueTypes.XML);
    public final com.trolltech.qt.QSignalEmitter.Signal0 closed = new com.trolltech.qt.QSignalEmitter.Signal0();
    private boolean isClosed;

    public InspectorDialog(WidgetInfo<T> wdgtInfo, IClientEnvironment environment, QWidget parent) {
        super(parent);
        isClosed = false;
        this.resize(1024, 768);
        this.setWindowTitle(environment.getMessageProvider().translate("inspector", "Inspector dialog"));
        this.environment = environment;

        this.qWidgetInspector = wdgtInfo.getWidgetInspector();

        QIcon qRefreshIcon = ExplorerIcon.getQIcon(ExplorerIcon.Editor.REFRESH);
        QIcon qNewSelectorIcon = ExplorerIcon.getQIcon(ExplorerIcon.Dialog.ABOUT);
        QIcon qCopyXPathIcon = ExplorerIcon.getQIcon(COPY_XPATH_ICON);

        QToolBar qtb = new QToolBar(this);
        this.addToolBar(qtb);
        qtb.addAction(qRefreshIcon, environment.getMessageProvider().translate("inspector", "Refresh"), this, "refreshSlot()");
        qtb.addAction(qNewSelectorIcon, environment.getMessageProvider().translate("inspector", "Select"), this, "selectNewWidgetSlot()");
        copyXPathAction
                = qtb.addAction(qCopyXPathIcon, environment.getMessageProvider().translate("inspector", "Copy XPath to Clipboard"), this, "copyXPathSlot()");

        showXml = qtb.addAction(qShowXmlIcon, environment.getMessageProvider().translate("inspector", "Show xml"), this, "showXmlSlot()");

        QWidget topWidget = new QWidget(this);
        this.setCentralWidget(topWidget);

        QSplitter sp = new QSplitter(Qt.Orientation.Horizontal, topWidget);
        sp.setVisible(true);
        qStackedWidget = new QStackedWidget(sp);
        qLabelDeletedFrame = new QFrame(qStackedWidget);
        QGridLayout qLabelDeletedFrameLayout = new QGridLayout(qLabelDeletedFrame);
        qLabelDeletedFrame.setFrameShape(QFrame.Shape.Box);
        qLabelDeletedFrame.setLayout(qLabelDeletedFrameLayout);
        QLabel qLabelDeleted = new QLabel(environment.getMessageProvider().translate("inspector", "Widget has been deleted!"), qLabelDeletedFrame);
        qLabelDeletedFrameLayout.addWidget(qLabelDeleted);
        qLabelDeleted.setAlignment(Qt.AlignmentFlag.AlignCenter);

        closed.connect(this, "inspectorClosedSlot()");

        treeWdgt = new TreeWdgt<>(sp, environment);
        treeWdgt.setHorizontalScrollBarPolicy(Qt.ScrollBarPolicy.ScrollBarAsNeeded);
        WidgetInfo<T> currentItemWidgetInfo = treeWdgt.build(wdgtInfo);
        propBrowser = new PropertyBrowser<>(qStackedWidget, currentItemWidgetInfo, environment);
        propBrowser.build(null, environment);
        treeWdgt.currentItemChanged.connect(this, "currentItemChangedSlot(QTreeWidgetItem, QTreeWidgetItem)");
        qStackedWidget.addWidget(propBrowser);
        qStackedWidget.addWidget(qLabelDeletedFrame);
        sp.addWidget(treeWdgt);
        sp.addWidget(qStackedWidget);
        QLayout layout = new QHBoxLayout(topWidget);
        layout.addWidget(sp);
        treeWdgt.scrollToItem(treeWdgt.currentItem());
        QRect r = treeWdgt.visualItemRect(treeWdgt.currentItem());
        if (treeWdgt.horizontalScrollBar().isEnabled()) {
            treeWdgt.horizontalScrollBar().setValue(r.x());
        }
        setAttribute(Qt.WidgetAttribute.WA_DeleteOnClose, true);
    }

    @SuppressWarnings("unused")
    private void refreshSlot() {
        if (treeWdgt.isDeleted(treeWdgt.currentItem())) {
            qStackedWidget.setCurrentWidget(qLabelDeletedFrame);
        } else {
            propBrowser.build(treeWdgt.getCurrentWidgetInfo(), environment);
        }
        treeWdgt.colorTree();
    }

    @SuppressWarnings("unused")
    private void currentItemChangedSlot(QTreeWidgetItem currItem, QTreeWidgetItem prevItem) {
        if (currItem != null) {
            WidgetInfo<T> widgetInfo = treeWdgt.getCurrentWidgetInfo();
            if (treeWdgt.isDeleted(currItem)) {
                copyXPathAction.setEnabled(false);
                showXml.setEnabled(false);
                qStackedWidget.setCurrentWidget(qLabelDeletedFrame);
            } else {
                copyXPathAction.setEnabled(true);
                showXml.setEnabled(true);
                propBrowser.build(widgetInfo, environment);
                qStackedWidget.setCurrentWidget(propBrowser);
            }
        }
    }

    @SuppressWarnings("unused")
    private void selectNewWidgetSlot() {
        WidgetInfo<T> wdgetInfo = qWidgetInspector.selectWidget(this);
        if (wdgetInfo != null && wdgetInfo.getWidget() != null) {
            treeWdgt.clear();
            WidgetInfo<T> currWidgetInfo = treeWdgt.build(wdgetInfo);
            propBrowser.build(currWidgetInfo, environment);
        }
    }

    @SuppressWarnings("unused")
    private void copyXPathSlot() {
        final WidgetInfo<T> currentWidgetInfo = treeWdgt.getCurrentWidgetInfo();
        final T currentWidget = currentWidgetInfo.getWidget();
        if (currentWidget != null) {
            final String xpath = qWidgetInspector.getWidgetXPath(currentWidget);
            if (xpath != null && !xpath.isEmpty()) {
                QApplication.clipboard().setText("/Root" + xpath);
            }
        }
    }

    @SuppressWarnings("unused")
    private void showXmlSlot() {
        final WidgetInfo<T> currentWidgetInfo = treeWdgt.getCurrentWidgetInfo();
        final T currentWidget = currentWidgetInfo.getWidget();

        if (currentWidget != null) {
            try {
                final Transformer transformer = TransformerFactory.newInstance().newTransformer();
                final Writer writer = new StringWriter();
                final Result output = new StreamResult(writer);
                final Source input = new DOMSource(new DocumentWrapper((QWidget) currentWidget));
                transformer.transform(input, output);
                final XmlEditor editor = new XmlEditor(environment, null);
                editor.setValue(XmlObject.Factory.parse(writer.toString()));
                editor.setReadOnly(true);
                editor.setWindowIcon(qShowXmlIcon);
                editor.setWindowTitle("Xml editor");
                editor.show();
            } catch (InstantiationException | IllegalAccessException | XmlException | TransformerException | TransformerFactoryConfigurationError ex) {
                environment.processException(ex);
            }
        }
    }

    @Override
    protected void closeEvent(final QCloseEvent arg__1) {
        super.closeEvent(arg__1);
        closed.emit();
    }

    @SuppressWarnings("unused")
    private void inspectorClosedSlot() {
        this.isClosed = true;
    }

    public boolean isClosed() {
        return this.isClosed;
    }

}
