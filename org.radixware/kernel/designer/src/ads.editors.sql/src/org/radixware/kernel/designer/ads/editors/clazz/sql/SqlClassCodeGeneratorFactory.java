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

package org.radixware.kernel.designer.ads.editors.clazz.sql;

import java.awt.Container;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.text.BadLocationException;
import javax.swing.text.Position.Bias;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.spi.editor.codegen.CodeGenerator;
import org.openide.util.Lookup;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsFieldPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsParameterPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsSqlClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsSqlClassDef.UsedTable;
import org.radixware.kernel.common.defs.ads.common.AdsUtils;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumUtils;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.enums.EPropNature;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.sqml.ISqmlProperty;
import org.radixware.kernel.common.sqml.tags.ParameterTag;
import org.radixware.kernel.common.sqml.tags.PropSqlNameTag;
import org.radixware.kernel.designer.ads.editors.clazz.creation.PropertyCreature;
import org.radixware.kernel.designer.ads.editors.clazz.sql.panels.AliasEditor;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinition;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlEditor;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlEditorPane;

import org.radixware.kernel.designer.common.dialogs.scmlnb.library.TagBounds;
import org.radixware.kernel.designer.common.dialogs.scmlnb.library.TagMapper;
import org.radixware.kernel.designer.common.dialogs.utils.IAcceptor;
import org.radixware.kernel.designer.common.dialogs.wizards.newobject.CreationWizard;
import org.radixware.kernel.designer.common.general.creation.ICreatureGroup;
import org.radixware.kernel.designer.common.general.creation.ICreatureGroup.ICreature;


@MimeRegistration(mimeType = "text/x-sqml", service = CodeGenerator.Factory.class)
public class SqlClassCodeGeneratorFactory implements CodeGenerator.Factory {

    private static abstract class AbstractParameterGenerator implements CodeGenerator {

        protected final AdsSqlClassCodeEditor editor;

        public AbstractParameterGenerator(final AdsSqlClassCodeEditor editor) {
            this.editor = editor;
        }

        @Override
        public void invoke() {
            final AdsParameterPropertyDef parameterProperty = createParameter();
            if (parameterProperty == null) {
                return;
            }
            final ParameterTag tag = ParameterTag.Factory.newInstance();
            tag.setParameterId(parameterProperty.getId());

            editor.getPane().insertTag(tag);
            getTree(editor).update();

            if (AdsParameterPropertyDef.canBeOutput(parameterProperty)) {
                editor.getPane().editTag(tag);
            }
        }

        protected abstract AdsParameterPropertyDef createParameter();
    }

    private static class ParameterFromScratchGenerator extends AbstractParameterGenerator {

        public ParameterFromScratchGenerator(final AdsSqlClassCodeEditor editor) {
            super(editor);
        }

        @Override
        public String getDisplayName() {
            return "Parameter from scratch...";
        }

        @Override
        public AdsParameterPropertyDef createParameter() {
            final AdsSqlClassDef sqlClass = editor.getSqlClass();
            final List<ICreature> creatures = PropertyCreature.Factory.createInstances(sqlClass.getPropertyGroup(), Collections.singleton(EPropNature.SQL_CLASS_PARAMETER));
            final ICreatureGroup group = new ICreatureGroup() {
                @Override
                public List<ICreature> getCreatures() {
                    return creatures;
                }

                @Override
                public String getDisplayName() {
                    return "group";
                }
            };
            final ICreature result = CreationWizard.execute(new ICreatureGroup[]{group}, creatures.get(0));
            if (result != null) {
                return ((AdsParameterPropertyDef) result.commit());
            }
            return null;
        }
    }

    private static class PkParameterGenerator extends AbstractParameterGenerator {

        public PkParameterGenerator(final AdsSqlClassCodeEditor editor) {
            super(editor);
        }

        @Override
        public String getDisplayName() {
            return "Parameter by PK...";
        }

        @Override
        public AdsParameterPropertyDef createParameter() {
            final List<DdsTableDef> allowedTables = new ArrayList<DdsTableDef>();
            for (UsedTable usedTable : editor.getSqlClass().getUsedTables().list()) {
                final DdsTableDef usedTableDef = usedTable.findTable();
                if (usedTableDef != null) {
                    allowedTables.add(usedTableDef);
                }
            }

            final ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(allowedTables);
            final Definition def = ChooseDefinition.chooseDefinition(cfg);

            if (def == null) {
                return null;
            }

            final DdsTableDef table = (DdsTableDef) def;

            final AdsParameterPropertyDef parameter = AdsParameterPropertyDef.Factory.newInstance();
            final AdsEntityClassDef entity = AdsUtils.findEntityClass(table);
            final String name = getName("p" + entity.getName());
            if (name == null) {
                return null;
            }
            final AdsTypeDeclaration type = AdsTypeDeclaration.Factory.newParentRef(entity);
            parameter.setName(name);
            parameter.getValue().setType(type);

            editor.getSqlClass().getProperties().getLocal().add(parameter);
            return parameter;
        }
    }

    private static String getName(String initialName) {
        return AliasEditor.editAlias(initialName, "Name:", "Name");
    }

    private static class ParameterByColumnGenerator extends AbstractParameterGenerator {

        private final ISqmlProperty property;

        public ParameterByColumnGenerator(final AdsSqlClassCodeEditor editor, final ISqmlProperty property) {
            super(editor);
            this.property = property;
        }

        @Override
        protected AdsParameterPropertyDef createParameter() {
            final AdsParameterPropertyDef parameter = AdsParameterPropertyDef.Factory.newInstance();
            final String name = getName("p" + Character.toUpperCase(property.getName().charAt(0)) + property.getName().substring(1));
            if (name == null) {
                return null;
            }
            parameter.setName(name);
            parameter.getValue().setType(AdsTypeDeclaration.Factory.newInstance(property.getValType()));
            editor.getSqlClass().getProperties().getLocal().add(parameter);
            return parameter;
        }

        @Override
        public String getDisplayName() {
            return "Parameter by \"" + property.getName() + "\"";
        }
    }

    private static abstract class AbstractFieldGenerator implements CodeGenerator {

        protected final AdsSqlClassCodeEditor editor;

        public AbstractFieldGenerator(final AdsSqlClassCodeEditor editor) {
            this.editor = editor;
        }

        protected abstract AdsFieldPropertyDef createField();

        @Override
        public void invoke() {
            final AdsFieldPropertyDef field = createField();
            if (field != null) {
                field.setPublished(true);
                final PropSqlNameTag propTag = PropSqlNameTag.Factory.newInstance();
                propTag.setOwnerType(PropSqlNameTag.EOwnerType.THIS);
                propTag.setPropId(field.getId());
                propTag.setPropOwnerId(field.getOwnerDefinition().getId());
                editor.getPane().insertTag(propTag);
                getTree(editor).update();
            }
        }
    }

    private static class FieldByColumnGenerator extends AbstractFieldGenerator {

        private final DdsColumnDef column;

        public FieldByColumnGenerator(final AdsSqlClassCodeEditor editor, final DdsColumnDef column) {
            super(editor);
            this.column = column;
        }

        private static AdsTypeDeclaration getColumnTypeDeclaration(final DdsColumnDef column) {
            final AdsEnumDef enumDef = AdsEnumUtils.findColumnEnum(column);
            if (enumDef != null) {
                return AdsTypeDeclaration.Factory.newInstance(enumDef);
            } else {
                final EValType realValType = column.getValType();
                return AdsTypeDeclaration.Factory.newInstance(realValType);
            }
        }

        @Override
        protected AdsFieldPropertyDef createField() {
            final AdsSqlClassDef sqlClass = editor.getSqlClass();
            final String name = getName(column.getName());
            if (name == null) {
                return null;
            }
            final AdsFieldPropertyDef field = AdsFieldPropertyDef.Factory.newInstance(name);
            sqlClass.getProperties().getLocal().add(field);
            field.setConst(true);
            field.getAccessFlags().setPublic();

            final AdsTypeDeclaration typeDecl = getColumnTypeDeclaration(column);
            field.getValue().setType(typeDecl);
            return field;
        }

        @Override
        public String getDisplayName() {
            return "Field by \"" + column.getName() + "\"";
        }
    }

    private static class FieldFromScratchGenerator extends AbstractFieldGenerator {

        public FieldFromScratchGenerator(final AdsSqlClassCodeEditor editor) {
            super(editor);
        }

        @Override
        protected AdsFieldPropertyDef createField() {
            final AdsSqlClassDef sqlClass = editor.getSqlClass();
            final List<ICreature> creatures = PropertyCreature.Factory.createInstances(sqlClass.getPropertyGroup(), Collections.singleton(EPropNature.FIELD));
            final ICreatureGroup group = new ICreatureGroup() {
                @Override
                public List<ICreature> getCreatures() {
                    return creatures;
                }

                @Override
                public String getDisplayName() {
                    return "group";
                }
            };
            final ICreature result = CreationWizard.execute(new ICreatureGroup[]{group}, creatures.get(0));
            if (result != null) {
                return ((AdsFieldPropertyDef) result.commit());
            }
            return null;
        }

        @Override
        public String getDisplayName() {
            return "Field from scratch...";
        }
    }

    private static AdsSqlClassTree getTree(final AdsSqlClassCodeEditor editor) {
        final AdsSqlClassBodyPanel bodyPanel = (AdsSqlClassBodyPanel) editor.getParent().getParent();
        return bodyPanel.treePanel.getTree();
    }

    private static ISqmlProperty findNearestProperty(final ScmlEditor editor, final Bias direction, final IAcceptor<String> textBeforeTagAcceptor) {
        final Scml.Tag tag = findNearestTag(editor, direction, textBeforeTagAcceptor);
        if (tag instanceof PropSqlNameTag) {
            return ((PropSqlNameTag) tag).findProperty();
        }
        return null;
    }

    private static Scml.Tag findNearestTag(final ScmlEditor editor, final Bias direction, final IAcceptor<String> textBeforeTagAcceptor) {
        final int caretPos = editor.getPane().getCaretPosition();
        final TagMapper tagMapper = editor.getPane().getScmlDocument().getTagMapper();
        final int shift = direction == Bias.Forward ? 1 : -1;
        TagBounds tagBounds;
        final StringBuilder textBeforeTag = new StringBuilder();
        final String text = editor.getPane().getText();
        for (int i = caretPos + shift; i >= 0 && i < text.length(); i += shift) {
            tagBounds = tagMapper.findContainingBounds(i);
            if (tagBounds != null) {
                if (textBeforeTagAcceptor.accept(textBeforeTag.reverse().toString())) {
                    return tagBounds.getVTag().getTag();
                } else {
                    return null;
                }
            }
            try {
                textBeforeTag.append(editor.getPane().getText(i, 1));
            } catch (BadLocationException ex) {
                return null;
            }
        }
        return null;
    }

    @Override
    public List<? extends CodeGenerator> create(final Lookup context) {
        final ScmlEditorPane pane = context.lookup(ScmlEditorPane.class);
        if (pane.getClientProperty(ScmlEditorPane.DISABLE_STANDART_GENERATORS) != null) {
            return Collections.EMPTY_LIST;
        }
        Container parentContainer = pane.getParent();
        AdsSqlClassCodeEditor editor = null;
        while (parentContainer != null) {
            if (parentContainer instanceof AdsSqlClassCodeEditor) {
                editor = (AdsSqlClassCodeEditor) parentContainer;
                break;
            }
            parentContainer = parentContainer.getParent();
        }
        if (editor != null && editor.getPane().isCurrentPositionEditable()) {
            final List<CodeGenerator> generators = new ArrayList<CodeGenerator>();


            final IAcceptor<String> asAcceptor = new IAcceptor<String>() {
                @Override
                public boolean accept(final String object) {
                    if (object == null || object.isEmpty()) {
                        return false;
                    }
                    if (object.trim().isEmpty()) {
                        return true;
                    }
                    return "as".equals(object.trim().toLowerCase());
                }
            };

            final Scml.Tag tagForField = findNearestTag(editor, Bias.Backward, asAcceptor);
            if (tagForField instanceof PropSqlNameTag) {
                final ISqmlProperty sqmlPropertyForField = ((PropSqlNameTag) tagForField).findProperty();
                if (sqmlPropertyForField != null && sqmlPropertyForField.getDefinition() instanceof DdsColumnDef) {
                    generators.add(new FieldByColumnGenerator(editor, (DdsColumnDef) sqmlPropertyForField.getDefinition()));
                }
            }

            final IAcceptor<String> operatorAcceptor = new IAcceptor<String>() {
                @Override
                public boolean accept(final String object) {
                    return true;
                }
            };
            final ISqmlProperty sqmlPropertyForParameter = findNearestProperty(editor, Bias.Backward, operatorAcceptor);
            if (sqmlPropertyForParameter != null) {
                generators.add(new ParameterByColumnGenerator(editor, sqmlPropertyForParameter));
            }



            generators.add(new PkParameterGenerator(editor));
            generators.add(new ParameterFromScratchGenerator(editor));
            generators.add(new FieldFromScratchGenerator(editor));

            return generators;
        }
        return Collections.EMPTY_LIST;
    }
}
