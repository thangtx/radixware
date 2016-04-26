#!/usr/bin/python
# -*- coding: utf-8 -*-
"""
tsMergeTool is a utility to merge translations from Radix.Explorer and Radix.Web 
projects of Radixware platform.
AUTHOR: Andrey Atapin <mailto:a.atapin@compassplus.com>
COPYRIGHT: 2012 (c) Compass Plus, LLC <http://www.compassplus.com>.
"""

import xml.sax, sys, os, argparse, collections
from copy import deepcopy

#--------------------------------------------------------------------------------------------
CONTEXT = "context"
MESSAGE = "message"
CTX_NAME = "name"
LOCATION = "location"
SOURCE = "source"
TRANSLATION = "translation"
#--------------------------------------------------------------------------------------------
## tested
class Message:
    """
    Class is used to describe a single message entry in a translation file
    """    
    
    def __init__(self, source='', translation='', finished=False):
        self.source = source
        self.translation = translation
        self.locations = []
        self.isFinished = False
    
    def __eq__(self, other):
        return self.source == other.source \
            and self.translation == other.translation \
            and self.locations == other.locations \
            and self.isFinished == other.isFinished
        
    def __locations2xml(self):
        output = u''
        for l in self.locations:
            output += u'\t\t<location filename="{0[0]}" line="{0[1]}"/>\n'.format(\
                l.split(':'))
        return output
    
    def __tran2xml(self):
        if self.isFinished:
            return u'\t\t<translation>{}</translation>'.format(\
                self.translation)
        else:
            return u'\t\t<translation type="unfinished">{}</translation>'.format(\
                self.translation)#.decode('utf-8'))
    
    def addlocation(self, loc):
        self.locations.append(loc)

    def getlocations(self):
        return self.locations

    def display(self, includeLocations=True):
        if includeLocations:
            return u"\t<message>\n\t\t<source>{0}</source>\n{1}\n{2}\t</message>".format(\
                self.source,\
                self.__tran2xml(),\
                self.__locations2xml())
        else:
            return u"\t<message>\n\t\t<source>{0}</source>\n{1}\n\t</message>".format(\
                self.source,\
                self.__tran2xml())
        
    def setfinished(self):
        self.isFinished = 1^self.isFinished
        
    def copy(self):
        """ 
        Java-style object copying
        """    
        msg = Message()
        msg.source = deepcopy(self.source)
        msg.translation = deepcopy(self.translation)
        msg.locations = deepcopy(self.locations)
        msg.isFinished = self.isFinished
        return msg
#--------------------------------------------------------------------------------------------
## tested
class Context:
    """
    Class is used to describe a context, which is a set of messages with a predefined sence
    """    

    name = ""
    messages = {}  
    def __init__(self, name=''):
        self.name = name
        self.messages = {}

    def addmessage(self, message):
        if self.messages.has_key(message.source):
            pass
            #raise Exception("Duplicated translation occured", message.source, message.translation, self.name)
        else:
            self.messages[message.source] = message.copy()

    def merge(self, ctx):
        if self.name == ctx.name:
            for m in ctx.messages:
                self.addmessage(ctx.messages[m].copy())
        else:
            raise Exception("Can't merge different contexts '" + self.name + "' and '" + ctx.name + "'")

    def display(self, includeLocations=True):
        output = u"<context>\n\t<name>{0}</name>\n".format(self.name)
        for m in self.messages:
            output += self.messages[m].display(includeLocations) + u'\n'
        output += u"</context>"    
        return output
#--------------------------------------------------------------------------------------------
## tested
class SaxParser(xml.sax.handler.ContentHandler):
    show_locs = True
    start_context = False
    start_message = False
    start_ctx_name = False
    start_source = False
    start_tr = False
    start_location = False

    contexts = {}
    last_ctx = None
    last_msg = None
    last_attrs = None
    last_characters = ''

    xml_pattern = u'<?xml version="1.0" encoding="utf-8"?> \
\n<!DOCTYPE TS> \
\n<TS version="2.0" language="ru_RU"> \
\n{0} \
\n</TS>'

    def __init__(self, includeLocations=True):
        xml.sax.ContentHandler.__init__(self)
        self.ignorableWhitespace(True)
        self.contexts = {}
        self.show_locs = includeLocations
    
    def __escape(self, string):
        import cgi
        return cgi.escape(string).replace("'", "&apos;").replace('"', "&quot;")
    
    def addcontext(self, name, context):
        if self.contexts.has_key(name):
            # if such context is already here we need to merge two contexts
            self.contexts[name].merge(context)
        else:
            self.contexts[name]=context

    def out(self, includeLocations=True):
        output = u''
        sorted_ctx = collections.OrderedDict(sorted(self.contexts.items()))
        for c in sorted_ctx:
            output += sorted_ctx[c].display(includeLocations)
        return self.xml_pattern.format(output).encode('utf-8')

    def characters(self, content):
        if self.start_ctx_name or self.start_source or self.start_tr:
            self.last_characters += self.__escape(content)

    def startElement(self, name, attrs):
        if name == CONTEXT and not self.start_message and not self.start_ctx_name:
            self.start_context = True
            self.last_ctx = Context()
        elif name == MESSAGE and self.start_context:
            self.start_message = True
            self.last_msg = Message()
        elif name == CTX_NAME:
            if self.start_context == True:
                self.start_ctx_name = True
                        
        if self.start_message:
            if name == SOURCE:
                self.start_source = True
            elif name == TRANSLATION:
                self.start_tr = True 
                self.last_attrs = attrs.copy()  
            elif name == LOCATION:
                self.start_location = True
                self.last_attrs = attrs.copy()
            
    def endElement(self, name):
        if name == CTX_NAME:
            self.last_ctx.name = self.last_characters
            self.start_ctx_name = False
            self.last_characters = ''
        elif name == MESSAGE:
            self.start_message = False
            self.last_ctx.addmessage(self.last_msg)#.source, self.last_msg.translation)
        elif name == CONTEXT:
            self.start_context = False
            self.addcontext(self.last_ctx.name, self.last_ctx)
            del self.last_ctx
            
        elif name == SOURCE:
            self.last_msg.source = self.last_characters
            self.last_characters = ''
            self.start_source = False
        elif name == TRANSLATION:
            self.last_msg.translation = self.last_characters
            if self.last_attrs.get('type') == None: 
                self.last_msg.isFinished = True
            self.last_characters = ''
            self.start_tr = False
        elif name == LOCATION:
            self.start_location = False
            self.last_msg.addlocation(u"{0}:{1}".format(self.last_attrs.get("filename"), self.last_attrs.get("line")))
        
#--------------------------------------------------------------------------------------------

def main():
    print "Merging TS files..."    
    #------ Parsing command line attributes -------#
    argparser = argparse.ArgumentParser()
    argparser.add_argument("-e", action='store_false', default=True, help='Exclude locations')
    argparser.add_argument("-o",  type=argparse.FileType('wb'), default=sys.stdout, help='Out file. STDOUT is default')
    argparser.add_argument("TSFILE", nargs=2, help='Translation files to merge')
    args = vars(argparser.parse_args(sys.argv[1:]))
        
    collector, mergee = args['TSFILE'][0], args['TSFILE'][1]
    includeLocations = args['e']
    output = args['o']
    #----- Working -----#    
    try:
        print "Opening %s..." % collector
        cc = open(collector, 'rb')
        print "Opening %s..." % mergee
        mm = open(mergee, 'rb')
        parser = SaxParser(includeLocations)
        print "Reading %s..." % collector
        xml.sax.parse(cc, parser)
        print "Reading %s..." % mergee
        xml.sax.parse(mm, parser)
        print "Saving..."
        output.write(parser.out(includeLocations))
    except IOError as ioe:
        print "= IOError ({0}): {1}".format(ioe.errno, ioe.strerror)
    finally:
        print "Closing input files..."
        if cc : cc.close()
        if mm : mm.close()
    output.close()
    print "Translations merged successfully!"
if __name__ == "__main__":
    main()
