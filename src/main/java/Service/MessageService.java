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
     * @return ArrayList<Message> a list of the retrieved messages.
     */
    public List<Message> getAllMessages() {
        List<Message> messages = messageDAO.getAllMessages();
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
}
