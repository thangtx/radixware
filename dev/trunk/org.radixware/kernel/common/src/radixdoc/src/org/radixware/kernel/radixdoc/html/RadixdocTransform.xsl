<?xml version="1.0" encoding="UTF-8"?>

<!--
    Document   : RadixdocTransform.xsl
    Created on : April 19, 2013, 5:08 PM
    Author     : mgukov
    Description:
        Purpose of transformation radixdoc to html.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
                xmlns:doc="http://schemas.radixware.org/radixdoc.xsd"
                xmlns:java="http://xml.apache.org/xalan/java"
                exclude-result-prefixes="java">

    <xsl:output method="html"/>

    <xsl:param name="generator"/>
    <xsl:param name="lang"/>
    <xsl:param name="rootPath"/>
    <xsl:param name="outPath"/>

    <xsl:strip-space elements="doc:Block doc:Resource doc:Resources doc:Page"/>
    <xsl:preserve-space elements="doc:Text"/>

    <xsl:template match="/doc:Page">
        
        <xsl:for-each select="doc:ResourceItem">
            <xsl:call-template name="copyFile">
                <xsl:with-param name="outFile" select="concat($outPath, concat('/resources/', @Key))"/>
                <xsl:with-param name="sourceFile" select="@Path"/>
                <xsl:with-param name="source" select="'internal'"/>
            </xsl:call-template>
        </xsl:for-each>
        
        <xsl:call-template name="doctype"/>
        <html>
            <head>
                <title>
                    <xsl:value-of select="/doc:Page/@Title"/>
                </title>
                <link rel="stylesheet" type="text/css" href="../../../styles.css" title="Style"/>
                <script type="text/javascript" src="../../../jquery.js"></script>
                <script type="text/javascript" src="../../../radixdoc.js"></script>
                <script type="text/javascript">
                    radixdoc.initCollapsible();
                </script>
            </head>
            <body>
                <div class='pageHead'>
                    <xsl:call-template name="tableContent"/>
                </div>
                <div class='contentContainer'>
                    <ul class='main-chapters'>
                        <xsl:apply-templates/>
                    </ul>
                </div>
                <div class='pageFoot'></div>
            </body>
        </html>
    </xsl:template>

    <xsl:template match="/doc:Page/doc:Block">
        <li>
            <xsl:call-template name="anchor"/>
            <xsl:attribute name="class">
                <xsl:text>main-chapter </xsl:text>
                <xsl:value-of select="@Style"/>
            </xsl:attribute>
            <xsl:apply-templates/>
        </li>
    </xsl:template>
    
    <xsl:template match="doc:Block">
        <div>
            <xsl:call-template name="anchor"/>
            <xsl:attribute name="class">
                <xsl:value-of select="@Style"/>
            </xsl:attribute>
            <xsl:apply-templates/>
        </div>
    </xsl:template>

    <xsl:template match="doc:Block[@Meta = 'collapsible']">
        <div class="collapsible">
            <xsl:call-template name="anchor"/>
            <div class="title">
                <div class="action">
                    <a href="javascript:void(0)">▶</a>
                </div>
                <xsl:apply-templates select="*[@Meta = 'title']"/>
            </div>
            <div class="content">
                <xsl:apply-templates select="*[@Meta = 'content']"/>
            </div>
        </div>
    </xsl:template>

    <xsl:template match="doc:Resource">
        <img>
            <xsl:attribute name="src">
                <xsl:text>resources/</xsl:text>
                <xsl:value-of select="@Source"/>
            </xsl:attribute>
        </img>
    </xsl:template>

    <xsl:template match="doc:Text">
        <xsl:variable name="text" select="text()"/>
        <xsl:choose>
            <xsl:when test="not(@Meta) and not(@Style)">
                <xsl:call-template name="printText">
                    <xsl:with-param name="escape" select="@Escape"/>
                    <xsl:with-param name="text" select="$text"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="@Meta = 'paragraph'">
                <p>
                    <xsl:call-template name="printText">
                        <xsl:with-param name="escape" select="@Escape"/>
                        <xsl:with-param name="text" select="$text"/>
                    </xsl:call-template>
                </p>
            </xsl:when>
            <xsl:when test="@Meta = 'code'">
                <code>
                    <xsl:call-template name="printText">
                        <xsl:with-param name="escape" select="@Escape"/>
                        <xsl:with-param name="text" select="$text"/>
                    </xsl:call-template>
                </code>
            </xsl:when>
            <xsl:when test="@Meta = 'mlsid'">
                <div>
                    <xsl:attribute name="class">
                        <xsl:text>text</xsl:text>
                    </xsl:attribute>
                    <xsl:call-template name="printText">
                        <xsl:with-param name="escape" select="'false'"/>
                        <xsl:with-param name="text"
                                        select="java:org.radixware.kernel.radixdoc.html.TransformLib.loadString($rootPath, $text, $generator, $lang)"/>
                    </xsl:call-template>
                </div>
            </xsl:when>

            <xsl:when test="@Meta = 'localized'">
                <xsl:call-template name="printText">
                    <xsl:with-param name="escape" select="@Escape"/>
                    <xsl:with-param name="text" select="$text"/>
                </xsl:call-template>
            </xsl:when>

            <xsl:otherwise>
                <div>
                    <xsl:attribute name="class">
                        <xsl:value-of select="@Meta"/>
                        <xsl:if test="string-length(@Style) > 0">
                            <xsl:text> </xsl:text>
                            <xsl:value-of select="@Style"/>
                        </xsl:if>
                    </xsl:attribute>
                    <xsl:call-template name="printText">
                        <xsl:with-param name="escape" select="@Escape"/>
                        <xsl:with-param name="text" select="$text"/>
                    </xsl:call-template>
                </div>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="doc:List">
        <ul>
            <xsl:if test="string-length(@Style) > 0">
                <xsl:attribute name="class">
                    <xsl:value-of select="@Style"/>
                </xsl:attribute>
            </xsl:if>
            <xsl:apply-templates/>
        </ul>
    </xsl:template>

    <xsl:template match="doc:List[@Meta = 'hierarchy']">
        <ul>
            <xsl:attribute name="class">
                <xsl:value-of select="@Meta"/>
            </xsl:attribute>
            <xsl:for-each select="doc:Item">
                <li>
                    <xsl:attribute name="class">
                        <xsl:text>hierarchyItem</xsl:text>
                    </xsl:attribute>
                    <xsl:attribute name="style">
                        <xsl:text>margin-left:</xsl:text>
                        <xsl:value-of select="(position() - 1) * 2"/>
                        <xsl:text>em;</xsl:text>
                    </xsl:attribute>
                    <xsl:if test="position() > 1">
                        <xsl:text>↳</xsl:text>
                    </xsl:if>
                    <xsl:apply-templates/>
                </li>
            </xsl:for-each>
        </ul>
    </xsl:template>

    <xsl:template match="doc:List[@Meta = 'single-line']">
        <div class="single-line {@Style}">
            <xsl:for-each select="doc:Item">
                <xsl:if test="position() != 1">
                    <xsl:text>, </xsl:text>
                </xsl:if>
                <xsl:apply-templates/>
            </xsl:for-each>
        </div>
    </xsl:template>

    <xsl:template match="doc:Item">
        <li>
            <xsl:if test="string-length(@Style) > 0">
                <xsl:attribute name="class">
                    <xsl:value-of select="@Style"/>
                </xsl:attribute>
            </xsl:if>
            <xsl:apply-templates/>
        </li>
    </xsl:template>

    <xsl:template match="doc:Ref">
        <xsl:variable name="path"
                      select="java:org.radixware.kernel.radixdoc.html.TransformLib.parsePath(@Path, $lang, $generator)"/>
        <xsl:choose>
            <xsl:when test="string-length($path) > 0">
                <a>
                    <xsl:if test="string-length(@Style) > 0">
                        <xsl:attribute name="class">
                            <xsl:value-of select="@Style"/>
                        </xsl:attribute>
                    </xsl:if>

                    <xsl:attribute name="href">
                        <xsl:value-of select="$path"/>
                    </xsl:attribute>

                    <xsl:if test="@Title">
                        <xsl:attribute name="title">
                            <xsl:value-of select="@Title"/>
                        </xsl:attribute>
                    </xsl:if>

                    <xsl:apply-templates/>
                </a>
            </xsl:when>
            <xsl:otherwise>
                <xsl:apply-templates/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="doc:Ref[not(@Path) or string-length(@Path) = 0]">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="doc:Table">
        <table>
            <xsl:if test="string-length(@Style) > 0">
                <xsl:attribute name="class">
                    <xsl:value-of select="@Style"/>
                </xsl:attribute>
            </xsl:if>
            <thead>
                <xsl:apply-templates select="doc:Row[@Meta = 'head']"/>
            </thead>
            <tbody>
                <xsl:apply-templates select="doc:Row[not(@Meta) or @Meta != 'head']"/>
            </tbody>
        </table>
    </xsl:template>

    <xsl:template match="doc:Row">
        <tr>
            <xsl:if test="string-length(@Style) > 0">
                <xsl:attribute name="class">
                    <xsl:value-of select="@Style"/>
                </xsl:attribute>
            </xsl:if>
            <xsl:apply-templates/>
        </tr>
    </xsl:template>

    <xsl:template match="doc:Cell">
        <td>
            <xsl:if test="@Colspan">
                <xsl:attribute name="colspan">
                    <xsl:value-of select="@Colspan"/>
                </xsl:attribute>
            </xsl:if>
            <xsl:if test="@Rowspan">
                <xsl:attribute name="rowspan">
                    <xsl:value-of select="@Rowspan"/>
                </xsl:attribute>
            </xsl:if>
            <xsl:if test="string-length(@Style) > 0">
                <xsl:attribute name="class">
                    <xsl:value-of select="@Style"/>
                </xsl:attribute>
            </xsl:if>
            <xsl:apply-templates/>
        </td>
    </xsl:template>

    <xsl:template match="doc:Column">
        <td>
            <xsl:if test="string-length(@Style) > 0">
                <xsl:attribute name="class">
                    <xsl:value-of select="@Style"/>
                </xsl:attribute>
            </xsl:if>
            <xsl:apply-templates/>
        </td>
    </xsl:template>

    <xsl:template match="doc:Attribute">
        <xsl:variable name="attrName" select="@Name"/>
        <xsl:attribute name="{$attrName}">
            <xsl:value-of select="node()"/>
        </xsl:attribute>
    </xsl:template>

    <xsl:template name="printText">
        <xsl:param name="escape"/>
        <xsl:param name="text"/>
        <xsl:choose>
            <xsl:when test="$escape = 'false'">
                <xsl:value-of disable-output-escaping="yes" select="$text"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$text"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template name="tableContent">
        <xsl:if test="count(*//doc:Block/doc:Attribute[@Name = 'table-content-item']) > 0">
            <ul class="tableContent">
                <xsl:for-each select="*//doc:Block/doc:Attribute[@Name = 'table-content-item']">
                    <li>
                        <a>
                            <xsl:attribute name="href">
                                <xsl:text>#</xsl:text>
                                <xsl:value-of select="text()"/>
                            </xsl:attribute>
                            <xsl:value-of select="text()"/>
                            <!--<xsl:value-of select="parent::*/*[@Meta = 'title']/text()"/>-->
                        </a>
                    </li>
                </xsl:for-each>
            </ul>
        </xsl:if>
    </xsl:template>

    <xsl:template name="anchor">
        <xsl:if test="doc:Attribute[@Name = 'table-content-item']">
            <a>
                <xsl:attribute name="name">
                    <xsl:value-of select="doc:Attribute[@Name]/text()"/>
                </xsl:attribute>
            </a>
        </xsl:if>
    </xsl:template>

    <xsl:template match="doc:Attribute[@Name = 'anchor']">
        <a>
            <xsl:attribute name="name">
                <xsl:value-of select="text()"/>
            </xsl:attribute>
        </a>
    </xsl:template>

    <xsl:template match="/doc:IndexUnit">
        <xsl:call-template name="doctype"/>
        <html>
            <head>
                <title>
                    <xsl:text>IndexPage</xsl:text>
                </title>
                <link rel="stylesheet" type="text/css" href="styles.css" title="Style"/>
                <script type="text/javascript" src="jquery.js"></script>
                <script type="text/javascript" src="radixdoc.js"></script>
                <script type="text/javascript">
                    <xsl:text>var units = [ </xsl:text>
                    <xsl:for-each select="doc:SubUnit">
                        <xsl:if test="position() > 1">
                            <xsl:text>,&#xa;</xsl:text>
                        </xsl:if>
                        <xsl:call-template name="createIndexUnit"/>
                    </xsl:for-each>
                    <xsl:text>];</xsl:text>
                    <xsl:text>
                        radixdoc.createIndex();
                        radixdoc.initCollapsible();
                    </xsl:text>
                </script>
            </head>
            <body>
                <div class="indexPage">
                    <div class="contentTable">
                        <div class="units"></div>
                        <div class="insert"></div>
                        <div class="definitions"></div>
                    </div>
                    <div class="documentation">
                        <iframe id="documentationFrame" src=""></iframe>
                    </div>
                </div>
            </body>
        </html>

        <xsl:call-template name="copyFile">
            <xsl:with-param name="outFile" select="'styles.css'"/>
            <xsl:with-param name="sourceFile"
                            select="'styles.css'"/>
            <xsl:with-param name="source" select="'internal'"/>
        </xsl:call-template>

        <xsl:call-template name="copyFile">
            <xsl:with-param name="outFile" select="'jquery.js'"/>
            <xsl:with-param name="sourceFile"
                            select="'jquery.js'"/>
            <xsl:with-param name="source" select="'internal'"/>
        </xsl:call-template>

        <xsl:call-template name="copyFile">
            <xsl:with-param name="outFile" select="'radixdoc.js'"/>
            <xsl:with-param name="sourceFile"
                            select="'radixdoc.js'"/>
            <xsl:with-param name="source" select="'internal'"/>
        </xsl:call-template>
        
        <xsl:call-template name="copyFile">
            <xsl:with-param name="outFile" select="'lodash.js'"/>
            <xsl:with-param name="sourceFile"
                            select="'lodash.js'"/>
            <xsl:with-param name="source" select="'internal'"/>
        </xsl:call-template>       
        
        <xsl:call-template name="copyFile">
            <xsl:with-param name="outFile" select="'backbone.js'"/>
            <xsl:with-param name="sourceFile"
                            select="'backbone.js'"/>
            <xsl:with-param name="source" select="'internal'"/>
        </xsl:call-template>  
        
        <xsl:call-template name="copyFile">
            <xsl:with-param name="outFile" select="'joint.js'"/>
            <xsl:with-param name="sourceFile"
                            select="'joint.js'"/>
            <xsl:with-param name="source" select="'internal'"/>
        </xsl:call-template>                     
        
        <xsl:call-template name="copyFile">
            <xsl:with-param name="outFile" select="'joint-db-diagrams.js'"/>
            <xsl:with-param name="sourceFile"
                            select="'joint-db-diagrams.js'"/>
            <xsl:with-param name="source" select="'internal'"/>
        </xsl:call-template>
        
        <xsl:call-template name="copyFile">
            <xsl:with-param name="outFile" select="'joint.css'"/>
            <xsl:with-param name="sourceFile"
                            select="'joint.css'"/>
            <xsl:with-param name="source" select="'internal'"/>
        </xsl:call-template>
        
        <xsl:call-template name="copyFile">
            <xsl:with-param name="outFile" select="'joint-db-diagrams.css'"/>
            <xsl:with-param name="sourceFile"
                            select="'joint-db-diagrams.css'"/>
            <xsl:with-param name="source" select="'internal'"/>
        </xsl:call-template>
    </xsl:template>

    <xsl:template name="createIndexUnit">
        <xsl:text>{ </xsl:text>
        <xsl:if test="@Reference">
            <xsl:text>ref: "</xsl:text>
            <xsl:value-of select="@Reference"/>
            <xsl:text>", </xsl:text>
        </xsl:if>

        <xsl:if test="@SubPath">
            <xsl:text>subPath: "</xsl:text>
            <xsl:value-of select="@SubPath"/>
            <xsl:text>", </xsl:text>
        </xsl:if>

        <xsl:text>title: "</xsl:text>
        <xsl:value-of select="@Title"/>
        <xsl:text>", </xsl:text>

        <xsl:text>icon: "</xsl:text>
        <xsl:value-of select="@Icon"/>
        <xsl:text>"</xsl:text>

        <xsl:if test="count(doc:SubUnit) > 0">
            <xsl:text>, children: [</xsl:text>
            <xsl:for-each select="doc:SubUnit">
                <xsl:sort select="@Title"/>
                <xsl:if test="position() > 1">
                    <xsl:text>,&#xa;</xsl:text>
                </xsl:if>
                <xsl:call-template name="createIndexUnit"/>
            </xsl:for-each>
            <xsl:text>] </xsl:text>
        </xsl:if>
        <xsl:text>}</xsl:text>
    </xsl:template>

    <xsl:template name="copyFile">
        <xsl:param name="source"/>
        <xsl:param name="sourceFile"/>
        <xsl:param name="outFile"/>

        <xsl:variable name="scripts" select="java:org.radixware.kernel.radixdoc.html.TransformLib.addFile(
                                      $source, $sourceFile, $outFile, $generator, $lang)"/>
    </xsl:template>

    <xsl:template name="doctype">
        <xsl:text disable-output-escaping="yes">&lt;!DOCTYPE html&gt;&#xa;</xsl:text>
    </xsl:template>
</xsl:stylesheet>
