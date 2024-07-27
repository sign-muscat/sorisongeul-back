package com.sorisonsoon.setting.domain.repository;

import com.sorisonsoon.setting.domain.entity.Setting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettingRepository extends JpaRepository<Setting, Long> {

}
