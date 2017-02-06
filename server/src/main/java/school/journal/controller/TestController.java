package school.journal.controller;

import org.hibernate.Session;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import school.journal.entity.Subject;
import school.journal.persistence.HibernateUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class TestController extends AbstractController{

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        ModelAndView model = new ModelAndView("Test");
        model.addObject("subjects", getSubjects());
        return model;
    }

    private List<Subject> getSubjects(){
        Session session = HibernateUtil.getSessionFactory().openSession();

        return session.createCriteria(Subject.class).list();
    }
}
