package Controller;

import java.util.List;
import io.javalin.Javalin;
import io.javalin.http.Context;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

/**
 * You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    /**
     * No-args constructor for a socialMediaController to instantiate a plain accountService and messageService.
     */
    public SocialMediaController() {
        accountService = new AccountService();
        messageService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);
        app.post("register", this::registerHandler);
        app.post("login", this::loginHandler);
        app.post("messages", this::postMessageHandler);
        app.get("messages", this::getAllMessagesHandler);
        app.get("messages/{message_id}", this::getMessageByIdHandler);
        app.delete("messages/{message_id}", this::deleteMessageByIdHandler);
        app.patch("messages/{message_id}", this::updateMessageByIdHandler);
        app.get("accounts/{account_id}/messages", this::getAllMessagesFromAccountHandler);
        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    /**
     * Handler to post a new account.
     * The Jackson ObjectMapper will automatically convert the JSON body of the POST request into an Account object.
     * If accountService returns a null value, the API will return status code 400 (client error).
     * @param context the Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void registerHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account addedAccount = accountService.addAccount(account);
        if (addedAccount == null) {
            context.status(400);
        } else {
            context.json(addedAccount).status(200);
        }
    }

    /**
     * Handler to verify account credentials.
     * The Jackson ObjectMapper will automatically convert the JSON body of the POST request into an Account object.
     * If accountService returns a null value, the API will return status code 401 (unauthorized).
     * @param context the Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void loginHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account verifiedAccount = accountService.verifyAccount(account);
        if (verifiedAccount == null) {
            context.status(401);
        } else {
            context.json(verifiedAccount).status(200);
        }
    }

    /**
     * Handler to post a new message.
     * The Jackson ObjectMapper will automatically convert the JSON body of the POST request into an Message object.
     * If messageService returns a null value, the API will return status code 400 (client error).
     * @param context the Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void postMessageHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        Message addedMessage = messageService.addMessage(message);
        if (addedMessage == null) {
            context.status(400);
        } else {
            context.json(addedMessage).status(200);
        }
    }

    /**
     * Handler to retrieve all messages.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void getAllMessagesHandler(Context context) {
        List<Message> messages = messageService.getAllMessages();
        context.json(messages).status(200);
    }

    /**
     * Handler to retrieve a message by it's message_id.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void getMessageByIdHandler(Context context) {
        int message_id = Integer.valueOf(context.pathParam("message_id"));
        Message message = messageService.getMessageById(message_id);
        if (message != null) {
            context.json(message).status(200);
        }
    }

    /**
     * Handler to delete a message by it's message_id.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void deleteMessageByIdHandler(Context context) {
        int message_id = Integer.valueOf(context.pathParam("message_id"));
        Message message = messageService.deleteMessageById(message_id);
        if (message != null) {
            context.json(message).status(200);
        }
    }

    /**
     * Handler to patch a message by it's message_id to update the message's text.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException will be thrown if there is an issue parsing message_text from the JSON body of the request.
     */
    private void updateMessageByIdHandler(Context context) throws JsonProcessingException {
        int message_id = Integer.valueOf(context.pathParam("message_id"));
        ObjectMapper mapper = new ObjectMapper();
        String message_text = mapper.readTree(context.body()).get("message_text").asText();
        Message message = messageService.updateMessageById(message_id, message_text);
        if (message == null) {
            context.status(400);
        } else {
            context.json(message).status(200);
        }
    }

    /**
     * Handler to retrieve all messages from the specified account.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void getAllMessagesFromAccountHandler(Context context) {
        int account_id = Integer.valueOf(context.pathParam("account_id"));
        List<Message> messages = accountService.getAllMessagesFromAccount(account_id);
        context.json(messages).status(200);
    }
}
