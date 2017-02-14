package br.com.sitedoph.bitwiseopsandmask;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import junit.extensions.RepeatedTest;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BitwisePermissionsTest extends TestCase {

    private static Logger cat = LoggerFactory.getLogger(BitwisePermissionsTest.class);

    // Fixtures - Class fields to allow access from anywhere in class
    HashMap bitwiseUsers;
    HashMap booleanUsers;

    // Some free memory info
    Runtime rt = Runtime.getRuntime();

    private long tm, tm2;
    private long free4, free5;

    // Test statistics
    private static int n2 = 0;
    private static long timetaken1 = 0;
    private static long timetaken2 = 0;
    private static long freechange1 = 0;
    private static long freechange2 = 0;

    // Required constructor - takes method name to test as parameter
    public BitwisePermissionsTest(String name) {
        super(name);
    }

    // Add tests to run in order of running into the suite
    public static TestSuite suite() {

        // Shortcut to automatically add all testXXX methods
        //return new TestSuite(BitwisePermissionsTest.class);

        // Number of repetitions per test
        n2 = 1;

        TestSuite suite1 = new TestSuite();
        suite1.addTest(
                new RepeatedTest(
                        new BitwisePermissionsTest("testBitwiseOperations"), n2));

        TestSuite suite = new TestSuite();
        //suite.addTest(new BitwisePermissionsTest("testBitwiseOperations"));
        //suite.addTest(new BitwisePermissionsTest("testBooleanOperations"));
        suite.addTest(suite1);
        //TestResult result = suite.run();

        return suite;
    }


    public static void main(String[] args) {

        // Initialize static fields
        timetaken1 = 0;
        timetaken2 = 0;
        freechange1 = 0;
        freechange2 = 0;

        // Run the tests
        junit.textui.TestRunner.run(suite());

        // Print the results
        // No sense in averaging less than 10 tests
        if (n2 > 9) {
            cat.error(
                    "Average time taken for testBitwiseOperations() = " +
                            ((0.0 + (timetaken1 * 100.0 / n2)) / 100.0) + " millisecs");
            cat.error(
                    "Average time taken for testBooleanOperations() = " +
                            ((0.0 + (timetaken2 * 100.0 / n2)) / 100.0) + " millisecs");

            if (timetaken1 != 0) {
                cat.error(
                        "testBooleanOperations() takes " +
                                ((timetaken2 - timetaken1) * 100 / timetaken1) +
                                "% more time than testBitwiseOperations()");
            }

        }

        // Because of automatic garbage collection, no sense in measuring
        // memory usage for more than 1 tests
        if (n2 == 1) {
            cat.error(
                    "Memory used for testBitwiseOperations() = " +
                            freechange1 + " bytes");
            cat.error(
                    "Memory used for testBooleanOperations() = " +
                            freechange2 + " bytes");
            if (freechange1 != 0) {
                cat.error("testBooleanOperations() uses " +
                        ((freechange2 - freechange1) * 100 / freechange1) +
                        "% more memory than testBitwiseOperations()");
            }
        }


    }


    // This method is called before running each test
    protected void setUp() {
        if (cat.isInfoEnabled()) {
            cat.info("setUp(): Started\n");
        }
        //System.gc();
    }


    // Test method
    public void testBitwiseOperations() {
        if (cat.isInfoEnabled()) {
            cat.info("testBitwiseOperations(): Started\n");
        }

        tm = System.currentTimeMillis();
        free4 = rt.freeMemory();

        bitwiseUsers = new HashMap();
        bitwiseUsers.put("user1",
                new Integer(BitwisePermissions.NOTHING_ALLOWED));
        bitwiseUsers.put("user2",
                new Integer(BitwisePermissions.VIEW_ALLOWED));
        bitwiseUsers.put("user3",
                new Integer(BitwisePermissions.VIEW_ADD_ALLOWED));
        bitwiseUsers.put("user4",
                new Integer(BitwisePermissions.VIEW_EDIT_ALLOWED));
        bitwiseUsers.put("user5",
                new Integer(BitwisePermissions.VIEW_ADD_EDIT_ALLOWED));
        bitwiseUsers.put("user6",
                new Integer(BitwisePermissions.ALL_ALLOWED));

        Set keys = bitwiseUsers.keySet();
        Iterator i = keys.iterator();

        while (i.hasNext()) {
            Object o = i.next();
            if (cat.isInfoEnabled()) {
                cat.info("User ID = " + o + "\n");
            }
            int currentPermissions =
                    ((Integer) bitwiseUsers.get(o)).intValue();

            try {
                currentPermissions =
                        BitwisePermissions.addPermissions(
                                currentPermissions,
                                new int[]{
                                        BitwisePermissions.DELETE,
                                        BitwisePermissions.ADD,
                                        BitwisePermissions.EDIT,
                                        BitwisePermissions.VIEW,
                                }
                        );
            } catch (DBSecurityException dbse) {
                cat.warn(dbse.getMessage());
            }

            try {
                currentPermissions =
                        BitwisePermissions.deletePermissions(
                                currentPermissions,
                                new int[]{
                                        BitwisePermissions.ADD,
                                        BitwisePermissions.EDIT,
                                        BitwisePermissions.VIEW,
                                }
                        );
            } catch (DBSecurityException dbse) {
                cat.warn(dbse.getMessage());
            }

            try {
                currentPermissions =
                        BitwisePermissions.addPermissions(
                                currentPermissions,
                                new int[]{
                                        BitwisePermissions.VIEW,
                                }
                        );
            } catch (DBSecurityException dbse) {
                cat.warn(dbse.getMessage());
            }

            try {
                currentPermissions =
                        BitwisePermissions.addPermissions(
                                currentPermissions,
                                new int[]{
                                        BitwisePermissions.DELETE,
                                        BitwisePermissions.VIEW,
                                }
                        );
            } catch (DBSecurityException dbse) {
                cat.warn(dbse.getMessage());
            }

            try {
                currentPermissions =
                        BitwisePermissions.deletePermissions(
                                currentPermissions,
                                new int[]{
                                        BitwisePermissions.DELETE,
                                }
                        );
            } catch (DBSecurityException dbse) {
                cat.warn(dbse.getMessage());
            }

            try {
                currentPermissions =
                        BitwisePermissions.addPermissions(
                                currentPermissions,
                                new int[]{
                                        BitwisePermissions.DELETE,
                                        BitwisePermissions.EDIT,
                                        BitwisePermissions.VIEW,
                                }
                        );
            } catch (DBSecurityException dbse) {
                cat.warn(dbse.getMessage());
            }

            try {
                currentPermissions =
                        BitwisePermissions.deletePermissions(
                                currentPermissions,
                                new int[]{
                                        BitwisePermissions.DELETE,
                                        BitwisePermissions.ADD,
                                        BitwisePermissions.EDIT,
                                        BitwisePermissions.VIEW,
                                }
                        );
            } catch (DBSecurityException dbse) {
                cat.warn(dbse.getMessage());
            }
        }

        tm2 = System.currentTimeMillis();
        free5 = rt.freeMemory();

        timetaken1 += (tm2 - tm);
        freechange1 += (free5 - free4);

        assertTrue(true);
    }
}