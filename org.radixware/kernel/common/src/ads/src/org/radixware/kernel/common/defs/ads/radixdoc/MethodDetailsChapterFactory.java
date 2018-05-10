/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.common.defs.ads.radixdoc;

import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsCommandHandlerMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodThrowsList;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPresentationSlotMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsRPCMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.MethodParameter;
import org.radixware.kernel.common.defs.ads.clazz.members.MethodReturnValue;
import org.radixware.kernel.common.defs.ads.radixdoc.ClassRadixdoc.DetailMembersChapterFactory;
import org.radixware.kernel.common.defs.ads.ui.AdsUIConnection;
import org.radixware.kernel.common.enums.EMethodNature;
import static org.radixware.kernel.common.enums.EMethodNature.ALGO_METHOD;
import static org.radixware.kernel.common.enums.EMethodNature.ALGO_START;
import static org.radixware.kernel.common.enums.EMethodNature.ALGO_STROB;
import static org.radixware.kernel.common.enums.EMethodNature.COMMAND_HANDLER;
import static org.radixware.kernel.common.enums.EMethodNature.PRESENTATION_SLOT;
import static org.radixware.kernel.common.enums.EMethodNature.RPC;
import static org.radixware.kernel.common.enums.EMethodNature.SYSTEM;
import static org.radixware.kernel.common.enums.EMethodNature.USER_DEFINED;
import org.radixware.kernel.common.radixdoc.DefaultAttributes;
import org.radixware.kernel.common.radixdoc.DefaultStyle;
import org.radixware.kernel.common.radixdoc.RadixIconResource;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.radixdoc.Block;
import org.radixware.schemas.radixdoc.ContentContainer;
import org.radixware.schemas.radixdoc.ElementList;
import org.radixware.schemas.radixdoc.Table;
import org.radixware.schemas.radixdoc.Text;

/**
 *
 * @author npopov
 */
public class MethodDetailsChapterFactory extends DetailMembersChapterFactory<AdsMethodDef> {

    private final ClassRadixdoc classDoc;
    private final Block container;
    private final ClassRadixdoc.ClassWriter clWriter;

    public MethodDetailsChapterFactory(ClassRadixdoc classDoc, Block container) {
        this.classDoc = classDoc;
        this.container = container;
        this.clWriter = classDoc.getClassWriter();
    }

    @Override
    void documentMemberDetail(AdsMethodDef member, ContentContainer contentBlock) {
        contentBlock.setStyle(DefaultStyle.NAMED);

        final Block title = clWriter.addBlockTitle(contentBlock);

        final RadixIconResource resource = new RadixIconResource(member.getIcon());
        title.addNewResource().setSource(resource.getKey());
        clWriter.addText(title, member.getName()).setStyle(DefaultStyle.IDENTIFIER);

        addArgs(title, member);
        classDoc.addResource(resource);

        final Block descriptionBlock = contentBlock.addNewBlock();
        descriptionBlock.setStyle(DefaultStyle.DESCRIPTION);
        
        final TypeDocument typeDoc = new TypeDocument();
        typeDoc.addString(member.getProfile().getAccessFlags().getRadixdocPresentation()).addString(" ");
        MethodReturnValue retVal = member.getProfile().getReturnValue();
        if (retVal != null) {
            typeDoc.addType(retVal.getType(), classDoc.getSource());
        }
        clWriter.documentType(descriptionBlock, typeDoc, classDoc.getSource());
        clWriter.addText(descriptionBlock, " " + member.getName());
        addArgs(descriptionBlock, member);
        descriptionBlock.addNewBlock();
        
        if (member.isOverride()) {
            AdsMethodDef parentDef = member.getHierarchy().findOverridden().get();
            if (parentDef != null) {
                Table overrideTable = clWriter.addNewTable(descriptionBlock, "Overrides");
                clWriter.addRefRow(overrideTable, null, parentDef, parentDef.getModule());
                clWriter.appendStyle(overrideTable, DefaultStyle.GENERAL_ATTRIBUTES);
            }
        }
        
        if (member.isOverwrite()) {
            AdsMethodDef parentDef = member.getHierarchy().findOverwritten().get();
            if (parentDef != null) {
                Table overwriteTable = clWriter.addNewTable(descriptionBlock, "Overwrites");
                clWriter.addRefRow(overwriteTable, null, parentDef, parentDef.getModule());
                clWriter.appendStyle(overwriteTable, DefaultStyle.GENERAL_ATTRIBUTES);
            }
        }
        
        clWriter.setAttribute(title, DefaultAttributes.ANCHOR, classDoc.getIdentifier(member));

        clWriter.documentDescription(descriptionBlock, member);

        Table commonPropsTable = clWriter.addGeneralAttrTable(descriptionBlock);

        List<String> modifiers = new ArrayList<>();
        modifiers.add(member.getAccessMode().getAsStr());
        if (member.isDeprecated()) {
            modifiers.add("Deprecated");
        }
        if (member.isOverwrite()) {
            modifiers.add("Overwriting");
        }
        if (member.isOverride()) {
            modifiers.add("Overriding");
        }
        if (member.isReflectiveCallable()) {
            modifiers.add("Reflective callable");
        }
        if (!modifiers.isEmpty()) {
            clWriter.addAllStrRow(commonPropsTable, "Modifiers", modifiers.toString().substring(1, modifiers.toString().length() - 1));
        }

        EMethodNature nature = member.getNature();
        switch (nature) {
            case COMMAND_HANDLER:
                AdsCommandHandlerMethodDef commHandlerMethod = (AdsCommandHandlerMethodDef) member;
                writeCommandHandlerInfo(commHandlerMethod, commonPropsTable);
                break;
            case PRESENTATION_SLOT:
                AdsPresentationSlotMethodDef presSlotMethod = (AdsPresentationSlotMethodDef) member;
                writePresentSlotInfo(presSlotMethod, commonPropsTable);
                break;
            case RPC:
                AdsRPCMethodDef rpcMethod = (AdsRPCMethodDef) member;
                writeRPCMethodInfo(rpcMethod, commonPropsTable);
                break;

//          case ALGO_BLOCK:
            case ALGO_METHOD:
            case ALGO_START:
            case ALGO_STROB:
            case SYSTEM:
            case USER_DEFINED:
            default:
        }

        AdsMethodDef profileDescriptionProviderDef = (AdsMethodDef) clWriter.findOwnerDescriptionDefinition(member);
        if (profileDescriptionProviderDef == null) {
            profileDescriptionProviderDef = member;
        }
        writeReturnValInfo(profileDescriptionProviderDef, descriptionBlock);
        writeParametersInfo(profileDescriptionProviderDef, descriptionBlock);
        writeExceptionsInfo(profileDescriptionProviderDef, descriptionBlock);
    }

    private void addArgs(ContentContainer root ,AdsMethodDef member) {
        final TypeDocument argsTypeDoc = new TypeDocument();
        argsTypeDoc.addString(" (");
        boolean first = true;
        for (final MethodParameter parametr : member.getProfile().getParametersList()) {
            if (first) {
                first = false;
            } else {
                argsTypeDoc.addString(", ");
            }
            argsTypeDoc.addType(parametr.getType(), member).addString(" ").addString(parametr.getName());
        }
        argsTypeDoc.addString(")");
        clWriter.documentType(root, argsTypeDoc, member);
    }

    @Override
    List<AdsMethodDef> getAllMembers(AdsClassDef c) {
        return classDoc.getSource().getMethods().getLocal().list();
    }

    @Override
    ElementList createRootList() {
        final Block rootChapter = container.addNewBlock();
        clWriter.appendStyle(rootChapter, DefaultStyle.CHAPTER);
        clWriter.addStrTitle(rootChapter, "Methods Detail");

        final ElementList rootList = rootChapter.addNewList();
        clWriter.appendStyle(rootList, DefaultStyle.MEMBERS);
        return rootList;
    }

    @Override
    ElementList.Item createItem() {
        ElementList.Item detailItem = getRootChapter().addNewItem();
        return detailItem;
    }

    private void writeCommandHandlerInfo(AdsCommandHandlerMethodDef handler, Table commonPropsTable) {
        if (handler.findCommand() != null) {
            clWriter.addStr2RefRow(commonPropsTable, "Command", handler.findCommand(), handler);
        }
    }

    private void writePresentSlotInfo(AdsPresentationSlotMethodDef presentSlot, Table commonPropsTable) {
        if (presentSlot.findUI() != null) {
            clWriter.addStr2RefRow(commonPropsTable, "Widget", presentSlot.findUI(), presentSlot);
        }
        if (presentSlot.findUIConnections() != null) {
            List<String> signals = new ArrayList<>();
            for (AdsUIConnection conn : presentSlot.findUIConnections()) {
                signals.add(conn.getSignalName());
            }
            clWriter.addAllStrRow(commonPropsTable, "Signals", signals.toString().substring(1, signals.toString().length() - 1));
        }
    }

    private void writeRPCMethodInfo(AdsRPCMethodDef rpcMethod, Table commonPropsTable) {
        if (rpcMethod.findCommand() != null) {
            clWriter.addStr2RefRow(commonPropsTable, "Command", rpcMethod.findCommand(), rpcMethod);
        }
        if (rpcMethod.findServerSideMethod() != null) {
            clWriter.addStr2RefRow(commonPropsTable, "Server side method", rpcMethod.findServerSideMethod(), rpcMethod);
        }
    }

    private void writeReturnValInfo(AdsMethodDef member, ContentContainer detailBlock) {
        MethodReturnValue retVal = member.getProfile().getReturnValue();
        if (retVal != null && retVal.getDescriptionId() != null && retVal.getDescriptionLocation() != null) {
            Text label = detailBlock.addNewText();
            label.setStyle(DefaultStyle.IDENTIFIER);
            label.setStringValue("Returns: ");
            final Block descriptionBlock = detailBlock.addNewBlock();
            descriptionBlock.setStyle(DefaultStyle.DESCRIPTION);
            clWriter.addMslId(descriptionBlock, member, retVal.getDescriptionLocation().getLocalizingBundleId(), retVal.getDescriptionId());
        }
    }

    private void writeParametersInfo(AdsMethodDef member, ContentContainer detailBlock) {
        if (!member.getProfile().getParametersList().isEmpty()) {
            Text label = detailBlock.addNewText();
            label.setStyle(DefaultStyle.IDENTIFIER);
            label.setStringValue("Parameters: ");
            final Block descriptionBlock = detailBlock.addNewBlock();
            descriptionBlock.setStyle(DefaultStyle.DESCRIPTION);
            Table paramsTable = descriptionBlock.addNewTable();
            for (MethodParameter par : member.getProfile().getParametersList()) {
                Table.Row row = paramsTable.addNewRow();
                row.addNewCell().addNewText().setStringValue(par.getName() + " - ");
                Id id = par.getDescriptionId();
                AdsDefinition def = par.getDescriptionLocation();
                if (id != null &&  def != null) {
                    clWriter.addMslId(row.addNewCell(), def, def.getLocalizingBundleId(), id);
                }
            }
        }
    }

    private void writeExceptionsInfo(AdsMethodDef member, ContentContainer detailBlock) {
        if (!member.getProfile().getThrowsList().isEmpty()) {
            Text label = detailBlock.addNewText();
            label.setStyle(DefaultStyle.IDENTIFIER);
            label.setStringValue("Throws: ");
            final Block descriptionBlock = detailBlock.addNewBlock();
            descriptionBlock.setStyle(DefaultStyle.DESCRIPTION);
            Table throwsTable = descriptionBlock.addNewTable();
            for (AdsMethodThrowsList.ThrowsListItem throwsItem : member.getProfile().getThrowsList()) {
                Table.Row excRow = throwsTable.addNewRow();
                TypeDocument excDoc = new TypeDocument();
                excDoc.addType(throwsItem.getException(), member);
                clWriter.documentType(excRow.addNewCell(), excDoc, member);
                if (throwsItem.getDescriptionId() != null && throwsItem.getDescriptionLocation() != null) {
                    clWriter.addMslId(excRow.addNewCell(), member, throwsItem.getDescriptionLocation().getLocalizingBundleId(), throwsItem.getDescriptionId());
                } else {
                    clWriter.addText(excRow.addNewCell(), "");
                }
            }
        }
    }
}
