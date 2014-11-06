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
	<xsl:template xmlns:util="java:test.Utility" match="/">namespace <xsl:value-of
             select="$namespace"/> 
{
             
        /** Constructor */
    	<xsl:value-of select="$className" />::<xsl:value-of select="$className"></xsl:value-of>():m_StateMachine(this)
        {
            
          <xsl:for-each select="scxml/state/transition">
          	igstkAddInputMacro( <xsl:call-template name="capitalizeString"><xsl:with-param name="str"  select="@event"/></xsl:call-template> ) 
          	
          </xsl:for-each>
          <xsl:for-each select="scxml/state">
          	igstkAddStateMacro( <xsl:value-of select="@id"/> ) 
          	
          </xsl:for-each>
           
        <xsl:for-each select="scxml/state">
	          	igstkAddTransitionMacro( <xsl:value-of select="@id"/>, <xsl:value-of select="transition/@event"/>, <xsl:value-of select="transition/target/@next"/>  ) 

	          </xsl:for-each>
          
          <xsl:for-each select="scxml">
            igstkSetInitialStateMacro( <xsl:value-of select="@initialstate" /> )
          </xsl:for-each>
          
          
          m_StateMachine.SetReadyToRun();   
        }
        
        
  /** Destructor */
 <xsl:value-of select="$className" />::~<xsl:value-of select="$className" />()  
 {
 	igstkLogMacro( DEBUG,  "Destructor called ....\n" );      
 }
 
 
}// end namespace igstk
	</xsl:template>
	
	
	
	
</xsl:stylesheet>