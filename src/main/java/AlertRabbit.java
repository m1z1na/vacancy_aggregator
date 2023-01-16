
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;

import java.io.*;
import java.sql.*;
import java.util.Properties;


public class AlertRabbit {

    public Connection init(Properties properties) throws ClassNotFoundException, SQLException {
        Connection connection;
        Class.forName(properties.getProperty("connection.driver_class"));
        String url = properties.getProperty("connection.url");
        String login = properties.getProperty("username");
        String password = properties.getProperty("password");
        connection = DriverManager.getConnection(url, login, password);
        return connection;

    }

    static Properties getProperties() {

        Properties properties = new Properties();
        try (InputStream in = AlertRabbit.class.getClassLoader().getResourceAsStream("rabbit.properties")) {
            properties.load(in);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return properties;
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        AlertRabbit rabbit = new AlertRabbit();
        Properties pr = getProperties();
        int interval = Integer.parseInt(pr.getProperty("rabbit.interval"));

        try (Connection connection = rabbit.init(pr)) {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            JobDataMap data = new JobDataMap();
            data.put("connection", connection);
            JobDetail job = newJob(Rabbit.class)
                    .usingJobData(data)
                    .build();
            SimpleScheduleBuilder times = simpleSchedule()
                    .withIntervalInSeconds(interval)
                    .repeatForever();
            Trigger trigger = newTrigger()
                    .startNow()
                    .withSchedule(times)
                    .build();
            scheduler.scheduleJob(job, trigger);
            Thread.sleep(5000);
            scheduler.shutdown();
        } catch (Exception se) {
            se.printStackTrace();
        }
    }


    public static class Rabbit implements Job {

        public Rabbit() {
            System.out.println(hashCode());
        }

        @Override
        public void execute(JobExecutionContext context) {
            System.out.println("Rabbit runs here ...");
            Connection cn = (Connection) context.getJobDetail().getJobDataMap().get("connection");
            try (PreparedStatement statement =
                         cn.prepareStatement("insert into rabbit(created_date) values (?)",
                                 Statement.RETURN_GENERATED_KEYS)) {
                statement.setLong(1, System.currentTimeMillis());
                statement.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }


    }
}