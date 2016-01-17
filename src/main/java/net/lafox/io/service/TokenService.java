package net.lafox.io.service;


import net.lafox.io.entity.Token;
import net.lafox.io.exceptions.RollBackException;

/**
 * Created by Alice Lafox <alice@lafox.net> on 22.12.15
 * Lafox.Net Software Developers Team http://dev.lafox.net
 */

public interface TokenService {
    Token addToken(String siteName, String ownerName, Long ownerId, String ip) throws RollBackException;

    String findWriteTokenBySiteNameAndOwnerNameAndOwnerId(String siteName, String ownerName, Long ownerId);

    Token findByWriteToken(String rwToken);

    Token findByTokenId(Long tokenId);

    Token checkWriteToken(String rwToken) throws RollBackException;

    Token checkReadToken(String readToken) throws RollBackException;

    Token findByReadToken(String readToken);

    void cleanupAfterTests();
}
