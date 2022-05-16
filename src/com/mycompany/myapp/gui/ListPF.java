/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.myapp.gui;

import com.codename1.components.InfiniteProgress;
import com.codename1.components.ScaleImageLabel;
import com.codename1.components.SpanLabel;
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
import com.mycompany.myapp.entities.ParticForm;
import com.mycompany.myapp.services.ServiceParticForm;

import java.util.ArrayList;

/**
 *
 * @author seifi
 */
public class ListPF extends BaseFormBack{
    Form current;
    public ListPF (Resources res) {
        
        
        
      
   super("Newsfeed", BoxLayout.y());
        Toolbar tb = new Toolbar(true);
        setToolbar(tb);
        getTitleArea().setUIID("Container");
        setTitle("Formations");
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
        g.fillArc(0, 0, size, size, 0, 360);
        Image selectedWalkthru = Image.createImage(size, size, 0);
        g = selectedWalkthru.getGraphics();
        g.setColor(0xffffff);
        g.setAntiAliased(true);
        g.fillArc(0, 0, size, size, 0, 360);
        RadioButton[] rbs = new RadioButton[swipe.getTabCount()];
        FlowLayout flow = new FlowLayout(CENTER);
        flow.setValign(BOTTOM);
        Container radioContainer = new Container(flow);
        for(int iter = 0 ; iter < rbs.length ; iter++) {
            rbs[iter] = RadioButton.createToggle(unselectedWalkthru, bg);
            rbs[iter].setPressedIcon(selectedWalkthru);
            rbs[iter].setUIID("Label");
            radioContainer.add(rbs[iter]);
        }
                
        rbs[0].setSelected(true);
        swipe.addSelectionListener((i, ii) -> {
            if(!rbs[ii].isSelected()) {
                rbs[ii].setSelected(true);
            }
        });
          Component.setSameSize(radioContainer, spacer1);
        add(LayeredLayout.encloseIn(swipe, radioContainer));
        
        ButtonGroup barGroup = new ButtonGroup();
        RadioButton Publications = RadioButton.createToggle("Formations", barGroup);
        Publications.setUIID("SelectBar");
        RadioButton pub = RadioButton.createToggle("Participants", barGroup);
        pub.setUIID("SelectBar");
        RadioButton ajoutFormation = RadioButton.createToggle("+formation", barGroup);
        ajoutFormation.setUIID("SelectBar");
         RadioButton ajoutparticipant = RadioButton.createToggle("+participant", barGroup);
        ajoutparticipant.setUIID("SelectBar");
        
    
        Label arrow = new Label(res.getImage("news-tab-down-arrow.png"), "Container");
        
        Publications.addActionListener((e) -> {
        InfiniteProgress ip = new InfiniteProgress();
        final Dialog ipD1g= ip.showInfiniteBlocking();
        new ListFormationAdmin(res).show();
     
        refreshTheme();
        
    });
         pub.addActionListener((e) -> {
        InfiniteProgress ip = new InfiniteProgress();
        final Dialog ipD1g= ip.showInfiniteBlocking();
        new ListPF(res).show();
     
        refreshTheme();
        
    });
         
           ajoutFormation.addActionListener((e) -> {
        InfiniteProgress ip = new InfiniteProgress();
        final Dialog ipD1g= ip.showInfiniteBlocking();
        new addformationform(res).show();
     
        refreshTheme();
        
    });
          ajoutparticipant.addActionListener((e) -> {
        InfiniteProgress ip = new InfiniteProgress();
        final Dialog ipD1g= ip.showInfiniteBlocking();
        new addParticFormAdmin(res).show();
     
        refreshTheme();
        
    });
        
        
        add(LayeredLayout.encloseIn(
                GridLayout.encloseIn(4,Publications,  pub,ajoutFormation,ajoutparticipant),
                FlowLayout.encloseBottom(arrow)
        ));
        
        pub.setSelected(true);
        arrow.setVisible(false);
        addShowListener(e -> {
            arrow.setVisible(true);
            updateArrowPosition(pub, arrow);
        });
        bindButtonSelection(Publications, arrow);
     bindButtonSelection(pub, arrow);
        bindButtonSelection(ajoutFormation, arrow);
           bindButtonSelection(ajoutparticipant, arrow);
      
       
       
        
        // special case for rotation
        addOrientationListener(e -> {
            updateArrowPosition(barGroup.getRadioButton(barGroup.getSelectedIndex()), arrow);
        });
    
    
    // appel affichage 
    ArrayList<ParticForm> list = ServiceParticForm.getInstance().afficherpf();
    
    for(ParticForm pf : list ) {
        
        
    String urlImage= ("dog.jpg");
    Image PlaceHolder= Image.createImage(120,90);
    EncodedImage enc=EncodedImage.createFromImage(PlaceHolder, false);
    URLImage urlim = URLImage.createToStorage(enc, urlImage, urlImage,URLImage.RESIZE_SCALE);
            
           
    addButton(urlim,pf,res);
    
    ScaleImageLabel image = new ScaleImageLabel(urlim);
    Container containerImg = new Container();
    image.setBackgroundType(Style.BACKGROUND_IMAGE_SCALED_FILL);
    
    
    }
    
    }
        
        
        
        
        
     private void addTab(Tabs swipe, Image img, Label spacer, String likesStr, String commentsStr, String text) {
        int size = Math.min(Display.getInstance().getDisplayWidth(), Display.getInstance().getDisplayHeight());
        if(img.getHeight() < size) {
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
        if(img.getHeight() > Display.getInstance().getDisplayHeight() / 2) {
            img = img.scaledHeight(Display.getInstance().getDisplayHeight() / 2);
        }
        ScaleImageLabel image = new ScaleImageLabel(img);
        image.setUIID("Container");
        image.setBackgroundType(Style.BACKGROUND_IMAGE_SCALED_FILL);
        Label overlay = new Label(" ", "ImageOverlay");
        
        Container page1 = 
            LayeredLayout.encloseIn(
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
        
        public void bindButtonSelection(Button btn,Label l){
        
        
        btn.addActionListener(e ->  {
        
        if(btn.isSelected()) {
        
        updateArrowPosition(btn,l);
        
        }
        
        
        });
        }
        
        
        
        
        private void updateArrowPosition(Button btn, Label l) {
        l.getUnselectedStyle().setMargin(LEFT,btn.getX() + btn.getWidth() / 2 - l.getWidth() / 2  );
        l.getParent().repaint();
        
        
        
        }
        private void addButton(Image img,ParticForm pf,Resources res) {
        int height =Display.getInstance().convertToPixels(11.5f);
        
        int width =Display.getInstance().convertToPixels(14f);
        
        Button image = new Button(img.fill(width,height));
        image.setUIID("Label");
        Container cnt = BorderLayout.west(image);
        
        Label nomText = new Label ("nom:"+pf.getNom(),"NewsTopLine2");
        Label prenomText = new Label ("prenom:"+pf.getPrenom(),"NewsTopLine2");
        Label ageText = new Label ("age:"+pf.getAge(),"NewsTopLine2");
        Label expText = new Label ("exp:"+pf.getExp(),"NewsTopLine2");
        Label so_domaineText = new Label ("so_domaine:"+pf.getSo_domaine(),"NewsTopLine2");
        Label so_assText = new Label ("so_ass:"+pf.getSo_ass(),"NewsTopLine2");
        Label Numero = new Label ("Numero:"+pf.getNumero(),"NewsTopLine2");
        
        
        /*
        Label nomeqText = new Label ("NomEquipe:"+pf.getNomeq(),"NewsTopLine2");
        Label dureeText = new Label ("Duree:"+pf.getDuree(),"NewsTopLine2");
        Label domaineText = new Label ("Domaine:"+pf.getDomaine(),"NewsTopLine2");
        
        Label nomformText = new Label ("NomFormation:"+pf.getNomform(),"NewsTopLine2");
        Label planText = new Label ("Plan:"+pf.getPlan(),"NewsTopLine2");
        Label dateText = new Label ("Date:"+pf.getDate(),"NewsTopLine2");
        
        */
        
        
        
        // supprimer button
        
        Label lSupprimer= new Label(" ");
        lSupprimer.setUIID("NewsTopLine");
        Style supprimerStyle = new Style(lSupprimer.getUnselectedStyle());
        supprimerStyle.setFgColor(0xf21f1f);
        
        FontImage supprimerImage =FontImage.createMaterial(FontImage.MATERIAL_DELETE, supprimerStyle);
        lSupprimer.setIcon(supprimerImage);
        lSupprimer.setTextPosition(RIGHT);
        
        
        // appel service
        lSupprimer.addPointerPressedListener(l -> {
            
            Dialog dig = new Dialog("suppression ");
            if (dig.show("suppression","voulez-vous supprimer ce Participant?","Annuler","oui")){
                
                dig.dispose();
            }
            else {
                dig.dispose();
                
                if(ServiceParticForm.getInstance().deletepf(pf.getId()))  {
                    new ListPF(res).show();
                    
                }
            }
            
            
            
        });
        
        // update
        
        /*
        Label Participer= new Label(" ");
        Participer.setUIID("NewsTopLine");
        Style modifierStyle = new Style(Participer.getUnselectedStyle());
        modifierStyle.setFgColor(0xf7ad02);
        
        FontImage mFontImage=FontImage.createMaterial(FontImage.MATERIAL_MODE_EDIT, modifierStyle);
        Participer.setIcon(mFontImage);
        Participer.setTextPosition(LEFT);
        
        
        Participer.addPointerPressedListener(l -> {
        
        System.out.println("modifier");
        new addParticFormForm(res).show();
        
        */
        
        
        //update
        Label lModifier= new Label(" ");
        lModifier.setUIID("NewsTopLine");
        Style modifierStyle = new Style(lModifier.getUnselectedStyle());
        modifierStyle.setFgColor(0xf7ad02);
        
        FontImage mFontImage=FontImage.createMaterial(FontImage.MATERIAL_MODE_EDIT, modifierStyle);
        lModifier.setIcon(mFontImage);
        lModifier.setTextPosition(LEFT);
        
        // appel service modifier
        lModifier.addPointerPressedListener(l -> {
            
            //System.out.println("modifier");
          new ModifierPFForm(res,pf).show();
            
            
            
        });
        
        
        
        
        
        
        
        
        
        cnt.add(BorderLayout.CENTER,BoxLayout.encloseY
                                (BoxLayout.encloseX(nomText,lModifier,lSupprimer),
                                        BoxLayout.encloseX(prenomText),
                                        BoxLayout.encloseX(ageText),
                                        BoxLayout.encloseX(expText),
                                        /* BoxLayout.encloseX(so_domaineText),
                                        BoxLayout.encloseX(so_assText),*/
                                         BoxLayout.encloseX(Numero)
                                ));
        
        
        
        
        
        
        
        
        
        
        add(cnt);
    }
    
    } 