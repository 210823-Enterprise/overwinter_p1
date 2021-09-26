# Overwinter
## Project Description
A java based ORM framework for simplifying connecting to and pulling from an SQL database without the need for SQL or connection management. 

## Technologies Used

* PostgreSQL - version 42.2.18  
* Java - version 8.0  
* Apache commons - version 2.1  
* commons-dbcp 1.4

## Features

List of features ready and TODOs for future development  
* Easy to use and straightforward user API.  
* No need for SQL, HQL, or any databse specific language.  
* Straightforward and simple Annotation based for ease of use. 

To-do list: [`for future iterations`]
* Mapping of join columns inside of entities.    
* Implement of aggregate functions.   
* Include LIKE in possible conditions
* fix application.properities finding bug with mac OS

## Getting Started  
Currently project must be included as local dependency. to do so:
```shell
  git clone https://github.com/210823-Enterprise/overwinter_p1.git
  cd overwinter_p1
  mvn install
```
Next, place the following inside your project pom.xml file:
```XML
  <dependency>
    <groupId>com.overwinter</groupId>
    <artifactId>overwinter_p1</artifactId>
    <version>0.0.1-SNAPSHOT</version>
	</dependency>
```

Finally, inside your project structure you need a application.proprties file. 
 (typically located src/main/resources/)
 ``` 
  url=path/to/database
  admin-usr=username/of/database
  admin-pw=password/of/database  
  ```
  
## Usage  
  ### Annotating classes  
  All classes which represent objects in database must be annotated.
   - #### @Entity(tableName = "table_name")  
      - Indicates that this class is associated with table 'table_name'  
   - #### @Column(columnName = "column_name")  
      - Indicates that the Annotated field is a column in the table with the name 'column_name'
   - #### @Setter(name = "column_name")
      - Indicates that the anotated method is a setter for 'column_name'.  
   - #### @Getter(name = "column_name")
      - Indicates that the anotated method is a getter for 'column_name'.  
   - #### @Id(name = "column_name")
      - Indicates that the annotated field is the primary key for the table.
   - #### @SerialKey(name = "column_name")(future)
      - Indicates that the annotated field is a serial key.

  ### User API
  - #### `public void printDbStatus()`  
     - print the current stats for connection pool
  - #### `public static OverWinterORM getInstance()`  
     - returns the singleton instance of the class. It is the starting point to calling any of the below methods.  
  - #### `public HashSet<Object> getCache(Class<?> clazz)`  
     - returns cache of requested class as a HashSet.  
  - #### `public boolean addTabletoDb(Class<?> clazz)`  
     - Adds a class to the ORM. This is the method to use to declare a Class is an object inside of the database. This will create the table within the database given in application.properties
  - #### `public boolean updateObjFromDB(final Object obj)`  
     - Updates the given object in the databse. Give this the completed object to be sent to the database
  - #### `public boolean deleteObjFromDB(final Object obj)`  
     - Removes the given object from the database.  
  - #### `public boolean insertObjectIntoDB(final Object obj)`  
     - Adds the given object to the database.  
  - #### `public Object getObjectFromDB(Class<?> clazz,int id)` 
      - Gets a object in the database which match the included id  
  - #### `public Optional<List<Object>> getListObjectFromDB(Object obj)`
  - #### `public Optional<List<Object>> getListObjectFromDB(final Class <?> clazz, final String columns, final String conditions)`
  - #### `public Object getObjectFromDB(Class<?> clazz,int id)`  
  - #### `public Optional<List<Object>> getListObjectFromDB(final Class <?> clazz, final String columns, final String conditions,final String operators)`  
  - #### `public Optional<List<Object>> getListObjectFromDB(final Class <?> clazz, final String columns, final String conditions,final String operators)`  
     - Gets a list of all objects in the database which match the included search criteria  
        - columns - comma seperated list of columns to search by.  
        - conditions - coma seperated list the values the columns should match to.  
        - operators(future implementation currently all AND) - comma seperated list of operators to apply to columns (AND/OR) in order that they should be applied.  
  - #### `public void commit()`  
     - begin databse commit.  
  - #### `public void rollBack()`  
     - Rollback to previous commit.  
  - #### `public void rollBackWithSpecificSavePoint(Savepoint savepoint)`  
     - Rollback to previous commit with savepoint. 
  - #### `public void setSavePointWithName(String name)`  
     - Set a savepoint with the given name.   
  - #### `public void setSavePointWithName(String name)`  
     - Set a savepoint with the given name.  
  - #### `public void enableAutoCommit()`  
     - Enable auto commits on the database.  
  - #### `public void beginTransaction()`
     - Start a transaction block.  
  - #### `public void addAllFromDBToCache(final Class<?> clazz)`  
     - Adds all objects currently in the databse of the given clas type to the cache.  
  - #### `public HashMap<Class<?>, HashSet<Object>> putObjectInCache(Object obj)`
     - Update/Add a single object to cache   
  - #### `public Connection getConn()`
     - get current connection  
  - #### `public void setConn(Connection connection)`
     - set a custom connection
## License

This project uses the following license: [GNU Public License 3.0](https://www.gnu.org/licenses/gpl-3.0.en.html).
