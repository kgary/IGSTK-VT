<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"  xmlns:xsl="http://www.w3.org/1999/XSL/Transform" >

 <xsl:output method="text" encoding="UTF-8" /> 
 
	<xsl:param name="className" />
	<xsl:param name="namespace" />
	<xsl:param name="stateName" />
	
	<xsl:template xmlns:util="java:test.Utility" match="/">
/* This file was generated. */
#include "<xsl:value-of select="$className" />State.h";

namespace <xsl:value-of
             select="$namespace"/> 
{

		
       <xsl:value-of select="$stateName" /> : public <xsl:value-of select="$className" />State { 
        
        public: 
        		static <xsl:value-of select="$className" />* Instance();
        		<xsl:for-each select="scxml/state">
          		<xsl:value-of select="@id"/>( <xsl:value-of select="$className" />* ); 
          		</xsl:for-each>
        } 
        
		
        
 
}// end namespace igstk
</xsl:template>
		
</xsl:stylesheet>