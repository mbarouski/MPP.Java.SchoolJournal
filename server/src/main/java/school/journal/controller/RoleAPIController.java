package school.journal.controller;

import org.hibernate.Session;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import school.journal.entity.Subject;
import school.journal.persistence.HibernateUtil;
import school.journal.service.IRoleService;
import school.journal.service.factory.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static school.journal.utils.ViewNameStorage.ROLES_VIEW;

public class RoleAPIController extends AbstractController{
    private static IRoleService roleService = ServiceFactory.getInstance().getRoleService();

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        ModelAndView model = new ModelAndView(ROLES_VIEW);
        model.addObject("roles", roleService.getRoles());
        return model;
    }
}
