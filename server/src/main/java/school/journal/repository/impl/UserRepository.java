package school.journal.repository.impl;

import school.journal.entity.User;
import school.journal.repository.IRepository;
import school.journal.repository.exception.RepositoryException;

import java.util.List;

public class UserRepository implements IRepository<User> {
    @Override
    public User create(User user) throws RepositoryException {
        return null;
    }

    @Override
    public User update(User user) throws RepositoryException {
        return null;
    }

    @Override
    public List<User> read() throws RepositoryException {
        return null;
    }

    @Override
    public User read(int id) throws RepositoryException {
        return null;
    }

    @Override
    public User delete(int id) throws RepositoryException {
        return null;
    }
}
