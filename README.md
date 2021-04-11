# Getting Started with this code

We require that all teams do their work for CS3733 Projects on a private repository in a Github Organization. 
In order to turn this code into such a repository, please follow these instructions.

https://docs.github.com/en/github/creating-cloning-and-archiving-repositories/duplicating-a-repository 


# Important Files
## build.gradle / gradle.properties
This is the gradle configuration file. Modify this file to add dependencies to your project. In
 general you should only modify the depedencies section of this file, however there are a few
  modification you will need to make when you begin the project
  
  - `mainClassName`
    - Modifiy this variable to point to your main class. By default it is `edu.wpi.teamo.Main
    `, but once you update your teamname package to your team letter you will need to update this
     path.



## .gitignore
This file tells git which files to ignore in the repo. It should be correctly configured already
**Only modify this file if you are sure you know what you are doing**


## resources/edu/wpi/teamname/fxml
Here you will find the Fxml files for the default scenes. This method of accessing resources is how we recommend you organize all your FXML for the project.

# Building a jar
To assemble a jar file for your project, run the "jar" gradle task, either through IntelliJ or by doing
`gradle jar` on a terminal. Gradle will automatically download all dependencies needed to compile your jar file,
which will be stored in the build/libs folder.

Make sure to edit the main class attribute the build.gradle file, you'll need to change it in order to obtain
a working jar file.





