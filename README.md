# Code review Java
This repository contains a small Java Spring Boot project that is used within our job application process.
Applicants may have the task to prepare a review of this project in order to find out if the applicant has the same understanding of software engineering as we do.
The code in this repository contains bad practices and other flaws that have been added intentionally. Therefore this project is not a blueprint that should be used for productive applications.

## Usage
Applicants are asked to review the project in this repository as a preparation task for an upcoming job interview. The goal is to discuss everything that the applicant likes or dislikes about the code, potential bugs or problems as well as bad practices that have been spotted. It's up to the applicant how to present the results.

## Maintainers
| Name              |
| :---------------- |
| Timo Appenzeller  |

# After the code review and refactor - code-refactoring
In this code review I have tried to include **good practices** and **code refactoring for optimization**.

On the one hand, I have updated the code in a way that I feel it may be more **self explanatory** when reading it, adding **comments** where I felt they could be **necessary**, also, for this task, I have added comments refering to what I have changed in the code compared to what it was before.

On the other hand, with the refactor, I have tried to make a more optimized code, using functions with **better performance** on some tasks (i.e. stream(), BigDecimal.compareTo()...). Also, **removing** and **adding** **annotations** where I felt they could or not be needed, as not using **@Data** Lombok annotation in a **@Entity** and adding just **@Getter** and **@Setter** Lombok annotations.

Other of the tasks was trying to stick to the **SOLID principles**, but as I see it, the project already had a good structure, delegating tasks to different layers **(Single Responsability principle)**. Using **DTOs** and **Repositories** improves this goal since we are respecting **Open-Closed principle** as we can extend API needs without needing to modify entities, and **Dependency Inversion**, since Repositories make sure the code depends on **abstractions** and not concrete implementations.

Finally, I also tried to add more **security/integrity** to the solution by making each DTO **immutable**, so that the data remains intact from point to point.
