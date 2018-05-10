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

package org.radixware.kernel.explorer.dialogs;

import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QFrame;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QPalette;
import com.trolltech.qt.gui.QScrollArea;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QTextEdit;
import com.trolltech.qt.gui.QTextOption;
import com.trolltech.qt.gui.QToolButton;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import java.awt.Dimension;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.dialogs.IDisplayProblemsDialog;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.exceptions.ExceptionMessage;
import org.radixware.kernel.common.client.exceptions.IProblemHandler;
import org.radixware.kernel.common.client.exceptions.StandardProblemHandler;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.utils.WidgetUtils;


public final class DisplayProblemsDialog extends ExplorerDialog implements IDisplayProblemsDialog{
    
    private static class DisplayProblemWidget extends QWidget{
        private final String exceptionDetails;
        private final IClientEnvironment environment;
                
        public DisplayProblemWidget(final IClientEnvironment environment, final Exception exception, final String title, final QWidget parentWidget){
            super(parentWidget);
            this.environment = environment;
            final ExceptionMessage exceptionMessage = new ExceptionMessage(environment,exception);
            final boolean isError = exceptionMessage.getSeverity().getValue()>EEventSeverity.WARNING.getValue();
            final boolean hasDetails = exceptionMessage.getDetails() != null && !exceptionMessage.getDetails().isEmpty();
            final boolean hasTrace = exceptionMessage.getTrace() != null && !exceptionMessage.getTrace().isEmpty();
            
            if (hasDetails || hasTrace){
                if (hasTrace){
                    exceptionDetails = 
                        exceptionMessage.getDetails() + "\n\nStack trace:\n" + exceptionMessage.getTrace();
                }
                else{
                    exceptionDetails = exceptionMessage.getDetails();
                }
            }
            else{
                exceptionDetails = "";
            }                
            
            if (title!=null && !title.isEmpty()){
                setupUi(title+":\n"+exceptionMessage.getDialogMessage(), isError);
            }
            else{
                setupUi(exceptionMessage.getDialogMessage(), isError);
            }
        }
        
        private void setupUi(final String exceptionMessage, final boolean isError){
            final QVBoxLayout vLayout = new QVBoxLayout();
            setLayout(vLayout);
            
            final QWidget messageWidget = new QWidget(this);            
            messageWidget.setObjectName("exceptionbox_message_widget");
            final QHBoxLayout hLayout = new QHBoxLayout();
            hLayout.setMargin(0);
            messageWidget.setLayout(hLayout);

            final QIcon icon = ExplorerIcon.getQIcon(isError ? ClientIcon.Message.ERROR : ClientIcon.Message.INFORMATION);
            final QLabel iconLabel = new QLabel(this);
            iconLabel.setObjectName("exceptionbox_icon");
            iconLabel.setFixedSize(32, 32);
            iconLabel.setSizePolicy(QSizePolicy.Policy.Fixed, QSizePolicy.Policy.Fixed);
            iconLabel.setPixmap(icon.pixmap(32));
            hLayout.addWidget(iconLabel, 0, new Qt.AlignmentFlag[]{Qt.AlignmentFlag.AlignTop, Qt.AlignmentFlag.AlignLeft});            

            final QTextEdit teMessage = new QTextEdit(messageWidget){
                @Override
                public QSize sizeHint() {
                    final QSize textSize = document().documentLayout().documentSize().toSize();
                    final int height = textSize.height() + 5;
                    final int width = textSize.width() + 5;
                    return new QSize(width, height);
                }                
            };            
            teMessage.setObjectName("exceptionbox_message");
            teMessage.setFrameShape(QFrame.Shape.NoFrame);
            teMessage.setContentsMargins(2, 0, 0, 0);
            final QPalette palette = new QPalette(teMessage.palette());
            palette.setColor(QPalette.ColorRole.Base, palette.color(QPalette.ColorRole.Window));            
            teMessage.setPalette(palette);
            teMessage.setReadOnly(true);
            //teMessage.setFixedHeight(32);
            teMessage.setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Maximum);
            hLayout.addWidget(teMessage, 0, new Qt.AlignmentFlag[]{Qt.AlignmentFlag.AlignTop});
            vLayout.addWidget(messageWidget);
            
            teMessage.setWordWrapMode(QTextOption.WrapMode.NoWrap);
            teMessage.setHorizontalScrollBarPolicy(Qt.ScrollBarPolicy.ScrollBarAlwaysOff);
            teMessage.setVerticalScrollBarPolicy(Qt.ScrollBarPolicy.ScrollBarAlwaysOff);
            teMessage.setText(exceptionMessage);            

            if (exceptionDetails!=null){
                final QToolButton detailedBtn = new QToolButton(this);
                detailedBtn.setObjectName("exceptionbox_detail_button");
                final QFont detailBtnFont = new QFont(detailedBtn.font());
                detailBtnFont.setPointSize(detailBtnFont.pointSize() - 1);
                detailBtnFont.setItalic(true);
                detailedBtn.setFont(detailBtnFont);
                detailedBtn.setToolButtonStyle(Qt.ToolButtonStyle.ToolButtonTextBesideIcon);
                detailedBtn.setAutoRaise(true);
                detailedBtn.setText(environment.getMessageProvider().translate("ExceptionDialog", "show details"));            
                detailedBtn.clicked.connect(this, "showDetails()");
                vLayout.addWidget(detailedBtn, 0, new Qt.AlignmentFlag[]{Qt.AlignmentFlag.AlignRight});
            }
            
            teMessage.document().adjustSize();
            teMessage.adjustSize();
            adjustSize();
        }
        
        @SuppressWarnings("unused")
        private void showDetails(){
            final ExceptionDetail dialog = new ExceptionDetail(environment,this);
            dialog.setMessage(exceptionDetails);
            dialog.exec();
        }
    }
    
    private static class ProblemsContainer extends QWidget{        
        private int widthHint = 200;
        
        public ProblemsContainer(){
            super();
            final QVBoxLayout contentLayout = new QVBoxLayout(this);
            contentLayout.setContentsMargins(0, 0, 0, 0);
        }
        
        public void addDisplayProblemWidget(final DisplayProblemWidget problemWidget){
            layout().addWidget(problemWidget);
            widthHint = Math.max(widthHint, problemWidget.sizeHint().width());
        }

        @Override
        public QSize sizeHint() {
            return new QSize(widthHint, super.sizeHint().height());
        }        
    }
    
    @SuppressWarnings("LeakingThisInConstructor")
    public DisplayProblemsDialog(final IClientEnvironment environment, final QWidget parentWidget, final StandardProblemHandler problemHandler){
        super(environment,parentWidget);
        setWindowIcon(ExplorerIcon.getQIcon(ClientIcon.Message.ERROR));
        
        final ProblemsContainer content = new ProblemsContainer();
        String problemTitle;
        IProblemHandler.ProblemSource problemSource;
        for (IProblemHandler.Problem problem: problemHandler){
            problemSource = problemHandler.getProblemSource(problem);
            problemTitle = problemSource.getOperationDescription();
            if (problemTitle!=null && !problemTitle.isEmpty() && 
                problemSource.getSourceDescription()!=null && 
                !problemSource.getSourceDescription().isEmpty()){
                problemTitle+=" "+problemSource.getSourceDescription();//NOPMD
            }
            content.addDisplayProblemWidget(new DisplayProblemWidget(environment, problem.getException(), problemTitle, content));//NOPMD
        }
        final QScrollArea saContent = new QScrollArea(){

            @Override
            public QSize sizeHint() {
                if (widget()==null){
                    return super.sizeHint();
                }
                else{
                    return widget().sizeHint();
                }
            }            
        };
        dialogLayout().addWidget(saContent);
        saContent.setWidget(content);
        saContent.setWidgetResizable(true);
        addButton(EDialogButtonType.CLOSE).setDefault(true);
        rejectButtonClick.connect(this, "close()");
        
        content.adjustSize();
        
        final QSize sizeHint = sizeHint();//NOPMD 
        final Dimension hardLimitSize = WidgetUtils.getWndowMaxSize();        
        final int heightHardLimit = hardLimitSize.height;
        final int widthHardLimit = hardLimitSize.width;
        
        final int height,width;                
        if (sizeHint.width()>widthHardLimit){
            saContent.setHorizontalScrollBarPolicy(Qt.ScrollBarPolicy.ScrollBarAlwaysOn);
            setMaximumWidth(sizeHint.width());
            width = widthHardLimit;
        }
        else{
            saContent.setHorizontalScrollBarPolicy(Qt.ScrollBarPolicy.ScrollBarAlwaysOff);
            width = sizeHint.width();
            setFixedWidth(width);
            
        }
        
        if (sizeHint.height()>heightHardLimit){
            saContent.setHorizontalScrollBarPolicy(Qt.ScrollBarPolicy.ScrollBarAlwaysOn);
            height = heightHardLimit;
        }
        else{
            saContent.setVerticalScrollBarPolicy(Qt.ScrollBarPolicy.ScrollBarAlwaysOff);
            height = sizeHint.height();
            setFixedHeight(height);
        }                
        
        if (width!=sizeHint.width() || height!=sizeHint.height()){
            resize(width, height);
        }        
    }
}