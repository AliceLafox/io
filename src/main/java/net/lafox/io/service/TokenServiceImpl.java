package net.lafox.io.service;

import net.bull.javamelody.MonitoredWithSpring;
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
@MonitoredWithSpring
@Transactional(rollbackFor = RollBackException.class)
public class TokenServiceImpl implements TokenService {

    @Autowired
    TokenDao tokenDao;

    @Override
    public Token addToken(String siteName, String ownerName, Long ownerId, String ip) throws RollBackException {

        if (siteName==null || siteName.isEmpty()|| ownerName==null || ownerName.isEmpty() || ownerId==null || ownerId==0)
            throw new RollBackException("rejected: required parameters are empty");

        Token token = tokenDao.findBySiteNameAndOwnerNameAndOwnerId(siteName, ownerName, ownerId);
        if (token == null) {
            token = new Token(siteName, ownerName, ownerId, ip);
            tokenDao.insert(token);
        }
        return token;
    }

    @Override
    public String findWriteTokenBySiteNameAndOwnerNameAndOwnerId(String siteName, String ownerName, Long ownerId) {
        return tokenDao.findBySiteNameAndOwnerNameAndOwnerId(siteName, ownerName, ownerId).getWriteToken();
    }


    @Override
    public Token findByTokenId(Long tokenId) {
        return tokenDao.findByTokenId(tokenId);
    }

    @Override
    public Token checkWriteToken(String writeToken) throws RollBackException{
        Token t=tokenDao.findByWriteToken(writeToken);
        if (t == null) throw new RollBackException("writeToken not found "+writeToken);
        return t;
    }

    @Override
    public Token findByWriteToken(String writeToken) {
        return tokenDao.findByWriteToken(writeToken);
    }

    @Override
    public Token checkReadToken(String readToken) throws RollBackException{
        Token t=tokenDao.findByReadToken(readToken);
        if (t == null) throw new RollBackException("readToken not found "+readToken);
        return t;
    }

    @Override
    public Token findByReadToken(String readToken) {
        return tokenDao.findByReadToken(readToken);
    }

    @Override
    public void cleanupAfterTests(){
        tokenDao.cleanupAfterTests();
    }
}
