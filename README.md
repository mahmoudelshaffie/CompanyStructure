# CompanyStructure

#Build Instructions
{project.basedir}/mvn install
{project.basedir}/docker-compose build


#APIs
- Concrete example documented in file Company Structure.postman_collection.json


- Initialize Strucutre with Root: POST http://localhost:8080/api/structure?root=1
- Add Child: POST http://localhost:8080/api/structure/{parent-key}/children?child={child-key}
- Change Parent:  PUT http://localhost:8080/api/structure/{child-key}/parent?newParent={newparent-key}
- Get Children: GET http://localhost:8080/api/structure/{parent-key}/children