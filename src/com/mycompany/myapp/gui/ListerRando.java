package com.mycompany.myapp.gui;

import com.mycompany.myapp.gui.ListerParticipant;
import com.mycompany.myapp.gui.BaseForm;
import com.codename1.components.InfiniteProgress;

import com.codename1.components.ScaleImageLabel;
import com.codename1.components.SpanLabel;
import static com.codename1.io.Log.p;
import static com.codename1.io.Log.p;

import com.codename1.ui.Button;
import com.codename1.ui.ButtonGroup;
import com.codename1.ui.Component;
import static com.codename1.ui.Component.BOTTOM;
import static com.codename1.ui.Component.CENTER;
import static com.codename1.ui.Component.LEFT;
import static com.codename1.ui.Component.RIGHT;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.FontImage;

import com.codename1.ui.Form;
import com.codename1.ui.Graphics;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.RadioButton;
import com.codename1.ui.Slider;
import com.codename1.ui.Tabs;

import com.codename1.ui.Toolbar;
import com.codename1.ui.URLImage;

import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.layouts.LayeredLayout;
import com.codename1.ui.plaf.Style;

import com.codename1.ui.util.Resources;
import com.mycompany.myapp.entities.Randonnee;

import com.mycompany.myapp.services.ServiceRandonnee;

import java.util.ArrayList;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author MSI
 */
public class ListerRando extends BaseForm {

    Form current;

    public ListerRando(Resources res) {

        super("Newsfeed", BoxLayout.y());
        Toolbar tb = new Toolbar(true);
        setToolbar(tb);
        getTitleArea().setUIID("Container");
        setTitle("Randonnées");
        getContentPane().setScrollVisible(false);

        super.addSideMenu(res);

        Tabs swipe = new Tabs();

        Label spacer1 = new Label();

        addTab(swipe, res.getImage("rando.jpg"), spacer1, "", "", "");

        swipe.setUIID("Container");
        swipe.getContentPane().setUIID("Container");
        swipe.hideTabs();

        ButtonGroup bg = new ButtonGroup();
        int size = Display.getInstance().convertToPixels(1);
        Image unselectedWalkthru = Image.createImage(size, size, 0);
        Graphics g = unselectedWalkthru.getGraphics();
        g.setColor(0x11111);
        g.setAlpha(100);
        g.setAntiAliased(true);
        g.fillArc(0, 0, size, size, 0, 200);
        Image selectedWalkthru = Image.createImage(size, size, 0);
        g = selectedWalkthru.getGraphics();
        g.setColor(0xffffff);
        g.setAntiAliased(true);
        g.fillArc(0, 0, size, size, 0, 200);
        RadioButton[] rbs = new RadioButton[swipe.getTabCount()];
        FlowLayout flow = new FlowLayout(CENTER);
        flow.setValign(BOTTOM);
        Container radioContainer = new Container(flow);
        for (int iter = 0; iter < rbs.length; iter++) {
            rbs[iter] = RadioButton.createToggle(unselectedWalkthru, bg);
            rbs[iter].setPressedIcon(selectedWalkthru);
            rbs[iter].setUIID("Label");
            radioContainer.add(rbs[iter]);
        }

        rbs[0].setSelected(true);
        swipe.addSelectionListener((i, ii) -> {
            if (!rbs[ii].isSelected()) {
                rbs[ii].setSelected(true);
            }
        });

        Component.setSameSize(radioContainer, spacer1);
        add(LayeredLayout.encloseIn(swipe, radioContainer));

        ButtonGroup barGroup = new ButtonGroup();
        RadioButton rando = RadioButton.createToggle("Randonnées", barGroup);
        rando.setUIID("SelectBar");
        RadioButton histo = RadioButton.createToggle("Historiques", barGroup);
        histo.setUIID("SelectBar");

        Label arrow = new Label(res.getImage("news-tab-down-arrow.png"), "Container");

        rando.addActionListener((e) -> {
            InfiniteProgress ip = new InfiniteProgress();
            final Dialog ipD1g = ip.showInfiniteBlocking();
            new ListerRando(res).show();

            refreshTheme();

        });

        histo.addActionListener((e) -> {
            InfiniteProgress ip = new InfiniteProgress();
            final Dialog ipD1g = ip.showInfiniteBlocking();
            new ListerParticipant(res).show();

            refreshTheme();

        });

        add(LayeredLayout.encloseIn(
                GridLayout.encloseIn(2, rando, histo),
                FlowLayout.encloseBottom(arrow)
        ));

        rando.setSelected(true);
        arrow.setVisible(false);
        addShowListener(e -> {
            arrow.setVisible(true);
            updateArrowPosition(rando, arrow);
        });
        bindButtonSelection(rando, arrow);
        bindButtonSelection(histo, arrow);

        // special case for rotation
        addOrientationListener(e -> {
            updateArrowPosition(barGroup.getRadioButton(barGroup.getSelectedIndex()), arrow);
        });

        // appel affichage 
        // appel affichage 
        ArrayList<Randonnee> list = ServiceRandonnee.getInstance().getAllRandonnees();

        for (Randonnee p : list) {

            String urlImage = ("logo.jpeg");
            Image PlaceHolder = Image.createImage(120, 90);
            EncodedImage enc = EncodedImage.createFromImage(PlaceHolder, false);
            URLImage urlim = URLImage.createToStorage(enc, urlImage, urlImage, URLImage.RESIZE_SCALE);

            addButton(urlim, p, res);

            ScaleImageLabel image = new ScaleImageLabel(urlim);
            Container containerImg = new Container();
            image.setBackgroundType(Style.BACKGROUND_IMAGE_SCALED_FILL);

        }

    }

    private void addTab(Tabs swipe, Image img, Label spacer, String likesStr, String commentsStr, String text) {
        int size = Math.min(Display.getInstance().getDisplayWidth(), Display.getInstance().getDisplayHeight());
        if (img.getHeight() < size) {
            img = img.scaledHeight(size);
        }
        Label likes = new Label(likesStr);
        Style heartStyle = new Style(likes.getUnselectedStyle());
        heartStyle.setFgColor(0xff2d55);
        FontImage heartImage = FontImage.createMaterial(FontImage.MATERIAL_FAVORITE, heartStyle);
        likes.setIcon(heartImage);
        likes.setTextPosition(RIGHT);

        Label comments = new Label(commentsStr);
        FontImage.setMaterialIcon(comments, FontImage.MATERIAL_CHAT);
        if (img.getHeight() > Display.getInstance().getDisplayHeight() / 2) {
            img = img.scaledHeight(Display.getInstance().getDisplayHeight() / 2);
        }
        ScaleImageLabel image = new ScaleImageLabel(img);
        image.setUIID("Container");
        image.setBackgroundType(Style.BACKGROUND_IMAGE_SCALED_FILL);
        Label overlay = new Label(" ", "ImageOverlay");

        Container page1
                = LayeredLayout.encloseIn(
                        image,
                        overlay,
                        BorderLayout.south(
                                BoxLayout.encloseY(
                                        new SpanLabel(text, "LargeWhiteText"),
                                        FlowLayout.encloseIn(likes, comments),
                                        spacer
                                )
                        )
                );

        swipe.addTab("", page1);
    }

    public void bindButtonSelection(Button btn, Label l) {

        btn.addActionListener(e -> {

            if (btn.isSelected()) {

                updateArrowPosition(btn, l);

            }

        });
    }

    private void updateArrowPosition(Button btn, Label l) {
        l.getUnselectedStyle().setMargin(LEFT, btn.getX() + btn.getWidth() / 2 - l.getWidth() / 2);
        l.getParent().repaint();

    }

    private void addButton(Image img, Randonnee r, Resources res) {
        int height = Display.getInstance().convertToPixels(11.5f);

        int width = Display.getInstance().convertToPixels(5f);

        Button image = new Button(img.fill(width, height));
        image.setUIID("Label");
        Container cnt = BorderLayout.west(image);

        Label nomText = new Label("nom:" + r.getNomRando(), "NewsTopLine2");
        Label destination = new Label("Destination:" + r.getDestination(), "NewsTopLine2");
        Label categorie = new Label("Catégorie:" + r.getCategorieRando(), "NewsTopLine2");

        Label duree = new Label("Duree:" + r.getDureeRando(), "NewsTopLine2");

        Label prix = new Label("Prix:" + r.getPrix(), "NewsTopLine2");
        Label date = new Label("Date:" + r.getDateRando(), "NewsTopLine2");
       // Label rat = new Label("Rating: " + r.getNote(), "NewsTopLine2");

       // RatingForm rf = new RatingForm(r.getNote());
        //Slider rating = rf.getRating();
        //rating.setEditable(false);

        //  c2.addAll(l1,l2,l3,l4,l5,l6);
        // add(rating);
        Label b = new Label(" ");
        b.setUIID("NewsTopLine");
        Style bStyle = new Style(b.getUnselectedStyle());
        bStyle.setFgColor(0xf7ad02);

        FontImage bFontImage = FontImage.createMaterial(FontImage.MATERIAL_BOOKMARK_ADD, bStyle);
        b.setIcon(bFontImage);
        b.setTextPosition(LEFT);

        b.addPointerPressedListener(l -> {

            //System.out.println("modifier");
            System.out.println(r.getId());

            new Participer(res, r).show();

        });
        Label lModifier = new Label(" ");
        lModifier.setUIID("NewsTopLine");
        Style modifierStyle = new Style(lModifier.getUnselectedStyle());
        modifierStyle.setFgColor(0xf7ad02);

        FontImage mFontImage = FontImage.createMaterial(FontImage.MATERIAL_MAP, modifierStyle);
        lModifier.setIcon(mFontImage);
        lModifier.setTextPosition(LEFT);

        // appel service modifier 
        lModifier.addPointerPressedListener(l -> {

            new MapRandonnee(res);

        });

        cnt.add(BorderLayout.CENTER, BoxLayout.encloseY(BoxLayout.encloseX(nomText, b, lModifier),
                BoxLayout.encloseX(destination),
                BoxLayout.encloseX(categorie),
                BoxLayout.encloseX(date),
                BoxLayout.encloseX(duree),
                BoxLayout.encloseX(prix)
               // BoxLayout.encloseX(rat),
               // FlowLayout.encloseCenter(rating)
        ));

        add(cnt);
    }

}