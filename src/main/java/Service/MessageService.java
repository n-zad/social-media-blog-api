package Service;

import java.util.List;

import Model.Message;
import DAO.MessageDAO;

public class MessageService {
    MessageDAO messageDAO;

    /**
     * No-args constructor for a messageService to instantiate a plain messageDAO.
     */
    public MessageService() {
        messageDAO = new MessageDAO();
    }

    /**
     * Constructor for a messageService when a messageDAO is provided.
     * 
     * @param messageDAO
     */
    public MessageService(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }

    /**
     * Use the MessageDAO to retrieve every message in the database.
     *
     * @return List<Message> a list of the retrieved messages.
     */
    public List<Message> getAllMessages() {
        List<Message> messages = messageDAO.getAllMessages();
        return messages;
    }

    /**
     * Use the MessageDAO to retrieve a single message in the database by it's message_id.
     *
     * @return Message the retrieved message as a Message object.
     */
    public Message getMessageById(int message_id) {
        Message message = messageDAO.getMessageById(message_id);
        return message;
    }

    /**
     * Use the MessageDAO to retrieve all messages in the database posted by the given account_id.
     *
     * @return List<Message> a list of the retrieved messages.
     */
    public List<Message> getAllMessagesPostedBy(int account_id) {
        List<Message> messages = messageDAO.getAllMessagesPostedBy(account_id);
        return messages;
    }

    /**
     * Use the MessageDAO to add a new message to the database, given an Message object with a non-empty text (max 255 characters) that is connected to an existing user.
     *
     * @param message an object representing a new message.
     * @return Message the newly added message if the insert operation was successful, including the message_id.
     */
    public Message addMessage(Message message) {
        AccountService accountService = new AccountService();
        if (message.getMessage_text().length() == 0 || message.getMessage_text().length() > 255 || !accountService.checkAccount(message.getPosted_by())) {
            return null;
        }
        return messageDAO.insertMessage(message);
    }

    /**
     * Use the MessageDAO to update the message_text of a single message in the database by it's message_id.
     * The message_text must be non-empty and less than 256 characters, and a record with the given message_id must exist.
     *
     * @return Message the updated record as a Message object.
     */
    public Message updateMessageById(int message_id, String message_text) {
        if (message_text.length() == 0 || message_text.length() > 255 || getMessageById(message_id) == null) {
            return null;
        }
        Message message = messageDAO.updateMessageById(message_id, message_text);
        return message;
    }

    /**
     * Use the MessageDAO to delete a single message in the database by it's message_id.
     *
     * @return Message the deleted record as a Message object.
     */
    public Message deleteMessageById(int message_id) {
        Message message = messageDAO.deleteMessageById(message_id);
        return message;
    }
}
