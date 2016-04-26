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

package org.radixware.kernel.designer.ads.editors.clazz.algo.workflow.palette;

import java.util.List;
import java.awt.Image;
import java.util.HashMap;
import java.util.ArrayList;
import org.openide.util.NbBundle;
import org.openide.util.ImageUtilities;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.*;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.AdsBaseObject.Kind;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.enums.EDwfFormSubmitVariant;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;


public class Item {

    public static final String IMAGE_PATH = "org/radixware/kernel/common/resources/workflow/";
    //public static final String APP_PATH = "org.radixware.kernel.server.algo.";

    public static final String ICON_DISCONNECT = "disconnect";
    public static final String ICON_EMPTY = "empty";
    public static final String ICON_NONE = "none";
    public static final String ICON_RECV = "recv";
    public static final String ICON_TIMEOUT = "timeout";
    public static final String ICON_OK = "Ok";
    public static final String ICON_CANCEL = "Cancel";

    private Kind kind;
    private String clazz;
    private Group_ group;
    private String title;
    private RadixIcon icon;

    private static class Items {

        private final ArrayList<Item> items = new ArrayList<Item>();
        private final HashMap<Kind, Item> kindItems = new HashMap<Kind, Item>();
        private final HashMap<String, Item> clazzItems = new HashMap<String, Item>();

        private static final Item PROGRAM = new Item(Kind.PROGRAM, Group_.GROUP_ALGBLOCKS, NbBundle.getMessage(Item.class, "Program"));
        private static final Item WAIT = new WaitItem();
        private static final Item SCOPE = new Item(Kind.SCOPE, Group_.GROUP_ALGBLOCKS, NbBundle.getMessage(Item.class, "Scope"));
        private static final Item INCLUDE = new Item(Kind.INCLUDE, Group_.GROUP_ALGBLOCKS, NbBundle.getMessage(Item.class, "Include"));
        private static final Item FINISH = new Item(Kind.FINISH, Group_.GROUP_ALGBLOCKS, NbBundle.getMessage(Item.class, "Finish"));
        private static final Item RETURN = new Item(Kind.RETURN, Group_.GROUP_ALGBLOCKS, NbBundle.getMessage(Item.class, "Return"));
        private static final Item TERMINATE = new Item(Kind.TERMINATE, Group_.GROUP_ALGBLOCKS, NbBundle.getMessage(Item.class, "Term"));
        private static final Item THROW = new Item(Kind.THROW, Group_.GROUP_ALGBLOCKS, NbBundle.getMessage(Item.class, "Throw"));
        private static final Item CATCH = new Item(Kind.CATCH, Group_.GROUP_ALGBLOCKS, NbBundle.getMessage(Item.class, "Catch"));
        private static final Item FORK = new Item(Kind.FORK, Group_.GROUP_ALGBLOCKS, NbBundle.getMessage(Item.class, "Fork"));
        private static final Item MERGE = new Item(Kind.MERGE, Group_.GROUP_ALGBLOCKS, NbBundle.getMessage(Item.class, "Merge"));
        private static final Item EMPTY = new Item(Kind.EMPTY, Group_.GROUP_ALGBLOCKS, NbBundle.getMessage(Item.class, "Empty"));
        private static final Item NOTE = new Item(Kind.NOTE, Group_.GROUP_ALGBLOCKS, NbBundle.getMessage(Item.class, "Note"));
        private static final Item VAR = new Item(Kind.VAR, Group_.GROUP_ALGBLOCKS, NbBundle.getMessage(Item.class, "Var"));

        private static final ReportGeneratorItem REPORT_GENERATOR = new ReportGeneratorItem();
        private static final NetPortItem NET_PORT = new NetPortItem();
        private static final PersonalCommunicatorItem PERSONAL_COMMUNICATOR = new PersonalCommunicatorItem();
        private static final EditorFormCreatorItem EDITOR_FORM_CREATOR = new EditorFormCreatorItem();
        private static final SelectorFormCreatorItem SELECTOR_FORM_CREATOR = new SelectorFormCreatorItem();
        private static final DocManagerCreatorItem DOC_MANAGER_CREATOR = new DocManagerCreatorItem();
        private static final DialogCreatorItem DIALOG_CREATOR = new DialogCreatorItem();
        private static final DialogDuplicatorItem DIALOG_DUPLICATOR = new DialogDuplicatorItem();

        public Items() {
            // register algo blocks
            register(PROGRAM);
            register(WAIT);
            register(SCOPE);
            register(INCLUDE);
            register(FINISH);
            register(RETURN);
            register(TERMINATE);
            register(THROW);
            register(CATCH);
            register(FORK);
            register(MERGE);
            register(EMPTY);
            register(NOTE);
            register(VAR);
            // register app blocks
//            register(REPORT_GENERATOR);
            register(NET_PORT);
            register(PERSONAL_COMMUNICATOR);
            register(EDITOR_FORM_CREATOR);
            register(SELECTOR_FORM_CREATOR);
//            register(DOC_MANAGER_CREATOR);
            register(DIALOG_CREATOR);
            register(DIALOG_DUPLICATOR);
        }

        public void register(Item item) {
            switch (item.getKind()) {
                case APP:
                    clazzItems.put(item.getClazz(), item);
                    break;
                default:
                    kindItems.put(item.getKind(), item);
            }
            items.add(item);
        }

        public void unregister(Item item) {
            switch (item.getKind()) {
                case APP:
                    clazzItems.remove(item.getClazz());
                    break;
                default:
                    kindItems.remove(item.getKind());
            }
            items.remove(item);
        }

        public List<Item> getItems() {
            return items;
        }

        public Item getItem(AdsBaseObject node) {
            switch (node.getKind()) {
                case APP:
                    Item item = clazzItems.get(((AdsAppObject)node).getClazz());
                    if (item == null)
                        item = AppItem.DEFAULT;
                    return item;
                default:
                    return kindItems.get(node.getKind());
            }
        }
    };

    private static final HashMap<Branch, Items> branchItems = new HashMap<Branch, Items>();
    private static final HashMap<String, Image> pinIcons = new HashMap<String, Image>();

    static {
/*
        // register app blocks
        register(new ReportGeneratorItem());
        register(new NetPortItem());
//        register(new PersonalCommunicatorItem());
        register(new BatchExecutorItem());
        register(new EditorFormCreatorItem());
        register(new SelectorFormCreatorItem());
        register(new DocManagerCreatorItem());
        register(new DialogCreatorItem());
        register(new DialogDuplicatorItem());
*/
        // register exits
        registerPin(EDwfFormSubmitVariant.ABORT.getValue()/*"abort"*/,       ImageUtilities.loadImage(IMAGE_PATH + "app/exit/abort.png"));
        registerPin(EDwfFormSubmitVariant.CANCEL.getValue()/*"cancel"*/,     ImageUtilities.loadImage(IMAGE_PATH + "app/exit/cancel.png"));
        registerPin(EDwfFormSubmitVariant.CLOSE.getValue()/*"close"*/,       ImageUtilities.loadImage(IMAGE_PATH + "app/exit/close.png"));
        registerPin(EDwfFormSubmitVariant.COMMIT.getValue()/*"commit"*/,     ImageUtilities.loadImage(IMAGE_PATH + "app/exit/commit.png"));
        registerPin(EDwfFormSubmitVariant.FINISH.getValue()/*"finish"*/,     ImageUtilities.loadImage(IMAGE_PATH + "app/exit/finish.png"));
        registerPin(EDwfFormSubmitVariant.IGNORE.getValue()/*"ignore"*/,     ImageUtilities.loadImage(IMAGE_PATH + "app/exit/ignore.png"));
        registerPin(EDwfFormSubmitVariant.NEXT.getValue()/*"next"*/,         ImageUtilities.loadImage(IMAGE_PATH + "app/exit/next.png"));
        registerPin(EDwfFormSubmitVariant.NO.getValue()/*"no"*/,             ImageUtilities.loadImage(IMAGE_PATH + "app/exit/no.png"));
        registerPin(EDwfFormSubmitVariant.OK.getValue()/*"ok"*/,             ImageUtilities.loadImage(IMAGE_PATH + "app/exit/ok.png"));
        registerPin(EDwfFormSubmitVariant.PREV.getValue()/*"prev"*/,         ImageUtilities.loadImage(IMAGE_PATH + "app/exit/prev.png"));
        registerPin(EDwfFormSubmitVariant.PROCEED.getValue()/*"proceed"*/,   ImageUtilities.loadImage(IMAGE_PATH + "app/exit/proceed.png"));
        registerPin(EDwfFormSubmitVariant.RETRY.getValue()/*"retry"*/,       ImageUtilities.loadImage(IMAGE_PATH + "app/exit/retry.png"));
        registerPin(EDwfFormSubmitVariant.REVERSE.getValue()/*"reverse"*/,   ImageUtilities.loadImage(IMAGE_PATH + "app/exit/reverse.png"));
        registerPin(EDwfFormSubmitVariant.ROLLBACK.getValue()/*"rollback"*/, ImageUtilities.loadImage(IMAGE_PATH + "app/exit/rollback.png"));
        registerPin(EDwfFormSubmitVariant.SKIP.getValue()/*"skip"*/,         ImageUtilities.loadImage(IMAGE_PATH + "app/exit/skip.png"));
        registerPin(EDwfFormSubmitVariant.UNDO.getValue()/*"undo"*/,         ImageUtilities.loadImage(IMAGE_PATH + "app/exit/undo.png"));
        registerPin(EDwfFormSubmitVariant.YES.getValue()/*"yes"*/,           ImageUtilities.loadImage(IMAGE_PATH + "app/exit/yes.png"));
        registerPin(ICON_DISCONNECT, ImageUtilities.loadImage(IMAGE_PATH + "app/exit/disconnect.png"));
        registerPin(ICON_EMPTY,      ImageUtilities.loadImage(IMAGE_PATH + "app/exit/empty.png"));
        registerPin(ICON_NONE,       ImageUtilities.loadImage(IMAGE_PATH + "app/exit/none.png"));
        registerPin(ICON_RECV,       ImageUtilities.loadImage(IMAGE_PATH + "app/exit/recv.png"));
        registerPin(ICON_TIMEOUT,    ImageUtilities.loadImage(IMAGE_PATH + "app/exit/timeout.png"));
    }

    public Item(Kind kind, Group_ group, String title) {
        this.kind = kind;
        this.clazz = null;
        this.group = group;
        this.title = title;
        this.icon = AdsDefinitionIcon.WORKFLOW.calcIcon(kind);
    }

    public Item(String clazz, Group_ group, String title, RadixIcon icon) {
        this.kind = Kind.APP;
        this.clazz = clazz;
        this.group = group;
        this.title = title;
        this.icon = icon;
    }

    private static Items getItemsByBranch(RadixObject context) {
        assert context != null;
        final Branch branch = context.getBranch();
        Items items = branchItems.get(branch);
        if (items == null) {
            items = new Items();
            branchItems.put(branch, items);
        }
        return items;
    }

    public static void register(RadixObject context, Item item) {
        Items items = getItemsByBranch(context);
        items.register(item);
    }

    public static void unregister(RadixObject context, Item item) {
        Items items = getItemsByBranch(context);
        items.unregister(item);
    }

    public static List<Item> getItems(RadixObject context) {
        Items items = getItemsByBranch(context);
        return items.getItems();
    }

    public static Item getItem(AdsBaseObject node) {
        Items items = getItemsByBranch(node);
        return items.getItem(node);
    }

    public static void registerPin(String name, Image image) {
        pinIcons.put(name, image);
    }

    public static Image getPin(String name) {
        return pinIcons.get(name);
    }

    public Kind getKind() {
        return kind;
    }

    public String getClazz() {
        return clazz;
    }

    public Group_ getGroup() {
        return group;
    }

    public String getTitle() {
        return title;
    }

    public RadixIcon getIcon() {
        return icon;
    }

    public Image getBigImage() {
        return icon.getImage(20);
    }

    public Image getSmalImage() {
        return icon.getImage(16);
    }

    public boolean isUserClass() {
        return group.equals(Group_.GROUP_APPBLOCKS) && !clazz.contains(".");
    }

    public AdsClassDef getUserDef(AdsDefinition context) {
        if (isUserClass() && context != null)
            return AdsSearcher.Factory.newAdsClassSearcher(context).findById(Id.Factory.loadFrom(clazz)).get();
        return null;
    }
    
}
