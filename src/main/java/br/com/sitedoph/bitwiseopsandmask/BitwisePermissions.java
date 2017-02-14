package br.com.sitedoph.bitwiseopsandmask;

import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BitwisePermissions {

    private static final Logger log = LoggerFactory.getLogger(BitwisePermissions.class);

    // Define masks for lowest level permissions as constants which
    // are powers of 2. This ensures that only one bit in an integer
    // is set to 1.  Make these private because combinations will be
    // used to publicly access valid permission sets.
    static final int VIEW = 4;
    static final int ADD = 16;
    static final int EDIT = 32;
    static final int DELETE = 256;

    /* Valid permission sets
    - No permission to do anything
    - View Only
    - View and Add
    - View and Edit
    - View, Add and Edit
    - All permissions (View, Add, Edit and Delete)
    */

    // Obtain valid permission sets by Bitwise ORing lower level permissions
    public static final int NOTHING_ALLOWED = 0;
    public static final int VIEW_ALLOWED = VIEW;
    public static final int VIEW_ADD_ALLOWED = VIEW | ADD;
    public static final int VIEW_EDIT_ALLOWED = VIEW | EDIT;
    public static final int VIEW_ADD_EDIT_ALLOWED = VIEW | ADD | EDIT;
    public static final int ALL_ALLOWED = VIEW | ADD | EDIT | DELETE;
    // Or, alternately
    //public static final int ALL_ALLOWED = VIEW_ADD_EDIT_ALLOWED | DELETE;

    public static final int[] validPermissions = {
            NOTHING_ALLOWED,
            VIEW_ALLOWED,
            VIEW_ADD_ALLOWED,
            VIEW_EDIT_ALLOWED,
            VIEW_ADD_EDIT_ALLOWED,
            ALL_ALLOWED,
    };

    static {
        // Sort needed to later use binarySearch() method
        Arrays.sort(validPermissions);
        for (int i = 0; i < validPermissions.length; i++) {
            if (log.isInfoEnabled()) {
                log.info("Valid permission:" +
                        printAsBinary(validPermissions[i]));
            }
        }
    }

    // Check permission(s)
    public static boolean isPermitted(
            int myPermissions,
            int permissionToCheck
    ) {
        return ((myPermissions & permissionToCheck) == permissionToCheck);
    }





    /* Public setter methods to make sure that only valid
     * permission sets can be assigned */

    public static int addPermission(int myPermissions, int permissionToAdd)
            throws DBSecurityException {

        return addPermissions(myPermissions,
                new int[]{permissionToAdd});

    }


    public static int addPermissions(int myPermissions, int[] permissionsToAdd)
            throws DBSecurityException {

        if (log.isInfoEnabled()) {
            log.info("BEFORE Permissions:" + printAsBinary(myPermissions));
        }
        for (int i = 0; i < permissionsToAdd.length; i++) {
            if (log.isInfoEnabled()) {
                log.info("Add     permission:" +
                        printAsBinary(permissionsToAdd[i]));
            }
            myPermissions |= permissionsToAdd[i];
        }

        return checkAndPrint(myPermissions);

    }


    public static int deletePermission(
            int myPermissions,
            int permissionToDelete
    ) throws DBSecurityException {
        return deletePermissions(myPermissions,
                new int[]{permissionToDelete});
    }


    public static int deletePermissions(
            int myPermissions,
            int[] permissionsToDelete
    ) throws DBSecurityException {

        if (log.isInfoEnabled()) {
            log.info("BEFORE Permissions:" + printAsBinary(myPermissions));
        }
        for (int i = 0; i < permissionsToDelete.length; i++) {
            if (log.isInfoEnabled()) {
                log.info("Delete  permission:" +
                        printAsBinary(permissionsToDelete[i]));
            }
            myPermissions &= ~permissionsToDelete[i];
        }

        return checkAndPrint(myPermissions);

    }

    private static int checkAndPrint(int myPermissions) throws DBSecurityException {
        if (Arrays.binarySearch(validPermissions, myPermissions) < 0) {
            throw new DBSecurityException(
                    "Resulting permission set will be invalid.  Aborted.");
        } else {
            if (log.isInfoEnabled()) {
                log.info("AFTER  Permissions:" +
                        printAsBinary(myPermissions) + "\n");
            }
            return myPermissions;
        }
    }


    // Toggle permission(s) - off if on, on if off - RARELY USED
    public static int togglePermission(
            int myPermissions,
            int permissionToToggle
    ) throws DBSecurityException {

        myPermissions ^= permissionToToggle;

        if (Arrays.binarySearch(validPermissions, myPermissions) < 0) {
            throw new DBSecurityException(
                    "Resulting permission set will be invalid.  Aborted.");
        } else {
            return myPermissions;
        }

    }


    // Convert an int to a string displaying int as binary
    private static String printAsBinary(int i) {

        if (log.isDebugEnabled()) {
            log.debug("incoming   = " + i);
        }

        StringBuffer sb = new StringBuffer();
        if (isPermitted(i, VIEW)) {
            sb.append('V');
        } else {
            sb.append(' ');
        }
        if (isPermitted(i, ADD)) {
            sb.append('A');
        } else {
            sb.append(' ');
        }
        if (isPermitted(i, EDIT)) {
            sb.append('E');
        } else {
            sb.append(' ');
        }
        if (isPermitted(i, DELETE)) {
            sb.append('D');
        } else {
            sb.append(' ');
        }

        String s = Integer.toString(i, 2);

        String pattern = "................................";

        if (log.isDebugEnabled()) {
            log.debug("pattern    = " + pattern);
        }

        String temp1 = pattern.substring(0, pattern.length() - s.length());

        String temp2 = temp1 + s;

        if (log.isDebugEnabled()) {
            log.debug("converted  = " + temp2);
        }

        temp2 = temp2.replace('0', '.');

        sb.append("=" + temp2);

        if (log.isDebugEnabled()) {
            log.debug("returned   = " + sb.toString());
        }
        return sb.toString();
    }


}
