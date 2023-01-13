
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;

import java.io.*;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;


public class AlertRabbit {


    public static void main(String[] args)  {

        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            JobDetail job = newJob(Rabbit.class).build();
            SimpleScheduleBuilder times = simpleSchedule()
                    .withIntervalInSeconds(getPeriod("rabbit.interval"))
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

    public static int getPeriod(String name) {
        Map<String, String> values = new HashMap<String, String>();
        String line;
        try (BufferedReader read = new BufferedReader(new FileReader("C:\\projects\\job4j_grabber\\src\\main\\resources\\rabbit.properties"))) {

            while ((line = read.readLine()) != null) {
                String[] parts = line.split("=", 2);
                if (parts.length >= 2 && parts[0] != "" && parts[1] != "") {
                    String key = parts[0];
                    String value = parts[1];
                    values.put(key, value);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Integer.parseInt(values.get(name));
    }
}