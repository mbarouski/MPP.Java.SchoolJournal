package school.journal.repository.impl;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.springframework.stereotype.Component;
import school.journal.entity.Token;
import school.journal.repository.RepositoryAbstractClass;
import school.journal.repository.exception.RepositoryException;
import school.journal.repository.specification.HibernateSpecification;

import java.util.List;

@Component
public class TokenRepository extends RepositoryAbstractClass<Token> {
    @Override
    public Token create(Token token, Session session) throws RepositoryException {
        session.save(token);
        return token;
    }

    @Override
    public Token update(Token token, Session session) throws RepositoryException {
        session.update(token);
        return token;
    }

    @Override
    public Token delete(Token token, Session session) throws RepositoryException {
        session.delete(token);
        return token;
    }

    @Override
    public List<Token> query(HibernateSpecification specification, Session session) throws RepositoryException {
        Criteria criteria =  session.createCriteria(Token.class);
        Criterion criterion;
        if((specification != null) && ((criterion = specification.toCriteria()) != null)){
            criteria.add(criterion);
        }
        return criteria.list();
    }
}
