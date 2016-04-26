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
package org.radixware.wps.rwt;

import org.radixware.kernel.common.html.Html;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;
import org.radixware.kernel.common.client.IClientApplication;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.dialogs.IMessageBox;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.trace.ClientTraceParser;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.utils.ClientValueFormatter;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.client.widgets.IButton.ClickHandler;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.client.widgets.actions.Action;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.enums.EDialogIconType;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.html.Div;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.dialogs.IDialogDisplayer;
import org.radixware.wps.icons.WpsIcon;
import org.radixware.wps.views.RwtAction;

public class MessageBox extends Dialog implements IMessageBox {

    private String optionText;
    private int optionTextRow = -1;
    private final int linesInMessage;
    private String messagetext;
    private UIObject content;
    private int maxContentHeight;
    
    private final GridBoxContainer mainLayout = new GridBoxContainer();    
    private final CheckBox option = new CheckBox();
    private final int textColumn;

    public static MessageBox exceptionBox(IDialogDisplayer displayer, String message) {
        return exceptionBox(displayer, displayer.getEnvironment().getMessageProvider().translate("MessageBox", "Error"), message);
    }

    public static MessageBox exceptionBox(IDialogDisplayer displayer, Throwable exception) {
        return exceptionBox(displayer, displayer.getEnvironment().getMessageProvider().translate("MessageBox", "Error"), exception);
    }

    public static MessageBox exceptionBox(IDialogDisplayer displayer, String title, String message) {
        Set<EDialogButtonType> b = EnumSet.of(EDialogButtonType.CLOSE);
        MessageBox box = new MessageBox(displayer, title, message, null, b, EDialogIconType.CRITICAL);
        return box;
    }

    public static MessageBox exceptionBox(IDialogDisplayer displayer, String title, String message, String detailsTitle, String details) {
        Set<EDialogButtonType> b = EnumSet.of(EDialogButtonType.CLOSE);

        MessageBox box = new MessageBox(displayer, title, message, detailsTitle, details, b, EDialogIconType.CRITICAL);
        return box;
    }

    public static MessageBox confirmation(IDialogDisplayer displayer, String message) {
        return confirmation(displayer, displayer.getEnvironment().getMessageProvider().translate("MessageBox", "Confirmation"), message);
    }

    public static MessageBox confirmation(IDialogDisplayer displayer, String title, String message) {
        Set<EDialogButtonType> b = EnumSet.of(EDialogButtonType.YES, EDialogButtonType.NO);

        MessageBox box = new MessageBox(displayer, title, message, null, b, EDialogIconType.QUESTION);
        return box;
    }

    public static MessageBox information(IDialogDisplayer displayer, String title, String message) {
        Set<EDialogButtonType> b = EnumSet.of(EDialogButtonType.CLOSE);
        MessageBox box = new MessageBox(displayer, title, message, null, b, EDialogIconType.INFORMATION);
        return box;
    }

    public static MessageBox information(String title, String message) {
        return information(((WpsEnvironment) getEnvironmentStatic()).getDialogDisplayer(), title, message);
    }

    public static MessageBox warning(IDialogDisplayer displayer, String title, String message) {
        Set<EDialogButtonType> b = EnumSet.of(EDialogButtonType.CLOSE);
        MessageBox box = new MessageBox(displayer, title, message, null, b, EDialogIconType.WARNING);
        return box;
    }

    public static MessageBox warning(IDialogDisplayer displayer, String message) {

        Set<EDialogButtonType> b = EnumSet.of(EDialogButtonType.CLOSE);
        MessageBox box = new MessageBox(displayer, displayer.getEnvironment().getMessageProvider().translate("MessageBox", "Warning"), message, null, b, EDialogIconType.WARNING);
        return box;
    }

    private static void exceptionStackToString(Throwable ex, StringBuilder sb) {
        sb.append(ex.getClass().getName()).append('\n');

        for (StackTraceElement e : ex.getStackTrace()) {
            sb.append("at ").append(e.toString()).append('\n');
        }
        if (ex.getCause() != null) {
            sb.append("Caused by ");
            exceptionStackToString(ex.getCause(), sb);
        }
    }

    public static MessageBox exceptionBox(IDialogDisplayer displayer, String title, Throwable exception) {
        StringBuilder trace = new StringBuilder();
        exceptionStackToString(exception, trace);

        Set<EDialogButtonType> buttons = EnumSet.of(EDialogButtonType.CLOSE);

        MessageBox messageBox = new MessageBox(displayer, title, exception.getMessage(), trace.toString(), buttons, EDialogIconType.CRITICAL);

        return messageBox;
    }
    private EDialogButtonType pressedButton = null;
    private final CloseActionHandler handler = new CloseActionHandler() {
        @Override
        public boolean canClose(String action, Object data) {
            return true;
        }

        @Override
        public void closed(String action, Object data) {
            if (data instanceof EDialogButtonType) {
                pressedButton = (EDialogButtonType) data;
            }
        }
    };

    public MessageBox(String title, String message, String details, Set<EDialogButtonType> buttons) {
        this(getEnvironmentStatic(), title, message, details, buttons);
    }

    public MessageBox(String title, String message, String details, Set<EDialogButtonType> buttons, EDialogIconType icon) {
        this(getEnvironmentStatic(), title, message, details, buttons, icon);
    }

    public MessageBox(String title, String message, Set<EDialogButtonType> buttons, EDialogIconType icon) {
        this(getEnvironmentStatic(), title, message, null, buttons, icon);
    }

    public MessageBox(String title, String message, String detailsTitle, String details, Set<EDialogButtonType> buttons, EDialogIconType icon) {
        this(getEnvironmentStatic(), title, message, detailsTitle, details, buttons, icon);
    }

    public MessageBox(IClientEnvironment env, String title, String message, String details, Set<EDialogButtonType> buttons) {
        this(((WpsEnvironment) env).getDialogDisplayer(), title, message, details, buttons);
    }

    public MessageBox(IClientEnvironment env, String title, String message, String details, Set<EDialogButtonType> buttons, EDialogIconType icon) {
        this(((WpsEnvironment) env).getDialogDisplayer(), title, message, details, buttons, icon);
    }

    public MessageBox(IClientEnvironment env, String title, String message, String detailsTitle, String details, Set<EDialogButtonType> buttons, EDialogIconType icon) {
        this(((WpsEnvironment) env).getDialogDisplayer(), title, message, detailsTitle, details, buttons, icon);
    }

    public MessageBox(IDialogDisplayer displayer, String title, String message, String details, Set<EDialogButtonType> buttons) {
        this(displayer, title, message, details, buttons, null);
    }

    public MessageBox(IDialogDisplayer displayer, String title, String message, String details, Set<EDialogButtonType> buttons, EDialogIconType icon) {
        this(displayer, title, message, null, details, buttons, icon);
    }
    private final ClientTraceParser traceParser;

    public MessageBox(IDialogDisplayer displayer, String title, String message, final String detailsTitle, final String details, Set<EDialogButtonType> buttons, EDialogIconType icon) {
        super(displayer, ClientValueFormatter.capitalizeIfNecessary(displayer.getEnvironment(), title), false);        
        /*
               mainLayout:
            ?
        ----------------------------
        |spacer |                  |
        | ICON  |                  |
        |       |       TEXT       |
        |       |                  |
        |       |                  |
        ----------------------------
        |       | details button   | ?
        ----------------------------
        |       | option check box | ?
        ----------------------------
        |       |      spacer      |
        ----------------------------
        */
        final WpsEnvironment environment = displayer.getEnvironment();
        final MessageProvider mp = environment.getMessageProvider();
        final IClientApplication application = environment.getApplication();
        traceParser = new ClientTraceParser(displayer.getEnvironment());
        setWidth(20);
        setAutoWidth(true);
        setMaxWidth(640);
        setAutoHeight(true);
        setMaxHeight(480);
        WpsIcon iconImage;        
        if (icon==null){
            iconImage = null;            
        }else{
            try{
                switch(icon){
                    case CRITICAL:
                        iconImage = (WpsIcon) application.getImageManager().getIcon(ClientIcon.Message.ERROR);
                        break;
                    case QUESTION:
                        iconImage = (WpsIcon) application.getImageManager().getIcon(ClientIcon.Message.CONFIRMATION);
                        break;
                    case WARNING:
                        iconImage = (WpsIcon) application.getImageManager().getIcon(ClientIcon.Message.WARNING);
                        break;
                    default:
                        iconImage = (WpsIcon) application.getImageManager().getIcon(ClientIcon.Message.INFORMATION);                
                }
            }catch(DefinitionError e){
                iconImage = null;
            }            
        }        
        final UIObject spacer;
        if (iconImage == null) {
            textColumn = 0;
            spacer = null;
        }else{
            textColumn = 1;
            final Image image = new Image();
            image.setWidth(30);
            image.setHeight(30);
            image.setIcon(iconImage);
            spacer = new UIObject(new Div());
            spacer.setHeight(20);
            spacer.getHtml().setCss("min-height", "20px");
            spacer.getHtml().setCss("max-height", "20px");
            mainLayout.add(spacer, 0, 0);
            mainLayout.add(image, 0, 0);
            mainLayout.setCellVerticalAlign(0, 0, Alignment.TOP);
        }
        message = (message == null || message.isEmpty()) ? getMessageText() : message;
        if (isTextHtml(message)) {
            final HtmlText htmlText = new HtmlText();
            htmlText.setHtmlText(message);
            linesInMessage = -1;
            mainLayout.add(htmlText, 0, textColumn);   
            content = htmlText;
        } else {
            final StaticText messageText = new StaticText();            
            messageText.setText(message);
            if (messageText.getLinesCount()<3 && spacer!=null){
                spacer.setVisible(false);
            }            
            linesInMessage = messageText.getLinesCount();
            messageText.setSizePolicy(SizePolicy.MINIMUM_EXPAND, SizePolicy.MINIMUM_EXPAND);
            mainLayout.add(messageText, 0, textColumn);
            content = messageText;            
        }
        setContentMaxHeight(400);        
        if (details != null) {            
            final HrefButton button = new HrefButton(mp.translate("ExceptionDialog", "show details"));
            button.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(IButton source) {
                    final Dialog exceptionDialog = 
                        new Dialog(environment, mp.translate("ExceptionDialog", "Exceptions Details"));

                    final ToolBar toolBar = new ToolBar();
                    exceptionDialog.add(toolBar);

                    final RwtAction translateAction = 
                        new RwtAction(application.getImageManager().getIcon(ClientIcon.CommonOperations.TRANSLATE), mp.translate("TraceDialog", "&Translate"));
                    translateAction.setEnabled(true);
                    translateAction.setCheckable(true);
                    translateAction.setTextShown(true);
                    toolBar.addAction(translateAction);

                    final TextArea messageText = new TextArea();
                    exceptionDialog.add(messageText);
                    messageText.setLineWrap(false);
                    messageText.setReadOnly(true);

                    messageText.setTop(30);
                    messageText.setLeft(3);
                    messageText.getAnchors().setRight(new Anchors.Anchor(1, -3));
                    messageText.getAnchors().setBottom(new Anchors.Anchor(1, -3));
                    messageText.setText(details);

                    translateAction.addActionListener(new Action.ActionListener() {
                        @Override
                        public void triggered(Action action) {
                            messageText.setText(translateAction.isChecked() ? traceParser.parse(details) : details);
                        }
                    });

                    exceptionDialog.addCloseAction(EDialogButtonType.CLOSE);
                    exceptionDialog.setWidth(400);
                    exceptionDialog.setHeight(300);
                    exceptionDialog.html.setAttr("dlgId", "exception_details");
                    exceptionDialog.execDialog();
                }
            });            
            mainLayout.add(button, 1, textColumn, Alignment.CENTER);
            setContentMaxHeight(maxContentHeight-25);
        }
        
        add(mainLayout);
        mainLayout.setTop(10);
        mainLayout.setLeft(10);
        mainLayout.unsetPosition();
        mainLayout.setColummnExpand(textColumn, 100);
        //mainLayout.getAnchors().setLeft(new Anchors.Anchor(0, 10));
        mainLayout.getAnchors().setRight(new Anchors.Anchor(1, -3));
        //mainLayout.setHSizePolicy(SizePolicy.EXPAND);
        mainLayout.getHtml().setCss("display", "inline-block");//size by inner table
        addSpacer();
        
        if (buttons != null) {
            for (EDialogButtonType button : buttons) {
                addCloseAction(StandardButton.getTitle(button, environment), IMessageBox.StandardButton.getDialogResult(button), handler, button);
            }
        }

        setupDefaultButtons(buttons);
        updateContentMaxWidth();
    }
    
    private void setContentMaxHeight(final int maxHeight){
        content.getHtml().setCss("max-height", String.valueOf(maxHeight)+"px");
        maxContentHeight = maxHeight;
    }
    
    private void updateContentMaxWidth(){
        final boolean hasIcon = mainLayout.getCellCount(0)>1;
        int maxWidth = hasIcon ? 599 : 635;
        if (linesInMessage>0){            
            final int addintionalRows = mainLayout.getRowCount() - 2;            
            final int maxLines;
            if (addintionalRows==0){
                maxLines = 21;
            }else if (addintionalRows==1){
                maxLines = 20;
            }else if (addintionalRows==2){
                maxLines = 18;
            }else{
                maxLines = 21 - (int)Math.round((float)addintionalRows*1.5);
            }
            final boolean verticalScrollbarVisible = linesInMessage>maxLines;
            final int verticalScrollBarWidth = 20;
            if (verticalScrollbarVisible && hasIcon){
                this.getHtml().setAttr("addintional-width", String.valueOf(verticalScrollBarWidth));
                maxWidth+=verticalScrollBarWidth;
            }                     
        }
        content.getHtml().setCss("max-width", String.valueOf(maxWidth)+"px");
    }
    
    private void addSpacer(){
        final UIObject spacer = new UIObject(new Div());
        spacer.setMinimumHeight(3);
        spacer.getHtml().setCss("max-height", 3+"px");
        final int spacerRow = mainLayout.getRowCount();
        mainLayout.add(spacer, spacerRow, 0);
        mainLayout.setRowHeight(spacerRow, 3);
    }

    private boolean isTextHtml(final String str) {
        if (str == null) {
            return false;
        }
        final String testString = str.trim().toLowerCase();
        if (!testString.isEmpty() && testString.charAt(0)=='<') {
            int closeTag = testString.indexOf('>');
            if (closeTag > 1 && testString.charAt(closeTag - 1) == '/') {
                closeTag--;
            }
            if (closeTag > 1) {
                String tagName = testString.substring(1, closeTag);
                for (int i = 0; i < tagName.length(); i++) {
                    final char c = tagName.charAt(i);
                    if (c == ' ' || c == '\n' || c == '\b' || c == '\r') {
                        if (i > 0) {
                            tagName = tagName.substring(0, i);
                            break;
                        } else {
                            return false;
                        }
                    }
                }
                return Html.TagNames.findTag(tagName) != null;
            }
        }
        return false;
    }

    @Override
    public EDialogButtonType show(final IClientEnvironment env, final String message, final String title, final EDialogIconType icon, final Set<EDialogButtonType> buttons) {
        final MessageBox mb = new MessageBox(((WpsEnvironment) env).getDialogDisplayer(), title, message, null, buttons, icon);
        return mb.execMessageBox();
    }

    @Override
    public void setOptionText(final String text) {
        if (!Objects.equals(optionText,text)){
            if (text==null || text.isEmpty()){
                if (optionTextRow>-1){
                    mainLayout.removeRow(optionTextRow);
                    optionTextRow = -1;
                    setContentMaxHeight(maxContentHeight+25);
                }
            }else{                
                option.setText(text);
                if (optionTextRow<0){
                    final int rowCount = mainLayout.getRowCount();
                    optionTextRow = rowCount-1;
                    mainLayout.removeRow(optionTextRow);//remove spacer
                    mainLayout.add(option, optionTextRow, textColumn);
                    setContentMaxHeight(maxContentHeight-25);
                    addSpacer();
                }
            }
            this.optionText = text;
        } 
        updateContentMaxWidth();
    }
            

    public final void setMessageText(final String text) {
        this.messagetext = text;
    }

    public final String getMessageText() {
        return messagetext;
    }

    @Override
    public String getOptionText() {
        return optionText;
    }

    public PushButton setStandardCloseActionHandler(EDialogButtonType button) {
        return addCloseAction(button, handler);
    }

    @Override
    public boolean isOptionActivated() {
        return optionText!=null && !optionText.isEmpty() && option.isSelected();
    }

    @Override
    public DialogResult execDialog(final IWidget parentWidget) {
        getEnvironment().getProgressHandleManager().blockProgress();
        try{
            return super.execDialog(parentWidget);
        }finally{
            getEnvironment().getProgressHandleManager().unblockProgress();
        }
    }

    @Override
    public EDialogButtonType execMessageBox() {
        execDialog();
        if (pressedButton == null) {
            return EDialogButtonType.CANCEL;
        }
        return pressedButton;
    }

    public EDialogButtonType getPressedButton() {
        return pressedButton;
    }

    @Override
    public void addButton(final EDialogButtonType buttonType, final String title, final Icon icon) {
        final Dialog.DialogResult dlgResult = IMessageBox.StandardButton.getDialogResult(buttonType);
        final PushButton pb = addCloseAction(title, dlgResult, handler, buttonType);
        pb.setIcon(icon);
    }

    @Override
    protected final void setupDefaultButtons(Set<EDialogButtonType> buttons) {
        if (buttons == null || buttons.isEmpty()) {
            setStandardCloseActionHandler(EDialogButtonType.OK);
        } else {
            super.setupDefaultButtons(buttons);
        }
    }

}
