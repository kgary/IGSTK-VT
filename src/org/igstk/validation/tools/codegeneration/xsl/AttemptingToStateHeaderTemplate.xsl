<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"  xmlns:xsl="http://www.w3.org/1999/XSL/Transform" >

 <xsl:output method="text" encoding="UTF-8" /> 
 
	<xsl:param name="className" />
	<xsl:param name="namespace" />


	<xsl:template match="/">
/* This file was generated. */
namespace <xsl:value-of
             select="$namespace"/> 
{
	class AttemptingToState : <xsl:value-of select="$className" />State{	
        protected: 
        	virtual void processEvent() = 0;
	};
 
}// end namespace igstk
	</xsl:template>
	
</xsl:stylesheet>