package br.com.sitedoph.bitwiseopsandmask;

import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BitwiseStatuses {

    // Define masks for lowest level statuses as constants which
    // are powers of 2. This ensures that only one bit in an integer
    // is set to 1.  Make these private because combinations will be
    // used to publicly access valid status sets.
    static final int NOT_COVERABLE = 4;
    static final int COVERABLE = 16;
    static final int NOT_COVERED = 32;
    static final int COVERED = 256;
    // Obtain valid status sets by Bitwise ORing lower level statuses
    static final int INITIAL_STATUS = 0;

    /* Valid status sets
    - No status
    - Not Coverable
    - Coverable Not Covered
    - Coverable Covered
    */
    static final int COVERABLE_NOT_COVERED = COVERABLE | NOT_COVERED;
    static final int COVERABLE_COVERED = COVERABLE | COVERED;
    private static final Logger log = LoggerFactory.getLogger(BitwiseStatuses.class);
    // Or, alternately
    //public static final int ALL_ALLOWED = VIEW_ADD_EDIT_ALLOWED | COVERED;
    private static final int[] validStatuses = {
            NOT_COVERABLE,
            COVERABLE_NOT_COVERED,
            COVERABLE_COVERED,
    };

    static {
        // Sort needed to later use binarySearch() method
        Arrays.sort(validStatuses);
        for (int i = 0; i < validStatuses.length; i++) {
            log.info("Valid status:" + printAsBinary(validStatuses[i]));
        }
    }

    // Check status(s)
    private static boolean isPermitted(int myStatuses, int statusToCheck) {
        return ((myStatuses & statusToCheck) == statusToCheck);
    }

    /* Public setter methods to make sure that only valid
     * status sets can be assigned */
    public static int addStatus(int myStatuses, int statusToAdd) throws DBSecurityException {
        return addStatus(myStatuses, new int[]{statusToAdd});
    }

    static int addStatus(int myStatuses, int[] statusesToAdd) throws DBSecurityException {
        log.info("BEFORE Statuses:" + printAsBinary(myStatuses));
        for (int aStatusesToAdd : statusesToAdd) {
            log.info("Add (|)     status:" + printAsBinary(aStatusesToAdd));
            myStatuses |= aStatusesToAdd;
        }
        return checkAndPrint(myStatuses);
    }

    public static int deleteStatus(int myStatuses, int statusToDelete) throws DBSecurityException {
        return deleteStatuses(myStatuses, new int[]{statusToDelete});
    }

    static int deleteStatuses(int myStatuses, int[] statusesToDelete) throws DBSecurityException {
        log.info("BEFORE Statuses:" + printAsBinary(myStatuses));
        for (int aStatusesToDelete : statusesToDelete) {
            log.info("Delete (& ~)  status:" + printAsBinary(aStatusesToDelete));
            log.info("~aStatusesToDelete: " + printAsBinary(~aStatusesToDelete));
            myStatuses &= ~aStatusesToDelete;
        }
        return checkAndPrint(myStatuses);
    }

    private static int checkAndPrint(int myStatuses) throws DBSecurityException {
        if (Arrays.binarySearch(validStatuses, myStatuses) < 0) {
            throw new DBSecurityException("Resulting status set will be invalid.  Aborted.");
        } else {
            log.info("AFTER  Statuses:" + printAsBinary(myStatuses) + "\n");
            return myStatuses;
        }
    }

    // Toggle status(s) - off if on, on if off - RARELY USED
    public static int toggleStatus(int myStatuses, int statusToToggle) throws DBSecurityException {
        myStatuses ^= statusToToggle;
        if (Arrays.binarySearch(validStatuses, myStatuses) < 0) {
            throw new DBSecurityException("Resulting status set will be invalid.  Aborted.");
        } else {
            return myStatuses;
        }
    }

    // Convert an int to a string displaying int as binary
    private static String printAsBinary(int i) {
        log.debug("incoming   = " + i);
        StringBuilder sb = new StringBuilder();
        if (isPermitted(i, NOT_COVERABLE)) {
            sb.append("NCA");
        } else {
            sb.append(' ');
        }
        if (isPermitted(i, COVERABLE)) {
            sb.append("CA");
        } else {
            sb.append(' ');
        }
        if (isPermitted(i, NOT_COVERED)) {
            sb.append("NCD");
        } else {
            sb.append(' ');
        }
        if (isPermitted(i, COVERED)) {
            sb.append("CD");
        } else {
            sb.append(' ');
        }

        String s = Integer.toString(i, 2);

        String pattern = "................................";

        log.debug("pattern    = " + pattern);

        String temp1 = pattern.substring(0, pattern.length() - s.length());

        String temp2 = temp1 + s;

        log.debug("converted  = " + temp2);

        temp2 = temp2.replace('0', '.');

        sb.append("=" + temp2);

        log.debug("returned   = " + sb.toString());

        return sb.toString();
    }


}
