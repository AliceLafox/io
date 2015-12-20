package net.lafox.io.dao;

import net.lafox.io.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TokenDao extends JpaRepository<Token, Long> {
    Token findBySiteNameAndOwnerNameAndOwnerId(String siteName, String ownerName, Long ownerId);
}
