package net.lafox.io.service;


public interface TokenService {
    String addToken(String siteName, String ownerName, Long ownerId, String ip);

    String findBySiteNameAndOwnerNameAndOwnerId(String siteName, String ownerName, Long ownerId);

}
