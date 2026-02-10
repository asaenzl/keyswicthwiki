package com.keyswitch.keyswitchwiki.repository;

import com.keyswitch.keyswitchwiki.entity.ExternalLink;
import com.keyswitch.keyswitchwiki.entity.LinkType;
import com.keyswitch.keyswitchwiki.entity.Switch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ExternalLinkRepository extends JpaRepository<ExternalLink, Long> {

    // Find all links for a specific switch
    List<ExternalLink> findBySwitchEntity(Switch switchEntity);

    // Find links by type for a specific switch
    List<ExternalLink> findBySwitchEntityAndLinkType(Switch switchEntity, LinkType linkType);

    // Find all links of a specific type
    List<ExternalLink> findByLinkType(LinkType linkType);
}
