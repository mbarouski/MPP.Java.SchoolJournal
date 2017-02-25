package school.journal;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import school.journal.entity.Subject;
import school.journal.persistence.HibernateUtil;

public class App
{
    private static Logger LOGGER = Logger.getLogger(App.class);

    public static void main( String[] args )
    {
        LOGGER.info("Yooooou!");
        /*Session session = HibernateUtil.getSessionFactory().openSession();

        session.beginTransaction();
        Subject subject = new Subject();
        subject.setName("Biology");
        subject.setDescription("Ebal ja v rot vashu biology.");

        session.save(subject);
        session.getTransaction().commit();*/
    }
}
