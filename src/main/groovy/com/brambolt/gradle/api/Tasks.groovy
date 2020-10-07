package com.brambolt.gradle.api

import org.gradle.api.Project
import org.gradle.api.Task

class Tasks {

  @Deprecated
  static Task createSubTask(Task mainTask, Class taskClass, String nameSuffix = '') {
    createTask(mainTask.project, taskClass, nameSuffix)
  }

  static Task createTask(Project project, Class taskClass, String nameSuffix) {
    String taskName = createTaskName(taskClass, nameSuffix)
    project.logger.debug("Creating $taskName...")
    createTaskIfMissing(project, taskName, taskClass)
  }

  static Task createTaskIfMissing(Project project, String name, Class<?> taskClass) {
    Task task = project.tasks.findByName(name)
    if (null == task)
      task = project.task([type: taskClass], name)
    task
  }

  static Task createTaskIfMissing(Project project, String name, Class<?> taskClass, Closure closure) {
    Task task = project.tasks.findByName(name)
    if (null == task)
      task = project.task([type: taskClass], name, closure)
    task.configure(closure)
  }

  static String createTaskName(Class taskClass, String suffix = '') {
    String className = taskClass.getSimpleName()
    className.substring(0, 1).toLowerCase() + className.substring(1) + suffix
  }
}
