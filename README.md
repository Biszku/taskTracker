# Task Tracker

## Description

This is a simple Command Line Interface (CLI) application that 
allows users to create, read, update, delete and list tasks.

## Features

- Add a task
- Update a task
- Delete a task
- Mark a task as either done or in-progress
- List all tasks or tasks by status

## Installation

1. Clone the repository
```bash
git clone https://github.com/Biszku/taskTracker
cd taskTracker
```

2. Compile the source code:
```bash
javac -d out .\src\main\java\com\biszku\taskTracker\*.java
```

3. Run the application:
```bash
java -cp out main.java.com.biszku.taskTracker.TasksTracker
```

## Usage

```bash
#add a task
Example input: task-cli add "Task description"
#output: Task added successfully (ID: 1)

#update a task
Example input: #output: Task updated successfully (ID: 1)

#delete a task
Example input: task-cli delete 1
#output: Task deleted successfully (ID: 1)

#mark a task as in-progress
Example input: task-cli mark-in-progress 1
#output: Task marked as in-progress (ID: 1)

#mark a task as done
Example input: task-cli mark-done 1
#output: Task marked as done (ID: 1)

#list all tasks
Example input: task-cli list
#output:
#ID: 1, Description: Task description, Status: Status, Created: Date, Updated: Date
#...

#list tasks by status
Example input: task-cli list [status]
#output:
#ID: 1, Description: Task description, Status: [status], Created: Date, Updated: Date
#...

#close the application
Example input: task-cli exit
#output: Closing application...
```
