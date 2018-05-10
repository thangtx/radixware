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
package org.radixware.kernel.explorer.editors.jmleditor;

import com.trolltech.qt.core.QMimeData;
import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.QPoint;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QBrush;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QHeaderView;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QMenu;
import com.trolltech.qt.gui.QResizeEvent;
import com.trolltech.qt.gui.QTreeWidget;
import com.trolltech.qt.gui.QTreeWidgetItem;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import java.util.*;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.check.RadixProblem.ESeverity;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.defs.ads.src.SrcPositionLocator.SrcLocation;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.editors.jmleditor.JmlEditor.IUserFuncLocator;
import org.radixware.kernel.explorer.editors.jmleditor.jmltags.JmlTag;
import org.radixware.kernel.explorer.editors.xscmleditor.TagInfo;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.schemas.userfunccommands.CompileRsDocument;
import org.radixware.schemas.userfunccommands.CompileRsDocument.CompileRs.Result.Problem.Location;

public class JmlProblemList extends QWidget {

    public static int[] convertPosition(Scml.ScmlAreaInfo info, JmlProcessor jmlProcessor) {
        int[] offsetAndLength;
        Scml.Item startItem = info.getStartJmlItem();
        if (startItem == null) {
            return null;
        }
        int start = jmlProcessor.getItemOffset(startItem);
        if (startItem instanceof Scml.Text) {
            start += info.getSourceStartOffset();
        }

        Scml.Item endItem = info.getEndJmlItem();
        if (endItem == null) {
            return null;
        }
        int end;
        int[] tagInfo = jmlProcessor.itemOffsetAndLength(endItem);
        if (tagInfo == null) {
            offsetAndLength = new int[]{0, 0};
        } else {
            if (endItem instanceof Scml.Text) {
                end = tagInfo[0] + info.getSourceEndOffset();
            } else {
                end = tagInfo[0] + tagInfo[1];
            }
            offsetAndLength = new int[]{start, end};
        }
        return offsetAndLength;
    }

    public interface IJmlProblemTreeItemModel {
        
        void calcProblemsCnt(int[] errors, int[] warnings);

        QTreeWidgetItem getOrCreateTreeItem();

        QTreeWidgetItem getTreeItem();

        IJmlProblemTreeItemModel getChild(int index);

        void addChildItem(IJmlProblemTreeItemModel item);

        boolean removeChild(IJmlProblemTreeItemModel item);

        int getChildsCount();

        void clearChildsList();
    }
    
    private static class BaseTreeItem extends QTreeWidgetItem {

        //Override to implement partial order:
        //Only top level items (UfTreeItem) are ordered
        @Override
        public boolean operator_less(QTreeWidgetItem other) {
            return false;
        }
    }

    public static class ProblemItem implements IJmlProblemTreeItemModel {

        private RadixProblem problem;
        private ProblemInfo problemInfo;
        private int[] posInfo = null;
        private UfItem ufItem = null;
        private QTreeWidgetItem treeItem;
        private String treeItemText;
        private String tooltip;
        RadixProblem.ESeverity severity;
        //private List<ProblemItem> copies = new ArrayList<>();

        ProblemItem(RadixProblem problem) {
            this(problem.getMessage(), problem.getSeverity());
            this.problem = problem;

        }

        ProblemItem(ProblemInfo problem) {
            this(problem.getMessage(), problem.getSeverity());
            this.problemInfo = problem;
        }

        ProblemItem(String message, RadixProblem.ESeverity severity) {
            tooltip = message;
            treeItemText = message;
            int index = treeItemText.indexOf('\n');
            if (index != -1) {
                treeItemText = treeItemText.replaceAll("\n", " ");
            }
            
            StringBuilder lineBuilder = new StringBuilder();
            StringBuilder tooltipText = new StringBuilder();
            String[] words = tooltip.split(" ");
            for(String word : words) {
                lineBuilder.append(word).append(' ');
                if(lineBuilder.length() > 80) {
                    tooltipText.append(lineBuilder.toString()).append(System.lineSeparator());
                    lineBuilder.setLength(0);
                }
            }
            if(lineBuilder.length() != 0) {
                tooltipText.append(lineBuilder.toString());
            }
            tooltip = tooltipText.toString();
            
            this.severity = severity;
        }

        ProblemItem(ProblemItem source) {
            this(source.getMessage(), source.getSeverity());
            this.problemInfo = source.problemInfo;
            this.problem = source.problem;
            int[] info = source.posInfo;
            if (info != null) {
                setPosInfo(info[0], info[1]);//posInfo.set() =source.getPosInfo();
            }
            //source.copies.add(this);
        }

        String getUfDisplayName() {
            if (problemInfo != null) {
                return problemInfo.getUfTitle();
            }
            return null;
        }

        private int[] computeItemInfo(JmlEditor editor) {
            int[] offsetAndLength = null;
            RadixProblem.IAnnotation problemAnnotation;
            if (problem != null) {
                problemAnnotation = problem.getAnnotation();
            } else {
                problemAnnotation = problemInfo.getProblemAnnotation(editor);
            }
            if ((problemAnnotation != null) && problemAnnotation instanceof Scml.ScmlAreaInfo) {
                Scml.ScmlAreaInfo info = (Scml.ScmlAreaInfo) problemAnnotation;
                offsetAndLength = convertPosition(info, editor.getJmlProcessor());
            } else {
                Jml.Tag srcTag = null;
                if (problem != null && problem.getSource() instanceof Jml.Tag) {
                    srcTag = (Jml.Tag) problem.getSource();
                } else if (problemInfo != null && problemInfo.getLocation() != null && problemInfo.getLocation().isSetEndItem()) {
                    srcTag = (Jml.Tag) editor.getJmlProcessor().getSource().getItems().list().get(problemInfo.getLocation().getEndItem());
                }
                if (srcTag != null) {
                    for (TagInfo tagInfo : editor.getJmlProcessor().getCurrentTagList()) {
                        if (((JmlTag) tagInfo).getTag().equals(srcTag)) {
                            offsetAndLength = new int[]{(int) (tagInfo.getStartPos() - 1), (int) (tagInfo.getEndPos() - 1)};
                            break;
                        }
                    }
                }
            }
            return offsetAndLength;
        }

        public final int[] getPosInfo(JmlEditor editor) {
            if (posInfo == null) {
                posInfo = computeItemInfo(editor);
            }
            /*if (posInfo != null) {
             for (ProblemItem c : copies) {
             c.setPosInfo(posInfo[0], posInfo[1]);
             }
             }*/
            if (posInfo == null) {
                posInfo = new int[]{0, 0};
            }
            return Arrays.copyOf(posInfo, posInfo.length);//posInfo.clone();
        }

        final public void setPosInfo(int start, int end) {
            posInfo = new int[]{start, end};
        }

        //public RadixProblem getProblem() {
        //   return problem;
        //}
        //public RadixProblem getProblem() {
        //   return problem;
        //}
        public String getMessage() {
            return problem != null ? problem.getMessage() : problemInfo.getMessage();
        }

        public ESeverity getSeverity() {
            return problem != null ? problem.getSeverity() : problemInfo.getSeverity();
        }

        // public void setProblem(RadixProblem problem) {
        //     this.problem = problem;
        //}
        UfItem getUfItem() {
            return ufItem;
        }

        void setUfItem(UfItem ufItem) {
            this.ufItem = ufItem;
        }

        @Override
        public QTreeWidgetItem getTreeItem() {
            return treeItem;
        }

        @Override
        public QTreeWidgetItem getOrCreateTreeItem() {
            if (treeItem == null) {
                treeItem = new ProblemTreeItem(this);
                treeItem.setText(0, treeItemText);
                treeItem.setToolTip(0, tooltip);
                if (severity == RadixProblem.ESeverity.ERROR) {
                    treeItem.setForeground(0, errorBrush);
                } else {
                    treeItem.setForeground(0, warningBrush);
                }
                QIcon icon = severity == ESeverity.ERROR ? ExplorerIcon.getQIcon(JmlEditor.JmlEditorIcons.IMG_ERROR)
                        : ExplorerIcon.getQIcon(JmlEditor.JmlEditorIcons.TraceLevel.WARNING);
                treeItem.setIcon(0, icon);
            }
            return treeItem;
        }

        @Override
        public void addChildItem(IJmlProblemTreeItemModel item) {
            //do nothing
        }

        @Override
        public boolean removeChild(IJmlProblemTreeItemModel item) {
            return false;
        }

        @Override
        public IJmlProblemTreeItemModel getChild(int index) {
            throw new UnsupportedOperationException("Child list for ProblemItem is empty.");
        }

        @Override
        public int getChildsCount() {
            return 0;
        }

        @Override
        public void clearChildsList() {
            //do nothing
        }

        @Override
        public void calcProblemsCnt(int[] errors, int[] warnings) {
           if (getSeverity() != null) {
               if (getSeverity() == ESeverity.ERROR) {
                   errors[0]++;
               } else if (getSeverity() == ESeverity.WARNING) {
                   warnings[0]++;
               }
           }
        }
    }

    static class ProblemTreeItem extends BaseTreeItem {

        ProblemItem problemItem;

        public ProblemTreeItem(ProblemItem problemItem) {
            this.problemItem = problemItem;
        }

        public ProblemItem getProblemItem() {
            return problemItem;
        }
    }

    static class SourceVersionItem implements IJmlProblemTreeItemModel {

        private QTreeWidgetItem treeItem;
        private String version;
        private List<IJmlProblemTreeItemModel> childs = new LinkedList<>();

        public SourceVersionItem(String version) {
            this.version = version;
        }

        public String getVersion() {
            return version;
        }

        @Override
        public QTreeWidgetItem getTreeItem() {
            return treeItem;
        }

        @Override
        public QTreeWidgetItem getOrCreateTreeItem() {
            if (treeItem == null) {
                treeItem = new SourceVersionTreeItem(this);
                treeItem.setText(0, version);
                for (IJmlProblemTreeItemModel child : childs) {
                    treeItem.addChild(child.getOrCreateTreeItem());
                }
            }
            return treeItem;
        }

        @Override
        public void addChildItem(IJmlProblemTreeItemModel item) {
            childs.add(item);
        }

        @Override
        public boolean removeChild(IJmlProblemTreeItemModel item) {
            if (item == null) {
                return false;
            }
            if (childs.contains(item)) {
                childs.remove(item);
                item.clearChildsList();
                QTreeWidgetItem treeItemToRemove = item.getTreeItem();
                if (treeItemToRemove != null) {
                    if (treeItemToRemove.parent() != null) {
                        treeItemToRemove.parent().removeChild(treeItemToRemove);
                    } else {
                        treeItemToRemove.treeWidget().takeTopLevelItem(treeItemToRemove.treeWidget().indexOfTopLevelItem(treeItemToRemove));
                    }
                }
                return true;
            }
            return false;
        }

        @Override
        public int getChildsCount() {
            return childs.size();
        }

        @Override
        public IJmlProblemTreeItemModel getChild(int index) {
            return childs.get(index);
        }

        @Override
        public void clearChildsList() {
            childs.clear();
        }

        @Override
        public void calcProblemsCnt(int[] errors, int[] warnings) {
            for (IJmlProblemTreeItemModel child : childs) {
                child.calcProblemsCnt(errors, warnings);
            }
        }
    }

    static class SourceVersionTreeItem extends BaseTreeItem {

        private SourceVersionItem sourceVersionItem;

        public SourceVersionTreeItem(SourceVersionItem sourceVersionItem) {
            this.sourceVersionItem = sourceVersionItem;
        }

        public SourceVersionItem getSourceVersionItem() {
            return sourceVersionItem;
        }
    }

    static class UfItem implements IJmlProblemTreeItemModel {

        final long id;
        private final List<IJmlProblemTreeItemModel> childs = new LinkedList<>();
        private QTreeWidgetItem treeItem;
        private final String treeItemText;

        UfItem(IUserFuncLocator ufLocator, long id, String ufTitle) {
            this.id = id;
            if (ufTitle == null || ufTitle.isEmpty()) {
                ufTitle = ufLocator.getUserFuncDisplayName(id);
            }
            this.treeItemText = ufTitle;
        }

        UfItem(UfItem source) {
            this.id = source.id;
            this.treeItemText = source.treeItemText;
        }
        
        private void updateTreeItemText() {
            if (treeItem == null) {
                return;
            }
            final int[] errors = new int[1];
            final int[] warnings = new int[1];
            calcProblemsCnt(errors, warnings);
            
            final QBrush b;
            final String text;
            if (errors[0] > 0) {
                b  = errorBrush;
                if (warnings[0] > 0) {
                    text = String.format("[errors: %d, warnings: %d]", errors[0], warnings[0]);
                } else {
                    text = String.format("[errors: %d]", errors[0]);
                }
            } else if (warnings[0] > 0) {
                b  = warningBrush;
                text = String.format("[warnings: %d]", warnings[0]);
            } else {
                b = null;
                text = null;
            }
            treeItem.setForeground(0, b);
            treeItem.setForeground(1, b);
            treeItem.setText(1, text);
        }
        
        private String getTreeTextWithId() {
            return String.format("%d) %s", id, treeItemText);
        }

        @Override
        public QTreeWidgetItem getTreeItem() {
            return treeItem;
        }

        @Override
        public QTreeWidgetItem getOrCreateTreeItem() {
            if (treeItem == null) {
                treeItem = new UfTreeItem(this);
                treeItem.setText(0, getTreeTextWithId());
                for (IJmlProblemTreeItemModel child : childs) {
                    treeItem.addChild(child.getOrCreateTreeItem());
                }
                updateTreeItemText();
            }
            return treeItem;
        }

        @Override
        public void addChildItem(IJmlProblemTreeItemModel item) {
            childs.add(item);
        }

        @Override
        public boolean removeChild(IJmlProblemTreeItemModel item) {
            if (item == null) {
                return false;
            }
            if (childs.contains(item)) {
                childs.remove(item);
                item.clearChildsList();
                QTreeWidgetItem treeItemToRemove = item.getTreeItem();
                if (treeItemToRemove != null) {
                    if (treeItemToRemove.parent() != null) {
                        treeItemToRemove.parent().removeChild(treeItemToRemove);
                    } else {
                        treeItemToRemove.treeWidget().takeTopLevelItem(treeItemToRemove.treeWidget().indexOfTopLevelItem(treeItemToRemove));
                    }
                }
                return true;
            }
            return false;
        }

        @Override
        public int getChildsCount() {
            return childs.size();
        }

        @Override
        public IJmlProblemTreeItemModel getChild(int index) {
            return childs.get(index);
        }
        
        @Override
        public void clearChildsList() {
            childs.clear();
        }
        
        @Override
        public void calcProblemsCnt(int[] errors, int[] warnings) {
            for (IJmlProblemTreeItemModel child : childs) {
                child.calcProblemsCnt(errors, warnings);
            }
        }
    }

    static class UfTreeItem extends BaseTreeItem {

        private UfItem ufItem;

        public UfTreeItem(UfItem ufItem) {
            this.ufItem = ufItem;
        }

        public UfItem getUfItem() {
            return ufItem;
        }

        @Override
        public boolean operator_less(QTreeWidgetItem other) {
            if (other instanceof UfTreeItem) {
                return ufItem.id < ((UfTreeItem) other).ufItem.id;
            }
            return false;
        }
    }

    private static class DefaultProblemHandler implements IProblemHandler {

        private final JmlProblemList problemList;
        private long id;

        DefaultProblemHandler(JmlProblemList problemList) {
            this.problemList = problemList;
        }

        private void open(long pid) {
            this.id = pid;
        }

        @Override
        public void accept(RadixProblem problem) {
            this.problemList.acceptProblem(id, problem);
        }
    }
    private QTreeWidget problemTree;
    private DefaultProblemHandler problemHandler;
    //private final Map<Pid, AdsUserFuncDef> ufByPid = new HashMap<>();
    private final QAction actCopyProblemText;
    private final QAction actCopyCurUfProblemsText;
    private final QAction actCopyAllProblemsText;
    private final QAction actClearAllProblems;
    
    public JmlProblemList(IUserFuncLocator locator, QWidget parent) {
        this(locator, parent, false);
    }

    public JmlProblemList(IUserFuncLocator locator, QWidget parent, boolean showProblemCntColumn) {
        super(parent);
        this.ufLocator = locator;
        this.setLayout(new QVBoxLayout());
        this.layout().setMargin(0);
        this.problemTree = new QTreeWidget(this);
        this.problemTree.setObjectName("ProblrmTree");
        this.layout().addWidget(problemTree);
        this.problemHandler = new DefaultProblemHandler(this);
        
        if (showProblemCntColumn) {
            problemTree.setColumnCount(2);
            problemTree.header().setResizeMode(0, QHeaderView.ResizeMode.Interactive);
            problemTree.header().setResizeMode(1, QHeaderView.ResizeMode.ResizeToContents);
            problemTree.header().resizeSection(0, 500);
            problemTree.setHeaderLabels(Arrays.asList("", ""));
            problemTree.setSortingEnabled(true);
        } else {
            problemTree.header().hide();
        }
        problemTree.doubleClicked.connect(this, "dblClickItem(com.trolltech.qt.core.QModelIndex)");
        problemTree.setContextMenuPolicy(Qt.ContextMenuPolicy.CustomContextMenu);
        problemTree.customContextMenuRequested.connect(this, "customContextMenuRequested(QPoint)");
        setVisible(false);

        actCopyProblemText = new QAction(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.COPY), Application.translate("JmlEditor", "Copy Text"), null);
        actCopyProblemText.triggered.connect(this, "copyProblemText_Clicked()");
        actCopyCurUfProblemsText = new QAction(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.COPY), Application.translate("JmlEditor", "Copy Text of Current Function Errors"), null);
        actCopyCurUfProblemsText.triggered.connect(this, "copyProblemText_Clicked()");
        actCopyAllProblemsText = new QAction(ExplorerIcon.getQIcon(ClientIcon.Selector.COPY_ALL), Application.translate("JmlEditor", "Copy Text of All Errors"), null);
        actCopyAllProblemsText.triggered.connect(this, "copyAllProblemText_Clicked()");
        actClearAllProblems = new QAction(ExplorerIcon.getQIcon(ClientIcon.Selector.Editor.CLEAR), Application.translate("JmlEditor", "Clear All Errors"), null);
        actClearAllProblems.triggered.connect(this, "actClearAllProblems_Clicked()");
    }
    
    @SuppressWarnings("unused")
    private void actClearAllProblems_Clicked() {
        clear(-1);
        setVisible(false);
    }

    @SuppressWarnings("unused")
    private void copyProblemText_Clicked() {
        String result = "";
        final QTreeWidgetItem currentItem = problemTree.currentItem();
        if (currentItem instanceof ProblemTreeItem) {
            result = ((ProblemTreeItem) currentItem).getProblemItem().getMessage();
        } else if (currentItem instanceof UfTreeItem) {
            UfTreeItem ufItem = (UfTreeItem) currentItem;
            final StringBuffer sb = new StringBuffer(ufItem.text(0));
            for (int i = 0; i < ufItem.childCount(); i++) {
                QTreeWidgetItem childItem = ufItem.child(i);
                if (childItem instanceof ProblemTreeItem) {
                    sb.append("\n     ").append(((ProblemTreeItem) childItem).getProblemItem().getMessage());
                }
            }
            result = sb.toString();
        }
        QMimeData mimeData = new QMimeData();
        mimeData.setText(result);
        QApplication.clipboard().setMimeData(mimeData);
    }

    @SuppressWarnings("unused")
    private void copyAllProblemText_Clicked() {
        final StringBuffer sb = new StringBuffer();
        for (int i = 0; i < problemTree.topLevelItemCount(); i++) {
            final QTreeWidgetItem item = problemTree.topLevelItem(i);
            sb.append(item.text(0));
            for (int j = 0; j < item.childCount(); j++) {
                final QTreeWidgetItem childItem = item.child(j);
                sb.append("\n     ").append(childItem.text(0));
            }
            if (i < (problemTree.topLevelItemCount() - 1)) {
                sb.append("\n");
            }
        }
        QMimeData mimeData = new QMimeData();
        mimeData.setText(sb.toString());
        QApplication.clipboard().setMimeData(mimeData);
    }

    @SuppressWarnings("unused")
    private void customContextMenuRequested(QPoint point) {
        final QTreeWidgetItem currentItem = problemTree.itemAt(point);
        problemTree.setCurrentItem(currentItem);
        if (currentItem != null) {
            QMenu menu = new QMenu(problemTree);
            menu.addAction((currentItem instanceof ProblemTreeItem) ? actCopyProblemText : actCopyCurUfProblemsText);
            menu.addAction(actCopyAllProblemsText);
            menu.addAction(actClearAllProblems);
            menu.popup(problemTree.viewport().mapToGlobal(point));
        }
    }

    public void open(long id) {
        this.problemHandler.open(id);
        this.fillErrorList(id);
    }

    public IProblemHandler getProblemHandler() {
        return problemHandler;
    }

    @SuppressWarnings("unused")
    private void dblClickItem(QModelIndex index) {
        QTreeWidgetItem item = problemTree.currentItem();
        UfItem ufItem = null;
        ProblemTreeItem pi = null;
        QTreeWidgetItem sourceVersion = null;
        if (item instanceof UfTreeItem) {
            ufItem = ((UfTreeItem) item).getUfItem();
        } else if (item instanceof ProblemTreeItem) {
            pi = (ProblemTreeItem) item;
            if (item.parent() instanceof UfTreeItem) {
                ufItem = ((UfTreeItem) item.parent()).getUfItem();
            } else if (item.parent() != null) {
                sourceVersion = item.parent();
            }
            if (ufItem == null) {
                ufItem = pi.getProblemItem().getUfItem();
            }
        }
        if (ufItem == null) {
            return;
        }
        String sorceVersionName = (sourceVersion == null) ? null : sourceVersion.text(0);
        if (switchToContext(ufItem.id, sorceVersionName, null) && pi != null) {
            ufLocator.moveCursorToProblem(pi.getProblemItem());
        }
    }
    private final HashMap<Long, UfItem> roots = new HashMap<>();

    private UfItem findOrCreateRoot(IUserFuncLocator ufLocator, long id, String ufTitle) {

        UfItem item = roots.get(id);
        if (item == null) {
            item = new UfItem(ufLocator, id, ufTitle);
            roots.put(id, item);
            //this.problemTree.addTopLevelItem(item);
        }
        return item;
    }
    private static final QBrush errorBrush = new QBrush(QColor.red);
    private static final QBrush warningBrush = new QBrush(QColor.fromRgb(255, 165, 0)); //orange
    private IUserFuncLocator ufLocator;
    public final Signal3<JmlProblemList, Integer, Boolean> viewStateChanged = new Signal3<>();

    public boolean switchToContext(long id, String sourceVersionName, JmlEditor.IPostOpenAction action) {
        return ufLocator.enshureObjectIsOpened(id, sourceVersionName, false, action);
    }

    public void acceptProblem(long id, RadixProblem problem) {
        ProblemItem newItem = new ProblemItem(problem);
        acceptProblem(id, newItem);
    }

    private void acceptProblem(long id, ProblemItem newItem) {
        //ProblemItem newItem = new ProblemItem(problem);

        String sourceVersionName = ufLocator.getCurrentVersionName();
        String ufTitle = newItem.getUfDisplayName();
        UfItem ufItem = findOrCreateRoot(ufLocator, id, ufTitle);
        if (sourceVersionName == null) {
            ufItem.addChildItem(newItem);
        } else {
            SourceVersionItem sourceVersionItem = null;
            for (int i = 0; i < ufItem.getChildsCount(); i++) {
                if (!(ufItem.getChild(i) instanceof ProblemItem)
                        && (sourceVersionName.equals(((SourceVersionItem) ufItem.getChild(i)).getVersion()))) {
                    sourceVersionItem = (SourceVersionItem) ufItem.getChild(i);
                    break;
                }
            }
            if (sourceVersionItem == null) {
                sourceVersionItem = new SourceVersionItem(sourceVersionName);
            }
            newItem.setUfItem(ufItem);
            sourceVersionItem.addChildItem(newItem);
            ufItem.addChildItem(sourceVersionItem);
        }

    }

    @Override
    protected void resizeEvent(QResizeEvent qre) {
        super.resizeEvent(qre);
        viewStateChanged.emit(this, qre.size().height() - qre.oldSize().height(), true);
    }

    @Override
    public final void setVisible(boolean bln) {
        boolean fire = isVisible() != bln;
        super.setVisible(bln);
        if (fire) {
            viewStateChanged.emit(this, isVisible() ? size().height() : -size().height(), false);
        }

    }
    private static final Id UF_TABLE_ID = Id.Factory.loadFrom("tblJ6SOXKD3ZHOBDCMTAALOMT5GDM");

    /**
     * API Displays list of problems wrapped into xml (tipically it is a
     * response of server side compilation)
     */
    public void acceptProblemList(IUserFuncLocator ufLocator, CompileRsDocument xDoc) {
        this.blockSignals(true);
        try {
            if (ufLocator == null) {
                Application.messageError("Unable to expose problem list");
                return;
            }
            CompileRsDocument.CompileRs response = xDoc.getCompileRs();
            if (response != null) {
                for (CompileRsDocument.CompileRs.Result result : response.getResultList()) {
                    String ufTitle = result.getUfTitle();
                    long id = result.getUfId();
                    for (CompileRsDocument.CompileRs.Result.Problem pb : result.getProblemList()) {
                        ProblemInfo problemInfo = new ProblemInfo(pb, id, ufTitle, ufLocator);
                        acceptProblem(id, new ProblemItem(problemInfo));
                    }
                }
                fillErrorList();
            }
        } finally {
            this.blockSignals(false);
        }
    }

    static class ProblemInfo {

        private final CompileRsDocument.CompileRs.Result.Problem pb;
        private final long id;
        private final IUserFuncLocator ufLocator;
        private String ufTitle;

        ProblemInfo(CompileRsDocument.CompileRs.Result.Problem pb, final long pid, String ufTitle, final IUserFuncLocator ufLocator) {
            this.pb = pb;
            this.id = pid;
            this.ufLocator = ufLocator;
            this.ufTitle = ufTitle;
        }

        String getUfTitle() {
            return ufTitle;
        }

        String getMessage() {
            return pb.getMessage();
        }

        SrcLocation getProblemAnnotation(JmlEditor editor) {
            if (pb.getLocation() == null || !pb.getLocation().isSetEndOffset() || !pb.getLocation().isSetStartItem()) {
                return null;
            }
            
            return new SrcLocation((Jml) editor.getJmlProcessor().getSource(), pb.getLocation().getStartItem(), pb.getLocation().getStartOffset(), pb.getLocation().getEndItem(), pb.getLocation().getEndOffset());
        }
        
        Location getLocation() {
            return pb.getLocation();
        }

        RadixProblem.ESeverity getSeverity() {
            if (RadixProblem.ESeverity.ERROR.ordinal() == pb.getSeverity()) {
                return RadixProblem.ESeverity.ERROR;
            }
            return RadixProblem.ESeverity.WARNING;
        }
    }

    Set<ProblemItem> getProblems(long id) {
        UfItem item = roots.get(id);
        if (item == null) {
            return Collections.emptySet();
        } else {
            Set<ProblemItem> items = new HashSet<>();
            if (ufLocator.getCurrentVersionName() == null) {
                for (int i = 0; i < item.getChildsCount(); i++) {
                    IJmlProblemTreeItemModel c = item.getChild(i);
                    if (c instanceof ProblemItem) {
                        items.add((ProblemItem) c);
                    }
                }
            } else {
                for (int i = 0; i < item.getChildsCount(); i++) {
                    IJmlProblemTreeItemModel sourceVersionItem = item.getChild(i);
                    if (!(sourceVersionItem instanceof ProblemItem)
                            && ufLocator.getCurrentVersionName().equals(((SourceVersionItem) sourceVersionItem).getVersion())) {
                        for (int j = 0; j < sourceVersionItem.getChildsCount(); j++) {
                            if (sourceVersionItem.getChild(j) instanceof ProblemItem) {
                                items.add((ProblemItem) sourceVersionItem.getChild(j));
                            }
                        }
                    }
                }
            }

            return items;
        }
    }

    private void fillErrorList() {
        List<UfItem> list = new ArrayList<>();
        list.addAll(roots.values());
        problemTree.clear();
        if (list.size() > 1) {
            for (int i = 0; i < list.size(); i++) {
                UfItem itemUf = list.get(i);
                UfItem newUfItem = new UfItem(itemUf);
                if (itemUf.getChildsCount() > 0) {
                    for (int j = 0; j < itemUf.getChildsCount(); j++) {
                        final ProblemItem problItem = new ProblemItem((ProblemItem) itemUf.getChild(j));
                        newUfItem.addChildItem(problItem);
                    }
                }
                problemTree.addTopLevelItem(newUfItem.getOrCreateTreeItem());
            }
        } else if (list.size() == 1) {
            //fillErrorList(list.get(0).pid);
            UfItem itemUf = list.get(0);
            fillErrorList(itemUf, list.get(0).id);
        }

        if (problemTree.topLevelItemCount() == 0) {
            setVisible(false);
        } else {
            setVisible(true);
        }
    }

    public void fillErrorList(long id) {
        problemTree.clear();

        List<UfItem> list = new ArrayList<>();
        list.addAll(roots.values());
        if (list.size() > 1) {
            for (int i = 0; i < list.size(); i++) {
                UfItem itemUf = list.get(i);
                UfItem newUfItem = new UfItem(itemUf);
                final long itemPidAsStr = newUfItem.id;
                for (int j = 0; j < itemUf.getChildsCount(); j++) {
                    if (itemUf.getChild(j) instanceof ProblemItem) {
                        final ProblemItem problItem = new ProblemItem((ProblemItem) itemUf.getChild(j));
                        newUfItem.addChildItem(problItem);
                    } else {
                        SourceVersionItem ufSrcVersionName = new SourceVersionItem(((SourceVersionItem) itemUf.getChild(j)).getVersion());
                        for (int k = 0; k < itemUf.getChild(j).getChildsCount(); k++) {
                            final ProblemItem problItem = new ProblemItem((ProblemItem) itemUf.getChild(j).getChild(k));
                            problItem.setUfItem(newUfItem);
                            ufSrcVersionName.addChildItem(problItem);
                        }
                        newUfItem.addChildItem(ufSrcVersionName);
                    }
                }
                if (newUfItem.getChildsCount() > 0) {
                    problemTree.addTopLevelItem(newUfItem.getOrCreateTreeItem());
                    if (itemPidAsStr == id) {
                        newUfItem.getTreeItem().setExpanded(true);
                    }
                } else {
                    if (itemPidAsStr == id) {
                        roots.remove(id);
                    }
                }

            }

        } else if (list.size() == 1) {
            UfItem itemUf = list.get(0);
            fillErrorList(itemUf, id);
        }
        if (problemTree.topLevelItemCount() == 0) {
            setVisible(false);
        } else {
            setVisible(true);
        }
    }

    /*public void fillValidationProblemItems(IUserFuncLocator ufLocator, final Pid pid) {
     List<QTreeWidgetItem> validationProblemItems = new ArrayList<>();
     if (jmlEditor.getValidProblemList() != null) {
     for (Object problem : jmlEditor.getValidProblemList().keySet()) {
     RadixProblem radProblem = (RadixProblem) problem;
     ProblemItem problemItem = new ProblemItem(radProblem);
     validationProblemItems.add(problemItem);
     }
     }
     findOrCreateRoot(ufLocator, pid, null).addChildren(validationProblemItems);
     }*/
    private void fillErrorList(UfItem itemUf, final long id) {
        if (itemUf.id == id) {
            List<IJmlProblemTreeItemModel> items = new ArrayList<>();
            for (int i = 0; i < itemUf.getChildsCount(); i++) {
                if (itemUf.getChild(i) instanceof ProblemItem) {
                    ProblemItem problemItem = new ProblemItem((ProblemItem) itemUf.getChild(i));
                    problemItem.setUfItem(itemUf);
                    items.add(problemItem);
                } else {
                    SourceVersionItem ufSrcVersionName = new SourceVersionItem(((SourceVersionItem) itemUf.getChild(i)).getVersion());
                    for (int k = 0; k < itemUf.getChild(i).getChildsCount(); k++) {
                        ProblemItem problemItem = new ProblemItem((ProblemItem) itemUf.getChild(i).getChild(k));
                        problemItem.setUfItem(itemUf);
                        ufSrcVersionName.addChildItem(problemItem);
                    }
                    items.add(ufSrcVersionName);
                }
            }
            if (!items.isEmpty()) {
                for (IJmlProblemTreeItemModel item : items) {
                    problemTree.addTopLevelItem(item.getOrCreateTreeItem());
                }
            }
        } else {
            UfItem newUfItem = new UfItem(itemUf);
            if (itemUf.getChildsCount() > 0) {
                for (int i = 0; i < itemUf.getChildsCount(); i++) {
                    if (itemUf.getChild(i) instanceof ProblemItem) {
                        ProblemItem problemItem = new ProblemItem((ProblemItem) itemUf.getChild(i));
                        problemItem.setUfItem(itemUf);
                        newUfItem.addChildItem(problemItem);
                    } else {
                        SourceVersionItem ufSrcVersionName = new SourceVersionItem(((SourceVersionItem) itemUf.getChild(i)).getVersion());
                        for (int k = 0; k < itemUf.getChild(i).getChildsCount(); k++) {
                            ProblemItem problemItem = new ProblemItem((ProblemItem) itemUf.getChild(i).getChild(k));
                            problemItem.setUfItem(itemUf);
                            ufSrcVersionName.addChildItem(problemItem);
                        }
                        newUfItem.addChildItem(ufSrcVersionName);
                    }
                }
            }
            if (newUfItem.getChildsCount() > 0) {
                problemTree.addTopLevelItem(newUfItem.getOrCreateTreeItem());
            }
        }
        if (problemTree.topLevelItemCount() == 0) {
            setVisible(false);
        } else {
            setVisible(true);
        }
    }

    public void removeSourceVersionProblems(String removedSrcVersionName, long userFuncId) {
        for (int i = 0; i < problemTree.topLevelItemCount(); i++) {
            QTreeWidgetItem item = problemTree.topLevelItem(i);
            if (item instanceof UfTreeItem) {

                if (((UfTreeItem) item).getUfItem().id == userFuncId) {
                    for (int j = 0; j < item.childCount(); j++) {
                        QTreeWidgetItem child_item = item.child(j);
                        if (!(child_item instanceof ProblemTreeItem) && removedSrcVersionName.equals(child_item.text(0))) {
                            item.removeChild(child_item);
                            break;
                        }
                    }
                }
            } else if (!(item instanceof ProblemTreeItem) && removedSrcVersionName.equals(item.text(0))) {
                problemTree.takeTopLevelItem(i);
                UfItem ufItem = roots.get(userFuncId);
                for (int j = 0; j < ufItem.getChildsCount(); j++) {
                    QTreeWidgetItem child_item = ufItem.getChild(j).getTreeItem();
                    if (child_item != null && !(child_item instanceof ProblemTreeItem) && removedSrcVersionName.equals(child_item.text(0))) {
                        ufItem.removeChild(ufItem.getChild(j));
                    }
                }
                break;
            }
        }
    }

    public void clear(long userFuncId, String versionName) {
        for (int i = 0; i < problemTree.topLevelItemCount(); i++) {
            if (problemTree.topLevelItem(i) instanceof UfTreeItem) {
                UfTreeItem ufItem = (UfTreeItem) problemTree.takeTopLevelItem(i);
                if (ufItem.getUfItem().id == userFuncId) {
                    for (int j = 0; j < ufItem.childCount(); j++) {
                        if (ufItem.child(j).text(i).equals(versionName)) {
                            ufItem.removeChild(ufItem.child(j));
                            break;
                        }
                    }
                }
            } else if (problemTree.topLevelItem(i).text(0) != null && problemTree.topLevelItem(i).text(0).equals(versionName)) {
                problemTree.takeTopLevelItem(i);
                break;
            }
        }
        if (problemTree.topLevelItemCount() == 0) {
            setVisible(false);
        }
    }

    public void clear(long userFuncPid) {
        if (userFuncPid < 0) {
            roots.clear();
            problemTree.clear();
        } else {
            UfItem ufItem = roots.get(userFuncPid);
            if (ufItem != null) {
                if (ufLocator.getCurrentVersionName() == null) {
                    for (int i = 0; i < ufItem.getChildsCount(); i++) {
                        if (ufItem.getChild(i) instanceof ProblemItem) {
                            if (ufItem.removeChild(ufItem.getChild(i))) {
                                i--;
                            }
                        }
                    }
                } else {
                    for (int i = 0; i < ufItem.getChildsCount(); i++) {
                        if (ufLocator.getCurrentVersionName().equals(((SourceVersionItem) ufItem.getChild(i)).getVersion())) {
                            ufItem.removeChild(ufItem.getChild(i));
                            break;
                        }
                    }
                }
            }
        }
        if (problemTree.topLevelItemCount() == 0) {
            setVisible(false);
        }
    }

    public int getItemCount() {
        int res = roots.size();
        for (UfItem item : roots.values()) {
            res += item.getChildsCount();
            for (int i = 0; i < item.getChildsCount(); i++) {
                res += item.getChild(i).getChildsCount();
            }
        }
        return res;
    }

    public boolean isProblemTreeEmpty() {
        return problemTree.topLevelItemCount() <= 0;
    }

    ProblemItem createNewProblemItem(RadixProblem problem) {
        return new ProblemItem(problem);
    }
   
    public void onCancelChanges(final long id) {
        clear(id);
    }
}