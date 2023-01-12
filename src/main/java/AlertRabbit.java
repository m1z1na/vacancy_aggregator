
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;

import java.io.IOException;
import java.util.Properties;


public class AlertRabbit {


    public static void main(String[] args) {


        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            JobDetail job = newJob(Rabbit.class).build();
            SimpleScheduleBuilder times = simpleSchedule()
                    .withIntervalInSeconds(getPeriod())
                    .repeatForever();
            Trigger trigger = newTrigger()
                    .startNow()
                    .withSchedule(times)
                    .build();
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException se) {
            se.printStackTrace();
        }
    }

    public static class Rabbit implements Job {
        @Override
        public void execute(JobExecutionContext context) {
            System.out.println("Rabbit runs here ...");
        }
    }

    public static int getPeriod() {
        int period = 0;
        Properties prop = new Properties();
        try {
            prop.load(AlertRabbit.class.getClassLoader().getResourceAsStream("rabbit.properties"));
            period = Integer.parseInt(prop.getProperty("rabbit.interval"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return period;

    }
}