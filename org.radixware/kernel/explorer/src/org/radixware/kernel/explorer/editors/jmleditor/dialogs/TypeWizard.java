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
package org.radixware.kernel.explorer.editors.jmleditor.dialogs;

import com.trolltech.qt.core.QAbstractItemModel;
import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.QTimer;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.core.Qt.ItemDataRole;
import com.trolltech.qt.gui.QAbstractItemView;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QCloseEvent;
import com.trolltech.qt.gui.QComboBox;
import com.trolltech.qt.gui.QGroupBox;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QHeaderView;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QItemDelegate;
import com.trolltech.qt.gui.QKeyEvent;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QListWidget;
import com.trolltech.qt.gui.QListWidgetItem;
import com.trolltech.qt.gui.QMouseEvent;
import com.trolltech.qt.gui.QRadioButton;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QSpinBox;
import com.trolltech.qt.gui.QTableWidget;
import com.trolltech.qt.gui.QTableWidgetItem;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import com.trolltech.qt.gui.QWizardPage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import org.radixware.kernel.common.client.meta.mask.EditMaskStr;
import org.radixware.kernel.common.compiler.core.lookup.LookupUtils;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjectIcon;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.IEnvDependent;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.msdl.AdsMsdlSchemeDef;
import org.radixware.kernel.common.defs.ads.platform.PlatformLib;
import org.radixware.kernel.common.defs.ads.platform.RadixPlatformClass;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration.TypeArgument;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration.TypeArgument.Derivation;
import org.radixware.kernel.common.defs.ads.type.IAdsTypeSource;
import org.radixware.kernel.common.defs.ads.userfunc.AdsUserFuncDef;
import org.radixware.kernel.common.defs.ads.userfunc.AdsUserFuncDef.Lookup;
import org.radixware.kernel.common.defs.ads.userfunc.AdsUserFuncDef.Lookup.DefInfo;
import org.radixware.kernel.common.defs.ads.xml.AdsXmlSchemeDef;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.repository.ads.AdsSegment;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.SystemTools;
import org.radixware.kernel.explorer.editors.jmleditor.JmlEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValStrEditor;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.env.ExplorerSettings;
import org.radixware.kernel.explorer.env.KernelIcon;

public class TypeWizard extends BaseWizard implements IChooseDefFromList {

    private final JmlEditor editor;
    private RadixObject curItem;
    private RadixObject curDef;
    private EValType valType = null;
    private final Set<EDefType> selectedTypes;

    private enum TypeKind {

        ArrType, JavaArray, RadixType, JavaClassType, SimpleType
    };
    private boolean isArrType;
    private boolean isJavaArray = false;
    private boolean isRadixTypes;
    private boolean isJavaClassTypes;
    private int dimension = 1;
    private static final int CHOOSE_TYPE_PAGE = 0;
    private static final int CHOOSE_RADIX_TYPE_PAGE = 1;
    private static final int CHOOSE_DEF_PAGE = 2;
    private static final int CHOOSE_SCHEME_PAGE = 3;
    private static final int CHOOSE_SIMPLE_TYPE_PAGE = 4;
    private static final int CHOOSE_ARRAY_TYPE_PAGE = 5;
    private static final int CHOOSE_JAVA_CLASS_PAGE = 6;
    private static final int GENERIC_ARGUMENTS_PAGE = 7;
    private ChooseJavaClassPage javaClassPage;
    private ChooseGenericAtributesPage genericAtributesPage;
    private final QTimer doubleClickTimer = new QTimer(this);
    private boolean isDoubleClickPressed = false;

    public TypeWizard(final JmlEditor parent, final boolean isForPrifile) {
        super(parent, (ExplorerSettings) parent.getEnvironment().getConfigStore(), "TypeWizard");
        //this.isForPrifile=isForPrifile;
        selectedTypes = EnumSet.noneOf(EDefType.class);
        editor = parent;
        if (isForPrifile) {
            setPage(CHOOSE_TYPE_PAGE, new ChooseTypeKindPage(this, false));
            setPage(CHOOSE_SIMPLE_TYPE_PAGE, new ChooseTypePage(this, false));
            setPage(CHOOSE_ARRAY_TYPE_PAGE, new ChooseTypeKindPage(this, true));
        }
        setPage(CHOOSE_RADIX_TYPE_PAGE, new ChooseTypePage(this, true));
        setPage(CHOOSE_DEF_PAGE, new ChooseDefPage(this));
        setPage(CHOOSE_SCHEME_PAGE, new ChooseSchemaTypePage(this));
        javaClassPage = new ChooseJavaClassPage(this);
        setPage(CHOOSE_JAVA_CLASS_PAGE, javaClassPage);
        genericAtributesPage = new ChooseGenericAtributesPage(this);
        setPage(GENERIC_ARGUMENTS_PAGE, genericAtributesPage);

        this.button(WizardButton.NextButton).setEnabled(false);
        this.button(WizardButton.BackButton).released.connect(this, "btnBackWasClicked()");
        setWindowTitle(Application.translate("JmlEditor", "Type Selection Wizard"));
        
        doubleClickTimer.setSingleShot(true);
        doubleClickTimer.setInterval(300);
        doubleClickTimer.timeout.connect(this, "resetDoubleClickFlag()");
    }
    
    @SuppressWarnings("unused")
    private void resetDoubleClickFlag() {
        isDoubleClickPressed = false;
    }

    @SuppressWarnings("unused")
    private void btnBackWasClicked() {
        curItem = curDef;
    }

    public AdsTypeDeclaration calcType() {
        final AdsTypeDeclaration type;
        final EValType valTypeDef = getValTypeDef();
        final RadixObject obj = getSelectedDef();

        if (obj instanceof AdsTypeDeclaration) {
            type = (AdsTypeDeclaration) obj;
        } else {
            if (getDimension() > 0) {
                type = AdsTypeDeclaration.Factory.newInstance(valTypeDef, (IAdsTypeSource) obj, null, getDimension());
            } else {
                if (valTypeDef == null) {
                    type = AdsTypeDeclaration.Factory.newInstance((IAdsTypeSource) obj);
                } else {
                    type = AdsTypeDeclaration.Factory.newInstance(valTypeDef, (IAdsTypeSource) obj, null, 0);
                }
            }
        }
        return type;
    }

    public RadixObject getSelectedDef() {
        return curItem;
    }

    public int getDimension() {
        return isJavaArray ? dimension : 0;
    }

    public EValType getValTypeDef() {
        if (isArrType) {
            if (curItem instanceof AdsEnumDef) {
                return ((AdsEnumDef) curItem).getItemType() == EValType.STR ? EValType.ARR_STR : EValType.ARR_INT;
            }
            return EValType.ARR_REF;
        } else {
            return valType;
        }
    }
    
    
    
    @Override
    public void onItemClick(final QModelIndex modelIndex) {
        if (isDoubleClickPressed) {
            return;
        }
        setCurItem(modelIndex);
    }

    @Override
    public void onItemDoubleClick(final QModelIndex modelIndex) {
        isDoubleClickPressed = true;
        doubleClickTimer.start();
        final boolean hasNextPage = this.button(WizardButton.NextButton).isEnabled() && this.button(WizardButton.NextButton).isVisible();
        if (setCurItem(modelIndex) && !hasNextPage) {
            accept();
        } else if (hasNextPage) {
            TypeWizard.this.next();
        }
    }

    @Override
    public boolean setCurItem(final QModelIndex modelIndex) {
        if (modelIndex != null) {
            if (modelIndex.model() instanceof ListModel) {
                final DefInfo di = ((ListModel) modelIndex.model()).getDefList().get(modelIndex.row());
                if (isJavaClassTypes) {
                    final String strType = di.getPath()[0].toString();//(String)modelIndex.data(ItemDataRole.UserRole);
                    if (getDimension() > 0) {
                        curItem = AdsTypeDeclaration.Factory.newInstance(EValType.JAVA_TYPE, null, strType, getDimension());
                    } else {
                        curItem = AdsTypeDeclaration.Factory.newPlatformClass(strType);
                    }
                    javaClassPage.updateBtnsVisible();
                } else if (di != null) {
                    curItem = di.getDefinition();
                    if (curItem == null) {
                        curItem = Lookup.findTopLevelDefinition(editor.getUserFunc(), di.getPath()[0]);
                    }
                    curDef = curItem;
                }
            } else if (modelIndex.model() instanceof ListModelForRadixObj) {
                curItem = ((ListModelForRadixObj) modelIndex.model()).getDefList().get(modelIndex.row());
            } else {
                if (isRadixTypes) {
                    final String strType = (String) modelIndex.data(ItemDataRole.DisplayRole);
                    valType = EValType.getForName(strType);
                    curItem = null;
                } else {
                    final String strType = (String) modelIndex.data(ItemDataRole.UserRole);
                    if (getDimension() > 0) {
                        curItem = AdsTypeDeclaration.Factory.newInstance(EValType.JAVA_TYPE, null, strType, getDimension());
                    } else {
                        curItem = AdsTypeDeclaration.Factory.newPrimitiveType(strType);
                    }
                }
            }
            this.button(WizardButton.NextButton).setEnabled(true);
            this.button(WizardButton.FinishButton).setEnabled(true);
            if (curItem instanceof AdsXmlSchemeDef || curItem instanceof AdsMsdlSchemeDef) {
                return false;
            }
            return true;
        }
        this.button(WizardButton.NextButton).setEnabled(false);
        this.button(WizardButton.FinishButton).setEnabled(false);
        return false;
    }

    @Override
    public boolean setSelectedDefinition(AdsDefinition def) {
        return false;
    }

    private class ChooseTypeKindPage extends QWizardPage {

        private QSpinBox sbDimension;
        private final QLabel lbDimension;
        private final boolean isForArray;

        public ChooseTypeKindPage(final TypeWizard parent, final boolean isForArray) {
            super(parent);
            this.isForArray = isForArray;
            final QVBoxLayout layout = new QVBoxLayout();
            this.setObjectName("ChooseTypeKindPage");
            this.setTitle(Application.translate("JmlEditor", "Select Type"));
            final QGroupBox groupBox = new QGroupBox("");
            groupBox.setObjectName("groupBox");

            final RadioButton rbRadixTypes = new RadioButton(Application.translate("JmlEditor", "Radix Type"));
            rbRadixTypes.setObjectName("rbRadixTypes");
            rbRadixTypes.clicked.connect(this, "radixTypesSelected()");

            final RadioButton rbSimpleTypes = new RadioButton(Application.translate("JmlEditor", "Primitive Type"));
            rbSimpleTypes.setObjectName("rbSimpleTypes");
            rbSimpleTypes.clicked.connect(this, "simpleTypesSelected()");

            final RadioButton rbJavaClassTypes = new RadioButton(Application.translate("JmlEditor", "Java Class"));
            rbJavaClassTypes.setObjectName("rbJavaClassTypes");
            rbJavaClassTypes.clicked.connect(this, "javaClassSelected()");

            final RadioButton rbArrayTypes = new RadioButton(Application.translate("JmlEditor", "Array"));
            rbArrayTypes.setObjectName("rbArrayTypes");
            rbArrayTypes.clicked.connect(this, "arrayTypesSelected(boolean)");

            final QHBoxLayout arrayDimensionLayout = new QHBoxLayout();
            arrayDimensionLayout.setContentsMargins(20, 0, 0, 0);
            lbDimension = new QLabel(Application.translate("JmlEditor", "Dimension") + ":");
            //QSizePolicy.PolicyFlag.ExpandFlag
            //lbDimension.setSizePolicy(null);
            sbDimension = new QSpinBox();
            sbDimension.setMinimum(1);
            sbDimension.setValue(1);
            sbDimension.valueChanged.connect(this, "dimentionChanged(Integer)");
            sbDimension.setSizePolicy(QSizePolicy.Policy.Maximum, QSizePolicy.Policy.Maximum);
            arrayDimensionLayout.addWidget(lbDimension);
            arrayDimensionLayout.addWidget(sbDimension, 1, Qt.AlignmentFlag.AlignLeft);
            if (isForArray) {
                rbArrayTypes.setVisible(false);
                lbDimension.setVisible(false);
                sbDimension.setVisible(false);
            }

            rbRadixTypes.setChecked(true);

            QVBoxLayout vbox = new QVBoxLayout();
            vbox.setObjectName("vbox");
            vbox.addWidget(rbRadixTypes);
            vbox.addWidget(rbSimpleTypes);
            vbox.addWidget(rbJavaClassTypes);
            vbox.addWidget(rbArrayTypes);
            vbox.addLayout(arrayDimensionLayout);
            vbox.addStretch(1);
            groupBox.setLayout(vbox);
            layout.addWidget(groupBox);
            this.setLayout(layout);
        }

        private void dimentionChanged(final Integer dim) {
            dimension = dim;
        }

        @Override
        public void initializePage() {
            radixTypesSelected();
        }

        @SuppressWarnings("unused")
        private void arrayTypesSelected(final boolean isChecked) {
            setDimentionEnable(isChecked);
            isJavaArray = isChecked;
            isRadixTypes = !isChecked;
        }

        private void setDimentionEnable(final boolean enable) {
            lbDimension.setEnabled(enable);
            sbDimension.setEnabled(enable);
        }

        @Override
        public int nextId() {
            if (isRadixTypes) {
                return CHOOSE_RADIX_TYPE_PAGE;
            } else if (isJavaArray && !isForArray) {
                return CHOOSE_ARRAY_TYPE_PAGE;
            } else if (isJavaClassTypes) {
                return CHOOSE_JAVA_CLASS_PAGE;
            } else {
                return CHOOSE_SIMPLE_TYPE_PAGE;
            }
        }

        @SuppressWarnings("unused")
        private void radixTypesSelected() {
            isRadixTypes = true;
            isJavaClassTypes = false;
            if (!isForArray) {
                isJavaArray = false;
            }
            setDimentionEnable(false);
        }

        @SuppressWarnings("unused")
        private void simpleTypesSelected() {
            isRadixTypes = false;
            isJavaClassTypes = false;
            if (!isForArray) {
                isJavaArray = false;
            }
            setDimentionEnable(false);
        }

        @SuppressWarnings("unused")
        private void javaClassSelected() {
            isRadixTypes = false;
            isJavaClassTypes = true;
            if (!isForArray) {
                isJavaArray = false;
            }
            setDimentionEnable(false);
        }

        private class RadioButton extends QRadioButton {

            RadioButton(String s) {
                super(s);
            }

            @Override
            protected void mouseDoubleClickEvent(final QMouseEvent e) {
                super.mouseDoubleClickEvent(e);
                TypeWizard.this.next();
            }
        }
    }

    private class ChooseDefPage extends QWizardPage {

        private ChooseDefinitionPanel chooseDefPanel;
        private final QVBoxLayout layout;
        private final QWidget parent;

        public ChooseDefPage(final QWidget parent) {
            super(parent);
            this.parent = parent;
            this.setObjectName("ChooseDefPage");
            this.setFinalPage(true);
            layout = new QVBoxLayout();
            layout.setMargin(0);
            this.setLayout(layout);
        }

        @Override
        public boolean isComplete() {
            return chooseDefPanel.isComplete();
        }

        public void clear() {
            if (layout.indexOf(chooseDefPanel) != -1) {
                layout.removeWidget(chooseDefPanel);
            }
            chooseDefPanel.hide();
            layout.update();
        }

        @Override
        public int nextId() {
            if (!((selectedTypes.contains(EDefType.XML_SCHEME)) || (selectedTypes.contains(EDefType.MSDL_SCHEME)) || selectedTypes.isEmpty())) {
                return -1;
            }
            return CHOOSE_SCHEME_PAGE;
        }

        @Override
        public void initializePage() {
            final boolean isParentRef = isArrType && selectedTypes.contains(EDefType.CLASS);
            chooseDefPanel = new ChooseDefinitionPanel(TypeWizard.this, null, editor.getUserFunc(), selectedTypes, false, isParentRef);
            chooseDefPanel.setObjectName("ChooseDefinitionPanel");
            layout.addWidget(chooseDefPanel);
            if (!((selectedTypes.contains(EDefType.XML_SCHEME)) || (selectedTypes.contains(EDefType.MSDL_SCHEME)))) {
                if (this.nextId() != -1) {
                    this.setFinalPage(true);
                }
            } else {
                this.setFinalPage(false);
                if (this.nextId() == -1) {
                    addPage(new ChooseSchemaTypePage(parent));
                }
            }
        }

        @Override
        public void cleanupPage() {
            clear();
            chooseDefPanel.closeTread();
        }
    }

    private class ChooseSchemaTypePage extends QWizardPage {

        private final ChooseObjectPanel chooseObjPanel;

        public ChooseSchemaTypePage(final QWidget parent) {
            super(parent);
            this.setObjectName("ChooseSchemaTypePage");
            final QVBoxLayout layout = new QVBoxLayout();
            layout.setMargin(0);
            chooseObjPanel = new ChooseObjectPanel(TypeWizard.this, null);
            chooseObjPanel.setObjectName("ChooseObjectPanel");

            layout.addWidget(chooseObjPanel);
            this.setLayout(layout);

        }

        @Override
        public int nextId() {
            return -1;
        }

        @Override
        public void initializePage() {
            chooseObjPanel.update(getTypeDeclarationList());
        }

        private Collection<RadixObject> getTypeDeclarationList() {
            final Collection<RadixObject> allowedDefinitions = new ArrayList<>();
            if (curItem instanceof AdsXmlSchemeDef) {
                final AdsXmlSchemeDef scheme = (AdsXmlSchemeDef) curItem;
                if (scheme == null) {
                    return allowedDefinitions;
                }
                final Collection<String> types = scheme.getSchemaTypeList();
                for (String s : types) {
                    if (!s.contains("impl.")) {
                        final AdsTypeDeclaration type = AdsTypeDeclaration.Factory.newInstance(EValType.XML, scheme, s, getDimension());
                        type.setName(s);
                        allowedDefinitions.add(type);
                    }
                }
            }
            if (curItem instanceof AdsMsdlSchemeDef) {
                final AdsMsdlSchemeDef scheme = (AdsMsdlSchemeDef) curItem;
                if (scheme == null) {
                    return allowedDefinitions;
                }
                final Collection<String> types = scheme.getSchemaTypeList();
                for (String s : types) {
                    if (!s.contains("impl.")) {
                        final AdsTypeDeclaration type = AdsTypeDeclaration.Factory.newInstance(EValType.XML, scheme, s, getDimension());
                        type.setName(s);
                        allowedDefinitions.add(type);
                    }
                }
            }
            return allowedDefinitions;
        }
    }

    private class ChooseTypePage extends QWizardPage {

        private final QListWidget listWidget;
        private static final String msdlType = "MSDL";
        private static final String enumType = "Enumeration";
        private static final String arrEnumType = "Arr<Enumeration>";
        private final List<String> simpleJavaTypes = Arrays.asList("boolean", "byte", "char", "double", "float", "int", "long", "short");

        public ChooseTypePage(final QWidget parent, final boolean isRadixType) {
            super(parent);
            isRadixTypes = isRadixType;
            this.setFinalPage(!isRadixType);
            final String objectName = isRadixType ? "ChooseRadixTypePage" : "ChooseSimpleTypePage";
            this.setObjectName(objectName);

            final QVBoxLayout layout = new QVBoxLayout();
            layout.setMargin(0);
            listWidget = new QListWidget(this) {
                @Override
                protected void keyPressEvent(final QKeyEvent qke) {
                    if (qke.key() == Qt.Key.Key_Return.value() && SystemTools.isOSX) {
                        onItemDoubleClick(currentIndex());
                    } else {
                        super.keyPressEvent(qke);
                    }
                }
            };
            listWidget.setObjectName("TypeList");
            listWidget.setIconSize(new QSize(22, 22));
            listWidget.setAlternatingRowColors(true);
            listWidget.currentItemChanged.connect(this, "onItemChanged(QListWidgetItem,QListWidgetItem)");
            listWidget.activated.connect(this, "onItemDoubleClick(QModelIndex)");
            final List<EValType> types = new ArrayList<>(Arrays.asList(EValType.values()));
            if (isRadixType) {
                QListWidgetItem item = new QListWidgetItem(getTypeIcon(EValType.USER_CLASS), Application.translate("JmlEditor", "Class"));
                item.setData(ItemDataRole.UserRole, EValType.USER_CLASS.getName());
                listWidget.addItem(item);
                item = new QListWidgetItem(getTypeIcon(enumType), Application.translate("JmlEditor", "Enumeration"));
                item.setData(ItemDataRole.UserRole, enumType);
                listWidget.addItem(item);
                item = new QListWidgetItem(getTypeIcon(EValType.XML), Application.translate("JmlEditor", "Xml"));
                item.setData(ItemDataRole.UserRole, EValType.XML.getName());
                listWidget.addItem(item);
                item = new QListWidgetItem(getTypeIcon(msdlType), Application.translate("JmlEditor", "Msdl"));
                item.setData(ItemDataRole.UserRole, msdlType);
                listWidget.addItem(item);
                for (EValType type : types) {
                    if (type != EValType.PARENT_REF && /*type!=EValType.ARR_REF &&*/ type != EValType.ANY && type != EValType.JAVA_CLASS && type != EValType.XML
                            && type != EValType.JAVA_TYPE && type != EValType.NATIVE_DB_TYPE && type != EValType.USER_CLASS && type != EValType.OBJECT && type != EValType.IMG) {
                        item = new QListWidgetItem(getTypeIcon(type), type.getName());
                        item.setData(ItemDataRole.UserRole, type.getName());
                        listWidget.addItem(item);
                    }
                }
                item = new QListWidgetItem(getTypeIcon(arrEnumType), arrEnumType);
                item.setData(ItemDataRole.UserRole, arrEnumType);
                listWidget.addItem(item);
            } else {
                for (String type : simpleJavaTypes) {
                    QListWidgetItem item = new QListWidgetItem(type);
                    item.setData(ItemDataRole.UserRole, type);
                    listWidget.addItem(item);
                }
            }
            layout.addWidget(listWidget);
            this.setLayout(layout);
        }

        @SuppressWarnings("unused")
        private void onItemChanged(final QListWidgetItem item1, final QListWidgetItem item2) {
            final int row = listWidget.row(item1);
            final QModelIndex modelIndex = listWidget.model().index(row, 0);
            onItemClick(modelIndex);
        }

        private void onItemClick(final QModelIndex modelIndex) {
            final boolean res = canFinish(modelIndex);
            this.setFinalPage(res);
            curItem = null;
            TypeWizard.this.onItemClick(modelIndex);
            final String selectedType = (String) modelIndex.data(ItemDataRole.UserRole);
            TypeWizard.this.button(WizardButton.NextButton).setVisible(!res || selectedType.equals(EValType.XML.getName()));
            TypeWizard.this.button(WizardButton.FinishButton).setVisible(res);
        }

        @SuppressWarnings("unused")
        private void onItemDoubleClick(final QModelIndex modelIndex) {
            boolean res = canFinish(modelIndex);
            if (res) {
                final String selectedType = (String) modelIndex.data(ItemDataRole.UserRole);
                if (selectedType.equals(EValType.XML.getName())) {
                    res = false;
                }
            }
            TypeWizard.this.button(WizardButton.NextButton).setVisible(!res);
            TypeWizard.this.button(WizardButton.FinishButton).setVisible(res);
            TypeWizard.this.onItemDoubleClick(modelIndex);
        }

        private boolean canFinish(final QModelIndex modelIndex) {
            selectedTypes.clear();
            if (modelIndex == null || modelIndex.data(ItemDataRole.UserRole) == null) {
                return false;
            }
            boolean isFinishEnable = true;
            final String selectedType = (String) modelIndex.data(ItemDataRole.UserRole);
            if (selectedType.equals(msdlType)) {
                selectedTypes.add(EDefType.MSDL_SCHEME);
                isFinishEnable = false;
            } else if (selectedType.equals(EValType.USER_CLASS.getName()) || selectedType.equals(EValType.ARR_REF.getName()) || selectedType.equals(EValType.PARENT_REF.getName())) {
                selectedTypes.add(EDefType.CLASS);
                isArrType = selectedType.equals(EValType.ARR_REF.getName());
                isFinishEnable = false;
            } else if (selectedType.equals(EValType.XML.getName())) {
                selectedTypes.add(EDefType.XML_SCHEME);
                isFinishEnable = true;
            } else if (selectedType.equals(enumType) || selectedType.equals(arrEnumType)) {
                selectedTypes.add(EDefType.ENUMERATION);
                isArrType = selectedType.equals(arrEnumType);
                isFinishEnable = false;
            }
            return isFinishEnable;
        }

        @Override
        public void initializePage() {
            listWidget.setCurrentRow(0);
            curItem = null;
            onItemClick(listWidget.currentIndex());
        }

        @Override
        public boolean isComplete() {
            final QModelIndex modelIndex = listWidget.currentIndex();
            curItem = null;
            TypeWizard.this.onItemClick(modelIndex);
            return super.isComplete();
        }

        @Override
        public int nextId() {
            if (isRadixTypes) {
                return CHOOSE_DEF_PAGE;
            } else {
                return -1;
            }
        }

        private QIcon getTypeIcon(final EValType type) {
            final RadixIcon radixIcon = RadixObjectIcon.getForValType(type);
            return radixIcon == null ? null : ExplorerIcon.getQIcon(KernelIcon.getInstance(radixIcon));
        }

        private QIcon getTypeIcon(final String s) {
            RadixIcon radixIcon = null;
            switch (s) {
                case enumType:
                    radixIcon = RadixObjectIcon.ENUM;
                    break;
                case arrEnumType:
                    radixIcon = RadixObjectIcon.ARR_ENUM;
                    break;
                case msdlType:
                    radixIcon = AdsDefinitionIcon.XML_SCHEME;
                    break;
            }
            return radixIcon == null ? null : ExplorerIcon.getQIcon(KernelIcon.getInstance(radixIcon));
        }
    }

    private class ChooseJavaClassPage extends QWizardPage {

        private ChooseDefinitionPanel chooseDefPanel;
        private QVBoxLayout layout;
        private AdsDefinition def = null;

        public ChooseJavaClassPage(final QWidget parent) {
            super(parent);
            final Id ownerClassId = editor.getUserFunc().getOwnerClassId();
            if (ownerClassId != null) {
                def = AdsUserFuncDef.Lookup.findTopLevelDefinition(editor.getUserFunc(), ownerClassId);
            }
            this.setObjectName("ChooseJavaClassPage");
            this.setFinalPage(true);
            layout = new QVBoxLayout();
            layout.setMargin(0);
            this.setLayout(layout);
        }

        @Override
        public boolean isComplete() {
            return chooseDefPanel.isComplete();
        }

        public void clear() {
            if (layout.indexOf(chooseDefPanel) != -1) {
                layout.removeWidget(chooseDefPanel);
            }
            chooseDefPanel.hide();
            layout.update();
        }

        @Override
        public void initializePage() {
            final List<String> classes = getClasses();
            final Pattern INNER_CLASS_NAME = Pattern.compile("[1-9].*");
            final List<DefInfo> defInfos = new ArrayList<>();
            for (String s : classes) {
                final int index = s.lastIndexOf('.');
                String name = s, module = "";
                if (index != -1) {
                    name = s.substring(index + 1);
                    if (INNER_CLASS_NAME.matcher(name).matches() || name.length() == 1) {
                        continue;
                    }
                    module = s.substring(0, index);
                }
                final Id id = Id.Factory.loadFrom(s);
                DefInfo defInfo = new DefInfo(id, name, module, false, ERuntimeEnvironmentType.EXPLORER, null);
                defInfos.add(defInfo);
            }
            Collections.sort(defInfos, new DefInfoCompare());
            chooseDefPanel = new ChooseDefinitionPanel(TypeWizard.this, defInfos, editor.getUserFunc(), selectedTypes, false, false);
            chooseDefPanel.setObjectName("ChooseDefinitionPanel");

            layout.addWidget(chooseDefPanel);
        }

        @Override
        public void cleanupPage() {
            clear();
            chooseDefPanel.closeTread();
        }

        @Override
        public int nextId() {
            if ((curItem instanceof AdsTypeDeclaration) && def != null) {
                final String name = ((AdsTypeDeclaration) curItem).getExtStr();
                final RadixPlatformClass platformClass = getPlatformLib().findPlatformClass(name);
                boolean isGeneric = false;
                if (platformClass != null) {
                    platformClass.getDeclaration().getGenericArguments();
                    isGeneric = platformClass.hasGenericArguments();
                }
                if (platformClass != null && isGeneric) {
                    curItem = platformClass.getDeclaration();
                    final AdsTypeDeclaration.TypeArguments typeArguments = platformClass.getDeclaration().getGenericArguments();

                    genericAtributesPage.setGenericArguments(typeArguments.getArgumentList());
                    return GENERIC_ARGUMENTS_PAGE;
                }
            }
            return -1;
        }

        public void updateBtnsVisible() {
            if ((curItem instanceof AdsTypeDeclaration) && def != null) {
                final String name = ((AdsTypeDeclaration) curItem).getExtStr();
                final RadixPlatformClass platformClass = getPlatformLib().findPlatformClass(name);
                boolean isGeneric = false;
                if (platformClass != null) {
                    platformClass.getDeclaration().getGenericArguments();
                    isGeneric = platformClass.hasGenericArguments();
                }
                TypeWizard.this.button(WizardButton.NextButton).setVisible(isGeneric);
                TypeWizard.this.button(WizardButton.FinishButton).setVisible(!isGeneric);
            }
        }

        private PlatformLib getPlatformLib() {
            ERuntimeEnvironmentType env = ERuntimeEnvironmentType.COMMON;
            if (def instanceof IEnvDependent) {
                env = ((IEnvDependent) def).getUsageEnvironment();
            }
            return ((AdsSegment) def.getModule().getSegment()).getBuildPath().getPlatformLibs().getKernelLib(env);
        }

        private ERuntimeEnvironmentType getEnv() {
            ERuntimeEnvironmentType env = ERuntimeEnvironmentType.COMMON;
            if (def instanceof IEnvDependent) {
                env = ((IEnvDependent) def).getUsageEnvironment();
            }
            return env;
        }

        private List<String> getClasses() {
            final Set<String> result = new HashSet<>();
            if (def != null) {
                final AdsSegment segment = (AdsSegment) def.getModule().getSegment();
                if (segment != null) {
                    result.addAll(LookupUtils.collectLibraryClasses(segment.getLayer(), getEnv()));
                }
            }
            return new LinkedList<>(result);
        }

        class DefInfoCompare implements Comparator<DefInfo> {

            @Override
            public int compare(final DefInfo o1, final DefInfo o2) {
                return o1.getName().compareTo(o2.getName());
            }
        }
    }

    private class ChooseGenericAtributesPage extends QWizardPage {

        private final QTableWidget table;
        private static final int NAME_COLUMN = 0;
        private static final int TYPE_COLUMN = 1;
        private static final int BOUND_COLUMN = 2;
        private static final String strDefaultType = "java.lang.Object";

        public ChooseGenericAtributesPage(final QWidget parent) {
            super();
            final QVBoxLayout layout = new QVBoxLayout();
            table = new QTableWidget();
            table.setColumnCount(3);
            final List<String> columnNames = new ArrayList<>();
            columnNames.add(Application.translate("JmlEditor", "Name"));
            columnNames.add(Application.translate("JmlEditor", "Type"));
            columnNames.add(Application.translate("JmlEditor", "Bound"));
            table.setColumnCount(3);
            table.setHorizontalHeaderLabels(columnNames);
            table.setSelectionBehavior(QAbstractItemView.SelectionBehavior.SelectRows);
            table.verticalHeader().setVisible(false);
            table.setSelectionMode(QAbstractItemView.SelectionMode.SingleSelection);

            final BoundItemDelegate boundItemDelegate = new BoundItemDelegate();
            table.setItemDelegateForColumn(BOUND_COLUMN, boundItemDelegate);

            final TypeItemDelegate typeItemDelegate = new TypeItemDelegate();
            table.setItemDelegateForColumn(TYPE_COLUMN, typeItemDelegate);
            table.horizontalHeader().setResizeMode(BOUND_COLUMN, QHeaderView.ResizeMode.Stretch);
            table.resizeColumnsToContents();
            layout.addWidget(table);
            this.setLayout(layout);
        }

        public void setGenericArguments(final List<AdsTypeDeclaration.TypeArgument> args) {
            table.setRowCount(0);
            for (int i = 0; i < args.size(); i++) {
                table.insertRow(i);
                final QTableWidgetItem itemName = new QTableWidgetItem("?");
                final QTableWidgetItem itemType = new QTableWidgetItem(Derivation.EXTENDS.name());
                final QTableWidgetItem itemBound = new QTableWidgetItem(strDefaultType);
                table.setItem(i, NAME_COLUMN, itemName);
                table.setItem(i, TYPE_COLUMN, itemType);
                table.setItem(i, BOUND_COLUMN, itemBound);
            }
            table.resizeColumnsToContents();
        }

        private class BoundItemDelegate extends QItemDelegate {

            private ValStrEditor srcEditor;
            private List<AdsTypeDeclaration> curType = null;

            @Override
            public QWidget createEditor(final QWidget parent, final com.trolltech.qt.gui.QStyleOptionViewItem option, final QModelIndex index) {
                if (curType == null) {
                    curType = new LinkedList<>();
                    for (int i = 0; i < table.rowCount(); i++) {
                        curType.add(AdsTypeDeclaration.Factory.newPlatformClass(strDefaultType));
                    }
                }
                final EditMaskStr editMask = new EditMaskStr();
                editMask.setNoValueStr("");
                srcEditor = new ValStrEditor(TypeWizard.this.editor.getEnvironment(), parent, editMask, true, false);
                final AdsTypeDeclaration type = curType.get(index.row());
                srcEditor.setValue(type.getQualifiedName(editor.getUserFunc()));
                final QAction action = new QAction(this);
                action.triggered.connect(this, "actChooseGenericBound()");
                srcEditor.addButton("...", action);
                return srcEditor;
            }

            @SuppressWarnings("unused")
            private void actChooseGenericBound() {
                if (curItem != null && (curItem instanceof AdsTypeDeclaration)) {
                    int index = table.currentRow();
                    final TypeArgument arg = ((AdsTypeDeclaration) curItem).getGenericArguments().getArgumentList().get(index);
                    final TypeWizard genericArgTypeWizard = new TypeWizard(editor, true);
                    if (genericArgTypeWizard.exec() == 1) {
                        final AdsTypeDeclaration res = genericArgTypeWizard.calcType();
                        curType.remove(index);
                        curType.add(index, res);
                        arg.setType(res);
                        srcEditor.setValue(res.getQualifiedName(editor.getUserFunc()));
                    }
                }
            }

            @Override
            public void setModelData(final QWidget editor, final QAbstractItemModel model, final QModelIndex index) {
                super.setModelData(editor, model, index);
                final String newName = srcEditor.getValue();
                table.item(index.row(), index.column()).setText(newName);
                table.resizeColumnToContents(BOUND_COLUMN);
            }
        }

        private class TypeItemDelegate extends QItemDelegate {

            private QComboBox typeEditor;

            @Override
            public QWidget createEditor(final QWidget parent, final com.trolltech.qt.gui.QStyleOptionViewItem option, final QModelIndex index) {
                final EditMaskStr editMask = new EditMaskStr();
                editMask.setNoValueStr("");
                typeEditor = new QComboBox(parent);
                typeEditor.addItem(null, Derivation.EXTENDS.name(), Derivation.EXTENDS);
                typeEditor.addItem(null, Derivation.NONE.name(), Derivation.NONE);
                typeEditor.addItem(null, Derivation.SUPER.name(), Derivation.SUPER);
                typeEditor.setCurrentIndex(0);
                typeEditor.currentIndexChanged.connect(this, "currentIndexChanged(Integer)");
                return typeEditor;
            }

            @SuppressWarnings("unused")
            private void currentIndexChanged(final Integer index) {
                if (curItem != null && (curItem instanceof AdsTypeDeclaration)) {
                    final int row = table.currentRow();
                    final TypeArgument arg = ((AdsTypeDeclaration) curItem).getGenericArguments().getArgumentList().get(row);
                    final Derivation derivation = (Derivation) typeEditor.itemData(index);
                    arg.setDerivation(derivation);

                }
            }

            @Override
            public void setModelData(final QWidget editor, final QAbstractItemModel model, final QModelIndex index) {
                super.setModelData(editor, model, index);
                table.item(index.row(), index.column()).setText(typeEditor.currentText());
                table.resizeColumnToContents(TYPE_COLUMN);
            }
        }
    }

    @Override
    protected void closeEvent(QCloseEvent arg) {
        this.doubleClickTimer.disconnect();
    }
    
}
