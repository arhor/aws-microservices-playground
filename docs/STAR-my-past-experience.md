# Getting through the interview process using STAR technique

The STAR framework is a structured method for answering behavioural interview questions, which are questions that ask
you to provide specific examples of past experiences to demonstrate your skills and abilities.

STAR stands for _Situation_, _Task_, _Action_, and _Result_:

* **Situation** - Describe the situation and when it took place.
* **Task** - Explain the task and what was the goal.
* **Action** - Provide details about the action you took to attain this.
* **Result** - Conclude with the result of your action.

------------------------------------------------------------------------------------------------------------------------

## Tell me about your last project? (FreshDirect)

**Situation**

```text
During my last project, the client was developing a fresh UI for their e-commerce service, utilizing capabilities
of the SPA. Additionally, there was a specific requirement to utilize GraphQL instead of a traditional REST API.
It is important to note that there was no existing public API available, as the client's existing application was
built using SSR (Server-Side Rendering) technology.
```

**Task**

```text
Our team was given the objective of developing a GraphQL API to provide a modern and convenient data access
layer while maximizing the reuse of existing business logic.
```

**Action**

```text
Starting with a deep analysis of the existing codebase, we proceeded with step by step implementation of the
GQL API providing the essential functionalities utilized by the application. Server-side caching was also used in order
to increase the performance of the API, since some operations required a significant amount of time to complete.
```

**Result**

```text
As a result of our efforts, we achieved a complete replication of the existing API, making it accessible for
GraphQL queries. This smooth transition allowed the client to migrate their service seamlessly to a modern UI while
preserving the full user experience. Additionally, the implementation of backend caching significantly improved the
efficiency of long-running requests, resulting in an enhanced overall experience when interacting with the API.
```

------------------------------------------------------------------------------------------------------------------------

## Tax calculation service refactoring

**Situation**

```text
I was working on a project in a FinTech company developing software for workforce management. One of the internal 
services responsible for calculating taxes contained a significant amount of legacy code, boilerplate, and was 
challenging to maintain and troubleshoot. Fixing issues and introducing new functionality was time consuming. New team 
members required a significant amount of time to learn how to work with this service.
```

**Task**

```text
I discussed this issue with my team lead and was assigned to the task, consisting of the following parts:

* refactor the existing codebase
* apply possible optimizations without changing the overall semantics

The main goal was to simplify working with the service and reduce the time required for introducing new features and 
fixing existing bugs.
```

**Action**

```text
I've started with functional code decomposition - identified duplicated code fragments and leveraged the modern API
provided by the Java 8, which was used by that service. Additionally, I utilized the capabilities offered by the Spring 
Framework, like simplified DB interaction via Spring-JDBC module, changing embedded server from Tomcat to Undertow,
providing smaller memory footprint.
```

**Result**

```text
Working with the service was significantly simplified. Support tasks became available for the entire team without any 
specific training, eliminating the need for a single developer solely responsible for the service. Through fine-tuning
of the configuration, the average request processing time was reduced by approximately 10%.
```

------------------------------------------------------------------------------------------------------------------------

## 'External tax integration' related code moved to a separate gradle module

**Situation**: Describe the situation and when it took place.

```

```

**Task**: Explain the task and what was the goal.

```

```

**Action**: Provide details about the action you took to attain this.

```

```

**Result**: Conclude with the result of your action.

```

```

------------------------------------------------------------------------------------------------------------------------

### Creating a custom test framework based on Newman (CLI version of Postman)

**Situation**: Describe the situation and when it took place.

```

```

**Task**: Explain the task and what was the goal.

```

```

**Action**: Provide details about the action you took to attain this.

```

```

**Result**: Conclude with the result of your action.

```

```

------------------------------------------------------------------------------------------------------------------------

### Adapting spring-data-redis to a new service in the POC phase

**Situation**: Describe the situation and when it took place.

```

```

**Task**: Explain the task and what was the goal.

```

```

**Action**: Provide details about the action you took to attain this.

```

```

**Result**: Conclude with the result of your action.

```

```

------------------------------------------------------------------------------------------------------------------------

# Shift left

"Shift left" refers to the practice of moving certain activities or tasks earlier in the development process, typically
to detect and address issues earlier and improve overall efficiency and quality.

**Situation**:

```text
An organization may have frequent issues with identifying and fixing defects late in the software
development life cycle, causing delays and higher costs.
```

**Task**:

```text
In this case, the task would be to optimize the development process by implementing a "shift left" approach to
improve defect detection and resolution.
```

**Action**:

```text
Here are some possible actions that might have taken:

- Testing: introducing early testing practices, such as incorporating unit tests and automated testing frameworks,
  allows continuous testing throughout the development process, enabling quicker identification and resolution of
  defects.

- Code reviews: implementing a code review process where developers regularly review each other's code helps identify
  issues and promote coding best practices early, while reducing the chance of defects later.

- Collaboration: close collaboration between development and testing teams from the early stages involving regular
  meetings and discussions to address any concerns or questions, ensuring alignment and shared understanding of
  requirements and design.
```

**Result**:

```text
- Defect detection: with the implementation of "shift left" practices, defects were identified and addressed much
  earlier in the development process. This resulted in significant reductions in the number of defects discovered during
  later stages, leading to smoother and faster development cycles.

- Cost and time savings: by catching and resolving issues earlier, the organization experiencing cost and time
  savings.

- Quality improvement: The "shift left" practice contributing to improved software quality. By addressing issues
  earlier, the final products had fewer defects, resulting in higher customer satisfaction and reduced support and
  maintenance efforts.
```

------------------------------------------------------------------------------------------------------------------------

* describe failure
* describe success
* my achievements
* great success
* great failure
* conflict
* customer communication
* high-level project overview

Tell me about a challenge you faced in your last project.
Give me an example of most interesting tasks you have worked on.
Tell me about the time when the timeline you were given was not achievable and how you manage that.
Tell me about the previous mistake you made and what you learned from it.

How to avoid mistakes?

* prefer using "I" instead of "We", since client examining you, and not your team
* do not tell customers name as it could be a part of NDA
* omit framework names
* go from high to details: high overview -> more detailed -> details on concrete point (should I go into details?)
* use 4-8 sentences to answer
