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

package org.radixware.kernel.common.defs.ads.rights;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.DefinitionSearcher;
import org.radixware.kernel.common.defs.Definitions;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsInterfaceClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsCommandHandlerMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsDynamicPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsInnateColumnPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsInnateRefPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef.Setter;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyPresentationPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsUserMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.MethodParameter;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPageDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsScopeCommandDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSelectorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSelectorPresentationDef.SelectorColumn;
import org.radixware.kernel.common.defs.ads.clazz.presentation.EntityObjectPresentations;
import org.radixware.kernel.common.defs.ads.clazz.presentation.EntityPresentations;
import org.radixware.kernel.common.defs.ads.clazz.presentation.ParentRefPropertyPresentation;
import org.radixware.kernel.common.defs.ads.clazz.presentation.PropertyPresentation;
import org.radixware.kernel.common.defs.ads.common.AdsVisitorProviders;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.defs.ads.platform.IPlatformClassPublisher;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration.TypeArgument;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration.TypeArguments;
import org.radixware.kernel.common.defs.ads.ui.AdsCustomPropEditorDef;
import org.radixware.kernel.common.defs.ads.xml.AdsXmlSchemeDef;
import org.radixware.kernel.common.defs.dds.DdsAccessPartitionFamilyDef;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsFunctionDef;
import org.radixware.kernel.common.defs.dds.DdsPackageDef;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.ECommandScope;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EDrcPredefinedRoleId;
import org.radixware.kernel.common.enums.EEditPossibility;
import org.radixware.kernel.common.enums.EEditorPageType;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.EPropNature;
import org.radixware.kernel.common.enums.ERestriction;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.common.jml.JmlTagId;
import org.radixware.kernel.common.jml.JmlTagInvocation;
import org.radixware.kernel.common.jml.JmlTagLocalizedString;
import org.radixware.kernel.common.jml.JmlTagTypeDeclaration;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.ads.AdsSegment;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.sqml.tags.PropSqlNameTag;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.RadixObjectsUtils;


// TODO incorrect messages !!!
public class SystemPresentationBuilder {

    final public static Id USER2ROLE_ID = Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4");
    final public static Id USERGROUP2ROLE_ID = Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4");
    final public static Id ACCESSAREAMODE_ID = Id.Factory.loadFrom("acsX3OOTBYTZTORDIFLABQAQH3XQ4");
    final private static Id ADSDEF_ID = Id.Factory.loadFrom("xsd5NCENQ5IQ3OBDCKEAALOMT5GDM");
    final private static Id ENTITY_ID = Id.Factory.loadFrom("pdcEntity____________________");
    final private static Id TYPE_ID_ID = Id.Factory.loadFrom("adcELH54EKVCJAATNTLFHHCSVHPZU");
    final private static Id ROLE_HOLDER_ID = Id.Factory.loadFrom("picMRY76BSECRCYND3ZVMDJ3XF3FM");
    final public static Id PREDEFINED_EDITOR_PRESENTATION_ID_FOR_USER2ROLE = Id.Factory.loadFrom("epr3HIYWUVLLJCLDPWFCQPRHLHLWY");
    final public static Id PREDEFINED_SELECTOR_PRESENTATION_ID_FOR_USER2ROLE = Id.Factory.loadFrom("spr4ANIOJDV5BEANCPBFYUJD6WQBI");
    final public static Id PREDEFINED_EDITOR_PRESENTATION_ID_FOR_USER_GROUP2ROLE = Id.Factory.loadFrom("eprEQ7UIJIUWJAFVAKCR6ELGWNPWY");
    final public static Id PREDEFINED_SELECTOR_PRESENTATION_ID_FOR_USER_GROUP2ROLE = Id.Factory.loadFrom("spr4M72LWZY6BEFDBSMYOB27SEVBE");
    final private static String PREDEFINED_MAIN_EDITOR_PAGE_NAME = "Main";
    //final private static String PREDEFINED_HIDE_EDITOR_PAGE_NAME = "Hide";
    //adcELH54EKVCJAATNTLFHHCSVHPZU
    final private static String GETAREANAME = "getArea";
    final private static String ROLETITLENAME = "roleTitle";
    final private static String ROLEIDNAME = "roleId";
    final private static String IDNAME = "id";
    final private static String ISOWN = "isOwn";
    final private static String USERNAME = "userName";
    final private static String USERGROUPNAME = "groupName";
    final private static String USERTITLE = "userTitle";
    final private static String USERGROUPTITLE = "groupTitle";
    final private static String CMDGETAPFAMILYLIST = "GetAPFamilyList";
    final private static Id ROLESUPERADMINID = Id.Factory.loadFrom(EDrcPredefinedRoleId.SUPER_ADMIN.getValue());
    final private static Id CHOOSEROLEDLGID = Id.Factory.loadFrom("cpe2ATLT7MVSPORDID2ABQAQH3XQ4");
    final private static Id UTILSID = Id.Factory.loadFrom("adcKXBE5RCT2DNRDISQAAAAAAAAAA");
    final private static String GETROLETITLENAME = "getRoleTitle";
    final private static Id PACKAGE_ACS_UTILSID = Id.Factory.loadFrom("pkgGKLDTS2YJLOBDH2FABQAQH3XQ4");
    final private static Id PACKAGE_ACS = Id.Factory.loadFrom("pkgYQZTLHT2YDORDIE4ABQAQH3XQ4");
    final private static Id COMPILE_RIGHTS_FOR_GROUP_ID = Id.Factory.loadFrom("dfnXMLATC5CW5BSFKCT7AXBI7NYWI");
    final private static Id CURR_USER_IS_IN_GROUP_ID = Id.Factory.loadFrom("dfn2EZTLHT2YDORDIE4ABQAQH3XQ4");
    final private static Id getRoleIdPropID = Id.Factory.loadFrom("mthH5ARGLQ3D5BXLERL3EHYVLCXVM");
    final private static Id getRoleTitlePropID = Id.Factory.loadFrom("mth2ZUYLGXZQRHC5LTWGQEDXMXPKE");
    final private AdsEntityClassDef classDef;
    private boolean isUser;
    private boolean isInherit;
    private DdsTableDef table = null;
    private AdsXmlSchemeDef AdsDef = null;
    private AdsRoleDef superAdmin = null;
    private AdsClassDef typeId = null;
    private AdsClassDef entityClass = null;
    private AdsClassDef utilsClass = null;
    private AdsUserMethodDef getArea = null;
    private AdsMethodDef getRoleTitle = null;
    private Collection<Definition> apfCollection = null;
    private List<ApfItem> apfItems = null;
    private AdsDynamicPropertyDef roleTitleProp = null;
    private AdsInnateColumnPropertyDef roleIdProp = null;
    private AdsInnateColumnPropertyDef userOrGroupNameProp = null;
    private AdsInnateRefPropertyDef userOrGroupNamePropParentRef = null;
    private AdsDynamicPropertyDef roleTitlePropInh = null;
    private AdsInnateColumnPropertyDef roleIdPropInh = null;
    private AdsInnateColumnPropertyDef userOrGroupNamePropInh = null;
    private AdsInnateRefPropertyDef userOrGroupNamePropParentRefInh = null;
    private DdsReferenceDef refToUserOrGroup = null;
    private AdsInnateColumnPropertyDef idProp = null;
    private AdsInnateColumnPropertyDef isOwnProp = null;
    private AdsInnateColumnPropertyDef idPropInh = null;
    private AdsInnateColumnPropertyDef isOwnPropInh = null;
    private AdsEnumDef accessAreaMode = null;
    private AdsEditorPresentationDef editorPres = null;
    private AdsInterfaceClassDef roleHolderInterface = null;
    private AdsScopeCommandDef cmdApFamilyList = null;
    private AdsMethodDef entityAfterReadMethod = null;
    private AdsCustomPropEditorDef chooseRoleDlg = null;
    private AdsEntityClassDef overwrittenClass = null;
    private DdsFunctionDef compileRightsForGroup = null;
    private DdsFunctionDef curUserIsInGroup = null;
    private DdsPackageDef acsUtils = null;
    private DdsPackageDef acs = null;

    SystemPresentationBuilder(AdsEntityClassDef classDef) {
        this.classDef = classDef;
    }

    public static void createSystemPresentation(AdsEntityClassDef classDef) {
        SystemPresentationBuilder builder = new SystemPresentationBuilder(classDef);
        builder.check();
        builder.getEditorPropertiesOrder();
        builder.createProps();
        builder.createGetArea();
        builder.createCommand();
        builder.createServerMethods();
        builder.createClassCatalog();
        builder.createEditorPresentation();
        builder.createSelectorPresentation();
        builder.createClassDefinition();

    }

    private class ApfItem extends RadixObject {

        //private boolean isInOurLayer = false;
        private DdsAccessPartitionFamilyDef apf;
        private int keyIndex = -1;
        private int modeIndex = -1;
        //  private int propKeyIndex = -1;
        //  private int propModeIndex = -1;
        private ApfItem parentAPFItem = null;
        DdsColumnDef parentAPFColumn = null;

        public DdsColumnDef getParentAPFColumn() {
            return parentAPFColumn;
        }

        public void setParentAPFColumn(DdsColumnDef parentAPFColumn) {
            this.parentAPFColumn = parentAPFColumn;
        }
        private String enStr = null;
        private String ruStr = null;
        private AdsInnateColumnPropertyDef keyProp = null;
        private AdsInnateColumnPropertyDef modeProp = null;
        private AdsInnateRefPropertyDef linkProp = null;// for tables
        private AdsInnateColumnPropertyDef keyPropInherit = null;
        private AdsInnateColumnPropertyDef modePropInherit = null;
        private AdsInnateRefPropertyDef linkPropInherit = null;
        private AdsPropertyDef keyPropPres = null;
        private AdsPropertyDef modePropPres = null;
        private AdsPropertyDef linkPropPres = null;
        private DdsReferenceDef ref = null;// for tables
        public boolean isTable;
        AdsEntityObjectClassDef referencedEntityClass;
        private List<ApfItem> childApfIndex = new ArrayList<>(0);

        public List<ApfItem> getChildApfIndex() {
            return childApfIndex;
        }

        ApfItem(DdsAccessPartitionFamilyDef apf) {
            this.apf = apf;
            isTable = apf.getHeadId().getPrefix() == EDefinitionIdPrefix.DDS_TABLE;
        }

        public AdsEntityObjectClassDef getReferencedEntityClass() {
            return referencedEntityClass;
        }

        public void setReferencedEntityClass(AdsEntityObjectClassDef x) {
            this.referencedEntityClass = x;
        }

        public ApfItem getParentApfItem() {
            return parentAPFItem;
        }

        public void setParentAPFItem(ApfItem x) {
            parentAPFItem = x;
        }

        public int getKeyIndexInColumns() {
            return keyIndex;
        }

        public void setKeyIndexInColumns(int x) {
            this.keyIndex = x;
        }

        public int getModeIndexInColumns() {
            return modeIndex;
        }

        public void setModeIndexInColumns(int x) {
            this.modeIndex = x;
        }

        /*
         * public int getKeyIndexInProps() { return propKeyIndex; }
         *
         * public void setKeyIndexInProps(int x) { this.propKeyIndex = x; }
         *
         * public int getModeIndexInProps() { return propModeIndex; }
         *
         * public void setModeIndexInProps(int x) { this.propModeIndex = x; }
         */
        public AdsInnateColumnPropertyDef getPropMode() {
            return modeProp;
        }

        public void setPropMode(AdsInnateColumnPropertyDef x) {
            this.modeProp = x;
        }

        public AdsInnateColumnPropertyDef getPropKey() {
            return keyProp;
        }

        public void setPropKey(AdsInnateColumnPropertyDef x) {
            this.keyProp = x;
        }

        public AdsInnateRefPropertyDef getPropLink() {
            return linkProp;
        }

        public void setPropLink(AdsInnateRefPropertyDef x) {
            this.linkProp = x;
        }

        public DdsReferenceDef getRef() {
            return ref;
        }

        public void setRef(DdsReferenceDef x) {
            this.ref = x;
        }

        DdsAccessPartitionFamilyDef getApf() {
            return apf;
        }

        @Override
        public String getName() {
            return apf.getName();
        }
    }

    private int findApfById(Id id) {
        int i = 0;
        for (ApfItem item : apfItems) {
            if (item.getApf().getId().equals(id)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    private void createClassDefinition() {
        this.classDef.getHeader().clear();
        Jml header = this.classDef.getHeader().ensureFirst();
        this.classDef.getBody().clear();
        Jml body = this.classDef.getBody().ensureFirst();

        header.getItems().clear();
        body.getItems().clear();

        if (this.classDef.getHierarchy().findOverwritten().get() == null) {
            if (isUser) {
                header.getItems().add(
                        Sqml.Text.Factory.newInstance(
                        "import java.util.HashMap;\n"
                        + "import org.radixware.schemas.eas.ExceptionEnum;\n"
                        + "import org.radixware.kernel.common.exceptions.ServiceProcessClientFault;\n"
                        + "import org.radixware.kernel.server.meta.roles.RadRoleDef;\n"
                        + "import org.radixware.kernel.server.arte.Rights;\n"
                        + "import org.radixware.kernel.common.types.Id;\n"
                        + "import org.radixware.kernel.common.defs.dds.DdsAccessPartitionFamilyDef;\n"));
            } else {
                header.getItems().add(
                        Sqml.Text.Factory.newInstance(
                        "import java.util.HashMap;\n"
                        //                        + "import java.sql.CallableStatement;\n"
                        //                        + "import java.sql.SQLException;\n"
                        //                        + "import org.radixware.kernel.server.exceptions.ArteInitializationError;\n"
                        //                        + "import org.radixware.kernel.server.exceptions.DatabaseError;\n"
                        + "import org.radixware.schemas.eas.ExceptionEnum;\n"
                        + "import org.radixware.kernel.common.exceptions.ServiceProcessClientFault;\n"
                        + "import org.radixware.kernel.server.meta.roles.RadRoleDef;\n"
                        + "import org.radixware.kernel.server.arte.Rights;\n"
                        + "import org.radixware.kernel.common.types.Id;\n"
                        + "import org.radixware.kernel.common.defs.dds.DdsAccessPartitionFamilyDef;\n"));

                body.getItems().add(
                        Sqml.Text.Factory.newInstance(
                        "private String sLastGroupName = null;" /*
                         * + "\nprivate CallableStatement qryReCompileRights =
                         * null;\n" + "private final CallableStatement
                         * getQryReCompileRights(final boolean useCached)\n" +
                         * "{\n" + "	if (qryReCompileRights == null ||
                         * !useCached)\n" + "	{\n" + "	try\n" + "	{\n" + "
                         * qryReCompileRights =
                         * getArte().getDbConnection().get().prepareCall(\n" + "
                         * \"begin \" + "));
                         *
                         * body.getItems().add(JmlTagDbName.Factory.newInstance(compileRightsForGroup));
                         *
                         * body.getItems().add( Sqml.Text.Factory.newInstance( "
                         * + \"(?); end;\"\n" + "	);\n" + "	}catch (SQLException
                         * e)\n" + "	{\n" + "	throw new
                         * ArteInitializationError(\"Can't prepare radix rights
                         * system service queries:\" + e.getMessage(), e);\n" +
                         * "	}\n" + "	}\n" + "	return qryReCompileRights;\n" +
                         * "}\n" + "private void CompileRights(final String
                         * group, boolean useCash)\n" + "{\n" + "	try\n" + "
                         * {\n" + "	final CallableStatement qry =
                         * getQryReCompileRights(useCash);\n" + "
                         * qry.setString(1, group);\n" + "	qry.execute();\n" + "
                         * }\n" + "	catch (SQLException e)\n" + "	{\n" + "	if
                         * (useCash)\n" + "	CompileRights(group, false);\n" + "
                         * else\n" + "	throw new DatabaseError(\"Can't check
                         * user rights: \" + e.getMessage(), e);\n" + "	}\n" +
                         * "}\n"
                         */ /*
                         * +
                         *
                         *
                         * "private CallableStatement qryCurrUserIsInGroup =
                         * null;\n" + "private final CallableStatement
                         * getQryCurrUserIsInGroup(final boolean useCached)\n" +
                         * "{\n" + "	if (qryCurrUserIsInGroup == null ||
                         * !useCached)\n" + "	{\n" + "	try\n" + "	{\n" + "
                         * qryCurrUserIsInGroup =
                         * getArte().getDbConnection().get().prepareCall(\n" + "
                         * \"begin ?:=\" + " ));
                         *
                         * body.getItems().add(JmlTagDbName.Factory.newInstance(curUserIsInGroup));
                         *
                         *
                         * body.getItems().add( Sqml.Text.Factory.newInstance( "
                         * + \"(?); end;\"\n" + "	);\n" + "
                         * qryCurrUserIsInGroup.registerOutParameter(1,
                         * java.sql.Types.INTEGER);\n" + "	}catch (SQLException
                         * e)\n" + "	{\n" + "	throw new
                         * ArteInitializationError(\"Can't prepare radix rights
                         * system service queries:\" + e.getMessage(), e);\n" +
                         * "	}\n" + "	}\n" + "	return qryCurrUserIsInGroup;\n" +
                         * "}\n" + "private boolean isCurUserInGroup(final
                         * String group, boolean useCash)\n" + "{\n" + "	try\n"
                         * + "	{\n" + "	final CallableStatement qry =
                         * getQryCurrUserIsInGroup(useCash);\n" + "
                         * qry.setString(2, group);\n" + "	qry.execute();\n" + "
                         * return qry.getLong(1) != 0;\n" + "	}\n" + "	catch
                         * (SQLException e)\n" + "	{\n" + "	if (useCash)\n" + "
                         * return isCurUserInGroup(group, false);\n" + "	else\n"
                         * + "	throw new DatabaseError(\"Can't check user
                         * rights: \" + e.getMessage(), e);\n" + "	}\n" + "}\n
                         */));


                //curUserIsInGroup

            }
        }

    }

    private void createClassCatalog() {
//        List<AdsClassCatalogDef> list = this.classDef.getPresentations().getClassCatalogs().getLocal().list();
//        if (list.size() > 0) {
//            сlassCatalog = list.get(0);
//        } else {
//            сlassCatalog = AdsClassCatalogDef.Factory.newInstance();
//            this.classDef.getPresentations().getClassCatalogs().getLocal().add(сlassCatalog);
//            сlassCatalog.setOverwrite(true);
//        }
//
    }

    private void createSelectorPresentation() {

        AdsSelectorPresentationDef selectorPres = null;
        Id id = isUser ? PREDEFINED_SELECTOR_PRESENTATION_ID_FOR_USER2ROLE : PREDEFINED_SELECTOR_PRESENTATION_ID_FOR_USER_GROUP2ROLE;


        for (AdsSelectorPresentationDef pres : classDef.getPresentations().getSelectorPresentations().getLocal()) {
            if (pres.getId().equals(id)) {
                classDef.getPresentations().getSelectorPresentations().getLocal().remove(pres);
                break;
            }
        }



        if (isInherit) {
            List<AdsSelectorPresentationDef> list = overwrittenClass.getPresentations().getSelectorPresentations().get(EScope.LOCAL);
            for (AdsSelectorPresentationDef sep : list) {
                if (sep.getId().equals(id)) {
                    selectorPres = classDef.getPresentations().getSelectorPresentations().overwrite(sep);
                    //inhPres =
                    break;
                }
            }
        }
        boolean isMustAddColumns = false;
        if (selectorPres == null) {
            if (isUser) {
                selectorPres = AdsSelectorPresentationDef.Factory.newSelectorPresentationForUser2Role();
            } else {
                selectorPres = AdsSelectorPresentationDef.Factory.newSelectorPresentationForUserGroup2Role();
            }
            isMustAddColumns = true;
            selectorPres.setName("General");
            classDef.getPresentations().getSelectorPresentations().getLocal().add(selectorPres);
            selectorPres.getEditorPresentations().add(editorPres.getId());
        }



//        selectorPres.setCreationClassCatalogId(сlassCatalog.getId());
        selectorPres.getCreatePresentationsList().addPresentationId(editorPres.getId());
        selectorPres.setAddonsInherited(false);
        selectorPres.setColumnsInherited(false);
        selectorPres.setConditionInherited(false);
        selectorPres.setCreationClassCatalogInherited(false);
        selectorPres.setCustomViewInherited(false);
        selectorPres.setRestrictionsInherited(false);
        selectorPres.setTitleInherited(false);

        selectorPres.getRestrictions().deny(ERestriction.DELETE_ALL);


        if (isMustAddColumns) {
            selectorPres.getColumns().add(SelectorColumn.Factory.newInstance(this.userOrGroupNamePropParentRef));
            selectorPres.getColumns().get(0).setTitle(EIsoLanguage.ENGLISH, isUser ? "User" : "User Group");
            selectorPres.getColumns().get(0).setTitle(EIsoLanguage.RUSSIAN, isUser ? "Пользователь" : "Группа пользователей");

            selectorPres.getColumns().add(SelectorColumn.Factory.newInstance(this.roleTitleProp));
            selectorPres.getColumns().get(1).setTitle(EIsoLanguage.ENGLISH, "Role");
            selectorPres.getColumns().get(1).setTitle(EIsoLanguage.RUSSIAN, "Роль");
        }

        if (isUser) {
            selectorPres.getCondition().getWhere().getItems().clear();

            PropSqlNameTag sqlNameTag = PropSqlNameTag.Factory.newInstance();
            sqlNameTag.setOwnerType(PropSqlNameTag.EOwnerType.THIS);
            sqlNameTag.setPropId(isOwnProp.getId());
            sqlNameTag.setPropOwnerId(this.classDef.getId());

            selectorPres.getCondition().getWhere().getItems().add(sqlNameTag);
            selectorPres.getCondition().getWhere().getItems().add(Sqml.Text.Factory.newInstance(" = 1"));
        }
        //isOwnProp


        classDef.getPresentations().setDefaultSelectorPresentationId(selectorPres.getId());
        selectorPres.setUseDefaultModel(true);
        //selectorPres.setCreationClassCatalogId(UTILSID);
        //classDef.

    }

    private void createEditorPresentation() {
        Id id = isUser ? PREDEFINED_EDITOR_PRESENTATION_ID_FOR_USER2ROLE : PREDEFINED_EDITOR_PRESENTATION_ID_FOR_USER_GROUP2ROLE;
        EntityPresentations entityPresentations = classDef.getPresentations();
        //entityPresentations.getEditorPresentations().getLocal().
        AdsEditorPresentationDef inhPres = null;
        for (AdsEditorPresentationDef pres : entityPresentations.getEditorPresentations().getLocal()) {
            if (pres.getId().equals(id)) {
                entityPresentations.getEditorPresentations().getLocal().remove(pres);
                break;
            }
        }

        editorPres = null;
        if (isInherit) {
            List<AdsEditorPresentationDef> list = overwrittenClass.getPresentations().getEditorPresentations().get(EScope.LOCAL);
            for (AdsEditorPresentationDef epr : list) {
                if (epr.getId().equals(id)) {
                    editorPres = classDef.getPresentations().getEditorPresentations().overwrite(inhPres = epr);
                    break;
                }
            }
        }
        if (editorPres == null) {
            if (isUser) {
                editorPres = AdsEditorPresentationDef.Factory.newEditorPresentationForUser2Role();
            } else {
                editorPres = AdsEditorPresentationDef.Factory.newEditorPresentationForUserGroup2Role();
            }
            entityPresentations.getEditorPresentations().getLocal().add(editorPres);
            editorPres.setName("General");
        }

        editorPres.setObjectTitleFormatInherited(false);
        editorPres.setTitleInherited(false);
        editorPres.setRestrictionsInherited(false);
        editorPres.setIconInherited(false);
        editorPres.setCustomViewInherited(false);
        editorPres.setExplorerItemsInherited(true);
        editorPres.setEditorPagesInherited(true);


        AdsEditorPageDef inhPage1 = null;
//        AdsEditorPageDef inhPage2 = null;
        editorPres.setExplorerItemsInherited(false);
        editorPres.setEditorPagesInherited(false);

        if (inhPres != null) {
            List<AdsEditorPageDef> editorPageList = inhPres.getEditorPages().get(EScope.ALL);
            for (AdsEditorPageDef editorPage : editorPageList) {
                if (editorPage.getName().equals(PREDEFINED_MAIN_EDITOR_PAGE_NAME)) {
                    inhPage1 = editorPage;
                }
//            else if (editorPage.getName().equals(PREDEFINED_HIDE_EDITOR_PAGE_NAME))
//                inhPage2 = editorPage;
            }

        }

        /*
         * PREDEFINED_MAIN_EDITOR_PAGE_NAME PREDEFINED_HIDE_EDITOR_PAGE_NAME
         */
        AdsEditorPageDef page1;
//        AdsEditorPageDef page2 = null;
        if (inhPage1 != null) {
            page1 = editorPres.getEditorPages().getLocal().overwrite(inhPage1);
        } else {
            page1 = AdsEditorPageDef.Factory.newInstance();
            editorPres.getEditorPages().getLocal().add(page1);
        }
//        if (inhPage2!=null)
//            page2 = editorPres.getEditorPages().getLocal().overwrite(inhPage2);
//        else{
//            page2 = AdsEditorPageDef.Factory.newInstance();
//            editorPres.getEditorPages().getLocal().add(page2);
//        }


        page1.setType(EEditorPageType.STANDARD);
        page1.setName(PREDEFINED_MAIN_EDITOR_PAGE_NAME);

//        page2.setType(EEditorPageType.STANDARD);
//        page2.setName(PREDEFINED_HIDE_EDITOR_PAGE_NAME);



        List<Id> isList = page1.getProperties().getIds();
        for (int k = isList.size() - 1; k >= 0; k--) {
            page1.getProperties().removeByPropId(isList.get(k));
        }

        page1.getProperties().addPropId(this.idProp.getId());
        page1.getProperties().addPropId(this.userOrGroupNamePropParentRef.getId());
        page1.getProperties().addPropId(this.roleTitleProp.getId());
//        page2.getProperties().addPropId(this.roleIdProp.getId());


        //AdsPropertyPresentationPropertyDef presUserOrGroupPropParentRef = null;
        //AdsPropertyPresentationPropertyDef presUserOrGroupNameProp = null;

        if (editorPres.getModel() == null) {
            editorPres.setUseDefaultModel(false);
        }
        editorPres.getModel().setClientEnvironment(ERuntimeEnvironmentType.COMMON_CLIENT);

//        presUserOrGroupPropParentRef = AdsPropertyPresentationPropertyDef.Factory.newInstance(this.userOrGroupNamePropParentRef);
//        editorPres.getModel().getProperties().getLocal().add(presUserOrGroupPropParentRef);

        if (isUser) {
//
//
//            presUserOrGroupNameProp = AdsPropertyPresentationPropertyDef.Factory.newInstance(this.userOrGroupNameProp);
//            editorPres.getModel().getProperties().getLocal().add(presUserOrGroupNameProp);
        }
//        
//        org.apache.xmlbeans.XmlObject ss = org.apache.xmlbeans.XmlObject.Factory.newInstance();
//        Node nn = ss.getDomNode();
//
//        nn.setNodeValue("123");
//        nn.getClass().getName();
//        boolean rr = nn instanceof  Element;
////
//ss.getDomNode().
//        if (isUser) {
//            page2.getProperties().addPropId(this.isOwnProp.getId());
//           // page2.getProperties().addPropId(presUserOrGroupPropParentRef.getId());
//            page2.getProperties().addPropId(presUserOrGroupNameProp.getId());
//        }


        editorPres.getModel().
                getInheritance().
                addSuperInterfaceRef(
                AdsTypeDeclaration.Factory.newInstance(roleHolderInterface));

        AdsPropertyPresentationPropertyDef presRoleIdProp =
                AdsPropertyPresentationPropertyDef.Factory.newInstance(this.roleIdProp);
        editorPres.getModel().getProperties().getLocal().add(presRoleIdProp);

//        AdsPropertyPresentationPropertyDef presRoleTitleProp =
//                AdsPropertyPresentationPropertyDef.Factory.newInstance(this.roleTitleProp);
//
//        editorPres.getModel().getProperties().getLocal().add(presRoleTitleProp);




        /*
         * Definitions<AdsMethodDef> roleHolderMethods =
         * roleHolderInterface.getMethods().getLocal(); if
         * (roleHolderMethods.findById(getRoleIdPropID) == null ||
         * roleHolderMethods.findById(getRoleTitlePropID) == null) {
         *
         */
        {
            AdsMethodDef oldGetRoleIdProp = roleHolderInterface.getMethods().getLocal().findById(getRoleIdPropID);
            AdsUserMethodDef currGetRoleIdProp = (AdsUserMethodDef) editorPres.getModel().getMethods().override(oldGetRoleIdProp);

            currGetRoleIdProp.getSource().getItems().clear();

            currGetRoleIdProp.getSource().getItems().add(Sqml.Text.Factory.newInstance(
                    "return "));
            currGetRoleIdProp.getSource().getItems().add(JmlTagInvocation.Factory.newInstance(presRoleIdProp));
            currGetRoleIdProp.getSource().getItems().add(Sqml.Text.Factory.newInstance(
                    ";"));
        }

        {
            AdsMethodDef oldGetRoleTitlePropID = roleHolderInterface.getMethods().getLocal().findById(getRoleTitlePropID);
            AdsUserMethodDef currGetRoleTitleProp = (AdsUserMethodDef) editorPres.getModel().getMethods().override(oldGetRoleTitlePropID);

            currGetRoleTitleProp.getSource().getItems().clear();

            currGetRoleTitleProp.getSource().getItems().add(Sqml.Text.Factory.newInstance(
                    "return "));
            currGetRoleTitleProp.getSource().getItems().add(JmlTagInvocation.Factory.newInstance(this.roleTitleProp));
            currGetRoleTitleProp.getSource().getItems().add(Sqml.Text.Factory.newInstance(
                    ";"));
        }




        for (ApfItem item : apfItems) {
            page1.getProperties().addPropId(item.getPropMode().getId());
            if (item.isTable) {
                page1.getProperties().addPropId(item.getPropLink().getId());
//                page2.getProperties().addPropId(item.getPropKey().getId());
                //   AdsPropertyPresentationPropertyDef presLnk =
                //           AdsPropertyPresentationPropertyDef.Factory.newInstance(item.getPropLink());
                //  editorPres.getModel().getProperties().getLocal().add(presLnk);

                item.linkPropPres = item.getPropLink();
            } else {
                page1.getProperties().addPropId(item.getPropKey().getId());
                //AdsInnateColumnPropertyDef ss = item.getPropKey();
            }


            AdsPropertyPresentationPropertyDef presMode =
                    AdsPropertyPresentationPropertyDef.Factory.newInstance(item.getPropMode());
            editorPres.getModel().getProperties().getLocal().add(presMode);

            item.modePropPres = presMode;


            item.keyPropPres = item.getPropKey();


            presMode.setSetterDefined(true);
            Setter modeSetter = presMode.findSetter(EScope.LOCAL).get();
            /*
             * m.getSource().getItems().add(
             * JmlTagInvocation.Factory.newInstance(keyProp));
             */
            modeSetter.getSource(ERuntimeEnvironmentType.COMMON_CLIENT).getItems().add(
                    Sqml.Text.Factory.newInstance(
                    "if ("));
            //"Ap1Mode"
            modeSetter.getSource(ERuntimeEnvironmentType.COMMON_CLIENT).getItems().add(JmlTagInvocation.Factory.newInternalPropAccessor(presMode));
            modeSetter.getSource(ERuntimeEnvironmentType.COMMON_CLIENT).getItems().add(
                    Sqml.Text.Factory.newInstance(
                    "!=val)\n"
                    + "{\n"));
            modeSetter.getSource(ERuntimeEnvironmentType.COMMON_CLIENT).getItems().add(
                    JmlTagInvocation.Factory.newInstance(item.isTable ? item.getPropLink() : item.getPropKey()));
            modeSetter.getSource(ERuntimeEnvironmentType.COMMON_CLIENT).getItems().add(
                    Sqml.Text.Factory.newInstance(
                    ".setVisible(val.Value==1);\n"));
            //"Ap1Mode" +
            modeSetter.getSource(ERuntimeEnvironmentType.COMMON_CLIENT).getItems().add(JmlTagInvocation.Factory.newInternalPropAccessor(presMode));
            modeSetter.getSource(ERuntimeEnvironmentType.COMMON_CLIENT).getItems().add(
                    Sqml.Text.Factory.newInstance(
                    " = val;\n"
                    + "}"));
            if (item.isTable && item.getChildApfIndex().size() > 0) {


                //JmlTagInternalPropertyAccess
                AdsPropertyPresentationPropertyDef presKey =
                        AdsPropertyPresentationPropertyDef.Factory.newInstance(item.getPropKey());
                editorPres.getModel().getProperties().getLocal().add(presKey);

                item.keyPropPres = presKey;


                presKey.setSetterDefined(true);
                Setter keySetter = presKey.findSetter(EScope.LOCAL).get();



                keySetter.getSource(ERuntimeEnvironmentType.COMMON_CLIENT).getItems().add(
                        Sqml.Text.Factory.newInstance(
                        "if ("));
                //internal[AP4Key]
                keySetter.getSource(ERuntimeEnvironmentType.COMMON_CLIENT).getItems().add(JmlTagInvocation.Factory.newInternalPropAccessor(presKey));
                keySetter.getSource(ERuntimeEnvironmentType.COMMON_CLIENT).getItems().add(
                        Sqml.Text.Factory.newInstance(
                        "!= val)\n{\n"));
                //internal[AP4Key]
                keySetter.getSource(ERuntimeEnvironmentType.COMMON_CLIENT).getItems().add(JmlTagInvocation.Factory.newInternalPropAccessor(presKey));
                keySetter.getSource(ERuntimeEnvironmentType.COMMON_CLIENT).getItems().add(
                        Sqml.Text.Factory.newInstance(
                        "= val;\n"));
                for (ApfItem item2 : item.getChildApfIndex()) {
                    if (item2.isTable) {
                        keySetter.getSource(ERuntimeEnvironmentType.COMMON_CLIENT).getItems().add(
                                JmlTagInvocation.Factory.newInstance(item2.getPropLink()));
                    } else {
                        keySetter.getSource(ERuntimeEnvironmentType.COMMON_CLIENT).getItems().add(
                                JmlTagInvocation.Factory.newInstance(item2.getPropKey()));
                    }

                    keySetter.getSource(ERuntimeEnvironmentType.COMMON_CLIENT).getItems().add(
                            Sqml.Text.Factory.newInstance(
                            ".setValueObject(null);\n}"));
                }
            }
        }

        AdsCommandHandlerMethodDef cmdHndl = AdsCommandHandlerMethodDef.Factory.newInstance(cmdApFamilyList);
        editorPres.getModel().getMethods().getLocal().add(cmdHndl);


        MethodParameter par = MethodParameter.Factory.newInstance();
        par.setName("command");
        par.setType(AdsTypeDeclaration.Factory.newInstance(cmdApFamilyList));
        cmdHndl.getProfile().getParametersList().add(par);

        cmdHndl.getSource().getItems().add(Sqml.Text.Factory.newInstance(
                "try{\n"
                + "  if (!(getContext() instanceof org.radixware.kernel.common.client.models.IContext.SelectorRow)){\n"
                + "    if ( "));
        cmdHndl.getSource().getItems().add(JmlTagInvocation.Factory.newInstance(presRoleIdProp));

        cmdHndl.getSource().getItems().add(Sqml.Text.Factory.newInstance(
                ".getValue() !=null){\n"
                + "       org.radixware.schemas.adsdef.RoleIDDocument RoleID = org.radixware.schemas.adsdef.RoleIDDocument.Factory.newInstance();\n"
                + "       RoleID.RoleID = org.radixware.schemas.adsdef.RoleIDDocument.RoleID.Factory.newInstance();\n"
                + "       RoleID.RoleID.Id = "));
        cmdHndl.getSource().getItems().add(
                new JmlTagTypeDeclaration(AdsTypeDeclaration.Factory.newInstance(typeId)));
        cmdHndl.getSource().getItems().add(Sqml.Text.Factory.newInstance(
                ".Factory.loadFrom("));

        cmdHndl.getSource().getItems().add(JmlTagInvocation.Factory.newInstance(presRoleIdProp));
        cmdHndl.getSource().getItems().add(Sqml.Text.Factory.newInstance(
                ".getValue());\n"
                + "       org.radixware.schemas.adsdef.APFamiliesDocument.APFamilies output = command.send(RoleID).APFamilies;\n"
                + "       boolean flag;\n"));
        for (ApfItem item : apfItems) {
            cmdHndl.getSource().getItems().add(Sqml.Text.Factory.newInstance(
                    "\n       flag = output.IdList.contains("));

            JmlTagId tagId;
            if (item.getApf().getHeadId().getPrefix() == EDefinitionIdPrefix.DDS_TABLE) {
                tagId = new JmlTagId((DdsTableDef) item.getApf().findHead());
            } else {
                tagId = new JmlTagId((AdsEnumDef) item.getApf().findHead());
            }
            cmdHndl.getSource().getItems().add(tagId);

            cmdHndl.getSource().getItems().add(Sqml.Text.Factory.newInstance(") || "));

            cmdHndl.getSource().getItems().add(JmlTagInvocation.Factory.newInstance(item.modePropPres));
            cmdHndl.getSource().getItems().add(Sqml.Text.Factory.newInstance(".getValue().Value!=0;\n       "));
            cmdHndl.getSource().getItems().add(JmlTagInvocation.Factory.newInstance(item.modePropPres));
            cmdHndl.getSource().getItems().add(Sqml.Text.Factory.newInstance(".setVisible(flag);\n       "));
            if (item.isTable) {
                cmdHndl.getSource().getItems().add(JmlTagInvocation.Factory.newInstance(item.linkPropPres));
            } else {
                cmdHndl.getSource().getItems().add(JmlTagInvocation.Factory.newInstance(item.keyPropPres));
            }
            cmdHndl.getSource().getItems().add(Sqml.Text.Factory.newInstance(".setVisible(flag && "));
            cmdHndl.getSource().getItems().add(JmlTagInvocation.Factory.newInstance(item.modePropPres));
            cmdHndl.getSource().getItems().add(Sqml.Text.Factory.newInstance(".getValue().Value==1);\n"));
        }
        cmdHndl.getSource().getItems().add(Sqml.Text.Factory.newInstance("      }\n     else{\n"));

        for (ApfItem item : apfItems) {
            cmdHndl.getSource().getItems().add(Sqml.Text.Factory.newInstance("       "));
            cmdHndl.getSource().getItems().add(JmlTagInvocation.Factory.newInstance(item.modePropPres));
            cmdHndl.getSource().getItems().add(Sqml.Text.Factory.newInstance(".setVisible(false);\n       "));
            if (item.isTable) {
                cmdHndl.getSource().getItems().add(JmlTagInvocation.Factory.newInstance(item.linkPropPres));
            } else {
                cmdHndl.getSource().getItems().add(JmlTagInvocation.Factory.newInstance(item.keyPropPres));
            }
            cmdHndl.getSource().getItems().add(Sqml.Text.Factory.newInstance(".setVisible(false);\n\n"));

        }
        cmdHndl.getSource().getItems().add(Sqml.Text.Factory.newInstance("       }\n     }\n   }\n"
                + "catch(org.radixware.kernel.common.exceptions.ServiceClientException e){\n"
                + "    showException(e);\n"
                + "}catch(java.lang.InterruptedException e){\n"
                + " 	showException(e);\n}"));


        AdsUserMethodDef afterReadMethod = (AdsUserMethodDef) editorPres.getModel().getMethods().override(entityAfterReadMethod);
        afterReadMethod.getSource().getItems().clear();
        afterReadMethod.getSource().getItems().add(Sqml.Text.Factory.newInstance("this.getCommand("));
        afterReadMethod.getSource().getItems().add(new JmlTagId(cmdApFamilyList));
        afterReadMethod.getSource().getItems().add(Sqml.Text.Factory.newInstance(").execute();\n"));

        if (isUser) {
            afterReadMethod.getSource().getItems().add(Sqml.Text.Factory.newInstance("\nString currUser = (String)"));
            afterReadMethod.getSource().getItems().add(JmlTagInvocation.Factory.newInstance(this.userOrGroupNameProp));
            afterReadMethod.getSource().getItems().add(
                    Sqml.Text.Factory.newInstance(
                    ".ValueObject;\n"
                    + "String currConnectionUser = getEnvironment().UserName;\n"
                    + "\nif (currConnectionUser.equals(currUser))\n{\n"
                    + "   this.getRestrictions().setDeleteRestricted(true);\n"
                    + "   this.getRestrictions().setDeleteRestricted(true);\n"
                    + "   this."));
            afterReadMethod.getSource().getItems().add(JmlTagInvocation.Factory.newInstance(this.roleTitleProp));
            afterReadMethod.getSource().getItems().add(
                    Sqml.Text.Factory.newInstance(
                    ".setEnabled(false);\n"
                    + "   this."));

            afterReadMethod.getSource().getItems().add(JmlTagInvocation.Factory.newInstance(this.userOrGroupNamePropParentRef));
            afterReadMethod.getSource().getItems().add(
                    Sqml.Text.Factory.newInstance(
                    ".setEnabled(false);\n"));
            for (ApfItem item : apfItems) {
                afterReadMethod.getSource().getItems().add(
                        Sqml.Text.Factory.newInstance("   this."));
                afterReadMethod.getSource().getItems().add(JmlTagInvocation.Factory.newInstance(item.modePropPres));
                afterReadMethod.getSource().getItems().add(
                        Sqml.Text.Factory.newInstance(".setEnabled(false);\n   this."));
                afterReadMethod.getSource().getItems().add(JmlTagInvocation.Factory.newInstance(item.linkPropPres == null ? item.keyPropPres : item.linkPropPres));
                afterReadMethod.getSource().getItems().add(
                        Sqml.Text.Factory.newInstance(".setEnabled(false);\n"));
            }

            afterReadMethod.getSource().getItems().add(
                    Sqml.Text.Factory.newInstance("}"));


        }




        //presRoleTitleProp
        //presUserOrGroupNameProp


        presRoleIdProp.setSetterDefined(true);
        Setter roleIdSetter = presRoleIdProp.findSetter(EScope.LOCAL).get();
        roleIdSetter.getSource(ERuntimeEnvironmentType.COMMON_CLIENT).getItems().clear();

        roleIdSetter.getSource(ERuntimeEnvironmentType.COMMON_CLIENT).getItems().add(JmlTagInvocation.Factory.newInternalPropAccessor(presRoleIdProp));
        roleIdSetter.getSource(ERuntimeEnvironmentType.COMMON_CLIENT).getItems().add(
                Sqml.Text.Factory.newInstance(
                "= val;\n"));

        roleIdSetter.getSource(ERuntimeEnvironmentType.COMMON_CLIENT).getItems().add(
                new JmlTagTypeDeclaration(AdsTypeDeclaration.Factory.newInstance(editorPres.getModel())));

        roleIdSetter.getSource(ERuntimeEnvironmentType.COMMON_CLIENT).getItems().add(Sqml.Text.Factory.newInstance(".this.getCommand("));
        roleIdSetter.getSource(ERuntimeEnvironmentType.COMMON_CLIENT).getItems().add(new JmlTagId(cmdApFamilyList));
        roleIdSetter.getSource(ERuntimeEnvironmentType.COMMON_CLIENT).getItems().add(Sqml.Text.Factory.newInstance(").execute();"));
    }

    private void getEditorPropertiesOrder() {
        Id id = isUser ? PREDEFINED_EDITOR_PRESENTATION_ID_FOR_USER2ROLE : PREDEFINED_EDITOR_PRESENTATION_ID_FOR_USER_GROUP2ROLE;
        EntityPresentations entityPresentations = classDef.getPresentations();
        AdsEditorPresentationDef currPres = null;
        for (AdsEditorPresentationDef pres : entityPresentations.getEditorPresentations().getLocal()) {
            if (pres.getId().equals(id)) {
                currPres = pres;
                break;
            }
        }
        if (currPres != null) {
            List<AdsEditorPageDef> editorPageList = currPres.getEditorPages().get(EScope.ALL);
            AdsEditorPageDef ePage = null;
            for (AdsEditorPageDef editorPage : editorPageList) {
                if (editorPage.getName().equals(PREDEFINED_MAIN_EDITOR_PAGE_NAME)) {
                    ePage = editorPage;
                    break;
                }
            }
            if (ePage != null) {
                int i = 0;
                int n = apfItems.size();

                List<AdsPropertyDef> propList = classDef.getProperties().get(EScope.ALL);
                List<Id> idList = ePage.getProperties().getIds();
                for (Id currId : idList) {
                    for (AdsPropertyDef prop : propList) {
                        if (prop instanceof AdsInnateColumnPropertyDef && prop.getId().equals(currId)) {
                            AdsInnateColumnPropertyDef colProp = (AdsInnateColumnPropertyDef) prop;
                            DdsColumnDef column = colProp.getColumnInfo().findColumn();
                            if (column != null) {
                                String dbName = column.getDbName();
                                if (dbName.length() == 30 && dbName.substring(0, 4).equals("Ma$$")) {
                                    dbName = EDefinitionIdPrefix.DDS_ACCESS_PARTITION_FAMILY.getValue() + dbName.substring(4);
                                    Id apfId = Id.Factory.loadFrom(dbName);
                                    for (int j = i; j < n; j++) {
                                        ApfItem item = apfItems.get(j);
                                        if (item.getApf().getId().equals(apfId)) {
                                            if (i != j) {
                                                ApfItem oldItem = apfItems.get(i);
                                                //int oldInd = oldItem.getParentAPFIndex();

                                                apfItems.set(i, item);
                                                apfItems.set(j, oldItem);
                                            }
                                            i++;
                                            break;
                                        }
                                    }
                                }

                            }
                            break;
                        }
                    }
                }

            }
        }
    }

    private void check() {
        if (classDef == null) {
            throw new DefinitionError("Entity class def not defined", this);
        }
        overwrittenClass = (AdsEntityClassDef) classDef.getHierarchy().findOverwritten().get();
        isInherit = overwrittenClass != null;


        if (classDef.getId().equals(USER2ROLE_ID)) {
            isUser = true;
        } else if (classDef.getId().equals(USERGROUP2ROLE_ID)) {
            isUser = false;
        } else {
            throw new DefinitionError("Incorrect entity class", this);
        }
        table = classDef.findTable(classDef);

        if (table == null) {
            throw new DefinitionError("Table for this entity class not found", this);
        }

        apfCollection =
                RadixObjectsUtils.collectAllAround(classDef,
                org.radixware.kernel.common.defs.dds.providers.DdsVisitorProviderFactory.newAccessPartitionFamilyProvider());
        if (apfCollection == null || apfCollection.isEmpty()) {
            throw new DefinitionError("Access partition family list is empty", this);
        }

        accessAreaMode = (AdsEnumDef) this.classDef.getModule().getDefinitionSearcher().findById(ACCESSAREAMODE_ID).get();

        if (accessAreaMode == null) {
            throw new DefinitionError("System enum \"AccessAreaMode\" is not found", this);
        }

        AdsDef = (AdsXmlSchemeDef) this.classDef.getModule().getDefinitionSearcher().findById(ADSDEF_ID).get();


        DefinitionSearcher<DdsPackageDef> srch = AdsSearcher.Factory.newDdsPackageSearcher(classDef);
        acsUtils = srch.findById(PACKAGE_ACS_UTILSID).get();

        if (acsUtils == null) {
            throw new DefinitionError("System package \"Acs_Utils\" is not found", this);
        }

        acs = srch.findById(PACKAGE_ACS).get();
        if (acs == null) {
            throw new DefinitionError("System package \"Acs\" is not found", this);
        }


        compileRightsForGroup = (DdsFunctionDef) acsUtils.find(new VisitorProvider() {
            @Override
            public boolean isTarget(RadixObject radixObject) {

                if (radixObject instanceof Definition) {
                    Definition def = (Definition) radixObject;
                    return def.getId().equals(COMPILE_RIGHTS_FOR_GROUP_ID);
                }
                return false;
            }
        });
        curUserIsInGroup = (DdsFunctionDef) acs.find(new VisitorProvider() {
            @Override
            public boolean isTarget(RadixObject radixObject) {
                if (radixObject instanceof Definition) {
                    Definition def = (Definition) radixObject;
                    return def.getId().equals(CURR_USER_IS_IN_GROUP_ID);
                }
                return false;
            }
        });

        if (compileRightsForGroup == null) {
            throw new DefinitionError("System package function \"Acs.compileRightsForGroup\" is not found", this);
        }

        if (curUserIsInGroup == null) {
            throw new DefinitionError("System package function \"Acs_Utils.curUserIsInGroup\" is not found", this);
        }



        if (AdsDef == null) {
            throw new DefinitionError("System schema \"AdsDef\" is not found", this);
        }

        typeId = (AdsClassDef) this.classDef.getModule().getDefinitionSearcher().findById(TYPE_ID_ID).get();

        if (typeId == null) {
            throw new DefinitionError("System class \"Radix::Radix.Types::Id\" is not found", this);
        }


        DefinitionSearcher searcher = AdsSearcher.Factory.newAdsDefinitionSearcher(classDef);
        superAdmin = (AdsRoleDef) searcher.findById(ROLESUPERADMINID).get();

        if (superAdmin == null) {
            throw new DefinitionError("System role \"SuperAdmin\" is not found", this);
        }

        entityClass = (AdsClassDef) searcher.findById(ENTITY_ID).get();

        if (entityClass == null) {
            throw new DefinitionError("System dinamic class \"entity\" is not found", this);
        }

        utilsClass = (AdsClassDef) searcher.findById(UTILSID).get();

        if (utilsClass == null) {
            throw new DefinitionError("System dinamic class \"utils\" is not found", this);
        }

        Definitions<AdsMethodDef> lst = utilsClass.getMethods().getLocal();
        for (AdsMethodDef method : lst) {
            if (method.getName().equals(GETROLETITLENAME)) {
                getRoleTitle = method;
                break;
            }
        }

        if (getRoleTitle == null) {
            throw new DefinitionError("System method \"getRoleTitle\" is not found", this);
        }


        chooseRoleDlg = (AdsCustomPropEditorDef) searcher.findById(CHOOSEROLEDLGID).get();
        if (chooseRoleDlg == null) {
            throw new DefinitionError("System custom prop editor dialog \"ChooseRoleDlg\" is not found", this);
        }

        roleHolderInterface = (AdsInterfaceClassDef) searcher.findById(ROLE_HOLDER_ID).get();
        if (roleHolderInterface == null) {
            throw new DefinitionError("System interface \"RoleHolder\" is not found", this);
        }


        Definitions<AdsMethodDef> roleHolderMethods = roleHolderInterface.getMethods().getLocal();
        if (roleHolderMethods.findById(getRoleIdPropID) == null
                || roleHolderMethods.findById(getRoleTitlePropID) == null) {
            throw new DefinitionError("Incorrect system interface \"RoleHolder\"", this);
        }

        AdsSegment s = (AdsSegment) classDef.getModule().getSegment();
        IPlatformClassPublisher platformClassPublisher = s.getBuildPath().getPlatformPublishers().findPublisherByName(AdsEntityModelClassDef.ENTITY_MODEL_JAVA_CLASS_NAME);
        if (platformClassPublisher == null || !(platformClassPublisher instanceof AdsClassDef)) {
            throw new DefinitionError("System class \"EntityModel\" is not found", this);
        }

        AdsClassDef entityModel = (AdsClassDef) platformClassPublisher;
        for (AdsMethodDef m : entityModel.getMethods().getLocal()) {
            if (m.getName().equals("afterRead")) {
                entityAfterReadMethod = m;
                break;
            }
        }
        if (entityAfterReadMethod == null) {
            throw new DefinitionError("System method \"EntityModel.afterRead\" not found", this);
        }


        // sort apf by name
        apfItems = new ArrayList<>(apfCollection.size());
        for (Definition def : apfCollection) {
            apfItems.add(new ApfItem((DdsAccessPartitionFamilyDef) def));
        }
        org.radixware.kernel.common.utils.RadixObjectsUtils.sortByName(apfItems);

        for (ApfItem item : apfItems) {
            if (item.getApf().findHead() == null) {
                throw new DefinitionError("Access partition family head not found - \""
                        + item.getApf().getHeadId().toString() + "\"", this);
            }
            /*
             * item.isInOurLayer =
             * item.getApf().getModule().getSegment().getLayer() ==
             * this.classDef.getModule().getSegment().getLayer();
             */
        }

        // Search system table
        //table.getColumns()
        //for (int i =0;i< )




        int k = 0;
        for (ApfItem item : apfItems) {
            if (item.getApf().getParentFamilyReferenceId() != null) {
                DdsReferenceDef parentRef = item.getApf().findParentFamilyReference();
                String sError = "Incorrect access partition family parent reference  \""
                        + item.getApf().getQualifiedName() + "\"";
                if (parentRef == null) {
                    throw new DefinitionError(sError, this);
                }
                DdsTableDef tbl = parentRef.findParentTable(classDef);
                if (tbl == null) {
                    throw new DefinitionError(sError, this);
                }
                if (parentRef.getColumnsInfo().size() != 1) {
                    throw new DefinitionError(sError, this);
                }

                DdsColumnDef column = parentRef.getColumnsInfo().get(0).findChildColumn();
                if (column == null) {
                    throw new DefinitionError(sError, this);
                }
                item.setParentAPFColumn(column);


                int i = 0;
                for (ApfItem item2 : apfItems) {
                    if (item2.getApf().findHead() == (Definition) tbl) {
                        item.setParentAPFItem(item2);
                        item2.getChildApfIndex().add(item);
                        break;
                    }
                    i++;
                }
                if (item.getParentApfItem() == null) {
                    throw new DefinitionError(sError, this);
                }
            }
            k++;
        }


        List<DdsColumnDef> сolumns = table.getColumns().get(EScope.ALL);
        int ind = 0;
        for (DdsColumnDef сolumn : сolumns) {
            String dbName = сolumn.getDbName();


            if (dbName.length() == 30) {
                String sId = dbName.substring(1);
                //String sPreff = sId.substring(0, 3);
                //boolean isTbl = sPreff.equals("t$$");
                // boolean isEnum = sPreff.equals("a$$");
                // if (isTbl || isEnum)
                {
                    sId = "apf" + sId.substring(3);
                    /*
                     * if (isTbl) { sId = "tbl" + sId.substring(3); } else { sId
                     * = "acs" + sId.substring(3); }
                     */
                    Id id = Id.Factory.loadFrom(sId);
                    if (dbName.charAt(0) == 'P') {
                        int index = findApfById(id);
                        if (index >= 0) {
                            if (apfItems.get(index).getKeyIndexInColumns() > -1) {
                                throw new DefinitionError("Dublicate access partition family key column - "
                                        + apfItems.get(index).getApf().getName(), this);
                            } else {
                                apfItems.get(index).setKeyIndexInColumns(ind);
                            }

                            // find ref
                            if (apfItems.get(index).isTable) {
                                //System.out.println("Start search reference");
                                DdsTableDef tbl = (DdsTableDef) apfItems.get(index).getApf().findHead();
                                //System.out.println("tbl " + tbl.getName());
                                Iterator<DdsReferenceDef> iter = table.collectOutgoingReferences().iterator();
                                while (iter.hasNext()) {
                                    DdsReferenceDef r = iter.next();
                                    //System.out.println("ref " + r.getName());
                                    if (r.getParentTableId().equals(tbl.getId())) {
                                        List<DdsReferenceDef.ColumnsInfoItem> list = r.getColumnsInfo().list();
                                        if (list.size() == 1) {
                                            //System.out.println("@@ " + list.get(0).findChildColumn().getId().toString() + "~" + сolumn.getId().toString());
                                            //list.get(0).getChildColumnId().equals(id);
                                            if (list.get(0).findChildColumn().equals(сolumn)) {
                                                apfItems.get(index).setRef(r);
                                                //AdsEntityObjectClassDef referencedEntityClass;
                                                //r.
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            throw new DefinitionError("Access partition family for column \"" + сolumn.getName() + "\" not found.");
                        }
                    } else if (dbName.charAt(0) == 'M') {
                        int index = findApfById(id);
                        if (index >= 0) {
                            if (apfItems.get(index).getModeIndexInColumns() > -1) {
                                throw new DefinitionError(
                                        "Dublicate access partition family mode column - "
                                        + apfItems.get(index).getApf().getName(), this);
                            } else {
                                apfItems.get(index).setModeIndexInColumns(ind);
                            }
                        } else {
                            throw new DefinitionError("Access partition family for column \"" + сolumn.getName() + "\" not found.");
                        }
                    } else {
                        // do nothing
                    }

                }
            }
            ind++;
        }

        for (ApfItem item : apfItems) {
            if (item.getKeyIndexInColumns() == -1) {
                throw new DefinitionError(
                        "Column(key) from access partition family(" + item.getApf().getName() + ") is not found", this);
            }
            if (item.getModeIndexInColumns() == -1) {
                throw new DefinitionError(
                        "Column(mode) from access partition family(" + item.getApf().getName() + ") is not found", this);
            }
            if (item.isTable && item.getRef() == null) {
                throw new DefinitionError(
                        "Reference " + table.getName() + "-->" + item.getApf().getName() + " is not found", this);
            }
        }


        if (isInherit) {

            // item.isInOurLayer
            //linkPropInherit
            List<AdsPropertyDef> list = overwrittenClass.getProperties().get(EScope.ALL);
            String userOrGroupName = isUser ? USERNAME : USERGROUPNAME;
            String userOrGroupTitle = isUser ? USERTITLE : USERGROUPTITLE;
            for (AdsPropertyDef prop : list) {
                if (prop.getName().equals(userOrGroupName) && prop instanceof AdsInnateColumnPropertyDef) {
                    userOrGroupNamePropInh = (AdsInnateColumnPropertyDef) prop;
                } else if (prop.getName().equals(ROLEIDNAME) && prop instanceof AdsInnateColumnPropertyDef) {
                    roleIdPropInh = (AdsInnateColumnPropertyDef) prop;
                } else if (prop.getName().equals(ROLETITLENAME) && prop instanceof AdsDynamicPropertyDef) {
                    roleTitlePropInh = (AdsDynamicPropertyDef) prop;
                } else if (prop.getName().equals(userOrGroupTitle) && prop instanceof AdsInnateRefPropertyDef) {
                    userOrGroupNamePropParentRefInh = (AdsInnateRefPropertyDef) prop;
                } else if (prop.getName().equals(IDNAME) && prop instanceof AdsInnateColumnPropertyDef) {
                    idPropInh = (AdsInnateColumnPropertyDef) prop;
                } else if (isUser && prop.getName().equals(ISOWN) && prop instanceof AdsInnateColumnPropertyDef) {
                    isOwnPropInh = (AdsInnateColumnPropertyDef) prop;
                }
            }



            for (ApfItem item : apfItems) {
                //keyPropInherit
                String postfix = "$$" + item.getApf().getHeadId().toString().substring(3);
                for (AdsPropertyDef prop : list) {
                    if (prop instanceof AdsInnateColumnPropertyDef) {
                        AdsInnateColumnPropertyDef icpd = (AdsInnateColumnPropertyDef) prop;
                        DdsColumnDef column = icpd.getColumnInfo().findColumn();
                        if (column != null) {
                            String sDBName = column.getDbName();
                            if (sDBName.length() == 30) {
                                if (sDBName.substring(2).equals(postfix)) {
                                    if ('P' == sDBName.charAt(0)) {
                                        item.keyPropInherit = icpd;
                                    } else if (sDBName.charAt(0) == 'M') {
                                        item.modePropInherit = icpd;
                                    }
                                }
                            }
                            //System.out.println(sDBName);
                        }
                    }
                }

                if (item.isTable) {
                    for (AdsPropertyDef prop : list) {
                        if (prop instanceof AdsInnateRefPropertyDef) {
                            AdsInnateRefPropertyDef innateLnk = (AdsInnateRefPropertyDef) prop;
                            if (item.getRef().getId().equals(innateLnk.getParentReferenceInfo().getParentReferenceId())) {
                                String name = innateLnk.getName();
                                int len = name.length();
                                if (len > 5
                                        && name.substring(0, 2).equals("ap")
                                        && name.substring(len - 4).equals("Link")) {
                                    item.linkPropInherit = innateLnk;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void createServerMethods() {
        if (isInherit) {
            return;
        }
        List<AdsMethodDef> list = entityClass.getMethods().get(EScope.LOCAL);
        //entityClass.getMethods().findBySignature(signature, EScope.LOCAL);
        for (int i = 0; i < 6; i++) {

            if (isUser && i > 3) {
                return;
            }

            String name = "";

            switch (i) {
                /*
                 * case -1: name = "afterRead"; break;
                 */
                case 0:
                    name = "afterCreate";
                    break;
                case 1:
                    name = "beforeUpdate";
                    break;
                case 2:
                    name = "beforeDelete";
                    break;
                case 3:
                    name = "beforeCreate";
                    break;
                case 4:
                    name = "afterUpdate";
                    break;
                case 5:
                    name = "afterDelete";
                    break;
            }
            AdsMethodDef ownMethod = null;
            for (AdsMethodDef method : list) {
                if (method.getName().equals(name)) {
                    ownMethod = method;
                    break;
                }
            }
            if (ownMethod == null) {
                throw new DefinitionError(
                        "entity method \"" + name + "\" not found", this);
            }

            //  char[] ZZ=  JavaSignatures.generateSignature(ownMethod, true);



            List<AdsMethodDef> currList = classDef.getMethods().getLocal().list();
            AdsMethodDef currMethod = null;
            for (AdsMethodDef m : currList) {
                if (m.getId().equals(ownMethod.getId())) {
                    currMethod = m;
                    break;
                }
            }
            if (currMethod == null) {
                currMethod = this.classDef.getMethods().override(ownMethod);
            }

            //currMethod.setOverride(true);

            if (currMethod instanceof AdsUserMethodDef) {
                AdsUserMethodDef userMethod = (AdsUserMethodDef) currMethod;
                userMethod.setOverride(true);
                userMethod.getProfile().getAccessFlags().setProtected();
                userMethod.getProfile().getAccessFlags().setStatic(false);
                userMethod.getProfile().getAccessFlags().setFinal(false);
                userMethod.getProfile().getAccessFlags().setAbstract(false);
                userMethod.getProfile().getAccessFlags().setDeprecated(false);

                userMethod.getSource().getItems().clear();

                switch (i) {
                    case 0:
                        /*
                         * roleTitleSource.getItems().add(Sqml.Text.Factory.newInstance(
                         * ") )\nreturn \"SuperAdmin\";\n" + "return
                         * getArte().DefManager.getDefinitionName("));
                         *
                         */


                        userMethod.getSource().getItems().add(
                                Sqml.Text.Factory.newInstance(
                                "HashMap<String, Rights.AccessPartition> accessArea = "));

                        userMethod.getSource().getItems().add(
                                JmlTagInvocation.Factory.newInstance(getArea));

                        userMethod.getSource().getItems().add(
                                Sqml.Text.Factory.newInstance(
                                "(getArte().getRights());\n"
                                + "String sRoleId = "));

                        userMethod.getSource().getItems().add(
                                JmlTagInvocation.Factory.newInstance(this.roleIdProp));

                        userMethod.getSource().getItems().add(
                                Sqml.Text.Factory.newInstance(
                                ";\n\n"
                                + "if (getArte().getRights().getUser" + (isUser ? "" : "Group")
                                + "HasRoleForObject("));

                        userMethod.getSource().getItems().add(
                                JmlTagInvocation.Factory.newInstance(this.userOrGroupNameProp));

                        userMethod.getSource().getItems().add(
                                Sqml.Text.Factory.newInstance(
                                ", sRoleId, accessArea, " + (isUser ? "false, " : "")));

                        userMethod.getSource().getItems().add(
                                JmlTagInvocation.Factory.newInstance(this.idProp));

                        userMethod.getSource().getItems().add(
                                Sqml.Text.Factory.newInstance(
                                ".intValue())){\n"
                                + "    throw new ServiceProcessClientFault(ExceptionEnum.ACCESS_VIOLATION.toString(), "));

                        JmlTagLocalizedString tag = JmlTagLocalizedString.Factory.newInstance(classDef, EIsoLanguage.ENGLISH, "Rights duplication");
                        userMethod.getSource().getItems().add(tag);
                        tag.findLocalizedString(tag.getStringId()).setValue(EIsoLanguage.RUSSIAN, "Дублирование прав");


                        userMethod.getSource().getItems().add(
                                Sqml.Text.Factory.newInstance(
                                ", null, null);\n"
                                + "   }\n"));



                        if (!isUser) {
                            userMethod.getSource().getItems().add(
                                    Sqml.Text.Factory.newInstance(
                                    "getArte().getRights().compileGroupRights("));


                            userMethod.getSource().getItems().add(
                                    JmlTagInvocation.Factory.newInstance(this.userOrGroupNameProp));


                            userMethod.getSource().getItems().add(
                                    Sqml.Text.Factory.newInstance(
                                    ", true);\n"));
                        }

                        userMethod.getSource().getItems().add(
                                Sqml.Text.Factory.newInstance(
                                "return;"));

                        break;
                    case 1:

                        if (!isUser) {
                            userMethod.getSource().getItems().add(
                                    Sqml.Text.Factory.newInstance(
                                    "if (getArte().Rights.isCurUserInGroup("));

                            userMethod.getSource().getItems().add(
                                    JmlTagInvocation.Factory.newInstance(this.userOrGroupNameProp));

                            userMethod.getSource().getItems().add(
                                    Sqml.Text.Factory.newInstance(
                                    ")){\n"
                                    + "  throw new ServiceProcessClientFault(ExceptionEnum.ACCESS_VIOLATION.toString(), "));

                            tag = JmlTagLocalizedString.Factory.newInstance(classDef, EIsoLanguage.ENGLISH, "Attempt to modify own rights");
                            userMethod.getSource().getItems().add(tag);
                            tag.findLocalizedString(tag.getStringId()).setValue(EIsoLanguage.RUSSIAN, "Попытка изменить собственные права");

                            userMethod.getSource().getItems().add(
                                    Sqml.Text.Factory.newInstance(
                                    ", null, null);\n}\n"));
                        }

                        userMethod.getSource().getItems().add(
                                Sqml.Text.Factory.newInstance(
                                "HashMap<String, Rights.AccessPartition> accessArea = "));

                        userMethod.getSource().getItems().add(
                                JmlTagInvocation.Factory.newInstance(getArea));

                        userMethod.getSource().getItems().add(
                                Sqml.Text.Factory.newInstance(
                                "(getArte().getRights());\n"
                                + "String sRoleId = "));

                        userMethod.getSource().getItems().add(
                                JmlTagInvocation.Factory.newInstance(this.roleIdProp));

                        userMethod.getSource().getItems().add(
                                Sqml.Text.Factory.newInstance(
                                ";\n\n"
                                + "if (!getArte().getRights().curUserHasRightsUser"
                                + (isUser ? "" : "Group") + "2Role("));

                        userMethod.getSource().getItems().add(
                                JmlTagInvocation.Factory.newInstance(this.idProp));
                        userMethod.getSource().getItems().add(
                                Sqml.Text.Factory.newInstance(
                                ".intValue())){\n"
                                + "    throw new ServiceProcessClientFault(ExceptionEnum.ACCESS_VIOLATION.toString(), "));
                        //\"Attempt to modify exceeding rights\"" +
                        tag = JmlTagLocalizedString.Factory.newInstance(classDef, EIsoLanguage.ENGLISH, "Attempt to modify exceeding rights");
                        userMethod.getSource().getItems().add(tag);
                        tag.findLocalizedString(tag.getStringId()).setValue(EIsoLanguage.RUSSIAN, "Попытка изменить права, которыми пользователь не обладает");

                        userMethod.getSource().getItems().add(
                                Sqml.Text.Factory.newInstance(
                                ", null, null);\n"
                                + "   }\n\n"
                                + "if (!getArte().getRights().getUserHasRoleForObject(getArte().getUserName(), sRoleId, accessArea)){\n"
                                + "    throw new ServiceProcessClientFault(ExceptionEnum.ACCESS_VIOLATION.toString(), "));
                        //\"Attempt to set exceeding rights\"" +

                        tag = JmlTagLocalizedString.Factory.newInstance(classDef, EIsoLanguage.ENGLISH, "Attempt to set exceeding rights");
                        userMethod.getSource().getItems().add(tag);
                        tag.findLocalizedString(tag.getStringId()).setValue(EIsoLanguage.RUSSIAN, "Попытка установить права, которыми пользователь не обладает");

                        userMethod.getSource().getItems().add(
                                Sqml.Text.Factory.newInstance(", null, null);\n"
                                + "   }\n\n"));

                        userMethod.getSource().getItems().add(
                                Sqml.Text.Factory.newInstance(
                                "if (getArte().getRights().getUser" + (isUser ? "" : "Group") + "HasRoleForObject("));
                        userMethod.getSource().getItems().add(
                                JmlTagInvocation.Factory.newInstance(this.userOrGroupNameProp));

                        userMethod.getSource().getItems().add(
                                Sqml.Text.Factory.newInstance(
                                ", sRoleId, accessArea, " + (isUser ? "false, " : "")));
                        userMethod.getSource().getItems().add(
                                JmlTagInvocation.Factory.newInstance(this.idProp));

                        userMethod.getSource().getItems().add(
                                Sqml.Text.Factory.newInstance(
                                ".intValue())){\n"
                                + "    throw new ServiceProcessClientFault(ExceptionEnum.ACCESS_VIOLATION.toString(), "));
                        //\"Detected dubbed rights\"" +

                        tag = JmlTagLocalizedString.Factory.newInstance(classDef, EIsoLanguage.ENGLISH, "Rights duplication");
                        userMethod.getSource().getItems().add(tag);
                        tag.findLocalizedString(tag.getStringId()).setValue(EIsoLanguage.RUSSIAN, "Дублирование прав");

                        userMethod.getSource().getItems().add(
                                Sqml.Text.Factory.newInstance(", null, null);\n"
                                + "   }\n"
                                + "return true;"));


                        break;
                    case 2:

                        if (!isUser) {
                            userMethod.getSource().getItems().add(
                                    Sqml.Text.Factory.newInstance(
                                    "if (getArte().Rights.isCurUserInGroup("));

                            userMethod.getSource().getItems().add(
                                    JmlTagInvocation.Factory.newInstance(this.userOrGroupNameProp));

                            userMethod.getSource().getItems().add(
                                    Sqml.Text.Factory.newInstance(
                                    ")){\n"
                                    + "  throw new ServiceProcessClientFault(ExceptionEnum.ACCESS_VIOLATION.toString(), "));

                            tag = JmlTagLocalizedString.Factory.newInstance(classDef, EIsoLanguage.ENGLISH, "Attempt to revoke own rights");
                            userMethod.getSource().getItems().add(tag);
                            tag.findLocalizedString(tag.getStringId()).setValue(EIsoLanguage.RUSSIAN, "Попытка удалить собственные права");

                            userMethod.getSource().getItems().add(
                                    Sqml.Text.Factory.newInstance(
                                    ", null, null);\n}\n"));
                        }






                        userMethod.getSource().getItems().add(
                                Sqml.Text.Factory.newInstance(
                                "HashMap<String, Rights.AccessPartition> accessArea = "));

                        userMethod.getSource().getItems().add(
                                JmlTagInvocation.Factory.newInstance(getArea));

                        userMethod.getSource().getItems().add(
                                Sqml.Text.Factory.newInstance(
                                "(getArte().getRights());\n"
                                + "String sRoleId = "));

                        userMethod.getSource().getItems().add(
                                JmlTagInvocation.Factory.newInstance(this.roleIdProp));

                        userMethod.getSource().getItems().add(
                                Sqml.Text.Factory.newInstance(
                                ";\n\n"
                                + "if (!getArte().getRights().getUserHasRoleForObject(getArte().getUserName(), sRoleId, accessArea)){\n"
                                + "    throw new ServiceProcessClientFault(ExceptionEnum.ACCESS_VIOLATION.toString(), "));
                        //"\"Attempt to delete exceeding rights\"" +
                        tag = JmlTagLocalizedString.Factory.newInstance(classDef, EIsoLanguage.ENGLISH, "Attempt to revoke exceeding rights");
                        userMethod.getSource().getItems().add(tag);
                        tag.findLocalizedString(tag.getStringId()).setValue(EIsoLanguage.RUSSIAN, "Попытка забрать права, которыми пользователь не обладает");

                        userMethod.getSource().getItems().add(
                                Sqml.Text.Factory.newInstance(
                                ", null, null);\n"
                                + "   }\n"));

                        if (!isUser) {
                            userMethod.getSource().getItems().add(
                                    Sqml.Text.Factory.newInstance(
                                    "sLastGroupName = "));

                            userMethod.getSource().getItems().add(
                                    JmlTagInvocation.Factory.newInstance(this.userOrGroupNameProp));

                            userMethod.getSource().getItems().add(
                                    Sqml.Text.Factory.newInstance(
                                    ";\n"));

                        }



                        userMethod.getSource().getItems().add(
                                Sqml.Text.Factory.newInstance(
                                "return true;"));

                        break;
                    case 3:
                        if (!isUser) {
                            userMethod.getSource().getItems().add(
                                    Sqml.Text.Factory.newInstance(
                                    "if (getArte().Rights.isCurUserInGroup("));

                            userMethod.getSource().getItems().add(
                                    JmlTagInvocation.Factory.newInstance(this.userOrGroupNameProp));

                            userMethod.getSource().getItems().add(
                                    Sqml.Text.Factory.newInstance(
                                    ")){\n"
                                    + "  throw new ServiceProcessClientFault(ExceptionEnum.ACCESS_VIOLATION.toString(), "));

                            tag = JmlTagLocalizedString.Factory.newInstance(classDef, EIsoLanguage.ENGLISH, "Attempt to give rights own group");
                            userMethod.getSource().getItems().add(tag);
                            tag.findLocalizedString(tag.getStringId()).setValue(EIsoLanguage.RUSSIAN, "Попытка дать права собственной группе");

                            userMethod.getSource().getItems().add(
                                    Sqml.Text.Factory.newInstance(
                                    ", null, null);\n}\n"));
                        }




                        userMethod.getSource().getItems().add(
                                Sqml.Text.Factory.newInstance(
                                "HashMap<String, Rights.AccessPartition> accessArea = "));

                        userMethod.getSource().getItems().add(
                                JmlTagInvocation.Factory.newInstance(getArea));

                        userMethod.getSource().getItems().add(
                                Sqml.Text.Factory.newInstance(
                                "(getArte().getRights());\n"
                                + "String sRoleId = "));

                        userMethod.getSource().getItems().add(
                                JmlTagInvocation.Factory.newInstance(this.roleIdProp));

                        userMethod.getSource().getItems().add(
                                Sqml.Text.Factory.newInstance(
                                ";\n\n"
                                + "if (!getArte().getRights().getUserHasRoleForObject(getArte().getUserName(), sRoleId, accessArea)){\n"
                                + "    throw new ServiceProcessClientFault(ExceptionEnum.ACCESS_VIOLATION.toString(), "));
                        //"\"Attempt to set exceeding rights\"" +
                        tag = JmlTagLocalizedString.Factory.newInstance(classDef, EIsoLanguage.ENGLISH, "Attempt to give exceeding rights");
                        userMethod.getSource().getItems().add(tag);
                        tag.findLocalizedString(tag.getStringId()).setValue(EIsoLanguage.RUSSIAN, "Попытка дать права, которыми пользователь не обладает");

                        userMethod.getSource().getItems().add(
                                Sqml.Text.Factory.newInstance(
                                ", null, null);\n"
                                + "   }\n\n"
                                + "return true;"));
                        break;
                    case 4:
                        userMethod.getSource().getItems().add(
                                Sqml.Text.Factory.newInstance(
                                "getArte().getRights().compileGroupRights("));

                        userMethod.getSource().getItems().add(
                                JmlTagInvocation.Factory.newInstance(this.userOrGroupNameProp));

                        userMethod.getSource().getItems().add(
                                Sqml.Text.Factory.newInstance(
                                ", true);"));

                        break;
                    case 5:
                        userMethod.getSource().getItems().add(
                                Sqml.Text.Factory.newInstance(
                                "getArte().getRights().compileGroupRights(sLastGroupName, true);"));
                        break;

                }






            }
        }
    }

    private void createCommand() {

        //classDef.getPresentations().

        //AdsCommandDef
        List<AdsScopeCommandDef> cmdList = classDef.getPresentations().getCommands().getLocal().list();
        cmdApFamilyList = null;
        for (AdsScopeCommandDef cm : cmdList) {
            if (cm.getName().equals(CMDGETAPFAMILYLIST)) {
                cmdApFamilyList = cm;
                break;
            }
        }
        AdsScopeCommandDef cmdApFamilyListInh = null;
        if (cmdApFamilyList == null) {
            if (isInherit) {
                List<AdsScopeCommandDef> cmdListInh = overwrittenClass.getPresentations().getCommands().getLocal().list();
                for (AdsScopeCommandDef cm : cmdListInh) {
                    if (cm.getName().equals(CMDGETAPFAMILYLIST)) {
                        cmdApFamilyListInh = cm;
                        break;
                    }
                }
            }
            if (cmdApFamilyListInh != null) {
                cmdApFamilyList = classDef.getPresentations().getCommands().getLocal().overwrite(cmdApFamilyListInh);
            } else {
                cmdApFamilyList = AdsScopeCommandDef.Factory.newInstance(ECommandScope.OBJECT);
                classDef.getPresentations().getCommands().getLocal().add(cmdApFamilyList);
            }
            cmdApFamilyList.setName(CMDGETAPFAMILYLIST);
        }

        cmdApFamilyList.getPresentation().setIsVisible(false);
        cmdApFamilyList.getPresentation().setIsConfirmationRequired(false);


        cmdApFamilyList.getData().setInType(AdsTypeDeclaration.Factory.newXml(AdsDef, "RoleIDDocument"));
        cmdApFamilyList.getData().setOutType(AdsTypeDeclaration.Factory.newXml(AdsDef, "APFamiliesDocument"));



        //CommandHandler ch
        AdsCommandHandlerMethodDef cmdHndl = null;

        List<AdsMethodDef> list = classDef.getMethods().getLocal().list();
        for (AdsMethodDef method : list) {
            if (method instanceof AdsCommandHandlerMethodDef) {
                AdsCommandHandlerMethodDef hndl = (AdsCommandHandlerMethodDef) method;
                if (hndl.findCommand() == cmdApFamilyList) {
                    cmdHndl = hndl;
                    break;
                }
            }
        }
        if (cmdHndl == null) {
            if (isInherit && cmdApFamilyListInh != null) {
                List<AdsMethodDef> listInh = overwrittenClass.getMethods().getLocal().list();
                for (AdsMethodDef method : listInh) {
                    if (method instanceof AdsCommandHandlerMethodDef) {
                        AdsCommandHandlerMethodDef hndl = (AdsCommandHandlerMethodDef) method;
                        if (hndl.findCommand() == cmdApFamilyListInh) {
                            cmdHndl = (AdsCommandHandlerMethodDef) classDef.getMethods().getLocal().overwrite(hndl);
                            break;
                        }
                    }
                }
            }
            if (cmdHndl == null) {
                cmdHndl = AdsCommandHandlerMethodDef.Factory.newInstance(cmdApFamilyList);
                classDef.getMethods().getLocal().add(cmdHndl);
            }
        }


        cmdHndl.getProfile().getParametersList().clear();
        {
            MethodParameter par = MethodParameter.Factory.newInstance();
            par.setName("input");
            par.setType(AdsTypeDeclaration.Factory.newXml(AdsDef, "RoleIDDocument"));
            cmdHndl.getProfile().getParametersList().add(par);
        }

        {

            MethodParameter par = MethodParameter.Factory.newInstance();
//            par.setName("newPropValsById");

//            par.setType(AdsTypeDeclaration.Factory.newPlatformClass("org.radixware.kernel.server.types.PropValHandlersByIdMap"));
//                      profile.add(AdsTypeDeclaration.Factory.newPlatformClass("org.radixware.kernel.server.types.PropValHandlersByIdMap"));
//                        argumentNames.add("newPropValHandlersById");


            par.setName("newPropValHandlersById");
            //org.radixware.kernel.common.types.Id.
            par.setType(AdsTypeDeclaration.Factory.newPlatformClass("org.radixware.kernel.server.types.PropValHandlersByIdMap"));
            cmdHndl.getProfile().getParametersList().add(par);

        }


        cmdHndl.getProfile().getReturnValue().setType(AdsTypeDeclaration.Factory.newXml(AdsDef, "APFamiliesDocument"));

        cmdHndl.getSource().getItems().clear();

        //utilsClass

        cmdHndl.getSource().getItems().add(Sqml.Text.Factory.newInstance(
                "org.radixware.schemas.adsdef.APFamiliesDocument  apFamiliesDocument =\n"
                + "org.radixware.schemas.adsdef.APFamiliesDocument.Factory.newInstance();\n\n"
                + "apFamiliesDocument.APFamilies = \n"
                + "     org.radixware.schemas.adsdef.APFamiliesDocument.APFamilies.Factory.newInstance();\n"
                + "String sRoleId = input==null ? "));

        cmdHndl.getSource().getItems().add(JmlTagInvocation.Factory.newInstance(roleIdProp));
        cmdHndl.getSource().getItems().add(Sqml.Text.Factory.newInstance(
                " : input.RoleID.Id.toString();\n"
                + "if (sRoleId.equals("));
        cmdHndl.getSource().getItems().add(getRoleSuperAdminId());
        cmdHndl.getSource().getItems().add(Sqml.Text.Factory.newInstance(
                ".toString()) || sRoleId.length()>2 && sRoleId.substring(0,3).equals(\"apl\"))\n"
                + "{\n"
                + //"//for(AccessPartitionFamily f : TDbpProject.getMainProject().accessPartitionFamilies)\n" +
                "for (DdsAccessPartitionFamilyDef apf : getArte().getDefManager().getAccessPartitionFamilyDefs())\n"
                + //"//    ApFamiliesDocument.TApFamilies.addID(f.id);\n" +
                "    apFamiliesDocument.APFamilies.IdList.add(apf.getHeadId());\n"
                + "}\n"
                + "else\n"
                + "   {\n"
                + "   RadRoleDef role = getArte().getDefManager().getRoleDef("));
        cmdHndl.getSource().getItems().add(
                new JmlTagTypeDeclaration(AdsTypeDeclaration.Factory.newInstance(typeId)));
        cmdHndl.getSource().getItems().add(Sqml.Text.Factory.newInstance(
                ".Factory.loadFrom(sRoleId));\n"
                + "   for (Id id : role.getAPFamilies())\n"
                + //"       apFamiliesDocument.APFamilies.IdList.add(id);\n" +
                "       apFamiliesDocument.APFamilies.IdList.add(getArte().getDefManager().getAccessPartitionFamilyDef(id).getHeadId());\n"
                + "   }\n"
                + "return apFamiliesDocument;"));
    }

    private Sqml.Item getRoleSuperAdminId() {
        return new JmlTagId(superAdmin);
        //return JmlTagInvocation.Factory.newInstance(superAdmin);
    }

    private void createGetArea() {
        List<AdsMethodDef> list = classDef.getMethods().get(EScope.LOCAL);
        AdsUserMethodDef m = null;
        for (AdsMethodDef m2 : list) {
            if (m2.getName().equals(GETAREANAME) && m2 instanceof AdsUserMethodDef) {
                m = (AdsUserMethodDef) m2;
                break;
            }
        }
        if (m == null && isInherit) {
            List<AdsMethodDef> listInh = overwrittenClass.getMethods().getLocal().list();
            for (AdsMethodDef method : listInh) {
                if (method instanceof AdsUserMethodDef && method.getName().equals(GETAREANAME)) {
                    m = (AdsUserMethodDef) classDef.getMethods().getLocal().overwrite(method);
                    break;
                }
            }
        }
        if (m == null) {
            m = AdsUserMethodDef.Factory.newInstance();
            classDef.getMethods().getLocal().add(m);
        }

        getArea = m;

        m.setName(GETAREANAME);
        m.getProfile().getAccessFlags().setProtected();
        m.getProfile().getParametersList().clear();
        m.getProfile().getAccessFlags().setDeprecated(false);
        m.getProfile().getAccessFlags().setAbstract(false);
        m.getProfile().getAccessFlags().setFinal(false);
        m.getProfile().getAccessFlags().setStatic(false);

        MethodParameter par = MethodParameter.Factory.newInstance();
        par.setName("rights");
        par.setType(AdsTypeDeclaration.Factory.newPlatformClass("Rights"));
        m.getProfile().getParametersList().add(par);

        {
            AdsTypeDeclaration hMap = AdsTypeDeclaration.Factory.newPlatformClass("java.util.HashMap");
            TypeArguments arguments = TypeArguments.Factory.newInstance(m);

            TypeArgument arg1 = TypeArgument.Factory.newInstance(AdsTypeDeclaration.Factory.newPlatformClass("Str"));
            TypeArgument arg2 = TypeArgument.Factory.newInstance(AdsTypeDeclaration.Factory.newPlatformClass("org.radixware.kernel.server.arte.Rights.AccessPartition"));
            arguments.add(arg1);
            arguments.add(arg2);
            hMap = hMap.toGenericType(arguments);
            m.getProfile().getReturnValue().setType(hMap);
        }


        m.getSource().getItems().clear();
        m.getSource().getItems().add(Sqml.Text.Factory.newInstance(
                "HashMap<String, Rights.AccessPartition> accessArea = new HashMap<String, Rights.AccessPartition>();\n\n"));

        for (ApfItem item : apfItems) {
            AdsInnateColumnPropertyDef modeProp = item.getPropMode();
            AdsInnateColumnPropertyDef keyProp = item.getPropKey();

            m.getSource().getItems().add(Sqml.Text.Factory.newInstance(
                    "if ("));
            m.getSource().getItems().add(
                    JmlTagInvocation.Factory.newInstance(modeProp));
            m.getSource().getItems().add(Sqml.Text.Factory.newInstance(
                    ".Value.intValue()!=0)\n    accessArea.put("));
            JmlTagId tagId = new JmlTagId((DdsAccessPartitionFamilyDef) item.getApf());

            m.getSource().getItems().add(tagId);

            m.getSource().getItems().add(Sqml.Text.Factory.newInstance(
                    ".toString(), rights. new AccessPartition(("));
            m.getSource().getItems().add(
                    JmlTagInvocation.Factory.newInstance(keyProp));

            m.getSource().getItems().add(Sqml.Text.Factory.newInstance(
                    "==null ? null : "));

            m.getSource().getItems().add(
                    JmlTagInvocation.Factory.newInstance(keyProp));

            m.getSource().getItems().add(Sqml.Text.Factory.newInstance(
                    ".toString()) , "));
            m.getSource().getItems().add(
                    JmlTagInvocation.Factory.newInstance(modeProp));
            m.getSource().getItems().add(Sqml.Text.Factory.newInstance(
                    ".Value.intValue()==2));\n\n"));




            /*
             * "if (" + sModeTag + ".intValue()!=0)\n"+ " accessArea.put(" +
             * sTagAPF + ", " + "rights.new AccessPartition((" + sKeysOrLinksTag
             * + "==null ? null : " + sKeysOrLinksTag + ".toString()) , "+
             * sModeTag + ".intValue()==2"+ ")"+ ");\n\n";
             */
        }



        m.getSource().getItems().add(Sqml.Text.Factory.newInstance(
                "return accessArea;"));



    }

    private class ClassDefVisitorEx implements IVisitor {

        public List<AdsEntityObjectClassDef> clazzList = new ArrayList<>();

        @Override
        public void accept(RadixObject radixObject) {
            clazzList.add((AdsEntityObjectClassDef) radixObject);
        }
    }

    private void createProps() {
        List<AdsPropertyDef> list = classDef.getProperties().get(EScope.LOCAL);
        List<DdsColumnDef> columns = table.getColumns().get(EScope.ALL);
        String sUserOrGroupTitleName = isUser ? USERTITLE : USERGROUPTITLE;
        //int ind = 0;
        for (AdsPropertyDef prop : list) {
            if (prop.getNature().equals(EPropNature.INNATE) && prop instanceof AdsInnateColumnPropertyDef) {
                AdsInnateColumnPropertyDef pi = (AdsInnateColumnPropertyDef) prop;
                Id currId = pi.getColumnInfo().getColumnId();

                boolean found = false;
                for (ApfItem item : apfItems) {
                    if (columns.get(item.getKeyIndexInColumns()).getId().equals(currId)) {
                        item.setPropKey(pi);
                        //item.setKeyIndexInProps(ind);
                        found = true;
                        break;
                    } else if (columns.get(item.getModeIndexInColumns()).getId().equals(currId)) {
                        item.setPropMode(pi);
                        //item.setModeIndexInProps(ind);
                        found = true;
                        break;
                    }
                }
                if (!found && prop.getName().equals(ROLEIDNAME)) {
                    this.roleIdProp = pi;
                    found = true;
                }
                if (!found && prop.getName().equals(IDNAME)) {
                    this.idProp = pi;
                    found = true;
                }
                if (!found && isUser && prop.getName().equals(ISOWN)) {
                    this.isOwnProp = pi;
                    found = true;
                }
                if (!found && (isUser && prop.getName().equals(USERNAME)
                        || !isUser && prop.getName().equals(USERGROUPNAME))) {
                    this.userOrGroupNameProp = pi;
                }
            } else if (prop.getNature().equals(EPropNature.INNATE)
                    && prop instanceof AdsInnateRefPropertyDef) {
                AdsInnateRefPropertyDef lnk = (AdsInnateRefPropertyDef) prop;
                if (prop.getName().equals(sUserOrGroupTitleName)) {
                    this.userOrGroupNamePropParentRef = lnk;
                } else if (prop.getName().length() > 4 && // it's link
                        prop.getName().substring(prop.getName().length() - 4).equals("Link")) {
                    DdsReferenceDef ref = lnk.getParentReferenceInfo().findParentReference();
                    if (ref != null && table.getId().equals(ref.findChildTable(classDef).getId())) {
                        DdsTableDef parentTable = ref.findParentTable(classDef);
                        //boolean find = false;
                        for (ApfItem item : apfItems) {
                            if (item.isTable && item.getApf().getHead().getId().equals(parentTable.getId())) {
                                item.setPropLink(lnk);
                                lnk.setName("ap" + item.getApf().getHead().getName() + "Link");
                            }
                        }
                    }
                }
            } else if (prop.getNature().equals(EPropNature.DYNAMIC)) {
                if (prop.getName().equals(ROLETITLENAME) && prop instanceof AdsDynamicPropertyDef) {
                    this.roleTitleProp = (AdsDynamicPropertyDef) prop;
                }

            }
        }

        if (roleIdProp == null) {
            if (roleIdPropInh != null) {
                roleIdProp = (AdsInnateColumnPropertyDef) classDef.getProperties().getLocal().overwrite(roleIdPropInh);
            } else {
                throw new DefinitionError(
                        "column " + ROLEIDNAME + " not found ", this);
            }
        }

        if (userOrGroupNameProp == null) {
            if (userOrGroupNamePropInh != null) {
                userOrGroupNameProp = (AdsInnateColumnPropertyDef) classDef.getProperties().getLocal().overwrite(userOrGroupNamePropInh);
            } else {
                throw new DefinitionError(
                        "column " + (isUser ? USERNAME : USERGROUPNAME) + " not found ", this);
            }
        }

        // search ref
        //refToUserOrGroup
        Set<DdsReferenceDef> refList = table.collectOutgoingReferences();
        for (DdsReferenceDef r : refList) {
            if (r.getColumnsInfo().size() == 1
                    && r.getColumnsInfo().get(0).getChildColumnId().equals(userOrGroupNameProp.getId())) {
                refToUserOrGroup = r;
                break;
            }
        }
        if (refToUserOrGroup == null) {
            throw new DefinitionError("reference to user(usergroup) not found ", this);
        }

        if (isUser && isOwnProp == null) {
            if (isOwnPropInh != null) {
                isOwnProp = (AdsInnateColumnPropertyDef) classDef.getProperties().getLocal().overwrite(isOwnPropInh);
            } else {
                throw new DefinitionError(
                        "column " + (ISOWN) + " not found ", this);
            }
        }


        if (idProp == null) {
            if (idPropInh != null) {
                idProp = (AdsInnateColumnPropertyDef) classDef.getProperties().getLocal().overwrite(idPropInh);
            } else {
                throw new DefinitionError(
                        "column " + (IDNAME) + " not found ", this);
            }
        }

        roleIdProp.getPresentationSupport().getPresentation().setTitleInherited(false);
        roleIdProp.getPresentationSupport().getPresentation().setEditOptionsInherited(false);
        roleIdProp.getPresentationSupport().getPresentation().getEditOptions().setEditPossibility(EEditPossibility.ALWAYS);
        roleIdProp.getPresentationSupport().getPresentation().getEditOptions().setNotNull(true);

        roleIdProp.getPresentationSupport().getPresentation().setTitle(EIsoLanguage.ENGLISH, "Role id");
        roleIdProp.getPresentationSupport().getPresentation().setTitle(EIsoLanguage.RUSSIAN, "Идентификатор роли");

        userOrGroupNameProp.getPresentationSupport().getPresentation().setTitleInherited(false);
        userOrGroupNameProp.getPresentationSupport().getPresentation().setEditOptionsInherited(false);

        userOrGroupNameProp.getPresentationSupport().getPresentation().getEditOptions().setEditPossibility(EEditPossibility.ON_CREATE);
        userOrGroupNameProp.getPresentationSupport().getPresentation().getEditOptions().setNotNull(true);


        if (userOrGroupNamePropParentRef == null) {
            if (userOrGroupNamePropParentRefInh != null) {
                userOrGroupNamePropParentRef = (AdsInnateRefPropertyDef) classDef.getProperties().getLocal().overwrite(userOrGroupNamePropParentRefInh);
            } else {
                userOrGroupNamePropParentRef = AdsInnateRefPropertyDef.Factory.newInstance(refToUserOrGroup);
                classDef.getProperties().getLocal().add(userOrGroupNamePropParentRef);
            }
            userOrGroupNamePropParentRef.setName(sUserOrGroupTitleName);
        }
        userOrGroupNamePropParentRef.getPresentationSupport().getPresentation().setTitleInherited(false);
        userOrGroupNamePropParentRef.getPresentationSupport().getPresentation().setEditOptionsInherited(false);


        {
            ParentRefPropertyPresentation propertyPresentation = (ParentRefPropertyPresentation) userOrGroupNamePropParentRef.getPresentationSupport().getPresentation();
            AdsEntityObjectClassDef ecld = userOrGroupNamePropParentRef.findReferencedEntityClass();
            if (ecld != null && ecld instanceof AdsEntityClassDef) {
                AdsEntityClassDef ecd = (AdsEntityClassDef) ecld;
                propertyPresentation.getParentSelect().setParentSelectorPresentationId(
                        ecd.getPresentations().getDefaultSelectorPresentationId());
            }
            propertyPresentation.getEditOptions().setEditPossibility(EEditPossibility.ON_CREATE);
            propertyPresentation.setTitle(EIsoLanguage.ENGLISH, isUser ? "User" : "User group");
            propertyPresentation.setTitle(EIsoLanguage.RUSSIAN, isUser ? "Пользователь" : "Группа пользователей");
        }


        for (ApfItem item : apfItems) {
            if (item.isTable) {//find entity def
                DdsTableDef tableDef = (DdsTableDef) item.getApf().getHead();
                Branch branch = this.classDef.getModule().getSegment().getLayer().getBranch();
                ClassDefVisitorEx сlassDefVisitorEx = new ClassDefVisitorEx();
                branch.visit(сlassDefVisitorEx, AdsVisitorProviders.newEntityObjectTypeProvider(tableDef.getId()));
                for (AdsEntityObjectClassDef cl : сlassDefVisitorEx.clazzList) {
                    String se = cl.getTitle(EIsoLanguage.ENGLISH);
                    String sr = cl.getTitle(EIsoLanguage.RUSSIAN);
                    if (se != null && sr != null) {
                        item.enStr = se;
                        item.ruStr = sr;
                        break;
                    }
                    if (item.enStr == null) {
                        item.enStr = item.ruStr = tableDef.getName();
                    }
                }

            } else {
                AdsEnumDef enumDef = (AdsEnumDef) item.getApf().getHead();
                String s = enumDef.getTitle(EIsoLanguage.ENGLISH);
                item.enStr = (s != null) ? s : enumDef.getName();
                s = enumDef.getTitle(EIsoLanguage.RUSSIAN);
                item.ruStr = (s != null) ? s : enumDef.getName();
            }

        }
        // modes and keys
        for (ApfItem item : apfItems) {
            {
                final AdsInnateColumnPropertyDef prop;
                if (item.getPropKey() == null) {
                    if (item.keyPropInherit == null) {
                        DdsColumnDef columnKey = columns.get(item.getKeyIndexInColumns());
                        prop = AdsInnateColumnPropertyDef.Factory.newInstance(columnKey);
                        classDef.getProperties().getLocal().add(prop);
                    } else {
                        //RadixObject.EEditState es = item.keyPropInherit.getEditState();
                        prop = (AdsInnateColumnPropertyDef) classDef.getProperties().getLocal().overwrite(item.keyPropInherit);
                        //item.keyPropInherit.setEditState(es);
                    }
                    item.setPropKey(prop);
                } else {
                    prop = item.getPropKey();
                }
                prop.setName("ap" + item.getApf().getHead().getName() + "Key");

                prop.getPresentationSupport().getPresentation().setTitleInherited(false);
                prop.getPresentationSupport().getPresentation().setEditOptionsInherited(false);
                prop.getPresentationSupport().getPresentation().getEditOptions().setEditPossibility(EEditPossibility.ALWAYS);
                PropertyPresentation propertyPresentation =
                        (PropertyPresentation) prop.getPresentationSupport().getPresentation();
                if (!item.isTable) {
                    propertyPresentation.setTitle(EIsoLanguage.ENGLISH, item.enStr);
                    propertyPresentation.setTitle(EIsoLanguage.RUSSIAN, item.ruStr);
                    propertyPresentation.getEditOptions().setEditPossibility(EEditPossibility.ALWAYS);
                    prop.getValue().setType(AdsTypeDeclaration.Factory.newInstance((AdsEnumDef) item.getApf().getHead()));
                }
            }

            {
                final AdsInnateColumnPropertyDef prop;
                if (item.getPropMode() == null) {
                    if (item.modePropInherit == null) {
                        DdsColumnDef columnMode = columns.get(item.getModeIndexInColumns());
                        prop = AdsInnateColumnPropertyDef.Factory.newInstance(columnMode);
                        classDef.getProperties().getLocal().add(prop);
                    } else {
                        prop = (AdsInnateColumnPropertyDef) classDef.getProperties().getLocal().overwrite(item.modePropInherit);
                    }
                    item.setPropMode(prop);

                } else {
                    prop = item.getPropMode();
                }
                prop.setName("ap" + item.getApf().getHead().getName() + "Mode");

                PropertyPresentation propertyPresentation =
                        (PropertyPresentation) prop.getPresentationSupport().getPresentation();
                propertyPresentation.setTitleInherited(false);
                propertyPresentation.setEditOptionsInherited(false);

                propertyPresentation.setTitle(EIsoLanguage.ENGLISH,
                        "Bounding mode for \'" + item.enStr + "\' family");
                propertyPresentation.setTitle(EIsoLanguage.RUSSIAN,
                        "Режим ограничения для семейства \'" + item.ruStr + "\'");
                propertyPresentation.getEditOptions().setNotNull(true);
                propertyPresentation.getEditOptions().setEditPossibility(EEditPossibility.ALWAYS);
                prop.getValue().setType(AdsTypeDeclaration.Factory.newInstance(accessAreaMode));
                prop.getValue().setInitial(ValAsStr.Factory.newInstance("0", EValType.INT));//unbounded

            }
            if (item.isTable) {
                final AdsInnateRefPropertyDef prop;
                if (item.getPropLink() == null) {
                    if (item.linkPropInherit != null) {
                        prop = (AdsInnateRefPropertyDef) classDef.getProperties().getLocal().overwrite(item.linkPropInherit);
                    } else {
                        prop = AdsInnateRefPropertyDef.Factory.newInstance(item.getRef());
                        classDef.getProperties().getLocal().add(prop);
                    }
                    //innateLnk
                    prop.setName("ap" + item.getApf().getHead().getName() + "Link");
                    item.setPropLink(prop);
                } else {
                    prop = item.getPropLink();
                }

                item.setReferencedEntityClass(item.getPropLink().findReferencedEntityClass());

                if (item.getReferencedEntityClass() == null) {
                    throw new DefinitionError("Class for property \"" + item.getPropLink().getName() + "\" not found", this);
                }


                ParentRefPropertyPresentation parentRefPropertyPresentation =
                        (ParentRefPropertyPresentation) prop.getPresentationSupport().getPresentation();
                parentRefPropertyPresentation.setTitleInherited(false);
                parentRefPropertyPresentation.setEditOptionsInherited(false);

                parentRefPropertyPresentation.setTitle(EIsoLanguage.ENGLISH,
                        item.enStr);
                parentRefPropertyPresentation.setTitle(EIsoLanguage.RUSSIAN,
                        item.ruStr);
                parentRefPropertyPresentation.getEditOptions().setEditPossibility(EEditPossibility.ALWAYS);

                parentRefPropertyPresentation.getParentSelect().setParentSelectConditionInherited(false);
                parentRefPropertyPresentation.getParentTitle().setParentTitleFormatInherited(true);
                //ExtendableDefinitions<AdsSelectorPresentationDef> selectorPresentations =
                EntityObjectPresentations presentations = item.getReferencedEntityClass().getPresentations();
                if (presentations instanceof EntityPresentations) {
                    EntityPresentations entityPresentations = (EntityPresentations) presentations;
                    parentRefPropertyPresentation.getParentSelect().setParentSelectorPresentationId(entityPresentations.getDefaultSelectorPresentationId());
                }
            }
        }

        for (ApfItem item : apfItems) {
            if (item.isTable) {
                final AdsInnateRefPropertyDef prop = item.getPropLink();
                final ParentRefPropertyPresentation parentRefPropertyPresentation = (ParentRefPropertyPresentation) prop.getPresentationSupport().getPresentation();
                if (item.getParentApfItem() != null) {//condition
                    final Sqml where = parentRefPropertyPresentation.getParentSelect().getParentSelectorCondition().getWhere();
                    //where.setSql("");
                    where.getItems().clear();

                    where.getItems().add(Sqml.Text.Factory.newInstance("("));

                    PropSqlNameTag sqlNameTag = PropSqlNameTag.Factory.newInstance();
                    sqlNameTag.setOwnerType(PropSqlNameTag.EOwnerType.CHILD);
                    sqlNameTag.setPropId(item.getParentApfItem().getPropMode().getId());
                    sqlNameTag.setPropOwnerId(this.classDef.getId());
                    where.getItems().add(sqlNameTag);


                    where.getItems().add(Sqml.Text.Factory.newInstance("<>1 or "));

                    PropSqlNameTag sqlNameTag2 = PropSqlNameTag.Factory.newInstance();
                    sqlNameTag2.setOwnerType(PropSqlNameTag.EOwnerType.CHILD);
                    sqlNameTag2.setPropId(item.getParentApfItem().getPropKey().getId());
                    sqlNameTag2.setPropOwnerId(this.classDef.getId());
                    where.getItems().add(sqlNameTag2);

                    where.getItems().add(Sqml.Text.Factory.newInstance("="));

                    PropSqlNameTag sqlNameTag3 = PropSqlNameTag.Factory.newInstance();
                    sqlNameTag3.setOwnerType(PropSqlNameTag.EOwnerType.THIS);
                    AdsEntityObjectClassDef referencedEntityClass = item.getReferencedEntityClass();


                    sqlNameTag3.setPropOwnerId(referencedEntityClass.getId());
                    List<AdsPropertyDef> props = referencedEntityClass.getProperties().get(EScope.ALL);
                    DdsColumnDef searchCol = item.getParentAPFColumn();
                    //DdsColumnDef searchCol = item.getRef().getColumnsInfo().get(0).findChildColumn();

                    if (searchCol == null) {
                        throw new DefinitionError("Propertie (primary key) at class " + referencedEntityClass.getName() + " not found", this);
                    }

                    AdsInnateColumnPropertyDef propParentId = null;
                    for (AdsPropertyDef prop2 : props) {
                        if (prop2 instanceof AdsInnateColumnPropertyDef) {
                            AdsInnateColumnPropertyDef iProp = (AdsInnateColumnPropertyDef) prop2;
                            DdsColumnDef col = iProp.getColumnInfo().findColumn();
                            if (col == searchCol) {
                                propParentId = iProp;
                                break;
                            }
                        }
                    }
                    if (propParentId == null) {
                        throw new DefinitionError("Propertie (primary key) at class " + referencedEntityClass.getName() + " not found", this);
                    }
                    sqlNameTag3.setPropId(propParentId.getId());
                    where.getItems().add(sqlNameTag3);

                    where.getItems().add(Sqml.Text.Factory.newInstance(")"));


                    /*
                     *
                     * AdsInnateColumnPropertyDef keyProp = item.getPropKey();
                     * m.getSource().getItems().add(Sqml.Text.Factory.newInstance(
                     * "if (")); m.getSource().getItems().add(
                     * JmlTagInvocation.Factory.newInstance(modeProp));
                     *
                     */


                }
            }
        }



        // role title
        if (roleTitleProp == null) {
            if (roleTitlePropInh != null) {
                roleTitleProp = (AdsDynamicPropertyDef) classDef.getProperties().getLocal().overwrite(roleTitlePropInh);
            } else {
                roleTitleProp = AdsDynamicPropertyDef.Factory.newInstance();
                classDef.getProperties().getLocal().add(roleTitleProp);
            }
            roleTitleProp.setName(ROLETITLENAME);
        }

        PropertyPresentation parentRefPropertyPresentation =
                (PropertyPresentation) roleTitleProp.getPresentationSupport().getPresentation();
        parentRefPropertyPresentation.setTitleInherited(false);
        parentRefPropertyPresentation.setEditOptionsInherited(false);

        parentRefPropertyPresentation.getEditOptions().setCustomDialogId(ERuntimeEnvironmentType.EXPLORER, chooseRoleDlg.getId());
        parentRefPropertyPresentation.getEditOptions().setCustomEditOnly(true);
        parentRefPropertyPresentation.getEditOptions().setShowDialogButton(true);

        parentRefPropertyPresentation.setTitle(EIsoLanguage.ENGLISH, "Role");
        parentRefPropertyPresentation.setTitle(EIsoLanguage.RUSSIAN, "Роль");

        roleTitleProp.getValue().setType(AdsTypeDeclaration.Factory.newInstance(EValType.STR));
        roleTitleProp.setGetterDefined(true);
        roleTitleProp.getPresentationSupport().getPresentation().getEditOptions().setEditPossibility(EEditPossibility.ALWAYS);
        Jml roleTitleSource = roleTitleProp.findGetter(EScope.LOCAL).get().getSource((ERuntimeEnvironmentType) null);
        roleTitleSource.getItems().clear();

        roleTitleSource.getItems().add(Sqml.Text.Factory.newInstance(
                "if ("));



        roleTitleSource.getItems().add(
                JmlTagInvocation.Factory.newInstance(
                this.roleIdProp));

        roleTitleSource.getItems().add(Sqml.Text.Factory.newInstance(
                " == null)\n   return null;\nreturn "));


        roleTitleSource.getItems().add(
                new JmlTagTypeDeclaration(AdsTypeDeclaration.Factory.newInstance(utilsClass)));
        roleTitleSource.getItems().add(Sqml.Text.Factory.newInstance("."));


        roleTitleSource.getItems().add(JmlTagInvocation.Factory.newInstance(getRoleTitle));
        roleTitleSource.getItems().add(Sqml.Text.Factory.newInstance("("));
        roleTitleSource.getItems().add(
                new JmlTagTypeDeclaration(AdsTypeDeclaration.Factory.newInstance(typeId)));
        roleTitleSource.getItems().add(Sqml.Text.Factory.newInstance(
                ".Factory.loadFrom("));
        roleTitleSource.getItems().add(
                JmlTagInvocation.Factory.newInstance(
                this.roleIdProp));
        roleTitleSource.getItems().add(Sqml.Text.Factory.newInstance(
                "));"));


    }
}
