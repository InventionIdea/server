package iakka.platform.domain.crew.repository;

import iakka.platform.domain.crew.entity.Crew;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrewRepository extends JpaRepository<Crew, Long> {
}
