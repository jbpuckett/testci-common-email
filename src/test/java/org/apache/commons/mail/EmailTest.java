package org.apache.commons.mail;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for testing the Email class functionalities.
 */
public class EmailTest {

    // Test data for email addresses
    private static final String[] TEST_EMAILS = {"ab@bccom", "a.b@c.org", "abcdefghijklmnopqrst@abcdefghijklmnopqrst.com.bd"};

    // Instance of the concrete Email class for testing
    private EmailConcrete email;

    /**
     * Setup method executed before each test case.
     * Initializes the EmailConcrete instance.
     *
     * @throws Exception If an exception occurs during setup.
     */
    @Before
    public void setUpEmailTest() throws Exception {
        email = new EmailConcrete();
    }

    /**
     * Teardown method executed after each test case.
     * Sets the EmailConcrete instance to null.
     *
     * @throws Exception If an exception occurs during teardown.
     */
    @After
    public void tearDownEmailTest() throws Exception {
        email = null;
    }

    /**
     * Test method for the addBcc() function of the EmailConcrete class.
     * Verifies that the specified email addresses are added to the BCC list.
     *
     * @throws Exception If an exception occurs during the test.
     */
    @Test
    public void testAddBcc() throws Exception {
        email.addBcc(TEST_EMAILS);
        assertEquals(3, email.getBccAddresses().size());
    }

    /**
     * Test method for the addCc() function of the EmailConcrete class.
     * Verifies that the specified email address is added to the CC list.
     *
     * @throws Exception If an exception occurs during the test.
     */
    @Test
    public void testAddCc() throws Exception {
        email.addCc("test@example.com");
        assertEquals(1, email.getCcAddresses().size());
    }

    /**
     * Test case to ensure that a header is added to the headers Map.
     */
    @Test
    public void testAddHeader_ValidInput() throws EmailException {
        email.addHeader("X-Test-Header", "Test Value");
        assertEquals("Test Value", email.getHeaders().get("X-Test-Header"));
    }

    /**
     * Test case to ensure that an IllegalArgumentException is thrown
     * when name is null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddHeader_NullName() throws EmailException {
        email.addHeader(null, "Test Value");
    }

    /**
     * Test case to ensure that an IllegalArgumentException is thrown
     * when value is null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddHeader_NullValue() throws EmailException {
        email.addHeader("X-Test-Header", null);
    }

    /**
     * Test case to ensure that an IllegalArgumentException is thrown
     * when name is empty.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddHeader_EmptyName() throws EmailException {
        email.addHeader("", "Test Value");
    }

    /**
     * Test case to ensure that an IllegalArgumentException is thrown
     * when value is empty.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddHeader_EmptyValue() throws EmailException {
        email.addHeader("X-Test-Header", "");
    }

    /**
    * Test case to verify the functionality of adding a reply-to address
    * */
    @Test
    public void testAddReplyTo() throws Exception {
        // Test case to verify the functionality of adding a reply-to address
        
        // Adding a reply-to address with the email "reply@example.com" and the personal name "John Doe"
        email.addReplyTo("reply@example.com", "John Doe");
        
        // Asserting that the first reply-to address added matches the email "reply@example.com"
        assertEquals("reply@example.com", email.getReplyToAddresses().get(0).getAddress());
        
        // Asserting that the personal name associated with the first reply-to address is "John Doe"
        assertEquals("John Doe", email.getReplyToAddresses().get(0).getPersonal());
    }

    @Test
    public void testBuildMimeMessage_NotNull() throws Exception {
        // Set up required properties
        email.setHostName("localhost");
        email.setFrom("from@example.com");
        email.addTo("to@example.com");
        email.setSubject("Test Subject");
        email.setMsg("Test Message");

        // Call buildMimeMessage
        email.buildMimeMessage();

        // Assert that MimeMessage is not null
        assertNotNull(email.getMimeMessage());
    }

    @Test
    public void testBuildMimeMessage_Subject() throws Exception {
        // Set up required properties
        email.setHostName("localhost");
        email.setFrom("from@example.com");
        email.addTo("to@example.com");
        email.setSubject("Test Subject");
        email.setMsg("Test Message");

        // Call buildMimeMessage
        email.buildMimeMessage();

        // Assert that the subject of MimeMessage is as expected
        assertEquals("Test Subject", email.getMimeMessage().getSubject());
    }

    @Test
    public void testBuildMimeMessage_FromAddress() throws Exception {
        // Set up required properties
        email.setHostName("localhost");
        email.setFrom("from@example.com");
        email.addTo("to@example.com");
        email.setSubject("Test Subject");
        email.setMsg("Test Message");

        // Call buildMimeMessage
        email.buildMimeMessage();

        // Assert that the from address of MimeMessage is as expected
        assertEquals("from@example.com", email.getMimeMessage().getFrom()[0].toString());
    }

    @Test
    public void testBuildMimeMessage_Recipient() throws Exception {
        // Set up required properties
        email.setHostName("localhost");
        email.setFrom("from@example.com");
        email.addTo("to@example.com");
        email.setSubject("Test Subject");
        email.setMsg("Test Message");

        // Call buildMimeMessage
        email.buildMimeMessage();

        // Assert that the recipient address of MimeMessage is as expected
        assertEquals("to@example.com", email.getMimeMessage().getAllRecipients()[0].toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildMimeMessage_AlreadyBuilt() throws Exception {
        // Set up required properties
        email.setHostName("localhost");
        email.setFrom("from@example.com");
        email.addTo("to@example.com");
        email.setSubject("Test Subject");
        email.setMsg("Test Message");

        // Call buildMimeMessage twice
        email.buildMimeMessage();
        email.buildMimeMessage(); // This should throw IllegalStateException
    }

    /**
     * Test case to cover the case where session is not null.
     */
    @Test
    public void testGetHostName_SessionNotNull() {
        // Set up a mock session with a property
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.example.com");
        email.setMailSession(Session.getInstance(properties));

        assertEquals("smtp.example.com", email.getHostName());
    }

    /**
     * Test case to cover the case where hostName is not empty.
     */
    @Test
    public void testGetHostName_HostNameNotEmpty() {
        email.setHostName("smtp.example.com");
        assertEquals("smtp.example.com", email.getHostName());
    }

    /**
     * Test case to cover the case where both session and hostName are null or empty.
     */
    @Test
    public void testGetHostName_NullOrEmpty() {
        assertNull(email.getHostName());
    }

    /**
     * Test case to cover the case where session is not null.
     */
    @Test
    public void testGetMailSession_SessionNotNull() throws EmailException {
        // Set up existing session
        Session session = Session.getDefaultInstance(new Properties());
        email.setMailSession(session);

        // Ensure that existing session is returned
        assertEquals(session, email.getMailSession());
    }

    /**
     * Test case to cover the case where session is null.
     */
    @Test
    public void testGetMailSession_SessionNull() throws EmailException {
        // Set up required properties
        email.setHostName("smtp.example.com");
        email.setSmtpPort(25);
        email.setSocketTimeout(5000);
        email.setSocketConnectionTimeout(3000);

        // Ensure that a new session is created
        assertNotNull(email.getMailSession());
    }
    
    @Test
    public void testGetMailSessionWithNull() throws EmailException {
    	email.setHostName("localhost");
    	email.setSSLOnConnect(true);
    	Session session=email.getMailSession();
    }

    @Test
    public void testGetSentDate_NullSentDate() {
        email.setSentDate(null);
        assertNotNull(email.getSentDate()); // Ensure that a default date is returned
    }

    /**
     * Test case to cover the case where sentDate is not null.
     */
    @Test
    public void testGetSentDate_NotNullSentDate() {
        Date date = new Date();
        email.setSentDate(date);
        assertEquals(date, email.getSentDate());
    }

    /**
     * Test for getSocketConnectionTimeout method, setting timeout to 5000 milliseconds.
     */
    @Test
    public void testGetSocketConnectionTimeout() {
        // Setting the socket connection timeout value to 5000 milliseconds
        email.setSocketConnectionTimeout(5000);
        // Asserting the returned timeout value is as expected
        assertEquals(5000, email.getSocketConnectionTimeout());
    }

    /**
     * Test for setFrom method, setting "From" address to "from@example.com".
     */
    @Test
    public void testSetFrom() throws Exception {
        // Setting the "From" address of the email to "from@example.com"
        email.setFrom("from@example.com");
        // Asserting the returned "From" address is as expected
        assertEquals("from@example.com", email.getFromAddress().getAddress());
    }
}


