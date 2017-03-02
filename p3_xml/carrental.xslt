<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:template match="/carrental">
    <html>
        <head><title>RENTALS</title></head>
        <body>
            <xsl:apply-templates select="rental"/>
        </body>
    </html>
</xsl:template>
<xsl:template match="rental">
    <table border="0">
        <h1>MARCA=<xsl:value-of select="marca"/></h1><br />
        <h1>MODEL=<xsl:value-of select="model"/></h1><br />
        <h1>START=<xsl:value-of select="start"/></h1><br />
        <h1>END=<xsl:value-of select="end"/></h1><br />
    </table>        
</xsl:template>
</xsl:stylesheet>
