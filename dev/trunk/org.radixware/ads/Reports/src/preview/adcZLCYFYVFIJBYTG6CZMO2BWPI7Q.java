
/* Radix::Reports::InteractiveReportFileController - Server Executable*/

/*Radix::Reports::InteractiveReportFileController-Server Dynamic Class*/

package org.radixware.ads.Reports.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::InteractiveReportFileController")
 class InteractiveReportFileController  implements org.radixware.kernel.server.reports.IReportFileController,org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {

	private final String directory;
	private final String defaultFileName;

	private java.io.File tempDir = org.radixware.kernel.server.reports.DefaultReportFileController.createTmpDir();
	private final ReportExportFormat format;

	private final java.util.Map<ReportExportFormat, Str> format2ExportPath;
	private final java.util.Map<ReportExportFormat, Int> format2FileCount = new java.util.HashMap<ReportExportFormat, Int>();

	private boolean isNeedCountFiles = true;
	private int totalFileCount = 0;

	private boolean yesAll = false;
	private boolean noAll = false;
	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return InteractiveReportFileController_mi.rdxMeta;}

	/*Radix::Reports::InteractiveReportFileController:Nested classes-Nested Classes*/

	/*Radix::Reports::InteractiveReportFileController:Properties-Properties*/





























	/*Radix::Reports::InteractiveReportFileController:Methods-Methods*/

	/*Radix::Reports::InteractiveReportFileController:adjustFileName-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::InteractiveReportFileController:adjustFileName",line=69)
	public published  Str adjustFileName (org.radixware.kernel.server.types.Report report, Str fileName) {
		totalFileCount = isNeedCountFiles ? totalFileCount + 1 : totalFileCount;

		return fileName;
	}

	/*Radix::Reports::InteractiveReportFileController:afterCloseFile-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::InteractiveReportFileController:afterCloseFile",line=79)
	public published  void afterCloseFile (org.radixware.kernel.server.types.Report report, java.io.File file) {
		totalFileCount = isNeedCountFiles ? totalFileCount - 1 : totalFileCount;
		isNeedCountFiles = false;

		try {
		    Str fileName = calcFileName(report, file.getName());

		    Str outFileName;
		    if (format2ExportPath == null || format2ExportPath.isEmpty() || format2ExportPath.get(report.getCurrentExportFormat()) == null) {
		        outFileName = new java.io.File(directory, fileName).getPath();
		    } else {
		        Str formatPath = format2ExportPath.get(report.getCurrentExportFormat());                
		        outFileName = new java.io.File(formatPath, fileName).getPath();
		    }

		    if (Arte::Arte.getClientEnvironment() != Common::RuntimeEnvironmentType:WEB && ReportsServerUtils.isFileExists(outFileName)) {
		        if (noAll) {
		            return;
		        }

		        if (!yesAll) {
		            switch (ReportsServerUtils.showReplaceFileDialog(outFileName)) {
		                case Client.Resources::DialogButtonType:No:
		                    return;
		                case Client.Resources::DialogButtonType:NoToAll:
		                    noAll = true;
		                    return;
		                case Client.Resources::DialogButtonType:YesToAll:
		                    yesAll = true;
		                    break;
		                default:
		                    break;
		            }
		        }
		    }

		    Client.Resources::FileOutResource out = ReportsServerUtils.openFileOutResource(outFileName);

		    final java.io.FileInputStream in = new java.io.FileInputStream(file);
		    org.radixware.kernel.common.utils.FileUtils.copyStream(in, out);
		    out.flush();
		    out.close();
		} catch (Exceptions::IOException ex) {
		    Arte::Trace.error(Utils::ExceptionTextFormatter.exceptionStackToString(ex), Arte::EventSource:ArteReports);
		}
	}

	/*Radix::Reports::InteractiveReportFileController:afterCreateFile-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::InteractiveReportFileController:afterCreateFile",line=130)
	public published  void afterCreateFile (org.radixware.kernel.server.types.Report report, java.io.File file, java.io.OutputStream outputStream) {

	}

	/*Radix::Reports::InteractiveReportFileController:beforeCloseFile-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::InteractiveReportFileController:beforeCloseFile",line=138)
	public published  void beforeCloseFile (org.radixware.kernel.server.types.Report report, java.io.File file, java.io.OutputStream outputStream) {

	}

	/*Radix::Reports::InteractiveReportFileController:InteractiveReportFileController-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::InteractiveReportFileController:InteractiveReportFileController",line=145)
	public  InteractiveReportFileController (Str directory, org.radixware.kernel.common.enums.EReportExportFormat format) {
		this.directory = directory;
		this.format = format;

		this.format2ExportPath = null;
		this.defaultFileName = null;
	}

	/*Radix::Reports::InteractiveReportFileController:getDirectory-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::InteractiveReportFileController:getDirectory",line=157)
	public published  java.io.File getDirectory () {
		return tempDir;
	}

	/*Radix::Reports::InteractiveReportFileController:InteractiveReportFileController-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::InteractiveReportFileController:InteractiveReportFileController",line=164)
	public  InteractiveReportFileController (Str directory, java.util.Map<org.radixware.kernel.common.enums.EReportExportFormat,Str> format2ExportPath, Str defaultFileName) {
		this.directory = directory;
		this.defaultFileName = defaultFileName;
		this.format2ExportPath = format2ExportPath != null ? new java.util.HashMap<ReportExportFormat, Str>(format2ExportPath) : null;

		this.format = null;
	}

	/*Radix::Reports::InteractiveReportFileController:calcFileName-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports::InteractiveReportFileController:calcFileName",line=175)
	public  Str calcFileName (org.radixware.kernel.server.types.Report report, Str fileName) {
		if (defaultFileName == null || defaultFileName.isEmpty()) {
		    return fileName;
		}

		ReportExportFormat exportFormat = report.getCurrentExportFormat();

		java.lang.StringBuilder suffix = new java.lang.StringBuilder("");
		if (report.isMultyFile()) {        
		    Int fileCount = format2FileCount.get(exportFormat);
		    format2FileCount.put(exportFormat, fileCount == null ? 1 : fileCount.intValue() + 1);
		    suffix.append(" ").append(Str.valueOf(fileCount == null ? 1 : fileCount.intValue() + 1));

		    if (totalFileCount > 0) {       
		        suffix.append(" of ").append(Str.valueOf(totalFileCount));
		    }
		}
		suffix.append(exportFormat == ReportExportFormat:XLS ? ".xls" : "." + exportFormat.getExt());

		return defaultFileName + suffix.toString();
	}


}

/* Radix::Reports::InteractiveReportFileController - Server Meta*/

/*Radix::Reports::InteractiveReportFileController-Server Dynamic Class*/

package org.radixware.ads.Reports.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class InteractiveReportFileController_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("adcZLCYFYVFIJBYTG6CZMO2BWPI7Q"),"InteractiveReportFileController",null,

						org.radixware.kernel.common.enums.EClassType.DYNAMIC,
						null,
						null,
						null,

						/*Radix::Reports::InteractiveReportFileController:Properties-Properties*/
						null,

						/*Radix::Reports::InteractiveReportFileController:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthMMHPPAI5T5EFHOO5OTAQN4DSWI"),"adjustFileName",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("report",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCQMPQYYB4JC6VGLDTCB3WRTJN4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("fileName",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprVXP55YM2SJF7XFKIB2HFJN4BK4"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthHCFFTVMPBFBMVJOMMDZMTW4LWU"),"afterCloseFile",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("report",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprISVG2PPZSJDQLCR75SJMEZXPVE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("file",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr3JYAPCXELBCDTHPCOVYZ5ZDCFM"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPQ6T4E3WPFELTDJAEYHOVJIXMU"),"afterCreateFile",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("report",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprB6C7WKR5WFE2NKW64WIJXTF5ZQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("file",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprXQI5VPZV75A75CLY3OPTB32RXU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("outputStream",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr3LHWL2WPWBCHDIFYCHZZVBDUIU"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthEQWZZHVBAZGUTNKSUVXAIQ4ZPI"),"beforeCloseFile",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("report",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprR3I6K4XNY5ENTDBUZKDNFKKEMY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("file",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCLHTBWIHOJAXFOOFBQG4NDMCDY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("outputStream",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprODRHGSJKIVHXJFNFRPJ5E5EDQ4"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthENOMTP7QJVA4HIADRTVA5QKQEA"),"InteractiveReportFileController",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("directory",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr33NOMZBKSNCU3D2DSHNBUA4ZYU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("format",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr7IFFO5A6INGVJDEMGV5FMEBGKQ"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthNMVOW6LIMVH3LA5FQV52TIFPOI"),"getDirectory",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthDDBBDTLSVVBXFE5MM42BYR6GKE"),"InteractiveReportFileController",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("directory",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprZRZABYWSLVGM3JTI7INZ2GKCFQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("format2ExportPath",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprVIDNASQYM5BRLNWRX7WD4XUORI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("defaultFileName",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprHAWJCHL24NH6NCNPFCRN5WRYOU"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthRBPNIR63SFHV3EW37CJ7C4GPD4"),"calcFileName",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("report",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprJSD2UHK76NEYFH5T7L5YG2QARM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("fileName",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprKZN4UGMN7VG6JAPH47QWKAQSCQ"))
								},org.radixware.kernel.common.enums.EValType.STR)
						},
						null,
						null,null,null,false);
}

/* Radix::Reports::InteractiveReportFileController - Localizing Bundle */
package org.radixware.ads.Reports.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class InteractiveReportFileController - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN," из ");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH," of ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUEBCZSWGLNFY7PXK4BABRKZYKM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"E:\\SVN\\RW_TRUNK\\org.radixware\\ads\\Reports"));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(InteractiveReportFileController - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbadcZLCYFYVFIJBYTG6CZMO2BWPI7Q"),"InteractiveReportFileController - Localizing Bundle",$$$items$$$);
}
