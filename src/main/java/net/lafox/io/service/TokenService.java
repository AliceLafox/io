package net.lafox.io.service;


import net.lafox.io.entity.Token;
import net.lafox.io.exceptions.EmptyFieldException;

/**
 * Created by Alice Lafox <alice@lafox.net> on 22.12.15
 * Lafox.Net Software Developers Team http://dev.lafox.net
 */

public interface TokenService {
    String addToken(String siteName, String ownerName, Long ownerId, String ip) throws EmptyFieldException;

    String findBySiteNameAndOwnerNameAndOwnerId(String siteName, String ownerName, Long ownerId);

    Token findByToken(String token);

}
