package net.lafox.io.dao;

import net.lafox.io.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Alice Lafox <alice@lafox.net> on 22.12.15
 * Lafox.Net Software Developers Team http://dev.lafox.net
 */

public interface TokenDao extends JpaRepository<Token, Long> {
    Token findBySiteNameAndOwnerNameAndOwnerId(String siteName, String ownerName, Long ownerId);

    Token findByToken(String token);
}
