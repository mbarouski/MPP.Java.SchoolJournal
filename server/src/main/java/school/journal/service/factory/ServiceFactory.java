package school.journal.service.factory;

import school.journal.service.IRoleService;
import school.journal.service.impl.RoleService;

public class ServiceFactory {
    private static ServiceFactory instance = new ServiceFactory();

    private IRoleService roleService = RoleService.getInstance();

    public static ServiceFactory getInstance(){
        return instance;
    }

    public IRoleService getRoleService(){
        return roleService;
    }
}
