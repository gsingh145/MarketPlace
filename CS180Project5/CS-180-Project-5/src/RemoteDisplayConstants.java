import javax.swing.*;

/**
 * RemoteDisplayConstants
 * <p>
 * Shares important constants to server and client
 *
 * @author Isaac, Cynthia, Gaurav, Hayden, Albert
 * @version 12/12/22
 */

public class RemoteDisplayConstants {
    //
    // Return values.
    //
    /**
     * Return value from class method if YES is chosen.
     */
    public static final int YES_OPTION = JOptionPane.YES_OPTION;
    /**
     * Return value from class method if NO is chosen.
     */
    public static final int NO_OPTION = JOptionPane.NO_OPTION;
    /**
     * Return value from class method if CANCEL is chosen.
     */
    public static final int CANCEL_OPTION = JOptionPane.CANCEL_OPTION;
    /**
     * Return value form class method if OK is chosen.
     */
    public static final int OK_OPTION = JOptionPane.OK_OPTION;
    /**
     * Return value from class method if user closes window without selecting
     * anything, more than likely this should be treated as either a
     * <code>CANCEL_OPTION</code> or <code>NO_OPTION</code>.
     */
    public static final int CLOSED_OPTION = JOptionPane.CLOSED_OPTION;

    //
    // Message types. Used by the UI to determine what icon to display,
    // and possibly what behavior to give based on the type.
    //

    /**
     * Used for error messages.
     */
    public static final int ERROR_MESSAGE = JOptionPane.ERROR_MESSAGE;
    /**
     * Used for information messages.
     */
    public static final int INFORMATION_MESSAGE = JOptionPane.INFORMATION_MESSAGE;
    /**
     * Used for warning messages.
     */
    public static final int WARNING_MESSAGE = JOptionPane.WARNING_MESSAGE;
    /**
     * Used for questions.
     */
    public static final int QUESTION_MESSAGE = JOptionPane.QUESTION_MESSAGE;
    /**
     * No icon is used.
     */
    public static final int PLAIN_MESSAGE = JOptionPane.PLAIN_MESSAGE;
}
