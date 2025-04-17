package DAO;

import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {

    /**
     * Retrieve all records in the message table.
     *
     * @return ArrayList<Message> a list of Message objects, corresponding to the retrieved records.
     */
    public ArrayList<Message> getAllMessages() {
        ArrayList<Message> arr = new ArrayList<Message>();
        Connection connection = ConnectionUtil.getConnection();
        try {
            Statement statement = connection.createStatement();

            ResultSet rs = statement.executeQuery("SELECT * FROM message");
            while (rs.next()) {
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"),
                        rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                arr.add(message);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return arr;
    }

    /**
     * Add a message record into the database which matches the fields contained in the Message object.
     *
     * @param message a Message object that does not contain an message_id.
     * @return Message the message object that was successfully inserted into the database.
     */
    public Message insertMessage(Message message) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)" ;
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());

            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if (pkeyResultSet.next()) {
                int generated_message_id = (int) pkeyResultSet.getLong(1);
                return new Message(generated_message_id, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
