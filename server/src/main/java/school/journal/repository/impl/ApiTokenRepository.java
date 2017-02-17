package school.journal.repository.impl;

import school.journal.entity.ApiToken;
import school.journal.repository.IRepository;
import school.journal.repository.exception.RepositoryException;

import java.util.List;

public class ApiTokenRepository implements IRepository<ApiToken> {
    @Override
    public ApiToken create(ApiToken apiToken) throws RepositoryException {
        return null;
    }

    @Override
    public ApiToken update(ApiToken apiToken) throws RepositoryException {
        return null;
    }

    @Override
    public List<ApiToken> read() throws RepositoryException {
        return null;
    }

    @Override
    public ApiToken read(int id) throws RepositoryException {
        return null;
    }

    @Override
    public ApiToken delete(int id) throws RepositoryException {
        return null;
    }
}
