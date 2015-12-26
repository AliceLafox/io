package net.lafox.io.service;

import net.lafox.io.dao.TokenDao;
import net.lafox.io.entity.Token;
import net.lafox.io.exceptions.RollBackException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Alice Lafox <alice@lafox.net> on 23.12.15
 * Lafox.Net Software Developers Team http://dev.lafox.net
 */

@Service
@Transactional(rollbackFor = RollBackException.class)
public class TokenServiceImpl implements TokenService {

    @Autowired
    TokenDao tokenDao;

    @Override
    public String addToken(String siteName, String ownerName, Long ownerId, String ip) throws RollBackException {

        if (siteName==null || siteName.isEmpty()|| ownerName==null || ownerName.isEmpty() || ownerId==null || ownerId==0)
            throw new RollBackException("rejected: required parameters are empty");

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

    @Override
    public Token findByToken(String token) {
        return tokenDao.findByToken(token);
    }

    @Override
    public Token checkToken(String token) throws RollBackException{
        Token t=tokenDao.findByToken(token);
        if (t == null) throw new RollBackException("token not found");
        return t;
    }
}
