/**
 * 
 */
package com.org.mycompany.positions.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.org.mycompany.positions.domain.Position;

/**
 * @author Dayanand Mishra
 *
 */
@Repository
public interface PositionRepository extends CrudRepository<Position, Long> {

}
