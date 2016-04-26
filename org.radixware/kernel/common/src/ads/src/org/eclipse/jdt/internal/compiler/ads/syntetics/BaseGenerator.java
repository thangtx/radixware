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

package org.eclipse.jdt.internal.compiler.ads.syntetics;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.ast.AbstractMethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.AllocationExpression;
import org.eclipse.jdt.internal.compiler.ast.Annotation;
import org.eclipse.jdt.internal.compiler.ast.Argument;
import org.eclipse.jdt.internal.compiler.ast.Assignment;

import org.eclipse.jdt.internal.compiler.ast.Block;
import org.eclipse.jdt.internal.compiler.ast.CastExpression;
import org.eclipse.jdt.internal.compiler.ast.DoubleLiteral;
import org.eclipse.jdt.internal.compiler.ast.EqualExpression;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.FalseLiteral;
import org.eclipse.jdt.internal.compiler.ast.FieldReference;
import org.eclipse.jdt.internal.compiler.ast.InstanceOfExpression;
import org.eclipse.jdt.internal.compiler.ast.IntLiteral;
import org.eclipse.jdt.internal.compiler.ast.LocalDeclaration;
import org.eclipse.jdt.internal.compiler.ast.LongLiteral;
import org.eclipse.jdt.internal.compiler.ast.MarkerAnnotation;
import org.eclipse.jdt.internal.compiler.ast.MessageSend;
import org.eclipse.jdt.internal.compiler.ast.MethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.NullLiteral;
import org.eclipse.jdt.internal.compiler.ast.OperatorIds;
import org.eclipse.jdt.internal.compiler.ast.ParameterizedQualifiedTypeReference;
import org.eclipse.jdt.internal.compiler.ast.QualifiedNameReference;
import org.eclipse.jdt.internal.compiler.ast.QualifiedTypeReference;
import org.eclipse.jdt.internal.compiler.ast.SingleMemberAnnotation;
import org.eclipse.jdt.internal.compiler.ast.SingleNameReference;
import org.eclipse.jdt.internal.compiler.ast.SingleTypeReference;
import org.eclipse.jdt.internal.compiler.ast.Statement;
import org.eclipse.jdt.internal.compiler.ast.StringLiteral;
import org.eclipse.jdt.internal.compiler.ast.TaggedSingleNameReference;
import org.eclipse.jdt.internal.compiler.ast.ThisReference;
import org.eclipse.jdt.internal.compiler.ast.ThrowStatement;
import org.eclipse.jdt.internal.compiler.ast.TrueLiteral;
import org.eclipse.jdt.internal.compiler.ast.TypeReference;
import org.eclipse.jdt.internal.compiler.ast.UnaryExpression;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.impl.ReferenceContext;
import org.eclipse.jdt.internal.compiler.lookup.TypeIds;
import org.radixware.kernel.common.compiler.core.ast.JMLMessageSend;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.profiling.AdsProfileSupport;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Id;


public class BaseGenerator {

    public static final char[] ORG = "org".toCharArray();
    public static final char[] APACHE = "apache".toCharArray();
    public static final char[] XMLBEANS = "xmlbeans".toCharArray();
    public static final char[] RADIXWARE = "radixware".toCharArray();
    public static final char[] KERNEL = "kernel".toCharArray();
    public static final char[] SERVER = "server".toCharArray();
    public static final char[] COMMON = "common".toCharArray();
    public static final char[] ARTE = "arte".toCharArray();
    public static final char[] JAVA = "java".toCharArray();
    public static final char[] LANG = "lang".toCharArray();
    public static final char[] IO = "io".toCharArray();
    public static final char[] EXCEPTIONS = "exceptions".toCharArray();
    public static final char[] SQL = "sql".toCharArray();
    public static final char[] TYPES = "types".toCharArray();
    public static final char[] DEFS = "defs".toCharArray();
    public static final char[] ADS = "ads".toCharArray();
    public static final char[] CLAZZ = "clazz".toCharArray();
    public static final char[] REPORT = "report".toCharArray();
    public static final char[] FACTORY = "Factory".toCharArray();
    public static final char[] NEWINSTANCE = "newInstance".toCharArray();
    public static final char[] MAP = "Map".toCharArray();
    public static final char[] UTIL = "util".toCharArray();
    public static final char[] UTILS = "utils".toCharArray();
    public static final char[] AWT = "awt".toCharArray();
    public static final char[][] JAVALANGOBJECT_TYPENAME = new char[][]{
        JAVA, LANG, "Object".toCharArray()
    };
    public static final char[][] JAVALANGSTRING_TYPENAME = new char[][]{
        JAVA, LANG, "String".toCharArray()
    };
    public static final char[][] SRVVALASSTR_TYPE_NAME = new char[][]{
        ORG, RADIXWARE, KERNEL, SERVER, "utils".toCharArray(), "SrvValAsStr".toCharArray()
    };
    public static final char[][] EVALTYPE_TYPE_NAME = new char[][]{
        ORG, RADIXWARE, KERNEL, COMMON, "enums".toCharArray(), "EValType".toCharArray()
    };
    public static final char[][] INPUTSTREAM_TYPE_NAME = new char[][]{
        JAVA, IO, "InputStream".toCharArray()
    };
    public static final char[][] READER_TYPE_NAME = new char[][]{
        JAVA, IO, "Reader".toCharArray()
    };
    public static final char[][] CLOB_TYPE_NAME = new char[][]{
        JAVA, SQL, "Clob".toCharArray()
    };
    public static final char[][] BLOB_TYPE_NAME = new char[][]{
        JAVA, SQL, "Blob".toCharArray()
    };
    public static final char[][] XMLOPTIONS_TYPE_NAME = new char[][]{
        ORG, APACHE, XMLBEANS, "XmlOptions".toCharArray()
    };
    public static final char[][] XMLOBJECT_TYPE_NAME = new char[][]{
        ORG, APACHE, XMLBEANS, "XmlObject".toCharArray()
    };
    public static final char[][] XMLBEANS_TYPE_NAME = new char[][]{
        ORG, APACHE, XMLBEANS, "XmlBeans".toCharArray()
    };
    public static final char[][] SQLEXCEPTION_TYPE_NAME = new char[][]{
        JAVA, SQL, "SQLException".toCharArray()
    };
    public static final char[][] IOEXCEPTION_TYPE_NAME = new char[][]{
        JAVA, IO, "IOException".toCharArray()
    };
    public static final char[][] XMLEXCEPTION_TYPE_NAME = new char[][]{
        ORG, APACHE, XMLBEANS, "XmlException".toCharArray()
    };
    public static final char[][] XMLSTREAMEXCEPTION_TYPE_NAME = new char[][]{
        ORG, APACHE, XMLBEANS, "xml".toCharArray(), "stream".toCharArray(), "XMLStreamException".toCharArray()
    };
    public static final char[][] DATABASEERROR_TYPE_NAME = new char[][]{
        ORG, RADIXWARE, KERNEL, SERVER, EXCEPTIONS, "DatabaseError".toCharArray()
    };
    public static final char[][] WRONGFORMATERROR_TYPE_NAME = new char[][]{
        ORG, RADIXWARE, KERNEL, COMMON, EXCEPTIONS, "WrongFormatError".toCharArray()
    };
    public static final char[][] ENTITYOBJECTNOTEXISTERROR_TYPE_NAME = new char[][]{
        ORG, RADIXWARE, KERNEL, SERVER, EXCEPTIONS, "EntityObjectNotExistsError".toCharArray()
    };
    public static final char[][] NOCONSTITEMVITHSUCHVALUEERROR_TYPE_NAME = new char[][]{
        ORG, RADIXWARE, KERNEL, COMMON, EXCEPTIONS, "NoConstItemWithSuchValueError".toCharArray()
    };
    public static final char[][] ID_TYPE_NAME = new char[][]{
        ORG, RADIXWARE, KERNEL, COMMON, TYPES, "Id".toCharArray()
    };
    public static final char[][] SERVERPIDID_TYPE_NAME = new char[][]{
        ORG, RADIXWARE, KERNEL, SERVER, TYPES, "Pid".toCharArray()
    };
    public static final char[][] IDFACTORY_TYPE_NAME = new char[][]{
        ORG, RADIXWARE, KERNEL, COMMON, TYPES, "Id".toCharArray(), "Factory".toCharArray()
    };
    public static final char[][] JAVAIOFILE_TYPE_NAME = new char[][]{
        JAVA, IO, "File".toCharArray()
    };
    public static final char[][] JAVAIOINPUTSTREAM_TYPE_NAME = new char[][]{
        JAVA, IO, "InputStream".toCharArray()
    };
    public static final char[][] JAVAIOREADER_TYPE_NAME = new char[][]{
        JAVA, IO, "Reader".toCharArray()
    };
    public static final char[][] JAVANETURL_TYPE_NAME = new char[][]{
        JAVA, "net".toCharArray(), "URL".toCharArray()
    };
    public static final char[][] JAVAMATHBIGDECIMAL_TYPE_NAME = new char[][]{
        JAVA, "math".toCharArray(), "BigDecimal".toCharArray()
    };
    public static final char[][] JAVASQLTIMESTAMP_TYPE_NAME = new char[][]{
        JAVA, "sql".toCharArray(), "Timestamp".toCharArray()
    };
    public static final char[][] JAVAXXMLSTREAMXMLSTREAMREADER_TYPE_NAME = new char[][]{
        "javax".toCharArray(), "xml".toCharArray(), "stream".toCharArray(), "XMLStreamReader".toCharArray()
    };
    public static final char[][] ORGW3CDOMNODE_TYPE_NAME = new char[][]{
        ORG, "w3c".toCharArray(), "dom".toCharArray(), "Node".toCharArray()
    };
    public static final char[][] XBEANSXMLINPUTSTREAM_TYPE_NAME = new char[][]{
        ORG, APACHE, XMLBEANS, "xml".toCharArray(), "stream".toCharArray(), "XMLInputStream".toCharArray()
    };
    public static final char[][] OVERRIDE_ANNOTATION_TYPE_NAME = new char[][]{
        JAVA, LANG, "Override".toCharArray()
    };
    public static final char[][] JAVAAWTCOLOR_TYPE_NAME = new char[][]{
        JAVA, AWT, "Color".toCharArray()
    };
    public static final char[][] JAVAAWT_PACKAGE_NAME = new char[][]{
        JAVA, AWT
    };
    public static final char[][] JAVAUTILMAP_TYPE_NAME = new char[][]{
        JAVA, UTIL, MAP
    };
    public static final char[][] JAVAUTILLIST_TYPE_NAME = new char[][]{
        JAVA, UTIL, "List".toCharArray()
    };
    public static final char[][] JAVAUTILHASHMAP_TYPE_NAME = new char[][]{
        JAVA, UTIL, "HashMap".toCharArray()
    };
    public static final char[][] RADIX_ENUMS_PACKAGE_NAME = new char[][]{
        ORG, RADIXWARE, KERNEL, COMMON, "enums".toCharArray()
    };
    public static final char[][] ARTE_TYPE_NAME = new char[][]{
        ORG, RADIXWARE, KERNEL, SERVER, ARTE, "Arte".toCharArray()
    };
    public static final char[][] PDC_ARTE_TYPE_NAME = new char[][]{
        ORG, RADIXWARE, ADS, "mdlPEKYFVDRVZHGZCBQQDY2NOYFOY".toCharArray(), SERVER, "pdcArte______________________".toCharArray()
    };
    public static final char[][] VALUE2SQLCONVERTER_TYPE_NAME = new char[][]{
        ORG, RADIXWARE, KERNEL, COMMON, UTILS, "ValueToSqlConverter".toCharArray()
    };
    public static final char[][] RADIX_MATHOPERATIONS_TYPE_NAME = new char[][]{
        ORG, RADIXWARE, KERNEL, COMMON, LANG, "MathOperations".toCharArray()
    };

    public static Expression createIdInvocation(Id id) {
        if (id == null) {
            return new NullLiteral(0, 0);
        }
        MessageSend createId = new MessageSend();
        createId.selector = "loadFrom".toCharArray();
        createId.arguments = new Expression[]{new StringLiteral(id.toCharArray(), 0, 0, 0)};
        createId.receiver = createQualifiedName(IDFACTORY_TYPE_NAME);
        return createId;
    }

    protected ThrowStatement createThrow(char[][] typeName, String message, boolean withCause) {
        return createThrow(typeName, new StringLiteral(message.toCharArray(), 0, 0, 0), withCause);
    }

    public static Expression createStringLiteral(String string) {
        if (string == null) {
            return new NullLiteral(0, 0);
        } else {
            return new StringLiteral(string.toCharArray(), 0, 0, 0);
        }
    }

    public static Annotation createOverrideAnnotation() {
        return new MarkerAnnotation(createQualifiedType(OVERRIDE_ANNOTATION_TYPE_NAME), 0);
    }

    public static Annotation createSuppressWarningsAnnotation(String warning) {
        SingleMemberAnnotation annotation = new SingleMemberAnnotation(new SingleTypeReference("SuppressWarnings".toCharArray(), 0), 0);
        annotation.memberValue = createStringLiteral(warning);
        return annotation;
    }

    public static ParameterizedQualifiedTypeReference createListType(TypeReference leafType, int dim) {
        return new ParameterizedQualifiedTypeReference(JAVAUTILLIST_TYPE_NAME, new TypeReference[][]{null, null, {leafType}}, dim, new long[3]);
    }

    public MethodDeclaration createMethod(String selector, boolean isOverride, TypeReference returnType, int modifiers) {
        final MethodDeclaration decl = new MethodDeclaration(compilationResult);
        decl.selector = selector.toCharArray();
        if (isOverride) {
            decl.annotations = new Annotation[]{
                createOverrideAnnotation()
            };
        }
        decl.modifiers = modifiers;
        decl.returnType = returnType == null ? new SingleTypeReference(TypeReference.VOID, 0) : returnType;
        return decl;
    }

    public MethodDeclaration createMethod(String selector, boolean isOverride) {
        return createMethod(selector, isOverride, null, ClassFileConstants.AccPublic);
    }

    public MethodDeclaration createMethod(String selector) {
        return createMethod(selector, false, null, ClassFileConstants.AccPublic);
    }

    public static ThrowStatement createThrow(char[] typeName, Expression expression, boolean withCause) {
        AllocationExpression alloc = new AllocationExpression();
        alloc.type = new SingleTypeReference(typeName, 0);
        if (withCause) {
            alloc.arguments = new Expression[]{
                expression,
                new SingleNameReference("e".toCharArray(), 0)
            };
        } else {
            if (expression != null) {
                alloc.arguments = new Expression[]{
                    expression
                };
            }
        }
        return new ThrowStatement(alloc, 0, 0);
    }

    public static ThrowStatement createThrow(char[][] typeName, Expression message, char[] causeName) {
        AllocationExpression alloc = new AllocationExpression();
        alloc.type = new QualifiedTypeReference(typeName, new long[typeName.length]);
        if (causeName != null) {
            if (message != null) {
                alloc.arguments = new Expression[]{
                    message,
                    new SingleNameReference(causeName, 0)
                };
            } else {
                alloc.arguments = new Expression[]{
                    new SingleNameReference(causeName, 0)
                };
            }
        } else {
            if (message != null) {
                alloc.arguments = new Expression[]{
                    message
                };
            }
        }
        return new ThrowStatement(alloc, 0, 0);
    }

    public static ThrowStatement createThrow(char[][] typeName, Expression expression, boolean withCause) {
        AllocationExpression alloc = new AllocationExpression();
        alloc.type = new QualifiedTypeReference(typeName, new long[typeName.length]);
        if (withCause) {
            if (expression == null) {
                alloc.arguments = new Expression[]{
                    new SingleNameReference("e".toCharArray(), 0)
                };
            } else {
                alloc.arguments = new Expression[]{
                    expression,
                    new SingleNameReference("e".toCharArray(), 0)
                };
            }
        } else {
            if (expression != null) {
                alloc.arguments = new Expression[]{
                    expression
                };
            }
        }
        return new ThrowStatement(alloc, 0, 0);
    }

    public static Block createBlock(Statement... stmts) {
        Block b = new Block(0);
        if (stmts != null) {
            for (int i = 0; i < stmts.length; i++) {
                if (stmts[i] instanceof LocalDeclaration) {
                    b.explicitDeclarations++;
                }
            }
        }
        b.statements = stmts;
        return b;
    }

    public static QualifiedTypeReference createQualifiedType(char[][] name) {
        return new QualifiedTypeReference(name, new long[name.length]);
    }

    public static QualifiedTypeReference createQualifiedType(char[][] name, TypeReference... arguments) {
        if (arguments != null && arguments.length > 0) {
            TypeReference[][] args = new TypeReference[name.length][];
            args[name.length - 1] = arguments;
            ParameterizedQualifiedTypeReference pt = new ParameterizedQualifiedTypeReference(name, args, 0, new long[name.length]);
            return pt;
        } else {
            return createQualifiedType(name);
        }
    }

    public static QualifiedNameReference createQualifiedName(char[][] name) {
        return new QualifiedNameReference(name, new long[name.length], 0, 0);
    }

    protected Expression createStringValueOf(Expression expr) {
        MessageSend ms = new MessageSend();
        ms.receiver = new SingleNameReference(TypeReference.JAVA_LANG_STRING[2], 0);
        ms.selector = "valueOf".toCharArray();
        ms.arguments = new Expression[]{
            expr
        };
        return ms;
    }

    protected Expression createStringValueOf(char[] varName) {
        return createStringValueOf(new SingleNameReference(varName, 0));
    }

    protected EqualExpression createNullCheck(char[] varName) {
        return new EqualExpression(new SingleNameReference(varName, 0), new NullLiteral(0, 0), OperatorIds.EQUAL_EQUAL);
    }

    protected EqualExpression createNullCheck(Expression nameExpression) {
        if (nameExpression instanceof SingleNameReference) {
            return new EqualExpression(new SingleNameReference(((SingleNameReference) nameExpression).token, 0), new NullLiteral(0, 0), OperatorIds.EQUAL_EQUAL);
        } else if (nameExpression instanceof ThisReference) {
            return new EqualExpression(new ThisReference(0, 0), new NullLiteral(0, 0), OperatorIds.EQUAL_EQUAL);
        } else {
            return new EqualExpression(nameExpression, new NullLiteral(0, 0), OperatorIds.EQUAL_EQUAL);
        }
    }

    protected MessageSend createEqualsCall(Expression what, Expression with) {
        MessageSend ms = new MessageSend();
        ms.receiver = what;
        ms.selector = "equals".toCharArray();
        ms.arguments = new Expression[]{
            with
        };
        return ms;
    }

    public static LocalDeclaration createDeclaration(String varName, TypeReference type, Expression init) {
        return createDeclaration(varName.toCharArray(), type, init);
    }

    public static LocalDeclaration createDeclaration(char[] varName, TypeReference type, Expression init) {
        return createDeclaration(varName, type, init, 0);
    }

    public static LocalDeclaration createDeclaration(char[] varName, TypeReference type, Expression init, int modifiers) {
        LocalDeclaration decl = new LocalDeclaration(varName, 0, 0);
        decl.type = type;
        decl.modifiers = modifiers;
        decl.initialization = init;
        return decl;
    }

    public static Assignment createAssignment(Expression lhs, Expression rhs) {
        return new Assignment(lhs, rhs, 0);
    }

    public static AllocationExpression createAlloc(TypeReference type) {
        return createAlloc(type, new Expression[0]);
    }

    public static AllocationExpression createAlloc(TypeReference type, Expression... args) {
        AllocationExpression alloc = new AllocationExpression();
        alloc.type = type;
        if (args != null && args.length > 0) {
            alloc.arguments = args;
        }
        return alloc;
    }

    public static Expression createIntConstant(int value) {
        if (value >= 0) {
            return IntLiteral.buildIntLiteral(String.valueOf(value).toCharArray(), 0, 0);
        } else {
            return new UnaryExpression(IntLiteral.buildIntLiteral(String.valueOf(-value).toCharArray(), 0, 0), OperatorIds.MINUS);
        }
    }

    public static Expression createBooleanConstant(boolean value) {
        return value ? new TrueLiteral(0, 0) : new FalseLiteral(0, 0);
    }

    protected EqualExpression createNotNullCheck(char[] varName) {
        return new EqualExpression(new SingleNameReference(varName, 0), new NullLiteral(0, 0), OperatorIds.NOT_EQUAL);
    }

    protected InstanceOfExpression createInstanceOfCheck(char[] varName, TypeReference type) {
        return new InstanceOfExpression(new SingleNameReference(varName, 0), type);
    }

    protected CastExpression createCastExpression(char[] varName, TypeReference type) {
        return new CastExpression(new SingleNameReference(varName, 0), type);
    }

    protected CastExpression createCastExpression(Expression expression, TypeReference type) {
        return new CastExpression(expression, type);
    }
    protected final CompilationResult compilationResult;
    protected final ReferenceContext referenceContext;

    public BaseGenerator(CompilationResult compilationResult, ReferenceContext referenceContext) {
        this.compilationResult = compilationResult;
        this.referenceContext = referenceContext;
    }

    protected MessageSend createSetBoolean(char[] selector, char[] varName, boolean value) {
        return createSetBoolean(selector, new SingleNameReference(varName, 0), value);
    }

    protected MessageSend createSetBoolean(char[] selector, boolean value) {
        return createSetBoolean(selector, ThisReference.implicitThis(), value);
    }

    protected MessageSend createSetBoolean(String selector, Expression recv, boolean value) {
        return createSetBoolean(selector.toCharArray(), recv, value);
    }

    protected MessageSend createSetBoolean(char[] selector, Expression recv, boolean value) {
        MessageSend setBool = new MessageSend();
        setBool.selector = selector;
        setBool.receiver = recv;
        setBool.arguments = new Expression[]{
            value ? new TrueLiteral(0, 0) : new FalseLiteral(0, 0)
        };
        return setBool;
    }

    protected MessageSend createSetLong(char[] selector, char[] varName, long value) {
        return createSetLong(selector, new SingleNameReference(varName, 0), value);
    }

    protected MessageSend createSetLong(char[] selector, long value) {
        return createSetLong(selector, ThisReference.implicitThis(), value);
    }

    protected MessageSend createSetLong(String selector, Expression recv, long value) {
        return createSetLong(selector.toCharArray(), recv, value);
    }

    protected MessageSend createSetLong(char[] selector, Expression recv, long value) {
        MessageSend setLong = new MessageSend();
        setLong.selector = selector;
        setLong.receiver = recv;
        if (value >= 0) {
            setLong.arguments = new Expression[]{
                LongLiteral.buildLongLiteral(String.valueOf(value).toCharArray(), 0, 0)
            };
        } else {
            setLong.arguments = new Expression[]{
                new UnaryExpression(LongLiteral.buildLongLiteral(String.valueOf(-value).toCharArray(), 0, 0), OperatorIds.MINUS)
            };
        }
        return setLong;
    }

    protected MessageSend createSetInt(String selector, Expression recv, int value) {
        return createSetInt(selector.toCharArray(), recv, value);
    }

    protected MessageSend createSetInt(char[] selector, Expression recv, int value) {
        MessageSend setLong = new MessageSend();
        setLong.selector = selector;
        setLong.receiver = recv;
        if (value >= 0) {
            setLong.arguments = new Expression[]{
                IntLiteral.buildIntLiteral(String.valueOf(value).toCharArray(), 0, 0)
            };
        } else {
            setLong.arguments = new Expression[]{
                new UnaryExpression(IntLiteral.buildIntLiteral(String.valueOf(-value).toCharArray(), 0, 0), OperatorIds.MINUS)
            };
        }
        return setLong;
    }

    protected void wrapWithProfilerCode(AdsProfileSupport.IProfileable profileable, Statement[] src, List<Statement> result) {
        if (src != null) {
            result.addAll(Arrays.asList(src));
        }
    }

    protected MessageSend createSetString(char[] selector, char[] varName, String value) {
        return createSetString(selector, new SingleNameReference(varName, 0), value);
    }

    protected MessageSend createSetString(char[] selector, String value) {
        return createSetString(selector, ThisReference.implicitThis(), value);
    }

    protected MessageSend createSetString(String selector, Expression recv, String value) {
        return createSetString(selector.toCharArray(), recv, value);
    }

    protected MessageSend createSetString(char[] selector, Expression recv, String value) {
        MessageSend setLong = new MessageSend();
        setLong.selector = selector;
        setLong.receiver = recv;
        setLong.arguments = new Expression[]{
            value == null ? new NullLiteral(0, 0) : new StringLiteral(value.toCharArray(), 0, 0, 0)
        };
        return setLong;
    }

    protected MessageSend createSetDouble(char[] selector, char[] varName, double value) {
        return createSetDouble(selector, new SingleNameReference(varName, 0), value);
    }

    protected MessageSend createSetDouble(char[] selector, double value) {
        return createSetDouble(selector, ThisReference.implicitThis(), value);
    }

    protected MessageSend createSetDouble(String selector, Expression recv, double value) {
        return createSetDouble(selector.toCharArray(), recv, value);
    }

    protected MessageSend createSetDouble(char[] selector, Expression recv, double value) {
        MessageSend setDouble = new MessageSend();
        setDouble.selector = selector;
        setDouble.receiver = recv;
        setDouble.arguments = new Expression[]{
            new DoubleLiteral(String.valueOf(value).toCharArray(), 0, 0)
        };
        return setDouble;
    }

    protected MessageSend createSetColor(char[] selector, char[] varName, Color value) {
        return createSetColor(selector, new SingleNameReference(varName, 0), value);
    }

    protected MessageSend createSetColor(char[] selector, Color value) {
        return createSetColor(selector, ThisReference.implicitThis(), value);
    }

    protected MessageSend createSetColor(char[] selector, Expression recv, Color value) {
        MessageSend setColor = new MessageSend();
        setColor.selector = selector;
        setColor.receiver = recv;

        Expression color;
        if (value == null) {
            color = new NullLiteral(0, 0);
        } else {
            AllocationExpression alloc = new AllocationExpression();
            alloc.type = createQualifiedType(JAVAAWTCOLOR_TYPE_NAME);
            alloc.arguments = new Expression[]{
                IntLiteral.buildIntLiteral(String.valueOf(value.getRed()).toCharArray(), 0, 0),
                IntLiteral.buildIntLiteral(String.valueOf(value.getGreen()).toCharArray(), 0, 0),
                IntLiteral.buildIntLiteral(String.valueOf(value.getBlue()).toCharArray(), 0, 0)
            };
            color = alloc;
        }
        setColor.arguments = new Expression[]{
            color
        };
        return setColor;
    }

    public static MessageSend createSetValue(String selector, Expression recv, Expression value) {
        return createSetValue(selector.toCharArray(), recv, value);
    }

    public static MessageSend createSetValue(String selector, Expression value) {
        return createSetValue(selector.toCharArray(), ThisReference.implicitThis(), value);
    }

    public static MessageSend createSetValue(char[] selector, Expression recv, Expression value) {
        MessageSend setColor = new MessageSend();
        setColor.selector = selector;
        setColor.receiver = recv;
        setColor.arguments = new Expression[]{
            value
        };
        return setColor;
    }

    public static MessageSend createSetId(String selector, Expression recv, Id id) {
        return createSetValue(selector.toCharArray(), recv, createIdInvocation(id));
    }

    public static MessageSend createSetEnumValue(String selector, Expression recv, Enum value) {
        return createSetEnumValue(selector.toCharArray(), recv, value);
    }

    public static MessageSend createSetEnumValue(char[] selector, Expression recv, Enum value) {
        MessageSend setColor = new MessageSend();
        setColor.selector = selector;
        setColor.receiver = recv;
        setColor.arguments = new Expression[]{
            createEnumFieldRef(value)
        };
        return setColor;
    }

    public static MessageSend createMessageSend(Expression recv, String selector, Expression... args) {
        return createMessageSend(recv, selector.toCharArray(), args);
    }

    public static MessageSend createMessageSend(Expression recv, char[] selector, Expression... args) {
        MessageSend ms = new JMLMessageSend();
        ms.receiver = recv;
        ms.selector = selector;
        if (args != null && args.length > 0) {
            ms.arguments = args;
        }
        return ms;
    }

    public static MessageSend createMessageSend(Expression recv, String selector) {
        return createMessageSend(recv, selector.toCharArray());
    }

    public static MessageSend createMessageSend(Expression recv, char[] selector) {
        return createMessageSend(recv, selector, new Expression[0]);
    }

    protected static String toLower(final String s) {
        return String.valueOf(Character.toLowerCase(s.charAt(0))) + s.substring(1);
    }

    public static Expression createEnumFieldRef(Enum e) {
        FieldReference ref = new FieldReference(e.name().toCharArray(), 0);
        String[] names = e.getClass().getName().split("\\.");
        char[][] namesChar = new char[names.length][];
        for (int i = 0; i < namesChar.length; i++) {
            namesChar[i] = names[i].toCharArray();
        }
        ref.receiver = createQualifiedName(namesChar);
        final String s = e.getClass().getSimpleName() + "." + e.name();
        return ref;
    }

    public static Statement createArtePtrDeclaration(AdsDefinition referenceContext, String arteVarName) {
        LocalDeclaration decl = new LocalDeclaration(arteVarName.toCharArray(), 0, 0);
        decl.type = createQualifiedType(ARTE_TYPE_NAME);
        decl.initialization = createArteAccessMethodInvocation(referenceContext);
        return decl;
    }

    public static MessageSend createArteAccessMethodInvocation(AdsDefinition referenceContext) {
        final TaggedSingleNameReference ref = new TaggedSingleNameReference(referenceContext, AdsTypeDeclaration.Factory.newInstance(EValType.USER_CLASS, new Id[]{Id.Factory.loadFrom("pdcArte______________________")}));
        return createMessageSend(ref, "__getArteInstanceInternal");
    }

    public static AbstractMethodDeclaration createMethodDeclaration(char[] selector, TypeReference resultType, int modifiers, Argument[] arguments, CompilationResult compilationResult) {
        final MethodDeclaration method = new MethodDeclaration(compilationResult);
        method.selector = selector;
        method.modifiers = modifiers;
        method.returnType = resultType;
        method.arguments = arguments;
        return method;
    }

    public static AbstractMethodDeclaration createMethodDeclaration(String selector, TypeReference resultType, int modifiers, CompilationResult compilationResult) {
        return createMethodDeclaration(selector.toCharArray(), resultType, modifiers, null, compilationResult);
    }

    public static AbstractMethodDeclaration createMethodDeclaration(String selector, int modifiers, CompilationResult compilationResult) {
        return createMethodDeclaration(selector.toCharArray(), TypeReference.baseTypeReference(TypeIds.T_void, 0), modifiers, null, compilationResult);
    }
    public static AbstractMethodDeclaration createMethodDeclaration(String selector, int modifiers,Argument[] arguments,  CompilationResult compilationResult) {
        return createMethodDeclaration(selector.toCharArray(), TypeReference.baseTypeReference(TypeIds.T_void, 0), modifiers, arguments, compilationResult);
    }

    public static AbstractMethodDeclaration createMethodDeclaration(char[] selector, TypeReference resultType, int modifiers, CompilationResult compilationResult) {
        return createMethodDeclaration(selector, resultType, modifiers, null, compilationResult);
    }

    public static AbstractMethodDeclaration createMethodDeclaration(String selector, TypeReference resultType, int modifiers, Argument[] arguments, CompilationResult compilationResult) {
        return createMethodDeclaration(selector.toCharArray(), resultType, modifiers, arguments, compilationResult);
    }
}