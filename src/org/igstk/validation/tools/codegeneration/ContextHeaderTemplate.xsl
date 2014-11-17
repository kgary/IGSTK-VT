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
namespace <xsl:value-of
             select="$namespace"/> 
{
             
        
    	class <xsl:value-of select="$className" />{
        	
        	public:
        			<xsl:value-of select="$className" />();
            		<xsl:for-each select="scxml/state">
          			void <xsl:value-of select="@id"/>();
          			</xsl:for-each>
            
            private: 
            		friend class <xsl:value-of select="$className" />State;
            		void ChangeState( <xsl:value-of select="$className" />State* );
            
            private: 
            		<xsl:value-of select="$className" />State* _State;
            
         
        };
 
}// end namespace igstk
	</xsl:template>
</xsl:stylesheet>