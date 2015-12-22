package net.lafox.io.service;

import net.lafox.io.dao.TokenDao;
import net.lafox.io.entity.Token;
import net.lafox.io.exceptions.EmptyFieldException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class TokenServiceImpl implements TokenService {

    @Autowired
    TokenDao tokenDao;

    @Override
    public String addToken(String siteName, String ownerName, Long ownerId, String ip) throws EmptyFieldException {

        if (siteName==null || siteName.isEmpty()|| ownerName==null || ownerName.isEmpty() || ownerId==null || ownerId==0)
            throw new EmptyFieldException("rejected: required parameters are empty");

        Token token = tokenDao.findBySiteNameAndOwnerNameAndOwnerId(siteName, ownerName, ownerId);
        if (token == null) {
            token = new Token(siteName, ownerName, ownerId, ip);
            tokenDao.save(token);
        }
        return token.getToken();
    }

    @Override
    public String findBySiteNameAndOwnerNameAndOwnerId(String siteName, String ownerName, Long ownerId) {
        return tokenDao.findBySiteNameAndOwnerNameAndOwnerId(siteName, ownerName, ownerId).getToken();
    }
}
