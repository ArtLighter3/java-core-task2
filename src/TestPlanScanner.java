import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public class TestPlanScanner {

    record TestStep(String id, String methodName, int order) {}

    public static void buildPlan(Class<?> cl) {
        Set<TestStep> testSteps = new TreeSet<>(Comparator
                .comparingInt(TestStep::order)
                .thenComparing(TestStep::id)
                .thenComparing(TestStep::methodName)
        );

        for (Method method : cl.getDeclaredMethods()) {
            if (method.isAnnotationPresent(TestId.class)) {
                TestId annotation = method.getAnnotation(TestId.class);
                testSteps.add(new TestStep(annotation.value(), method.getName(), annotation.order()));
            }
        }

        System.out.printf("План прогона для %s: ", cl.getSimpleName());
        if (testSteps.isEmpty()) System.out.println("Кейсов нет");
        else {
            System.out.println();
            for (TestStep testStep : testSteps) {
                System.out.printf("  %d. %s -> %s%n", testStep.order, testStep.id, testStep.methodName);
            }
        }
    }
}
