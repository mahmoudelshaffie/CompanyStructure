package org.tradeshit.companystructure.repository.mockutils;

import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.tradeshit.companystructure.entities.Position;
import org.tradeshit.companystructure.repository.IPositionRep;

public class IPositionRepMocker {
	
	public static void mockGetRootToReturn(IPositionRep mockedRep, Position root) {
		when(mockedRep.getRoot()).thenReturn(Optional.ofNullable(root));
	}
	
	public static void mockFindByIdToReturn(IPositionRep mockedRep, Position position, String key) {
		when(mockedRep.findById(key)).thenReturn(Optional.ofNullable(position));
	}
	
	public static void mockGetByParentToReturn(IPositionRep mockedRep, String parentKey, List<Position> children) {
		when(mockedRep.getByParent(parentKey)).thenReturn(children);
	}
}
