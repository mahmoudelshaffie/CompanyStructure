package org.tradeshit.companystructure.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.tradeshit.companystructure.entities.mocking.PositionStubbingUtil.stub11;
import static org.tradeshit.companystructure.entities.mocking.PositionStubbingUtil.stub12;
import static org.tradeshit.companystructure.entities.mocking.PositionStubbingUtil.stubRoot;
import static org.tradeshit.companystructure.repository.mockutils.IPositionRepMocker.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.tradeshit.companystructure.entities.Position;
import org.tradeshit.companystructure.exceptions.KeyNotFoundException;
import org.tradeshit.companystructure.exceptions.NoInitializedStructureException;
import org.tradeshit.companystructure.repository.IPositionRep;

public class CompanyStructureServiceTest {

	@Test(expected = NoInitializedStructureException.class)
	public void testAddChildAndStructureNotInitializedYetShouldThrowNoInitializedStructureException() {
		IPositionRep positionRep = mock(IPositionRep.class);
		CompanyStructureService target = new CompanyStructureService(positionRep);
		String parentKey = "1";
		String positionKey = "11";
		target.addChild(parentKey, positionKey);
	}

	@Test(expected = KeyNotFoundException.class)
	public void testAddChildAndStructureButParentDoesNotExistShouldThrowKeyNotFoundException() {
		IPositionRep positionRep = mock(IPositionRep.class);
		CompanyStructureService target = new CompanyStructureService(positionRep);

		Position root = stubRoot();
		mockGetRootToReturn(positionRep, root);

		String parentKey = "1";
		String positionKey = "11";
		target.addChild(parentKey, positionKey);
	}
	
	@Test
	public void testAddChildToTheRootShouldBeAddedSuccessfully() {
		IPositionRep positionRep = mock(IPositionRep.class);
		CompanyStructureService target = new CompanyStructureService(positionRep);

		Position root = stubRoot();
		mockGetRootToReturn(positionRep, root);

		String parentKey = "1";
		mockFindByIdToReturn(positionRep, root, parentKey);
		
		String positionKey = "11";
		target.addChild(parentKey, positionKey);
	}
	
	@Test(expected = KeyNotFoundException.class)
	public void testGetChildrenForPositionDoesNotExistShouldThrowKeyNotFoundException() {
		IPositionRep positionRep = mock(IPositionRep.class);
		CompanyStructureService target = new CompanyStructureService(positionRep);

		String parentKey = "1";
		target.getChildren(parentKey);
	}
	
	@Test
	public void testGetChildrenForAlreadyExistPositionShouldReturnPositionChildrenSuccessfullySuccessfully() {
		IPositionRep positionRep = mock(IPositionRep.class);
		CompanyStructureService target = new CompanyStructureService(positionRep);

		Position root = stubRoot();
		mockGetRootToReturn(positionRep, root);
		mockFindByIdToReturn(positionRep, root, "1");

		String parentKey = "1";
		Position p11 = stub11(root);
		Position p12 = stub12(root);
		
		List<Position> children = new ArrayList<>(2);
		children.add(p11);
		children.add(p12);
		mockGetByParentToReturn(positionRep, parentKey, children);
		
		List<Position> actual = target.getChildren(parentKey);
		
		assertEquals("Expected two children", 2, actual.size());
		assertTrue("Expected Position 11 Is One Of Children", actual.contains(p11));
		assertTrue("Expected Position 12 Is One Of Children", actual.contains(p12));
	}
		
}
