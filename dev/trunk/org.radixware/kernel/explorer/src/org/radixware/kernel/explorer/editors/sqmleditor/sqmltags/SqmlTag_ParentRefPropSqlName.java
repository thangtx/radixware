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

package org.radixware.kernel.explorer.editors.sqmleditor.sqmltags;

import java.util.ArrayList;
import java.util.List;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EDefinitionDisplayMode;
import org.radixware.kernel.common.client.meta.sqml.ISqmlColumnDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlDefinition;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableReference;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.editors.sqmleditor.tageditors.ParentRef_Dialog;
import org.radixware.kernel.explorer.editors.xscmleditor.XscmlEditor;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.schemas.xscml.Sqml;
import org.radixware.schemas.xscml.Sqml.Item.ParentRefPropSqlName;


public class SqmlTag_ParentRefPropSqlName extends SqmlTag {

    private ParentRefPropSqlName parentRefPropSqlName;
    private ISqmlTableDef presentationClassDef;
    private ISqmlColumnDef prop;
    private static final String path = "org.radixware.explorer/S_E/SYNTAX_SQML/SQML_PROP_SQL_NAME";
    private final List<ISqmlTableReference> referencePath = new ArrayList<>();

    public SqmlTag_ParentRefPropSqlName(final IClientEnvironment environment, ISqmlColumnDef prop, ISqmlTableDef presentationClassDef, ISqmlTableDef contextClass, List<ISqmlTableReference> referencePath, long pos, EDefinitionDisplayMode showMode) {
        super(environment, pos, prop==null?false :prop.isDeprecated());
        this.presentationClassDef = presentationClassDef;
        this.prop = prop;
        this.referencePath.addAll(referencePath);
        setParentRefPropSqlName(contextClass, showMode);
    }

    public SqmlTag_ParentRefPropSqlName(final IClientEnvironment environment, ParentRefPropSqlName parentRefPropSqlName, ISqmlTableDef contextClass, long pos, EDefinitionDisplayMode showMode) {
        super(environment, pos);
        this.parentRefPropSqlName = parentRefPropSqlName;
        String errorMess = collectPath(parentRefPropSqlName, contextClass);
        if (errorMess == null) {
            setParentRefPropSqlName(parentRefPropSqlName);
            setDisplayedInfo(showMode);
        } else {
            setNotValid(parentRefPropSqlName.getPropId(), errorMess);
        }
    }
    private List<ISqmlDefinition> refs = new ArrayList<>();

    private String collectPath(ParentRefPropSqlName parentRefPropSqlName, ISqmlTableDef classDef) {
        if (classDef == null) {
            String mess = Application.translate("SqmlEditor", "context class is null");
            return String.format(mess);
        }
        refs = new ArrayList<>();
        refs.add(classDef);
        for (int i = 0; i < parentRefPropSqlName.getReferenceIdList().size(); i++) {
            Id refId = parentRefPropSqlName.getReferenceIdList().get(i);
            ISqmlTableReference ref = classDef.getReferences().getReferenceById(refId);
            if (ref == null) {
                String mess = Application.translate("SqmlEditor", "reference #%s not found");
                return String.format(mess, refId);
            }
            try {
                classDef = ref.findReferencedTable();
                refs.add(classDef);
            } catch (DefinitionError ex) {
                String mess = Application.translate("SqmlEditor", "referenced table for reference #%s not found");
                return String.format(mess, refId);
            }
        }
        Id propId = parentRefPropSqlName.getPropId();
        if (classDef.getColumns().getColumnById(propId) == null) {
            String mess = Application.translate("SqmlEditor", "column #%s not found");
            return String.format(mess, propId);
        }
        if (presentationClassDef == null) {
            presentationClassDef = classDef;
        }
        if (prop == null) {
            prop = classDef.getColumns().getColumnById(propId);
        }
        refs.add(prop);
        setIsDeprecated(prop.isDeprecated());
        if ((prop.getType() == EValType.PARENT_REF) && prop.getReferenceIndex() == null) {
            String mess = Application.translate("SqmlEditor", "reference index for parent reference property #%s is null");
            return String.format(mess, propId);
        }
        return null;
    }

    @Override
    public boolean showEditDialog(XscmlEditor editText, EDefinitionDisplayMode showMode) {
        if (valid) {
            ParentRef_Dialog dialog = new ParentRef_Dialog(editText.getEnvironment(), editText, refs, showMode);
            if (dialog.exec() == 1) {
                return true;
            }
        }
        return false;
    }

    private void setParentRefPropSqlName(ParentRefPropSqlName parentRefPropSqlName) {
        this.parentRefPropSqlName = ParentRefPropSqlName.Factory.newInstance();
        this.parentRefPropSqlName.setPropId(parentRefPropSqlName.getPropId());
        for (int i = 0; i < parentRefPropSqlName.getReferenceIdList().size(); i++) {
            this.parentRefPropSqlName.getReferenceIdList().add(parentRefPropSqlName.getReferenceIdList().get(i));
        }
    }

    private void setParentRefPropSqlName(ISqmlTableDef contextClass, EDefinitionDisplayMode showMode) {
        parentRefPropSqlName = ParentRefPropSqlName.Factory.newInstance();
        this.parentRefPropSqlName.setPropId(prop.getId());
        if (referencePath != null && !referencePath.isEmpty()) {
            for (ISqmlTableReference reference : referencePath) {
                parentRefPropSqlName.getReferenceIdList().add(reference.getId());
            }
        }
        collectPath(parentRefPropSqlName, contextClass);
        setDisplayedInfo(showMode);
        parentRefPropSqlName = ParentRefPropSqlName.Factory.newInstance();
        parentRefPropSqlName.setPropId(prop.getId());
        if (referencePath != null && !referencePath.isEmpty()) {
            for (ISqmlTableReference reference : referencePath) {
                parentRefPropSqlName.getReferenceIdList().add(reference.getId());
            }
        }
    }

    private void setNotValid(final Id propId, final String mess) {
        valid = false;
        String s = "???ParentRefProp-" + propId + "???";
        environment.getTracer().warning(mess);
        setDisplayedInfo(s, s);
    }

    private String createTitle(String sTable, String sProp, String sPropType, List<String> refColumnNameList) {
        if (!valid) {
            return "";
        }
        if (sTable.indexOf('<') != -1) {
            sTable = sTable.replaceAll("<", " ");//&#60;
        }
        if (sTable.indexOf('>') != -1) {
            sTable = sTable.replaceAll(">", " ");//&#62;
        }
        if (sProp.indexOf('<') != -1) {
            sProp = sProp.replaceAll("<", " ");//&#60;
        }
        if (sProp.indexOf('>') != -1) {
            sProp = sProp.replaceAll(">", " ");//&#62;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<b>ParentRef property:</b><br>&nbsp;&nbsp;&nbsp;&nbsp; ").append(sProp).append("</br>");
        if (!refColumnNameList.isEmpty()) {
            sb.append("<br><b>Reference columns:</b></br>");
            for (String refColumnName : refColumnNameList) {
                if (refColumnName.indexOf('<') != -1) {
                    refColumnName = refColumnName.replaceAll("<", "&#60;");
                }
                sb.append("<br>&nbsp;&nbsp;&nbsp;&nbsp;").append(refColumnName).append("</br>");
            }
        }
        sb.append("<br><b>Table:</b></br><br>&nbsp;&nbsp;&nbsp;&nbsp; ").append(sTable);
        sb.append("</br><br><b>Property type:</b></br><br>&nbsp;&nbsp;&nbsp;&nbsp;");
        sb.append(sPropType).append("</br>");
        return sb.toString();
    }

    @Override
    public final boolean setDisplayedInfo(EDefinitionDisplayMode showMode) {
        if (isValid()){
            String getTagName = getTagName(showMode);
            final String propTypeName = prop.getType().getName();
            if (EDefinitionDisplayMode.SHOW_TITLES == showMode) {
                final String defName = presentationClassDef.getFullName();
                final String propName = prop.getShortName();
                setDisplayedInfo(createTitle(defName, propName, propTypeName, getColumnsName(showMode)), getTagName);
            } else {
                final String defTitle = presentationClassDef.getTitle();
                final String propTitle = prop.getTitle();
                setDisplayedInfo(createTitle(defTitle, propTitle, propTypeName, getColumnsName(showMode)), getTagName);
            }
            return true;
        }
        return false;
    }

    private List<String> getColumnsName(EDefinitionDisplayMode showMode) {
        List<String> refColumnNameList = new ArrayList<>();
        if (prop.getType() == EValType.PARENT_REF) {
            List<ISqmlColumnDef> refColumns = prop.getReferenceColumns();
            for (int i = 0; i < refColumns.size(); i++) {
                ISqmlColumnDef col = refColumns.get(i);
                String parentRefPropName = (EDefinitionDisplayMode.SHOW_TITLES == showMode ? col.getShortName() : col.getTitle());
                refColumnNameList.add(parentRefPropName);
            }
        }
        return refColumnNameList;
    }

    private String getTagName(EDefinitionDisplayMode showMode) {
        String sPath = getStrPath(showMode);
        //String tagName = "";
        StringBuilder sb=new StringBuilder();
        if (prop.getType() == EValType.PARENT_REF) {
            List<ISqmlColumnDef> refColumns = prop.getReferenceColumns();
            StringBuilder tagName=new StringBuilder();
            for (int i = 0; i < refColumns.size(); i++) {
                ISqmlColumnDef col = refColumns.get(i);
                tagName.append(sPath).append('.');
                tagName.append((EDefinitionDisplayMode.SHOW_TITLES == showMode ? col.getTitle() : col.getShortName()));
                if (i < refColumns.size() - 1) {
                    tagName.append(", ");
                }
            }
            if (refColumns.size() > 1) {
                sb.append('(').append(tagName.toString()).append(')');
            }else{
                sb.append(tagName.toString());
            }
        } else {
            int n = refs.size() - 1;
            sb.append(sPath).append('.').append((EDefinitionDisplayMode.SHOW_TITLES == showMode ? refs.get(n).getTitle() : refs.get(n).getShortName()));
        }
        return sb.toString();
    }

    private String getStrPath(EDefinitionDisplayMode showMode) {
        //String sPath = "";
        StringBuilder sb=new StringBuilder();
        int n = 0;
        if (prop.getType() == EValType.PARENT_REF) {
            List<ISqmlColumnDef> refColumns = prop.getReferenceColumns();
            if (!refColumns.isEmpty()) {
                ISqmlColumnDef col = refColumns.get(0);
                n = refs.indexOf(col.getOwnerTable()) + 1;
            }
        } else {
            n = refs.size() - 1;
        }
        for (int i = 0; i < n; i++) {
            sb.append(refs.get(i).getDisplayableText(showMode));
            if (i < n - 1) {
                sb.append("->");
            }
        }
        return sb.toString();
    }

    private SqmlTag_ParentRefPropSqlName(final IClientEnvironment environment, SqmlTag_ParentRefPropSqlName source) {
        super(environment, source);
    }

    @Override
    public SqmlTag_ParentRefPropSqlName copy() {
        SqmlTag_ParentRefPropSqlName res = new SqmlTag_ParentRefPropSqlName(environment, this);
        res.parentRefPropSqlName = parentRefPropSqlName;
        res.prop = prop;
        res.presentationClassDef = presentationClassDef;
        res.refs = new ArrayList<>();
        res.refs.addAll(refs);
        return res;
    }

    @Override
    public void addTagToSqml(XmlObject itemTag) {
        Sqml.Item item = (Sqml.Item) itemTag;
        item.setParentRefPropSqlName(parentRefPropSqlName);
    }

    @Override
    protected String getSettingsPath() {
        return path;
    }
}