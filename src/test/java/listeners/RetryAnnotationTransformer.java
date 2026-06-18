package listeners;

import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * ═══════════════════════════════════════════════════════════════════════════
 * RetryAnnotationTransformer — automatically wires RetryAnalyzer onto
 * every @Test method in the suite.
 * ═══════════════════════════════════════════════════════════════════════════
 *
 * WHY THIS FILE WAS FAILING:
 *   Previously this class was written inside RetryAnalyzer.java as a
 *   package-private class (no 'public' keyword on the class declaration).
 *
 *   When TestNG reads testng.xml and sees:
 *     <listener class-name="listeners.RetryAnnotationTransformer"/>
 *
 *   It uses Java reflection to create an instance:
 *     Class.forName("listeners.RetryAnnotationTransformer")
 *         .getDeclaredConstructor()
 *         .newInstance()
 *
 *   This requires the class to be:
 *     1. PUBLIC          — so reflection can see it from outside the package.
 *     2. TOP-LEVEL       — its own .java file, not nested inside another class.
 *     3. PUBLIC no-arg constructor — Java adds this automatically when no
 *        constructor is written, BUT only if the class itself is public.
 *
 *   A package-private class (no 'public') inside another file fails step 1.
 *   TestNG throws: "Couldn't find a constructor in class ..."
 *
 * THE FIX:
 *   Move it into its own file: RetryAnnotationTransformer.java
 *   Make the class declaration: public class RetryAnnotationTransformer
 *   No constructor needed — Java provides a public no-arg constructor automatically.
 *
 * WHAT THIS CLASS DOES:
 *   TestNG calls transform() once for every @Test method it finds before
 *   running the suite. We use it to set RetryAnalyzer on each test so that
 *   failed tests automatically retry (up to 2 times, as set in RetryAnalyzer).
 *
 *   WITHOUT this transformer: you must add retryAnalyzer = RetryAnalyzer.class
 *   to every single @Test annotation manually.
 *
 *   WITH this transformer: all tests get retry behaviour automatically.
 *
 * HOW TO REGISTER IN testng.xml:
 *   <listeners>
 *     <listener class-name="listeners.AllureListener"/>
 *     <listener class-name="listeners.RetryAnnotationTransformer"/>
 *   </listeners>
 *
 *   NOTE: IAnnotationTransformer listeners MUST be in the <listeners> block
 *   at the <suite> level (not inside a <test> block). TestNG processes
 *   annotations before tests run, so it must be registered at suite level.
 * ═══════════════════════════════════════════════════════════════════════════
 */
public class RetryAnnotationTransformer implements IAnnotationTransformer {

    /**
     * Called by TestNG once for every @Test method before execution begins.
     *
     * PARAMETERS (TestNG passes these — you don't call this method yourself):
     * @param annotation      the @Test annotation object for this method.
     *                        It is mutable — we can change its properties here.
     * @param testClass       the class that contains the @Test method
     * @param testConstructor unused here — for constructor-level annotations
     * @param testMethod      the actual @Test method (can read its name, etc.)
     *
     * WHAT WE DO:
     *   annotation.setRetryAnalyzer(RetryAnalyzer.class) tells TestNG:
     *   "when this test fails, use RetryAnalyzer to decide whether to retry."
     *
     *   RetryAnalyzer.retry() returns true up to 2 times, then returns false.
     *   So each test can run at most 3 times: 1 original + 2 retries.
     */
    @Override
    public void transform(ITestAnnotation annotation,
                          Class testClass,
                          Constructor testConstructor,
                          Method testMethod) {

        // Set RetryAnalyzer on every @Test method.
        // This is equivalent to writing on each test:
        //   @Test(retryAnalyzer = RetryAnalyzer.class)
        // but we do it here once instead of on every test method.
        annotation.setRetryAnalyzer(RetryAnalyzer.class);
    }
}