package Service;

import java.util.List;

import Model.Account;
import Model.Message;
import DAO.AccountDAO;

public class AccountService {
    AccountDAO accountDAO;

    /**
     * No-args constructor for an accountService to instantiate a plain accountDAO.
     */
    public AccountService() {
        accountDAO = new AccountDAO();
    }

    /**
     * Constructor for an accountService when an accountDAO is provided.
     * 
     * @param accountDAO
     */
    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    /**
     * Use the AccountDAO to add a new account to the database, given an Account object with a unique non-empty username and a password with 4 or more characters.
     *
     * @param account an object representing a new account.
     * @return Account the newly added account if the insert operation was successful, including the account_id.
     */
    public Account addAccount(Account account) {
        if (account.getPassword().length() < 4 || account.getUsername().length() == 0 || accountDAO.getAccount(account.getUsername()) != null) {
            return null;
        }
        return accountDAO.insertAccount(account);
    }

    /**
     * Use the AccountDAO to verify the existence of an account, given an Account object with a username and password pair that exists in the database.
     *
     * @param account an object representing an account to verify.
     * @return Account the verified account, including the account_id.
     */
    public Account verifyAccount(Account account) {
        return accountDAO.getAccount(account.getUsername(), account.getPassword());
    }

    /**
     * Use the AccountDAO to check that an account with the given account_id exists in the database (used by MessageService before attempting to insert a new message).
     *
     * @param account_id an int representing an account_id to check.
     * @return boolean true if account exists, otherwise false.
     */
    public boolean checkAccount(int account_id) {
        return accountDAO.getAccount(account_id) != null;
    }

    /**
     * Use MessageService to get a list of every message from the specified account.
     *
     * @param account_id the id of the account to lookup.
     * @return List<Message> the list of messages with posted_by equal to the specified account_id.
     */
    public List<Message> getAllMessagesFromAccount(int account_id) {
        MessageService messageService = new MessageService();
        return messageService.getAllMessagesPostedBy(account_id);
    }
}
