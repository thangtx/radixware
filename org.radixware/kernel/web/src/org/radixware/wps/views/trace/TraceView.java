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

package org.radixware.wps.views.trace;

import org.radixware.kernel.common.html.Html;
import java.awt.Color;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.dialogs.IFindAndReplaceDialog;
import org.radixware.kernel.common.client.editors.traceprofile.TraceProfileTreeController;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.mask.EditMaskList;
import org.radixware.kernel.common.client.trace.ClientTraceItem;
import org.radixware.kernel.common.client.trace.ClientTraceParser;
import org.radixware.kernel.common.client.trace.TraceBuffer;
import org.radixware.kernel.common.client.views.IDialog;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.client.widgets.actions.Action;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.trace.TraceParser;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.dialogs.FindAndReplaceDialog;
import org.radixware.wps.icons.WpsIcon;
import org.radixware.wps.rwt.*;
import org.radixware.wps.views.RwtAction;
import org.radixware.wps.views.editors.valeditors.ValDateTimeEditorController;
import org.radixware.wps.views.editors.valeditors.ValIntEditorController;
import org.radixware.wps.views.editors.valeditors.ValListEditorController;


public class TraceView extends VerticalBoxContainer {

    private static class Devider extends UIObject {

        public Devider() {
            super(new Html("div"));
            setMinimumHeight(3);
        }
    }

    private static class TraceMessageDialog extends Dialog {

        private final static String CONFIG_PREFIX = SettingNames.SYSTEM + "/TraceDialog/TraceMessageDialog";
        private final ClientTraceParser clientTraceParser;
        private final TraceParser traceParser;

        public TraceMessageDialog(final ClientTraceItem traceItem, final IClientEnvironment environment) {
            super(environment, getSeverityTitle(traceItem.getSeverity(), environment.getMessageProvider()));
            if (environment.getApplication().isReleaseRepositoryAccessible()) {
                clientTraceParser = new ClientTraceParser(environment);
                traceParser = new TraceParser(environment.getDefManager().getClassLoader());
            } else {
                clientTraceParser = null;
                traceParser = null;
            }
            getHtml().setAttr("dlgId", "traceMessageDialog");
            setupUi(traceItem);
            setWindowIcon(ClientIcon.TraceLevel.findEventSeverityIcon(traceItem.getSeverity(), environment));
        }

        private void setupUi(final ClientTraceItem traceItem) {
            final VerticalBoxContainer verticalContainer = new VerticalBoxContainer();
            final TableLayout tableLayout = new TableLayout();
            {//Time of trace event
                final TableLayout.Row row = tableLayout.addRow();
                row.addCell().add(new Label(getEnvironment().getMessageProvider().translate("TraceDialog", "Time:")));
                final InputBox<String> inputBox = new InputBox<>();
                inputBox.setValue(getTime(traceItem));
                row.addCell().add(inputBox);
            }
            {
                tableLayout.addRow().addSpace();
            }
            {//Source of trace event
                final TableLayout.Row row = tableLayout.addRow();
                row.addCell().add(new Label(getEnvironment().getMessageProvider().translate("TraceDialog", "Source:")));
                final InputBox<String> inputBox = new InputBox<>();
                inputBox.setValue(traceItem.getSource());
                row.addCell().add(inputBox);
            }

            verticalContainer.setTop(3);
            verticalContainer.setLeft(3);
            verticalContainer.add(tableLayout);
            tableLayout.setSizePolicy(SizePolicy.PREFERRED, SizePolicy.PREFERRED);
            verticalContainer.add(new Devider());
            verticalContainer.add(new Devider());
            tableLayout.getAnchors().setRight(new Anchors.Anchor(1, 0));

            final ToolBar toolBar = new ToolBar();
            final boolean canTranslateMessage =
                clientTraceParser != null && (traceItem.getSeverity() == EEventSeverity.ERROR || traceItem.getSeverity() == EEventSeverity.ALARM);
            final boolean canTranslateStackTrace = traceItem.getStackTrace() != null && traceParser != null;
            final boolean isTranslateButtonVisible = canTranslateMessage || canTranslateStackTrace;
            toolBar.setVisible(isTranslateButtonVisible);
            verticalContainer.add(toolBar);

            final RwtAction translateAction = new RwtAction(getApplication().getImageManager().getIcon(ClientIcon.CommonOperations.TRANSLATE), getEnvironment().getMessageProvider().translate("TraceDialog", "&Translate"));
            translateAction.setEnabled(true);
            translateAction.setCheckable(true);
            translateAction.setTextShown(true);
            toolBar.addAction(translateAction);

            final TextArea messageArea = new TextArea();
            messageArea.setText(traceItem.getMessageText());
            messageArea.setLineWrap(false);
            messageArea.setReadOnly(true);

            final TextArea stackTraceArea;
            if (traceItem.getStackTrace() != null) {
                final Splitter splitter = new Splitter(getEnvironment().getConfigStore(), CONFIG_PREFIX);
                splitter.setOrientation(Splitter.Orientation.VERTICAL);
                verticalContainer.add(splitter);
                splitter.add(messageArea);
                final VerticalBoxContainer stackTraceContainer = new VerticalBoxContainer();
                final Label stackTraceLabel = new Label(getEnvironment().getMessageProvider().translate("TraceDialog", "Call stack:"));
                stackTraceContainer.add(stackTraceLabel);
                stackTraceArea = new TextArea();
                stackTraceArea.setText(traceItem.getStackTrace());
                stackTraceArea.setLineWrap(false);
                stackTraceArea.setReadOnly(true);
                stackTraceContainer.add(stackTraceArea);
                stackTraceContainer.setAutoSize(stackTraceArea, true);
                splitter.add(stackTraceContainer);
                if (!getEnvironment().getConfigStore().contains(CONFIG_PREFIX)) {
                    splitter.setPart(0, (float) 2. / 3);
                }
                verticalContainer.setAutoSize(splitter, true);
            } else {
                verticalContainer.add(messageArea);
                verticalContainer.setAutoSize(messageArea, true);
                stackTraceArea = null;
            }

            verticalContainer.getAnchors().setBottom(new Anchors.Anchor(1, -3));
            verticalContainer.getAnchors().setRight(new Anchors.Anchor(1, -3));

            translateAction.addActionListener(new Action.ActionListener() {
                @Override
                public void triggered(Action action) {
                    if (traceItem.getSeverity() == EEventSeverity.ERROR || traceItem.getSeverity() == EEventSeverity.ALARM) {
                        messageArea.setText(translateAction.isChecked() ? clientTraceParser.parse(traceItem.getMessageText()) : traceItem.getMessageText());
                    }
                    if (stackTraceArea != null && traceParser != null) {
                        if (translateAction.isChecked()) {
                            stackTraceArea.setText(traceParser.parse(traceItem.getStackTrace()));
                        } else {
                            stackTraceArea.setText(traceItem.getStackTrace());
                        }
                    }
                }
            });

            add(verticalContainer);
            addCloseAction(EDialogButtonType.CLOSE).setVisible(false);
        }

        private static String getTime(final ClientTraceItem traceItem) {
            final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss.S");//NOPMD
            return df.format(new Date(traceItem.getTime()));
        }

        private static String getSeverityTitle(final EEventSeverity severity, final MessageProvider messageProvider) {
            switch (severity) {
                case DEBUG:
                    return messageProvider.translate("TraceDialog", "Debug Message");
                case EVENT:
                    return messageProvider.translate("TraceDialog", "Event Message");
                case WARNING:
                    return messageProvider.translate("TraceDialog", "Warning Message");
                case ERROR:
                    return messageProvider.translate("TraceDialog", "Error Message");
                case ALARM:
                    return messageProvider.translate("TraceDialog", "Alarm Message");
                default:
                    return messageProvider.translate("TraceDialog", "Unknown Message");
            }
        }
    }

    private final static class TraceListItem extends ListBox.ListBoxItem {

        private final static Color WARNING_COLOR = new Color(243, 111, 20);
        private final static Color ALARM_COLOR = new Color(128, 0, 0);
        private final IClientEnvironment environment;

        public TraceListItem(final ClientTraceItem traceItem, final IClientEnvironment environment) {
            setText(traceItem.getDisplayText());
            setIcon(ClientIcon.TraceLevel.findEventSeverityIcon(traceItem.getSeverity(), environment));
            getHtml().setCss("white-space", "nowrap");
            this.environment = environment;
            setUserData(traceItem);
            getHtml().markAsChoosable();
            unmarkCurrent();
        }

        public ClientTraceItem getTraceItem() {
            return (ClientTraceItem) getUserData();
        }

        public void markCurrent() {
            setForeground(Color.WHITE);
            getHtml().addClass("rwt-ui-selected-item");
        }

        public void unmarkCurrent() {
            setForeground(getListBoxItemColor(getTraceItem().getSeverity()));
            getHtml().removeClass("rwt-ui-selected-item");
        }

        private static Color getListBoxItemColor(final EEventSeverity severity) {
            switch (severity) {
                case EVENT:
                    return Color.BLUE;
                case WARNING:
                    return WARNING_COLOR;
                case ERROR:
                    return Color.RED;
                case ALARM:
                    return ALARM_COLOR;
                default:
                    return Color.BLACK;
            }
        }

        @Override
        public void processAction(String actionName, String actionParam) {
            if (Events.EVENT_NAME_ONDBLCLICK.equals(actionName)) {
                new TraceMessageDialog(getTraceItem(), environment).execDialog();
            }
            super.processAction(actionName, actionParam);
        }
    }
    private final TableLayout traceItems = new TableLayout();
    private final ButtonBase.ClickHandler currentItemHandler = new ButtonBase.ClickHandler() {
        @Override
        public void onClick(final IButton source) {
            if (source != null) {
                final TraceListItem listItem = (TraceListItem) source;
                if (currentItem == listItem) {//NOPMD
                    new TraceMessageDialog(listItem.getTraceItem(), environment).execDialog();
                } else {
                    setCurrentItem(listItem);
                }
            }
        }
    };
    private final TraceBuffer traceBuffer;
    private final IClientEnvironment environment;
    private TraceListItem currentItem;
    private FindAndReplaceDialog searchDialog = null;
    private RwtAction findAction;
    private RwtAction findNextAction;
    private ValListEditorController<String> severityListWidget;
    private ValIntEditorController maxCountBox;
    private long begTime = -1;
    private long endTime = -1;

    public TraceView(final IClientEnvironment environment, final TraceBuffer traceBuffer) {
        super();
        this.environment = environment;
        this.traceBuffer = traceBuffer;
        setupUi();

        traceBuffer.addTraceBufferListener(new TraceBuffer.TraceBufferListener<ClientTraceItem>() {
            @Override
            public void newItemInBuffer(ClientTraceItem traceItem) {
                if (traceBuffer.getMaxSize() != 0) {
                    addNewItem(traceItem, traceBuffer.getMaxSize());
                }
            }

            @Override
            public void maxSeverityChanged(EEventSeverity eventSeverity) {
                //do not process
            }

            @Override
            public void cleared() {
                clearItems();
            }
        });

        for (ClientTraceItem item : traceBuffer.asList()) {
            if (traceBuffer.getMaxSize() != 0) {
                addNewItem(item, traceBuffer.getMaxSize());
            }
        }
    }

    private void setupUi() {
        final ToolBar actionsPanel = new ToolBar();
        actionsPanel.getHtml().setCss("padding-bottom", "0px");
        actionsPanel.getHtml().setCss("padding-top", "4px");
        actionsPanel.setHeight(24);
        final TableLayout box = new TableLayout();
        TableLayout.Row row = box.addRow();
        final String settingKey = SettingNames.SYSTEM + "/TraceDialog/TraceFilter";
        final int currentLevel = environment.getConfigStore().readInteger(settingKey, 0);
        severityListWidget = new ValListEditorController<>(environment);
        maxCountBox = new ValIntEditorController(environment);
        severityListWidget.setMandatory(true);
        {
            String filterTitle = environment.getMessageProvider().translate("TraceDialog", "Filter");

            WpsIcon icon;
            EEventSeverity severity = EEventSeverity.getForValue((long) currentLevel);
            EditMaskList mask = new EditMaskList();
            final List<TraceProfileTreeController.EventSeverity> eventSeverityItems =
                    TraceProfileTreeController.getEventSeverityItemsByOrder(environment);
            for (TraceProfileTreeController.EventSeverity eventSeverity : eventSeverityItems) {
                if (!Utils.equals(eventSeverity.getValue(), EEventSeverity.NONE.getName())) {
                    icon = (WpsIcon) eventSeverity.getIcon();//gotta do something with that!
                    mask.addItem(eventSeverity.getTitle(), eventSeverity.getValue());
                    try {
                        final EEventSeverity kernelEnum = EEventSeverity.getForName(eventSeverity.getValue());
                        if (kernelEnum.getValue().intValue() == currentLevel) {
                            long s = mask.getItems().size() - 1;
                            severity = EEventSeverity.getForValue(s);
                        }
                    } catch (NoConstItemWithSuchValueError error) {//NOPMD
                    }
                }
            }
            severityListWidget.setEditMask(mask);

            severityListWidget.addValueChangeListener(new ValueEditor.ValueChangeListener<String>() {
                @Override
                public void onValueChanged(String oldValue, String newValue) {
                    filterTrace();
                }
            });
            severityListWidget.setValue(severity.getName());
            severityListWidget.setLabel(filterTitle);
            severityListWidget.setLabelVisible(true);
            row.addCell().add((UIObject) severityListWidget.getValEditor());
            row.addSpace(10);
        }

        {
            String timeFromTitle = environment.getMessageProvider().translate("TraceDialog", "Time: from");
            ValDateTimeEditorController fromDT = new ValDateTimeEditorController(environment);
            ((UIObject) fromDT.getValEditor()).setMinimumWidth(120);
            ((UIObject) fromDT.getValEditor()).setPreferredWidth(150);
            fromDT.setLabelVisible(true);
            fromDT.setLabel(timeFromTitle);
            row.addCell().add(((UIObject) fromDT.getValEditor()));
            row.addSpace(10);
            fromDT.addValueChangeListener(new ValueEditor.ValueChangeListener<Timestamp>() {
                @Override
                public void onValueChanged(Timestamp oldValue, Timestamp newValue) {
                    begTime = newValue != null ? newValue.getTime() : -1;
                    filterTrace();
                }
            });
        }
        {
            String timeToTitle = environment.getMessageProvider().translate("TraceDialog", "to");
            ValDateTimeEditorController toDT = new ValDateTimeEditorController(environment);
            ((UIObject) toDT.getValEditor()).setMinimumWidth(120);
            ((UIObject) toDT.getValEditor()).setPreferredWidth(150);
            toDT.setLabel(timeToTitle);
            toDT.setLabelVisible(true);
            row.addCell().add(((UIObject) toDT.getValEditor()));
            row.addSpace(10);
            toDT.addValueChangeListener(new ValueEditor.ValueChangeListener<Timestamp>() {
                @Override
                public void onValueChanged(Timestamp oldValue, Timestamp newValue) {
                    endTime = newValue != null ? newValue.getTime() : -1;
                    filterTrace();
                }
            });
        }
        {
            final String actionTitle =
                    environment.getMessageProvider().translate("TraceDialog", "Save");
            final RwtAction action =
                    new RwtAction(environment, ClientIcon.CommonOperations.SAVE, actionTitle);
            action.addActionListener(new Action.ActionListener() {
                @Override
                public void triggered(final Action action) {
                    onSaveAction();
                }
            });
            actionsPanel.addAction(action);
        }
        {
            final String actionTitle =
                    environment.getMessageProvider().translate("TraceDialog", "Clear");
            final RwtAction action =
                    new RwtAction(environment, ClientIcon.CommonOperations.CLEAR, actionTitle);
            action.addActionListener(new Action.ActionListener() {
                @Override
                public void triggered(final Action action) {
                    onClearAction();
                }
            });
            actionsPanel.addAction(action);
        }
        {
            final String actionTitle =
                    environment.getMessageProvider().translate("TraceDialog", "Find");
            findAction =
                    new RwtAction(environment, ClientIcon.CommonOperations.FIND, actionTitle);
            findAction.addActionListener(new Action.ActionListener() {
                @Override
                public void triggered(final Action action) {
                    if (searchDialog == null) {
                        searchDialog = new FindAndReplaceDialog(environment, environment.getMessageProvider().translate("TraceDialog", "Find"), false);
                        searchDialog.addFindActionListener(new IFindAndReplaceDialog.IFindActionListener() {
                            @Override
                            public void find() {
                                searchDialog.acceptDialog();
                            }
                        });
                    }
                    if (searchDialog.execDialog() == IDialog.DialogResult.ACCEPTED) {
                        if (searchString()) {
                            findNextAction.setEnabled(true);
                        } else {
                            findAction.trigger();
                        }
                    }
                }
            });
            findAction.setEnabled(false);
            actionsPanel.addAction(findAction);
        }
        {
            final String actionTitle =
                    environment.getMessageProvider().translate("TraceDialog", "Find Next");
            findNextAction =
                    new RwtAction(environment, ClientIcon.CommonOperations.FIND_NEXT, actionTitle);
            findNextAction.addActionListener(new Action.ActionListener() {
                @Override
                public void triggered(final Action action) {
                    if (searchDialog != null && searchDialog.getFindWhat() != null && !searchDialog.getFindWhat().isEmpty()) {
                        searchString();
                    }
                }
            });
            findNextAction.setEnabled(false);
            actionsPanel.addAction(findNextAction);
        }
        row.addCell().add(actionsPanel);
        add(box);

        final AbstractContainer traceItemsContainter = new AbstractContainer();
        traceItemsContainter.add(traceItems);
        traceItems.getHtml().setCss("overflow-y", "hidden");
        traceItemsContainter.setBackground(Color.WHITE);
        traceItemsContainter.getHtml().setCss("overflow-y", "auto");
        traceItemsContainter.getHtml().setCss("overflow-x", "scroll");
        add(traceItemsContainter);
        setAutoSize(traceItemsContainter, true);
        traceItemsContainter.getAnchors().setBottom(new Anchors.Anchor(1, -10));
        traceItemsContainter.getAnchors().setRight(new Anchors.Anchor(1, 0));
        setTop(2);
        getAnchors().setBottom(new Anchors.Anchor(1, -4));
        getAnchors().setRight(new Anchors.Anchor(1, -2));
        getHtml().setCss("height", "100%");
    }

    public void addMaxItemsCountBox(Dialog dialog) {
        dialog.getBottomContainer().add((UIObject) maxCountBox.getValEditor());
        ((UIObject) maxCountBox.getValEditor()).setWidth(200);
        ((UIObject) maxCountBox.getValEditor()).getHtml().setCss("padding", "4px 10px");
        maxCountBox.setLabel(environment.getMessageProvider().translate("TraceDialog", "Keep items: "));
        maxCountBox.setLabelVisible(true);
        maxCountBox.getEditMask().setMaxValue(1000);
        maxCountBox.getEditMask().setMinValue(-1);
        maxCountBox.addValueChangeListener(new ValueEditor.ValueChangeListener<Long>() {
            @Override
            public void onValueChanged(Long oldValue, Long newValue) {
                checkTraceItemCount();
            }
        });
        long initMaxSizeItems = environment.getConfigStore().readInteger(SettingNames.SYSTEM + "/TraceDialog/MaxItemCount", 500);
        maxCountBox.setValue(initMaxSizeItems);
    }

    private void filterTrace() {
        checkTraceItemCount();
        filterBySeverity();
    }

    private void filterBySeverity() {
        if (maxCountBox.getValue() != null) {
            for (int i = 0; i < traceItems.getRowCount(); ++i) {
                boolean visible = isAllowed(getItem(i).getTraceItem());
                getItem(i).setVisible(visible);
            }
        }
    }

    private void checkTraceItemCount() {
        if (maxCountBox.getValue() != null) {
            int val = maxCountBox.getValue().intValue();
            if (getEnvironment().getTracer().getBuffer().getMaxSize() != val) {
                getEnvironment().getTracer().getBuffer().setMaxSize(val);
            }

            if (val >= 0 && traceItems.getRowCount() >= val) {
                final int cnt = traceItems.getRowCount() - val;
                for (int i = 0; i < cnt; ++i) {
                    traceItems.removeRow(traceItems.getRow(traceItems.getRowCount() - 1));
                }
            }
                }
            }

    public boolean isAllowed(final ClientTraceItem traceItem) {
        if (traceItem.getSeverity().getValue() < getSeverity().getValue()) {
            return false;
        }
        if ((begTime != -1 && traceItem.getTime() < begTime) || (endTime != -1 && traceItem.getTime() > endTime)) {
            return false;
        }
        return true;
    }

    public EEventSeverity getSeverity() {
        if (severityListWidget.getValue() != null) {
            try {
                return EEventSeverity.getForName(severityListWidget.getValue());
            } catch (NoConstItemWithSuchValueError error) {//NOPMD
                return EEventSeverity.DEBUG;
            }
        }
        return EEventSeverity.DEBUG;
    }

    private boolean searchString() {
        final String stringToFind = searchDialog.getFindWhat();
        final boolean searchForward = searchDialog.isForwardChecked();
        if (stringToFind != null && !stringToFind.isEmpty()) {
            TraceListItem traceItem;
            for (int row = getNextRowToSearch(findCurrentRow(), searchForward); row >= 0; row = getNextRowToSearch(row, searchForward)) {
                traceItem = getItem(row);
                if (searchDialog.match(traceItem.getTraceItem().getMessageText())) {
                    setCurrentItem(traceItem);
                    return true;
                }
            }
            final String title = environment.getMessageProvider().translate("TraceDialog", "Information");
            final String message = environment.getMessageProvider().translate("TraceDialog", "String \'%s\' not found");
            environment.messageInformation(title, String.format(message, stringToFind));
        }
        return false;
    }

    private int getNextRowToSearch(final int previousRow, final boolean isForward) {
        final int rowsCount = traceItems.getRowCount();
        int nextRow = isForward ? previousRow + 1 : previousRow - 1;
        while (nextRow >= 0 && nextRow < rowsCount) {
            if (traceItems.getRow(nextRow).isVisible()) {
                return nextRow;
            }
            nextRow = isForward ? nextRow + 1 : nextRow - 1;
        }
        return -1;
    }

    private int getFirstVisibleRow() {
        for (int i = 0, count = traceItems.getRowCount(); i < count; i++) {
            if (traceItems.getRow(i).isVisible()) {
                return i;
            }
        }
        return -1;
    }

    private int getLastVisibleRow() {
        for (int i = traceItems.getRowCount() - 1; i >= 0; i--) {
            if (traceItems.getRow(i).isVisible()) {
                return i;
            }
        }
        return -1;
    }

    private void addNewItem(final ClientTraceItem traceItem, final int maxItems) {
        boolean currentItemRemoved = false;
        int i = 0;
        if(maxItems>=0)
        while (traceItems.getRowCount() > 0 && traceItems.getRowCount() + 1 > maxItems) {
            if (currentItem == getItem(i)) {
                currentItemRemoved = true;
            }
            traceItems.removeRow(traceItems.getRow(i));
            i++;
        }
        if (maxItems != 0) {
            final TraceListItem listItem = new TraceListItem(traceItem, environment);
            listItem.addClickHandler(currentItemHandler);
            TableLayout.Row.Cell cell = traceItems.addRow(0).addCell();
            cell.add(listItem);
            listItem.setVisible(isAllowed(traceItem));
            cell.getHtml().setCss("height", "21px");
        }
        if (traceItems.getRowCount() > 0) {
            int currentRow = -1;
            if (currentItemRemoved) {
                currentRow = getLastVisibleRow();
            } else if (currentItem == null) {
                findAction.setEnabled(true);
                currentRow = getFirstVisibleRow();
            }
            if (currentRow > 0) {
                setCurrentItem(currentRow);
            }
        }
    }

    private TraceListItem getItem(final int row) {
        return (TraceListItem) traceItems.getCell(row, 0).getChildren().get(0);
    }

    private int findCurrentRow() {
        if (currentItem == null) {
            return -1;
        }
        for (int i = 0, count = traceItems.getRowCount() - 1; i < count; i++) {
            if (getItem(i) == currentItem) {
                return i;
            }
        }
        return -1;
    }

    private void clearItems() {
        currentItem = null;
        traceItems.clearRows();
    }

    private void setCurrentItem(final TraceListItem item) {
        if (currentItem != null) {
            currentItem.unmarkCurrent();
        }
        currentItem = item;
        if (currentItem != null) {
            currentItem.markCurrent();
        }
    }

    private void setCurrentItem(final int row) {
        setCurrentItem(getItem(row));
    }

    private void onSaveAction() {
        final File tmpFile;
        try {
            tmpFile = File.createTempFile("radix_client_trace", null);
            tmpFile.deleteOnExit();
        } catch (IOException exception) {
            final String title =
                    getEnvironment().getMessageProvider().translate("ExplorerError", "Input/Output Exception");
            final String message =
                    getEnvironment().getMessageProvider().translate("TraceDialog", "Could not open file");
            getEnvironment().messageException(title, message, exception);
            getEnvironment().getTracer().error(title, exception);
            return;
        }
        try (FileWriter fileWriter = new FileWriter(tmpFile)) {
            for (int i = 0, count = traceItems.getRowCount(); i < count; i++) {
                if (traceItems.getRow(i).isVisible()) {
                    final TraceListItem listItem =
                            (TraceListItem) traceItems.getRow(i).getCell(0).getChildren().get(0);
                    fileWriter.write(listItem.getTraceItem().toString() + "\r\n");
                }
            }
            final String title =
                    getEnvironment().getMessageProvider().translate("TraceDialog", "Save to file");
            ((WpsEnvironment) getEnvironment()).sendFileToTerminal(title, tmpFile, "Text", false);
        } catch (IOException exception) {
            final String title =
                    getEnvironment().getMessageProvider().translate("ExplorerError", "Input/Output Exception");
            final String message =
                    getEnvironment().getMessageProvider().translate("ExplorerError", "Can't write file '%s'");
            final String formattedMessage = String.format(message, tmpFile.getAbsolutePath());
            getEnvironment().messageException(title, formattedMessage, exception);
            getEnvironment().getTracer().error(formattedMessage, exception);
        }
    }

    private void onClearAction() {
        traceBuffer.clear();//it calls clearItems()
    }
}