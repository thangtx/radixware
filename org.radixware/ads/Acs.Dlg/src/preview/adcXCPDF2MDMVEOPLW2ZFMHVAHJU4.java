
/* Radix::Acs.Dlg::RoleEditorUtils - Desktop Executable*/

/*Radix::Acs.Dlg::RoleEditorUtils-Client-Common Dynamic Class*/

package org.radixware.ads.Acs.Dlg.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs.Dlg::RoleEditorUtils")
public published class RoleEditorUtils  {

	public static final com.trolltech.qt.gui.QBrush APP_ROLE_BRUSH = new com.trolltech.qt.gui.QBrush(com.trolltech.qt.gui.QColor.blue);
	static final class RolesComparator implements java.util.Comparator<Meta::AdsDefXsd:RoleListDocument.RoleList.Role> {   
	    public int compare(Meta::AdsDefXsd:RoleListDocument.RoleList.Role role1, Meta::AdsDefXsd:RoleListDocument.RoleList.Role role2) {                  
	        int res = role1.Name.compareTo(role2.Name);          
	        return res > 0 ? 1 : (res < 0 ? - 1 : 0);
	    }
	}

	static final String unrestricted_access = "<unrestricted access>";

	/*Radix::Acs.Dlg::RoleEditorUtils:Nested classes-Nested Classes*/

	/*Radix::Acs.Dlg::RoleEditorUtils:RoleData-Client-Common Dynamic Class*/


	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs.Dlg::RoleEditorUtils:RoleData")
	public static class RoleData  {



		/*Radix::Acs.Dlg::RoleEditorUtils:RoleData:Nested classes-Nested Classes*/

		/*Radix::Acs.Dlg::RoleEditorUtils:RoleData:Properties-Properties*/

		/*Radix::Acs.Dlg::RoleEditorUtils:RoleData:data-Dynamic Property*/



		protected Str data=null;











		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs.Dlg::RoleEditorUtils:RoleData:data")
		public final  Str getData() {
			return data;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs.Dlg::RoleEditorUtils:RoleData:data")
		public final   void setData(Str val) {
			data = val;
		}

		/*Radix::Acs.Dlg::RoleEditorUtils:RoleData:realFullName-Dynamic Property*/



		protected Str realFullName=null;











		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs.Dlg::RoleEditorUtils:RoleData:realFullName")
		public final  Str getRealFullName() {
			return realFullName;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs.Dlg::RoleEditorUtils:RoleData:realFullName")
		public final   void setRealFullName(Str val) {
			realFullName = val;
		}

		/*Radix::Acs.Dlg::RoleEditorUtils:RoleData:isAppRole-Dynamic Property*/



		protected boolean isAppRole=false;











		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs.Dlg::RoleEditorUtils:RoleData:isAppRole")
		public final  boolean getIsAppRole() {
			return isAppRole;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs.Dlg::RoleEditorUtils:RoleData:isAppRole")
		public final   void setIsAppRole(boolean val) {
			isAppRole = val;
		}

		/*Radix::Acs.Dlg::RoleEditorUtils:RoleData:Methods-Methods*/

		/*Radix::Acs.Dlg::RoleEditorUtils:RoleData:RoleData-User Method*/

		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs.Dlg::RoleEditorUtils:RoleData:RoleData")
		public  RoleData (Str data, Str realFullName, boolean isAppRole) {
			data = data;
			realFullName = realFullName;
			isAppRole = isAppRole;
		}


	}

	/*Radix::Acs.Dlg::RoleEditorUtils:RoleTitleCache-Client-Common Dynamic Class*/


	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs.Dlg::RoleEditorUtils:RoleTitleCache")
	public static published class RoleTitleCache  {

		private static final long RESET_PERIOD = 60_000; // 1 min 
		private static final java.util.Map<String, String> cache = new java.util.HashMap<String, String>();
		private static final java.util.concurrent.atomic.AtomicLong lastReset = new java.util.concurrent.atomic.AtomicLong(System.currentTimeMillis() - (RESET_PERIOD + 1));
		private static final java.util.concurrent.locks.ReentrantReadWriteLock cacheManager = new java.util.concurrent.locks.ReentrantReadWriteLock();

		private static void updateCache(org.radixware.kernel.common.client.IClientEnvironment environment) {
		    final long lastResetMillis = lastReset.get();
		    if (System.currentTimeMillis() - lastResetMillis > RESET_PERIOD && lastReset.compareAndSet(lastResetMillis, System.currentTimeMillis())) {
		        try {
		            cacheManager.writeLock().lock();
		            cache.clear();

		            org.apache.xmlbeans.XmlObject response = null;
		            try {
		                response = environment.getEasSession().executeContextlessCommand(idof[GetAllRoleTitle], null, Acs::CommandsXsd:RoleGuidsTitleArrDocument.class);
		            } catch (Exception ex) {
		                environment.messageException("Error", "Error occurred while decoding the GUID of the roles.", ex);
		            }

		            if (response != null) {
		                java.util.List<Acs::CommandsXsd:RoleGuidsTitleArrDocument.RoleGuidsTitleArr.RoleTitleGuids> responseList = ((Acs::CommandsXsd:RoleGuidsTitleArrDocument) response).getRoleGuidsTitleArr().RoleTitleGuidsList;
		                for (Acs::CommandsXsd:RoleGuidsTitleArrDocument.RoleGuidsTitleArr.RoleTitleGuids elem : responseList) {
		                    cache.put(elem.getRoleGuid(), elem.getRoleTitle());
		                }
		            } else {
		                environment.messageWarning("Error", "Error occurred while decoding the GUID of the roles.");
		            }
		        } finally {
		            cacheManager.writeLock().unlock();
		        }

		        lastReset.set(System.currentTimeMillis());
		    }
		}

		/*Radix::Acs.Dlg::RoleEditorUtils:RoleTitleCache:Nested classes-Nested Classes*/

		/*Radix::Acs.Dlg::RoleEditorUtils:RoleTitleCache:Properties-Properties*/

		/*Radix::Acs.Dlg::RoleEditorUtils:RoleTitleCache:Methods-Methods*/

		/*Radix::Acs.Dlg::RoleEditorUtils:RoleTitleCache:getTitleByGuids-User Method*/

		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs.Dlg::RoleEditorUtils:RoleTitleCache:getTitleByGuids")
		public static  Str getTitleByGuids (org.radixware.kernel.common.types.ArrStr guids, org.radixware.kernel.common.client.IClientEnvironment environment) {
			updateCache(environment);

			if (guids != null && !guids.isEmpty()) {
			    
			    StringBuilder titleStr = new StringBuilder();

			    try {
			        cacheManager.readLock().lock();

			        for (int i = 0; i < guids.size(); i++) {
			            if (titleStr.length() > 0) {
			                titleStr.append("; ");
			            }

			            titleStr.append(cache.containsKey(guids.get(i)) ? cache.get(guids.get(i)) : "?");
			        }
			    } finally {
			        cacheManager.readLock().unlock();
			    }
			    
			    return titleStr.toString();
			}

			return "";
		}


	}

	/*Radix::Acs.Dlg::RoleEditorUtils:Properties-Properties*/

	/*Radix::Acs.Dlg::RoleEditorUtils:Methods-Methods*/

	/*Radix::Acs.Dlg::RoleEditorUtils:applyStyle-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs.Dlg::RoleEditorUtils:applyStyle")
	public static published  void applyStyle (org.radixware.kernel.common.client.IClientEnvironment environment, java.lang.Object roleRepresentationObject, boolean isApplicationRole, int column) {
		if (isApplicationRole) {
		    Client.Types::Icon iconDescriptor = environment.getDefManager().getImage(idof[Acs::user]);
		    com.trolltech.qt.gui.QIcon icon = new com.trolltech.qt.gui.QIcon(Explorer.Icons::ExplorerIcon.getQIcon(iconDescriptor).pixmap(16, 16));
		    if ((roleRepresentationObject instanceof Explorer.Qt.Types::QWidget)) {
		        ((Explorer.Qt.Types::QWidget) roleRepresentationObject).setStyleSheet("foreground-color: " + APP_ROLE_BRUSH.color().name());
		    } else if ((roleRepresentationObject instanceof Explorer.Qt.Types::QTreeWidgetItem)) {
		        ((Explorer.Qt.Types::QTreeWidgetItem) roleRepresentationObject).setForeground(column, APP_ROLE_BRUSH);
		        ((Explorer.Qt.Types::QTreeWidgetItem) roleRepresentationObject).setIcon(column, icon);
		    } else if ((roleRepresentationObject instanceof com.trolltech.qt.gui.QListWidgetItem)) {
		        ((com.trolltech.qt.gui.QListWidgetItem) roleRepresentationObject).setForeground(APP_ROLE_BRUSH);
		        ((com.trolltech.qt.gui.QListWidgetItem) roleRepresentationObject).setIcon(icon);
		    } else {
		        throw new AppError("Unsupported role representation object: " + roleRepresentationObject);
		    }
		} else {
		    Client.Types::Icon iconDescriptor = environment.getDefManager().getImage(idof[System::System]);
		    com.trolltech.qt.gui.QIcon icon = new com.trolltech.qt.gui.QIcon(Explorer.Icons::ExplorerIcon.getQIcon(iconDescriptor).pixmap(16, 16));
		    if ((roleRepresentationObject instanceof Explorer.Qt.Types::QTreeWidgetItem)) {
		        ((Explorer.Qt.Types::QTreeWidgetItem) roleRepresentationObject).setIcon(column, icon);
		    } else if ((roleRepresentationObject instanceof com.trolltech.qt.gui.QListWidgetItem)) {
		        ((com.trolltech.qt.gui.QListWidgetItem) roleRepresentationObject).setIcon(icon);
		    } else {
		        throw new AppError("Unsupported role representation object: " + roleRepresentationObject);
		    }
		}
	}

	/*Radix::Acs.Dlg::RoleEditorUtils:applyStyle-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs.Dlg::RoleEditorUtils:applyStyle")
	public static published  void applyStyle (org.radixware.kernel.common.client.IClientEnvironment environment, java.lang.Object roleRepresentationObject, boolean isApplicationRole) {
		applyStyle(environment, roleRepresentationObject, isApplicationRole, 0);
	}

	/*Radix::Acs.Dlg::RoleEditorUtils:createLegendWidget-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs.Dlg::RoleEditorUtils:createLegendWidget")
	public static published  com.trolltech.qt.gui.QWidget createLegendWidget (org.radixware.kernel.common.client.IClientEnvironment environment, boolean isNeedShowEas) {
		Explorer.Qt.Types::QWidget widget = new QWidget();
		com.trolltech.qt.gui.QHBoxLayout layout = new com.trolltech.qt.gui.QHBoxLayout();
		layout.setAlignment(new com.trolltech.qt.core.Qt.Alignment(com.trolltech.qt.core.Qt.AlignmentFlag.AlignCenter));
		widget.setLayout(layout);

		Client.Types::Icon iconDescriptor = environment.getDefManager().getImage(idof[System::System]);
		com.trolltech.qt.gui.QIcon sysRole = Explorer.Icons::ExplorerIcon.getQIcon(iconDescriptor);

		com.trolltech.qt.gui.QLabel iconSystemRole = new com.trolltech.qt.gui.QLabel();
		iconSystemRole.setPixmap(sysRole.pixmap(16, 16));
		layout.addWidget(iconSystemRole);

		com.trolltech.qt.gui.QLabel labelSystemRole = new com.trolltech.qt.gui.QLabel("- system role");
		layout.addWidget(labelSystemRole);

		iconDescriptor = environment.getDefManager().getImage(idof[Acs::user]);
		com.trolltech.qt.gui.QIcon appRole = Explorer.Icons::ExplorerIcon.getQIcon(iconDescriptor);

		com.trolltech.qt.gui.QLabel iconAppRole = new com.trolltech.qt.gui.QLabel();
		iconAppRole.setPixmap(appRole.pixmap(16, 16));
		layout.addWidget(iconAppRole);

		com.trolltech.qt.gui.QLabel labelAppRole = new com.trolltech.qt.gui.QLabel("<font color='blue'>" + "- application role" + "</font>");
		layout.addWidget(labelAppRole);

		if (isNeedShowEas) {
		    iconDescriptor = environment.getDefManager().getImage(idof[Explorer.Icons::explorer]);
		    com.trolltech.qt.gui.QIcon explorerIcon = Explorer.Icons::ExplorerIcon.getQIcon(iconDescriptor);

		    com.trolltech.qt.gui.QLabel explorerIconLabel = new com.trolltech.qt.gui.QLabel();
		    explorerIconLabel.setPixmap(explorerIcon.pixmap(16, 16));
		    layout.addWidget(explorerIconLabel);

		    com.trolltech.qt.gui.QLabel labelExplorerIcon = new com.trolltech.qt.gui.QLabel("- connect to Explorer Access Service");
		    layout.addWidget(labelExplorerIcon);
		}

		widget.setSizePolicy(com.trolltech.qt.gui.QSizePolicy.Policy.Expanding, com.trolltech.qt.gui.QSizePolicy.Policy.Minimum);

		return widget;


	}

	/*Radix::Acs.Dlg::RoleEditorUtils:getShortDescription-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs.Dlg::RoleEditorUtils:getShortDescription")
	public static  Str getShortDescription (Str description, int size) {
		if(description == null || description.isEmpty()) {
		    return "";
		}

		String text = description.replaceAll("\\s+", " ");
		String[] sText = text.split(" ");
		java.lang.StringBuilder finalText = new java.lang.StringBuilder(sText[0]);
		int index = 1;

		while(index < sText.length && (finalText.length() + sText[index].length() + 1) < size) {
		    finalText.append(" ").append(sText[index]);
		    index++;
		}

		return finalText.toString();
	}


}

/* Radix::Acs.Dlg::RoleEditorUtils - Desktop Meta*/

/*Radix::Acs.Dlg::RoleEditorUtils-Client-Common Dynamic Class*/

package org.radixware.ads.Acs.Dlg.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class RoleEditorUtils_mi{
}

/* Radix::Acs.Dlg::RoleEditorUtils - Web Executable*/

/*Radix::Acs.Dlg::RoleEditorUtils-Client-Common Dynamic Class*/

package org.radixware.ads.Acs.Dlg.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs.Dlg::RoleEditorUtils")
public published class RoleEditorUtils  {

	static final class RolesComparator implements java.util.Comparator<Meta::AdsDefXsd:RoleListDocument.RoleList.Role> {   
	    public int compare(Meta::AdsDefXsd:RoleListDocument.RoleList.Role role1, Meta::AdsDefXsd:RoleListDocument.RoleList.Role role2) {                  
	        int res = role1.Name.compareTo(role2.Name);          
	        return res > 0 ? 1 : (res < 0 ? - 1 : 0);
	    }
	}

	static final String unrestricted_access = "<unrestricted access>";

	/*Radix::Acs.Dlg::RoleEditorUtils:Nested classes-Nested Classes*/

	/*Radix::Acs.Dlg::RoleEditorUtils:RoleData-Client-Common Dynamic Class*/


	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs.Dlg::RoleEditorUtils:RoleData")
	public static class RoleData  {



		/*Radix::Acs.Dlg::RoleEditorUtils:RoleData:Nested classes-Nested Classes*/

		/*Radix::Acs.Dlg::RoleEditorUtils:RoleData:Properties-Properties*/

		/*Radix::Acs.Dlg::RoleEditorUtils:RoleData:data-Dynamic Property*/



		protected Str data=null;











		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs.Dlg::RoleEditorUtils:RoleData:data")
		public final  Str getData() {
			return data;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs.Dlg::RoleEditorUtils:RoleData:data")
		public final   void setData(Str val) {
			data = val;
		}

		/*Radix::Acs.Dlg::RoleEditorUtils:RoleData:realFullName-Dynamic Property*/



		protected Str realFullName=null;











		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs.Dlg::RoleEditorUtils:RoleData:realFullName")
		public final  Str getRealFullName() {
			return realFullName;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs.Dlg::RoleEditorUtils:RoleData:realFullName")
		public final   void setRealFullName(Str val) {
			realFullName = val;
		}

		/*Radix::Acs.Dlg::RoleEditorUtils:RoleData:isAppRole-Dynamic Property*/



		protected boolean isAppRole=false;











		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs.Dlg::RoleEditorUtils:RoleData:isAppRole")
		public final  boolean getIsAppRole() {
			return isAppRole;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs.Dlg::RoleEditorUtils:RoleData:isAppRole")
		public final   void setIsAppRole(boolean val) {
			isAppRole = val;
		}

		/*Radix::Acs.Dlg::RoleEditorUtils:RoleData:Methods-Methods*/

		/*Radix::Acs.Dlg::RoleEditorUtils:RoleData:RoleData-User Method*/

		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs.Dlg::RoleEditorUtils:RoleData:RoleData")
		public  RoleData (Str data, Str realFullName, boolean isAppRole) {
			data = data;
			realFullName = realFullName;
			isAppRole = isAppRole;
		}


	}

	/*Radix::Acs.Dlg::RoleEditorUtils:RoleTitleCache-Client-Common Dynamic Class*/


	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs.Dlg::RoleEditorUtils:RoleTitleCache")
	public static published class RoleTitleCache  {

		private static final long RESET_PERIOD = 60_000; // 1 min 
		private static final java.util.Map<String, String> cache = new java.util.HashMap<String, String>();
		private static final java.util.concurrent.atomic.AtomicLong lastReset = new java.util.concurrent.atomic.AtomicLong(System.currentTimeMillis() - (RESET_PERIOD + 1));
		private static final java.util.concurrent.locks.ReentrantReadWriteLock cacheManager = new java.util.concurrent.locks.ReentrantReadWriteLock();

		private static void updateCache(org.radixware.kernel.common.client.IClientEnvironment environment) {
		    final long lastResetMillis = lastReset.get();
		    if (System.currentTimeMillis() - lastResetMillis > RESET_PERIOD && lastReset.compareAndSet(lastResetMillis, System.currentTimeMillis())) {
		        try {
		            cacheManager.writeLock().lock();
		            cache.clear();

		            org.apache.xmlbeans.XmlObject response = null;
		            try {
		                response = environment.getEasSession().executeContextlessCommand(idof[GetAllRoleTitle], null, Acs::CommandsXsd:RoleGuidsTitleArrDocument.class);
		            } catch (Exception ex) {
		                environment.messageException("Error", "Error occurred while decoding the GUID of the roles.", ex);
		            }

		            if (response != null) {
		                java.util.List<Acs::CommandsXsd:RoleGuidsTitleArrDocument.RoleGuidsTitleArr.RoleTitleGuids> responseList = ((Acs::CommandsXsd:RoleGuidsTitleArrDocument) response).getRoleGuidsTitleArr().RoleTitleGuidsList;
		                for (Acs::CommandsXsd:RoleGuidsTitleArrDocument.RoleGuidsTitleArr.RoleTitleGuids elem : responseList) {
		                    cache.put(elem.getRoleGuid(), elem.getRoleTitle());
		                }
		            } else {
		                environment.messageWarning("Error", "Error occurred while decoding the GUID of the roles.");
		            }
		        } finally {
		            cacheManager.writeLock().unlock();
		        }

		        lastReset.set(System.currentTimeMillis());
		    }
		}

		/*Radix::Acs.Dlg::RoleEditorUtils:RoleTitleCache:Nested classes-Nested Classes*/

		/*Radix::Acs.Dlg::RoleEditorUtils:RoleTitleCache:Properties-Properties*/

		/*Radix::Acs.Dlg::RoleEditorUtils:RoleTitleCache:Methods-Methods*/

		/*Radix::Acs.Dlg::RoleEditorUtils:RoleTitleCache:getTitleByGuids-User Method*/

		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs.Dlg::RoleEditorUtils:RoleTitleCache:getTitleByGuids")
		public static  Str getTitleByGuids (org.radixware.kernel.common.types.ArrStr guids, org.radixware.kernel.common.client.IClientEnvironment environment) {
			updateCache(environment);

			if (guids != null && !guids.isEmpty()) {
			    
			    StringBuilder titleStr = new StringBuilder();

			    try {
			        cacheManager.readLock().lock();

			        for (int i = 0; i < guids.size(); i++) {
			            if (titleStr.length() > 0) {
			                titleStr.append("; ");
			            }

			            titleStr.append(cache.containsKey(guids.get(i)) ? cache.get(guids.get(i)) : "?");
			        }
			    } finally {
			        cacheManager.readLock().unlock();
			    }
			    
			    return titleStr.toString();
			}

			return "";
		}


	}

	/*Radix::Acs.Dlg::RoleEditorUtils:Properties-Properties*/

	/*Radix::Acs.Dlg::RoleEditorUtils:Methods-Methods*/

	/*Radix::Acs.Dlg::RoleEditorUtils:makeLegend-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs.Dlg::RoleEditorUtils:makeLegend")
	public static  void makeLegend (org.radixware.kernel.common.client.IClientEnvironment environment, org.radixware.wps.rwt.Label system, org.radixware.wps.rwt.Label application) {
		org.radixware.wps.icons.WpsIcon icon = (org.radixware.wps.icons.WpsIcon) environment.getDefManager().getImage(idof[System::System]);
		Utils::Html div = new Html("div");
		system.getHtml().getParent().add(0, div);
		system.getHtml().setCss("padding", "10px");
		div.setCss("display", "blocl");
		div.setCss("width", "20px");
		div.setCss("height", "20px");
		div.setCss("margin-top", "5px");
		Utils::Html iconHtml = new Html("img");
		iconHtml.setAttr("src", icon.getURI(system));
		iconHtml.setCss("width", "20px");
		iconHtml.setCss("height", "20px");
		div.add(iconHtml);

		icon = (org.radixware.wps.icons.WpsIcon) environment.getDefManager().getImage(idof[Acs::user]);
		div = new Html("div");
		application.getHtml().getParent().add(2, div);
		application.getHtml().setCss("padding", "10px");
		div.setCss("display", "blocl");
		div.setCss("width", "20px");
		div.setCss("height", "20px");
		div.setCss("margin-top", "5px");
		iconHtml = new Html("img");
		iconHtml.setAttr("src", icon.getURI(system));
		iconHtml.setCss("width", "20px");
		iconHtml.setCss("height", "20px");
		div.add(iconHtml);
	}

	/*Radix::Acs.Dlg::RoleEditorUtils:getShortDescription-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs.Dlg::RoleEditorUtils:getShortDescription")
	public static  Str getShortDescription (Str description, int size) {
		if(description == null || description.isEmpty()) {
		    return "";
		}

		String text = description.replaceAll("\\s+", " ");
		String[] sText = text.split(" ");
		java.lang.StringBuilder finalText = new java.lang.StringBuilder(sText[0]);
		int index = 1;

		while(index < sText.length && (finalText.length() + sText[index].length() + 1) < size) {
		    finalText.append(" ").append(sText[index]);
		    index++;
		}

		return finalText.toString();
	}


}

/* Radix::Acs.Dlg::RoleEditorUtils - Web Meta*/

/*Radix::Acs.Dlg::RoleEditorUtils-Client-Common Dynamic Class*/

package org.radixware.ads.Acs.Dlg.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class RoleEditorUtils_mi{
}

/* Radix::Acs.Dlg::RoleEditorUtils - Localizing Bundle */
package org.radixware.ads.Acs.Dlg.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class RoleEditorUtils - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Error");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7UHH4CMGCBB6VPSR6C5DVMYBVA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Error occurred while decoding the GUID of the roles.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Произошла ошибка при расшифровке GUID\'ов ролей. ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKJPOJWM6WZG2TOEKALCWIQRD7A"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Error");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRV7GPMRJTJDTXCGVZR3C6RZU24"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Error occurred while decoding the GUID of the roles.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Произошла ошибка при расшифровке GUID\'ов ролей. ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSA4JFYDSERCKJMHIKHHZKAGU2I"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"- application role");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"- прикладная роль");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYRB54DNZCVBLNF3LC6YCCW5VWM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"- connect to Explorer Access Service");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"- даёт возможность подключения к EAS");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYTLE7KIC7FDMRMR4NVUULS3B54"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"- system role");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"- системная роль");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZYTZQL2APVBEBNPBEKPH2EZKF4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(RoleEditorUtils - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbadcXCPDF2MDMVEOPLW2ZFMHVAHJU4"),"RoleEditorUtils - Localizing Bundle",$$$items$$$);
}
