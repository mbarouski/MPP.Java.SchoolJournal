package school.journal.service.impl;

import school.journal.entity.Role;
import school.journal.repository.IRepository;
import school.journal.repository.exception.RepositoryException;
import school.journal.repository.factory.RepositoryFactory;
import school.journal.repository.impl.RoleRepository;
import school.journal.service.IRoleService;
import school.journal.service.ServiceAbstractClass;
import school.journal.service.exception.ServiceException;

import java.util.ArrayList;
import java.util.List;

public class RoleService extends ServiceAbstractClass implements IRoleService {
    private static RoleService instance = new RoleService();
    private IRepository<Role> roleRepository = RepositoryFactory.getInstance().getRoleRepository();

    public static RoleService getInstance(){
        return instance;
    }

    @Override
    public List<Role> getRoles() throws ServiceException {
        try {
            return ((RoleRepository)roleRepository).read();
        } catch (RepositoryException exc){
            return new ArrayList<Role>();
        }
    }
}
