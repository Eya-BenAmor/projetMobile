/*
 * Copyright (c) 2016, Codename One
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated 
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation 
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, 
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions 
 * of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A 
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT 
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF 
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE 
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE. 
 */

package com.mycompany.myapp.gui;

import com.mycompany.myapp.gui.NewsfeedForm;
import com.mycompany.myapp.gui.BaseForm;
import com.codename1.components.FloatingHint;
import com.codename1.ui.Button;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.Label;
import com.codename1.ui.TextField;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.util.Resources;
import com.mycompany.myapp.services.ServiceAdmin;
import com.mycompany.myapp.services.ServiceUser;


/**
 * Sign in UI
 *
 * @author Shai Almog
 */
public class SignInForm extends BaseForm {

    public SignInForm(Resources res) {
        super(new BorderLayout());
        
        if(!Display.getInstance().isTablet()) {
            BorderLayout bl = (BorderLayout)getLayout();
            bl.defineLandscapeSwap(BorderLayout.NORTH, BorderLayout.EAST);
            bl.defineLandscapeSwap(BorderLayout.SOUTH, BorderLayout.CENTER);
        }
        getTitleArea().setUIID("Container");
        setUIID("SignIn");
        
        add(BorderLayout.NORTH, new Label(res.getImage("Logo.png"), "LogoLabel"));  
       
        TextField username = new TextField("", "Email", 20, TextField.ANY);
        TextField password = new TextField("", "Mot de passe", 20, TextField.PASSWORD);
        username.setSingleLineTextArea(false);
        password.setSingleLineTextArea(false);
        Button signIn = new Button("Connecter");
        Button signUp = new Button("Inscrire");
        signUp.addActionListener(e -> new SignUpForm(res).show());
        signUp.setUIID("Link");
        Label doneHaveAnAccount = new Label("Vous n'avez pas un compte?");
         String email =username.getText().toString();
        Container content = BoxLayout.encloseY(
                new FloatingHint(username),
                createLineSeparator(),
                new FloatingHint(password),
                createLineSeparator(),
                signIn,
                FlowLayout.encloseCenter(doneHaveAnAccount, signUp)
        );
        content.setScrollableY(true);
        add(BorderLayout.SOUTH, content);
        signIn.requestFocus();
       
       signIn.addActionListener(e -> {
           
         if ((ServiceAdmin.getInstance().getByEmailAdmin(username.getText().toString())).equals("Ok"))
            {   
            ServiceAdmin.getInstance().signin(username, password, res);
            }else if ((ServiceUser.getInstance().getByEmail(username.getText().toString())).equals("Ok"))
            {
              ServiceUser.getInstance().signin(username, password, res);
            }
         else
                  Dialog.show("Echec d'authentification","Username ou mot de passe eronné","OK",null);
         });
        
                
                
                
              
    }
    
}
