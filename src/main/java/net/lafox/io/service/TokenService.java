package net.lafox.io.service;


import net.lafox.io.exceptions.EmptyFieldException;

public interface TokenService {
    String addToken(String siteName, String ownerName, Long ownerId, String ip) throws EmptyFieldException;

    String findBySiteNameAndOwnerNameAndOwnerId(String siteName, String ownerName, Long ownerId);

}
