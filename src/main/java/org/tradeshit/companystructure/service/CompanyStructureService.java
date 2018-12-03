package org.tradeshit.companystructure.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tradeshit.companystructure.entities.Position;
import org.tradeshit.companystructure.exceptions.KeyNotFoundException;
import org.tradeshit.companystructure.exceptions.NoInitializedStructureException;
import org.tradeshit.companystructure.repository.IPositionRep;

@Service
public class CompanyStructureService {

	private IPositionRep positionRep;
	
	@Autowired
	public CompanyStructureService(IPositionRep positionRep) {
		this.positionRep = positionRep;
	}

	public Optional<Position> getRoot() {
		return positionRep.getRoot();
	}
	
	public void create(String root) {
		positionRep.save(new Position(root));
	}
	
	public Optional<Position> getPosition(String key) {
		return positionRep.findById(key);
	}
	
	public Position addChild(String parentKey, String positionKey) {		
		Optional<Position> root = getRoot();
		if (! root.isPresent()) {
			throw new NoInitializedStructureException();
		}
		
		Optional<Position> parent = getPosition(parentKey);
		if (! parent.isPresent()) {
			throw new KeyNotFoundException(parentKey);
		}
		int height = parent.get().getHeight() + 1;
		
		Position position = new Position(positionKey, parent.get(), root.get().getKey(), height);
		return positionRep.save(position);		
	}
	
	public List<Position> getChildren(String parentKey) {
		Optional<Position> parent = getPosition(parentKey);
		if (! parent.isPresent()) {
			throw new KeyNotFoundException(parentKey);
		}
		return positionRep.getByParent(parentKey);
	}
}
