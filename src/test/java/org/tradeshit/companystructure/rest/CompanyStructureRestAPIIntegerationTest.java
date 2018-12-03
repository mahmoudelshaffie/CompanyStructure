package org.tradeshit.companystructure.rest;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringRunner;
import org.tradeshit.companystructure.CompanyStructureApiApplication;

import io.restassured.response.Response;

@RunWith(SpringRunner.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(classes = CompanyStructureApiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompanyStructureRestAPIIntegerationTest {

	private String BASE_URL = "/api/structure";
	private String CHILDREN_URL = BASE_URL + "/{parent}/children";
	private String CHANGE_PARENT_URL = BASE_URL + "/{child}/parent";

	private String ROOT_PARAM = "root";
	private String PARENT_PARAM = "parent";
	private String NEW_PARENT_PARAM = "newParent";
	private String CHILD_PARAM = "child";

	private final int OK = HttpStatus.OK.value();
	private final int NOT_FOUND = HttpStatus.NOT_FOUND.value();

	private String ROOT_KEY = "1";
	private int ROOT_HIEGHT = 0;

	@LocalServerPort
	private int port;

	private void initializeStructureSuccessfully(String rootKey) {
		given().port(port).basePath(BASE_URL).queryParam(ROOT_PARAM, rootKey).post().then().assertThat().statusCode(OK);
	}

	private void addChildSuccessfully(String parentKey, String childKey) {
		given().port(port).basePath(CHILDREN_URL).pathParam(PARENT_PARAM, parentKey).queryParam(CHILD_PARAM, childKey)
				.post().then().assertThat().statusCode(OK);
	}

	private Response getChildren(String parentKey) {
		return given().port(port).basePath(CHILDREN_URL).pathParam(PARENT_PARAM, parentKey).get();
	}
	
	private Response getRoot() {
		return given().port(port).basePath(BASE_URL).get();
	}

	private void changeParentSuccessfully(String childKey, String newParentKey) {
		given().port(port).basePath(CHANGE_PARENT_URL).pathParam(CHILD_PARAM, childKey)
				.queryParam(NEW_PARENT_PARAM, newParentKey).put().then().assertThat().statusCode(OK);
	}
	
	public void validateRootJson(JSONObject root, String expectedKey, int expectedRootChildren) throws JSONException {
		assertEquals("Invalid root key", expectedKey, root.getString("key"));
		assertEquals("Invlaid root height, Expected height of root to be zero", ROOT_HIEGHT, root.getInt("height"));
		assertFalse("Expected root doesn't have a parent", root.has("parent"));
		JSONArray rootChildren = root.getJSONArray("children");
		assertEquals("Invalid length of children of root", expectedRootChildren, rootChildren.length());
	}
	
	public void validatePositionJsonWithChildren(JSONObject position, String key, String parent, int height, String root, int children) throws JSONException {
		assertEquals("Invalid Key", key, position.get("key"));
		assertEquals("Invalid Height", height, position.getInt("height"));
		assertEquals("Invalid parent", parent, position.get("parent"));
		assertEquals("Invalid root", root, position.get("root"));
		JSONArray posChildren = position.getJSONArray("children");
		assertEquals("Invalid Length of children", children, posChildren.length());
	}
	
	public void validatePositionJsonWithoutChildren(JSONObject position, String key, String parent, int height, String root) throws JSONException {
		assertEquals("Invalid Key", key, position.get("key"));
		assertEquals("Invalid Height", height, position.getInt("height"));
		assertEquals("Invalid parent", parent, position.get("parent"));
		assertEquals("Invalid root", root, position.get("root"));
		assertFalse("Expected to don't have any child", position.has("children"));
	}

	@Test
	public void testChangeParentOfRootWithOneOfLeafsShouldBeChangedSuccessfully() throws JSONException {
		initializeStructureSuccessfully(ROOT_KEY);
		addChildSuccessfully(ROOT_KEY, "11");
		addChildSuccessfully("11", "111");
		addChildSuccessfully("11", "112");
		addChildSuccessfully(ROOT_KEY, "12");
		addChildSuccessfully("12", "121");
		addChildSuccessfully("12", "122");

		String newRootKey = "122";
		String oldRootKey = "1";
		changeParentSuccessfully(oldRootKey, newRootKey);
		Response response = getRoot();
		JSONObject root = new JSONObject(response.body().asString());
		validateRootJson(root, newRootKey, 1);
		
		JSONArray rootChildren = root.getJSONArray("children");
		JSONObject p1 = rootChildren.getJSONObject(0);
		validatePositionJsonWithChildren(p1, oldRootKey, newRootKey, 1, newRootKey, 2);
		JSONArray p1Children = p1.getJSONArray("children");
		
		JSONObject p11 = p1Children.getJSONObject(0);
		validatePositionJsonWithChildren(p11, "11", oldRootKey, 2, newRootKey, 2);
		JSONArray p11Children = p11.getJSONArray("children");
		validatePositionJsonWithoutChildren(p11Children.getJSONObject(0), "111", "11", 3, newRootKey);
		validatePositionJsonWithoutChildren(p11Children.getJSONObject(1), "112", "11", 3, newRootKey);
		
		JSONObject p12 = p1Children.getJSONObject(1);
		validatePositionJsonWithChildren(p12, "12", oldRootKey, 2, newRootKey, 1);
		JSONArray p12Children = p12.getJSONArray("children");
		validatePositionJsonWithoutChildren(p12Children.getJSONObject(0), "121", "12", 3, newRootKey);
	}
	
	@Test
	public void testChangeParentOfNodeWithOneOfItsChildren() throws JSONException {
		initializeStructureSuccessfully(ROOT_KEY);
		addChildSuccessfully(ROOT_KEY, "11");
		addChildSuccessfully("11", "111");
		addChildSuccessfully("11", "112");
		addChildSuccessfully(ROOT_KEY, "12");
		addChildSuccessfully("12", "121");
		addChildSuccessfully("12", "122");

		String newParentKey = "122";
		String childKey = "12";
		changeParentSuccessfully(childKey, newParentKey);
		Response response = getRoot();
		JSONObject root = new JSONObject(response.body().asString());
		validateRootJson(root, ROOT_KEY, 2);
		JSONArray rootChildren = root.getJSONArray("children");
		
		JSONObject p11 = rootChildren.getJSONObject(0);
		validatePositionJsonWithChildren(p11, "11", ROOT_KEY, 1, ROOT_KEY, 2);
		JSONArray p11Children = p11.getJSONArray("children");
		validatePositionJsonWithoutChildren(p11Children.getJSONObject(0), "111", "11", 2, ROOT_KEY);
		validatePositionJsonWithoutChildren(p11Children.getJSONObject(1), "112", "11", 2, ROOT_KEY);
		
		JSONObject p122 = rootChildren.getJSONObject(1);
		validatePositionJsonWithChildren(p122, "122", ROOT_KEY, 1, ROOT_KEY, 1);
		JSONArray p122Children = p122.getJSONArray("children");
		JSONObject p12 = p122Children.getJSONObject(0);
		validatePositionJsonWithChildren(p12, childKey, newParentKey, 2, ROOT_KEY, 1);
		
		JSONArray p12Children = p12.getJSONArray("children");
		JSONObject p121 = p12Children.getJSONObject(0);
		validatePositionJsonWithoutChildren(p121, "121", childKey, 3, ROOT_KEY);
	}
	
	@Test
	public void testChangeParentOfNodeWithOnTheSameLevel() throws JSONException {
		initializeStructureSuccessfully(ROOT_KEY);
		addChildSuccessfully(ROOT_KEY, "11");
		addChildSuccessfully("11", "111");
		addChildSuccessfully("11", "112");
		addChildSuccessfully(ROOT_KEY, "12");
		addChildSuccessfully("12", "121");
		addChildSuccessfully("12", "122");

		String newParentKey = "11";
		String childKey = "122";
		changeParentSuccessfully(childKey, newParentKey);
		Response response = getRoot();
		JSONObject root = new JSONObject(response.body().asString());
		validateRootJson(root, ROOT_KEY, 2);
		JSONArray rootChildren = root.getJSONArray("children");
		
		JSONObject p11 = rootChildren.getJSONObject(0);
		validatePositionJsonWithChildren(p11, "11", ROOT_KEY, 1, ROOT_KEY, 3);
		JSONArray p11Children = p11.getJSONArray("children");
		validatePositionJsonWithoutChildren(p11Children.getJSONObject(0), "111", "11", 2, ROOT_KEY);
		validatePositionJsonWithoutChildren(p11Children.getJSONObject(1), "122", "11", 2, ROOT_KEY);
		validatePositionJsonWithoutChildren(p11Children.getJSONObject(2), "112", "11", 2, ROOT_KEY);
		
		JSONObject p12 = rootChildren.getJSONObject(1);
		validatePositionJsonWithChildren(p12, "12", ROOT_KEY, 1, ROOT_KEY, 1);
		
		JSONArray p12Children = p12.getJSONArray("children");
		JSONObject p121 = p12Children.getJSONObject(0);
		validatePositionJsonWithoutChildren(p121, "121", "12", 2, ROOT_KEY);
	}
	
	@Test
	public void testChangeParentOfNodeToBeOneOfLeafs() throws JSONException {
		initializeStructureSuccessfully(ROOT_KEY);
		addChildSuccessfully(ROOT_KEY, "11");
		addChildSuccessfully("11", "111");
		addChildSuccessfully("11", "112");
		addChildSuccessfully(ROOT_KEY, "12");
		addChildSuccessfully("12", "121");
		addChildSuccessfully("12", "122");

		String newParentKey = "112";
		String childKey = "12";
		changeParentSuccessfully(childKey, newParentKey);
		Response response = getRoot();
		JSONObject root = new JSONObject(response.body().asString());
		validateRootJson(root, ROOT_KEY, 1);
		JSONArray rootChildren = root.getJSONArray("children");
		
		JSONObject p11 = rootChildren.getJSONObject(0);
		validatePositionJsonWithChildren(p11, "11", ROOT_KEY, 1, ROOT_KEY, 2);
		JSONArray p11Children = p11.getJSONArray("children");
		validatePositionJsonWithoutChildren(p11Children.getJSONObject(0), "111", "11", 2, ROOT_KEY);
		
		JSONObject newParent = p11Children.getJSONObject(1);
		validatePositionJsonWithChildren(newParent, newParentKey, "11", 2, ROOT_KEY, 1);
		JSONArray newParentChildren = newParent.getJSONArray("children");
		
		
		JSONObject p12 = newParentChildren.getJSONObject(0);
		validatePositionJsonWithChildren(p12, "12", newParentKey, 3, ROOT_KEY, 2);
		JSONArray p12Children = p12.getJSONArray("children");
		validatePositionJsonWithoutChildren(p12Children.getJSONObject(0), "121", "12", 4, ROOT_KEY);
		validatePositionJsonWithoutChildren(p12Children.getJSONObject(1), "122", "12", 4, ROOT_KEY);
		
	}
}
