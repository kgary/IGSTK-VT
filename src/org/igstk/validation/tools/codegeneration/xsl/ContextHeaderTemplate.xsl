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
      				void <xsl:value-of select="$v"/>();
      <xsl:call-template name="loopThroughList">
        <xsl:with-param name="input" select="substring-after($input, ',')"/>
      </xsl:call-template>
    </xsl:if> 
</xsl:template>
	
	<xsl:template match="/">
/* This file was generated. */
namespace <xsl:value-of
             select="$namespace"/> 
{
             
        
    	class <xsl:value-of select="$className" />Context{
        	
        	public:
        			<xsl:value-of select="$className" />Context();
            		 <xsl:call-template name="loopThroughList">
    					<xsl:with-param name="input" select="$methods"/> 
					</xsl:call-template>

            
            private: 
            		friend class <xsl:value-of select="$className" />State;
            		void transition(<xsl:value-of select="$className" />State* newState );
            
            private: 
            		<xsl:value-of select="$className" />State* _state;
            
         
        };
 
}// end namespace igstk
	</xsl:template>
</xsl:stylesheet>