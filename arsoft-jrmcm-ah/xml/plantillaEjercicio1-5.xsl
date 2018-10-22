<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
xmlns:p="http://www.example.org/ejercicio1-2">

<xsl:output method="xml" indent="yes"/>

	<xsl:template match="/">
		<feed xmlns="http://www.w3.org/2005/Atom">
			<xsl:apply-templates/>
		</feed>
	</xsl:template>
	
	<xsl:template match="p:programa">
		<programa id="{@id}">
			<nombre> <xsl:value-of select="p:nombre"/> </nombre>
			<URLprograma> <xsl:value-of select="p:URLprograma"/> </URLprograma>
			<URLimagen> <xsl:value-of select="p:URLimagen"/> </URLimagen>
			<xsl:for-each select="p:emision">
				<emision titulo="{@titulo}" fecha="{@fecha}" tiempo="{@tiempo}" URL="{@URL}"/>
			</xsl:for-each>
		</programa>
	</xsl:template>
</xsl:stylesheet>