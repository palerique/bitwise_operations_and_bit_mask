package br.com.sitedoph.bitwiseopsandmask;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import junit.extensions.RepeatedTest;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BitwiseStatusesTest extends TestCase {

    private static Logger log = LoggerFactory.getLogger(BitwiseStatusesTest.class);
    // Test statistics
    private static int n2 = 0;
    private static long timetaken1 = 0;
    private static long freechange1 = 0;
    // Fixtures - Class fields to allow access from anywhere in class
    HashMap bitwiseLines;
    // Some free memory info
    Runtime rt = Runtime.getRuntime();
    private long tm, tm2;
    private long free4, free5;

    // Required constructor - takes method name to test as parameter
    public BitwiseStatusesTest(String name) {
        super(name);
    }

    // Add tests to run in order of running into the suite
    public static TestSuite suite() {

        // Shortcut to automatically add all testXXX methods
        //return new TestSuite(BitwiseStatusesTest.class);

        // Number of repetitions per test
        n2 = 1;

        TestSuite suite1 = new TestSuite();
        suite1.addTest(
                new RepeatedTest(
                        new BitwiseStatusesTest("testBitwiseOperations"), n2));

        TestSuite suite = new TestSuite();
        //suite.addTest(new BitwiseStatusesTest("testBitwiseOperations"));
        //suite.addTest(new BitwiseStatusesTest("testBooleanOperations"));
        suite.addTest(suite1);
        //TestResult result = suite.run();

        return suite;
    }


    public static void main(String[] args) {
        // Initialize static fields
        timetaken1 = 0;
        freechange1 = 0;

        // Run the tests
        junit.textui.TestRunner.run(suite());

        // Print the results
        // No sense in averaging less than 10 tests
        if (n2 > 9) {
            log.error(
                    "Average time taken for testBitwiseOperations() = " +
                            ((0.0 + (timetaken1 * 100.0 / n2)) / 100.0) + " millisecs");
        }

        // Because of automatic garbage collection, no sense in measuring
        // memory usage for more than 1 tests
        if (n2 == 1) {
            log.error(
                    "Memory used for testBitwiseOperations() = " +
                            freechange1 + " bytes");
        }
    }


    // This method is called before running each test
    protected void setUp() {
        log.info("setUp(): Started\n");
        //System.gc();
    }


    // Test method
    public void testBitwiseOperations() {
        log.info("testBitwiseOperations(): Started\n");

        tm = System.currentTimeMillis();
        free4 = rt.freeMemory();

        bitwiseLines = new HashMap();
        bitwiseLines.put("line1",
                new Integer(BitwiseStatuses.INITIAL_STATUS));
        bitwiseLines.put("line2",
                new Integer(BitwiseStatuses.NOT_COVERABLE));
        bitwiseLines.put("line3",
                new Integer(BitwiseStatuses.COVERABLE_NOT_COVERED));
        bitwiseLines.put("line4",
                new Integer(BitwiseStatuses.COVERABLE_COVERED));

        Set keys = bitwiseLines.keySet();
        Iterator i = keys.iterator();

        while (i.hasNext()) {
            Object o = i.next();
            log.info("Line ID = " + o + "\n");
            int currentStatus =
                    ((Integer) bitwiseLines.get(o)).intValue();

            try {
                currentStatus =
                        BitwiseStatuses.addStatus(
                                currentStatus,
                                new int[]{
                                        BitwiseStatuses.COVERED,
                                        BitwiseStatuses.COVERABLE,
                                        BitwiseStatuses.NOT_COVERED,
                                        BitwiseStatuses.NOT_COVERABLE,
                                }
                        );
            } catch (DBSecurityException dbse) {
                log.warn(dbse.getMessage());
            }

            try {
                currentStatus =
                        BitwiseStatuses.deleteStatuses(
                                currentStatus,
                                new int[]{
                                        BitwiseStatuses.COVERABLE,
                                        BitwiseStatuses.NOT_COVERED,
                                        BitwiseStatuses.NOT_COVERABLE,
                                }
                        );
            } catch (DBSecurityException dbse) {
                log.warn(dbse.getMessage());
            }

            try {
                currentStatus =
                        BitwiseStatuses.addStatus(
                                currentStatus,
                                new int[]{
                                        BitwiseStatuses.NOT_COVERABLE,
                                }
                        );
            } catch (DBSecurityException dbse) {
                log.warn(dbse.getMessage());
            }

            try {
                currentStatus =
                        BitwiseStatuses.addStatus(
                                currentStatus,
                                new int[]{
                                        BitwiseStatuses.COVERED,
                                        BitwiseStatuses.NOT_COVERABLE,
                                }
                        );
            } catch (DBSecurityException dbse) {
                log.warn(dbse.getMessage());
            }

            try {
                currentStatus =
                        BitwiseStatuses.deleteStatuses(
                                currentStatus,
                                new int[]{
                                        BitwiseStatuses.COVERED,
                                }
                        );
            } catch (DBSecurityException dbse) {
                log.warn(dbse.getMessage());
            }

            try {
                currentStatus =
                        BitwiseStatuses.addStatus(
                                currentStatus,
                                new int[]{
                                        BitwiseStatuses.COVERED,
                                        BitwiseStatuses.NOT_COVERED,
                                        BitwiseStatuses.NOT_COVERABLE,
                                }
                        );
            } catch (DBSecurityException dbse) {
                log.warn(dbse.getMessage());
            }

            try {
                currentStatus =
                        BitwiseStatuses.deleteStatuses(
                                currentStatus,
                                new int[]{
                                        BitwiseStatuses.COVERED,
                                        BitwiseStatuses.COVERABLE,
                                        BitwiseStatuses.NOT_COVERED,
                                        BitwiseStatuses.NOT_COVERABLE,
                                }
                        );
            } catch (DBSecurityException dbse) {
                log.warn(dbse.getMessage());
            }
        }

        tm2 = System.currentTimeMillis();
        free5 = rt.freeMemory();

        timetaken1 += (tm2 - tm);
        freechange1 += (free5 - free4);

        assertTrue(true);
    }
}