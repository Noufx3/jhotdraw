/**
 * @(#)DefaultFontModel.java  1.0  May 18, 2008
 *
 * Copyright (c) 2008 by the original authors of JHotDraw
 * and all its contributors.
 * All rights reserved.
 *
 * The copyright of this software is owned by the authors and  
 * contributors of the JHotDraw project ("the copyright holders").  
 * You may not use, copy or modify this software, except in  
 * accordance with the license agreement you entered into with  
 * the copyright holders. For details see accompanying license terms. 
 */
package org.jhotdraw.gui.fontchooser;

import java.awt.*;
import java.util.*;
import java.util.ArrayList;
import javax.swing.tree.*;
import org.jhotdraw.util.ResourceBundleUtil;

/**
 * DefaultFontChooserModel.
 *
 * @author Werner Randelshofer
 * @version 1.0 May 18, 2008 Created.
 */
public class DefaultFontChooserModel extends AbstractFontChooserModel {

    /**
     * Root node.
     */
    protected DefaultMutableTreeNode root;

    public DefaultFontChooserModel() {
        root = new DefaultMutableTreeNode();
        init(GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts());
    }

    public DefaultFontChooserModel(Font[] fonts) {
        root = new DefaultMutableTreeNode();
        init(fonts);
    }

    protected void init(Font[] fonts) {
        ResourceBundleUtil labels = ResourceBundleUtil.getLAFBundle("org.jhotdraw.gui.Labels");

        // collect families and sort them alphabetically
        ArrayList<FontFamilyNode> families = new ArrayList<FontFamilyNode>();
        HashMap<String, FontFamilyNode> familyMap = new HashMap<String, FontFamilyNode>();
        for (Font f : fonts) {
            String familyName = f.getFamily();
            FontFamilyNode family;
            if (familyMap.containsKey(familyName)) {
                family = familyMap.get(familyName);
            } else {
                family = new FontFamilyNode(familyName);
                familyMap.put(familyName, family);
            }
            family.add(new FontFaceNode(f));
        }
        families.addAll(familyMap.values());
        Collections.sort(families);

        // group families into collections
        root.removeAllChildren();

        root.add(new FontCollectionNode(labels.getString("FontCollection.allFonts"), (ArrayList<FontFamilyNode>) families.clone()));
        
        // Web-save fonts
        root.add(
                new FontCollectionNode(labels.getString("FontCollection.web"), collectFamiliesNamed(families,
                "Arial",
                "Arial Black",
                "Comic Sans MS",
                "Georgia",
                "Impact",
                "Times New Roman",
                "Trebuchet MS",
                "Verdana",
                "Webdings")));
        
        /*
        // PDF Fonts
        root.add(
                new FontCollectionNode(labels.getString("FontCollection.pdf"), collectFamiliesNamed(families,
                "Andale Mono",
                "Courier",
                "Helvetica",
                "Symbol",
                "Times",
                "Zapf Dingbats")));
        */
        // Java System fonts
        root.add(
                new FontCollectionNode(labels.getString("FontCollection.system"), collectFamiliesNamed(families,
                "Dialog",
                "DialogInput",
                "Monospaced",
                "SansSerif",
                "Serif")));
        root.add(
                new FontCollectionNode(labels.getString("FontCollection.serif"), collectFamiliesNamed(families,
                // Fonts on Mac OS X 10.5:
                "Adobe Caslon Pro",
                "Adobe Garamond Pro",
                "American Typewriter",
                "Arno Pro",
                "Baskerville",
                "Baskerville Old Face",
                "Bell MT",
                "Big Caslon",
                "Bodoni SvtyTwo ITC TT",
                "Bodoni SvtyTwo OS ITC TT",
                "Bodoni SvtyTwo SC ITC TT",
                "Book Antiqua",
                "Bookman Old Style",
                "Calisto MT",
                "Chaparral Pro",
                "Century",
                "Century Schoolbook",
                "Cochin",
                "Footlight MT Light",
                "Garamond",
                "Garamond Premier Pro",
                "Georgia",
                "Goudy Old Style",
                "Hoefler Text",
                "Lucida Bright",
                "Lucida Fax",
                "Minion Pro",
                "Palatino",
                "Times",
                "Times New Roman",
                
                // Fonts on Windows XP:
                "Palatino Linotype"
                )));
        root.add(
                new FontCollectionNode(labels.getString("FontCollection.sansSerif"), collectFamiliesNamed(families,
                // Fonts on Mac OS X 10.5:
                "Abadi MT Condensed Extra Bold",
                "Abadi MT Condensed Light",
                "Al Bayan",
                "AppleGothic",
                "Arial",
                "Arial Black",
                "Arial Narrow",
                "Arial Rounded MT Bold",
                "Arial Unicode MS",
                "Bell Gothic Std",
                "Blair MdITC TT",
                "Century Gothic",
                "Frutiger",
                "Futura",
                "Geneva",
                "Gill Sans",
                "Gulim",
                "Helvetica",
                "Helvetica Neue",
                "Lucida Grande",
                "Lucida Sans",
                "Microsoft Sans Serif",
                "Myriad Pro",
                "News Gothic",
                "Tahoma",
                "Trebuchet MS",
                "Verdana",
                
                // Fonts on Windows XP:
                "Franklin Gothic Medium",
                "Lucida Sans Unicode")));
        root.add(
                new FontCollectionNode(labels.getString("FontCollection.script"), collectFamiliesNamed(families,
                "Apple Chancery",
                "Bickham Script Pro",
                "Blackmoor LET",
                "Bradley Hand ITC TT",
                "Brush Script MT",
                "Brush Script Std",
                "Chalkboard",
                "Charlemagne Std",
                "Comic Sans MS",
                "Curlz MT",
                "Edwardian Script ITC",
                "Footlight MT Light",
                "Giddyup Std",
                "Handwriting - Dakota",
                "Harrington",
                "Herculanum",
                "Kokonor",
                "Lithos Pro",
                "Lucida Blackletter",
                "Lucida Calligraphy",
                "Lucida Handwriting",
                "Marker Felt",
                "Matura MT Script Capitals",
                "Mistral",
                "Monotype Corsiva",
                "Party LET",
                "Papyrus",
                "Santa Fe LET",
                "Savoye LET",
                "SchoolHouse Cursive B",
                "SchoolHouse Printed A",
                "Skia",
                "Snell Roundhand",
                "Tekton Pro",
                "Trajan Pro",
                "Zapfino",
                "")));
        root.add(
                new FontCollectionNode(labels.getString("FontCollection.monospaced"), collectFamiliesNamed(families,
                // Fonts on Mac OS X 10.5:
                "Andale Mono",
                "Courier",
                "Courier New",
                "Letter Gothic Std",
                "Lucida Sans Typewriter",
                "Monaco",
                "OCR A Std",
                "Orator Std",
                "Prestige Elite Std",
                
                // Fonts on Windows XP:
                "Lucida Console"
                )));
        root.add(
                new FontCollectionNode(labels.getString("FontCollection.decorative"), collectFamiliesNamed(families,
                // Fonts on Mac OS X 10.5:
                "Academy Engraved LET",
                "Arial Black",
                "Bank Gothic",
                "Bauhaus 93",
                "Bernard MT Condensed",
                "Birch Std",
                "Blackoak Std",
                "BlairMdITC TT",
                "Bordeaux Roman Bold LET",
                "Braggadocio",
                "Britannic Bold",
                "Capitals",
                "Colonna MT",
                "Cooper Black",
                "Cooper Std",
                "Copperplate",
                "Copperplate Gothic Bold",
                "Copperplate Gothic Light",
                "Cracked",
                "Desdemona",
                "Didot",
                "Eccentric Std",
                "Engravers MT",
                "Eurostile",
                "Gill Sans Ultra Bold",
                "Gloucester MT Extra Condensed",
                "Haettenschweiler",
                "Hobo Std",
                "Impact",
                "Imprint MT Shadow",
                "Jazz LET",
                "Kino MT",
                "Matura MT Script Capitals",
                "Mesquite Std",
                "Modern No. 20",
                "Mona Lisa Solid ITC TT",
                "MS Gothic",
                "Nueva Std",
                "Onyx",
                "Optima",
                "Perpetua Titling MT",
                "Playbill",
                "Poplar Std",
                "PortagoITC TT",
                "Princetown LET",
                "Rockwell",
                "Rockwell Extra Bold",
                "Rosewood Std",
                "Santa Fe LET",
                "Stencil",
                "Stencil Std",
                "Stone Sans ITC TT",
                "Stone Sans OS ITC TT",
                "Stone Sans Sem ITC TT",
                "Stone Sans Sem OS ITCTT",
                "Stone Sans Sem OS ITC TT",
                "Synchro LET",
                "Wide Latin")));
        root.add(
                new FontCollectionNode(labels.getString("FontCollection.symbols"), collectFamiliesNamed(families,
                // Fonts on Mac OS X 10.5:
                "Apple Symbols",
                "Blackoack Std",
                "Bodoni Ornaments ITC TT",
                "EuropeanPi",
                "Monotype Sorts",
                "MT Extra",
                "Symbol",
                "Type Embellishments One LET",
                "Webdings",
                "Wingdings",
                "Wingdings 2",
                "Wingdings 3",
                "Zapf Dingbats")));
        
        // Collect font families, which are not in one of the other collections
        // (except the collection AllFonts).
        FontCollectionNode others = new FontCollectionNode(labels.getString("FontCollection.other"));
        HashSet<FontFamilyNode> otherFamilySet = new HashSet<FontFamilyNode>();
        otherFamilySet.addAll(families);
        for (int i=1,n=root.getChildCount(); i < n; i++) {
            FontCollectionNode fcn = (FontCollectionNode) root.getChildAt(i);
            for (FontFamilyNode ffn : fcn.families()) {
                otherFamilySet.remove(ffn);
            }
        }
        ArrayList<FontFamilyNode> otherFamilies = new ArrayList<FontFamilyNode>();
        for (FontFamilyNode ffn : otherFamilySet) {
            otherFamilies.add(ffn.clone());
        }
        Collections.sort(otherFamilies);
        others.addAll(otherFamilies);
        root.add(others);
    }

    protected ArrayList<FontFamilyNode> collectFamiliesNamed(ArrayList<FontFamilyNode> families, String... names) {
        ArrayList<FontFamilyNode> coll = new ArrayList<FontFamilyNode>();
        HashSet<String> nameMap = new HashSet<String>();
        nameMap.addAll(Arrays.asList(names));
        for (FontFamilyNode family : families) {
            String fName = family.getName();
            if (nameMap.contains(family.getName())) {
                coll.add(family.clone());
            }

        }
        return coll;
    }

    public boolean isEditable(MutableTreeNode node) {
        boolean result = true;
        if (node instanceof FontFaceNode) {
            result &= ((FontFaceNode) node).isEditable();
            node =
                    (MutableTreeNode) node.getParent();
        }

        if (result && (node instanceof FontFamilyNode)) {
            result &= ((FontFamilyNode) node).isEditable();
            node =
                    (MutableTreeNode) node.getParent();
        }

        if (result && (node instanceof FontCollectionNode)) {
            result &= ((FontFamilyNode) node).isEditable();
        }

        return result;
    }

    public Object getRoot() {
        return root;
    }

    public Object getChild(
            Object parent, int index) {
        return ((TreeNode) parent).getChildAt(index);
    }

    public int getChildCount(Object parent) {
        return ((TreeNode) parent).getChildCount();
    }

    public boolean isLeaf(Object node) {
        return ((TreeNode) node).isLeaf();
    }

    public void valueForPathChanged(TreePath path, Object newValue) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getIndexOfChild(Object parent, Object child) {
        return ((TreeNode) parent).getIndex((TreeNode) child);
    }
}
