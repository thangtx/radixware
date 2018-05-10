package org.radixware.kernel.common.defs.ads.userfunc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IFileSystemRadixObject;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.utils.XPathUtils;
import org.radixware.kernel.common.utils.XmlUtils;
import org.w3c.dom.Element;


public class UdsUtils {

    public static final String JAVA_SRC = "JavaSrc";
    public static final String STRINGS  = "Strings";
    public static final String USERFUNCLIB  = "UserFuncLib";
    public static final String LIBRARY = "Library";
    public static final String METHOD_PROFILE = "MethodProfile";
    public static final String METHOD_ID = "MethodId";
    public static final String FUNCTIONS = "Functions";
    public static final String EXCHANGE_USER_REPORT = "AdsUserReportExchange";
    public static final String USER_REPORT = "AdsUserReportDefinition";
    
    
    public static final String COMMON_NAMESPASE = "http://schemas.radixware.org/common.xsd";
    public static final String USER_FUNC_LIB_NAMESPASE = "http://schemas.radixware.org/userfunc-impexp.xsd";
    
    public static final QName JAVA_SRC_QNAME = new QName(COMMON_NAMESPASE, JAVA_SRC);
    public static final QName STRINGS_QNAME = new QName(COMMON_NAMESPASE, STRINGS);
    public static final QName USERFUNCLIB_QNAME = new QName(USER_FUNC_LIB_NAMESPASE, USERFUNCLIB);
    public static final QName LIBRARY_QNAME = new QName(USER_FUNC_LIB_NAMESPASE, LIBRARY);

    public static String getXPath(Element e) {
        List<String> list = new ArrayList<>();
        list.add("id");
        return XPathUtils.getXPath(e, list);
    }

}
