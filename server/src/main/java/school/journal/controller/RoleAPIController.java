package school.journal.controller;

import org.hibernate.Session;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import school.journal.controller.exception.ControllerException;
import school.journal.entity.Role;
import school.journal.entity.Subject;
import school.journal.persistence.HibernateUtil;
import school.journal.service.IRoleService;
import school.journal.service.exception.ServiceException;
import school.journal.service.factory.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

import static school.journal.utils.ViewNameStorage.ROLES_VIEW;

@Controller
@RequestMapping(value = "/api/roles")
public class RoleAPIController {
    private static IRoleService roleService = ServiceFactory.getInstance().getRoleService();

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody List<Role> getList(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
            throws ControllerException {
        try{
            return roleService.getRoles();
        } catch (ServiceException exc){
            return new ArrayList<>();
        }
    }
}
