package org.tradeshit.companystructure.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.tradeshit.companystructure.entities.Position;
import org.tradeshit.companystructure.exceptions.CycleDetectedException;
import org.tradeshit.companystructure.exceptions.KeyNotFoundException;
import org.tradeshit.companystructure.exceptions.NoInitializedStructureException;
import org.tradeshit.companystructure.repository.IPositionRep;

@Service
public class ChangeParentService {
	private IPositionRep positionRep;

	@Autowired
	public ChangeParentService(IPositionRep positionRep) {
		this.positionRep = positionRep;
	}

	@Transactional(propagation = Propagation.NESTED, isolation = Isolation.READ_UNCOMMITTED)
	public void changeParent(String key, String newParentKey) {
		Optional<Position> root = positionRep.getRoot();
		if (!root.isPresent()) {
			throw new NoInitializedStructureException();
		}

		Optional<Position> child = positionRep.findById(key);
		if (!child.isPresent()) {
			throw new KeyNotFoundException(key);
		}

		Optional<Position> newParent = positionRep.findById(newParentKey);
		if (!newParent.isPresent()) {
			throw new KeyNotFoundException(newParentKey);
		}

		Set<String> parentsKeys = new HashSet<>();

		Position currentRoot = root.get();
		boolean isRoot = currentRoot.equals(child.get());
		boolean isPromoted = child.get().isChild(newParent.get());
		if (isRoot) {
			handleChangeParentOfRoot(child.get(), newParent.get(), currentRoot, parentsKeys);
		} else if (isPromoted) {
			handlePromotedParent(child.get(), newParent.get(), currentRoot.getKey(), parentsKeys);
		} else {
			handleChildPath(child.get(), newParent.get(), currentRoot.getKey(), parentsKeys);
		}
	}

	protected void handleChangeParentOfRoot(Position child, Position newParent, Position currentRoot,
			Set<String> parentsKeys) {
		newParent.setRoot(null);
		newParent.setHeight(0);
		Position oldParent = newParent.getParent();
		oldParent.removeChild(newParent);
		newParent.addChild(currentRoot);

		updateChildrenHeightAndRoot(newParent, newParent.getKey(), parentsKeys);
	}

	protected void handlePromotedParent(Position child, Position newParent, String rootKey, Set<String> parentsKeys) {
		child.removeChild(newParent);
		newParent.setParent(child.getParent());
		newParent.setHeight(child.getHeight());
		newParent.addChild(child);
		child.setParent(newParent);

		updateChildrenHeightAndRoot(newParent, rootKey, parentsKeys);
		positionRep.save(newParent);
	}

	protected void handleChildPath(Position child, Position newParent, String newRoot, Set<String> parentsKeys) {
		child.setParent(newParent);
		child.setHeight(newParent.getHeight() + 1);
		Position oldParent = child.getParent();
		oldParent.removeChild(child);
		newParent.addChild(child);
		updateChildrenHeightAndRoot(child, newRoot, parentsKeys);
		positionRep.save(child);
	}

	protected void updateChildrenHeightAndRoot(Position node, String newRoot, Set<String> parentsKeys) {
		if (!parentsKeys.add(node.getKey())) {
			throw new CycleDetectedException();
		}

		int childHeight = node.getHeight() + 1;
		for (Position child : node.getChildren()) {
			child.setHeight(childHeight);
			child.setRoot(newRoot);
			updateChildrenHeightAndRoot(child, newRoot, parentsKeys);
			positionRep.save(child);
		}

	}
}