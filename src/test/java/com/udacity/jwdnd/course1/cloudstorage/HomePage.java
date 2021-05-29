package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage {

    @FindBy(id = "button_logout")
    WebElement button_logout;

    @FindBy(id = "nav-notes-tab")
    WebElement tab_note;

    @FindBy(id = "button_newNote")
    WebElement button_addNote;

    @FindBy(id = "note-title")
    WebElement input_noteTitle;

    @FindBy(id = "note-description")
    WebElement input_noteDescription;

    @FindBy(id = "button_savechanges")
    WebElement button_noteSubmit;

    @FindBy(xpath = "//*[@id=\"noteTable\"]/tbody/tr[1]/th/span")
    WebElement note_firstNoteTitle;

    @FindBy(xpath = "//*[@id=\"noteTable\"]/tbody/tr[1]/td[1]/span")
    WebElement note_firstNoteDescription;

    @FindBy(xpath = "//*[@id=\"noteTable\"]/tbody/tr[1]/td[2]/button")
    WebElement buttton_editNote;

    @FindBy(xpath = "//*[@id=\"noteTable\"]/tbody/tr/td[2]/a")
    WebElement button_deleteNote;

    @FindBy(xpath = "//*[@id=\"noteTable\"]/tbody")
    WebElement tableNotesBody;

    @FindBy(id = "nav-credentials-tab")
    WebElement tab_credentials;

    @FindBy(id = "button_newCredential")
    WebElement button_addCredential;

    @FindBy(id = "credential-url")
    WebElement input_credentialUrl;

    @FindBy(id = "credential-username")
    WebElement input_credentialUsername;

    @FindBy(id = "credential-password")
    WebElement input_credentialPassword;

    @FindBy(id = "button_savechanges2")
    WebElement button_CredentialSubmit;

    @FindBy(xpath = "//*[@id=\"credentialTable\"]/tbody/tr/th/span")
    WebElement note_firstCredentialURL;

    @FindBy(xpath = "//*[@id=\"credentialTable\"]/tbody/tr/td[1]/span")
    WebElement note_firstCredentialUsername;

    @FindBy(xpath = "//*[@id=\"credentialTable\"]/tbody/tr/td[2]/span")
    WebElement note_firstCredentialPassword;

    @FindBy(xpath = "//*[@id=\"credentialTable\"]/tbody/tr/td[3]/button")
    WebElement buttton_editCredential;

    @FindBy(xpath = "//*[@id=\"credentialTable\"]/tbody/tr/td[3]/a")
    WebElement button_deleteCredential;

    @FindBy(xpath = "//*[@id=\"credentialTable\"]/tbody")
    WebElement tableCredentialBody;


    public HomePage(WebDriver webDriver){
        PageFactory.initElements(webDriver, this);
    }

    public void clickDeleteCredentialButton(){
        button_deleteCredential.click();
    }

    public void clickEditCredentialButton(){
        buttton_editCredential.click();
    }

    public void openCredentialTab(){
        tab_credentials.click();
    }

    public void clickNewCredentialButton(){
        button_addCredential.click();
    }

    public void setCredentialURL(String url){
        input_credentialUrl.clear();
        input_credentialUrl.sendKeys(url);
    }

    public void setCredentialUsername(String username){
        input_credentialUsername.clear();
        input_credentialUsername.sendKeys(username);
    }

    public void setCredentialPassword(String password){
        input_credentialPassword.clear();
        input_credentialPassword.sendKeys(password);
    }

    public void clickSaveButtonNewCredential(){
        button_CredentialSubmit.click();
    }

    public Credential getFirstCredential(){
        if(!tableCredentialBody.getText().equals("")){
            String url = note_firstCredentialURL.getText();
            String username = note_firstCredentialUsername.getText();
            String password = note_firstCredentialPassword.getText();
            return new Credential(1, url, username, null, password, 1);
        }
        return null;
    }

    public void clickDeleteNoteButton(){
        button_deleteNote.click();
    }

    public void clickEditNoteButton(){
        buttton_editNote.click();
    }

    public void openNoteTab(){
        tab_note.click();
    }

    public void clickNewNoteButton(){
        button_addNote.click();
    }

    public void setNoteTitle(String title){
        input_noteTitle.clear();
        input_noteTitle.sendKeys(title);
    }

    public void setNodeDescription(String description){
        input_noteDescription.clear();
        input_noteDescription.sendKeys(description);
    }

    public void clickSaveButtonNewNote(){
        button_noteSubmit.click();
    }

    public Note getFirstNote(){
        if(!tableNotesBody.getText().equals("")){
            String title = note_firstNoteTitle.getText();
            String description = note_firstNoteDescription.getText();
            return new Note(1, title, description, 1);
        }
        return null;
    }

    void performLogout(){
        button_logout.click();
    }
}
