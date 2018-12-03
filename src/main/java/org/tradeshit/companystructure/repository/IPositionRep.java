package org.tradeshit.companystructure.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.tradeshit.companystructure.entities.Position;

@Repository
public interface IPositionRep extends PagingAndSortingRepository<Position, String> {

	@Query("SELECT P FROM Position P WHERE P.key=?1")
	List<Position> getByParent(String parentKey);
	
	@Query("SELECT P FROM Position P WHERE P.parent=NULL")
	Optional<Position> getRoot();
	
}
