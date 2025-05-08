package T6Devs_Back.T6Devs_Back.api.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import T6Devs_Back.T6Devs_Back.api.model.entity.SoftwareUsage;

@Repository
public interface SoftwareUsageRepository extends JpaRepository<SoftwareUsage, Long> {
}