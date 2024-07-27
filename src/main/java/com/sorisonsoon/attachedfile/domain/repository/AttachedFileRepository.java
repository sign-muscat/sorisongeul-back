package com.sorisonsoon.attachedfile.domain.repository;

import com.sorisonsoon.attachedfile.domain.entity.AttachedFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachedFileRepository extends JpaRepository<AttachedFile, Long> {

}
