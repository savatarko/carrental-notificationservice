package org.komponente.notificationservice.repository;

import org.komponente.notificationservice.domain.MailType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MailTypeRepository extends JpaRepository<MailType, Long> {


}
