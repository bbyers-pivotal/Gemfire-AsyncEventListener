# Gemfire-AsyncEventListener

This project helps serve as a reference for building an application that uses Gemfire and AsyncEventListeners. This application has 2 endpoints
* `POST /somefilename` which will create a key/value entry in Gemfire with a key name of `somefilename` and value of the `bacon.txt` file
* `GET /somefilename` which will return the text from the `bacon.txt` file that was stored in Gemfire under the key `somefilename`

#### Install Gemfire
* `$ brew tap pivotal/tap`
* `$ brew install gemfire`

#### Start Gemfire
* Launch the client `$ gfsh`
* Start a locator `$ start locator --name=locator --port=10334`
* Start a server `$ start server --name=server1 --locators=localhost[10334]`

#### Building the Gemfire AsyncEventListener jar
There are 2 files needed. `FileContents.class` and `FileSavedAsyncEventListener.class`

Let's create a folder and copy them in, then create a jar
* `$ mkdir myjar && cd myjar`
* `$ mkdir com && cd com`
* `$ mkdir example && cd example`
* `$ cp ~/thisproject/target/classes/com/example/FileContents.class .`
* `$ cd ../../`
* `$ mkdir listener && cd listener`
* `$ mkdir gemfire && cd gemfire`
* `$ cp ~/thisproject/target/classes/listener/gemfire/FileSavedAsyncEventListener.class .`
* `$ cd ../../`
* `$ jar cf mylistener.jar .`

#### Deploying to Gemfire
Go back to the `gfsh` client opened to start Gemfire
* `$ deploy --jar=/where/my/jar/is/mylistener.jar`

Bounce the server and locator
* `$ stop server --name=server1`
* `$ stop locator --name=locator`
* `$ start locator --name=locator --port=10334`
* `$ start server --name=server1 --locators=localhost[10334]`

Create AsyncEventQueue in Gemfire
* `create async-event-queue --id=myQueueId --listener=listener.gemfire.FileSavedAsyncEventListener --listener-param=path#/where/I/want/to/save/files/`



#### Building the Spring Boot Client app
