package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;

	private static final String FIRST_NAME = "Miguel";
	private static final String LAST_NAME  = "Canessa";
	private static final String PASSWORD   = "testPass!";

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	public void testAuthorisedEndpoint() {
		driver.get("http://localhost:" + this.port + "/signup");
		assertEquals("Sign Up", driver.getTitle());

		driver.get("http://localhost:" + this.port + "/login");
		assertEquals("Login", driver.getTitle());

		driver.get("http://localhost:" + this.port + "/home");
		assertFalse("Home" == driver.getTitle());
	}

	@Test
	public void testSignupLoginFlow()  throws InterruptedException{
		String username = performSignup();
		Thread.sleep(2000);

		assertEquals("Login", driver.getTitle());

		performLogin(username, false);
		Thread.sleep(2000);

		assertEquals("Home", driver.getTitle());

		Thread.sleep(2000);
		HomePage homePage = new HomePage(driver);
		homePage.performLogout();

		assertEquals("Login", driver.getTitle());

		Thread.sleep(2000);
		driver.get("http://localhost:" + this.port + "/home");

		assertFalse(driver.getTitle() == "Home");

	}

	@Test
	public void testNoteCreation() throws InterruptedException{
		String noteTitle = "Note title";
		String noteDescription = "Note description";
		createNewUserAndGoHome();

		HomePage homePage = new HomePage(driver);

		Thread.sleep(2000);
		assertEquals("Home", driver.getTitle());

		createNewNote(homePage, noteTitle, noteDescription);

		Thread.sleep(2000);

		Note note = homePage.getFirstNote();

		Assertions.assertEquals(noteTitle, note.getNotetitle());
		Assertions.assertEquals(noteDescription, note.getNotedescription());

		homePage.performLogout();
		assertEquals("Login", driver.getTitle());
	}

	@Test
	public void testNoteUpdating() throws InterruptedException{
		String noteTitle = "Note title";
		String noteDescription = "Note description";
		createNewUserAndGoHome();

		HomePage homePage = new HomePage(driver);

		Thread.sleep(2000);
		assertEquals("Home", driver.getTitle());

		createNewNote(homePage, noteTitle, noteDescription);

		Thread.sleep(2000);

		String noteTitle_modified = "Note title v2";
		String noteDescription_modified = "Note description v2";

		udpateNote(homePage, noteTitle_modified, noteDescription_modified);

		Thread.sleep(2000);

		Note note = homePage.getFirstNote();

		Assertions.assertEquals(noteTitle_modified, note.getNotetitle());
		Assertions.assertEquals(noteDescription_modified, note.getNotedescription());

		homePage.performLogout();
		assertEquals("Login", driver.getTitle());
	}

	@Test
	public void testNoteDeleting() throws InterruptedException{
		String noteTitle = "Note title";
		String noteDescription = "Note description";
		createNewUserAndGoHome();

		HomePage homePage = new HomePage(driver);

		Thread.sleep(2000);
		assertEquals("Home", driver.getTitle());

		createNewNote(homePage, noteTitle, noteDescription);

		Thread.sleep(2000);
		deleteNote(homePage);

		Thread.sleep(2000);

		Note note = homePage.getFirstNote();

		Assertions.assertTrue(note == null);

		homePage.performLogout();
		assertEquals("Login", driver.getTitle());
	}

	@Test
	public void testCredentialCreation() throws InterruptedException{
		String credentialURL = "Credential url";
		String credentialUsername = "Credential username";
		String credentialPassword = "Credential password";
		createNewUserAndGoHome();

		HomePage homePage = new HomePage(driver);

		Thread.sleep(2000);
		assertEquals("Home", driver.getTitle());

		createNewCredential(homePage, credentialURL, credentialUsername, credentialPassword);

		Thread.sleep(2000);

		Credential credential = homePage.getFirstCredential();

		Assertions.assertEquals(credentialURL, credential.getUrl());
		Assertions.assertEquals(credentialUsername, credential.getUsername());

		homePage.performLogout();
		assertEquals("Login", driver.getTitle());
	}

	@Test
	public void testCredentialUpdating() throws InterruptedException{
		String credentialURL = "Credential url";
		String credentialUsername = "Credential username";
		String credentialPassword = "Credential password";
		createNewUserAndGoHome();

		HomePage homePage = new HomePage(driver);

		Thread.sleep(2000);
		assertEquals("Home", driver.getTitle());

		createNewCredential(homePage, credentialURL, credentialUsername, credentialPassword);

		Thread.sleep(2000);

		String credentialURL_modified = "Credential url v2";
		String credentialUsername_modified = "Credential username v2";
		String credentialPassword_modified = "Credential password v2";

		udpateCredential(homePage, credentialURL_modified, credentialUsername_modified, credentialPassword_modified);

		Thread.sleep(2000);

		Credential credential = homePage.getFirstCredential();

		Assertions.assertEquals(credentialURL_modified, credential.getUrl());
		Assertions.assertEquals(credentialUsername_modified, credential.getUsername());

		homePage.performLogout();
		assertEquals("Login", driver.getTitle());
	}

	@Test
	public void testCredentialDeleting() throws InterruptedException{
		String credentialURL = "Credential url";
		String credentialUsername = "Credential username";
		String credentialPassword = "Credential password";
		createNewUserAndGoHome();

		HomePage homePage = new HomePage(driver);

		Thread.sleep(2000);
		assertEquals("Home", driver.getTitle());

		createNewCredential(homePage, credentialURL, credentialUsername, credentialPassword);

		Thread.sleep(2000);
		deleteCredential(homePage);

		Thread.sleep(2000);

		Credential credential = homePage.getFirstCredential();

		Assertions.assertTrue(credential == null);

		homePage.performLogout();
		assertEquals("Login", driver.getTitle());
	}

	private void deleteNote(HomePage homePage) throws InterruptedException{
		homePage.openNoteTab();Thread.sleep(2000);
		homePage.clickDeleteNoteButton();Thread.sleep(2000);
	}

	private void udpateNote(HomePage homePage, String title, String description) throws InterruptedException{
		homePage.openNoteTab();Thread.sleep(2000);
		homePage.clickEditNoteButton();Thread.sleep(2000);
		homePage.setNoteTitle(title);
		homePage.setNodeDescription(description);
		homePage.clickSaveButtonNewNote();
		homePage.openNoteTab();
	}

	private void createNewNote(HomePage homePage, String title, String description) throws InterruptedException{
		homePage.openNoteTab();Thread.sleep(2000);
		homePage.clickNewNoteButton();Thread.sleep(2000);
		homePage.setNoteTitle(title);
		homePage.setNodeDescription(description);
		homePage.clickSaveButtonNewNote();
		homePage.openNoteTab();
	}

	private void deleteCredential(HomePage homePage) throws InterruptedException{
		homePage.openCredentialTab();Thread.sleep(2000);
		homePage.clickDeleteCredentialButton();Thread.sleep(2000);
	}

	private void udpateCredential(HomePage homePage, String url, String username, String password) throws InterruptedException{
		homePage.openCredentialTab();Thread.sleep(2000);
		homePage.clickEditCredentialButton();Thread.sleep(2000);
		homePage.setCredentialURL(url);
		homePage.setCredentialUsername(username);
		homePage.setCredentialPassword(password);
		homePage.clickSaveButtonNewCredential();
		homePage.openCredentialTab();
	}

	private void createNewCredential(HomePage homePage, String url, String username, String password) throws InterruptedException{
		homePage.openCredentialTab();Thread.sleep(2000);
		homePage.clickNewCredentialButton();Thread.sleep(2000);
		homePage.setCredentialURL(url);
		homePage.setCredentialUsername(username);
		homePage.setCredentialPassword(password);
		homePage.clickSaveButtonNewCredential();
		homePage.openCredentialTab();
	}

	public String performSignup(){
		String username = "mcanessa_"+ System.currentTimeMillis();
		driver.get("http://localhost:" + this.port + "/signup");
		SignupPage signupPage = new SignupPage(driver);
		signupPage.performSignUp(FIRST_NAME, LAST_NAME, username, PASSWORD);

		return username;
	}

	public void performLogin(String username, boolean forceUrl){

		if(forceUrl)
			driver.get("http://localhost:" + this.port + "/login");

		LoginPage loginPage = new LoginPage(driver);
		loginPage.performLogin(username, PASSWORD);
	}

	public void  createNewUserAndGoHome() throws InterruptedException{
		String username = performSignup();
		Thread.sleep(2000);
		performLogin(username, false);
		Thread.sleep(2000);
	}

}
