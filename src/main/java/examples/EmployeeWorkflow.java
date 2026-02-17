package examples;

import engine.DurableContext;

public class EmployeeWorkflow {

    public void run(DurableContext ctx) throws Exception {

        ctx.step("CreateEmployee", () -> {
            System.out.println("Creating employee...");
            return "EMP001";
        });

        ctx.step("ProvisionLaptop", () -> {
            System.out.println("Provisioning laptop...");
            return "Laptop OK";
        });

        ctx.step("SendEmail", () -> {
            System.out.println("Sending email...");
            return "Email Sent";
        });
    }
}

