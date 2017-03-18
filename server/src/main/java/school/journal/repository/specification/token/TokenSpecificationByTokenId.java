package school.journal.repository.specification.token;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

public class TokenSpecificationByTokenId extends TokenSpecification {
    private int tokenId;

    public TokenSpecificationByTokenId(int tokenId) {
        this.tokenId = tokenId;
    }

    @Override
    public Criterion toCriteria() {
        return Restrictions.eq("masterId", tokenId);
    }

    @Override
    public boolean specified(Token token) {
        return token.getMasterId() == tokenId;
    }
}
