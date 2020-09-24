/*
 * @(#)SvgTinySceneGraphExporter.java
 * Copyright © 2020 The authors and contributors of JHotDraw. MIT License.
 */
package org.jhotdraw8.svg.io;

import javafx.scene.Node;
import javafx.scene.shape.Path;
import org.jhotdraw8.annotation.NonNull;
import org.jhotdraw8.annotation.Nullable;
import org.jhotdraw8.css.CssDimension2D;
import org.jhotdraw8.geom.Shapes;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Exports a JavaFX scene graph to SVG Tiny 1.2.
 * <p>
 * References:<br>
 * <a href="https://www.w3.org/TR/SVGTiny12/">SVG 1.1</a>
 *
 * @author Werner Randelshofer
 */
public class FXSvgTinyWriter extends AbstractFXSvgWriter {

    private final static String SVG_VERSION = "1.2";
    private final static String SVG_BASE_PROFILE = "tiny";
    public final static String SVG_MIME_TYPE_WITH_VERSION = SVG_MIME_TYPE + ";version=\"" + SVG_VERSION + "\"";

    /**
     * @param imageUriKey this property is used to retrieve an URL from an
     *                    ImageView
     * @param skipKey     this property is used to retrieve a Boolean from a Node.
     */
    public FXSvgTinyWriter(Object imageUriKey, Object skipKey) {
        super(imageUriKey, skipKey);
    }

    protected String getSvgVersion() {
        return SVG_VERSION;
    }

    protected String getSvgBaseProfile() {
        return SVG_BASE_PROFILE;
    }

    protected void writeDocumentElementAttributes(@NonNull XMLStreamWriter
                                                          w, Node drawingNode, @Nullable CssDimension2D size) throws XMLStreamException {
        w.writeAttribute("version", getSvgVersion());
        w.writeAttribute("baseProfile", getSvgBaseProfile());
        if (size != null) {
            w.writeAttribute("width", nb.toString(size.getWidth().getValue()) + size.getWidth().getUnits());
            w.writeAttribute("height", nb.toString(size.getHeight().getValue()) + size.getHeight().getUnits());
        }
    }

    @Override
    protected void writeClipAttributes(@NonNull XMLStreamWriter w, @NonNull Node node) {
        // do not write clip attributes
    }

    @Override
    protected void writeClipPathDefs(@NonNull XMLStreamWriter w, @NonNull Node node) throws IOException {

        // do not write clip node defs
    }

    protected void writeCompositingAttributes(@NonNull XMLStreamWriter w, @NonNull Node
            node) {
        // do not write compositing attributes
    }

    private boolean isSuppressGroups() {
        return true;
    }

    @Nullable
    protected Element writePath(@NonNull Document doc, @NonNull Element
            parent, @NonNull Path node) {
        if (node.getElements().isEmpty()) {
            return null;
        }
        Element elem = doc.createElement("path");
        parent.appendChild(elem);
        String d;
        if (isRelativizePaths()) {
            d = Shapes.floatRelativeSvgStringFromAWT(Shapes.awtShapeFromFXPathElements(node.getElements(), node.getFillRule()).getPathIterator(null));
        } else {
            d = Shapes.floatSvgStringFromElements(node.getElements());
        }
        elem.setAttribute("d", d);
        return elem;
    }

    @Override
    protected List<String> getAdditionalNodeClasses(@NonNull Node node) {
        return Collections.emptyList();
    }

    protected void writeIdAttribute(@NonNull Element elem, @NonNull Node node) {
        // suppress id attribute
    }

}