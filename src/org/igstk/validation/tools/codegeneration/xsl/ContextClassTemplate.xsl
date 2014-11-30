<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"  xmlns:xsl="http://www.w3.org/1999/XSL/Transform" >

 <xsl:output method="text" encoding="UTF-8" /> 
 
	<xsl:param name="className" />
	<xsl:param name="namespace" />
	<xsl:param name="methods" />


	<xsl:template name="capitalizeString">
  	<xsl:param name="str"/>
  		<xsl:value-of select="translate(substring($str,1,1)
                 ,'abcdefghijklmnopqrstuvwxyz'
                 ,'ABCDEFGHIJKLMNOPQRSTUVWXYZ')"/>
  		<xsl:value-of select="substring($str,2)"/>
	</xsl:template>
	
	<xsl:template name="loopThroughList">
    <xsl:param name="input"/>
    <xsl:if test="string-length($input) &gt; 0">
      <xsl:variable name="v" select="substring-before($input, ',')"/>
      	void <xsl:value-of select="$className" />Context::<xsl:value-of select="$v"/>(){
      		_state-><xsl:value-of select="$v"/>(*this);
      	}
      <xsl:call-template name="loopThroughList">
        <xsl:with-param name="input" select="substring-after($input, ',')"/>
      </xsl:call-template>
    </xsl:if> 
</xsl:template>
	
	<xsl:template match="/">
/* This file was generated. */
#include "<xsl:value-of select="$className" />Context.h"

namespace <xsl:value-of
             select="$namespace"/> 
{
    	<xsl:value-of select="$className" />Context::<xsl:value-of select="$className"></xsl:value-of>(){
          <xsl:for-each select="scxml">
        	_state = <xsl:value-of select="@initialstate" />::the<xsl:value-of select="@initialstate" />();
          </xsl:for-each>
        }
        
         <xsl:call-template name="loopThroughList">
    		<xsl:with-param name="input" select="$methods"/> 
		 </xsl:call-template>
        
        void <xsl:value-of select="$className" />Context::transition (<xsl:value-of select="$className" />State* newState){
        	_state = newState;
        }

		
}// end namespace igstk
</xsl:template>
	
</xsl:stylesheet>