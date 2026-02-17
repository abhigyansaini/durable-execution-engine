import engine.Database;
import engine.DurableContext;
import examples.EmployeeWorkflow;

public class App {
    public static void main(String[] args) throws Exception {

        Database db = new Database("jdbc:sqlite:workflow.db");

        DurableContext ctx = new DurableContext("employee-workflow", db);

        if (args.length > 0) {
            int crashAfter = Integer.parseInt(args[0]);
            ctx.setCrashAfter(crashAfter);
        }

        EmployeeWorkflow wf = new EmployeeWorkflow();
        wf.run(ctx);
    }
}
