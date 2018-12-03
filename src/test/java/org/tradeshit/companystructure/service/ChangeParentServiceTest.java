package org.tradeshit.companystructure.service;

import static org.mockito.Mockito.mock;
import static org.tradeshit.companystructure.repository.mockutils.IPositionRepMocker.*;
import static org.tradeshit.companystructure.entities.mocking.PositionStubbingUtil.*;

import org.junit.Test;
import org.tradeshit.companystructure.entities.Position;
import org.tradeshit.companystructure.exceptions.KeyNotFoundException;
import org.tradeshit.companystructure.exceptions.NoInitializedStructureException;
import org.tradeshit.companystructure.repository.IPositionRep;

public class ChangeParentServiceTest {

	@Test(expected = NoInitializedStructureException.class)
	public void testChangeParentAndStructureNotInitializedYetShouldThrowNoInitializedStructureException() {
		IPositionRep positionRep = mock(IPositionRep.class);
		ChangeParentService target = new ChangeParentService(positionRep);
		String key = "122";
		String newParentKey = "11";
		target.changeParent(key, newParentKey);
	}

	@Test(expected = KeyNotFoundException.class)
	public void testChangeParentWithChildKeyDoesNotExistShouldThrowKeyNotFoundException() {
		Position root = stubRoot();
		IPositionRep positionRep = mock(IPositionRep.class);
		mockGetRootToReturn(positionRep, root);
		ChangeParentService target = new ChangeParentService(positionRep);
		String key = "122";
		String newParentKey = "11";
		target.changeParent(key, newParentKey);
	}
	
	@Test(expected = KeyNotFoundException.class)
	public void testChangeParentWithNewParentKeyDoesNotExistShouldThrowKeyNotFoundException() {
		Position root = stubRoot();
		IPositionRep positionRep = mock(IPositionRep.class);
		mockGetRootToReturn(positionRep, root);
		
		String childKey = "122";
		Position child = new Position(childKey, root, ROOT_KEY, 1);
		mockFindByIdToReturn(positionRep, child, childKey);
		
		ChangeParentService target = new ChangeParentService(positionRep);
		String newParentKey = "11";
		target.changeParent(childKey, newParentKey);
	}

	@Test
	public void testChangeParentOfRootShouldBeChangedSuccessfully() {
		IPositionRep positionRep = mock(IPositionRep.class);
		Position root = stubRoot();
		mockGetRootToReturn(positionRep, root);
		
		Position p11 = stub11(root);
		Position p12 = stub12(root);
		
		String childKey = "122";
		Position child = stub122(p12);
		mockFindByIdToReturn(positionRep, child, childKey);
		
		String newParentKey = "11";
		Position newParent = stub111(p11);
		mockFindByIdToReturn(positionRep, newParent, newParentKey);
		
		ChangeParentService target = new ChangeParentService(positionRep);
		target.changeParent(childKey, newParentKey);
	}
	
}
