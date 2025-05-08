package T6Devs_Back.T6Devs_Back.api.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import T6Devs_Back.T6Devs_Back.api.model.entity.Software;

import java.util.List;

@Repository
public interface SoftwareRepository extends JpaRepository<Software, Long> {
    List<Software> findByDisponivelTrue();
}