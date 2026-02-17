package engine;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

public class DurableContext {

    private final String workflowId;
    private final Database db;
    private final AtomicInteger sequence = new AtomicInteger(0);
    private int crashAfter = -1;

    public DurableContext(String workflowId, Database db) {
        this.workflowId = workflowId;
        this.db = db;
    }

    public void setCrashAfter(int crashAfter) {
        this.crashAfter = crashAfter;
    }

    public <T> T step(String id, Callable<T> fn) throws Exception {

        int seq = sequence.incrementAndGet();
        String stepKey = id + "_" + seq;

        if (seq == crashAfter) {
            System.out.println("Simulating crash at step: " + stepKey);
            System.exit(1);
        }

        String existing = db.find(workflowId, stepKey);

        if (existing != null) {
            System.out.println("Skipping step: " + stepKey);
            return (T) existing;
        }

        System.out.println("Executing step: " + stepKey);

        T result = fn.call();

        db.save(workflowId, stepKey, "COMPLETED", result.toString());

        return result;
    }
}
