<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"  xmlns:xsl="http://www.w3.org/1999/XSL/Transform" >

 <xsl:output method="text" encoding="UTF-8" /> 
 
	<xsl:param name="className" />
	<xsl:param name="namespace" />
	<xsl:param name="stateName" />
<!-- Removes a word from a string. Used to replace the Input in the method names -->	
<xsl:template name="removeWordFromString">
    <xsl:param name="word" />
    <xsl:param name="text" />
    <xsl:choose>
        <xsl:when test="contains($text, $word)">
            <xsl:value-of select="substring-before($text, $word)" />
            <xsl:call-template name="removeWordFromString">
                <xsl:with-param name="word" select="$word" />
                <xsl:with-param name="text" select="substring-after($text, $word)" />
            </xsl:call-template>
        </xsl:when>
        <xsl:otherwise>
            <xsl:value-of select="$text" />
        </xsl:otherwise>
    </xsl:choose>
</xsl:template>


	<xsl:template match="/">
/* This file was generated. */
#include "<xsl:value-of select="$namespace" /><xsl:value-of select="$className" />State.h";
<xsl:if test="contains($stateName, 'Attempting')">#include "<xsl:value-of select="$namespace" />AttemptingToState.h"; </xsl:if>

namespace <xsl:value-of
             select="$namespace"/> 
{
    
    <xsl:if test="not(contains($stateName, 'Attempting'))">
       class <xsl:value-of select="$stateName" /> : public <xsl:value-of select="$className" />State { 
    </xsl:if>
    <xsl:if test="contains($stateName, 'Attempting')">
       class <xsl:value-of select="$stateName" /> : public AttemptingToState { 
    </xsl:if>
        public: 
        		static <xsl:value-of select="$stateName" />* the<xsl:value-of  select="$stateName" />();
        		<xsl:for-each select="/*/state[@id=$stateName]/transition">
          		virtual void <xsl:call-template name="removeWordFromString"><xsl:with-param name="word" select="'Input'" /><xsl:with-param name="text" select="@event" /></xsl:call-template>( <xsl:value-of select="$className" />Context<xsl:text disable-output-escaping="yes"><![CDATA[&]]></xsl:text> context) { transition(context, <xsl:text disable-output-escaping="yes"><![CDATA[&]]></xsl:text><xsl:value-of select="target/@next"/>::the<xsl:value-of select="target/@next"/>()) } 
          		</xsl:for-each>
       
        		<xsl:if test="contains($stateName, 'Attempting')">
        protected: 
       			virtual void processEvent(){ /****PLEASE FILL IN THIS METHOD****/ }
    			</xsl:if>
        }; 
        
		
        
 
}// end namespace igstk
</xsl:template>
		
</xsl:stylesheet>