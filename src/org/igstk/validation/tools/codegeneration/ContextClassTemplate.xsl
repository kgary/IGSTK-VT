<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"  xmlns:xsl="http://www.w3.org/1999/XSL/Transform" >

 <xsl:output method="text" encoding="UTF-8" /> 
 
	<xsl:param name="className" />
	<xsl:param name="namespace" />


	<xsl:template name="capitalizeString">
  	<xsl:param name="str"/>
  		<xsl:value-of select="translate(substring($str,1,1)
                 ,'abcdefghijklmnopqrstuvwxyz'
                 ,'ABCDEFGHIJKLMNOPQRSTUVWXYZ')"/>
  		<xsl:value-of select="substring($str,2)"/>
	</xsl:template>
	
	<xsl:template xmlns:util="java:test.Utility" match="/">
/* This file was generated. */
#include "<xsl:value-of select="$className" />.h"

namespace <xsl:value-of
             select="$namespace"/> 
{
    	<xsl:value-of select="$className" />::<xsl:value-of select="$className"></xsl:value-of>(){
          <xsl:for-each select="scxml">
        	_state = <xsl:value-of select="@initialstate" />::Instance();
          </xsl:for-each>
        }
        
        void <xsl:value-of select="$className" />::ChangeState (<xsl:value-of select="$className" />State* s){
        	_state = s;
        }

		<xsl:for-each select="scxml/state">
		void <xsl:value-of select="$className" />::<xsl:value-of select="@id"/>() {
			_state-><xsl:value-of select="@id"/>(this);
		} 
        </xsl:for-each>
		
}// end namespace igstk
	</xsl:template>
	
</xsl:stylesheet>