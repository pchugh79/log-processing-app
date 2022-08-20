package com.mycompany.log.processor;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogEventRepository extends CrudRepository<LogEventEntity, Long> {
}
