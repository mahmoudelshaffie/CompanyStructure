package org.tradeshit.companystructure.rest;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.tradeshit.companystructure.entities.Position;
import org.tradeshit.companystructure.exceptions.NoInitializedStructureException;
import org.tradeshit.companystructure.service.ChangeParentService;
import org.tradeshit.companystructure.service.CompanyStructureService;

@RestController
@RequestMapping("/api/structure")
public class CompanyStructureRestAPI {
	
	@Autowired
	private CompanyStructureService structureService;
	
	@Autowired
	private ChangeParentService changeParentService;
	
	@GetMapping
	@ResponseBody
	public Position getRoot() {
		 Optional<Position> root = structureService.getRoot();
		 if (! root.isPresent()) {
			 throw new NoInitializedStructureException();
		 }
		 return root.get();
	}
	
	@PostMapping
	public void createStructure(@RequestParam("root") String root) {
		structureService.create(root);
	}
	
	@PostMapping("/{nodeKey}/children")
	public boolean addChild(@PathVariable("nodeKey") String parentKey, @RequestParam("child") String childKey) {
		structureService.addChild(parentKey, childKey);
		return true;
	}
	
	@GetMapping("/{nodeKey}/children")
	public List<Position> getChildren(@PathVariable("nodeKey") String parentKey) {
		return structureService.getChildren(parentKey);
	}
	
	@PutMapping("/{nodeKey}/parent")
	public void changeParent(@PathVariable("nodeKey") String nodeKey, @RequestParam("newParent") String newParentKey) {
		changeParentService.changeParent(nodeKey, newParentKey);
	}
}
