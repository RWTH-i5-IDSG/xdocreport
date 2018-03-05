/**
 * Copyright (C) 2011-2015 The XDocReport Team <xdocreport@googlegroups.com>
 *
 * All rights reserved.
 *
 * Permission is hereby granted, free  of charge, to any person obtaining
 * a  copy  of this  software  and  associated  documentation files  (the
 * "Software"), to  deal in  the Software without  restriction, including
 * without limitation  the rights to  use, copy, modify,  merge, publish,
 * distribute,  sublicense, and/or sell  copies of  the Software,  and to
 * permit persons to whom the Software  is furnished to do so, subject to
 * the following conditions:
 *
 * The  above  copyright  notice  and  this permission  notice  shall  be
 * included in all copies or substantial portions of the Software.
 *
 * THE  SOFTWARE IS  PROVIDED  "AS  IS", WITHOUT  WARRANTY  OF ANY  KIND,
 * EXPRESS OR  IMPLIED, INCLUDING  BUT NOT LIMITED  TO THE  WARRANTIES OF
 * MERCHANTABILITY,    FITNESS    FOR    A   PARTICULAR    PURPOSE    AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE,  ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package fr.opensagres.xdocreport.document.textstyling.html;

import fr.opensagres.xdocreport.core.utils.StringUtils;
import fr.opensagres.xdocreport.document.textstyling.properties.CaptionProperties;
import fr.opensagres.xdocreport.document.textstyling.properties.ContainerProperties;
import fr.opensagres.xdocreport.document.textstyling.properties.HeaderProperties;
import fr.opensagres.xdocreport.document.textstyling.properties.ListItemProperties;
import fr.opensagres.xdocreport.document.textstyling.properties.ListProperties;
import fr.opensagres.xdocreport.document.textstyling.properties.ParagraphProperties;
import fr.opensagres.xdocreport.document.textstyling.properties.SpanProperties;
import fr.opensagres.xdocreport.document.textstyling.properties.TableCellProperties;
import fr.opensagres.xdocreport.document.textstyling.properties.TableProperties;
import fr.opensagres.xdocreport.document.textstyling.properties.TableRowProperties;
import fr.opensagres.xdocreport.document.textstyling.properties.TextAlignment;
import org.xml.sax.Attributes;

import java.awt.Color;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Styles Helper.
 */
public class StylesHelper
{

    public static Map<String, String> parse( String style )
    {
        if ( StringUtils.isEmpty( style ) )
        {
            return Collections.emptyMap();
        }
        Map<String, String> stylesMap = new HashMap<String, String>();
        String[] styles = style.split( ";" );
        String s = null;
        int index = 0;
        String name = null;
        String value = null;
        for ( int i = 0; i < styles.length; i++ )
        {
            name = null;
            value = null;
            s = styles[i];
            index = s.indexOf( ':' );
            if ( index != -1 )
            {
                name = s.substring( 0, index ).trim();
                value = s.substring( index + 1, s.length() ).trim();
                stylesMap.put( name, value );
            }
        }
        return stylesMap;
    }

    /**
     * Create {@link ParagraphProperties} from inline style.
     * 
     * @param style
     * @return
     */
    public static ParagraphProperties createParagraphProperties( String style )
    {
        Map<String, String> stylesMap = StylesHelper.parse( style );
        if ( !stylesMap.isEmpty() )
        {
            ParagraphProperties properties = new ParagraphProperties();
            processContainerProperties(properties, stylesMap);
            return properties;
        }
        return null;
    }

    /**
     * Create {@link HeaderProperties} from inline style.
     * 
     * @param style
     * @return
     */
    public static HeaderProperties createHeaderProperties( String style )
    {
        Map<String, String> stylesMap = StylesHelper.parse( style );
        if ( !stylesMap.isEmpty() )
        {
            HeaderProperties properties = new HeaderProperties();
            processContainerProperties( properties, stylesMap );
            return properties;
        }
        return null;
    }

    /**
     * Create {@link ListItemProperties} from inline style.
     * 
     * @param style
     * @return
     */
    public static ListItemProperties createListItemProperties( String style )
    {
        Map<String, String> stylesMap = StylesHelper.parse( style );
        if ( !stylesMap.isEmpty() )
        {
            ListItemProperties properties = new ListItemProperties();
            processContainerProperties( properties, stylesMap );
            return properties;
        }
        return null;
    }

    /**
     * Create {@link ListProperties} from inline style.
     * 
     * @param style
     * @return
     */
    public static ListProperties createListProperties( String style )
    {
        Map<String, String> stylesMap = StylesHelper.parse( style );
        if ( !stylesMap.isEmpty() )
        {
            ListProperties properties = new ListProperties();
            processContainerProperties( properties, stylesMap );
            return properties;
        }
        return null;
    }

    /**
     * Create {@link SpanProperties} from inline style.
     * 
     * @param style
     * @return
     */
    public static SpanProperties createSpanProperties( String style )
    {
        Map<String, String> stylesMap = StylesHelper.parse( style );
        if ( !stylesMap.isEmpty() )
        {
            SpanProperties properties = new SpanProperties();
            processContainerProperties( properties, stylesMap );
            return properties;
        }
        return null;
    }

    private static void processContainerProperties(ContainerProperties properties, Map<String, String> stylesMap )
    {
        // page-break-before
        properties.setPageBreakBefore( false );
        if ( "always".equals( stylesMap.get( "page-break-before" ) ) )
        {
            properties.setPageBreakBefore( true );
        }

        // page-break-after
        properties.setPageBreakAfter( false );
        if ( "always".equals( stylesMap.get( "page-break-after" ) ) )
        {
            properties.setPageBreakAfter( true );
        }

        // font-weight
        String fontWeight = stylesMap.get( "font-weight" );
        properties.setBold( false );
        if ( fontWeight != null )
        {
            if ( "bold".equals( fontWeight ) || "700".equals( fontWeight ) )
            {
                properties.setBold( true );
            }
        }

        // font-style
        String fontStyle = stylesMap.get( "font-style" );
        properties.setItalic( false );
        if ( fontStyle != null )
        {
            if ( "italic".equals( fontStyle ) )
            {
                properties.setItalic( true );
            }
        }

        // text-decoration
        String textDecoration = stylesMap.get( "text-decoration" );
        properties.setStrike( false );
        properties.setUnderline( false );
        if ( textDecoration != null )
        {
            if ( textDecoration.contains( "underline" ) )
            {
                properties.setUnderline( true );
            }

            if ( textDecoration.contains( "line-through" ) )
            {
                properties.setStrike( true );
            }
        }

        // vertical-align
        String verticalAlign = stylesMap.get( "vertical-align" );
        properties.setSuperscript( false );
        properties.setSubscript( false );
        if ( verticalAlign != null )
        {
            if ( "sub".equals( verticalAlign ) )
            {
                properties.setSubscript( true );
            }
            else if ( "super".equals( verticalAlign ) )
            {
                properties.setSuperscript( true );
            }
        }

        // text-align
        String textAlignment = stylesMap.get( "text-align" );
        if ( textAlignment != null )
        {
            if ( "left".equals( textAlignment ) )
            {
                properties.setTextAlignment( TextAlignment.Left );
            }
            else if ( "center".equals( textAlignment ) )
            {
                properties.setTextAlignment( TextAlignment.Center );
            }
            else if ( "right".equals( textAlignment ) )
            {
                properties.setTextAlignment( TextAlignment.Right );
            }
            else if ( "justify".equals( textAlignment ) )
            {
                properties.setTextAlignment( TextAlignment.Justify );
            }
            else if ( "inherit".equals( textAlignment ) )
            {
                properties.setTextAlignment( TextAlignment.Inherit );
            }

        }
        // style
        String styleName = stylesMap.get("name");
        if (styleName != null) {
            properties.setStyleName(styleName);
        }

        // text color
        String cssColor = stylesMap.get("color");
        if (cssColor != null) {
            Color color = parseToColor(cssColor);
            if (color != null) {
                // without #, because Word wants it this way (http://officeopenxml.com/WPtextFormatting.php)
                String hex = String.format("%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
                properties.setTextColorHex(hex);
            }
        }

        // background/highlight color
        String cssBgColor = stylesMap.get("background-color");
        if (cssBgColor != null) {
            Color color = parseToColor(cssBgColor);
            if (color != null) {
                properties.setBackgroundColorName(HighlightColor.getNameOfClosest(color));
            }
        }
    }

    public static TableProperties createTableProperties( Attributes attributes )
    {
        TableProperties properties = new TableProperties();
        return properties;
    }

    public static TableRowProperties createTableRowProperties( Attributes attributes )
    {
        TableRowProperties properties = new TableRowProperties();
        return properties;
    }

    public static TableCellProperties createTableCellProperties( Attributes attributes )
    {
        TableCellProperties properties = new TableCellProperties();
        return properties;
    }

    public static CaptionProperties createCaptionProperties( Attributes attributes )
    {
        CaptionProperties properties = new CaptionProperties();
        return properties;
    }

    /**
     * At the moment, we support rgb and hex formats.
     */
    private static Color parseToColor(String input) {
        try {
            // Taken from: https://stackoverflow.com/a/7614202
            Pattern rgbPattern = Pattern.compile("rgb *\\( *([0-9]+), *([0-9]+), *([0-9]+) *\\)");
            Matcher m = rgbPattern.matcher(input);
            if (m.matches()) {
                return new Color(
                        Integer.valueOf(m.group(1)), // r
                        Integer.valueOf(m.group(2)), // g
                        Integer.valueOf(m.group(3))  // b
                );
            } else if (input.startsWith("#")) {
                return Color.decode(input);
            }
        } catch (Exception e) {
            // No-op
        }
        return null;
    }

    /**
     * Names taken from {@link org.openxmlformats.schemas.wordprocessingml.x2006.main.STHighlightColor}
     * Color codes chosen to match {@link fr.opensagres.poi.xwpf.converter.core.styles.run.RunTextHighlightingValueProvider}
     */
    private enum HighlightColor {
        BLACK("black", new Color(0, 0, 0)),
        BLUE("blue", new Color(0, 0, 255)),
        CYAN("cyan", new Color(0, 255, 255)),
        GREEN("green", new Color(0, 255, 0)),
        MAGENTA("magenta", new Color(255, 0, 255)),
        RED("red", new Color(255, 0, 0)),
        YELLOW("yellow", new Color(255, 255, 0)),
        WHITE("white", new Color(255, 255, 255)),

        DARK_BLUE("darkBlue", new Color(0, 0, 128)),
        DARK_CYAN("darkCyan", new Color(0, 139, 139)),
        DARK_GREEN("darkGreen", new Color(0, 51, 0)),
        DARK_MAGENTA("darkMagenta", new Color(139, 0, 139)),
        DARK_RED("darkRed", new Color(139, 0, 0)),
        DARK_YELLOW("darkYellow", new Color(128, 128, 0)),
        DARK_GRAY("darkGray", new Color(64, 64, 64)),
        LIGHT_GRAY("lightGray", new Color(192, 192, 192));

        private final String colorName;
        private final Color color;

        HighlightColor(String colorName, Color color) {
            this.colorName = colorName;
            this.color = color;
        }

        private static String getNameOfClosest(Color c) {
            String lastName = null;
            double lastDistance = Double.MAX_VALUE;
            for (HighlightColor highlightColor : values()) {
                double distance = colorDistance(c, highlightColor.color);
                if (distance < lastDistance) {
                    lastDistance = distance;
                    lastName = highlightColor.colorName;
                }
            }
            return lastName;
        }

        /**
         * Taken from: https://stackoverflow.com/a/6334454
         */
        private static double colorDistance(Color c1, Color c2) {
            int red1 = c1.getRed();
            int red2 = c2.getRed();
            int rmean = (red1 + red2) >> 1;
            int r = red1 - red2;
            int g = c1.getGreen() - c2.getGreen();
            int b = c1.getBlue() - c2.getBlue();
            return Math.sqrt((((512 + rmean) * r * r) >> 8) + 4 * g * g + (((767 - rmean) * b * b) >> 8));
        }

    }
}
