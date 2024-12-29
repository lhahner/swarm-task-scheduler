# Summary Cloudsim schedeuling

- Take a look at complexity classes like NP-hard problems.
- The scheduler is the core component and is responsible for
allocating tasks to resources using the optimization schedul-
ing method, which this paper proposes, and schedules tasks
to resources according to this information. First, it collects
this task and resource information from the task manager
and the global resource manager. Second, it judges whether
the resource Rj meets the requirement of the task Ti. The
requirements include deadline, cost and include the actual
requirements.
Finally, the scheduler allocates the resource Rj to the
task Ti.
- First, input the number of tasks,
the task deadline and budget costs, the number of resources,
their ability, and other relevant parameters.
- Each task is assigned an ant. When the task T is successfully
allocated to resource R task T will be recorded through
the taboo table. Third, the above steps are repeated for the
next task, which is not in the taboo table until all tasks
are scheduled completely.